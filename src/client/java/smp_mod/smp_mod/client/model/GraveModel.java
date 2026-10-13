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

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(1, 33).addBox(-24.0F, -18.0F, 8.0F, 16.0F, 15.0F, 15.0F, new CubeDeformation(0.0F))
				.texOffs(15, 7).addBox(-23.0F, -3.0F, 8.0F, 22.0F, 2.0F, 23.0F, new CubeDeformation(0.0F))
				.texOffs(139, 7).addBox(-8.0F, -4.0F, -4.0F, 13.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(129, 9).addBox(-31.0F, -3.0F, -6.0F, 23.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(143, 5).addBox(-37.0F, -3.0F, 23.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(165, 3).addBox(-7.0F, -2.0F, -8.0F, 4.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(111, 11).addBox(-17.0F, -2.0F, -9.0F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(33, 9).addBox(-34.0F, -2.0F, -7.0F, 17.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(138, 8).addBox(0.0F, -2.0F, 17.0F, 8.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(107, 9).addBox(-40.0F, -3.0F, 5.0F, 9.0F, 4.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(145, 11).addBox(-39.0F, -2.0F, 18.0F, 9.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(132, 11).addBox(-6.0F, -2.0F, 29.0F, 11.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(86, 7).addBox(-35.0F, -2.0F, 30.0F, 17.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(165, 12).addBox(6.0F, -1.0F, 4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(175, 3).addBox(6.0F, -1.0F, 15.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(141, 3).addBox(3.0F, -1.0F, 25.0F, 8.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(160, 0).addBox(-2.0F, -2.0F, -9.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(75, 12).addBox(-10.0F, -1.0F, -12.0F, 9.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(175, 15).addBox(-29.0F, -1.0F, -11.0F, 11.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(160, 12).addBox(-41.0F, -1.0F, -7.0F, 11.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(164, 4).addBox(-46.0F, -1.0F, 5.0F, 10.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(164, 0).addBox(-45.0F, -1.0F, 14.0F, 5.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(160, 18).addBox(-43.0F, -1.0F, 27.0F, 7.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(151, 13).addBox(-30.0F, -1.0F, 34.0F, 11.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(160, 18).addBox(-18.0F, -1.0F, 36.0F, 12.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(151, 18).addBox(4.0F, -2.0F, 6.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(159, 21).addBox(3.0F, -1.0F, 23.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(151, 18).addBox(-5.0F, -2.0F, -5.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(175, 23).addBox(-34.0F, -1.0F, -4.0F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(141, 23).addBox(-40.0F, -2.0F, 7.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(159, 21).addBox(-40.0F, -1.0F, 22.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(75, 20).addBox(-6.0F, -1.0F, 31.0F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(141, 14).addBox(-34.0F, -2.0F, 31.0F, 8.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition MID_S1_r1 = bb_main.addOrReplaceChild("MID_S1_r1", CubeListBuilder.create().texOffs(162, 14).addBox(-18.0F, -3.0F, 31.0F, 11.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(141, 9).addBox(-19.0F, -3.0F, 24.0F, 0.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(142, 7).addBox(-9.0F, -3.0F, 23.0F, 13.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(144, 10).addBox(-8.0F, -3.0F, 8.0F, 15.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition MID_W1_r1 = bb_main.addOrReplaceChild("MID_W1_r1", CubeListBuilder.create().texOffs(120, 11).addBox(0.0F, -3.0F, 6.0F, 9.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

		PartDefinition L1_E_r1 = bb_main.addOrReplaceChild("L1_E_r1", CubeListBuilder.create().texOffs(138, 3).addBox(-38.0F, -4.0F, 8.0F, 15.0F, 3.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}


}



