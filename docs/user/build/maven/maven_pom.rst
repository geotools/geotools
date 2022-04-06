Project Object Model (POM) Files
--------------------------------

Complete documentation for the project XML file for Maven can be found at the Maven site, and in particular in the project descriptor part of the reference section.

We only show the things specific to a GeoTools module :file:`pom.xml` file here.

Extending a Parent Module
^^^^^^^^^^^^^^^^^^^^^^^^^

The <parent> section allows one ``pom.xml`` file to inherit items from another. Modules should extend the :file:`pom.xml` within the module, plugin, extension or demo directory they belong to:

.. code-block:: xml
   
   <parent>
     <groupId>org.geotools</groupId>
     <artifactId>gt-module</artifactId>
     <version>2.2-SNAPSHOT</version>
   </Parent>

Artifact Id and GroupId
^^^^^^^^^^^^^^^^^^^^^^^

The combination of `id` and `groupId` uniquely identifies each artifact in a maven build.

The `id` should reflect the name of the module.
We use a “gt” prefix to avoid conflicts (as the ``groupId`` ``org.geotools`` does not appear in the final JAR filenames).

Examples:

* gt-main
* gt-referencing.

This policy allows our :file:`gt-main.jar` to avoid conflict with an application :file:`main.jar` created that makes use of GeoTools.

The `groupId` is initially based reverse domain name `org.geotools` (following Java package name rules). GeoTools is a multi-module build with each submodule appending to the parent's groupId.

Examples:

* org.geotools
* org.geotools.plugin
* org.geotools.extension

Dependencies
^^^^^^^^^^^^

Dependencies are specified within the :file:`pom.xml` file, but care is required to handle this in a consistent manner.

New dependencies on another GeoTools module, should use:

* ``groupId`` - identify the project
* ``artifactId`` - identify the jar within that project
* ``version`` - ``${project.version}`` to match the current build

Sample :file:`pom.xml` dependency entry:

.. code-block:: xml
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
      <version>${project.version}</version>
   </Dependency>

Dependencies on a third-party jar are defined in two parts.

First the dependency is supplied in the ``pom.xml`` file:

.. code-block:: xml

   <dependencies>
      ...
      <dependency>
        <groupId>org.postgis</groupId>
        <artifactId>postgis-driver</artifactId>
        <!-- The version number is specified in the parent POM. -->
      </dependency>
      ...
   </dependencies>

Second the dependency version is supplied in the “root” :file:`pom.xml` file in the dependency management section:

.. code-block:: xml

   <dependencyManagement>
      ...
      <dependency>
        <groupId>org.postgis</groupId>
        <artifactId>postgis-driver</artifactId>
        <version>1.0</version>
      </dependency>
      ...
   <dependencyManagement>

We make use of properties to update multi-module dependencies at the same time:

.. literalinclude:: /../../pom.xml
   :start-after: <!-- dependency management -->
   :end-before: <!-- javadoc configuration -->
   :prepend: <properties>
   :append: </properties>
