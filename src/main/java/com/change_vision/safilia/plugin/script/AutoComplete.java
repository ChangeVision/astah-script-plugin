package com.change_vision.safilia.plugin.script;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.change_vision.safilia.api.inf.project.ProjectAccessor;

public class AutoComplete {

    public static void installTo(RSyntaxTextArea textArea) {
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(textArea);
    }

    private static CompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        // For Astah API
        addAstahApiCompletion(provider);

        // Mainly for javascript
        addJavascriptCompletion(provider);

        return provider;
    }

    private static void addJavascriptCompletion(DefaultCompletionProvider provider) {
        provider.addCompletion(new BasicCompletion(provider, "break"));
        provider.addCompletion(new BasicCompletion(provider, "case"));
        provider.addCompletion(new BasicCompletion(provider, "continue"));
        provider.addCompletion(new BasicCompletion(provider, "default"));
        provider.addCompletion(new BasicCompletion(provider, "do"));
        provider.addCompletion(new BasicCompletion(provider, "else"));
        provider.addCompletion(new BasicCompletion(provider, "finally"));
        provider.addCompletion(new BasicCompletion(provider, "for"));
        provider.addCompletion(new BasicCompletion(provider, "function"));
        provider.addCompletion(new BasicCompletion(provider, "goto"));
        provider.addCompletion(new BasicCompletion(provider, "if"));
        provider.addCompletion(new BasicCompletion(provider, "importPackage"));
        provider.addCompletion(new BasicCompletion(provider, "instanceof"));
        provider.addCompletion(new BasicCompletion(provider, "new"));
        provider.addCompletion(new BasicCompletion(provider, "print"));
        provider.addCompletion(new BasicCompletion(provider, "println"));
        provider.addCompletion(new BasicCompletion(provider, "return"));
        provider.addCompletion(new BasicCompletion(provider, "switch"));
        provider.addCompletion(new BasicCompletion(provider, "this"));
        provider.addCompletion(new BasicCompletion(provider, "try"));
        provider.addCompletion(new BasicCompletion(provider, "void"));
        provider.addCompletion(new BasicCompletion(provider, "while"));
    }

    private static void addAstahApiCompletion(DefaultCompletionProvider provider) {
        Set<String> completionStrSet = new HashSet<String>();
        Class<ProjectAccessor> accessorClass = ProjectAccessor.class;
        Method[] methods = accessorClass.getMethods();
        for (Method method : methods) {
            StringBuilder sb = new StringBuilder();
            sb.append("astah.").append(method.getName()).append("(");

            if (method.getParameterTypes().length == 0) {
                sb.append(")");
            }

            String completionStr = sb.toString();
            if (!completionStrSet.contains(completionStr)) {
                BasicCompletion completion = new BasicCompletion(provider, completionStr);
                provider.addCompletion(completion);
                completionStrSet.add(completionStr);
            }
        }

        provider.addCompletion(new BasicCompletion(provider,
                "TransactionManager.beginTransaction()"));
        provider.addCompletion(new BasicCompletion(provider, "TransactionManager.endTransaction()"));
        provider.addCompletion(new BasicCompletion(provider,
                "importPackage(com.change_vision.jude.api.inf.editor)"));
        provider.addCompletion(new BasicCompletion(provider,
                "importPackage(com.change_vision.jude.api.inf.model)"));
        provider.addCompletion(new BasicCompletion(provider,
                "importPackage(com.change_vision.jude.api.inf.presentation)"));
        provider.addCompletion(new BasicCompletion(provider, "com.change_vision.jude.api.inf."));
    }

    /*
     * Trying to use FunctionCompletion private static void
     * addAstahApiCompletions(DefaultCompletionProvider provider) { Class
     * accessorClass = ProjectAccessor.class; Method[] methods =
     * accessorClass.getMethods(); for (Method method : methods) {
     * FunctionCompletion completion = new FunctionCompletion(provider,
     * method.getName(), method.getReturnType().getSimpleName());
     * completion.setReturnValueDescription("");
     * completion.setShortDescription(""); List params = new
     * ArrayList(); for (Class<?>paramType : method.getParameterTypes()) {
     * Parameter parameter = new ParameterizedCompletion.Parameter(paramType,
     * paramType.getSimpleName()); params.add(parameter); }
     * completion.setParams(params); completion.setSummary(method.toString());
     * provider.addCompletion(completion); //provider.addCompletion(new
     * BasicCompletion(provider, "astah." + method.getName() + "(")); } }
     */
}
