package com.change_vision.astah.extension.plugin.script.command;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowseCommand {
    public static void execute(String uriString) {
        URI uri = null;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(uri);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
