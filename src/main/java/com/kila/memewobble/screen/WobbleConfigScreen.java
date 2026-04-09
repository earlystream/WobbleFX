package com.kila.memewobble.screen;

import com.kila.memewobble.config.WobbleConfig;
import com.kila.memewobble.config.WobbleConfigManager;
import com.kila.memewobble.config.WobblePreset;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public final class WobbleConfigScreen extends Screen {
	private static final int COLUMN_WIDTH = 150;
	private static final int ROW_HEIGHT = 22;
	private static final int SECTION_TITLE_COLOR = 0xFFE0E0E0;

	private final Screen parent;
	private WobbleConfig workingCopy;

	private CyclingButtonWidget<Boolean> enabledButton;
	private CyclingButtonWidget<WobblePreset> presetButton;
	private CyclingButtonWidget<Boolean> affectMobsButton;
	private CyclingButtonWidget<Boolean> affectPlayersButton;
	private CyclingButtonWidget<Boolean> affectArmorStandsButton;
	private ConfigSlider strengthSlider;
	private ConfigSlider durationSlider;
	private ConfigSlider speedSlider;
	private ConfigSlider bodyRollSlider;
	private ConfigSlider bodyYawSlider;
	private ConfigSlider headCounterSlider;
	private ConfigSlider limbAmountSlider;
	private ConfigSlider bounceSlider;
	private ConfigSlider armorStandDurationSlider;

	public WobbleConfigScreen(Screen parent) {
		super(Text.literal("WobbleFX Settings"));
		this.parent = parent;
		this.workingCopy = WobbleConfigManager.INSTANCE.getConfig().copy();
	}

	@Override
	protected void init() {
		this.clearChildren();

		int leftX = this.width / 2 - 160;
		int rightX = this.width / 2 + 10;
		int generalY = 48;
		int targetsY = 132;
		int animationY = 48;

		this.enabledButton = this.addDrawableChild(
			CyclingButtonWidget.onOffBuilder(this.workingCopy.enabled)
				.build(leftX, generalY, COLUMN_WIDTH, 20, Text.literal("Enabled"), (button, value) -> this.workingCopy.enabled = value)
		);
		this.presetButton = this.addDrawableChild(
			CyclingButtonWidget.builder(WobblePreset::displayText, (Supplier<WobblePreset>) () -> WobblePreset.fromId(this.workingCopy.preset))
				.values(WobblePreset.values())
				.build(leftX, generalY + ROW_HEIGHT, COLUMN_WIDTH, 20, Text.literal("Preset"), (button, value) -> {
					this.workingCopy.applyPreset(value);
					this.refreshWidgetsFromConfig();
				})
		);
		this.affectMobsButton = this.addDrawableChild(
			CyclingButtonWidget.onOffBuilder(this.workingCopy.affectMobs)
				.build(leftX, targetsY, COLUMN_WIDTH, 20, Text.literal("Affect Mobs"), (button, value) -> this.workingCopy.affectMobs = value)
		);
		this.affectPlayersButton = this.addDrawableChild(
			CyclingButtonWidget.onOffBuilder(this.workingCopy.affectPlayers)
				.build(leftX, targetsY + ROW_HEIGHT, COLUMN_WIDTH, 20, Text.literal("Affect Players"), (button, value) -> this.workingCopy.affectPlayers = value)
		);
		this.affectArmorStandsButton = this.addDrawableChild(
			CyclingButtonWidget.onOffBuilder(this.workingCopy.affectArmorStands)
				.build(leftX, targetsY + ROW_HEIGHT * 2, COLUMN_WIDTH, 20, Text.literal("Affect Armor Stands"), (button, value) -> this.workingCopy.affectArmorStands = value)
		);

		this.strengthSlider = this.addSlider(rightX, animationY, "Strength", 0.0F, 3.0F, () -> this.workingCopy.strength, value -> this.workingCopy.strength = value, value -> format("%.2f", value));
		this.durationSlider = this.addSlider(rightX, animationY + ROW_HEIGHT, "Duration", 1.0F, 20.0F, () -> this.workingCopy.durationTicks, value -> this.workingCopy.durationTicks = value, value -> format("%.1f ticks", value));
		this.speedSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 2, "Speed", 0.1F, 3.0F, () -> this.workingCopy.speed, value -> this.workingCopy.speed = value, value -> format("%.2f", value));
		this.bodyRollSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 3, "Body Roll", 0.0F, 3.0F, () -> this.workingCopy.bodyRoll, value -> this.workingCopy.bodyRoll = value, value -> format("%.2f", value));
		this.bodyYawSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 4, "Body Yaw", 0.0F, 3.0F, () -> this.workingCopy.bodyYaw, value -> this.workingCopy.bodyYaw = value, value -> format("%.2f", value));
		this.headCounterSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 5, "Head Counter", 0.0F, 3.0F, () -> this.workingCopy.headCounter, value -> this.workingCopy.headCounter = value, value -> format("%.2f", value));
		this.limbAmountSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 6, "Limb Amount", 0.0F, 2.0F, () -> this.workingCopy.limbAmount, value -> this.workingCopy.limbAmount = value, value -> format("%.2f", value));
		this.bounceSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 7, "Bounce", 0.0F, 1.0F, () -> this.workingCopy.bounce, value -> this.workingCopy.bounce = value, value -> format("%.2f", value));
		this.armorStandDurationSlider = this.addSlider(rightX, animationY + ROW_HEIGHT * 8, "Armor Stand Duration", 0.5F, 20.0F, () -> this.workingCopy.armorStandDurationSeconds, value -> this.workingCopy.armorStandDurationSeconds = value, value -> format("%.1f s", value));

		int buttonsY = this.height - 28;
		this.addDrawableChild(ButtonWidget.builder(Text.literal("Reset Defaults"), button -> {
			this.workingCopy = WobbleConfig.defaults();
			this.refreshWidgetsFromConfig();
		}).dimensions(this.width / 2 - 155, buttonsY, 100, 20).build());
		this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> this.saveAndClose()).dimensions(this.width / 2 - 50, buttonsY, 100, 20).build());
		this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> this.close()).dimensions(this.width / 2 + 55, buttonsY, 100, 20).build());

		this.refreshWidgetsFromConfig();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		context.fill(0, 0, this.width, this.height, 0xB0101018);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 14, 0xFFFFFF);

		int leftX = this.width / 2 - 160;
		int rightX = this.width / 2 + 10;
		context.drawTextWithShadow(this.textRenderer, Text.literal("General"), leftX, 34, SECTION_TITLE_COLOR);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Targets"), leftX, 118, SECTION_TITLE_COLOR);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Animation"), rightX, 34, SECTION_TITLE_COLOR);

		super.render(context, mouseX, mouseY, deltaTicks);
	}

	@Override
	public void close() {
		if (this.client != null) {
			this.client.setScreen(this.parent);
		}
	}

	private void saveAndClose() {
		WobbleConfigManager.INSTANCE.setConfig(this.workingCopy);
		WobbleConfigManager.INSTANCE.save();
		this.close();
	}

	private ConfigSlider addSlider(int x, int y, String label, float min, float max, Supplier<Float> getter, Consumer<Float> setter, ValueFormatter formatter) {
		ConfigSlider slider = new ConfigSlider(x, y, COLUMN_WIDTH, label, min, max, getter, setter, formatter);
		return this.addDrawableChild(slider);
	}

	private void refreshWidgetsFromConfig() {
		this.workingCopy.clamp();
		this.enabledButton.setValue(this.workingCopy.enabled);
		this.presetButton.setValue(WobblePreset.fromId(this.workingCopy.preset));
		this.affectMobsButton.setValue(this.workingCopy.affectMobs);
		this.affectPlayersButton.setValue(this.workingCopy.affectPlayers);
		this.affectArmorStandsButton.setValue(this.workingCopy.affectArmorStands);
		this.strengthSlider.refreshFromConfig();
		this.durationSlider.refreshFromConfig();
		this.speedSlider.refreshFromConfig();
		this.bodyRollSlider.refreshFromConfig();
		this.bodyYawSlider.refreshFromConfig();
		this.headCounterSlider.refreshFromConfig();
		this.limbAmountSlider.refreshFromConfig();
		this.bounceSlider.refreshFromConfig();
		this.armorStandDurationSlider.refreshFromConfig();
	}

	private static String format(String pattern, float value) {
		return String.format(Locale.ROOT, pattern, value);
	}

	private interface ValueFormatter {
		String format(float value);
	}

	private static final class ConfigSlider extends SliderWidget {
		private final String label;
		private final float min;
		private final float max;
		private final Supplier<Float> getter;
		private final Consumer<Float> setter;
		private final ValueFormatter formatter;

		private ConfigSlider(int x, int y, int width, String label, float min, float max, Supplier<Float> getter, Consumer<Float> setter, ValueFormatter formatter) {
			super(x, y, width, 20, Text.empty(), 0.0D);
			this.label = label;
			this.min = min;
			this.max = max;
			this.getter = getter;
			this.setter = setter;
			this.formatter = formatter;
			this.refreshFromConfig();
		}

		@Override
		protected void updateMessage() {
			this.setMessage(Text.literal(this.label + ": " + this.formatter.format(this.getValue())));
		}

		@Override
		protected void applyValue() {
			this.setter.accept(this.getValue());
		}

		private float getValue() {
			return this.min + (float) this.value * (this.max - this.min);
		}

		private void refreshFromConfig() {
			float current = MathHelper.clamp(this.getter.get(), this.min, this.max);
			this.value = (current - this.min) / (this.max - this.min);
			this.updateMessage();
		}
	}
}
