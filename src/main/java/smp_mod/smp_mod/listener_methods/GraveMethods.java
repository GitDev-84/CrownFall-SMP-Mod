package smp_mod.smp_mod.listener_methods;


import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import smp_mod.smp_mod.ModEntities;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.PlayerData;
import smp_mod.smp_mod.SaveData.ServerData;
import smp_mod.smp_mod.entity.GraveChestMenu;
import smp_mod.smp_mod.entity.GraveEntity;
import net.minecraft.world.item.Items;
import smp_mod.smp_mod.sync.SyncHeartsPayload;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class GraveMethods {


    public GraveMethods(){}





    public static void spawnGrave(ServerPlayer player, DamageSource damageSource, float damageAmount){


        boolean isDuelModLoaded = FabricLoader.getInstance().isModLoaded("simpleduels");

        boolean deadPlayerIsInDuel = false;


        if(isDuelModLoaded){
            try {
                Class<?> attachmentsClass = Class.forName("net.ben.simpleduels.SaveData.ModAttachments");

                java.lang.reflect.Field field = attachmentsClass.getField("PLAYER_DATA");

                var attachmentType = (net.fabricmc.fabric.api.attachment.v1.AttachmentType<?>) field.get(null);

                Object playerData = player.getAttached(attachmentType);

                if (playerData != null) {
                    java.lang.reflect.Field inDuelField = playerData.getClass().getField("inActiveDuel");
                    boolean inDuel = inDuelField.getBoolean(playerData);

                    if (inDuel) {

                        deadPlayerIsInDuel = true;
                        SMP_Mod.LOGGER.info("dead player is in duel");


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }






        GraveEntity GRAVE = new GraveEntity(ModEntities.GRAVE, player.level());
        GRAVE.setCustomName(null);
        Vec3 pos = player.getPosition(1.0F);
        PlayerData playerData = player.getAttached(SMP_Mod.PLAYER_DATA);
        System.out.println("line before setting owner");
        GRAVE.owner = player.getUUID();


        if (playerData == null) {
            playerData = new PlayerData();
            player.setAttached(SMP_Mod.PLAYER_DATA, playerData);
        }

        UUID playerUUID = player.getUUID();
        ServerLevel serverLevel = (ServerLevel) player.level();
        Set<ResourceKey<Level>> worldIdentifier = (serverLevel).getServer().levelKeys();


        if(pos.y>-65) {
            GRAVE.setPos(pos);
        }else{
            GRAVE.setPos(new Vec3(pos.x,-60.0,pos.z));
        }
        GRAVE.setNoGravity(true);
        player.level().addFreshEntity(GRAVE);

        int newHearts = playerData.hearts;

        if(playerData.inFight >0 && !deadPlayerIsInDuel){
            GRAVE.owner = player.getUUID();
            GRAVE.killer = UUID.fromString( playerData.gotHitByUUID);

            Entity killer = serverLevel.getEntity( UUID.fromString( playerData.gotHitByUUID));

            if(killer instanceof  ServerPlayer playerKiller){
                PlayerData playerDataKiller = playerKiller.getAttached(SMP_Mod.PLAYER_DATA);
                if(playerDataKiller == null){
                    playerDataKiller = new PlayerData();
                }
                playerKiller.setAttached(SMP_Mod.PLAYER_DATA,new PlayerData(playerDataKiller.hearts,0,32, playerDataKiller.gotHitByUUID));
                ServerPlayNetworking.send(playerKiller, new SyncHeartsPayload(playerKiller.getAttached(SMP_Mod.PLAYER_DATA)));
                if(playerData.hearts >0){
                    newHearts--;
                    serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.literal(player.getName().toString() + " lost one of their hearts to " + playerKiller.getName().getString() + "!"), false);


                }
            }

        }
        if(newHearts == 0){
            player.sendSystemMessage(Component.literal("You lost all of your hearts"));
            serverLevel.getServer().getPlayerList().broadcastSystemMessage(Component.literal(player.getName().toString() + " lost all of their hearts!"), false);
            serverLevel.getServer().getPlayerList().getBans().add(new UserBanListEntry(new NameAndId(player.getGameProfile())));
            player.connection.disconnect(Component.literal("You lost all of your hearts"));
        }
        System.out.println("spawnGrave ausgefuehrt");


        player.setAttached(SMP_Mod.PLAYER_DATA, new PlayerData(newHearts,0, 32,""));




        Inventory inventory = player.getInventory();



        inventory.forEach(itemStack -> {

            GRAVE.addItemStack(itemStack);
        });





        //for(int i = 0;i<equipment.length;i++){
        //     GRAVE.addItemStack(player.getItemBySlot(equipment[i]));
        //}; inventory includes equipment

        int xp = player.totalExperience;
        System.out.println("Player xp after death: " + xp);
        GRAVE.setXp(xp);
        System.out.println("grave xp after setting: " + GRAVE.getXp());

        inventory.clearContent();
        player.experienceLevel = 0;
        player.experienceProgress = 0.0F;
        player.totalExperience = 0;
        player.connection.send(new ClientboundSetExperiencePacket(0.0F, 0, 0));
        ServerPlayNetworking.send(player, new SyncHeartsPayload(player.getAttached(SMP_Mod.PLAYER_DATA)));

        player.setAttached(SMP_Mod.PLAYER_DATA,new PlayerData(
                newHearts,
                0,
                32,
                ""
                ));

    }


    public static void useTotem(ServerPlayer player, InteractionHand hand){

        ServerLevel serverLevel = (ServerLevel) player.level();

        player.connection.send(new ClientboundEntityEventPacket(player, (byte) 35));
        serverLevel.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.TOTEM_USE,
                SoundSource.PLAYERS,
                1.0F, 1.0F
        );
        player.setHealth(0.5F);
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.setAbsorptionAmount(8.0F);
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));

        player.getItemInHand(hand).consume(1,player);

    }


    public static boolean totemCheck(ServerPlayer player, DamageSource damageSource, float damageAmount, Runnable callback) {

        ServerLevel overworld = player.level().getServer().overworld();

        ServerData serverData = overworld.getAttached(SMP_Mod.SERVER_DATA);
        if(serverData == null){
            serverData = new ServerData();
            overworld.setAttached(SMP_Mod.SERVER_DATA, serverData);
        }
        if(!serverData.allowTotems){
            callback.run();
            return true;
        }

        ItemStack mainHand =  player.getMainHandItem();

        ItemStack secHand = player.getItemBySlot(EquipmentSlot.OFFHAND);


        if(mainHand.getItem() == Items.TOTEM_OF_UNDYING){
            GraveMethods.useTotem(player,InteractionHand.MAIN_HAND);
            return false;
        }else if(secHand.getItem() == Items.TOTEM_OF_UNDYING){
            GraveMethods.useTotem(player,InteractionHand.OFF_HAND);




            return false;
        }else{
            callback.run();
        }
        return true;
    }
    public static InteractionResult openGraveMenu(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult){
        if(entity.getType() == ModEntities.GRAVE) {
            if (!level.isClientSide()) {
                //server code
                GraveEntity grave = (GraveEntity) entity;
                System.out.println(player.getUUID());
                System.out.println(grave.owner);
                if(player.getUUID().equals(grave.owner) || player.getUUID().equals(grave.killer)) {
                    player.openMenu(new SimpleMenuProvider((syncId, playerInventory, playerEntity) -> {
                        return new GraveChestMenu(syncId, playerInventory, grave.inventory, grave);
                    }, Component.literal("GraveChest")));
                }

            } else {
                //client code
            }
            return InteractionResult.SUCCESS;

        }else{
            return InteractionResult.PASS;
        }
        }

}