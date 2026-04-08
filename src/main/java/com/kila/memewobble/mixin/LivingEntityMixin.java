package com.kila.memewobble.mixin;

import com.kila.memewobble.wobble.WobbleManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void memewobble$trackFreshHits(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (!entity.getEntityWorld().isClient()) {
			return;
		}

		WobbleManager.INSTANCE.onClientTick(entity);
	}
}
