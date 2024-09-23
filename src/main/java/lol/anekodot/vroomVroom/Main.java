package lol.anekodot.vroomVroom;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import lol.anekodot.vroomVroom.commands.DebugCommand;
import lol.anekodot.vroomVroom.commands.ShulkerStoreCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CommandService commands = Drink.get(this);

        commands.register(new DebugCommand(), "debug", "dbg", "d");
        commands.register(new ShulkerStoreCommand(), "shulkerstore", "st", "s");

        commands.registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
