package com.change_vision.astah.extension.plugin.script.command;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.swing.JOptionPane;

import com.change_vision.astah.extension.plugin.script.ScriptViewContext;
import com.change_vision.astah.extension.plugin.script.util.FileChooser;
import com.change_vision.astah.extension.plugin.script.util.Messages;

public class OpenCommand {
    public static void execute(ScriptViewContext context) {
        execute(context, null);
    }
    
    public static void execute(ScriptViewContext context, String filePath) {
        if (context.isModified) {
            int result = JOptionPane.showConfirmDialog(context.dialog,
                    Messages.getMessage("message.ask_save"));
            if (result == JOptionPane.YES_OPTION) {
                if (SaveCommand.execute(context) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        if (filePath == null) {
            filePath = FileChooser.chooseFileToLoad(context.dialog);
            if (filePath == null) {
                return;
            }

            context.setIsModified(false);
            NewCommand.execute(context); // Close current script file
        }
        File f = new File(filePath);
        context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), context.encoding));
            context.scriptTextArea.read(reader, null);
            reader.close();
            context.scriptTextArea.discardAllEdits();

            context.setCurrentFile(f);
            updateScriptKindByFileExtension(context);
            context.setIsModified(false);
            context.statusBar.setText(Messages.getMessage("status.loaded") + filePath);
            context.historyManager.addFile(context.currentFile.getAbsolutePath());
        } catch (FileNotFoundException exc) {
            context.statusBar.setText(Messages.getMessage("status.file_not_found") + filePath);
        } catch (IOException exc) {
            context.statusBar.setText(Messages.getMessage("status.io_exception") + filePath);
        } finally {
            context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private static void updateScriptKindByFileExtension(ScriptViewContext context) {
        if (context.currentFile == null) {
            return;
        }
        String fileName = context.currentFile.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot < 0) {
            return;
        }
        String extension = fileName.substring(lastIndexOfDot + 1);

        ScriptEngine engine = context.scriptEngineManager.getEngineByExtension(extension);
        if (engine != null) {
            String langName = engine.getFactory().getLanguageName();
            context.scriptKindCombobox.setSelectedItem(langName);
        }
    }

}
