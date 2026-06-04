package ru.kirill.goldeneggs.egg;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import java.util.List;
import java.util.Random;

public class Egg {

    public String id;
    public String name;
    public Material mat;
    public List<String> lore;
    public List<MobData> mobs;

    Random rnd = new Random();

    public Egg(String id, String name, Material mat, List<String> lore, List<MobData> mobs) {
        this.id = id;
        this.name = name;
        this.mat = mat;
        this.lore = lore;
        this.mobs = mobs;
    }

    // выбор моба по шансам
    public MobData roll() {
        // TODO посчитать сумму шансов и рандомить взвешенно
        if (mobs == null || mobs.size() == 0) return null;
        return null;
    }

    public static class MobData {
        public EntityType type;
        public double chance;
        public boolean baby;

        public MobData(EntityType type, double chance, boolean baby) {
            this.type = type;
            this.chance = chance;
            this.baby = baby;
        }
    }
}

