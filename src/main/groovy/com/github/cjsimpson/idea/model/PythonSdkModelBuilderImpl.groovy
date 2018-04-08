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

package com.github.cjsimpson.idea.model

import com.github.cjsimpson.idea.python.PythonSdkModel
import com.github.cjsimpson.idea.python.PythonSdkModelImpl
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.gradle.tooling.ErrorMessageBuilder
import org.jetbrains.plugins.gradle.tooling.ModelBuilderService

class PythonSdkModelBuilderImpl implements ModelBuilderService {

    @Override
    boolean canBuild(String modelName) {
        return PythonSdkModel.class.getName() == modelName
    }

    @Override
    Object buildAll(String s, Project project) {
        AppliedPlugin pythonPlugin = project.getPluginManager().findPlugin("com.github.cjsimpson.python")
        if (pythonPlugin != null) {
            def pythonSdkConfig = project.getExtensions().findByName("pythonSdk")
            if (pythonSdkConfig != null) {
                PythonSdkModelImpl pythonSdkModelImpl = new PythonSdkModelImpl()
                pythonSdkModelImpl.setPythonExec(pythonSdkConfig.getPythonExec())
                pythonSdkModelImpl.setName(pythonSdkConfig.getName())
                pythonSdkModelImpl.setAdditionalPaths(pythonSdkConfig.getAdditionalPaths())
                return pythonSdkModelImpl
            }
        }
        return null
    }

    @Override
    ErrorMessageBuilder getErrorMessageBuilder(@NotNull Project project, @NotNull Exception e) {
        StringWriter sw = new StringWriter()
        e.printStackTrace(new PrintWriter(sw))
        String exceptionAsString = sw.toString()

        return ErrorMessageBuilder.create(
                project, e, "Error configuring Python SDK"
        ).withDescription("Python SDK may not be configured properly. Stack: " + exceptionAsString)
    }
}
