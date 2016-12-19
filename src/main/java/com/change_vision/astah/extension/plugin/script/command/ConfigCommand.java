package com.change_vision.astah.extension.plugin.script.command;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.change_vision.astah.extension.plugin.script.util.Messages;

public class ConfigCommand extends JFrame {
    ConfigCommand() {
        getContentPane().setLayout(new FlowLayout());

        DialogWindow dlg = new DialogWindow(this);
        //setVisible(true);
     }
     public static void excute(String[] args) {
        new ConfigCommand();
     }
}

class DialogWindow extends JDialog implements ActionListener {
    DialogWindow(JFrame owner) {
       super(owner);
       getContentPane().setLayout(new FlowLayout());

       JButton btn = new JButton("ボタン表示");
       btn.addActionListener(this);
       getContentPane().add(btn);

       setTitle(Messages.getMessage("action.config.label"));
       setSize(400, 300);
       setLocationRelativeTo(null);
       setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
       setVisible(true);
    }
 }