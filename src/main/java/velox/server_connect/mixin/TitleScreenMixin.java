package velox.server_connect.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow
    @Final
    private static Logger LOGGER;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private final MultiplayerServerListPinger pinger = new MultiplayerServerListPinger();

    @Inject(method = "init", at = @At("TAIL"))
    private void addPrivateServerButton(CallbackInfo info) {
        // Coordinates and size for the button
        int width = 200;
        int height = 40;
        int x = (this.width - width) / 2;
        int y = this.height / 4 + 48; // adjust vertical position

        // Server info
        final MinecraftClient client = MinecraftClient.getInstance();
        final String ip = "mc.legion-networks.com";
        final int port = 25565;
        final ServerInfo data = new ServerInfo("Velox Cobblemon Server" ,ip + ":" + port, ServerInfo.ServerType.OTHER);
        final ServerAddress address = new ServerAddress(ip, port);

        final String defaultMessage = "Connect to Velox Cobblemon Server";

        // Server button
        final ButtonWidget serverButton = ButtonWidget.builder(Text.of(":)"), button -> {
                    ConnectScreen.connect(this, client, address, data,true,null);
                })
                .dimensions(x, y -45, width, height)
                .build();

        this.addDrawableChild(serverButton);

        try {
            pinger.add(data, () -> { }, () -> {
                boolean isOnline = data.ping != -1;

                if (isOnline) {
                    String playersText = data.playerCountLabel != null ? data.playerCountLabel.getString() : "?/?";

                    serverButton.setMessage(Text.literal(defaultMessage + "\n[Status - ").append(Text.literal("Online").formatted(Formatting.GREEN)).append(Text.literal(" | " + playersText + "]")));
                } else
                {
                    serverButton.setMessage(Text.literal(defaultMessage + "\n[Status - ").append(Text.literal("Offline").formatted(Formatting.RED)).append(Text.literal("]")));
                }
            });
        } catch (Exception e) {
            serverButton.setMessage(Text.literal(defaultMessage + "\nERROR"));
            e.printStackTrace();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.pinger.tick();
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void onRemoved(CallbackInfo ci) {
        this.pinger.cancel();
    }
}


