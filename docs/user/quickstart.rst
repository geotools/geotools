.. _quickstart:

Quickstart
==========

Welcome to your first GeoTools project! We are going to set up a project to use GeoTools; and then display a shapefile on the screen.

Please note that GeoTools is large. Well actually quite HUGE. And it depends on a lot of other open source libraries,
toolkits, hacks and so on. Keeping track of all of this is a bit of a chore - so I would like to introduce a tool to
you.

*  Maven - http://maven.apache.org/

Maven is a build tool that is going to help sort all of this stuff out. You may be used to using ant, or sticking to the
safe confines of your IDE. If so, bear with me for a moment as we set up a simple maven project. I think you will find
this tool makes things much easier.

Ensure you have Java and Maven
------------------------------

You can check to see if you have the command line Maven utility installed and, if so, the version as follows::

 C:\java\geotools-example>mvn -version
 Maven version: 2.1.0
 Java version: 1.5.0_18
 OS name: "windows vista" version: "6.0" arch: "x86" Family: "windows"

I am using Java 1.5 above; and Maven 2.1.0. You can use Java 6 if you like; currently GeoTools is developed against Java
1.5 (for all the Java EE applications out there).

Notes:

* If you are using the Netbeans IDE for development then the command line Maven utility is optional because there is
  support for Maven within the IDE. The notes below refer to Netbeans version 6.8

* Windows and OSX users can download and install maven from apache: http://maven.apache.org/download.html

* Linux users can either download or use apt-get::  
 
    apt-get maven

Setting up your Project Folder
==============================

First of all let's use maven to create our project. You can do this from the command line::

 C:java> mvn archetype:generate

First up you'll be presented with a long list of archetypes to select from. Choose the default quickstart archetype
(number 15).

Next you'll get a series of prompts. Enter the following inputs:

  :groupId: *org.geotools.demo*
  :artifactId: *example*
  :version: *1.0-SNAPSHOT*
  :package: *org.geotools.demo.example*

The final prompt is just to confirm your choices. Press return and maven will wirr and click, downloading a bunch of
stuff before creating the *example* directory for you.

Eclipse IDE
-----------

Eclipse users now need to set things up for the IDE::

 C:java>cd example
 C:java\example>mvn eclipse:eclipse

You can now give Eclipse the background information it needs to talk to your "maven repository" (maven downloaded
something like 30 jars for you):

1. Start up Eclipse
2. Open up the Windows > Preferences menu
3. Navigate to the Java > Classpath Variables preferences page
4. Add an M2_REPO classpath variable pointing to your "local repository" (in your home directory):

 - Windows XP: C:\\Documents and Settings\\USERNAME\\.m2\\repository
 - Windows Vista: C:\\Users\\USERNAME\.m2\\repository
 - Linux and Mac: ~/.m2/repository
   
You can now import your project into eclipse:

1. Select the File > Import menu
2. Choose Existing Projects into Workspace from the list, and press Next
3. Select the project you created: C:\\java\\example
4. Finish

Netbeans IDE
------------

If you are using Netbeans you don't need to go through the steps outlined above for Eclipse. You can create a new Maven
project from within Netbeans rather than at the command line. To do this, select the Maven Quickstart Archetype in the
New Project dialog to create a basic pom.xml and directory structure, and then manually edit the POM to add the
necessary repositories and dependencies as outlined below. 

If you have already created your project at the command line (see above) then all that you need to do is open the
project within Netbeans as if it was a normal Java project.

Your New Project
----------------

1. In your IDE you can now now open up your *pom.xml* file and have a look at it:

.. sourcecode:: xml

    <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>org.geotools.demo</groupId>
      <artifactId>example</artifactId>
      <packaging>jar</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>example</name>
      <url>http://maven.apache.org</url>
      <dependencies>
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>3.8.1</version>
          <scope>test</scope>
        </dependency>
      </dependencies>
    </project>

2. This file describes your project for maven. Right now you have a single dependency on junit version 3.8.1.
3. You should be able to see this dependency in your IDE as well.

.. Tip:: Netbeans defaults to Java 1.3 format for new Maven projects. To
         correct this:

         * Go to the Project properties dialog
         * Select "Sources"
         * Set the "Source / binary format" to 1.5

Depending on GeoTools
---------------------

To make use of GeoTools we are going to add two things to your pom.xml file:

* A *properties* element defining the version of GeoTools that we want to use (2.7.0 for this example)
* A new dependency: ``gt-main``
* A list of *repositories* where maven can find GeoTools and all the cool stuff it uses

