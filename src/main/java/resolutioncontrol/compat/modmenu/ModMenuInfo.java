package resolutioncontrol.compat.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import resolutioncontrol.ResolutionControlMod;
import resolutioncontrol.client.gui.screen.SettingsScreen;

import java.util.function.Function;

public final class ModMenuInfo implements ModMenuApi {
	@Override
	public String getModId() {
		return ResolutionControlMod.MOD_ID;
	}
	
	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return SettingsScreen::new;
	}
}
