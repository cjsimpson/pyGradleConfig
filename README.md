# Python Gradle Plugin

IntelliJ and Gradle pluings to allow configuring Python in IntelliJ projects via Gradle.


#### Gradle Plugin
Sets attributes about Python.
Currently supported options:
* **sdk** - The name of an _existing_ Python SDK

#### IntelliJ Plugin
Reads configuration information from the gradle plugin and configures Intellij


### Usage

build.gradle
```gradel
apply plugin: 'python'

python {
    sdk 'Python 3.5'
}
```

#### Future
* Ability to create Python SDK