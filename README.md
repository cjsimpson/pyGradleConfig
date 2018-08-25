# PyFusion

PyFusion is an IntelliJ and Gradle plugin which work together to allow configuring Python projects in IntelliJ projects via Gradle.


#### Gradle Plugin
Sets attributes about Python.
Currently supported options:
* PythonSdk Parameters - Use to create Python SDKs in IntelliJ
  * **pythonExec** - Path to the python executable (e.g /usr/bin/python3)
  * **name** - (Optional) A custom name to use for this interpreter
  * **additionalPaths** - A list of additional paths to include 


* Python Parameters - Use to associate a gradle project with an IntelliJ SDK
  * **sdk** - The name of an _existing_ Python SDK. The SDK can be one created by this plugin, or one created by other means.

#### IntelliJ Plugin
Reads configuration information from the gradle plugin and configures Intellij


### Usage

build.gradle
```gradle
apply plugin: 'com.boxterra.python'

pythonSdk {
    pythonExec = '/usr/bin/python3'
    name = 'py_test'
    additionalPaths = ['/usr/python/cool_stuff', '/usr/python/other_stuff']
}

python {
    sdk = 'py_test'
}
```