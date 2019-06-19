package resolutioncontrol.util;

public final class Config {
	public static Config getInstance() {
		return ConfigHandler.instance.getConfig();
	}
	
	public int scaleFactor = 2;
}
