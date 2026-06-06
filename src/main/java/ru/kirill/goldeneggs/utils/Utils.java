package ru.kirill.goldeneggs.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    static Pattern hex = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String color(String s) {
        if (s == null) return "";
        Matcher m = hex.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String h = m.group(1);
            m.appendReplacement(sb, Matcher.quoteReplacement(ChatColor.of("#" + h).toString()));
        }
        m.appendTail(sb);
        String res = ChatColor.translateAlternateColorCodes('&', sb.toString());
        return res;
    }

    public static List<String> colorList(List<String> list) {
        List<String> out = new ArrayList<String>();
        if (list == null) return out;
        for (int i = 0; i < list.size(); i++) {
            out.add(color(list.get(i)));
        }
        return out;
    }
}
