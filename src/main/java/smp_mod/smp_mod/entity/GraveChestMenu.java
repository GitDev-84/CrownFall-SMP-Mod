package smp_mod.smp_mod.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GraveChestMenu extends ChestMenu {

    public final GraveEntity grave;

    public GraveChestMenu(int containerId, Inventory inventory, Container container, GraveEntity graveEntity) {
        super(MenuType.GENERIC_9x6, containerId, inventory, container, 6);
        this.grave = graveEntity;
        ItemStack hopper = new ItemStack(Items.HOPPER);
        hopper.set(DataComponents.CUSTOM_NAME, Component.literal("Click to get all your items."));

        ItemStack barrier = new ItemStack(Items.BARRIER);
        barrier.set(DataComponents.CUSTOM_NAME, Component.literal("Click to remove the grave."));



        container.setItem(50,hopper);
        container.setItem(48,barrier);

    }

    @Override
    public void clicked(final int slotIndex, final int buttonNum, final ContainerInput containerInput, final Player player){
        if(slotIndex == 50){
            Inventory playerInv = player.getInventory();
            if(!player.level().isClientSide()) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                System.out.println("Xp: "+ grave.getXp());
                player.giveExperiencePoints(grave.getXp());

                serverPlayer.connection.send(new ClientboundSetExperiencePacket(
                        player.experienceProgress,
                        player.totalExperience,
                        player.experienceLevel
                ));
            }

            for(int i = 0;i<54;i++){


                if (playerInv.add(this.getSlot(i).getItem()) == false) {
                    this.grave.inventory.setChanged();
                    return;
                }

                }
            this.grave.inventory.setChanged();
            return;
        }else if(slotIndex == 48){
            for(int i = 0; i<54;i++) {
                Item itemOfSlot = this.getSlot(i).getItem().getItem();
                if(i != 48 && i != 50 )this.grave.drop(this.getSlot(i).getItem(),true, Prediction.PREDICTED);

            }
            if(!player.level().isClientSide()) {
                grave.kill((ServerLevel) player.level());
                grave.discard();
            }


            this.grave.inventory.setChanged();
            return;
        }
        super.clicked(slotIndex,buttonNum,containerInput,player);
    }

}
//hier auch andere menutypes probieren