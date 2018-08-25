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

import com.boxterra.pyfusion.idea.model.data.PythonSdkModelData
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.SdkModificator
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.python.sdk.PythonSdkType
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class PythonSdkGradleDataService extends AbstractProjectDataService<PythonSdkModelData, Project> {

    @NotNull
    @Override
    Key<PythonSdkModelData> getTargetDataKey() {
        return PythonSdkModelData.KEY
    }


    @Override
    void importData(@NotNull final Collection<DataNode<PythonSdkModelData>> toImport,
                    @Nullable final ProjectData projectData,
                    @NotNull final Project project,
                    @NotNull final IdeModifiableModelsProvider modelsProvider) {
        if (toImport.isEmpty() || projectData == null) {
            return
        }

        for (DataNode<PythonSdkModelData> pythonSdkDataNode : toImport) {

            final PythonSdkModelData pythonSdkModelData = pythonSdkDataNode.getData()

            for(sdk in PythonSdkType.allSdks) {
                if (sdk.homePath == pythonSdkModelData.pythonExec && sdk.name == pythonSdkModelData.name) {
                    // check for paths missing from existing SDK
                    def rootFiles = sdk.getRootProvider().getFiles(OrderRootType.CLASSES)
                    for (path in pythonSdkModelData.additionalPaths) {
                        if (!rootFiles.contains(LocalFileSystem.getInstance().findFileByIoFile(new File(path)))) {
                            return // there is at least one additionPath not included in the existing SDK
                        }
                    }
                    return
                }
            }

            // IntelliJ only allows updating SDKs from the event thread
            ApplicationManager.getApplication().invokeAndWait(new Runnable() {
                @Override
                void run() {
                    Sdk pythonSdk = SdkConfigurationUtil.createAndAddSDK(pythonSdkModelData.pythonExec, PythonSdkType.getInstance())
                    SdkModificator sdkModifier = pythonSdk.getSdkModificator()
                    if(pythonSdkModelData.name != null) {
                        sdkModifier.setName(pythonSdkModelData.name)
                    }
                    for(String additionalPath in pythonSdkModelData.getAdditionalPaths()) {
                        sdkModifier.addRoot(LocalFileSystem.getInstance().findFileByIoFile(new File(additionalPath)), OrderRootType.CLASSES)
                    }
                    sdkModifier.commitChanges()
                }
            })
        }
    }
}

