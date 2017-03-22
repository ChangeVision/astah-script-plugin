package safilia.plugin.script.command;

import javax.swing.JOptionPane;

import safilia.plugin.script.ScriptViewContext;
import safilia.plugin.script.util.Messages;

public class CloseCommand {
    public static void execute(ScriptViewContext context) {
        if (context.isModified) {
            int result = JOptionPane.showConfirmDialog(context.dialog,
                    Messages.getMessage("message.ask_save"));
            if (result == JOptionPane.YES_OPTION) {
                SaveCommand.execute(context);
            }
        }

        context.dialog.setVisible(false);
    }
}