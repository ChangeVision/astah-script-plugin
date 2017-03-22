package com.change_vision.safilia.plugin.script;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import com.change_vision.safilia.plugin.script.command.OpenCommand;

public class HistoryManager {
    private static final int MAX_SIZE = 10;
    private static final String PREFERENCE_KEY_PREFIX = "recent_file_";
    private List<String> recentFiles = new ArrayList<String>();
    private JMenuItem parentMenu;
    private Preferences prefs;
    private ScriptViewContext context;

    public HistoryManager(JMenuItem parentMenu, ScriptViewContext context) {
        this.parentMenu = parentMenu;
        this.context = context;
        prefs = Preferences.userNodeForPackage(this.getClass());
        load();
    }

    private void load() {
        for (int i = 0; i < MAX_SIZE; i++) {
            String path = prefs.get(PREFERENCE_KEY_PREFIX + i, null);
            if (path != null) {
                File file = new File(path);
                if (file.canRead()) {
                    recentFiles.add(path);
                }
            } else {
                return;
            }
        }
    }

    private void store() {
        for (int i = 0; i < recentFiles.size(); i++) {
            String filePath = recentFiles.get(i);
            prefs.put(PREFERENCE_KEY_PREFIX + i, filePath);
        }
        for (int i = recentFiles.size(); i < MAX_SIZE; i++) {
            prefs.remove(PREFERENCE_KEY_PREFIX + i);
        }

        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public void updateRecentFileMenu() {
        parentMenu.removeAll();
        for (int i = 0; i < recentFiles.size(); i++) {
            String filePath = recentFiles.get(i);
            JMenuItem menu = createRecentFileMenuItem(i, filePath);
            parentMenu.add(menu);
        }
    }

    public void addFile(String filePath) {
        if (recentFiles.contains(filePath)) {
            // change order
            recentFiles.remove(filePath);
            recentFiles.add(0, filePath);
        } else {
            // insert
            recentFiles.add(0, filePath);
            if (recentFiles.size() > MAX_SIZE) {
                recentFiles.remove(recentFiles.size() - 1);
            }
        }

        updateRecentFileMenu();

        store();
    }

    private JMenuItem createRecentFileMenuItem(int i, String filePath) {
        String label = String.valueOf(i + 1) + "." + filePath;
        JMenuItem menu = new JMenuItem(label);
        menu.setMnemonic(i + 1);
        menu.addActionListener(new OpenFileAction(filePath));
        return menu;
    }

    @SuppressWarnings("serial")
    class OpenFileAction extends AbstractAction {
        private String filePath;

        public OpenFileAction(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            OpenCommand.execute(context, filePath);
        }
    }
}
