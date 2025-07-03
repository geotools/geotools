:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

**********************
 Maven Quickstart 
**********************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>
.. sectionauthor:: Michael Bedward <michael.bedward@gmail.org>

Welcome 
=======

This tutorial is for those who prefer the pleasant company of a text editor and a trusty command
prompt. Even if you routinely use an IDE, you will find that it's often quicker and easier to
compile, test and install your applications from the command line. We'll be using Maven
(http://maven.apache.org/) to manage the large number of jars that a GeoTools projects depend on.
Don't worry if you're not familiar with Maven because we will explain everything step by step.

The example application is the same one used for the NetBeans and Eclipse Quickstart tutorials: a
simple program to load and display a shapefile.

We would like thank members of the `GeoTools User mailing list
<https://lists.sourceforge.net/lists/listinfo/geotools-gt2-users>`_ for their feedback while we were
preparing the course material, with special thanks to Eva Shon for testing/reviewing early drafts. If you have any questions or comments about this tutorial, please post them to the user list.

Java Install
============

We are going to be making use of Java so if you don't have a Java Development Kit (JDK) installed now is the time to do so. 

#. Download the latest Java 11 JDK:

   * OpenJDK: http://openjdk.java.net/
   * Adoptium distribution: https://adoptium.net/
    
#. Click through the installer you will need to set an acceptance a license agreement and so forth.
     
.. Note::

   In this tutorial we refer to file and directory paths as used by Windows. If you are fortunate
   enough to be using another operating system such as Linux or OSX all of the commands and source
   code below will work, just modify the paths to suit. 


Maven (and why it's not so bad)
===============================

Maven is a widely-used build tool which works by describing the contents of a project. This is a
different approach than that used by the Make or Ant tools which list the steps required to build.

It takes a while to get used to Maven and, for some, it remains a love-hate relationship, but it
definitely makes working with GeoTools much easier:

* You only download as much of GeoTools as your application requires.
 
* Jars are downloaded into a single location in your home directory (e.g. :file:`C:\\Documents and Settings\<user\>\\.m2` 
  on Windows). This is your *local repository*.

* The correct versions of all of the third-party jars required by GeoTools will be downloaded for
  you. This helps you to avoid obscure errors than can be caused by mismatched dependencies which
  can be very difficult to track down.

* The single *local repository* makes it easier to work on other multiple open source projects.

* GeoTools provides a **Bill of Materials (BOM)** that manages dependency versions automatically,
  ensuring all GeoTools modules and their transitive dependencies work together correctly.


Installing Maven
----------------

#. Download Maven from http://maven.apache.org/download.html 
   
   In this tutorial we refer to Maven version 3.8.6.
   
#. Unzip the file ``apache-maven-3.8.6-bin.zip``

#. You need to have a couple of environmental variables set for maven to work. Navigate to
   :menuselection:`Control Panel --> System --> Advanced`. Change to the :guilabel:`Advanced` tab and click :guilabel:`Environmental Variables` button.
   
   Add the following system variables (short paths on Windows might be needed):
   
   * JAVA_HOME = :file:`C:\PROGRA~1\ECLIPS~1\jdk-11.0.17.8-hotspot`
   * MAVEN_HOME = :file:`C:\\java\\apache-maven-3.8.6`
   
   And add the following to your PATH:
   
   * PATH = :file:`%JAVA_HOME%\\bin;%MAVEN_HOME%\\bin`
   
   .. image:: images/env-variables.png
      :scale: 60
   
#. Open up a commands prompt :menuselection:`Accessories --> Command Prompt`

#. Type the following command to confirm you are set up correctly::
   
      C:java> mvn --version
      
#. This should produce something similar to the following output::

      C:\java>mvn -version
      Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
      Maven home: C:\devel\apache-maven-3.8.6
      Java version: 11.0.17, vendor: Eclipse Adoptium, runtime: C:\PROGRA~1\ECLIPS~1\jdk-11.0.17.8-hotspot
      Default locale: it_IT, platform encoding: Cp1252
      OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

   .. image:: images/maven-version.png
      :scale: 60

Creating a new project
----------------------

#. We can now generate our project from maven-archetype-quickstart with::

      C:>cd C:\java
      C:java>  mvn archetype:generate -DgroupId=org.geotools -DartifactId=tutorial -Dversion=1.0-SNAPSHOT -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart

#. The above command creates the following files and directories::
   
        tutorial
        tutorial\pom.xml
        tutorial\src
        tutorial\src\main
        tutorial\src\main\java
        tutorial\src\main\java\org
        tutorial\src\main\java\org\geotools
        tutorial\src\main\java\org\geotools\App.java
        tutorial\src\test
        tutorial\src\test\java
        tutorial\src\test\java\org
        tutorial\src\test\java\org\geotools
        tutorial\src\test\java\org\geotools\AppTest.java

   ``App.java`` and ``AppTest.java`` are just placeholder files not used in this tutorial.
   
#. During the build process your local maven repository will be used to store both
   downloaded jars, and those you build locally.
   
   Your local Maven repository is located in your home folder.
    
    ==================  ========================================================
       PLATFORM           LOCAL REPOSITORY
    ==================  ========================================================
       Windows XP:      :file:`C:\\Documents and Settings\\You\\.m2\\repository`
       Windows:         :file:`C:\\Users\\You\.m2\\repository`
       Linux and Mac:   :file:`~/.m2/repository`
    ==================  ========================================================

#. Open the ``pom.xml`` file in your favorite text editor. If your editor has an XML syntax mode
   switch into that now because it will make it a lot easier to find errors such as mismatched
   brackets. Some editors, such as `vim <http://www.vim.org/>`_, will do this automatically on
   loading the file.

#. We are going to start by defining the version number of GeoTools we wish to use. This workbook
   was written for |release| although you may wish to try a different version.
   
   For production a stable release of |branch| should be used for `geotools.version`:
    
   .. literalinclude:: /../../tutorials/quickstart/pom.xml
      :language: xml
      :start-after: <url>http://maven.apache.org</url>
      :end-before: <dependencyManagement>
   
   To make use of a nightly build set the `geotools.version` property to |branch|-SNAPSHOT .

#. We use the GeoTools Bill of Materials (BOM) to manage dependency versions. This ensures that all GeoTools modules use compatible versions and simplifies dependency management:

   .. literalinclude:: /../../tutorials/quickstart/pom.xml
      :language: xml
      :start-at: <dependencyManagement>
      :end-at: </dependencyManagement>

   The BOM (Bill of Materials) pattern is a Maven best practice that centralizes version management. By importing the ``gt-bom``, we don't need to specify version numbers for individual GeoTools modules.
   
#. We specify the following dependencies (GeoTools modules which your application will need).

   Note that we don't specify version numbers since these are managed by the BOM:

   .. literalinclude:: /../../tutorials/quickstart/pom.xml
      :language: xml
      :start-after: </dependencyManagement>
      :end-at: </dependencies>
        
#. We tell maven which repositories to download jars from:
   
   .. literalinclude:: /../../tutorials/quickstart/pom.xml
      :language: xml
      :start-at: <repositories>
      :end-before: <build>

   .. note:: Note the snapshot repository above is only required if you are using a nightly build (such as |branch|-SNAPSHOT)

#. GeoTools requires Java 11, you need to tell Maven to use the 11 source level

   .. literalinclude:: /../../tutorials/quickstart/pom.xml
      :language: xml
      :start-after: </repositories>
      :end-before: <profiles>

#. Here is what the completed :file:`pom.xml` looks like:

   .. literalinclude:: /../../tutorials/quickstart/pom.xml
        :language: xml
        :end-before: <profiles>
        :append: </project>
   
   * Recommend cutting and pasting the above to avoid mistakes when typing
   
   * You may also download :download:`pom.xml </../../tutorials/quickstart/pom.xml>`, if this opens in your browser use :command:`Save As` to save to disk.
   
     The download has an optional quality assurance profile you can safely ignore. 

#. Return to the command line and get maven to download the required jars for your project with this
   command:
   
   .. code-block:: bash
    
      mvn install
      
#. If maven has trouble downloading any jar, you can always try again. A national mirror is often faster than the default maven central.
   
Creating the Quickstart application
-----------------------------------

Now we are ready to create the application.

#. Crete the *org.geotools.tutorial.quickstart* package by navigating to the directory
   :file:`tutorial` and create the directory :file:`src\\main\\java\\org\\geotools\\tutorial\\quickstart`

#. In the new sub-directory, create a new file ``Quickstart.java`` using your text editor.

#. Fill in the following code :file:`Quickstart.java`:
  
   .. literalinclude:: /../../tutorials/quickstart/src/main/java/org/geotools/tutorial/quickstart/Quickstart.java
      :language: java
      
   * You may find cutting and pasting from the documentation to be easier then typing.
   
   * You may also download :download:`Quickstart.java </../../tutorials/quickstart/src/main/java/org/geotools/tutorial/quickstart/Quickstart.java>`

#. Go back to the top project directory (the one that contains your :file:`pom.xml` file) and build the
   application with the command:
   
   .. code-block:: bash

     mvn clean install


Running the application
-----------------------
        
#. If you need some shapefiles to work with you will find a selection of data at the
   http://www.naturalearthdata.com/ project which is supported by the North American Cartographic
   Information Society. Head to the link below and download some cultural vectors. You can use the 'Download all 50m cultural themes' at top.
   
   * `1:50m Cultural Vectors <http://www.naturalearthdata.com/downloads/50m-cultural-vectors/>`_
   
   Unzip the above data into a location you can find easily such as the desktop.

#. You can run the application using Maven on the command line:

   .. code-block:: bash
   
      mvn exec:java -Dexec.mainClass=org.geotools.tutorial.quickstart.Quickstart
   
#. The application will connect to your shapefile, produce a map context, and display the shapefile.

   .. image:: images/QuickstartMap.png
      :scale: 60
         
#. A couple of things to note about the code example:
   
* The shapefile is not loaded into memory. Instead it is read from disk each and every time it is needed.
  This approach allows you to work with data sets larger than available memory.
      
* The example uses very simple display style here that just shows feature outlines. In the tutorials
  that follow we will see how to specify more sophisticated styles.

Things to Try
=============

* When working in a text editor instead of an IDE use the `GeoTools javadocs 
  <http://docs.geotools.org/latest/javadocs/>`__ to work out what import statements are required
  in your source. The javadocs also list the GeoTools module in which each class is found.

.. include:: try.txt

* To force maven to download new snapshots:
  
  .. code-block:: bash
  
     mvn clean install -U
  
  To avoid downloading any new snapshots (when offline) use:
  
  .. code-block:: bash
  
     mvn clean install -nsu

* So what jars did maven actually use for the Quickstart application? Try the following on the
  command line::
  
    mvn dependency:tree
  
  We will be making use of some of the project in greater depth in the remaining tutorials.
