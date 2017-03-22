package com.change_vision.safilia.plugin.script;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ScriptEngineHelper {

    public static List<ScriptEngineFactory> getScriptEngineFactories(
            ScriptEngineManager scriptEngineManager) {
        List<ScriptEngineFactory> factories = new ArrayList<ScriptEngineFactory>();
        factories.addAll(scriptEngineManager.getEngineFactories());

        // add script engine factories in other bundles
        BundleContext bundleContext = ScriptViewContext.bundleContext;
        if (bundleContext == null) {
            return factories;
        }
        ServiceReference[] services = null;
        try {
            services = bundleContext.getServiceReferences("javax.script.ScriptEngineFactory", null);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        if (services == null) {
            return factories;
        }
        for (ServiceReference service : services) {
            ScriptEngineFactory factory = (ScriptEngineFactory) bundleContext.getService(service);
            if (factory != null) {
                factories.add(factory);
                registerFactoryToManager(factory, scriptEngineManager);
                bundleContext.ungetService(service);
            }
        }

        return factories;
    }

    private static void registerFactoryToManager(ScriptEngineFactory factory,
            ScriptEngineManager scriptEngineManager) {
        for (String name : factory.getNames()) {
            scriptEngineManager.registerEngineName(name, factory);
        }
        for (String extension : factory.getExtensions()) {
            scriptEngineManager.registerEngineExtension(extension, factory);
        }
        for (String mime : factory.getMimeTypes()) {
            scriptEngineManager.registerEngineMimeType(mime, factory);
        }
    }

}
