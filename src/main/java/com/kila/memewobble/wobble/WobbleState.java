package com.kila.memewobble.wobble;

import net.minecraft.entity.LivingEntity;

public final class WobbleState {
	public static final float DEFAULT_DURATION_TICKS = 9.0F;

	public int lastHurtTimeSeen;
	public float ageTicks;
	public float durationTicks = DEFAULT_DURATION_TICKS;
	public float strength = 1.0F;
	public float phase;
	public float directionSign;
	public boolean active;

	public static WobbleState create(LivingEntity entity) {
		WobbleState state = new WobbleState();
		state.phase = WobbleMath.seededPhase(entity.getId());
		state.directionSign = WobbleMath.seededDirection(entity.getId());
		return state;
	}

	public void restart(float durationTicks, float strength) {
		this.active = true;
		this.ageTicks = 0.0F;
		this.durationTicks = durationTicks;
		this.strength = strength;
	}
}
