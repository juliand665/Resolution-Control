package resolutioncontrol;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlFramebuffer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import resolutioncontrol.client.gui.screen.SettingsScreen;
import resolutioncontrol.util.*;

import javax.annotation.Nullable;

public class ResolutionControlMod implements ModInitializer {
	public static final String MOD_ID = "resolutioncontrol";
	
	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
	
	private static ResolutionControlMod instance;
	
	public static ResolutionControlMod getInstance() {
		return instance;
	}
	
	private static FabricKeyBinding settingsKeyBinding = FabricKeyBinding.Builder.create(
		identifier("settings"),
		InputUtil.Type.KEYSYM,
		GLFW.GLFW_KEY_P,
		"Resolution Control"
	).build();
	
	private boolean shouldScale = false;
	@Nullable
	private GlFramebuffer framebuffer;
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		instance = this;
		
		KeyBindingRegistry.INSTANCE.register(settingsKeyBinding);
		
		ClientTickCallback.EVENT.register(new KeyBindingHandler(settingsKeyBinding) {
			@Override
			public void handlePress() {
				MinecraftClient.getInstance().openScreen(new SettingsScreen());
			}
		});
	}
	
	public void setShouldScale(boolean shouldScale) {
		if (shouldScale == this.shouldScale) return;
		
		Window window = MinecraftClient.getInstance().window;
		if (framebuffer == null) {
			this.shouldScale = true; // so we get the right dimensions
			framebuffer = new GlFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
		}
		
		this.shouldScale = shouldScale;
		updateViewport();
		
		boolean shouldUpdateViewport = false;
		if (shouldScale) {
			framebuffer.beginWrite(shouldUpdateViewport);
		} else {
			MinecraftClient.getInstance().getFramebuffer().beginWrite(shouldUpdateViewport);
			framebuffer.draw(window.getFramebufferWidth(), window.getFramebufferHeight());
		}
	}
	
	public int getScaleFactor() {
		return Config.getInstance().scaleFactor;
	}
	
	public void setScaleFactor(int scaleFactor) {
		if (scaleFactor == Config.getInstance().scaleFactor) return;
		
		Config.getInstance().scaleFactor = scaleFactor;
		
		if (shouldScale) {
			updateViewport();
		}
		
		updateFramebufferSize();
		
		ConfigHandler.instance.saveConfig();
	}
	
	public int getCurrentScaleFactor() {
		return shouldScale ? Config.getInstance().scaleFactor : 1;
	}
	
	public void onResolutionChanged() {
		updateFramebufferSize();
	}
	
	private void updateFramebufferSize() {
		if (framebuffer == null) return;
		
		Window window = MinecraftClient.getInstance().window;
		boolean prev = shouldScale;
		shouldScale = true;
		framebuffer.resize(window.getFramebufferWidth(), window.getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
		shouldScale = prev;
	}
	
	private void updateViewport() {
		MinecraftClient.getInstance().window.method_4493(MinecraftClient.IS_SYSTEM_MAC);
	}
}
