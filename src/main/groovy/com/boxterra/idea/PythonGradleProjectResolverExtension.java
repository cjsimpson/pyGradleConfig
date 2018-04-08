package com.boxterra.idea;

import com.boxterra.idea.model.PythonModelBuilderImpl;
import com.boxterra.idea.model.data.PythonModelData;
import com.boxterra.idea.python.PythonModel;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.project.ModuleData;
import com.intellij.openapi.externalSystem.util.ExternalSystemConstants;
import com.intellij.openapi.externalSystem.util.Order;
import org.gradle.tooling.model.idea.IdeaModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.service.project.AbstractProjectResolverExtension;
import org.jetbrains.plugins.gradle.util.GradleConstants;

import java.util.Collections;
import java.util.Set;


@Order(ExternalSystemConstants.UNORDERED)
public class PythonGradleProjectResolverExtension extends AbstractProjectResolverExtension {
    private static final Logger LOG = Logger.getInstance(PythonGradleProjectResolverExtension.class);


    @Override
    public void populateModuleExtraModels(@NotNull IdeaModule gradleModule, @NotNull DataNode<ModuleData> ideModule) {
        PythonModel pythonModel = resolverCtx.getExtraProject(gradleModule, PythonModel.class);
        if (pythonModel != null) {
            PythonModelData pythonModelData = create(pythonModel);
            ideModule.createChild(PythonModelData.KEY, pythonModelData);
        }

        nextResolver.populateModuleExtraModels(gradleModule, ideModule);
    }


    @NotNull
    @Override
    public Set<Class> getExtraProjectModelClasses() {
        return Collections.singleton(PythonModel.class);
    }

    @NotNull
    @Override
    public Set<Class> getToolingExtensionsClasses() {
        return Collections.singleton(PythonModelBuilderImpl.class);
    }


    @NotNull
    private static PythonModelData create(@NotNull PythonModel pythonModel) {
        PythonModelData pythonModelData = new PythonModelData(GradleConstants.SYSTEM_ID);
        pythonModelData.setPythonPath(pythonModel.getPythonPath());
        return pythonModelData;
    }
}