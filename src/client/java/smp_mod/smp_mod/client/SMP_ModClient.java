package smp_mod.smp_mod.client;


import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import smp_mod.smp_mod.ModEntities;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.SaveData.PlayerData;
import smp_mod.smp_mod.client.model.DummyModel;
import smp_mod.smp_mod.client.model.GraveModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import smp_mod.smp_mod.sync.SyncHeartsPayload;

public class SMP_ModClient implements ClientModInitializer {
	public static boolean wasSprinting = false;
	public static boolean rightTiming = false;
	public void onInitializeClient() {




			ModelLayerRegistry.registerModelLayer(
					GraveModel.LAYER_LOCATION,
					GraveModel::createBodyLayer
			);

			EntityRendererRegistry.register(
					ModEntities.GRAVE,
					GraveRenderer::new
			);



		ModelLayerRegistry.registerModelLayer(
				DummyModel.LAYER_LOCATION,
				DummyModel::createBodyLayer
		);

		EntityRendererRegistry.register(
				ModEntities.DUMMY,
				DummyRenderer::new
		);


		HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("smp_mod", "smp_mod"), (GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker)->{
			HeartsRenderer.register(guiGraphicsExtractor,deltaTracker);


		});



		ClientPlayNetworking.registerGlobalReceiver(SyncHeartsPayload.TYPE, (payload, context) -> {
			context.client().execute(() -> {
				if (context.client().player != null) {
					PlayerData clientData = payload.attachment();
					context.client().player.setAttached(SMP_Mod.PLAYER_DATA, clientData);
				}
			});
		});
	}
}