package com.change_vision.astah.extension.plugin.script.command;

import java.awt.Cursor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.change_vision.astah.extension.plugin.script.ScriptViewContext;
import com.change_vision.astah.extension.plugin.script.util.FileChooser;
import com.change_vision.astah.extension.plugin.script.util.Messages;

public class SaveCommand {
    public static void execute(ScriptViewContext context) {
        if (context.currentFile == null) {
            String filePath = FileChooser.chooseFileToSave(context.dialog);
            if (filePath == null) {
                return;
            } else {
                File file = new File(filePath);
                context.setCurrentFile(file);
            }
        }

        String fileName = context.currentFile.getAbsolutePath();
        context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            context.fileModificationChecker.stop();
            FileWriter fw = new FileWriter(context.currentFile);
            String text = context.scriptTextArea.getText();
            int textSize = text.length();
            fw.write(context.scriptTextArea.getText(), 0, textSize);
            fw.close();
            context.fileModificationChecker.start();
            context.setIsModified(false);
            context.statusBar.setText(Messages.getMessage("status.saved") + fileName);
        } catch (IOException exc) {
            context.statusBar.setText(Messages.getMessage("status.save_failed") + fileName);
        }

        context.historyManager.addFile(context.currentFile.getAbsolutePath());
        context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
