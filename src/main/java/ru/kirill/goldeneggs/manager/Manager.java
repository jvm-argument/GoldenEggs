package ru.kirill.goldeneggs.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import ru.kirill.goldeneggs.Main;
import ru.kirill.goldeneggs.egg.Egg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {

    public Main plugin;
    NamespacedKey key;
    HashMap<String, Egg> eggs = new HashMap<String, Egg>();

    public Manager(Main plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "egg_type");
    }

    public void load() {
        eggs.clear();
        // TODO читать секцию items из конфига и парсить каждое яйцо
    }

    public Egg get(String id) {
        if (id == null) return null;
        return eggs.get(id.toLowerCase());
    }

    public List<String> getList() {
        List<String> l = new ArrayList<String>();
        for (String k : eggs.keySet()) {
            l.add(k);
        }
        return l;
    }

    public ItemStack makeEgg(Egg e, int amount) {
        // TODO собрать ItemStack, поставить name, lore, pdc тег
        return null;
    }

    public Egg fromItem(ItemStack item) {
        // TODO достать pdc тег и вернуть яйцо
        return null;
    }
}

