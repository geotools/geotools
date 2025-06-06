Maven Bill of Materials (BOM)
=============================

GeoTools 34 introduces the Maven Bill of Materials (BOM) pattern to improve dependency management. This document explains how the BOM is structured and how to use it effectively both for internal GeoTools development and for downstream projects.

What is a BOM?
--------------

A BOM (Bill of Materials) is a special POM file that manages dependency versions across a project. Rather than specifying versions for each dependency individually, you can import the BOM which provides consistent versioning.

GeoTools provides two BOMs:

1. ``gt-platform-dependencies`` - Manages third-party dependency versions
2. ``gt-bom`` - Manages GeoTools module versions (also imports platform-dependencies)

BOM Structure
-------------

The GeoTools BOM system is organized into two main components:

1. **Platform Dependencies BOM** (``gt-platform-dependencies``)

   * Located at: ``/platform-dependencies/pom.xml``
   * Contains version management for all third-party dependencies used across GeoTools
   * Includes libraries like JTS, Jackson, logging frameworks, JDBC drivers, etc.
   * Defined using standard Maven ``<dependencyManagement>`` section with explicit versions

2. **GeoTools Modules BOM** (``gt-bom``)

   * Located at: ``/bom/pom.xml``
   * Imports the platform dependencies BOM
   * Manages versions for all GeoTools modules
   * Covers GeoTools library modules, plugin modules, extensions modules, and test artifacts

BOM Usage in GeoTools Development
---------------------------------

When developing GeoTools modules, you benefit from the BOM without explicitly importing it, as it is managed at the parent POM level. This means:

1. No version needed for GeoTools dependencies::

    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
    </dependency>

2. No version needed for third-party dependencies declared in platform-dependencies::

    <dependency>
      <groupId>org.locationtech.jts</groupId>
      <artifactId>jts-core</artifactId>
    </dependency>

This approach has several benefits for GeoTools development:

* Consistent dependency versions across all modules
* Reduced maintenance burden - only update versions in one place
* Simplified dependency declarations in module POMs
* Better controlled transitive dependencies

The GeoTools internal development model follows the "eat your own dog food" principle - the project uses the BOM itself to manage dependencies across its modules, ensuring it works as expected.

BOM Usage for Downstream Projects
---------------------------------

Applications using GeoTools should import the ``gt-bom`` in their dependency management. This ensures consistent versioning of all GeoTools modules and their transitive dependencies.

To use the GeoTools BOM in your project:

1. Import the BOM in your ``dependencyManagement`` section::

    <dependencyManagement>
      <dependencies>
        <!-- Import GeoTools BOM -->
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-bom</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
      </dependencies>
    </dependencyManagement>

2. Declare GeoTools dependencies without versions::

    <dependencies>
      <!-- No version needed for GeoTools modules -->
      <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
      </dependency>
      <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-epsg-hsql</artifactId>
      </dependency>
      
      <!-- Other non-GeoTools dependencies still need versions -->
      <dependency>
        <groupId>com.example</groupId>
        <artifactId>my-library</artifactId>
        <version>1.0.0</version>
      </dependency>
    </dependencies>

Using Platform Dependencies Only
--------------------------------

In some cases, you might want to use only the platform dependencies BOM:

.. code-block:: xml

    <dependencyManagement>
      <dependencies>
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-platform-dependencies</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
      </dependencies>
    </dependencyManagement>

This approach is useful if you only need the third-party dependencies managed by GeoTools but not the GeoTools modules themselves.

Test Artifacts
--------------

The BOM also manages test artifacts, which use the ``tests`` classifier:

.. code-block:: xml

    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
      <classifier>tests</classifier>
      <scope>test</scope>
    </dependency>

Version Overrides
-----------------

If you need to override a specific dependency version managed by the BOM:

.. code-block:: xml

    <dependencyManagement>
      <dependencies>
        <!-- Import GeoTools BOM -->
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-bom</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
        
        <!-- Override a specific dependency -->
        <dependency>
          <groupId>org.locationtech.jts</groupId>
          <artifactId>jts-core</artifactId>
          <version>1.19.0</version>
        </dependency>
      </dependencies>
    </dependencyManagement>

Multiple BOMs
-------------

When using Spring Framework or other frameworks that provide their own BOMs, you can combine them with GeoTools BOM:

.. code-block:: xml

    <dependencyManagement>
      <dependencies>
        <!-- Spring Framework BOM -->
        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-framework-bom</artifactId>
          <version>5.3.39</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
        
        <!-- GeoTools BOM -->
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-bom</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
      </dependencies>
    </dependencyManagement>

Benefits
--------

Using the GeoTools BOM offers several advantages:

1. **Simplified dependency management**: No need to specify versions for each GeoTools module
2. **Version consistency**: All GeoTools modules use compatible versions
3. **Easier upgrades**: Change one version number instead of dozens
4. **Reduced transitive dependency conflicts**: The BOM ensures compatible dependencies
5. **Industry standard approach**: Following Maven best practices

Common Issues
-------------

Missing Dependency Version
^^^^^^^^^^^^^^^^^^^^^^^^^^

If you see an error like::

    'dependencies.dependency.version' for org.geotools:gt-main:jar is missing

It means you haven't properly imported the BOM in your ``dependencyManagement`` section.

Version Conflicts
^^^^^^^^^^^^^^^^^

If you see version conflicts, check if you're:

1. Importing the BOM correctly with ``<scope>import</scope>`` and ``<type>pom</type>``
2. Not overriding versions unintentionally
3. Using compatible versions of BOMs if importing multiple
