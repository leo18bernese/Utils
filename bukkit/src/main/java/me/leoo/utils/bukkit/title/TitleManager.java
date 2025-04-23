package me.leoo.utils.bukkit.title;

import com.cryptomorin.xseries.messages.Titles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class TitleManager {

    private final ConfigManager config;
    private final String basePath;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    private final String title;
    private final String subTitle;

    private String message;
    private List<String> messagesList;

    public TitleManager(ConfigManager config, String basePath, int fadeIn, int stay, int fadeOut) {
        this.config = config;
        this.basePath = basePath;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;

        // Title handling
        this.title = config.getString(basePath + ".title");
        this.subTitle = config.getString(basePath + ".sub-title");

        // Message handling
        String messagePath = basePath + ".message";

        Object messageObject = config.getYml().get(messagePath);
        if (messageObject instanceof String) {
            this.message = config.getString(messagePath);
        } else if (messageObject instanceof List) {
            this.messagesList = config.getList(messagePath);
        }
    }

    public TitleManager(ConfigManager config, String basePath) {
        this(config, basePath, 0, 70, 20);
    }

    private void sendMessageInternal(Player player, Function<String, String> replace) {
        if (message != null && !message.isEmpty()) {
            player.sendMessage(replace.apply(message));
        } else if (messagesList != null) {
            messagesList.forEach(msg -> player.sendMessage(replace.apply(msg)));
        }
    }

    public void send(Player player, Function<String, String> replace) {
        sendMessageInternal(player, replace);

        new Titles(replace.apply(this.title), replace.apply(this.subTitle), fadeIn, stay, fadeOut)
                .send(player);
    }

    public void send(Player player) {
        send(player, string -> string);
    }

    public void send(List<Player> players, Function<String, String> replace) {
        players.forEach(player -> send(player, replace));
    }

    public void send(List<Player> players) {
        send(players, string -> string);
    }
}