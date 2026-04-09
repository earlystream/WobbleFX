package com.kila.memewobble.config;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public final class WobbleConfig {
	public static final float DEFAULT_STRENGTH = 1.0F;
	public static final float DEFAULT_DURATION_TICKS = 9.0F;
	public static final float DEFAULT_SPEED = 1.0F;
	public static final float DEFAULT_BODY_ROLL = 1.0F;
	public static final float DEFAULT_BODY_YAW = 0.5F;
	public static final float DEFAULT_HEAD_COUNTER = 0.4F;
	public static final float DEFAULT_LIMB_AMOUNT = 0.25F;
	public static final float DEFAULT_BOUNCE = 0.15F;

	public boolean enabled = true;
	public String preset = WobblePreset.NORMAL.id();
	public float strength = DEFAULT_STRENGTH;
	public float durationTicks = DEFAULT_DURATION_TICKS;
	public float speed = DEFAULT_SPEED;
	public float bodyRoll = DEFAULT_BODY_ROLL;
	public float bodyYaw = DEFAULT_BODY_YAW;
	public float headCounter = DEFAULT_HEAD_COUNTER;
	public float limbAmount = DEFAULT_LIMB_AMOUNT;
	public float bounce = DEFAULT_BOUNCE;
	public float armorStandDurationSeconds = 5.0F;
	public boolean affectMobs = true;
	public boolean affectPlayers = false;
	public boolean affectArmorStands = true;

	public static WobbleConfig defaults() {
		WobbleConfig config = new WobbleConfig();
		WobblePreset.NORMAL.applyTo(config);
		config.enabled = true;
		config.affectMobs = true;
		config.affectPlayers = false;
		config.affectArmorStands = true;
		return config;
	}

	public WobbleConfig copy() {
		WobbleConfig copy = new WobbleConfig();
		copy.enabled = this.enabled;
		copy.preset = this.preset;
		copy.strength = this.strength;
		copy.durationTicks = this.durationTicks;
		copy.speed = this.speed;
		copy.bodyRoll = this.bodyRoll;
		copy.bodyYaw = this.bodyYaw;
		copy.headCounter = this.headCounter;
		copy.limbAmount = this.limbAmount;
		copy.bounce = this.bounce;
		copy.armorStandDurationSeconds = this.armorStandDurationSeconds;
		copy.affectMobs = this.affectMobs;
		copy.affectPlayers = this.affectPlayers;
		copy.affectArmorStands = this.affectArmorStands;
		return copy;
	}

	public WobbleConfig clamp() {
		this.strength = MathHelper.clamp(this.strength, 0.0F, 3.0F);
		this.durationTicks = MathHelper.clamp(this.durationTicks, 1.0F, 20.0F);
		this.speed = MathHelper.clamp(this.speed, 0.1F, 3.0F);
		this.bodyRoll = MathHelper.clamp(this.bodyRoll, 0.0F, 3.0F);
		this.bodyYaw = MathHelper.clamp(this.bodyYaw, 0.0F, 3.0F);
		this.headCounter = MathHelper.clamp(this.headCounter, 0.0F, 3.0F);
		this.limbAmount = MathHelper.clamp(this.limbAmount, 0.0F, 2.0F);
		this.bounce = MathHelper.clamp(this.bounce, 0.0F, 1.0F);
		this.armorStandDurationSeconds = MathHelper.clamp(this.armorStandDurationSeconds, 0.5F, 20.0F);
		this.preset = WobblePreset.fromId(this.preset).id();
		return this;
	}

	public void applyPreset(WobblePreset preset) {
		preset.applyTo(this);
		this.clamp();
	}

	public boolean affects(LivingEntity entity) {
		if (!this.enabled) {
			return false;
		}

		if (entity instanceof ArmorStandEntity) {
			return this.affectArmorStands;
		}

		if (entity instanceof PlayerEntity) {
			return this.affectPlayers;
		}

		return this.affectMobs;
	}
}
