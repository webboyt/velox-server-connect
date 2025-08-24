package velox.server_connect.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addPrivateServerButton(CallbackInfo info) {
        // Coordinates and size for the button
        int width = 200;
        int height = 20;
        int x = (this.width - width) / 2;
        int y = this.height / 4 + 48; // adjust vertical position

        this.addDrawableChild(
                ButtonWidget.builder(Text.of("Connect to Velox Cobblemon Server"), button -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            String ip = "mc.legion-networks.com";
                            int port = 25565;
                            ServerInfo data = new ServerInfo("Velox Cobblemon Server" ,ip + ":" + port, ServerInfo.ServerType.LAN);
                            ConnectScreen.connect(this, client, new ServerAddress(ip, port),data,true,null);
                        })
                        .dimensions(x, y -45, width, height)
                        .build()
        );
    }
}


