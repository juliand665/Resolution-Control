package resolutioncontrol;

import com.sun.istack.internal.Nullable;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlFramebuffer;
import net.minecraft.client.util.Window;

public class ResolutionControlMod implements ModInitializer {
	private static ResolutionControlMod instance;
	
	public static ResolutionControlMod getInstance() {
		return instance;
	}
	
	private boolean shouldScale = false;
	private int scaleFactor = 8;
	@Nullable
	private GlFramebuffer framebuffer;
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		instance = this;
	}
	
	public int getCurrentScaleFactor() {
		return shouldScale ? scaleFactor : 1;
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
	
	public void setScaleFactor(int scaleFactor) {
		if (scaleFactor == this.scaleFactor) return;
		
		this.scaleFactor = scaleFactor;
		
		if (shouldScale) {
			updateViewport();
		}
		
		updateFramebufferSize();
	}
	
	public void onResolutionChanged() {
		System.out.println("resolution changed!");
		if (framebuffer != null) {
			updateFramebufferSize();
		}
	}
	
	private void updateFramebufferSize() {
		Window window = MinecraftClient.getInstance().window;
		boolean prev = shouldScale;
		shouldScale = true;
		System.out.println("window size is " + window.getWidth() + "×" + window.getHeight());
		System.out.println("window framebuffer size is " + window.getFramebufferWidth() + "×" + window.getFramebufferHeight());
		framebuffer.resize(window.getFramebufferWidth(), window.getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
		shouldScale = prev;
	}
	
	private void updateViewport() {
		MinecraftClient.getInstance().window.method_4493(MinecraftClient.IS_SYSTEM_MAC);
	}
}
