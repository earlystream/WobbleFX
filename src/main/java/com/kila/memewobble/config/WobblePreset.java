package com.kila.memewobble.config;

import net.minecraft.text.Text;

public enum WobblePreset {
	SUBTLE("subtle", "Subtle"),
	NORMAL("normal", "Normal"),
	MEME("meme", "Meme"),
	EXTREME("extreme", "Extreme");

	private final String id;
	private final String displayName;

	WobblePreset(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public String id() {
		return this.id;
	}

	public Text displayText() {
		return Text.literal(this.displayName);
	}

	public void applyTo(WobbleConfig config) {
		switch (this) {
			case SUBTLE -> {
				config.strength = 0.65F;
				config.durationTicks = 7.0F;
				config.speed = 0.9F;
				config.bodyRoll = 0.65F;
				config.bodyYaw = 0.30F;
				config.headCounter = 0.25F;
				config.limbAmount = 0.12F;
				config.bounce = 0.08F;
			}
			case NORMAL -> {
				config.strength = 1.0F;
				config.durationTicks = 9.0F;
				config.speed = 1.0F;
				config.bodyRoll = 1.0F;
				config.bodyYaw = 0.5F;
				config.headCounter = 0.4F;
				config.limbAmount = 0.25F;
				config.bounce = 0.15F;
			}
			case MEME -> {
				config.strength = 1.45F;
				config.durationTicks = 10.5F;
				config.speed = 1.15F;
				config.bodyRoll = 1.40F;
				config.bodyYaw = 0.85F;
				config.headCounter = 0.60F;
				config.limbAmount = 0.45F;
				config.bounce = 0.22F;
			}
			case EXTREME -> {
				config.strength = 2.20F;
				config.durationTicks = 13.0F;
				config.speed = 1.35F;
				config.bodyRoll = 2.10F;
				config.bodyYaw = 1.25F;
				config.headCounter = 0.90F;
				config.limbAmount = 0.75F;
				config.bounce = 0.35F;
			}
		}
		config.preset = this.id;
	}

	public static WobblePreset fromId(String id) {
		for (WobblePreset preset : values()) {
			if (preset.id.equalsIgnoreCase(id)) {
				return preset;
			}
		}

		return NORMAL;
	}
}
