Maven Bill of Materials (BOM)
=============================

GeoTools 34 introduces the Maven Bill of Materials (BOM) pattern to improve dependency management. This document explains how the BOM is structured and how to use it effectively both for internal GeoTools development and for downstream projects.

What is a BOM?
--------------

A BOM (Bill of Materials) is a special POM file that manages dependency versions across a project. Rather than specifying versions for each dependency individually, you can import the BOM which provides consistent versioning.

GeoTools provides two BOMs that serve different purposes and are imported separately:

1. ``gt-platform-dependencies`` - Manages third-party dependency versions only
2. ``gt-bom`` - Manages GeoTools module versions only

**Important**: These BOMs are independent and should be imported separately based on your project's needs.

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
   * Manages versions for all GeoTools modules only
   * Covers GeoTools library modules, plugin modules, extensions modules, and test artifacts
   * Does not include third-party dependencies (these are managed separately by ``gt-platform-dependencies``)

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

**Simple Applications using GeoTools**

Applications using GeoTools should import the ``gt-bom`` in their dependency management. This ensures consistent versioning of all GeoTools modules.

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

**Projects Heavily Integrated with GeoTools**

Projects like GeoServer and GeoWebCache that are heavily integrated with GeoTools should import both BOMs to centralize management of shared third-party dependencies:

.. code-block:: xml

    <dependencyManagement>
      <dependencies>
        <!-- Import platform dependencies for third-party libraries -->
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-platform-dependencies</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>

        <!-- Import GeoTools modules BOM -->
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

The ``gt-platform-dependencies`` BOM can be imported separately from ``gt-bom`` when you only need third-party dependency management:

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

**When to use gt-platform-dependencies only:**

* You're building a project that uses some of the same third-party libraries as GeoTools (JTS, Jackson, etc.) but don't need GeoTools modules
* You want to ensure version compatibility with GeoTools without depending on GeoTools itself
* You're creating a library that will be used alongside GeoTools

**When to use gt-bom only:**

* You're building a simple application that uses GeoTools modules (recommended for most users)
* You're following the quickstart tutorials
* You only need GeoTools modules and are fine with transitive dependency versions

**When to use both BOMs:**

* You're building a project heavily integrated with GeoTools (like GeoServer, GeoWebCache)
* You need centralized management of both GeoTools modules and shared third-party dependencies
* You want explicit control over third-party library versions used by both your project and GeoTools

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

Combining Multiple BOMs
-----------------------

You can combine GeoTools BOMs with other framework BOMs. Here are common patterns:

**Simple Application with Spring Framework:**

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

**Complex Application with Full Dependency Control:**

.. code-block:: xml

    <dependencyManagement>
      <dependencies>
        <!-- Platform dependencies for third-party libraries -->
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-platform-dependencies</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>

        <!-- GeoTools modules -->
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-bom</artifactId>
          <version>34.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>

        <!-- Other framework BOMs -->
        <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-framework-bom</artifactId>
          <version>5.3.39</version>
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
