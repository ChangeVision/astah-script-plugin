package com.change_vision.astah.extension.plugin.script;

import java.util.prefs.*;

public class ConfigManager {
    public static boolean console_clear = false;
    private static Preferences prefs;
    private static final String KEY = "console_clear";
    
    public ConfigManager(){
        prefs = Preferences.userNodeForPackage(this.getClass());
        load();
    }

    public static void save(Boolean check){
        try{
            prefs.putBoolean(KEY, check);
            prefs.flush();
        }catch(BackingStoreException ex){
            ex.printStackTrace();
        }
    }

    public void load(){
        console_clear = prefs.getBoolean(KEY, false);
    }
}
