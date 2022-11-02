package com.change_vision.astah.extension.plugin.script.util;

import java.net.URL;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.view.ILookAndFeelManager;

public class ViewUtil {

    public static boolean isDark() {
        try {
            return getLookAndFeelManager().isDarkLookAndFeel();
        } catch (Throwable e) {
            return false;
        }
    }

    public static URL getResource(ClassLoader classLoader, String path) {
        try {
            return getLookAndFeelManager().getResourceWithThemeAware(classLoader, path);
        } catch (Throwable e) {
        }
        return classLoader != null ? classLoader.getResource(path) : null;
    }

    private static ILookAndFeelManager getLookAndFeelManager()
            throws ClassNotFoundException, InvalidUsingException {
        return AstahAPI.getAstahAPI().getViewManager().getLookAndFeelManager();
    }
}
