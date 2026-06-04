package ru.kirill.goldeneggs.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.kirill.goldeneggs.Main;
import ru.kirill.goldeneggs.db.DBHelper;
import ru.kirill.goldeneggs.manager.Manager;

public class Events implements Listener {

    public Main plugin;
    public Manager manager;
    public DBHelper db;

    public Events(Main plugin, Manager manager, DBHelper db) {
        this.plugin = plugin;
        this.manager = manager;
        this.db = db;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        // TODO проверить что в руке наше яйцо
        // TODO setCancelled(true)
        // TODO если ПКМ по спавнеру - привязать через db.bind и списать яйцо
    }

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent e) {
        // TODO если спавнер привязан - заролить моба и подменить
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        // TODO если сломали спавнер - db.remove
    }
}

