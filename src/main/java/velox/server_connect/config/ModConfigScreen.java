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

        ConfigCategory mainButton = builder.getOrCreateCategory(Text.translatable("category.velox-server-connect.mainButton"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        mainButton.addEntry(entryBuilder.startTextField(Text.translatable("field.velox-server-connect.modpack_name"), ModConfig.modpackName)
                .setDefaultValue("")
                .setSaveConsumer(newValue -> ModConfig.modpackName = newValue)
                .build());



        return builder.build();
    }

}
