package com.kila.memewobble;

import com.kila.memewobble.wobble.WobbleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public final class WobbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> WobbleManager.INSTANCE.clear());
	}
}
