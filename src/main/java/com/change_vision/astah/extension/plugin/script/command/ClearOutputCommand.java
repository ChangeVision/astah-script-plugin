package com.change_vision.astah.extension.plugin.script.command;

import com.change_vision.astah.extension.plugin.script.ScriptViewContext;
import com.change_vision.astah.extension.plugin.script.util.Messages;

public class ClearOutputCommand {
    public static void execute(ScriptViewContext context) {
        context.scriptOutput.clear();
        context.statusBar.setText(Messages.getMessage("status.clear_console"));
    }
}
