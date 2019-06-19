package resolutioncontrol.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import resolutioncontrol.ResolutionControlMod;

import java.io.*;

public final class ConfigHandler {
	public static final ConfigHandler instance = new ConfigHandler();
	
	private static File configFile() {
		return new File(MinecraftClient.getInstance().runDirectory, "config/" + ResolutionControlMod.MOD_ID + ".json");
	}
	
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Config config = new Config();
	
	public Config getConfig() {
		return config;
	}
	
	private ConfigHandler() {
		loadConfig();
	}
	
	public void loadConfig() {
		File configFile = configFile();
		if (!configFile.exists()) {
			config = new Config();
			saveConfig();
		}
		
		try (FileReader reader = new FileReader(configFile)) {
			config = gson.fromJson(reader, Config.class);
		} catch (IOException e) {
			System.err.println("Could not load config file at " + configFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
		File configFile = configFile();
		configFile.getParentFile().mkdirs();
		
		try (final Writer writer = new FileWriter(configFile)) {
			writer.write(gson.toJson(config));
		} catch (IOException e) {
			System.err.println("Could not save config file at " + configFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
}
