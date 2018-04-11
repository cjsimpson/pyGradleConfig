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

package com.boxterra.pyfusion.idea.model.data

import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.model.project.AbstractExternalEntityData
import org.jetbrains.annotations.NotNull

class PythonSdkModelData extends AbstractExternalEntityData {
    private static final long serialVersionUID = 1L
    private static final int PROCESSING_AFTER_BUILTIN_SERVICES

    @NotNull
    public static final Key<PythonSdkModelData> KEY
    private String pythonExec
    private String name
    private List<String> additionalPaths = new ArrayList<>()

    String getPythonExec() {
        return pythonExec
    }
    void setPythonExec(String pythonExec) {
        this.pythonExec = pythonExec
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }
    List<String> getAdditionalPaths() {
        return additionalPaths
    }

    void setAdditionalPaths(List<String> additionalPaths) {
        this.additionalPaths = additionalPaths
    }


    PythonSdkModelData(@NotNull ProjectSystemId owner) {
        super(owner)
    }

    static {
        PROCESSING_AFTER_BUILTIN_SERVICES = ProjectKeys.TASK.getProcessingWeight() + 1
        KEY = Key.create(PythonSdkModelData.class, PROCESSING_AFTER_BUILTIN_SERVICES)
    }
}
