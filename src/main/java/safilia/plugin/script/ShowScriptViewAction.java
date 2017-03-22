package safilia.plugin.script;

import javax.swing.JOptionPane;

import com.change_vision.safilia.api.inf.ui.IPluginActionDelegate;
import com.change_vision.safilia.api.inf.ui.IWindow;

import safilia.plugin.script.util.Messages;

public class ShowScriptViewAction implements IPluginActionDelegate {

    public Object run(IWindow window) throws UnExpectedException {
        try {
            ScriptView.getInstance().show(window);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window.getParent(),
                    Messages.getMessage("message.unknown_error"), "Alert",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            throw new UnExpectedException();
        }
        return null;
    }

}