.. sourcecode:: xml

    <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>org.geotools.demo</groupId>
      <artifactId>example</artifactId>
      <packaging>jar</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>example</name>
      <url>http://maven.apache.org</url>
      <properties>
        <geotools.version>2.7.0</geotools.version>
      </properties>
      <dependencies>
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>3.8.1</version>
          <scope>test</scope>
        </dependency>
        <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-main</artifactId>
          <version>${geotools.version}</version>
        </dependency>
      </dependencies>
      <repositories>
        <repository>
          <id>maven2-repository.dev.java.net</id>
          <name>Java.net repository</name>
          <url>http://download.java.net/maven/2</url>
        </repository>
        <repository>
          <id>osgeo</id>
          <name>Open Source Geospatial Foundation Repository</name>
          <url>http://download.osgeo.org/webdav/geotools/</url>
        </repository>
      </repositories>
    </project>

Note how we use the *geotools.version* property with our ``gt-main`` dependency. 

In later tutorials we will just show the dependency section of the pom.xml file since you won't need to make any further
changes to the other sections. We will be adding dependencies over time as we try out more of the library.
      
Updating the IDE (Eclipse only)
-------------------------------

1. We can regenerate our .classpath and .project files so the IDE knows about this stuff::

     C:java\geotools-example>mvn eclipse:eclipse

2. Hit refresh in Eclipse
3. GeoTools (and a bunch of other stuff) will now show up in your project!)

If you are using Netbeans you don't need to worry about this step.

Modifying Main
--------------

Let's open up your App:

.. sourcecode:: java

         package org.geotools.demo.example;

         /**
          * Hello world!
          *
          */
         public class App
         {
             public static void main( String[] args )
             {
                 System.out.println( "Hello World!" );
             }
         }

And add some GeoTools code to it:

.. sourcecode:: java

         package org.geotools.demo.example;

         import org.geotools.factory.GeoTools;
         /**
          * Hello world!
          *
          */
         public class App
         {
             public static void main( String[] args )
             {
                 System.out.println( "Hello GeoTools:" + GeoTools.getVersion() );
             }
         }

You can build and run the application from within your IDE or from the command line.

Compiling your application from the command line is as simple as typing ``mvn compile``::

 C:\java\example>mvn compile
 [INFO] Scanning for projects...
 [INFO] ------------------------------------------------------------------------
 [INFO] Building example
 [INFO]    task-segment: [compile]
 [INFO] ------------------------------------------------------------------------
 [INFO] [resources:resources]
 [INFO] Using encoding: 'UTF-8' to copy filtered resources.
 [INFO] [compiler:compile]
 [INFO] Compiling 1 source file to C:\java\example\target\classes
 [INFO] ------------------------------------------------------------------------
 [INFO] BUILD SUCCESSFUL
 [INFO] ------------------------------------------------------------------------
 [INFO] Total time: 1 second
 [INFO] Finished at: Fri Aug 07 20:51:48 EST 2009
 [INFO] Final Memory: 5M/16M
 [INFO] ------------------------------------------------------------------------


Running your application from the command line is a bit more cumbersome, requiring this Maven incantation::

 C:\java\example>mvn exec:java -Dexec.mainClass="org.geotools.demo.example.App"
 [INFO] Scanning for projects...
 [INFO] Searching repository for plugin with prefix: 'exec'.
 [INFO] ------------------------------------------------------------------------
 [INFO] Building example
 [INFO]    task-segment: [exec:java]
 [INFO] ------------------------------------------------------------------------
 [INFO] Preparing exec:java
 [INFO] No goals needed for project - skipping
 [INFO] [exec:java]
 Hello GeoTools:2.7.0
 [INFO] ------------------------------------------------------------------------
 [INFO] BUILD SUCCESSFUL
 [INFO] ------------------------------------------------------------------------
 [INFO] Total time: 2 seconds
 [INFO] Finished at: Fri Aug 07 21:09:19 EST 2009
 [INFO] Final Memory: 7M/13M
 [INFO] ------------------------------------------------------------------------

.. tip:: If you will be running your application from the command line frequently you can avoid the long
         incantation above by specifying the main class in the pom.xml file. See the Maven documentation
         for details.
 
How to read and display a shapefile
===================================

Now that we have tried out maven, we can get down to working with some real spatial data. The shapefile format used by
ESRI products is in very common use. If you don't have a shapefile handy, you can download "world_borders.zip" and
"world_borders.prj" from the following location:

* http://www.mappinghacks.com/data/

You can also find some more sample data here:

* http://udig.refractions.net/docs/data.zip

