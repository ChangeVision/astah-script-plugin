package com.change_vision.astah.extension.plugin.script.command;

import java.awt.Cursor;
import java.util.Date;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.text.BadLocationException;

import com.change_vision.astah.extension.plugin.script.ScriptViewContext;
import com.change_vision.astah.extension.plugin.script.util.Messages;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class RunCommand {
    private static final String SEPARATOR_STRING = "\n============ ";

    public static void execute(ScriptViewContext context) {
        context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        context.statusBar.setText(Messages.getMessage("status.running"));
        String scriptKind = (String) context.scriptKindCombobox.getSelectedItem();
        context.scriptOutput.beginConsole();
        try {
            evalScript(scriptKind, context.scriptTextArea.getText(), context);
        } finally {
            context.scriptOutput.endConsole();
            context.statusBar.setText(Messages.getMessage("status.finish"));
            context.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (TransactionManager.isInTransaction()) {
                TransactionManager.abortTransaction();
            }
        }
    }

    public static void evalScript(String scriptKind, String scriptString, ScriptViewContext context) {
        System.out.println(SEPARATOR_STRING + new Date().toString());
        ProjectAccessor projectAccessor = null;
        try {
            projectAccessor = ProjectAccessorFactory.getProjectAccessor();
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            // ignore to test
        }
        ScriptEngine scEngine = context.scriptEngineManager.getEngineByName(scriptKind);
        scEngine.put("projectAccessor", projectAccessor);
        scEngine.put("astah", projectAccessor);
        scEngine.put("scriptWindow", context.dialog);
        scEngine.put("astahWindow", context.dialog.getParent());
        try {
            scEngine.eval(scriptString);
        } catch (ScriptException ex) {
            try {
                //Select the error line.
                if (ex.getLineNumber() >= 0) {
                    int lineStartOffset = context.scriptTextArea.getLineStartOffset(ex.getLineNumber() - 1);
                    int lineEndOffset = context.scriptTextArea.getLineStartOffset(ex.getLineNumber()) - 1;
                    context.scriptTextArea.setSelectionStart(lineStartOffset);
                    if (ex.getColumnNumber() > 0) {
                        context.scriptTextArea.setSelectionEnd(lineStartOffset + ex.getColumnNumber());
                    } else {
                        context.scriptTextArea.setSelectionEnd(lineEndOffset);
                    }
                }
            } catch (BadLocationException e) {
                //e.printStackTrace();
            }
            System.err.println("ERROR: line = " + ex.getLineNumber() + ", column = " + ex.getColumnNumber());
            String errorMessage = ex.getLocalizedMessage();
             
            errorMessage = errorMessage.replaceFirst("javax.script.ScriptException: ", "");
            errorMessage = errorMessage.replaceFirst("sun.org.mozilla.javascript.internal.EcmaError: ", "");
            System.err.println(errorMessage);
        }
        scEngine.getBindings(ScriptContext.GLOBAL_SCOPE).clear();
        scEngine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
    }
}
