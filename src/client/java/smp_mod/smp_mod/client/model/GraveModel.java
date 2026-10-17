package smp_mod.smp_mod.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import smp_mod.smp_mod.client.render.GraveRenderState;

public class GraveModel extends EntityModel<GraveRenderState> {


	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(
					Identifier.fromNamespaceAndPath("smp_mod", "grave"),
					"main"
			);
	private final ModelPart bb_main;



	public GraveModel(ModelPart root) {
		super(root);
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -17.0F, -2.0F, 2.0F, 15.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, -1.0F, -6.0F, 6.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(10, 22).addBox(-3.0F, -3.0F, -0.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -11.0F, 4.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 17).addBox(-3.0F, -3.0F, -0.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -11.0F, -3.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 12).addBox(-8.0F, -1.0F, -1.0F, 9.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


}



