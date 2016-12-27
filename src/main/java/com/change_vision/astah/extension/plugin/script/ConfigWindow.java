package com.change_vision.astah.extension.plugin.script;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.change_vision.astah.extension.plugin.script.util.Messages;

public class ConfigWindow extends JDialog implements ActionListener {
    JCheckBox consoleClearBox = new JCheckBox(Messages.getMessage("config.clear_console"), ConfigManager.console_clear);
    public ConfigWindow() {
        setLayout(new BorderLayout());
        
        JPanel componentPanel = new JPanel();
        JPanel southBtnPanel = new JPanel();
        
        componentPanel.setLayout(new GridBagLayout());
        //southBtnPanel.setLayout(new BoxLayout(southBtnPanel, BoxLayout.X_AXIS));
        southBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        add("Center", componentPanel);
        add("South", southBtnPanel);
        
           
        JButton okBtn = new JButton(Messages.getMessage("config.ok.button"));
        okBtn.addActionListener(this);
        okBtn.setActionCommand("okBtn");
        
        JButton cancelBtn = new JButton(Messages.getMessage("config.cancel.button"));
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("cancelBtn");
        
        componentPanel.add(consoleClearBox);
        southBtnPanel.add(okBtn);
        southBtnPanel.add(cancelBtn);
        
        setTitle(Messages.getMessage("action.config.label"));
        setSize(400, 300);
        setLocationRelativeTo(null);
     }
     
     public void actionPerformed(ActionEvent e) {
         String cmd = e.getActionCommand();

         if (cmd.equals("okBtn")) {
             java.lang.System.out.println("OK!!!");
             if (consoleClearBox.isSelected()) {
                 ConfigManager.console_clear = true;
                 ConfigManager.save(ConfigManager.console_clear);
                 java.lang.System.out.println("check!");
             } else{
                 ConfigManager.console_clear = false;
                 ConfigManager.save(ConfigManager.console_clear);
                 java.lang.System.out.println("no check!");
             }
             setVisible(false);
         } else if (cmd.equals("cancelBtn")) {
             java.lang.System.out.println("Cancel...");
             setVisible(false);
         }
     }
}