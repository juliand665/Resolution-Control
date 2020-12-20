package resolutioncontrol.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.*;
import resolutioncontrol.ResolutionControlMod;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements ResolutionControlMod.MutableMinecraftClient {
	@Mutable
	@Final
	@Shadow
	private Framebuffer framebuffer;
	
	@Override
	public void setFramebuffer(Framebuffer framebuffer) {
		this.framebuffer = framebuffer;
	}
}
