package resolutioncontrol.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import resolutioncontrol.ResolutionControlMod;


public final class SettingsScreen extends Screen {
	private static final Identifier backgroundTexture = ResolutionControlMod.identifier("textures/gui/settings.png");
	
	private static Text text(String path, Object... args) {
		return new TranslatableText("screen." + ResolutionControlMod.MOD_ID + ".settings." + path, args);
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
		super(text("title"));
		
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
			new LiteralText("-"),
			button -> changeScaleFactor(-1));
		addButton(decreaseButton);
		
		increaseButton = new ButtonWidget(
			centerX + buttonOffset - buttonSize / 2, buttonY,
			buttonSize, buttonSize,
				new LiteralText("+"),
			button -> changeScaleFactor(+1)
		);
		addButton(increaseButton);
		
		doneButton = new ButtonWidget(
			centerX - 100 / 2, startY + containerHeight - 10 - 20,
			100, buttonSize,
			new TranslatableText("gui.done"),
			button -> client.openScreen(parent)
		);
		addButton(doneButton);
		
		updateButtons();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float time) {
		assert client != null;
		
		if (client.world == null) {
			renderBackgroundTexture(0);
		}
		
		GlStateManager.enableAlphaTest();
		client.getTextureManager().bindTexture(backgroundTexture);
		GlStateManager.color4f(1, 1, 1, 1);
		
		int textureWidth = 256;
		int textureHeight = 192;
		drawTexture(
			matrices,
			centerX - textureWidth / 2, centerY - textureHeight / 2,
			0, 0,
			textureWidth, textureHeight
		);
		
		drawCenteredString(matrices, getTitle().getString(), centerX, startY + 10, 0x404040);
		
		Text scaleFactor = text("current", mod.getScaleFactor());
		drawCenteredString(matrices, scaleFactor.getString(), centerX, centerY - 20, 0x000000);
		
		super.render(matrices, mouseX, mouseY, time); // buttons
	}
	
	private void drawCenteredString(MatrixStack matrices, String text, int x, int y, int color) {
		textRenderer.draw(matrices, text, x - textRenderer.getWidth(text) / 2, y, color);
	}
	
	private void changeScaleFactor(int change) {
		int scaleFactor = mod.getScaleFactor() + change;
		
		if (scaleFactor < 1) {
			scaleFactor = 1;
		}
		mod.setScaleFactor(scaleFactor);
		
		updateButtons();
	}
	
	private void updateButtons() {
		decreaseButton.active = mod.getScaleFactor() > 1;
	}
}
