package com.voxel0046.enderbutt;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    // Supports: #RRGGBB and &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)&?#([A-F0-9]{6})");

    private ColorUtil() {}

    public static String color(String text) {
        if (text == null) return "";

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder out = new StringBuilder();
        int last = 0;

        while (matcher.find()) {
            out.append(text, last, matcher.start());
            out.append(toSpigotHex(matcher.group(1)));
            last = matcher.end();
        }

        out.append(text.substring(last));

        return ChatColor.translateAlternateColorCodes('&', out.toString());
    }

    private static String toSpigotHex(String hex) {
        StringBuilder sb = new StringBuilder("§x");
        for (char c : hex.toCharArray()) {
            sb.append('§').append(c);
        }
        return sb.toString();
    }
}
