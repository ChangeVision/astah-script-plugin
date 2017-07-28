package com.change_vision.astah.extension.plugin.script;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.change_vision.astah.extension.plugin.script.util.Messages;

@SuppressWarnings("serial")
public class ConfigDialog extends JDialog implements ActionListener {

    private JCheckBox consoleClearBox;
    private ButtonGroup fontGroup;
    private boolean result;
    
    public ConfigDialog(JDialog owner) {
        super(owner);
        setModal(true);
        setLayout(new BorderLayout());
        
        JPanel componentPanel = createComponentPanel();
        JPanel southBtnPanel = createButtonPanel();
        
        add("Center", componentPanel);
        add("South", southBtnPanel);
        
        setTitle(Messages.getMessage("action.config.label"));
        pack();
        setMinimumSize(getPreferredSize());
    }

    private JPanel createComponentPanel() {
        JPanel componentPanel = new JPanel();
        componentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        componentPanel.setLayout(new GridBagLayout());
        JPanel consolePanel = createConsolePanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        componentPanel.add(consolePanel, gbc);
        
        JPanel fontPanel = createFontPanel();
        gbc.gridy = 1;
        componentPanel.add(fontPanel, gbc);
        return componentPanel;
    }

    private JPanel createFontPanel() {
        JPanel fontPanel = new JPanel();
        fontPanel.setBorder(new TitledBorder(new EtchedBorder(), Messages.getMessage("config.font_size")));
        fontGroup = new ButtonGroup();

        JRadioButton smallFont = new JRadioButton(Messages.getMessage("config.small_font"));
        smallFont.setName(ConfigManager.FONT_SMALL);
        fontGroup.add(smallFont);
        JRadioButton mediumFont = new JRadioButton(Messages.getMessage("config.medium_font"));
        mediumFont.setName(ConfigManager.FONT_MEDIUM);
        fontGroup.add(mediumFont);
        JRadioButton largeFont = new JRadioButton(Messages.getMessage("config.large_font"));
        largeFont.setName(ConfigManager.FONT_LARGE);
        fontGroup.add(largeFont);
        
        fontPanel.add(smallFont);
        fontPanel.add(mediumFont);
        fontPanel.add(largeFont);
        return fontPanel;
    }

    private JPanel createConsolePanel() {
        JPanel consolePanel = new JPanel();
        consolePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        consoleClearBox = new JCheckBox(Messages.getMessage("config.clear_console"));
        consolePanel.add(consoleClearBox);
        return consolePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        JPanel innerPanel = new JPanel();
        innerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        innerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        
        JButton okBtn = new JButton(Messages.getMessage("config.ok.button"));
        okBtn.addActionListener(this);
        okBtn.setActionCommand("okBtn");
        
        JButton cancelBtn = new JButton(Messages.getMessage("config.cancel.button"));
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("cancelBtn");
        
        buttonPanel.add(innerPanel);
        innerPanel.add(okBtn);
        innerPanel.add(cancelBtn);
        
        return buttonPanel;
    }
    
    public void update() {
        consoleClearBox.setSelected(ConfigManager.getInstance().isAutoConsoleClear());
        selectFontSize();
        result = false;
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("okBtn")) {
            ConfigManager config = ConfigManager.getInstance();
            config.setAutoConsoleClear(consoleClearBox.isSelected());
            config.setStoredFontSize(getSelectedFontSize());
            config.save();
            result = true;
        } else if (cmd.equals("cancelBtn")) {
            result = false;
        }
        setVisible(false);
    }
    
    public boolean isSubmitted() {
        return result;
    }
    
    private String getSelectedFontSize() {
        for (AbstractButton b : Collections.list(fontGroup.getElements())) {
            if (b.isSelected()) {
                return b.getName();
            }
        }
        return "";
    }

    private void selectFontSize() {
        ConfigManager config = ConfigManager.getInstance();
        for (AbstractButton b : Collections.list(fontGroup.getElements())) {
            b.setSelected(b.getName().equals(config.getStoredFontSize()));
        }
        if (fontGroup.getSelection() == null) {
            for (AbstractButton b : Collections.list(fontGroup.getElements())) {
                b.setSelected(true);
                break;
            }
        }
        return;
    }

}