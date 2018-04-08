package com.boxterra.idea.model

import com.boxterra.idea.python.PythonModel;
import com.boxterra.idea.python.PythonModelImpl;
import org.gradle.api.Project;
import org.gradle.api.plugins.AppliedPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.tooling.ErrorMessageBuilder;
import org.jetbrains.plugins.gradle.tooling.ModelBuilderService

public class PythonModelBuilderImpl implements ModelBuilderService {

    @Override
    public boolean canBuild(String modelName) {
        return PythonModel.class.getName().equals(modelName);
    }

    @Override
    public Object buildAll(String s, Project project) {
        AppliedPlugin pythonPlugin = project.getPluginManager().findPlugin("com.boxterra.python");
        if(pythonPlugin != null){
            def pythonConfig = project.getExtensions().findByName("python");
            if (pythonConfig != null) {
                PythonModelImpl pythonModelImpl = new PythonModelImpl();
                for (String path : pythonConfig.getPythonPath()) {
                    File file = new File(path);
                    pythonModelImpl.addToPythonPython(file);
                }
                return pythonModelImpl;
            }
        }
        return null;
    }

    @Override
    public ErrorMessageBuilder getErrorMessageBuilder(@NotNull Project project, @NotNull Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();

        return ErrorMessageBuilder.create(
                project, e, "Python project import errors"
        ).withDescription("Python may not be configured properly. Stack: " + exceptionAsString);
    }
}
