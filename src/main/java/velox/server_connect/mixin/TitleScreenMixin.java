package velox.server_connect.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import velox.server_connect.config.ModConfig;

import java.awt.*;
import java.util.Objects;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private final MultiplayerServerListPinger pinger = new MultiplayerServerListPinger();

    @Unique
    private ButtonWidget serverButton = null;

    @Unique
    private final ModConfig config = ModConfig.INSTANCE;

    @Inject(method = "init", at = @At("TAIL"))
    private void addPrivateServerButton(CallbackInfo info) {

        removeRealmsButton();

        // Coordinates and size for the button
        int width = 200;
        int height = 20;
        int x = (this.width - width) / 2;

        int y = this.height / 4 + (24 * config.buttonYOffset); // adjust vertical position

        // Server info
        final MinecraftClient client = MinecraftClient.getInstance();

        String packName = "";
        if (!Objects.equals(config.modpackName, ""))
        {
            packName = config.modpackName + " ";
        }

        final String serverName = "Velox " + packName + "Server";
        final String ip = config.serverIp;
        final int port = config.serverPort;

        final ServerInfo data = new ServerInfo(serverName ,ip + ":" + port, ServerInfo.ServerType.OTHER);
        final ServerAddress address = new ServerAddress(ip, port);

        final String defaultMessage = "Connect to " + serverName;

        // Server button
        serverButton = ButtonWidget.builder(Text.of("Pinging..."), button -> {
                    ConnectScreen.connect(this, client, address, data,true,null);
                })
                .dimensions(x, y, width, height)
                .build();

        this.addDrawableChild(serverButton);

        try {
            pinger.add(data, () -> { }, () -> {
                boolean isOnline = data.ping != -1;

                if (isOnline) {
                    String playersText = data.playerCountLabel != null ? data.playerCountLabel.getString() : "?/?";
                    String pingText = data.ping < 0 ? "??ms" : data.ping + "ms";

                    serverButton.setMessage(Text.literal(defaultMessage));

                    if (config.showTooltip)
                        serverButton.setTooltip(Tooltip.of(setTooltipText(true, pingText, playersText)));
                } else
                {
                    serverButton.setMessage(Text.literal(serverName  + " - ").append(Text.literal("Offline").formatted(Formatting.RED)));
                    if (config.showTooltip)
                        serverButton.setTooltip(Tooltip.of(setTooltipText(false, "", "")));
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

    @Unique
    private void removeRealmsButton() {
        if (!config.hideRealmsButton)
            return;

        for (Element element : this.children()) {
            if (element instanceof ButtonWidget button) {

                String realmsText = Text.translatable("menu.online").getString();
                if (button.getMessage().getString().equalsIgnoreCase(realmsText)) {
                    button.visible = false;
                    button.active = false;
                }

                String singleplayerText = Text.translatable("menu.singleplayer").getString();
                String multiplayerText = Text.translatable("menu.multiplayer").getString();
                if (button.getMessage().getString().equalsIgnoreCase(singleplayerText) || button.getMessage().getString().equalsIgnoreCase(multiplayerText))
                {
                    button.setY(button.getY() + 24);
                }
            }
        }
    }

    @Unique
    private Text setTooltipText(boolean isOnline, String pingText, String playersText) {

        String tooltipString = "[";
        String dividerString = " | ";
        String statusString = config.showOnlineStatus ? "Status - " : "";
        String onlineString = config.showOnlineStatus ? (isOnline ? "Online" : "Offline") : "";

        String pingString = config.showPing ? (config.showOnlineStatus && isOnline ? dividerString + pingText : pingText) : "";
        String playersString = config.showPlayers ? (config.showOnlineStatus && isOnline || config.showPing && isOnline ? dividerString + playersText : playersText) : "";
        String endString = "]";

        MutableText tooltipText = Text.literal(tooltipString).append(Text.literal(statusString));
        MutableText onlineText = Text.literal(onlineString).formatted(isOnline ? Formatting.GREEN : Formatting.RED);
        MutableText remainingText = Text.literal(pingString).append(playersString).append(endString);

        return tooltipText.append(onlineText).append(remainingText);

    }
}


