package ru.kirill.goldeneggs.db;

import org.bukkit.Location;
import org.bukkit.World;
import ru.kirill.goldeneggs.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;

public class DBHelper {

    public Main plugin;
    Connection con;
    HashMap<String, String> data = new HashMap<String, String>();

    public DBHelper(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        String dbName = plugin.getConfig().getString("db.name", "eggs.db");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        File f = new File(plugin.getDataFolder(), dbName);

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + f.getAbsolutePath());

            Statement st = con.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS spawners (" +
                    "loc_key TEXT PRIMARY KEY, " +
                    "world TEXT NOT NULL, " +
                    "x INTEGER NOT NULL, " +
                    "y INTEGER NOT NULL, " +
                    "z INTEGER NOT NULL, " +
                    "egg_id TEXT NOT NULL)");
            st.close();

            // TODO loadAll()
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String makeKey(Location loc) {
        World w = loc.getWorld();
        String wn = "unknown";
        if (w != null) wn = w.getName();
        return wn + "|" + loc.getBlockX() + "|" + loc.getBlockY() + "|" + loc.getBlockZ();
    }

    public String getEgg(Location loc) {
        return data.get(makeKey(loc));
    }

    public void bind(Location loc, String eggId) {
        data.put(makeKey(loc), eggId);
        // TODO записать в бд (INSERT / ON CONFLICT update), сделать асинхронно
    }

    public void remove(Location loc) {
        data.remove(makeKey(loc));
        // TODO удалить из бд
    }

    public void close() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

