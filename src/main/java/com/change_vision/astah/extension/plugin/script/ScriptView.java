package com.change_vision.astah.extension.plugin.script;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.RecordableTextAction;

import com.change_vision.astah.extension.plugin.script.command.BrowseCommand;
import com.change_vision.astah.extension.plugin.script.command.ClearOutputCommand;
import com.change_vision.astah.extension.plugin.script.command.CloseCommand;
import com.change_vision.astah.extension.plugin.script.command.ConfigCommand;
import com.change_vision.astah.extension.plugin.script.command.NewCommand;
import com.change_vision.astah.extension.plugin.script.command.OpenCommand;
import com.change_vision.astah.extension.plugin.script.command.ReloadCommand;
import com.change_vision.astah.extension.plugin.script.command.RunCommand;
import com.change_vision.astah.extension.plugin.script.command.SaveAsCommand;
import com.change_vision.astah.extension.plugin.script.command.SaveCommand;
import com.change_vision.astah.extension.plugin.script.util.Messages;
import com.change_vision.astah.extension.plugin.script.util.ViewUtil;
import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.ui.IWindow;

public class ScriptView {

    private static ScriptView instance; // singleton
    private ScriptViewContext context = new ScriptViewContext();

    private ScriptView() {
    }

    public static ScriptView getInstance() {
        instance = new ScriptView();
        return instance;
    }

    public void show(IWindow window) throws ClassNotFoundException, ProjectNotFoundException {
        if (context.dialog == null) {
            createAndShowScriptDialog(window);
        } else {
            context.dialog.setVisible(true);
        }
    }

