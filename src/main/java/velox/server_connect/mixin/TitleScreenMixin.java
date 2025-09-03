package velox.server_connect.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private final MultiplayerServerListPinger pinger = new MultiplayerServerListPinger();

    @Inject(method = "init", at = @At("TAIL"))
    private void addPrivateServerButton(CallbackInfo info) {
        // Coordinates and size for the button
        int width = 200;
        int height = 20;
        int x = (this.width - width) / 2;
        int y = this.height / 4 + 93; // adjust vertical position

        // Server info
        final MinecraftClient client = MinecraftClient.getInstance();

        final String serverName = "Velox Cobblemon Server";
        final String ip = "mc.legion-networks.com";
        final int port = 25565;

        final ServerInfo data = new ServerInfo(serverName ,ip + ":" + port, ServerInfo.ServerType.OTHER);
        final ServerAddress address = new ServerAddress(ip, port);

        final String defaultMessage = "Connect to " + serverName;

        // Server button
        final ButtonWidget serverButton = ButtonWidget.builder(Text.of("Pinging..."), button -> {
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

                    serverButton.setMessage(Text.literal(defaultMessage));
                    serverButton.setTooltip(Tooltip.of(Text.literal("[Status - ").append(Text.literal("Online").formatted(Formatting.GREEN)).append(Text.literal(" | " + playersText + "]"))));
                } else
                {
                    serverButton.setMessage(Text.literal(serverName  + " - ").append(Text.literal("Offline").formatted(Formatting.RED)));
                    serverButton.setTooltip(Tooltip.of(Text.literal("[Status - ").append(Text.literal("Offline").formatted(Formatting.RED)).append(Text.literal("]"))));
                }
            });
        } catch (Exception e) {
            serverButton.setMessage(Text.literal(serverName + " - ").append(Text.literal("ERROR").formatted(Formatting.RED)));
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

    @Inject(method = "init", at = @At("TAIL"))
    private void removeRealmsButton(CallbackInfo info) {
        List<ButtonWidget> toRemove = new ArrayList<>();

        for (Element element : this.children()) {
            if (element instanceof ButtonWidget button) {
                String buttonText = button.getMessage().getString().toLowerCase();

                if (buttonText.contains("realms")) {
                    toRemove.add(button);
                }

                if (buttonText.contains("singleplayer") || buttonText.contains("multiplayer"))
                {
                    button.setY(button.getY() + 24);
                }
            }
        }

        for (ButtonWidget button : toRemove) {
            this.remove(button); // Safe removal after iteration
        }
    }
}


