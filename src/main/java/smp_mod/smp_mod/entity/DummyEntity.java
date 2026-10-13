package smp_mod.smp_mod.entity;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import smp_mod.smp_mod.ModEntities;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.PlayerData;
import smp_mod.smp_mod.SaveData.ServerData;
import smp_mod.smp_mod.sync.SyncHeartsPayload;

import java.lang.reflect.Array;
import java.util.*;

public class DummyEntity extends PathfinderMob {

    private final List<ItemStack> inventoryAsArray = new ArrayList<>();
    public GameProfile gameProfile;
    public UUID playerUUID;
    public int timeInCombat;
    public UUID killerUUID;


    public DummyEntity(EntityType<DummyEntity> type, Level level){
        super(type,level);
        this.setNoAi(true);
        this.timeInCombat = 0;
        this.playerUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
    public void syncWithPlayer(Player player){


        System.out.println("syncing with player");

        PlayerData playerData = player.getAttached(SMP_Mod.PLAYER_DATA);

        this.timeInCombat = playerData.inFight;
        this.playerUUID = player.getUUID();
        this.setPos(player.getPosition(1.0F));
        this.setItemSlot(EquipmentSlot.LEGS,player.getItemBySlot(EquipmentSlot.LEGS));
        this.setItemSlot(EquipmentSlot.FEET,player.getItemBySlot(EquipmentSlot.FEET));
        this.setItemSlot(EquipmentSlot.OFFHAND,player.getItemBySlot(EquipmentSlot.OFFHAND));
        this.setItemSlot(EquipmentSlot.CHEST,player.getItemBySlot(EquipmentSlot.CHEST));
        this.setItemSlot(EquipmentSlot.HEAD,player.getItemBySlot(EquipmentSlot.HEAD));
        for(int i = 0; i<36;i++){
            this.inventoryAsArray.add(player.getInventory().getItem(i));
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getMaxHealth());
        this.gameProfile = player.getGameProfile();
        Collection<MobEffectInstance> statusEffects = player.getActiveEffects();
        for(MobEffectInstance effect:statusEffects){
            this.addEffect(effect);
        }
        this.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, MobEffectInstance.INFINITE_DURATION, 2, false,false));
        this.setCustomName(player.getName());
        this.setPos(player.getPosition(1.0F));
        this.setHealth(player.getHealth());
        this.playerUUID = player.getUUID();
        this.killerUUID = UUID.fromString(playerData.gotHitByUUID);


    }
    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH,20)
                .add(Attributes.KNOCKBACK_RESISTANCE,1);
    }
    @Override
    public void die(final DamageSource source) {
        super.die(source);
        if(this.timeInCombat != 0 && !source.is(DamageTypes.GENERIC_KILL)){
            Level level = this.level();


            ServerData serverData = level.getAttached(SMP_Mod.SERVER_DATA);
            if (serverData == null) {
                serverData = new ServerData();
                level.setAttached(SMP_Mod.SERVER_DATA, serverData);
            }
            List<String> newPlayersToKill = new ArrayList<>(serverData.playersToKill);


            if (this.playerUUID != null) {
                System.out.println("PlayerUUID in die: " + this.playerUUID.toString());
            }

            newPlayersToKill.add(this.playerUUID.toString());
            level.setAttached(
                    SMP_Mod.SERVER_DATA,
                    new ServerData(
                            newPlayersToKill,
                            serverData.playersAndDummys,
                            serverData.carriedAndCarryers,
                            serverData.blockCarriers,
                            serverData.webHookURL,
                            serverData.allowTotems
                    )
            );
            GraveEntity GRAVE = new GraveEntity(ModEntities.GRAVE, this.level());
            GRAVE.setCustomName(null);
            Vec3 pos = this.getPosition(1.0F);
            GRAVE.owner = this.playerUUID;
            GRAVE.killer = this.killerUUID;

            UUID playerUUID = this.playerUUID;
            ServerLevel serverLevel = (ServerLevel) this.level();
            Set<ResourceKey<Level>> worldIdentifier = (serverLevel).getServer().levelKeys();


            if(pos.y>-65) {
                GRAVE.setPos(pos);
            }else{
                GRAVE.setPos(new Vec3(pos.x,-60.0,pos.z));
            }
            GRAVE.setNoGravity(true);
            this.level().addFreshEntity(GRAVE);


            List<ItemStack> inventory = this.inventoryAsArray;



            inventory.forEach(itemStack -> {

                GRAVE.addItemStack(itemStack);
            });

            serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.literal(GRAVE.killer + " took a heart from " + GRAVE.owner + ". "), true);






            //for(int i = 0;i<equipment.length;i++){
            //     GRAVE.addItemStack(player.getItemBySlot(equipment[i]));
            //}; inventory includes equipment
            this.inventoryAsArray.clear();

        }

    }

    @Override
    public void tick(){
        super.tick();

        if (!this.level().isClientSide()) {


            if (this.tickCount % 20 == 0 && this.timeInCombat > 0) {
                this.timeInCombat--;
            }
            if (this.timeInCombat <= 0 && this.isAlive()) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    this.kill(serverLevel);
                }
            }
        }
    }

}
