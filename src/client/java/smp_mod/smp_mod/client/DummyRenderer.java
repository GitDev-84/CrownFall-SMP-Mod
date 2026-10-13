package smp_mod.smp_mod.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import smp_mod.smp_mod.client.model.DummyModel;
import smp_mod.smp_mod.client.render.DummyRenderstate;
import smp_mod.smp_mod.entity.DummyEntity;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DummyRenderer extends MobRenderer<DummyEntity,DummyRenderstate,DummyModel> {




    public DummyRenderer(EntityRendererProvider.Context context) {



        super(context,new DummyModel(context.bakeLayer(DummyModel.LAYER_LOCATION)),0.6F);

    }

    @Override
    public Identifier getTextureLocation(DummyRenderstate state) {
        if (state.skinTexture == null) {
            return Identifier.fromNamespaceAndPath("smp_mod","textures/entity/dummy/dummy_texture.png");
        }

        return state.skinTexture;
    }

    public DummyRenderstate createRenderState(){

        return new DummyRenderstate();

    }
    public void extractRenderState(DummyEntity entity, DummyRenderstate state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        if(entity.gameProfile != null && state.skinTexture != null) {
            CompletableFuture<Optional<PlayerSkin>> future =
                    Minecraft.getInstance().getSkinManager().get(entity.gameProfile);


            if (future.isDone() && future.join().isPresent()) {
                state.skinTexture = future.join().get().body().texturePath();
            } else {
                state.skinTexture = DefaultPlayerSkin.getDefaultTexture();
            }
        }

    }

}