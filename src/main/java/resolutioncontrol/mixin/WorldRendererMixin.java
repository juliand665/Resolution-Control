package resolutioncontrol.mixin;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import resolutioncontrol.ResolutionControlMod;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow
	private Framebuffer entityOutlinesFramebuffer;
	
	@Inject(at = @At("RETURN"), method = "loadEntityOutlineShader")
	private void onLoadEntityOutlineShader(CallbackInfo callbackInfo) {
		ResolutionControlMod.getInstance().resize(entityOutlinesFramebuffer);
	}
	
	@Inject(at = @At("RETURN"), method = "onResized")
	private void onOnResized(CallbackInfo callbackInfo) {
		if (entityOutlinesFramebuffer == null) return;
		ResolutionControlMod.getInstance().resize(entityOutlinesFramebuffer);
	}
}
