package com.change_vision.astah.extension.plugin.script;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.change_vision.astah.extension.plugin.script.util.Messages;

public class ConfigWindow extends JDialog implements ActionListener {
    public ConfigWindow() {
        setLayout(new BorderLayout());
        
        JPanel panel0 = new JPanel();
        JPanel panel1 = new JPanel();
        
        
        
        panel0.setLayout(new GridBagLayout());
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        
        add("South", panel0);
        add("Center", panel1);
        
        
        JCheckBox ckbox = new JCheckBox();
        ckbox = new JCheckBox(Messages.getMessage("config.clear_console"));
           
        JButton btn1 = new JButton(Messages.getMessage("config.ok.button"));
        btn1.addActionListener(this);
        
        JButton btn2 = new JButton(Messages.getMessage("config.cancel.button"));
        btn1.addActionListener(this);
        
        panel1.add(ckbox);
        panel0.add(btn1);
        panel0.add(btn2);
        
        setTitle(Messages.getMessage("action.config.label"));
        setSize(400, 300);
        setLocationRelativeTo(null);
     }
     
     public void actionPerformed(ActionEvent e) {
        setVisible(true);
     }
}