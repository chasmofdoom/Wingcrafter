package org.aussiebox.wingcrafter.client.entity.animation;

import net.minecraft.client.render.entity.animation.AnimationDefinition;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class MoonGlobeEntityAnimations {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.create(10.0F).looping()
		.addBoneAnimation("globe", new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(5.0F, AnimationHelper.createRotationalVector(0.0F, -90.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(10.0F, AnimationHelper.createRotationalVector(0.0F, -180.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("globe", new Transformation(Transformation.Targets.MOVE_ORIGIN,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(5.0F, AnimationHelper.createTranslationalVector(0.0F, 4.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(10.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();
}