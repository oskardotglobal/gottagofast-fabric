package global.oskar.gottagofast.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {

    private final Gson Gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "gottagofast.json");

    private final Boolean debug = false;

    public ConfigHandler() {
        try (FileReader reader = new FileReader(configFile)) {
            Gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            saveConfig(new Config());
        }
    }

    public Config getConfig() {
        try (FileReader reader = new FileReader(configFile)) {
            return Gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            if (debug) e.printStackTrace();
            return new Config();
        }
    }

    private void saveConfig(Config config) {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(Gson.toJson(config));
        } catch (IOException e) {
            if (debug) e.printStackTrace();
        }
    }

    public static class Config {

        private float defaultMaxPlayerSpeed = 1000000.0F;
        private float maxPlayerElytraSpeed = 1000000.0F;
        private float maxPlayerVehicleSpeed = 1000000.0F;

        public float getDefaultMaxPlayerSpeed() {
            return defaultMaxPlayerSpeed;
        }

        public float getMaxPlayerElytraSpeed() {
            return maxPlayerElytraSpeed;
        }

        public float getMaxPlayerVehicleSpeed() {
            return maxPlayerVehicleSpeed;
        }
    }
}
