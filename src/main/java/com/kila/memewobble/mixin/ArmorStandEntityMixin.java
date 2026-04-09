package com.kila.memewobble.mixin;

import com.kila.memewobble.wobble.WobbleManager;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandEntityMixin {
	@Inject(method = "handleStatus", at = @At("TAIL"))
	private void memewobble$triggerArmorStandWobble(byte status, CallbackInfo ci) {
		if (status != 32) {
			return;
		}

		ArmorStandEntity armorStand = (ArmorStandEntity) (Object) this;
		if (!armorStand.getEntityWorld().isClient()) {
			return;
		}

		WobbleManager.INSTANCE.triggerArmorStandHit(armorStand);
	}
}
