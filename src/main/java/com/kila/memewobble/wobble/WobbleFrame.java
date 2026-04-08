package com.kila.memewobble.wobble;

public record WobbleFrame(
	boolean active,
	float progress,
	float envelope,
	float primary,
	float wave,
	float limbWave,
	float directionSign,
	float rootRollDeg,
	float rootYawDeg,
	float rootPitchDeg,
	float rootYOffset
) {
	public static final WobbleFrame INACTIVE = new WobbleFrame(
		false,
		1.0F,
		0.0F,
		0.0F,
		0.0F,
		0.0F,
		1.0F,
		0.0F,
		0.0F,
		0.0F,
		0.0F
	);
}
