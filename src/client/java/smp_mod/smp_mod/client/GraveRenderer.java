package smp_mod.smp_mod.client;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import smp_mod.smp_mod.entity.GraveEntity;
import smp_mod.smp_mod.client.model.GraveModel;
import smp_mod.smp_mod.client.render.GraveRenderState;

public class GraveRenderer extends LivingEntityRenderer<GraveEntity, GraveRenderState, GraveModel> {

    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath("smp_mod", "textures/entity/grave/grave.png");

    public GraveRenderer(EntityRendererProvider.Context context) {
        super(context, new GraveModel(context.bakeLayer(GraveModel.LAYER_LOCATION)), 1.5F);
    }
    @Override
    public GraveRenderState createRenderState() {
        return new GraveRenderState();
    }

    @Override
    public Identifier getTextureLocation(GraveRenderState state) {
        return TEXTURE;
    }
    public void extractRenderState(GraveEntity entity, GraveRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

    }

    @Override
    protected @Nullable Component getNameTag(GraveEntity entity) {
        return null;
    } 
}