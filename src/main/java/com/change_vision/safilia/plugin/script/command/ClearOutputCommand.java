package com.change_vision.safilia.plugin.script.command;

import com.change_vision.safilia.plugin.script.ScriptViewContext;

public class ClearOutputCommand {
    public static void execute(ScriptViewContext context) {
        context.scriptOutput.clear();
    }
}
