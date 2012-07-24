package com.change_vision.astah.extension.plugin.script.command;

import com.change_vision.astah.extension.plugin.script.ScriptViewContext;

public class SaveAsCommand {

    public static void execute(ScriptViewContext context) {
        context.setCurrentFile(null);
        SaveCommand.execute(context);
    }
}
