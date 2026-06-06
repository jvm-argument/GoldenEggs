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

    // хз писал друг, вроде норм работает
    public MobData roll() {
        if (mobs == null || mobs.size() == 0) return null;
        double total = 0;
        for (int i = 0; i < mobs.size(); i++) {
            total = total + mobs.get(i).chance;
        }
        if (total <= 0) return null;
        double r = rnd.nextDouble() * total;
        double cur = 0;
        for (int i = 0; i < mobs.size(); i++) {
            cur = cur + mobs.get(i).chance;
            if (r < cur) {
                return mobs.get(i);
            }
        }
        return mobs.get(mobs.size() - 1);
    }

    // fix
    public MobData find(org.bukkit.entity.EntityType t) {
        if (mobs == null) return null;
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).type == t) {
                return mobs.get(i);
            }
        }
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
