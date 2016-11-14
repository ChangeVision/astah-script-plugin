package com.change_vision.astah.extension.plugin.script.util;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.File;

import javax.swing.JFileChooser;

public class FileChooser {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    
    private static File directory = null;
    
    public static String chooseFileToLoad(Dialog parentDialog) {
        return chooseFile(parentDialog, Messages.getMessage("open_script_dialog.title"), FileDialog.LOAD);
    }

    public static String chooseFileToSave(Dialog parentDialog) {
        return chooseFile(parentDialog, Messages.getMessage("save_dialog.title"), FileDialog.SAVE);
    }

    private static String chooseFile(Dialog parentDialog, String title, int mode) {
        if (isMac()) {
            //For the usability
            return chooseFileByFileDialog(parentDialog, title, mode);
        } else {
            //To avoid the bug of Java(GTK + Java7 + FileDialog freezes)
            return chooseFileByJFileChooser(parentDialog, title, mode);
        }
    }
    
    private static String chooseFileByFileDialog(Dialog parentDialog, String title, int mode) {
        FileDialog fileDialog = new FileDialog(parentDialog, title, mode);
        if(directory != null) fileDialog.setDirectory(directory.getPath());
        fileDialog.setVisible(true);
        String selectedFile = fileDialog.getFile();
        if (selectedFile != null) {
            directory = new File(fileDialog.getDirectory());
            return fileDialog.getDirectory() + selectedFile;
        } else {
            return null;
        }
    }
    
    private static String chooseFileByJFileChooser(Dialog parentDialog, String title, int mode) {
        JFileChooser chooser = new JFileChooser();
        if(directory != null) chooser.setCurrentDirectory(directory);
        chooser.setDialogTitle(title);
        int returnValue = 0;
        if (mode == FileDialog.LOAD) {
            returnValue = chooser.showOpenDialog(parentDialog);
        } else if (mode == FileDialog.SAVE) {
            returnValue = chooser.showSaveDialog(parentDialog);
        }
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            directory = chooser.getSelectedFile().getParentFile();
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
    
    private static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }
}
