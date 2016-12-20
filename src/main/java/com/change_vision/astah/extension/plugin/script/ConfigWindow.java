package com.change_vision.astah.extension.plugin.script;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.change_vision.astah.extension.plugin.script.util.Messages;

public class ConfigWindow extends JDialog implements ActionListener {
    public ConfigWindow() {
        setLayout(new BorderLayout());
        
        JCheckBox ckbox = new JCheckBox();
        ckbox = new JCheckBox("Clear");
        add("North", ckbox);
        
        JButton btn = new JButton("ok");
        btn.addActionListener(this);
        add("South", btn);
        
        
        
        setTitle(Messages.getMessage("action.config.label"));
        setSize(400, 300);
        setLocationRelativeTo(null);
     }
     
     public void actionPerformed(ActionEvent e) {
        setVisible(true);
     }
}