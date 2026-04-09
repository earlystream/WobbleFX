package com.kila.memewobble.mixin;

import com.kila.memewobble.config.WobbleConfig;
import com.kila.memewobble.config.WobbleConfigManager;
import com.kila.memewobble.render.WobbleRenderStateAccess;
import com.kila.memewobble.wobble.WobbleFrame;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> {
	private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;

	@Shadow
	@Final
	public ModelPart body;

	@Shadow
	@Final
	public ModelPart head;

	@Shadow
	@Final
	public ModelPart rightArm;

	@Shadow
	@Final
	public ModelPart leftArm;

	@Shadow
	@Final
	public ModelPart rightLeg;

	@Shadow
	@Final
	public ModelPart leftLeg;

	@Inject(method = "setAngles", at = @At("TAIL"))
	private void memewobble$applyPartWobble(T state, CallbackInfo ci) {
		WobbleFrame frame = ((WobbleRenderStateAccess) state).memewobble$getFrame();
		if (!frame.active()) {
			return;
		}

		float roll = frame.rootRollDeg();
		float yaw = frame.rootYawDeg();
		float pitch = frame.rootPitchDeg();
		float limbLag = frame.limbWave();
		float direction = frame.directionSign();
		WobbleConfig config = WobbleConfigManager.INSTANCE.getConfig();
		float headScale = config.headCounter / WobbleConfig.DEFAULT_HEAD_COUNTER;
		float limbScale = config.limbAmount / WobbleConfig.DEFAULT_LIMB_AMOUNT;

		this.body.roll += roll * 0.55F * DEG_TO_RAD;
		this.body.yaw += yaw * 0.35F * DEG_TO_RAD;
		this.body.pitch += pitch * 0.25F * DEG_TO_RAD;

		this.head.roll -= roll * 0.28F * headScale * DEG_TO_RAD;
		this.head.yaw -= yaw * 0.18F * headScale * DEG_TO_RAD;
		this.head.pitch += pitch * 0.18F * headScale * DEG_TO_RAD;

		this.rightArm.roll += (roll * 0.18F * limbScale + limbLag * 2.2F * direction * limbScale) * DEG_TO_RAD;
		this.leftArm.roll += (roll * 0.14F * limbScale - limbLag * 1.9F * direction * limbScale) * DEG_TO_RAD;
		this.rightArm.yaw += (yaw * 0.10F * limbScale + limbLag * 1.1F * direction * limbScale) * DEG_TO_RAD;
		this.leftArm.yaw += (yaw * 0.08F * limbScale - limbLag * 1.0F * direction * limbScale) * DEG_TO_RAD;

		this.rightLeg.roll += (-roll * 0.08F * limbScale + limbLag * 0.8F * direction * limbScale) * DEG_TO_RAD;
		this.leftLeg.roll += (roll * 0.08F * limbScale - limbLag * 0.8F * direction * limbScale) * DEG_TO_RAD;
	}
}
