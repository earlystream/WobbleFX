package com.kila.memewobble.mixin;

import com.kila.memewobble.render.WobbleRenderStateAccess;
import com.kila.memewobble.wobble.WobbleFrame;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements WobbleRenderStateAccess {
	@Unique
	private WobbleFrame memewobble$frame = WobbleFrame.INACTIVE;

	@Override
	public WobbleFrame memewobble$getFrame() {
		return this.memewobble$frame;
	}

	@Override
	public void memewobble$setFrame(WobbleFrame frame) {
		this.memewobble$frame = frame;
	}
}
