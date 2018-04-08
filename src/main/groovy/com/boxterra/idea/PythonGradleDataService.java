package com.boxterra.idea;

import com.boxterra.idea.model.data.PythonModelData;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.Key;
import com.intellij.openapi.externalSystem.model.ProjectKeys;
import com.intellij.openapi.externalSystem.model.project.ModuleData;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider;
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService;
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.jetbrains.python.facet.PythonFacet;
import com.jetbrains.python.facet.PythonFacetConfiguration;
import com.jetbrains.python.facet.PythonFacetType;
import icons.PythonIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PythonGradleDataService extends AbstractProjectDataService<PythonModelData, Project> {

    @NotNull
    @Override
    public Key<PythonModelData> getTargetDataKey() {
        return PythonModelData.KEY;
    }



    @Override
    public void importData(@NotNull final Collection<DataNode<PythonModelData>> toImport,
                           @Nullable final ProjectData projectData,
                           @NotNull final Project project,
                           @NotNull final IdeModifiableModelsProvider modelsProvider) {
        if (toImport.isEmpty() || projectData == null) {
            return;
        }

        if (toImport.size() != 1) {
            throw new IllegalArgumentException(String.format("Expected to get a single project but got %d: %s", toImport.size(), toImport));
        }

        final DataNode<PythonModelData> pythonDataNode = toImport.iterator().next();
        Module[] modules = modelsProvider.getModules();

        for (Module module : modelsProvider.getModules()) {
            ModifiableFacetModel model = modelsProvider.getModifiableFacetModel(module);
            Collection<PythonFacet> existingPythonFacets = model.getFacetsByType(PythonFacet.ID);
            if (existingPythonFacets.isEmpty()) {
                PythonFacetConfiguration pythonFacetConfiguration = new PythonFacetConfiguration();
                PythonFacet pythonFacet = new PythonFacet(PythonFacetType.getInstance(), module, "Python", pythonFacetConfiguration, null);
                model.addFacet(pythonFacet);
            }
        }

    }
}

