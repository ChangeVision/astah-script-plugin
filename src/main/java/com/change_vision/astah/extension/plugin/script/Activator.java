package com.change_vision.astah.extension.plugin.script;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    private ScriptBundleCollector scriptBundleCollector;

    public void start(BundleContext context) {
        scriptBundleCollector = new ScriptBundleCollector(context);
        scriptBundleCollector.startCollecting();
        ScriptViewContext.bundleContext = context;
    }

    public void stop(BundleContext context) {
        scriptBundleCollector.stopCollecting();
    }
}
