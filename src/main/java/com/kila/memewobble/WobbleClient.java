package com.kila.memewobble;

import com.kila.memewobble.config.WobbleConfigManager;
import com.kila.memewobble.screen.WobbleConfigScreen;
import com.kila.memewobble.wobble.WobbleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

public final class WobbleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		WobbleConfigManager.INSTANCE.load();
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> WobbleManager.INSTANCE.clear());
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
			ClientCommandManager.literal("wobblefx").executes(context -> {
				MinecraftClient client = MinecraftClient.getInstance();
				client.setScreen(new WobbleConfigScreen(client.currentScreen));
				return 1;
			})
		));
	}
}
