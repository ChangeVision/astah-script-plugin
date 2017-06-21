package com.change_vision.astah.extension.plugin.script.command;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.change_vision.astah.extension.plugin.script.ConfigDialog;
import com.change_vision.astah.extension.plugin.script.ConfigManager;
import com.change_vision.astah.extension.plugin.script.ScriptView;
import com.change_vision.astah.extension.plugin.script.ScriptViewContext;

public class ConfigCommand {
    
    private ScriptViewContext context;
    
    public ConfigCommand(ScriptViewContext context) {
        this.context = context;
    }
    
    public void execute() {
        ConfigDialog configDialog = new ConfigDialog(context.dialog);
        configDialog.update();
        configDialog.setLocationRelativeTo(context.dialog);
        configDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        configDialog.setVisible(true);
        if (!configDialog.isSubmitted()) {
            return;
        }
        changeFontSize();
    }
    
    private void changeFontSize() {
        changeFontSize(context.scriptScrollPane);
        context.scriptOutput.changeFontSize();
    }

    private void changeFontSize(RTextScrollPane scrollPane) {
        Font font = new JTextField().getFont();
        RTextArea textArea = scrollPane.getTextArea();
        if (textArea != null) {
            font = textArea.getFont();
        }
        int newFontSize = ConfigManager.getInstance().getFontSize();
        if (font.getSize() == newFontSize) {
            return;
        }
        font = new Font(font.getName(), font.getStyle(), newFontSize);
        ScriptView.getInstance().updateFont(scrollPane, font);
    }
    
}
