package resolutioncontrol.mixin;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import resolutioncontrol.ResolutionControlMod;

@Mixin(Window.class)
public class WindowMixin {
	@Shadow
	private int framebufferWidth;
	@Shadow
	private int framebufferHeight;
	@Shadow
	private double scaleFactor;
	
	/**
	 @author Julian Dunskus
	 */
	@Overwrite
	public int getFramebufferWidth() {
		return scale(framebufferWidth);
	}
	
	/**
	 @author Julian Dunskus
	 */
	@Overwrite
	public int getFramebufferHeight() {
		return scale(framebufferHeight);
	}
	
	/**
	 @author Julian Dunskus
	 */
	@Overwrite
	public double getScaleFactor() {
		return scaleFactor / ResolutionControlMod.getInstance().getCurrentScaleFactor();
	}
	
	private int scale(int value) {
		int scaleFactor = ResolutionControlMod.getInstance().getCurrentScaleFactor();
		return (int) Math.ceil(1d * value / scaleFactor);
	}
	
	@Inject(at = @At("RETURN"), method = "onFramebufferSizeChanged")
	private void onFramebufferSizeChanged(CallbackInfo callbackInfo) {
		ResolutionControlMod.getInstance().onResolutionChanged();
	}
	
	@Inject(at = @At("RETURN"), method = "method_4483")
	private void onMethod4483(CallbackInfo callbackInfo) {
		ResolutionControlMod.getInstance().onResolutionChanged();
	}
}
