package global.oskar.gottagofast.config;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "gottagofast", wrapperName = "ModConfig")
public class ConfigModel {
    public boolean hideConsoleWarnings = false;

    public float playerLimit = 100.0F;
    public float playerFallFlyingLimit = 300.0F;
    public float vehicleLimit = 100.0F;
}
