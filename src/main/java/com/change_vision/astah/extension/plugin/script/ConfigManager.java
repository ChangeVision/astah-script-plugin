package com.change_vision.astah.extension.plugin.script;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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
    public static final int FONT_SMALL_SIZE = 16;
    public static final int FONT_MEDIUM_SIZE = 20;
    public static final int FONT_LARGE_SIZE = 24;
    private static Map<String, Integer> fontSizeMap;
    
    public static ConfigManager getInstance() {
        return configManager;
    }

    private ConfigManager() {
        prefs = Preferences.userNodeForPackage(this.getClass());
        fontSizeMap = new HashMap<String, Integer>() {
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
        return prefs.get(KEY_FONT_SIZE, FONT_SMALL);
    }

    public void setStoredFontSize(String fontSize) {
        prefs.put(KEY_FONT_SIZE, fontSize);
    }
    
    public int getFontSize() {
        String key = getStoredFontSize();
        if (! fontSizeMap.containsKey(key)) {
            return fontSizeMap.get(FONT_SMALL);
        }
        return fontSizeMap.get(key);
    }
    
}
