//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.boxterra.idea.model.data;

import com.intellij.openapi.externalSystem.model.Key;
import com.intellij.openapi.externalSystem.model.ProjectKeys;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.model.project.AbstractExternalEntityData;
import java.io.File;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class PythonModelData extends AbstractExternalEntityData {
    private static final long serialVersionUID = 1L;
    private static final int PROCESSING_AFTER_BUILTIN_SERVICES;

    @NotNull
    public static final Key<PythonModelData> KEY;
    private List<File> pythonPath;

    public PythonModelData(@NotNull ProjectSystemId owner) {
        super(owner);
    }

    public List<File> getPythonPath() {
        return this.pythonPath;
    }

    public void setPythonPath(List<File> pythonPath) {
        this.pythonPath = pythonPath;
    }

    static {
        PROCESSING_AFTER_BUILTIN_SERVICES = ProjectKeys.TASK.getProcessingWeight() + 1;
        KEY = Key.create(PythonModelData.class, PROCESSING_AFTER_BUILTIN_SERVICES);
    }
}
