package velox.server_connect.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public class ModConfig {

    // Button
    @ConfigEntry.Category("Connect Button")
    public static String modpackName = "";
    @ConfigEntry.Category("Connect Button")
    public static int buttonYOffset = 24;

    // Tooltip
    @ConfigEntry.Category("Tooltip")
    public static boolean showTooltip = true;
    @ConfigEntry.Category("Tooltip")
    public static boolean showOnlineStatus = true;
    @ConfigEntry.Category("Tooltip")
    public static boolean showPing = false;
    @ConfigEntry.Category("Tooltip")
    public static boolean showPlayers = true;

    // Realms
    @ConfigEntry.Category("Other")
    public static boolean showRealmsButton = false;

}
