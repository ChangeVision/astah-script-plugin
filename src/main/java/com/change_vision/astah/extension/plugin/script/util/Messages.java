package com.change_vision.astah.extension.plugin.script.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import com.change_vision.jude.api.inf.ui.IMessageProvider;

public class Messages implements IMessageProvider {

    public static final String DEFAULT_BUNDLE = "com.change_vision.astah.extension.plugin.script.messages";

    private static ResourceBundle INTERNAL_MESSAGES = ResourceBundle.getBundle(DEFAULT_BUNDLE,
            Locale.getDefault(), Messages.class.getClassLoader());

    Messages() {
    }

    static void setupForTest(String bundlePath, Class<?> clazz) {
        INTERNAL_MESSAGES = ResourceBundle.getBundle(bundlePath, Locale.getDefault(),
                clazz.getClassLoader());
    }

    public static String getMessage(String key, Object... parameters) {
        String entry = INTERNAL_MESSAGES.getString(key);
        return MessageFormat.format(entry, parameters);
    }

    static Enumeration<String> getKeys() {
        return INTERNAL_MESSAGES.getKeys();
    }

    static Locale getLocale() {
        return INTERNAL_MESSAGES.getLocale();
    }

    @Override
    public String provideMessage(String key, Object... parameters) {
        return getMessage(key, parameters);
    }
}
