package ru.kirill.goldeneggs.db;

import org.bukkit.Location;
import org.bukkit.World;
import ru.kirill.goldeneggs.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class DBHelper {

    Main plugin;
    Connection con;
    HashMap<String, String> data = new HashMap<String, String>();

    public DBHelper(Main plugin) {
        this.plugin = plugin;
    }
    // сори я не умею с бд работать
    // так что это делал клод опус 4.8 в Max режиме
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

            loadAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void loadAll() {
        data.clear();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT loc_key, egg_id FROM spawners");
            while (rs.next()) {
                data.put(rs.getString("loc_key"), rs.getString("egg_id"));
            }
            rs.close();
            st.close();
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
        final String k = makeKey(loc);
        data.put(k, eggId);
        World w = loc.getWorld();
        String wn = "unknown";
        if (w != null) wn = w.getName();
        final String world = wn;
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final String egg = eggId;
        Runnable r = new Runnable() {
            public void run() {
                try {
                    String sql = "INSERT INTO spawners(loc_key, world, x, y, z, egg_id) VALUES(?,?,?,?,?,?) " +
                            "ON CONFLICT(loc_key) DO UPDATE SET egg_id=excluded.egg_id, world=excluded.world, " +
                            "x=excluded.x, y=excluded.y, z=excluded.z";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, k);
                    ps.setString(2, world);
                    ps.setInt(3, x);
                    ps.setInt(4, y);
                    ps.setInt(5, z);
                    ps.setString(6, egg);
                    ps.executeUpdate();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        if (plugin.isEnabled()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, r);
        } else {
            r.run();
        }
    }

    public void remove(Location loc) {
        final String k = makeKey(loc);
        if (data.remove(k) == null) return;
        Runnable r = new Runnable() {
            public void run() {
                try {
                    PreparedStatement ps = con.prepareStatement("DELETE FROM spawners WHERE loc_key = ?");
                    ps.setString(1, k);
                    ps.executeUpdate();
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        if (plugin.isEnabled()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, r);
        } else {
            r.run();
        }
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
