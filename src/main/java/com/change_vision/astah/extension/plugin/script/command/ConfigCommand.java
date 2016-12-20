package com.change_vision.astah.extension.plugin.script.command;

import com.change_vision.astah.extension.plugin.script.ConfigWindow;

public class ConfigCommand {
    public static void excute(String[] args) {
        ConfigWindow dlg = new ConfigWindow();
        dlg.setVisible(true);
    }
}