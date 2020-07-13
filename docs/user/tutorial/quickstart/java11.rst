:Author: Torben Barsballe
:Thanks: geotools-user list
:Version: |release|
:License: Creative Commons with attribution

**********************
 Java 11 Quickstart 
**********************

.. sectionauthor:: Torben Barsballe <tbarsballe@boundlessgeo.com>

Welcome 
=======

GeoTools 21.0 and newer can be used in a Java 11 modular application. This requires a few additions on top of the regular tutorial application.

This quickstart assumes you have already run through one of the regular quickstarts:

 * `Eclipse <./eclipse.html>`_
 * `NetBeans <./netbeans.html>`_
 * `IntelliJ <./intellij.html>`_
 * `Maven <./maven.html>`_

Java Install
============

Instead of Java 8, we will want to install Java 11.

#. Download the latest Java 11 JDK:

   * OpenJDK: http://jdk.java.net/11/

   .. info:

      OpenJDK updates every six months, for longer term support consider:
      
      * Adopt OpenJDK: https://adoptopenjdk.net
      
      Oracle distributes a supported JDK 11 for their customers, which is available for development and testing. It is **not** available for production use without a license:
      
      * http://www.oracle.com/technetwork/java/javase/downloads/
   
#. At the time of writing the latest Java 11 release was:
   
   * ``jdk-11.0.1``
   
#. Click through the installer you will need to set an acceptance a license agreement and so forth.
   By default this will install to:
   
   * :file:`C:\\Program Files\\Java\jdk-11.0.1`

#. Update the ``JAVA_HOME`` system variable so that maven uses the newly installed Java 11:

   * JAVA_HOME = :file:`C:\\Program Files\\Java\\jdk-11.0.1`

#. Verify ``mvn`` is using the correct Java version by running:

   .. code-block:: sh

       mvn -version

   It should report a Java version of 11.0.1.

.. Note::

   In this tutorial we refer to file and directory paths as used by Windows. If you are fortunate
   enough to be using another operating system such as Linux or OSX all of the commands and source
   code below will work, just modify the paths to suit. 

Updating the POM
----------------

A few changes are required to compile and run on Java 11:

#. Add dependencies for **gt-render** and **gt-main**, since we import packages from them:

    .. literalinclude:: artifacts/pom-jdk11.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>

#. Update the Maven plugin versions. We want to ensure we use the most recent, Java 11-compatible, versions. We will also update the configuration of the Maven compile plugin to target Java 11:

    .. literalinclude:: artifacts/pom-jdk11.xml
        :language: xml
        :start-after: </repositories>
        :end-before: </project>

Adding Module Info
------------------

Java 9 introduced the concept of modules to the JVM. 

A module is a named, self-contained bundle of Java code. Modules may depend upon other modules, and may export packages from themselves to other modules that depend upon them. Module configuration is controlled by the :file:`module-info.java` file.

For a more detailed overview of the module system, refer to the `State of the Module System <http://openjdk.java.net/projects/jigsaw/spec/sotms/>`_.

#. Add a :file:`module-info.java` file under :file:`tutorial\src\main\java`. 

#. Name our module ``org.geotools.tutorial.quickstart``:

   .. code-block:: java

        module org.geotools.tutorial.quickstart { }

#. Then, add the modules we depend upon:

   .. literalinclude:: artifacts/module-info.java
      :language: java

   You'll notice the four GeoTools modules match those added as dependencies in the :file:`pom.xml`. We also include the ``java.desktop`` module, which contains user interface components required for the app to function.

Running the application
-----------------------
        
#. If you need some shapefiles to work with you will find a selection of data at the
   http://www.naturalearthdata.com/ project which is supported by the North American Cartographic
   Information Society. Head to the link below and download some cultural vectors. You can use the 'Download all 50m cultural themes' at top.
   
   * `1:50m Cultural Vectors <http://www.naturalearthdata.com/downloads/50m-cultural-vectors/>`_
   
   Unzip the above data into a location you can find easily such as the desktop.

#. You can run the application using Maven on the command line::
   
     mvn exec:java -Dexec.mainClass=org.geotools.tutorial.quickstart.Quickstart
   
#. The application will connect to your shapefile, produce a map context, and display the shapefile.

   .. image:: images/QuickstartMap.png
      :scale: 60
   
#. A couple of things to note about the code example:
   
* The shapefile is not loaded into memory. Instead it is read from disk each and every time it is needed.
  This approach allows you to work with data sets larger than available memory.
      
* We are using a very basic display style here that just shows feature outlines. In the examples
  that follow we will see how to specify more sophisticated styles.


Things to Try
=============

* Try out the different sample data sets.

* Try adding a different profiles to your :file:`pom.xml` for running on Java 8 and Java 11

  .. Hint::
     You'll want to exclude the module-info,java file from the build when running on Java 8.

* Advanced: Try to get another tutorial to run on Java 11 using the module system.
