package velox.server_connect.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;
import velox.server_connect.VeloxServerConnect;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

    // Button
    public String modpackName = "";
    public int buttonYOffset = 1;

    // Tooltip
    public boolean showTooltip = true;
    public boolean showOnlineStatus = true;
    public boolean showPing = false;
    public boolean showPlayers = true;

    // Realms
    public boolean hideRealmsButton = true;


    public static ModConfig INSTANCE = new ModConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", VeloxServerConnect.MOD_ID + ".json");

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                INSTANCE = GSON.fromJson(json, ModConfig.class);
            } else {
                save(); // Save defaults if config doesn't exist
            }
        } catch (IOException e) {
            System.err.println("[MyMod] Failed to load config: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(INSTANCE);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("[MyMod] Failed to save config: " + e.getMessage());
        }
    }
}
