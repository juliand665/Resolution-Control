package resolutioncontrol.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Framebuffer.class)
public abstract class FramebufferMixin {
    @Shadow public abstract void setTexFilter(int i);

    @Redirect(method = "initFbo", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/platform/GlStateManager;texParameter(III)V"))
    private void onInitFbo(int target, int pname, int param) {
        if (target == GL11.GL_TEXTURE_2D && pname == GL11.GL_TEXTURE_MIN_FILTER) {
            GlStateManager.texParameter(target, pname, GL11.GL_LINEAR);
        } else if (target == GL11.GL_TEXTURE_2D && pname == GL11.GL_TEXTURE_MAG_FILTER) {
            GlStateManager.texParameter(target, pname, GL11.GL_LINEAR);
        } else {
            GlStateManager.texParameter(target, pname, param);
        }
    }

    @Redirect(method = "initFbo", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gl/Framebuffer;setTexFilter(I)V"))
    private void onInitFbo(Framebuffer framebuffer, int i) {
        setTexFilter(GL11.GL_LINEAR);
    }
}
