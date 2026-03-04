package org.aussiebox.wingcrafter.client.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.client.entity.animation.MoonGlobeEntityAnimations;
import org.aussiebox.wingcrafter.client.entity.render.state.MoonGlobeEntityRenderState;

public class MoonGlobeEntityModel extends EntityModel<MoonGlobeEntityRenderState> {
	public static final EntityModelLayer GLOBE = new EntityModelLayer(Wingcrafter.id("moon_globe_entity"), "main");
	private final ModelPart globe;

	private final Animation idleAnimation;

	public MoonGlobeEntityModel(ModelPart root) {
        super(root);
        this.globe = root.getChild("globe");
		this.idleAnimation = MoonGlobeEntityAnimations.idle.createAnimation(root);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData globe = modelPartData.addChild("globe", ModelPartBuilder.create().uv(0, 20).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 19.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(MoonGlobeEntityRenderState state) {
		super.setAngles(state);
		this.idleAnimation.apply(state.idleAnimationState, state.age, 1f);
	}
}