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

package com.boxterra.pyfusion.idea.model

import com.boxterra.pyfusion.idea.python.PythonModelImpl
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.gradle.tooling.ErrorMessageBuilder
import org.jetbrains.plugins.gradle.tooling.ModelBuilderService

class PythonModelBuilderImpl implements ModelBuilderService {

    @Override
    boolean canBuild(String modelName) {
        return com.boxterra.pyfusion.idea.python.PythonModel.class.getName() == modelName
    }

    @Override
    Object buildAll(String s, Project project) {
        AppliedPlugin pythonPlugin = project.getPluginManager().findPlugin("com.boxterra.pyfusion")
        if(pythonPlugin != null){
            def pythonConfig = project.getExtensions().findByName("python")
            if (pythonConfig != null) {
                PythonModelImpl pythonModelImpl = new PythonModelImpl()
                pythonModelImpl.setSdk(pythonConfig.getSdk())
                return pythonModelImpl
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
                project, e, "Python project import errors"
        ).withDescription("Python may not be configured properly. Stack: " + exceptionAsString)
    }
}
