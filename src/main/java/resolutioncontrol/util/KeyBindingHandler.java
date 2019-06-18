package resolutioncontrol.util;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;

public abstract class KeyBindingHandler implements ClientTickCallback {
	private final FabricKeyBinding keyBinding;
	private boolean isHoldingKey = false;
	
	protected KeyBindingHandler(FabricKeyBinding keyBinding) {
		this.keyBinding = keyBinding;
	}
	
	@Override
	public final void tick(MinecraftClient e) {
		if (keyBinding.isPressed()) {
			if (!isHoldingKey) {
				handlePress();
			}
			isHoldingKey = true;
		} else {
			isHoldingKey = false;
		}
	}
	
	public abstract void handlePress();
}
