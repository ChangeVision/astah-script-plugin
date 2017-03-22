package com.change_vision.safilia.plugin.script.command;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import com.change_vision.safilia.plugin.script.ScriptViewContext;
import com.change_vision.safilia.plugin.script.util.Messages;

public class ReloadCommand {

    public static void execute(ScriptViewContext context) {
        if (context.currentFile == null) {
            return; // impossible
        } else if (context.isModified) {
            int result = JOptionPane.showConfirmDialog(context.dialog,
                    Messages.getMessage("message.ask_ok_to_reload"));
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                    context.currentFile), context.encoding));
            context.scriptTextArea.read(reader, null);
            reader.close();
            context.scriptTextArea.discardAllEdits();

            context.setIsModified(false);
            context.statusBar.setText(Messages.getMessage("status.reloaded"));
        } catch (FileNotFoundException exc) {
            context.statusBar.setText(Messages.getMessage("status.file_not_found")
                    + context.currentFile.getAbsolutePath());
        } catch (IOException exc) {
            context.statusBar.setText(Messages.getMessage("status.io_exception")
                    + context.currentFile.getAbsolutePath());
        }
        context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

}
