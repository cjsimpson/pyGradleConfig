package com.boxterra.idea.python;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PythonModelImpl implements PythonModel {

    private ArrayList<File> pythonPath = new ArrayList<>();

    public PythonModelImpl() {
    }

    @Override
    public List<File> getPythonPath() {
        return pythonPath;
    }

    public void setPythonPath(ArrayList<File> pythonPath) {
        this.pythonPath = pythonPath;
    }

    public void addToPythonPython(File file) {
        getPythonPath().add(file);
    }
}
