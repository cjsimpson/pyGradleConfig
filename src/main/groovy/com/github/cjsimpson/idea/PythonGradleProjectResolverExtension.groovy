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

package com.github.cjsimpson.idea

import com.github.cjsimpson.idea.model.PythonModelBuilderImpl
import com.github.cjsimpson.idea.model.data.PythonModelData
import com.github.cjsimpson.idea.python.PythonModel
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.project.ModuleData
import com.intellij.openapi.externalSystem.util.ExternalSystemConstants
import com.intellij.openapi.externalSystem.util.Order
import org.gradle.tooling.model.idea.IdeaModule
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.gradle.service.project.AbstractProjectResolverExtension
import org.jetbrains.plugins.gradle.util.GradleConstants

@Order(ExternalSystemConstants.UNORDERED)
class PythonGradleProjectResolverExtension extends AbstractProjectResolverExtension {
    private static final Logger LOG = Logger.getInstance(PythonGradleProjectResolverExtension.class)


    @Override
    void populateModuleExtraModels(@NotNull IdeaModule gradleModule, @NotNull DataNode<ModuleData> ideModule) {
        PythonModel pythonModel = resolverCtx.getExtraProject(gradleModule, PythonModel.class)
        if (pythonModel != null) {
            PythonModelData pythonModelData = create(pythonModel)
            ideModule.createChild(PythonModelData.KEY, pythonModelData)
        }
        nextResolver.populateModuleExtraModels(gradleModule, ideModule)
    }


    @NotNull
    @Override
    Set<Class> getExtraProjectModelClasses() {
        return Collections.singleton(PythonModel.class)
    }

    @NotNull
    @Override
    Set<Class> getToolingExtensionsClasses() {
        return Collections.singleton(PythonModelBuilderImpl.class)
    }


    @NotNull
    private static PythonModelData create(@NotNull PythonModel pythonModel) {
        PythonModelData pythonModelData = new PythonModelData(GradleConstants.SYSTEM_ID)
        pythonModelData.setSkd(pythonModel.getSdk())
        return pythonModelData
    }
}