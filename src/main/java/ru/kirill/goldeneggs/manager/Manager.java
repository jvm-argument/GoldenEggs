package ru.kirill.goldeneggs.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.EntityType;
import ru.kirill.goldeneggs.egg.Egg;
import ru.kirill.goldeneggs.Main;
import ru.kirill.goldeneggs.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    Main plugin;
    NamespacedKey key;
    NamespacedKey pickKey;
    HashMap<String, Egg> eggs = new HashMap<String, Egg>();

    public Manager(Main plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "egg_type");
        this.pickKey = new NamespacedKey(plugin, "jake_pick");
    }

    public void load() {
        eggs.clear();

        ConfigurationSection items = plugin.getConfig().getConfigurationSection("items");
        if (items == null) {
            return;
        }

        for (String id : items.getKeys(false)) {
            ConfigurationSection sec = items.getConfigurationSection(id);
            if (sec == null) continue;

            try {
                Egg e = parse(id, sec);
                if (e != null) {
                    eggs.put(id.toLowerCase(), e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    Egg parse(String id, ConfigurationSection sec) {
        String name = Utils.color(sec.getString("display_name", id));
        String matName = sec.getString("material", "EGG");
        Material mat = Material.matchMaterial(matName.toUpperCase());
        if (mat == null) {
            mat = Material.EGG;
        }
        List<String> lore = Utils.colorList(sec.getStringList("lore"));
        List<Egg.MobData> mobs = new ArrayList<Egg.MobData>();
        List<Map<?, ?>> raw = sec.getMapList("chances");
        for (int i = 0; i < raw.size(); i++) {
            Map<?, ?> map = raw.get(i);
            Object mobObj = map.get("mob");
            Object chObj = map.get("chance");
            if (mobObj == null || chObj == null) continue;
            String mobName = String.valueOf(mobObj).toUpperCase();
            EntityType et;
            try {
                et = EntityType.valueOf(mobName);
            } catch (IllegalArgumentException ex) {
                continue;
            }
            double ch;
            if (chObj instanceof Number) {
                ch = ((Number) chObj).doubleValue();
            } else {
                ch = Double.parseDouble(String.valueOf(chObj));
            }
            boolean baby = false;
            if (map.get("baby") != null) {
                baby = Boolean.parseBoolean(String.valueOf(map.get("baby")));
            }
            mobs.add(new Egg.MobData(et, ch, baby));
        }
        if (mobs.size() == 0) {
        }
        return new Egg(id, name, mat, lore, mobs);
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
        int amt = amount;
        if (amt < 1) amt = 1;
        ItemStack item = new ItemStack(e.mat, amt);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(e.name);
            meta.setLore(e.lore);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, e.id);
            item.setItemMeta(meta);
        }
        return item;
    }

    public Egg fromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return null;
        return get(id);
    }

    // jake кеирка
    // fix name кирки and fix lore
    public ItemStack makePick() {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("pickaxe");
        Material mat = Material.GOLDEN_PICKAXE;
        String name = "Золотая кирка Джейка";
        List<String> lore = new ArrayList<String>();
        if (sec != null) {
            String m = sec.getString("material", "GOLDEN_PICKAXE");
            Material mm = Material.matchMaterial(m.toUpperCase());
            if (mm != null) mat = mm;
            name = Utils.color(sec.getString("display_name", name));
            lore = Utils.colorList(sec.getStringList("lore"));
        }
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(pickKey, PersistentDataType.STRING, "yes");
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isPick(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        String v = meta.getPersistentDataContainer().get(pickKey, PersistentDataType.STRING);
        return v != null;
    }
}
