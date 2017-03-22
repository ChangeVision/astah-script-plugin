package safilia.plugin.script.command;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import safilia.plugin.script.ScriptViewContext;
import safilia.plugin.script.util.FileChooser;
import safilia.plugin.script.util.Messages;

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
            PrintWriter writer = new PrintWriter(context.currentFile, context.encoding);
            String text = context.scriptTextArea.getText();
            int textSize = text.length();
            writer.write(context.scriptTextArea.getText(), 0, textSize);
            writer.close();
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
