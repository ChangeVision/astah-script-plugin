package com.change_vision.astah.extension.plugin.script.command;

import javax.swing.JOptionPane;

import com.change_vision.astah.extension.plugin.script.ScriptViewContext;
import com.change_vision.astah.extension.plugin.script.util.Messages;

public class CloseCommand {
    public static void execute(ScriptViewContext context) {
        if (context.isModified) {
            int result = JOptionPane.showConfirmDialog(context.dialog,
                    Messages.getMessage("message.ask_save"));
            if (result == JOptionPane.YES_OPTION) {
                if (SaveCommand.execute(context) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            if (result == JOptionPane.CANCEL_OPTION|| result == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        context.dialog.setVisible(false);
    }
}