    public void createAndShowScriptDialog(IWindow window) throws ClassNotFoundException,
            ProjectNotFoundException {
        Window parentWindow = null;
        if (window != null) {
            parentWindow = window.getParent();
        }
        
        final JDialog dialog = new JDialog(parentWindow, Messages.getMessage("dialog.title"));

        JPanel cp = new JPanel(new BorderLayout());

        // Script Text & Output Text
        final JSplitPane mainPane = createMainPanel();
        cp.add(mainPane, BorderLayout.CENTER);

        JMenuBar menuBar = createMenuBar();
        dialog.setJMenuBar(menuBar);

        JToolBar toolBar = createToolBar();
        cp.add(toolBar, BorderLayout.NORTH);

        cp.add(createStatusBar(), BorderLayout.SOUTH);

        dialog.setContentPane(cp);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (context.isModified) {
                    int result = JOptionPane.showConfirmDialog(context.dialog,
                            Messages.getMessage("message.ask_save"));
                    if (result == JOptionPane.YES_OPTION) {
                        SaveCommand.execute(context);
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        //We don't close the dialog if user canceled
                        return;
                    }
                }
                context.dialog.setVisible(false);

                ConfigManager config = ConfigManager.getInstance();
                config.setWindowWidth(dialog.getWidth());
                config.setWindowHeight(dialog.getHeight());
                config.setWindowDividerLocation(mainPane.getDividerLocation());
                config.save();
            }
        });
        int width = ConfigManager.getInstance().getWindowWidth();
        int height = ConfigManager.getInstance().getWindowHeight();
        if (width > 0 && height > 0) {
            dialog.setPreferredSize(new Dimension(width, height));
        }
        dialog.pack();
        int dividerLocation = ConfigManager.getInstance().getWindowDividerLocation();
        if (dividerLocation > 0) {
            mainPane.setDividerLocation(ConfigManager.getInstance().getWindowDividerLocation());
        }
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setVisible(true);

        context.dialog = dialog;
        NewCommand.execute(context);
    }

    private JLabel createStatusBar() {
        JLabel statusBar = new JLabel();
        context.statusBar = statusBar;
        return statusBar;
    }

    private JSplitPane createMainPanel() {
        // Script Text
        RTextScrollPane scriptPane = createScriptEditorTextArea();
        context.scriptScrollPane = scriptPane;

        // Output Text
        context.scriptOutput = new ScriptOutput();
        JPanel outputPane = new JPanel();
        outputPane.setLayout(new BorderLayout());
        
        /*
        JToolBar outputToolBar = new JToolBar();
        outputToolBar.setFloatable(false);
        outputToolBar.setFocusable(false);

        JButton clearButton = new JButton(getIcon("images/clear_console.png"));
        //outputToolBar.add(Box.createGlue());
        outputToolBar.add(clearButton);
        clearButton.setToolTipText(Messages.getMessage("action.clear_console.tooltip"));
        clearButton.setRequestFocusEnabled(false);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClearOutputCommand.execute(context);
            }
        });

        outputPane.add("East", outputToolBar);
        */
        
        outputPane.add("Center", new JScrollPane(context.scriptOutput));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setTopComponent(scriptPane);
        splitPane.setBottomComponent(outputPane);

        return splitPane;
    }

    private RTextScrollPane createScriptEditorTextArea() {
        // Avoiding the problem that key input cannot be accepted when using
        // multiple RTextAreas
        // https://github.com/bobbylight/RSyntaxTextArea/issues/269
        JTextComponent.removeKeymap("RTextAreaKeymap");
        ScriptTextArea scriptTextArea = new ScriptTextArea();
        UIManager.put("RSyntaxTextAreaUI.actionMap", null);
        UIManager.put("RSyntaxTextAreaUI.inputMap", null);
        UIManager.put("RTextAreaUI.actionMap", null);
        UIManager.put("RTextAreaUI.inputMap", null);

        if (ViewUtil.isDark()) {
            try (InputStream in = getClass().getResourceAsStream("/dark.xml")) {
                Theme theme = Theme.load(in);
                theme.apply(scriptTextArea);
            } catch (IOException e) {
            }
        }

        scriptTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                // Ignore because this is called even if we change only
                // the style of document
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                context.setIsModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                context.setIsModified(true);
            }
        });
        context.scriptTextArea = scriptTextArea;
        RTextScrollPane areaWithScroll = new RTextScrollPane(scriptTextArea);
        adjustFontSizeForHighResDisplay(areaWithScroll);
        areaWithScroll.setFoldIndicatorEnabled(true);
        return areaWithScroll;
    }

    private void adjustFontSizeForHighResDisplay(RTextScrollPane rTextScrollPane) {
        Font font = new JTextField().getFont();
        font = new Font(font.getName(), font.getStyle(), ConfigManager.getInstance().getFontSize());
        updateFont(rTextScrollPane, font);
    }
    
    public void updateFont(RTextScrollPane pane, Font font) {
        RTextArea textArea = pane.getTextArea();
        if (textArea != null) {
            textArea.setFont(font);
        }
        Gutter gutter = pane.getGutter();
        if (gutter == null) {
            return;
        }
        for (int i = 0; i < gutter.getComponentCount(); i++) {
            gutter.getComponent(i).setFont(font);
        }
    }

    private JComboBox createScriptKindCombobox() {
        final JComboBox scriptKindCombobox = new JComboBox();

        final List<ScriptEngineFactory> engineFactories = ScriptEngineHelper
                .getScriptEngineFactories(context.scriptEngineManager);
        for (ScriptEngineFactory engineFactory : engineFactories) {
            scriptKindCombobox.addItem(engineFactory.getLanguageName());
        }
        scriptKindCombobox.setSelectedItem(ScriptTextArea.DEFAULT_LANGUAGE);

        scriptKindCombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object item = e.getItem();
                    context.scriptTextArea.updateSyntaxByLanguage((String) item);
                }
            }
        });

        return scriptKindCombobox;
    }

    private JMenuBar createMenuBar() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        final int shortcutKeyMask = tk.getMenuShortcutKeyMask();

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(Messages.getMessage("file_menu.label"));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem item;
        fileMenu.add(item = new JMenuItem(Messages.getMessage("action.new.label"),
                getAdjustedMenuIcon("images/new.png")));
        item.setMnemonic(KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewCommand.execute(context);
            }
        });

        fileMenu.add(item = new JMenuItem(Messages.getMessage("action.open.label"),
                getAdjustedMenuIcon("images/open.png")));
        item.setMnemonic(KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKeyMask));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenCommand.execute(context);
            }
        });

        fileMenu.add(item = new JMenuItem(Messages.getMessage("action.reload.label"),
                getAdjustedMenuIcon("images/reload.png")));
        item.setMnemonic(KeyEvent.VK_R);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReloadCommand.execute(context);
            }
        });

        fileMenu.add(item = new JMenuItem(Messages.getMessage("action.save.label"),
                getAdjustedMenuIcon("images/save.png")));
        item.setMnemonic(KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SaveCommand.execute(context);
            }
        });

        fileMenu.add(item = new JMenuItem(Messages.getMessage("action.save_as.label"),
                getAdjustedMenuIcon("images/save.png")));
        item.setMnemonic(KeyEvent.VK_A);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask
                | KeyEvent.SHIFT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SaveAsCommand.execute(context);
            }
        });

        fileMenu.addSeparator();

        fileMenu.add(item = new JMenuItem(Messages.getMessage("action.close.label")));
        item.setMnemonic(KeyEvent.VK_C);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, shortcutKeyMask));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CloseCommand.execute(context);
            }
        });

        menuBar.add(fileMenu);

        JMenu historyMenu = new JMenu(Messages.getMessage("history_menu.label"));
        fileMenu.add(historyMenu);
        context.historyManager = new HistoryManager(historyMenu, context);
        context.historyManager.updateRecentFileMenu();

        menuBar.add(historyMenu);

        JMenu editMenu = new JMenu(Messages.getMessage("edit_menu.label"));
        fileMenu.setMnemonic(KeyEvent.VK_E);

        RecordableTextAction undoAction = RTextArea.getAction(RTextArea.UNDO_ACTION);
        editMenu.add(item = new JMenuItem(undoAction));
        item.setIcon(getAdjustedMenuIcon("images/undo.png"));
        
        RecordableTextAction redoAction = RTextArea.getAction(RTextArea.REDO_ACTION);
        editMenu.add(item = new JMenuItem(redoAction));
        item.setIcon(getAdjustedMenuIcon("images/redo.png"));
        
        editMenu.addSeparator();

        RecordableTextAction cutAction = RTextArea.getAction(RTextArea.CUT_ACTION);
        editMenu.add(new JMenuItem(cutAction));

        RecordableTextAction copyAction = RTextArea.getAction(RTextArea.COPY_ACTION);
        editMenu.add(new JMenuItem(copyAction));

        RecordableTextAction pasteAction = RTextArea.getAction(RTextArea.PASTE_ACTION);
        editMenu.add(new JMenuItem(pasteAction));

        editMenu.addSeparator();

        RecordableTextAction selectAllAction = RTextArea.getAction(RTextArea.SELECT_ALL_ACTION);
        editMenu.add(new JMenuItem(selectAllAction));

        menuBar.add(editMenu);

        JMenu actionMenu = new JMenu(Messages.getMessage("action_menu.label"));
        actionMenu.setMnemonic(KeyEvent.VK_A);
        actionMenu.add(item = new JMenuItem(Messages.getMessage("action.run.label"),
                getAdjustedMenuIcon("images/run.png")));
        item.setMnemonic(KeyEvent.VK_R);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, shortcutKeyMask));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RunCommand.execute(context);
            }
        });
        
        actionMenu.addSeparator();
        
        actionMenu.add(item = new JMenuItem(Messages.getMessage("action.clear_console.label"),
                getAdjustedMenuIcon("images/clear.png")));
        item.setMnemonic(KeyEvent.VK_C);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, shortcutKeyMask));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClearOutputCommand.execute(context);
            }
        });
        
        menuBar.add(actionMenu);

        JMenu helpMenu = new JMenu(Messages.getMessage("help_menu.label"));
        helpMenu.setMnemonic(KeyEvent.VK_H);

        helpMenu.add(item = new JMenuItem(Messages.getMessage("action.open_sample_script.label")));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BrowseCommand.execute(Messages.getMessage("sample_script_uri", getEditionName()));
            }
        });

        helpMenu.add(item = new JMenuItem(Messages.getMessage("action.open_api_guide.label")));
        item.setMnemonic(KeyEvent.VK_A);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BrowseCommand.execute(Messages.getMessage("api_guide_uri", getEditionName()));
            }
        });

        helpMenu.add(item = new JMenuItem(Messages.getMessage("action.open_api_reference.label")));
        item.setMnemonic(KeyEvent.VK_J);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BrowseCommand.execute(Messages.getMessage("api_reference_uri", getEditionName()));
            }
        });

        helpMenu.addSeparator();
        
        helpMenu.add(item = new JMenuItem(Messages.getMessage("action.config.label"),
                getAdjustedMenuIcon("images/option.png")));
        item.setMnemonic(KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKeyMask
                | KeyEvent.SHIFT_MASK));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConfigCommand configCommand = new ConfigCommand(context);
                configCommand.execute();
            }
        });

        menuBar.add(helpMenu);
        return menuBar;
    }

    private ImageIcon getAdjustedMenuIcon(String path) {
        ImageIcon imageIcon = getIcon(path);
        if (imageIcon == null) {
            return imageIcon;
        }
        double scale = getUIScale();
        if (scale <= 1.0) {
            return imageIcon;
        }
        int buttonWidth = (int) Math.ceil(imageIcon.getIconWidth() * scale);
        int buttonHeight = (int) Math.ceil(imageIcon.getIconHeight() * scale);
        changeIconScale(imageIcon, buttonWidth, buttonHeight);
        return imageIcon;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        // toolBar.setRollover(true);
        toolBar.setFocusable(false);

        JButton newButton = new JButton(getIcon("images/new.png"));
        toolBar.add(newButton);
        newButton.setToolTipText(Messages.getMessage("action.new.tooltip"));
        newButton.setBorderPainted(false);
        newButton.setRequestFocusEnabled(false);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewCommand.execute(context);
            }
        });

        JButton openButton = new JButton(getIcon("images/open.png"));
        toolBar.add(openButton);
        openButton.setToolTipText(Messages.getMessage("action.open.tooltip"));
        openButton.setBorderPainted(false);
        openButton.setRequestFocusEnabled(false);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OpenCommand.execute(context);
            }
        });

        JButton saveButton = new JButton(getIcon("images/save.png"));
        toolBar.add(saveButton);
        saveButton.setToolTipText(Messages.getMessage("action.save.tooltip"));
        saveButton.setBorderPainted(false);
        saveButton.setRequestFocusEnabled(false);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SaveCommand.execute(context);
            }
        });

        JButton reloadButton = new JButton(getIcon("images/reload.png"));
        toolBar.add(reloadButton);
        reloadButton.setToolTipText(Messages.getMessage("action.reload.tooltip"));
        reloadButton.setBorderPainted(false);
        reloadButton.setRequestFocusEnabled(false);
        reloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReloadCommand.execute(context);
            }
        });

        toolBar.addSeparator();

        RecordableTextAction undoAction = RTextArea.getAction(RTextArea.UNDO_ACTION);
        JButton undoButton = new JButton(undoAction);
        undoButton.setIcon(getIcon("images/undo.png"));
        undoButton.setBorderPainted(false);
        undoButton.setHideActionText(true);
        undoButton.setRequestFocusEnabled(false);
        toolBar.add(undoButton);

        RecordableTextAction redoAction = RTextArea.getAction(RTextArea.REDO_ACTION);
        JButton redoButton = new JButton(redoAction);
        redoButton.setIcon(getIcon("images/redo.png"));
        redoButton.setBorderPainted(false);
        redoButton.setHideActionText(true);
        redoButton.setRequestFocusEnabled(false);
        toolBar.add(redoButton);

        toolBar.addSeparator();

        JButton runButton = new JButton(getIcon("images/run.png"));
        toolBar.add(runButton);
        runButton.setToolTipText(Messages.getMessage("action.run.tooltip"));
        runButton.setBorderPainted(false);
        runButton.setRequestFocusEnabled(false);
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RunCommand.execute(context);
            }
        });
        
        toolBar.addSeparator();
        
        JButton consoleClearButton = new JButton(getIcon("images/clear.png"));
        toolBar.add(consoleClearButton);
        consoleClearButton.setToolTipText(Messages.getMessage("action.clear_console.tooltip"));
        consoleClearButton.setBorderPainted(false);
        consoleClearButton.setRequestFocusEnabled(false);
        consoleClearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClearOutputCommand.execute(context);
            }
        });
        
        toolBar.addSeparator();
        
        JButton configButton = new JButton(getIcon("images/option.png"));
        toolBar.add(configButton);
        configButton.setToolTipText(Messages.getMessage("action.config.label"));
        configButton.setBorderPainted(false);
        configButton.setRequestFocusEnabled(false);
        configButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConfigCommand configCommand = new ConfigCommand(context);
                configCommand.execute();
            }
        });

        toolBar.add(Box.createGlue());

        // Script Kind
        context.scriptKindCombobox = createScriptKindCombobox();
        toolBar.add(context.scriptKindCombobox);
        adjustButtonSize(toolBar);

        return toolBar;
    }
    
    private void adjustButtonSize(JToolBar toolBar) {
        double scale = getUIScale();
        if (scale <= 1.0) {
            return;
        }
        for (int buttonIndex = 0; buttonIndex < toolBar.getComponentCount(); buttonIndex++) {
            Component component = toolBar.getComponent(buttonIndex);
            if (!(component instanceof JButton)) {
                continue;
            }
            JButton button = JButton.class.cast(component);
            Icon icon = button.getIcon();
            if (!(icon instanceof ImageIcon)) {
                continue;
            }
            ImageIcon imageIcon = ImageIcon.class.cast(icon);
            int buttonWidth = (int) Math.ceil(imageIcon.getIconWidth() * scale);
            int buttonHeight = (int) Math.ceil(imageIcon.getIconHeight() * scale);
            changeIconScale(imageIcon, buttonWidth, buttonHeight);
        }
    }

    public static boolean isMacOSX() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.startsWith("mac os x");
    }

    private double getUIScale() {
        if (isMacOSX()) {
            return 1.0;
        }
        final double labelNormalFontSize = 12.0;
        Font font = Font.class.cast(UIManager.get("Label.font"));
        double scale = font.getSize() / labelNormalFontSize;
        return scale;
    }

    private void changeIconScale(ImageIcon icon, int newWidth, int newHeight) {
        Image image = icon.getImage();
        icon.setImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
    }

    private ImageIcon getIcon(String path) {
        URL url = ViewUtil.getResource(getClass().getClassLoader(), path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            System.err.println("No icon resource: " + path);
            return null;
        }
    }

    private String getEditionName() {
        try {
            String astahEdition = AstahAPI.getAstahAPI().getProjectAccessor().getAstahEdition();
            if (astahEdition != null) {
                return astahEdition.toLowerCase();
            }
        } catch (ClassNotFoundException e) {
        }
        return "professional";
    }

}