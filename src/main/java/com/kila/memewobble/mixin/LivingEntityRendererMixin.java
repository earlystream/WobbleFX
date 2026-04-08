package com.kila.memewobble.mixin;

import com.kila.memewobble.render.WobbleRenderStateAccess;
import com.kila.memewobble.wobble.WobbleFrame;
import com.kila.memewobble.wobble.WobbleManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "updateRenderState", at = @At("TAIL"))
	private void memewobble$captureWobble(LivingEntity entity, LivingEntityRenderState state, float tickDelta, CallbackInfo ci) {
		((WobbleRenderStateAccess) state).memewobble$setFrame(WobbleManager.INSTANCE.sample(entity, tickDelta));
	}

	@Inject(method = "setupTransforms", at = @At("TAIL"))
	private void memewobble$applyRootWobble(LivingEntityRenderState state, MatrixStack matrices, float bodyYaw, float baseHeight, CallbackInfo ci) {
		WobbleFrame frame = ((WobbleRenderStateAccess) state).memewobble$getFrame();
		if (!frame.active()) {
			return;
		}

		// setupTransforms already applies vanilla facing, death, and sleep rotations.
		// Injecting at TAIL lets the wobble layer on top of the final body pose.
		matrices.translate(0.0F, frame.rootYOffset(), 0.0F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(frame.rootYawDeg()));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(frame.rootRollDeg()));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(frame.rootPitchDeg()));
	}
}
