package smp_mod.smp_mod.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import smp_mod.smp_mod.SMP_Mod;
import smp_mod.smp_mod.client.render.DummyRenderstate;
import smp_mod.smp_mod.entity.DummyEntity;

public class DummyModel extends EntityModel<DummyRenderstate> {

    private final ModelPart bb_main;
    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath("smp_mod", "textures/entity/dummy/dummy_texture.png");


    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            TEXTURE,
            "main"                                                     // 2. Der Layer-Name
    );

    public DummyModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-2.0F, -12.0F, -4.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-2.0F, -24.0F, -4.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 32).addBox(-2.0F, -24.0F, -8.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-2.0F, -24.0F, 4.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }




}
