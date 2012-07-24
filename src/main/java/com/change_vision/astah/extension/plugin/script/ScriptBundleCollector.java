package com.change_vision.astah.extension.plugin.script;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.script.ScriptEngineFactory;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 * This class tries to find ScriptEngines from OSGi bundles.
 */
public class ScriptBundleCollector implements BundleTrackerCustomizer {

    public static final String META_INF_SERVICES_DIR = "META-INF/services";
    public static final String SCRIPT_ENGINE_SERVICE_FILE = "javax.script.ScriptEngineFactory";

    private BundleContext context;
    private BundleTracker tracker;

    public ScriptBundleCollector(BundleContext context) {
        this.context = context;
    }

    public void startCollecting() {
        tracker = new BundleTracker(context, Bundle.ACTIVE, this);
        tracker.open();
    }

    public void stopCollecting() {
        tracker.close();
        context = null;
    }

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        registerScriptEngines(bundle);
        return bundle;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
    }

    private void registerScriptEngines(Bundle bundle) {
        Enumeration<?> e = bundle.findEntries(META_INF_SERVICES_DIR, SCRIPT_ENGINE_SERVICE_FILE,
                false);
        if (e == null) {
            return;
        }
        while (e.hasMoreElements()) {
            URL configURL = (URL) e.nextElement();
            if (configURL != null) {
                register(bundle, configURL);
            }
        }
    }

    public void register(Bundle bundle, URL configFile) {
        ScriptEngineFactory factory = getScriptEngineFactory(bundle, configFile);

        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put("engine.name", factory.getEngineName());
        properties.put("engine.version", factory.getEngineVersion());
        properties.put("language", factory.getLanguageName());
        properties.put("language.name", factory.getLanguageName());
        properties.put("language.version", factory.getLanguageVersion());

        bundle.getBundleContext().registerService("javax.script.ScriptEngineFactory", factory,
                properties);
    }

    public ScriptEngineFactory getScriptEngineFactory(Bundle bundle, URL configFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    configFile.openStream()));
            String className = reader.readLine();
            reader.close();

            Class<?> cls = bundle.loadClass(className);
            if (ScriptEngineFactory.class.isAssignableFrom(cls)) {
                ScriptEngineFactory factory = (ScriptEngineFactory) cls.newInstance();
                return factory;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
