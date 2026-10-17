package smp_mod.smp_mod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smp_mod.smp_mod.SaveData.PlayerData;
import smp_mod.smp_mod.SaveData.ServerData;
import smp_mod.smp_mod.commands.*;
import smp_mod.smp_mod.entity.DummyEntity;
import smp_mod.smp_mod.entity.GraveEntity;
import smp_mod.smp_mod.listener_methods.GraveMethods;

import java.util.*;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import smp_mod.smp_mod.networking.ModPackets;
import smp_mod.smp_mod.sync.SyncHeartsPayload;


public class SMP_Mod implements ModInitializer {
	public static final String MOD_ID = "smp_mod";


	public static final AttachmentType<PlayerData> PLAYER_DATA = AttachmentRegistry.create(
			Identifier.fromNamespaceAndPath(MOD_ID, "player_data"),
			builder -> builder
					.initializer(PlayerData::new)
					.persistent(PlayerData.CODEC)
					.copyOnDeath()
	);

	public static final AttachmentType<ServerData> SERVER_DATA = AttachmentRegistry.create(
			Identifier.fromNamespaceAndPath(MOD_ID, "server_data"),
			builder -> builder
					.initializer(ServerData::new)
					.persistent(ServerData.CODEC)
	);

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean itemMod = false;
	@Override
	public void onInitialize() {

		ModPackets.registerPackets();

		SMP_Mod.itemMod = FabricLoader.getInstance().isModLoaded("vanillaish-custom-items");

		PayloadTypeRegistry.clientboundPlay().register(SyncHeartsPayload.TYPE, SyncHeartsPayload.CODEC);

		ModEntities.register();
		FabricDefaultAttributeRegistry.register(ModEntities.GRAVE, GraveEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DUMMY, DummyEntity.createAttributes());


		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			ServerPlayer player = newPlayer;
			PlayerData playerData = player.getAttached(PLAYER_DATA);
			if (playerData == null) {
				playerData = new PlayerData();
				player.setAttached(PLAYER_DATA, playerData);
			}
			ServerPlayNetworking.send(player, new SyncHeartsPayload(playerData));
		});

		ServerPlayerEvents.ALLOW_DEATH.register((ServerPlayer player, DamageSource damageSource, float damageAmount) -> {
			return GraveMethods.totemCheck(player, damageSource, damageAmount, () -> {
				GraveMethods.spawnGrave(player, damageSource, damageAmount);
			});
		});
		UseEntityCallback.EVENT.register((Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) -> {
			return GraveMethods.openGraveMenu(player, level, hand, entity, hitResult);
		});


		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (world.isClientSide()) {
				return InteractionResult.PASS;
			}

			if (entity instanceof Player playerGotHit) {

				PlayerData playerData = player.getAttached(SMP_Mod.PLAYER_DATA);
				PlayerData playerDataGotHit = playerGotHit.getAttached(SMP_Mod.PLAYER_DATA);

				if (playerData == null) {
					playerData = new PlayerData();
				}
				if (playerDataGotHit == null) {
					playerDataGotHit = new PlayerData();
				}

				int inFight = playerData.inFight;
				int newInFight = (inFight == 0) ? 10 : (inFight < 10) ? inFight + 5 : (inFight < 180) ? inFight + 5 : inFight;

				int inFightGotHit = playerDataGotHit.inFight;
				int newInFightGotHit = (inFightGotHit == 0) ? 10 : (inFightGotHit < 10) ? inFightGotHit + 5 : (inFightGotHit < 180) ? inFightGotHit + 5 : inFightGotHit;

				PlayerData newAttackerData = new PlayerData(
						playerData.hearts,
						newInFight,
						playerData.enderPearlsToUse,
						playerGotHit.getStringUUID()
				);

				PlayerData newGotHitData = new PlayerData(
						playerDataGotHit.hearts,
						newInFightGotHit,
						playerDataGotHit.enderPearlsToUse,
						player.getStringUUID()
				);

				player.setAttached(SMP_Mod.PLAYER_DATA, newAttackerData);
				playerGotHit.setAttached(SMP_Mod.PLAYER_DATA, newGotHitData);

				ServerPlayNetworking.send((ServerPlayer) player, new SyncHeartsPayload(newAttackerData));
				ServerPlayNetworking.send((ServerPlayer) playerGotHit, new SyncHeartsPayload(newGotHitData));
			}
			return InteractionResult.PASS;
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(AllowTotems.register());
			dispatcher.register(CheckCommand.getCommand());
			dispatcher.register(SpawnDummyCommand.register());
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(SpawnGraveCommand.SpawnGrave());
			dispatcher.register(SetWebHookURL.register());
		});

		ServerTickEvents.END_SERVER_TICK.register((server) -> {
			if(server.getTickCount() % 20 == 0) {
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					PlayerData playerData = player.getAttached(PLAYER_DATA);


					if (playerData != null) playerData.tick(player);
				}
			}



		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			newPlayer.level().getServer().execute(() -> {
				PlayerData playerData = newPlayer.getAttached(SMP_Mod.PLAYER_DATA);
				if (playerData != null) {
					ServerPlayNetworking.send(newPlayer, new SyncHeartsPayload(playerData));

				}
			});
		});

		UseItemCallback.EVENT.register((Player player, Level level, InteractionHand hand) -> {
			boolean returnFailClient = false;
			if (!level.isClientSide()) {
				ItemStack item = player.getItemInHand(hand);
				if (item.is(Items.ENDER_PEARL)) {
					PlayerData playerData = player.getAttached(PLAYER_DATA);
					if (playerData == null) {
						playerData = new PlayerData();
						player.setAttached(PLAYER_DATA, playerData);
					}
					if (playerData.enderPearlsToUse == 0) {
						returnFailClient = true;
						player.sendSystemMessage(Component.literal("Du hast keine Enderperlen mehr"));
						return InteractionResult.FAIL;
					} else if (playerData.inFight != 0 && !player.getCooldowns().isOnCooldown(item)) {
						player.setAttached(
								PLAYER_DATA,
								new PlayerData(
										playerData.hearts,
										playerData.inFight,
										playerData.enderPearlsToUse - 1,

										playerData.gotHitByUUID
								)
						);
					}
					if (playerData.enderPearlsToUse % 4 == 0) {
						player.sendSystemMessage(Component.literal("Du hast noch " + playerData.enderPearlsToUse + " Enderperlen übrig").withStyle(ChatFormatting.RED));
					}
				} else if (itemMod) {
					if (BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("vanillaish-custom-items", "grappling_hook"))
							.map(item::is)
							.orElse(false)) {
						PlayerData playerData = player.getAttached(PLAYER_DATA);
						if (playerData == null) {
							playerData = new PlayerData();
							player.setAttached(PLAYER_DATA, playerData);
						}
						if (playerData.inFight != 0) {
							return InteractionResult.FAIL;
						}
					}
				}
			}
			if (returnFailClient) {
				return InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

			PlayerData playerData = handler.player.getAttached(SMP_Mod.PLAYER_DATA);
			if (playerData != null) {
				ServerPlayNetworking.send(handler.player, new SyncHeartsPayload(playerData));
			}

			ServerPlayer player = handler.player;
			ServerLevel serverLevel = player.level();

			ServerData serverData = serverLevel.getAttached(SERVER_DATA);
			if (serverData == null) {
				serverData = new ServerData();
				serverLevel.setAttached(SERVER_DATA, serverData);
			}

			if (playerData == null) {
				playerData = new PlayerData();
				player.setAttached(PLAYER_DATA, playerData);
			}

			if (serverData.playersToKill.contains(player.getStringUUID())) {

				int newHearts = Math.max(0, playerData.hearts - 1);
				if (newHearts == 0) {
					player.sendSystemMessage(Component.literal("Du hast alle deine Herzen verloren"));
				}

				player.setAttached(PLAYER_DATA, new PlayerData(
						newHearts,

						0,
						32,

						playerData.gotHitByUUID
				));

				List<String> updatedToKill = new ArrayList<>(serverData.playersToKill);
				updatedToKill.remove(player.getStringUUID());

				serverLevel.setAttached(SERVER_DATA, new ServerData(
						updatedToKill,
						serverData.playersAndDummys,
						serverData.carriedAndCarryers,
						serverData.blockCarriers,
						serverData.webHookURL,
						serverData.allowTotems
				));

				server.execute(() -> {
					player.getInventory().clearContent();
					player.hurt(player.damageSources().generic(), Float.MAX_VALUE);
				});

			} else if (serverData.playersAndDummys.containsKey(player.getStringUUID())) {
				String dummyUuidStr = serverData.playersAndDummys.get(player.getStringUUID());
				if (dummyUuidStr != null) {
					var entity = serverLevel.getEntity(UUID.fromString(dummyUuidStr));
					if (entity != null) {
						entity.kill(serverLevel);
					}
				}

				Map<String, String> updatedDummys = new HashMap<>(serverData.playersAndDummys);
				updatedDummys.remove(player.getStringUUID());

				serverLevel.setAttached(SERVER_DATA, new ServerData(
						serverData.playersToKill,
						updatedDummys,
						serverData.carriedAndCarryers,
						serverData.blockCarriers,
						serverData.webHookURL,
						serverData.allowTotems
				));
			}
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server)->{
			Player player = handler.getPlayer();

			PlayerData playerData = player.getAttached(PLAYER_DATA);

			if(playerData.inFight == 0 || playerData.hearts == 0){
				return;
			}

			ServerLevel serverLevel = (ServerLevel) handler.getPlayer().level();

			System.out.println("Spieler geleavt: " + player.getName());

			ServerData serverData = serverLevel.getAttached(SERVER_DATA);
			if(serverData == null){
				serverData = new ServerData();
				serverLevel.setAttached(SERVER_DATA,serverData);
			}

			Map<String,String> newPlayersAndDummys = new HashMap<>(serverData.playersAndDummys);

			DummyEntity DUMMY = new DummyEntity(ModEntities.DUMMY, player.level());

			DUMMY.syncWithPlayer(player);

			serverLevel.addFreshEntity(DUMMY);

			serverData.playersAndDummys.put(player.getStringUUID(),DUMMY.getStringUUID());
			serverLevel.setAttached(SERVER_DATA,serverData);



		});

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive)->{
			PlayerData playerData = newPlayer.getAttached(PLAYER_DATA);
			ServerPlayNetworking.send((ServerPlayer) newPlayer, new SyncHeartsPayload(playerData));
		});



	}

}


