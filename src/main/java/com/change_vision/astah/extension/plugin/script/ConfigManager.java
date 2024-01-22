package com.change_vision.astah.extension.plugin.script;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

public class ConfigManager {
    private static final ConfigManager configManager = new ConfigManager();
    private static Preferences prefs;
    private static final String KEY_WINDOW_WIDTH = "window_width";
    private static final String KEY_WINDOW_HEIGHT = "window_height";
    private static final String KEY_WINDOW_DIVIDER_LOCATION = "window_divider_location";
    private static final String KEY_CONSOLE_CLEAR = "console_clear";
    private static final String KEY_FONT_SIZE = "font_size";
    public static final String FONT_SMALL = "small font";
    public static final String FONT_MEDIUM = "medium font";
    public static final String FONT_LARGE = "large font";
    public static final double FONT_SMALL_SIZE = 0.8;
    public static final double FONT_MEDIUM_SIZE = 1.0;
    public static final double FONT_LARGE_SIZE = 1.5;
    private static Map<String, Double> fontSizeRatioMap;

    public static ConfigManager getInstance() {
        return configManager;
    }

    private ConfigManager() {
        prefs = Preferences.userNodeForPackage(this.getClass());
        fontSizeRatioMap = new HashMap<String, Double>() {
            { put(FONT_SMALL, FONT_SMALL_SIZE); }
            { put(FONT_MEDIUM, FONT_MEDIUM_SIZE); }
            { put(FONT_LARGE, FONT_LARGE_SIZE); }
        };
    }

    public void save() {
        try{
            prefs.flush();
        }catch(BackingStoreException ex){
            ex.printStackTrace();
        }
    }

    public int getWindowWidth() {
        return prefs.getInt(KEY_WINDOW_WIDTH, 0);
    }

    public void setWindowWidth(int width) {
        prefs.putInt(KEY_WINDOW_WIDTH, width);
    }

    public int getWindowHeight() {
        return prefs.getInt(KEY_WINDOW_HEIGHT, 0);
    }

    public void setWindowHeight(int height) {
        prefs.putInt(KEY_WINDOW_HEIGHT, height);
    }

    public int getWindowDividerLocation() {
        return prefs.getInt(KEY_WINDOW_DIVIDER_LOCATION, 0);
    }

    public void setWindowDividerLocation(int location) {
        prefs.putInt(KEY_WINDOW_DIVIDER_LOCATION, location);
    }

    public boolean isAutoConsoleClear() {
        return prefs.getBoolean(KEY_CONSOLE_CLEAR, false);
    }

    public void setAutoConsoleClear(boolean isClear) {
        prefs.putBoolean(KEY_CONSOLE_CLEAR, isClear);
    }

    public String getStoredFontSize() {
        return prefs.get(KEY_FONT_SIZE, FONT_MEDIUM);
    }

    public void setStoredFontSize(String fontSize) {
        prefs.put(KEY_FONT_SIZE, fontSize);
    }
    
    public int getFontSize() {
        Font font = (Font) UIManager.get("TextArea.font");
        int fontSize = font.getSize();
        String key = getStoredFontSize();
        if (! fontSizeRatioMap.containsKey(key)) {
            key = FONT_MEDIUM;
        }

        return (int) (fontSize * fontSizeRatioMap.get(key));
    }
}
