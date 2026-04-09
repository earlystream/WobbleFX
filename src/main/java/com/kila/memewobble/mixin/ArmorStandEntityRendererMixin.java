package com.kila.memewobble.mixin;

import com.kila.memewobble.render.WobbleRenderStateAccess;
import com.kila.memewobble.wobble.WobbleFrame;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin {
	@Inject(method = "setupTransforms", at = @At("TAIL"))
	private void memewobble$applyArmorStandWobble(ArmorStandEntityRenderState state, MatrixStack matrices, float bodyYaw, float baseHeight, CallbackInfo ci) {
		WobbleFrame frame = ((WobbleRenderStateAccess) state).memewobble$getFrame();
		if (!frame.active()) {
			return;
		}

		matrices.translate(0.0F, frame.rootYOffset(), 0.0F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(frame.rootYawDeg()));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(frame.rootRollDeg()));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(frame.rootPitchDeg()));
	}
}
