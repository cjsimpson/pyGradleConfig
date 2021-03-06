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

package com.boxterra.pyfusion.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class PythonGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().create("python", PythonConfig.class)
        project.getExtensions().create("pythonSdk", PythonSdkConfig.class)
    }

    static class PythonConfig {
        String sdk

        String getSdk() {
            return sdk
        }
    }

    static class PythonSdkConfig {
        String pythonExec
        String name
        List<String> additionalPaths = new ArrayList<>()

        String getPythonExec() {
            return pythonExec
        }
        String getName() {
            return name
        }
        List<String> getAdditionalPaths() {
            return additionalPaths
        }
        void setAdditionalPaths(List<String> additionalPaths) {
            this.additionalPaths = additionalPaths
        }
    }



}
