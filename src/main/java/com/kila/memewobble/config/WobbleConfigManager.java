package com.kila.memewobble.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WobbleConfigManager {
	public static final WobbleConfigManager INSTANCE = new WobbleConfigManager();

	private static final Logger LOGGER = LoggerFactory.getLogger("WobbleFX");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Path path = FabricLoader.getInstance().getConfigDir().resolve("wobblefx.json");
	private WobbleConfig config = WobbleConfig.defaults();

	private WobbleConfigManager() {
	}

	public WobbleConfig getConfig() {
		return this.config;
	}

	public void setConfig(WobbleConfig config) {
		this.config = config.copy().clamp();
	}

	public WobbleConfig resetToDefaults() {
		this.config = WobbleConfig.defaults();
		return this.config;
	}

	public void load() {
		try {
			Files.createDirectories(this.path.getParent());
			if (!Files.exists(this.path)) {
				this.config = WobbleConfig.defaults();
				this.save();
				return;
			}

			WobbleConfig loaded = GSON.fromJson(Files.readString(this.path, StandardCharsets.UTF_8), WobbleConfig.class);
			if (loaded == null) {
				throw new JsonParseException("Config file was empty");
			}

			this.config = loaded.clamp();
		} catch (IOException | JsonParseException exception) {
			LOGGER.warn("Failed to load {}. Falling back to defaults.", this.path.getFileName(), exception);
			this.config = WobbleConfig.defaults();
			this.save();
		}
	}

	public void save() {
		try {
			Files.createDirectories(this.path.getParent());
			Files.writeString(this.path, GSON.toJson(this.config), StandardCharsets.UTF_8);
		} catch (IOException exception) {
			LOGGER.warn("Failed to save wobble config to {}", this.path, exception);
		}
	}
}
