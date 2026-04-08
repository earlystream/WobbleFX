package com.kila.memewobble.wobble;

import net.minecraft.util.math.MathHelper;

public final class WobbleMath {
	private static final float PI = (float) Math.PI;
	private static final double GOLDEN_RATIO = 0.6180339887498949D;

	private WobbleMath() {
	}

	public static float seededPhase(int entityId) {
		double seeded = entityId * GOLDEN_RATIO;
		double fractional = seeded - Math.floor(seeded);
		return (float) (fractional * Math.PI * 2.0D);
	}

	public static float seededDirection(int entityId) {
		int mixed = entityId * 0x45d9f3b;
		mixed ^= mixed >>> 16;
		return (mixed & 1) == 0 ? 1.0F : -1.0F;
	}

	public static WobbleFrame sample(WobbleState state, float tickDelta) {
		if (!state.active) {
			return WobbleFrame.INACTIVE;
		}

		float interpolatedAge = state.ageTicks + tickDelta;
		float progress = interpolatedAge / state.durationTicks;
		if (progress >= 1.0F) {
			return WobbleFrame.INACTIVE;
		}

		float decay = 1.0F - progress;
		float envelope = decay * decay;

		// This is a damped impulse response rather than frame-to-frame noise:
		// one strong bend, an overshoot, then a quick settle.
		float primary = MathHelper.sin(progress * PI * 4.5F) * envelope;
		float secondary = MathHelper.sin(progress * PI * 7.0F + state.phase) * envelope * 0.18F;
		float wave = primary + secondary;
		float limbWave = MathHelper.sin(progress * PI * 5.0F + 0.7F) * envelope;

		float rootRollDeg = wave * 11.0F * state.strength * state.directionSign;
		float rootYawDeg = MathHelper.sin(progress * PI * 5.0F + 0.4F) * envelope * 5.5F * state.strength * state.directionSign;
		float rootPitchDeg = -Math.abs(primary) * 3.0F * state.strength;
		float rootYOffset = Math.abs(primary) * 0.05F * state.strength;

		return new WobbleFrame(
			true,
			progress,
			envelope,
			primary,
			wave,
			limbWave,
			state.directionSign,
			rootRollDeg,
			rootYawDeg,
			rootPitchDeg,
			rootYOffset
		);
	}
}
