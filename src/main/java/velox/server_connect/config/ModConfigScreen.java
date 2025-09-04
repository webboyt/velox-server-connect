package velox.server_connect.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModConfigScreen {

    public static Screen createScreen(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.velox-server-connect.config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        final var config = ModConfig.INSTANCE;

        // region Server Info

        ConfigCategory serverInfo = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.serverInfo"));

        serverInfo.addEntry(entryBuilder.startTextField(Text.translatable("field.velox-server-connect.server_name"), config.serverName)
                .setDefaultValue("Velox $m Server")
                //.setTooltip(Text.translatable("description.velox-server-connect.server_name"))
                .setTooltip(Text.translatable("tooltip.velox-server-connect.server_name"))
                .setSaveConsumer(newValue -> config.serverName = newValue)
                .build());

        serverInfo.addEntry(entryBuilder.startTextField(Text.translatable("field.velox-server-connect.server_ip"), config.serverIp)
                .setDefaultValue("mc.legion-networks.com")
                //.setTooltip(Text.translatable("description.velox-server-connect.server_ip"))
                .setSaveConsumer(newValue -> config.serverIp = newValue)
                .build());

        serverInfo.addEntry(entryBuilder.startIntField(Text.translatable("field.velox-server-connect.server_port"), config.serverPort)
                .setDefaultValue(25565)
                //.setTooltip(Text.translatable("description.velox-server-connect.server_port"))
                .setSaveConsumer(newValue -> config.serverPort = newValue)
                .build());

        // endregion

        // region Main Button

        ConfigCategory mainButton = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.mainButton"));

        mainButton.addEntry(entryBuilder.startTextField(Text.translatable("field.velox-server-connect.button_text"), config.buttonText)
                .setDefaultValue("Connect to $s")
                //.setTooltip(Text.translatable("description.velox-server-connect.button_text"))
                .setTooltip(Text.translatable("tooltip.velox-server-connect.button_text"))
                .setSaveConsumer(newValue -> config.buttonText = newValue)
                .build());

        mainButton.addEntry(entryBuilder.startTextField(Text.translatable("field.velox-server-connect.modpack_name"), config.modpackName)
                .setDefaultValue("")
                //.setTooltip(Text.translatable("description.velox-server-connect.modpack_name"))
                .setSaveConsumer(newValue -> config.modpackName = newValue)
                .build());

        mainButton.addEntry(entryBuilder.startIntSlider(Text.translatable("field.velox-server-connect.y_offset"), config.buttonYOffset, 0, 2)
                .setDefaultValue(1)
                //.setTooltip(Text.translatable("description.velox-server-connect.y_offset"))
                .setSaveConsumer(newValue -> config.buttonYOffset = newValue)
                .build());

        // endregion

        // region Tooltip

        ConfigCategory tooltip = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.tooltip"));

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_tooltip"), config.showTooltip)
                .setDefaultValue(true)
                //.setTooltip(Text.translatable("description.velox-server-connect.show_tooltip"))
                .setSaveConsumer(newValue -> config.showTooltip = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_server_name"), config.showServerName)
                .setDefaultValue(false)
                //.setTooltip(Text.translatable("description.velox-server-connect.show_server_name"))
                .setSaveConsumer(newValue -> config.showServerName = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_online_status"), config.showOnlineStatus)
                .setDefaultValue(true)
                //.setTooltip(Text.translatable("description.velox-server-connect.show_online_status"))
                .setSaveConsumer(newValue -> config.showOnlineStatus = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_ping"), config.showPing)
                .setDefaultValue(false)
                //.setTooltip(Text.translatable("description.velox-server-connect.show_ping"))
                .setSaveConsumer(newValue -> config.showPing = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_players"), config.showPlayers)
                .setDefaultValue(true)
                //.setTooltip(Text.translatable("description.velox-server-connect.show_players"))
                .setSaveConsumer(newValue -> config.showPlayers = newValue)
                .build());

        // endregion

        // region Other

        ConfigCategory other = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.other"));

        other.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.hide_realms"), config.hideRealmsButton)
                .setDefaultValue(true)
                //.setTooltip(Text.translatable("description.velox-server-connect.hide_realms"))
                .setSaveConsumer(newValue -> config.hideRealmsButton = newValue)
                .build());

        // endregion

        builder.setSavingRunnable(ModConfig::save);

        return builder.build();
    }

}
