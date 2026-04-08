package com.kila.memewobble.wobble;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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

		WobbleState state = this.states.computeIfAbsent(entityId, key -> WobbleState.create(entity));
		int currentHurtTime = entity.hurtTime;
		boolean freshHit = currentHurtTime > state.lastHurtTimeSeen;

		if (freshHit) {
			state.restart();
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

	public WobbleFrame sample(LivingEntity entity, float tickDelta) {
		WobbleState state = this.states.get(entity.getId());
		if (state == null) {
			return WobbleFrame.INACTIVE;
		}

		WobbleFrame frame = WobbleMath.sample(state, tickDelta);
		if (!frame.active() && !state.active && entity.hurtTime <= 0) {
			this.states.remove(entity.getId());
		}
		return frame;
	}

	public void clear() {
		this.states.clear();
	}
}