.. note:: Please make sure to unzip the archive into the individual shp, dbf, and shx files. The prj file is used to
          describe the projection of the data and is very useful if you want to draw or perform analysis.

Adding the Shape and EPSG-HSQL Plugins to your Project
------------------------------------------------------

We are going to start by adding two plugins to our GeoTools application. Plugins are used to add functionality to the core library.

Here are the plugins we will be using to to read a shapefile.

* **gt-shapefile** used to reads file.shp, file.dbf, file.shx etc...
* **gt-epsg-hsql** used to read file.prj (map projection)
* **gt-swing** a collection of Swing GUI classes for GeoTools
* **gt-render** classes to draw map features

.. sourcecode:: xml

    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-shapefile</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-epsg-hsql</artifactId>
        <version>${geotools.version}</version>
    </dependency>
    <dependency>
        <groupId>org.geotools</groupId>
        <artifactId>gt-swing</artifactId>
        <version>${geotools.version}</version>
    </dependency>

Refresh your IDE Project Files
------------------------------

Eclipse users
~~~~~~~~~~~~~

1. You will need to kick these dependencies into your IDE with another::

     C:\\java\\example>mvn eclipse:eclipse

2. Hit refresh in Eclipse

Netbeans users
~~~~~~~~~~~~~~

Make sure you save the edits to your pom.xml file, then in the Projects window, right-click on the Libraries element of
the Project and select 'Download missing dependencies' from the pop-up menu.

Where did all these other JARs come from?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

You should now be able to see the two new dependencies. You'll also see a lot of extra jars that you didn't add ! 

GeoTools is divided up into a series of modules, plugins and extensions. For the background information on how GeoTools
slots together please read: http://docs.codehaus.org/display/GEOTDOC/02+Meet+the+GeoTools+Library

As well as all of its own jars, GeoTools makes use of a **lot** of third party jars. Following our "don't invent here"
(well, mostly) policy we turn to the experts to handle things such as geometry, image file operations, logging etc. So,
although you might only specify a small number of GeoTools dependencies in your pom.xml file, each of them will usually
rely on a number of other GeoTools and third party jars. And each of these jars in turn... well, you get the idea.

We want to stick to working on spatial code rather than worrying about all of these extra jars and this is where using
Maven can make your life a lot easier. It keeps track of the dependencies between jars for you, downloading the
necessary jars as required into a local cache (repository) on your system.

To see this in action you can ask Maven to print out a tree of the dependencies for your project my typing ``mvn
dependency:tree`` at the command line::

 C:\java\example> mvn dependency:tree 
 mvn dependency:tree
 [INFO] Scanning for projects...
 [INFO] Searching repository for plugin with prefix: 'dependency'.
 [INFO] ------------------------------------------------------------------------
 [INFO] Building example
 [INFO]    task-segment: [dependency:tree]
 [INFO] ------------------------------------------------------------------------
 [INFO] [dependency:tree]
 [INFO] org.geotools.demo.example:example:jar:1.0-SNAPSHOT
 [INFO] +- junit:junit:jar:3.8.1:test
 [INFO] +- org.geotools:gt-main:jar:2.7.0:compile
 [INFO] |  +- org.geotools:gt-api:jar:2.7.0:compile
 [INFO] |  +- com.vividsolutions:jts:jar:1.9:compile
 [INFO] |  +- jdom:jdom:jar:1.0:compile
 [INFO] |  \- commons-beanutils:commons-beanutils:jar:1.7.0:compile
 [INFO] |     \- commons-logging:commons-logging:jar:1.0.3:compile
 [INFO] +- org.geotools:gt-shapefile:jar:2.7.0:compile
 [INFO] |  \- org.geotools:gt-referencing:jar:2.7.0:compile
 [INFO] |     +- java3d:vecmath:jar:1.3.1:compile
 [INFO] |     +- commons-pool:commons-pool:jar:1.3:compile
 [INFO] |     \- org.geotools:gt-metadata:jar:2.7.0:compile
 [INFO] |        +- org.opengis:geoapi:jar:2.2-SNAPSHOT:compile
 [INFO] |        \- net.java.dev.jsr-275:jsr-275:jar:1.0-beta-2:compile
 [INFO] \- org.geotools:gt-epsg-hsql:jar:2.7.0:compile
 [INFO]    \- hsqldb:hsqldb:jar:1.8.0.7:compile
 [INFO] ------------------------------------------------------------------------
 [INFO] BUILD SUCCESSFUL
 [INFO] ------------------------------------------------------------------------
 [INFO] Total time: 7 seconds
 [INFO] Finished at: Fri Aug 07 20:44:02 EST 2009
 [INFO] Final Memory: 12M/22M
 [INFO] ------------------------------------------------------------------------


