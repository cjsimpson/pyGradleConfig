/*
 * Copyright (c) 2018 Chris Simpson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.boxterra.pyfusion.idea

import com.boxterra.pyfusion.idea.model.data.PythonModelData
import com.intellij.facet.Facet
import com.intellij.facet.ModifiableFacetModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.project.ModuleData
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.jetbrains.python.facet.PythonFacet
import com.jetbrains.python.facet.PythonFacetConfiguration
import com.jetbrains.python.facet.PythonFacetType
import com.jetbrains.python.sdk.PythonSdkType
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.gradle.model.data.GradleSourceSetData

class PythonGradleDataService extends AbstractProjectDataService<PythonModelData, Project> {

    @NotNull
    @Override
    Key<PythonModelData> getTargetDataKey() {
        return PythonModelData.KEY
    }


    @Override
    void importData(@NotNull final Collection<DataNode<PythonModelData>> toImport,
                    @Nullable final ProjectData projectData,
                    @NotNull final Project project,
                    @NotNull final IdeModifiableModelsProvider modelsProvider) {
        if (toImport.isEmpty() || projectData == null) {
            return
        }

        for (DataNode<PythonModelData> pythonDataNode : toImport) {

            final PythonModelData pythonModelData = pythonDataNode.getData()
            DataNode<ModuleData> moduleDataNode = pythonDataNode.getParent(ModuleData.class)
            ArrayList<DataNode<ModuleData>> nodesToUpdate = new ArrayList<>()
            if(moduleDataNode == null) {
                return
            }
            for (DataNode child : moduleDataNode.getChildren()) {
                if (child.getKey() == GradleSourceSetData.KEY) {
                    nodesToUpdate.add(child)
                }
            }
            nodesToUpdate.add(moduleDataNode)

            String sdk = pythonModelData.getSkd()
            Library sdkLibrary
            Sdk pySdk
            if (sdk != null) {
                pySdk = PythonSdkType.findSdkByKey(sdk)
                sdkLibrary = LibraryTablesRegistrar.getInstance().getLibraryTable().getLibraryByName(sdk + " interpreter library")
                if (sdkLibrary == null || pySdk == null) {
                    Notification notification = new Notification("Python Gradle Plugin",
                            "Python SDK Not Found", "Error configuring module <" + moduleDataNode.getData().getExternalName() + "> could not find Python SDK <" + sdk +">. Double check IntelliJ SDK settings and build.gradle settings.",
                            NotificationType.WARNING)
                    Notifications.Bus.notify(notification)
                }
            }

            for (DataNode<ModuleData> node : nodesToUpdate) {
                Module ideModule = modelsProvider.findIdeModule(node.getData().getInternalName())

                // Remove any existing Python SDK library dependencies
                List<Library> libraries = Arrays.asList(modelsProvider.getModifiableRootModel(ideModule).getModuleLibraryTable().getLibraries())
                for (Sdk s : PythonSdkType.getAllSdks()) {
                    Library lib = LibraryTablesRegistrar.getInstance().getLibraryTable().getLibraryByName(s.getName() + " interpreter library")
                    if (lib != null && libraries.contains(lib)) {
                        modelsProvider.getModifiableRootModel(ideModule).getModuleLibraryTable().removeLibrary(lib)
                    }
                }

                // Add the desired Python SDK library dependency
                if (sdkLibrary != null) {
                    modelsProvider.getModifiableRootModel(ideModule).addLibraryEntry(sdkLibrary)
                }

                // Add and configure the Python Facet to the module
                ModifiableFacetModel facetModel = modelsProvider.getModifiableFacetModel(ideModule)
                Collection<PythonFacet> existingPythonFacets = facetModel.getFacetsByType(PythonFacet.ID)
                if (!existingPythonFacets.isEmpty()) {
                    for (Facet f : existingPythonFacets) {
                        facetModel.removeFacet(f)
                    }
                }
                PythonFacetConfiguration pythonFacetConfiguration = new PythonFacetConfiguration()
                if (pySdk != null) {
                    pythonFacetConfiguration.setSdk(pySdk)
                }
                PythonFacet pythonFacet = new PythonFacet(PythonFacetType.getInstance(), ideModule, "Python", pythonFacetConfiguration, null)
                facetModel.addFacet(pythonFacet)
            }
        }

    }
}

