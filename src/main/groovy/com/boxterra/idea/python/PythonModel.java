package com.boxterra.idea.python;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public interface PythonModel extends Serializable {

  List<File> getPythonPath();

}