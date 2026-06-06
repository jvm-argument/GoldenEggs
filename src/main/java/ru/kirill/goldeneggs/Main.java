package ru.kirill.goldeneggs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kirill.goldeneggs.command.Commands;
import ru.kirill.goldeneggs.db.DBHelper;
import ru.kirill.goldeneggs.event.Events;
import ru.kirill.goldeneggs.manager.Manager;
import ru.kirill.goldeneggs.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    Manager manager;
    DBHelper db;
    FileConfiguration mobsCfg;
    File mobsFile;

    public void onEnable() {
        saveDefaultConfig();
        loadMobsFile();
        initManager();
        regCommands();
        loadDB();
        regEvents();
    }

    public void onDisable() {
        if (db != null) db.close();
    }

    public void reloadAll() {
        reloadConfig();
        loadMobsFile();
        manager.load();
    }

    public FileConfiguration getMobs() {
        if (mobsCfg == null) loadMobsFile();
        return mobsCfg;
    }

    public String mobName(org.bukkit.entity.EntityType type) {
        if (type == null) return "?";
        String def = type.name();
        return getMobs().getString(type.name(), def);
    }

    // я чусвтую что это писал не человек а ИИ
    public String getPrefix() {
        return Utils.color(getConfig().getString("prefix"));
    }

    public String getMsg(String key) {
        String raw = getConfig().getString("messages." + key, "");
        String pref = getConfig().getString("prefix", "");
        return Utils.color(raw.replace("{prefix}", pref));
    }

    public List<String> getHelp() {
        List<String> raw = getConfig().getStringList("messages.help-message");
        String pref = getConfig().getString("prefix", "");
        List<String> out = new ArrayList<String>();
        for (int i = 0; i < raw.size(); i++) {
            String line = raw.get(i).replace("{prefix}", pref);
            out.add(Utils.color(line));
        }
        return out;
    }

    // TODO ЙОУ ЧТО ЗА НОВЫЕ ТЕХНОЛОГИИ
    // Какой бы мне пидорас не писал за эти методы
    // я начал так делать из за пастинга читов
    // потому что там так намного лучше
    // из за того что ты эконимишь место в методе инитиазиции чита
    // ПОЭТОМУ ИДИ НАХУЙ ЕСЛИ ТЫ ЭТО ЧИТАЕШЩЬ УРООД
    public void loadDB() {
        db = new DBHelper(this);
        db.start();
    }

    public void initManager() {
        manager = new Manager(this);
        manager.load();
    }
    public void regCommands() {
        getCommand("goldenegg").setExecutor(new Commands(this, manager));
        getCommand("goldenegg").setTabCompleter(new Commands(this, manager));
    }
    public void regEvents() {
        getServer().getPluginManager().registerEvents(new Events(this, manager, db), this);
    }
    void loadMobsFile() {
        mobsFile = new File(getDataFolder(), "mobs.yml");
        if (!mobsFile.exists()) {
            saveResource("mobs.yml", false);
        }
        mobsCfg = YamlConfiguration.loadConfiguration(mobsFile);
    }
}
