package com.change_vision.astah.extension.plugin.script;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

@SuppressWarnings("serial")
public class ScriptOutput extends JTextPane {

    private static final SimpleAttributeSet DEFAULT_SIMPLE_ATTRIBUTE_SET = new SimpleAttributeSet();
    private static final SimpleAttributeSet ERROR_SIMPLE_ATTRIBUTE_SET;
    static {
        ERROR_SIMPLE_ATTRIBUTE_SET = new SimpleAttributeSet();
        StyleConstants.setForeground(ERROR_SIMPLE_ATTRIBUTE_SET, Color.red);
    }

    private PrintStream out;
    private PrintStream err;
    private PrintStream originalOut;
    private PrintStream originalErr;

    public ScriptOutput() {
        out = new PrintStream(new OutputStreamExtension(), true);
        err = new PrintStream(new OutputStreamExtension(ERROR_SIMPLE_ATTRIBUTE_SET), true);
    }

    public void beginConsole() {
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    public void endConsole() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    public void clear() {
        setText("");
    }

    private class OutputStreamExtension extends ByteArrayOutputStream {
        private SimpleAttributeSet attr;

        private OutputStreamExtension() {
            attr = DEFAULT_SIMPLE_ATTRIBUTE_SET;
        }

        private OutputStreamExtension(SimpleAttributeSet attr) {
            this.attr = attr;
        }

        @Override
        public void flush() throws IOException {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Document doc = getDocument();
                    try {
                        String text = OutputStreamExtension.this.toString();
                        doc.insertString(doc.getLength(), text, attr);
                        OutputStreamExtension.this.reset();
                    } catch (BadLocationException e) {
                    }
                }
            });
        }
    }
}
