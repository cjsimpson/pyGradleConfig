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

package com.github.cjsimpson.idea.model.data

import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.model.project.AbstractExternalEntityData

import org.jetbrains.annotations.NotNull

class PythonModelData extends AbstractExternalEntityData {
    private static final long serialVersionUID = 1L
    private static final int PROCESSING_AFTER_BUILTIN_SERVICES

    @NotNull
    public static final Key<PythonModelData> KEY
    private String skd

    String getSkd() {
        return skd
    }

    void setSkd(String skd) {
        this.skd = skd
    }

    PythonModelData(@NotNull ProjectSystemId owner) {
        super(owner)
    }

    static {
        PROCESSING_AFTER_BUILTIN_SERVICES = ProjectKeys.TASK.getProcessingWeight() + 1
        KEY = Key.create(PythonModelData.class, PROCESSING_AFTER_BUILTIN_SERVICES)
    }
}
