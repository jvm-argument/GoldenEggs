package ru.kirill.goldeneggs.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.List;
import java.util.ArrayList;

public class Utils {

    public static String color(String s) {
        if (s == null) return "";
        // пока без хекса, потом доделаю &#rrggbb
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorList(List<String> list) {
        List<String> out = new ArrayList<String>();
        // TODO покрасить каждую строку
        return out;
    }
}
