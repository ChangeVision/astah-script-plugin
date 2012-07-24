package com.change_vision.astah.extension.plugin.script;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import javax.swing.text.Utilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.IconGroup;
import org.fife.ui.rtextarea.RTextAreaEditorKit;

@SuppressWarnings("serial")
public class ScriptTextArea extends RSyntaxTextArea {
    public static final String DEFAULT_LANGUAGE = "ECMAScript";
    private static final int TAB_SIZE = 4;
    
    public ScriptTextArea() {
        initialize();
    }
    
    @Override
    public void setCaret(Caret caret) {
        try {
            super.setCaret(caret);
        } catch (Exception e) {
            // Ignore(to avoid the bug of RSyntaxTextArea)
        }
    }
    
    private void initialize() {
        setCodeFoldingEnabled(true);
        setAntiAliasingEnabled(true);
        setPaintTabLines(true);
        setAutoIndentEnabled(true);
        setMarkOccurrences(true);
        // setWhitespaceVisible(true);
        setTabSize(TAB_SIZE);
        setTabsEmulated(true);
        updateSyntaxByLanguage(DEFAULT_LANGUAGE);
        
        IconGroup iconGroup = new IconGroup("script", "images/", "images/", "png");
        RSyntaxTextArea.setIconGroup(iconGroup);
        AutoComplete.installTo(this);
        if (isMacOSX()) {
            addDefaultMacKeybinds();
        }
    }

    public void updateSyntaxByLanguage(String lang) {
        String syntax = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;

        if (lang.equalsIgnoreCase("AppleScript")) {
            syntax = SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL;
        } else if (lang.equalsIgnoreCase("Groovy")) {
            syntax = SyntaxConstants.SYNTAX_STYLE_GROOVY;
        } else if (lang.equalsIgnoreCase("Scala")) {
            syntax = SyntaxConstants.SYNTAX_STYLE_SCALA;
        } else if (lang.equalsIgnoreCase("Ruby")) {
            syntax = SyntaxConstants.SYNTAX_STYLE_RUBY;
        } else if (lang.equalsIgnoreCase("Python")) {
            syntax = SyntaxConstants.SYNTAX_STYLE_PYTHON;
        }

        setSyntaxEditingStyle(syntax);
    }

    /**
     * Add Emacs like keybinds for MacOs
     */
    private void addDefaultMacKeybinds() {
        InputMap map = getInputMap();
        
        //move
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK),
                DefaultEditorKit.forwardAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK),
                DefaultEditorKit.backwardAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK),
                DefaultEditorKit.upAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK),
                DefaultEditorKit.downAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK),
                DefaultEditorKit.beginLineAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK),
                DefaultEditorKit.endLineAction);
        
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK),
                RTextAreaEditorKit.pasteAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK),
                RTextAreaEditorKit.deleteNextCharAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK),
                RTextAreaEditorKit.deletePrevCharAction);
        
        //shift + move ---> select
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK),
                DefaultEditorKit.selectionForwardAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK),
                DefaultEditorKit.selectionBackwardAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK),
                DefaultEditorKit.selectionUpAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK),
                DefaultEditorKit.selectionDownAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK),
                DefaultEditorKit.selectionBeginLineAction);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK),
                DefaultEditorKit.selectionEndLineAction);
        
        //Control + K --> cutEndLine
        Action cutToEndLineAction = new CutToEndLineAction("Cut To End Line");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK),
                cutToEndLineAction);
    }

    public static boolean isMacOSX() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.startsWith("mac os x");
    }
    
    /** Emacs like action for Cmd + K  */
    class CutToEndLineAction extends TextAction {
        Action selectionForwardAction;
        Action selectionEndLineAction;
        Action cutAction;
        
        CutToEndLineAction(String nm) {
            super(nm);
            selectionForwardAction = getActionMap().get(DefaultEditorKit.selectionForwardAction);
            selectionEndLineAction = getActionMap().get(RTextAreaEditorKit.selectionEndLineAction);
            cutAction = getActionMap().get(DefaultEditorKit.cutAction);
        }

        public void actionPerformed(ActionEvent e) {
            JTextComponent target = getTextComponent(e);
            if (target != null) {
                try {
                    int currentCaretPosition = target.getCaretPosition();
                    int end = target.getDocument().getLength();
                    
                    if (currentCaretPosition == end) {
                        return;
                    }
                    int lineEndOffs = Utilities.getRowEnd(target, currentCaretPosition);
                    if (currentCaretPosition == lineEndOffs) {
                        selectionForwardAction.actionPerformed(e);
                    } else {
                        selectionEndLineAction.actionPerformed(e);
                    }
                    cutAction.actionPerformed(e);
                } catch (BadLocationException bl) {
                    UIManager.getLookAndFeel().provideErrorFeedback(target);
                }
            }
        }
    }
}
