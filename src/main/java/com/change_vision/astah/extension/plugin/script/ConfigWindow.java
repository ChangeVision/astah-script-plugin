package com.change_vision.astah.extension.plugin.script;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.change_vision.astah.extension.plugin.script.util.Messages;

public class ConfigWindow extends JDialog implements ActionListener {

    private JCheckBox consoleClearBox;
    
    public ConfigWindow() {
        setModal(true);
        setLayout(new BorderLayout());
        
        JPanel componentPanel = new JPanel();
        JPanel southBtnPanel = new JPanel();
        
        componentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        componentPanel.setLayout(new GridBagLayout());
        southBtnPanel.setLayout(new BoxLayout(southBtnPanel, BoxLayout.Y_AXIS));
        
        add("Center", componentPanel);
        add("South", southBtnPanel);

        consoleClearBox = new JCheckBox(Messages.getMessage("config.clear_console"),
                                ConfigManager.getInstance().isConsoleClear());

        JPanel southInnerPanel = new JPanel();
        southInnerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        southInnerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        southInnerPanel.setLayout(new BoxLayout(southInnerPanel, BoxLayout.X_AXIS));
        
        JButton okBtn = new JButton(Messages.getMessage("config.ok.button"));
        okBtn.addActionListener(this);
        okBtn.setActionCommand("okBtn");
        
        JButton cancelBtn = new JButton(Messages.getMessage("config.cancel.button"));
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("cancelBtn");
        
        componentPanel.add(consoleClearBox);
        southBtnPanel.add(southInnerPanel);
        southInnerPanel.add(okBtn);
        southInnerPanel.add(cancelBtn);

        setTitle(Messages.getMessage("action.config.label"));
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
     }
     
     public void actionPerformed(ActionEvent e) {
         String cmd = e.getActionCommand();

         if (cmd.equals("okBtn")) {
             ConfigManager config = ConfigManager.getInstance();
             config.setConsoleClear(consoleClearBox.isSelected());
             config.save();
         } else if (cmd.equals("cancelBtn")) {
         }
         setVisible(false);
     }
}