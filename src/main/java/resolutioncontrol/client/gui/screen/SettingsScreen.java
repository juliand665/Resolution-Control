package resolutioncontrol.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import resolutioncontrol.ResolutionControlMod;

import javax.annotation.Nullable;

public final class SettingsScreen extends Screen {
	private static final Identifier backgroundTexture = ResolutionControlMod.identifier("textures/gui/settings.png");
	
	private static Component component(String path, Object... args) {
		return new TranslatableComponent("screen." + ResolutionControlMod.MOD_ID + ".settings." + path, args);
	}
	
	private final int containerWidth = 192;
	private final int containerHeight = 128;
	
	private final ResolutionControlMod mod = ResolutionControlMod.getInstance();
	
	@Nullable
	private final Screen parent;
	
	private ButtonWidget increaseButton;
	private ButtonWidget decreaseButton;
	private ButtonWidget doneButton;
	
	private int centerX;
	private int centerY;
	private int startX;
	private int startY;
	
	public SettingsScreen(Screen parent) {
		super(component("title"));
		
		this.parent = parent;
	}
	
	public SettingsScreen() {
		this(MinecraftClient.getInstance().currentScreen);
	}
	
	@Override
	protected void init() {
		super.init();
		
		centerX = width / 2;
		centerY = height / 2;
		startX = centerX - containerWidth / 2;
		startY = centerY - containerHeight / 2;
		
		int buttonSize = 20;
		int buttonOffset = buttonSize / 2;
		int buttonY = centerY + 5 - buttonSize / 2;
		
		decreaseButton = new ButtonWidget(
			centerX - buttonOffset - buttonSize / 2, buttonY,
			buttonSize, buttonSize,
			"-",
			button -> changeScaleFactor(-1));
		addButton(decreaseButton);
		
		increaseButton = new ButtonWidget(
			centerX + buttonOffset - buttonSize / 2, buttonY,
			buttonSize, buttonSize,
			"+",
			button -> changeScaleFactor(+1)
		);
		addButton(increaseButton);
		
		doneButton = new ButtonWidget(
			centerX - 100 / 2, startY + containerHeight - 10 - 20,
			100, buttonSize,
			I18n.translate("gui.done"),
			button -> minecraft.openScreen(parent)
		);
		addButton(doneButton);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float time) {
		assert minecraft != null;
		
		if (minecraft.world == null) {
			renderDirtBackground(0);
		}
		
		GlStateManager.enableAlphaTest();
		minecraft.getTextureManager().bindTexture(backgroundTexture);
		GlStateManager.color4f(1, 1, 1, 1);
		
		int textureWidth = 256;
		int textureHeight = 192;
		blit(
			centerX - textureWidth / 2, centerY - textureHeight / 2,
			0, 0,
			textureWidth, textureHeight
		);
		
		drawCenteredString(getTitle().getFormattedText(), centerX, startY + 10, 0x404040);
		
		Component scaleFactor = component("current", mod.getScaleFactor());
		drawCenteredString(scaleFactor.getFormattedText(), centerX, centerY - 20, 0x000000);
		
		super.render(mouseX, mouseY, time); // buttons
	}
	
	private void drawCenteredString(String text, int x, int y, int color) {
		font.draw(text, x - font.getStringWidth(text) / 2, y, color);
	}
	
	private void changeScaleFactor(int change) {
		int scaleFactor = mod.getScaleFactor() + change;
		
		if (scaleFactor < 1) {
			scaleFactor = 1;
		}
		mod.setScaleFactor(scaleFactor);
		
		decreaseButton.active = scaleFactor > 1;
	}
}