Example Code
------------

The following example is available from:

  http://svn.osgeo.org/geotools/tags/2.7.0/demo/example/src/main/java/org/geotools/demo/Quickstart.java

It is also included in the demo directory when you download geotools.

Application
-----------
We are going to create an application to open a shapefile and display it with a simple map viewer.

The code for the application is shown below. It consists of a single class: 

  **org.geotools.demo.Quickstart** 

Copy and paste the code into your IDE as part of your Maven project:

   .. literalinclude:: ../../demo/example/src/main/java/org/geotools/demo/Quickstart.java
      :language: java
      :start-after: // docs start source
      :end-before: // docs end main
   
Add a closing curly brace for the class and then build the application, either from within your IDE or from the command
line with ``mvn compile``.

If the application compiled you can now run it. Once again, you can do this from within your IDE or from the command
line. The program should display a dialog prompting you for a shapefile and then display it in a simple map viewer.

.. image:: QuickstartMap.jpg

Optional extra: speeding up the display of features
---------------------------------------------------
This application is reading the shapefile directly every time you refresh the display, for instance when you zoom in or
out. If the shapefile is large you will notice some delay while the data are read from disk. One way to speed up the
display's responsiveness is to cache some or all of the feature data in memory. **CachingFeatureSource** provides this
ability. It reads features from the original feature source (e.g. your shapefile) and stores them in memory in a
spatially indexed manner. Obviously the amount of memory available determines how practical this is for any particular
feature source.

The method below shows how to introduce **CachingFeatureSource** into our example application:

   .. literalinclude:: ../../demo/example/src/main/java/org/geotools/demo/Quickstart.java
      :language: java
      :start-after: // docs start cache
      :end-before: // docs end source
   
Questions
=========

What is a GeoTools SNAPSHOT version and how do I use it ?
---------------------------------------------------------

A snapshot is the bleeding edge GeoTools code that the developers are actively working on. Usually there will be two
active snapshots: one associated with the most recent formal release (e.g GeoTools 2.7-SNAPSHOT) and a second for work
that is continuing on a earlier version that is being widely used (e.g. GeoTools 2.6-SNAPSHOT).

New snapshot jars are built nightly and deployed to a repository separate from the one used for formal releases. To work
with the snapshot code in your own applications you will need to add this repository to your pom.xml:

.. sourcecode:: xml

    <repository>
      <id>opengeo</id>
      <name>OpenGeo Maven Repository</name>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <url>http://repo.opengeo.org/</url>
    </repository>

You can now refer to 2.7-SNAPSHOT with your version property:

.. sourcecode:: xml

    <properties>
      <geotools.version>2.7-SNAPSHOT</geotools.version>
    </properties>

What are FileDataStore and FeatureSource ?
------------------------------------------

Here is how this all fits together:

* FileDataStore represents the shapefile and allows you to work with the "shp", "dbf" and "prj" files as a group (even
  generating a new "qnx" index if needed)
* FeatureSource is used to read the data in the shapefile; you can perform queries and get a FeatureCollection out
* FeatureStore is used to modify the data; you can add features; and update features etc...
* FeatureCollection is used work with Features. Please note that this is more like a result set or data stream than a 
  Java Collection (you will need to close each iterator after use)
* Iterator, FeatureIterator or FeatureVisitors can all be used process the Features in your FeatureCollection.
* Each Feature has a Geometry (a JTS Geometry object)
* Each Feature has a number of Attributes (String, Integers, etc...)
* The FeatureCollection has a schema (ie a FeatureType) which tells you what the String, Integers, etc mean
* There is a CoordinateReferenceSystem to tell you what the Coordinates mean - so if you want to draw the shapefile 
  you can tell where in the world the coordinates go.
  
How can I write a Shapefile?
----------------------------

For an introduction to creating Features and writing them to a shapefile have a look at :ref:`csv2shp`

Can the program read files that are several MB in size?
-------------------------------------------------------

Yes the shapefile reading code actually does not read anything until you open up an iterator(); and then it only keeps
the file open as you call next(), .. hasNext(), ... next() ... etc...

The approach used is to "stream" the content into your application as you read; it does NOT load it into memory allowing
you to work with massive files. GIS data is almost always big; so this approach is needed.

If you have database experience you may wish to think of a FeatureCollection as a prepared statement, and iterator() as
executing the query.

