package com.change_vision.astah.extension.plugin.script;

import java.util.prefs.*;

public class ConfigManager {
    private static final ConfigManager configManager = new ConfigManager();
    private static Preferences prefs;
    private static final String KEY_CONSOLE_CLEAR = "console_clear";

    public ConfigManager() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }
    
    public static ConfigManager getInstance() {
        return configManager;
    }

    public void save() {
        try{
            prefs.flush();
        }catch(BackingStoreException ex){
            ex.printStackTrace();
        }
    }

    public boolean isConsoleClear() {
        return prefs.getBoolean(KEY_CONSOLE_CLEAR, false);
    }
    
    public void setConsoleClear(boolean isClear) {
        prefs.putBoolean(KEY_CONSOLE_CLEAR, isClear);
    }
}
