package ru.kirill.goldeneggs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.kirill.goldeneggs.Main;
import ru.kirill.goldeneggs.manager.Manager;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    public Main plugin;
    public Manager manager;

    public Commands(Main plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            // TODO help
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("give")) {
            // TODO /goldenegg give <Ник> <Тип_Яйца> [Кол-во]
            return true;
        }

        if (sub.equals("reload")) {
            plugin.reloadAll();
            sender.sendMessage("reloaded"); // потом нормальное сообщение
            return true;
        }

        // zov пасхалка потом

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> res = new ArrayList<String>();
        // TODO дополнение для give
        return res;
    }
}
