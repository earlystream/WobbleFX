package com.kila.memewobble.wobble;

import com.kila.memewobble.config.WobbleConfig;
import com.kila.memewobble.config.WobbleConfigManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.LivingEntity;

public final class WobbleManager {
	public static final WobbleManager INSTANCE = new WobbleManager();

	private final Int2ObjectMap<WobbleState> states = new Int2ObjectOpenHashMap<>();

	private WobbleManager() {
	}

	public void onClientTick(LivingEntity entity) {
		int entityId = entity.getId();
		if (entity.isRemoved() || !entity.isAlive()) {
			this.states.remove(entityId);
			return;
		}

		WobbleConfig config = WobbleConfigManager.INSTANCE.getConfig();
		if (!config.affects(entity)) {
			this.states.remove(entityId);
			return;
		}

		WobbleState state = this.states.computeIfAbsent(entityId, key -> WobbleState.create(entity));
		state.durationTicks = this.resolveDurationTicks(entity, config);
		int currentHurtTime = entity.hurtTime;
		boolean freshHit = !(entity instanceof ArmorStandEntity) && currentHurtTime > state.lastHurtTimeSeen;

		if (freshHit) {
			state.restart(this.resolveDurationTicks(entity, config), config.strength);
		} else if (state.active) {
			state.ageTicks += 1.0F;
			if (state.ageTicks >= state.durationTicks) {
				state.active = false;
			}
		}

		state.lastHurtTimeSeen = currentHurtTime;

		if (!state.active && currentHurtTime <= 0) {
			this.states.remove(entityId);
		}
	}

	public void triggerArmorStandHit(ArmorStandEntity armorStand) {
		WobbleConfig config = WobbleConfigManager.INSTANCE.getConfig();
		if (!config.affects(armorStand)) {
			return;
		}

		WobbleState state = this.states.computeIfAbsent(armorStand.getId(), key -> WobbleState.create(armorStand));
		state.restart(this.resolveDurationTicks(armorStand, config), config.strength);
	}

	public WobbleFrame sample(LivingEntity entity, float tickDelta) {
		WobbleConfig config = WobbleConfigManager.INSTANCE.getConfig();
		if (!config.affects(entity)) {
			this.states.remove(entity.getId());
			return WobbleFrame.INACTIVE;
		}

		WobbleState state = this.states.get(entity.getId());
		if (state == null) {
			return WobbleFrame.INACTIVE;
		}

		WobbleFrame frame = WobbleMath.sample(state, config, tickDelta);
		if (!frame.active() && !state.active && entity.hurtTime <= 0) {
			this.states.remove(entity.getId());
		}
		return frame;
	}

	public void clear() {
		this.states.clear();
	}

	private float resolveDurationTicks(LivingEntity entity, WobbleConfig config) {
		if (entity instanceof ArmorStandEntity) {
			return config.armorStandDurationSeconds * 20.0F;
		}

		return config.durationTicks;
	}
}
