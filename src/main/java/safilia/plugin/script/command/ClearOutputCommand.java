package safilia.plugin.script.command;

import safilia.plugin.script.ScriptViewContext;

public class ClearOutputCommand {
    public static void execute(ScriptViewContext context) {
        context.scriptOutput.clear();
    }
}
