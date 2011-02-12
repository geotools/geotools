Project Object Model (POM) Files
--------------------------------

Complete documentation for the project.xml file for maven can be found at the maven site, and in particular in the project descriptor part of the reference section.

We only show the things specific to a GeoTools **module pom.xml** file here.

Extending a Parent Module
^^^^^^^^^^^^^^^^^^^^^^^^^

The <parent> section allows one pom.xml file to inherit items from another. Modules should extend the pom.xml within the module, plugin, extension or demo directory they belong to:

.. code-block:: xml
   
   <parent>
     <groupId>org.geotools</groupId>
     <artifactId>gt-module</artifactId>
     <version>2.2-SNAPSHOT</version>
   </Parent>

Id
^^

The id should reflect the name of the module.
We use a “gt” prefix to avoid conflicts (as the groupId org.geotools does not appears in the final JAR filenames)

Examples:

* gt-main
* gt-referencing.

This policy allows our “gt-main.jar” to avoid conflict with a application “main” jar created that makes use of GeoTools.

Dependency Management
^^^^^^^^^^^^^^^^^^^^^

Dependencies are specified within the  pom.xml file, but care should be taken to handle this in a consistent manner.

New dependencies on another GeoTools module, should use:

* groupId - identify the project
* artifactId - identify the jar within that project
* version - ${project.version} to match the current build

Sample pom.xml dependency entry::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
      <version>${project.version}</version>
   </Dependency>

New dependencies on a third-party jar need to be handled in two parts.

The dependency is supplied in the pom.xml file::
   
   <dependency>
      <groupId>javax.media</groupId>
      <artifactId>jai_codec</artifactId>
      <!-- The version number is specified in the parent POM. -->
      <scope>test</scope>
   </Dependency>

And the dependency version is supplied in the “root” pom.xml file in the dependency management section.

Dependency Version Changes
^^^^^^^^^^^^^^^^^^^^^^^^^^

When a dependency changes you will need to update the root pom.xml "dependency management section" to reflect the new version number.

You should be able to locate an entry like this and change the version number::
   
   <dependency>
      <groupId>net.java.dev.swing-layout</groupId>
      <artifactId>swing-layout</artifactId>
      <version>1.0.2</version>
   </dependency>