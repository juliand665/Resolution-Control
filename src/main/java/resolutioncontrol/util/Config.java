package resolutioncontrol.util;

public final class Config {
	public static Config getInstance() {
		return ConfigHandler.instance.getConfig();
	}
	
	public static int getScaleFactor() {
		return getInstance().scaleFactor;
	}
	
	public int scaleFactor = 1;
}
