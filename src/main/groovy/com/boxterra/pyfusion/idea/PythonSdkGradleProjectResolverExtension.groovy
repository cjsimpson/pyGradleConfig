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

import com.boxterra.pyfusion.idea.model.PythonSdkModelBuilderImpl
import com.boxterra.pyfusion.idea.model.data.PythonSdkModelData
import com.boxterra.pyfusion.idea.python.PythonSdkModel
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.project.ModuleData
import com.intellij.openapi.externalSystem.util.Order
import org.gradle.tooling.model.idea.IdeaModule
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.gradle.service.project.AbstractProjectResolverExtension
import org.jetbrains.plugins.gradle.util.GradleConstants

@Order(999)
class PythonSdkGradleProjectResolverExtension extends AbstractProjectResolverExtension {
    private static final Logger LOG = Logger.getInstance(PythonSdkGradleProjectResolverExtension.class)


    @Override
    void populateModuleExtraModels(@NotNull IdeaModule gradleModule, @NotNull DataNode<ModuleData> ideModule) {
        PythonSdkModel pythonSdkModel = resolverCtx.getExtraProject(gradleModule, PythonSdkModel.class)
        if (pythonSdkModel != null) {
            PythonSdkModelData pythonSdkModelData = create(pythonSdkModel)
            ideModule.createChild(PythonSdkModelData.KEY, pythonSdkModelData)
        }
        nextResolver.populateModuleExtraModels(gradleModule, ideModule)
    }


    @NotNull
    @Override
    Set<Class> getExtraProjectModelClasses() {
        return Collections.singleton(PythonSdkModel.class)
    }

    @NotNull
    @Override
    Set<Class> getToolingExtensionsClasses() {
        return Collections.singleton(PythonSdkModelBuilderImpl.class)
    }


    @NotNull
    private static PythonSdkModelData create(@NotNull PythonSdkModel pythonSdkModel) {
        PythonSdkModelData pythonSdkModelData = new PythonSdkModelData(GradleConstants.SYSTEM_ID)
        pythonSdkModelData.pythonExec = pythonSdkModel.pythonExec
        pythonSdkModelData.name = pythonSdkModel.name
        pythonSdkModelData.additionalPaths = pythonSdkModel.additionalPaths
        return pythonSdkModelData
    }
}