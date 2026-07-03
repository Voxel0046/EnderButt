package com.voxel0046.enderbutt;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    // Supports: #RRGGBB and &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)&?#([A-F0-9]{6})");

    private ColorUtil() {}

    public static String color(String text) {
        if (text == null) return "";

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = ChatColor.of("#" + hex).toString();
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
