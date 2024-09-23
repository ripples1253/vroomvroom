package lol.anekodot.vroomVroom.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.anekodot.vroomVroom.Developer;
import lol.anekodot.vroomVroom.util.Messages;
import org.bukkit.entity.Player;

public class DebugCommand {
    @Command(name = "",
            desc = "Toggle debugging")
    public void handleDebugToggle(@Sender Player sender) {
        if (Developer.getDebugEnabledPlayers().contains(sender))
            Developer.getDebugEnabledPlayers().remove(sender);
        else
            Developer.getDebugEnabledPlayers().add(sender);

        sender.sendMessage(Messages.DEBUG_TOGGLE.get());
    }
}