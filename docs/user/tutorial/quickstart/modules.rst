:Author: Torben Barsballe
:Thanks: geotools-user list
:Version: |version|
:License: Creative Commons with attribution

**********************
 Java Modules 
**********************

Java 9 introduced the concept of modules to the JVM. 

A module is a named, self-contained bundle of Java code. Modules may depend upon other modules, and may export packages from themselves to other modules that depend upon them. Module configuration is controlled by the :file:`module-info.java` file.

In addition to the steps performed in the other tutorials (Eclipse, IntelliJ, Maven, Netbeans), which include setting up the GeoTools BOM for dependency management, the following steps are required to create a module:

#. Add a :file:`module-info.java` file under :file:`tutorial\src\main\java`. 

#. Name our module ``org.geotools.tutorial.quickstart``:

   .. code-block:: java

        module org.geotools.tutorial.quickstart { }

#. Then, add the modules we depend upon:

   .. literalinclude:: artifacts/module-info.java
      :language: java

   You'll notice the four GeoTools modules match those added as dependencies in the :file:`pom.xml`. Since we're using the GeoTools BOM for dependency management, these module versions are automatically coordinated with the Maven dependencies. We also include the ``java.desktop`` module, which contains user interface components required for the app to function.

For a more detailed overview of the module system, refer to the `State of the Module System <http://openjdk.java.net/projects/jigsaw/spec/sotms/>`_.

