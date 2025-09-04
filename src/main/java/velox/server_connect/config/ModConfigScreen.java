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

        // region Main Button

        ConfigCategory mainButton = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.mainButton"));

        mainButton.addEntry(entryBuilder.startTextField(Text.translatable("field.velox-server-connect.modpack_name"), config.modpackName)
                .setDefaultValue("")
                .setSaveConsumer(newValue -> config.modpackName = newValue)
                .build());

        mainButton.addEntry(entryBuilder.startIntSlider(Text.translatable("field.velox-server-connect.y_offset"), config.buttonYOffset, 0, 2)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> config.buttonYOffset = newValue)
                .build());

        // endregion

        // region Tooltip

        ConfigCategory tooltip = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.tooltip"));

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_tooltip"), config.showTooltip)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showTooltip = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_online_status"), config.showOnlineStatus)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showOnlineStatus = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_ping"), config.showPing)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.showPing = newValue)
                .build());

        tooltip.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.show_players"), config.showPlayers)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showPlayers = newValue)
                .build());

        // endregion

        // region Other

        ConfigCategory other = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.other"));

        other.addEntry(entryBuilder.startBooleanToggle(Text.translatable("field.velox-server-connect.hide_realms_button"), config.hideRealmsButton)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.hideRealmsButton = newValue)
                .build());

        // endregion

        builder.setSavingRunnable(ModConfig::save);

        return builder.build();
    }

}
