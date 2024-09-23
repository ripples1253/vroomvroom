package lol.anekodot.vroomVroom;

import lol.anekodot.vroomVroom.util.Messages;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "Developer Logs")
public class Developer {
    @Getter
    private static final List<Player> debugEnabledPlayers;

    static {
        debugEnabledPlayers = new ArrayList<>();
    }

    public static void log(String message, Object... args) {
        for (Player p : debugEnabledPlayers) {
            p.sendMessage(Messages.DEBUG_LOG.get(String.format(message, args)));
        }

        log.info("Developer > " + String.format(message, args));
    }
}
