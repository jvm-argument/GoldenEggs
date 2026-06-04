package ru.kirill.goldeneggs;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kirill.goldeneggs.command.Commands;
import ru.kirill.goldeneggs.db.DBHelper;
import ru.kirill.goldeneggs.events.Events;
import ru.kirill.goldeneggs.manager.Manager;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public Manager manager;
    public DBHelper db;

    public void onEnable() {
        saveDefaultConfig();

        manager = new Manager(this);
        manager.load();

        db = new DBHelper(this);
        db.start();

        getServer().getPluginManager().registerEvents(new Events(this, manager, db), this);

        PluginCommand c = getCommand("goldenegg");
        if (c != null) {
            Commands cmd = new Commands(this, manager);
            c.setExecutor(cmd);
            c.setTabCompleter(cmd);
        }
        // Plugin startup logic
    }

    public void onDisable() {
        if (db != null) db.close();
        // Plugin shutdown logic
    }

    public void reloadAll() {
        reloadConfig();
        manager.load();
        // TODO бд тоже наверно перезагрузить?
    }

    public String getPrefix() {
        return getConfig().getString("prefix", "яйца »");
        // TODO покрасить через Utils
    }

    public String getMsg(String key) {
        // TODO
        return "";
    }

    public List<String> getHelp() {
        // TODO достать из конфига messages.help-message
        return new ArrayList<String>();
    }
}
