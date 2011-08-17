:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

**********************
 Maven Quickstart 
**********************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>

Welcome Maven Developers
========================

This quickstart is under construction; it provides a command line quickstart for those who perfer
the plesent company of a text editor and a trusty command prompt.

Welcome to **Geospatial for Java** this workbook is aimed at Java developers who are new to
geospatial and would like to get started.

The build tool Maven (http://maven.apache.org/) is our preferred option for downloading and
managing jars for GeoTools projects because there tend to a large number of jars involved. If
you are already familiar with Maven that is an advantage but if not, don't worry, we will be
explaining things step by step.

Extra care has been taken to these tutorials visual right from the get go. While these the
examples will make use of Swing please be assured that that this is only an aid in making the
examples easy and fun to use so if your own work is based on another GUI framework the material
that we cover here will still be relevant.

These sessions are applicable to both server side and client side development.

We would like thank members of the GeoTools users list for their feedback while were preparing the
course material, with special thanks to Tom Williamson for reviewing early drafts.

Java Install
============

We are going to be making use of Java so if you don't have a Java Development Kit installed now is
the time to do so. Even if you have Java installed already check out the optional Java Advanced
Imaging and Java Image IO section – both of these libraries are used by GeoTools.

#. Download the latest JDK from the the java.sun.com website:

   http://java.sun.com/javase/downloads/index.jsp
   
#. At the time of writing the latest JDK was:
   
   jdk-6u20-windows-i586.exe
   
#. Click through the installer you will need to set an acceptance a license agreement and so forth.
   By default this will install to:     
   
   C:\\Program Files\\Java\\jdk1.6.0_20\\
     
#. Optional: Java Advanced Imaging is used by GeoTools for raster support. If you install JAI 1.1.3 
   performance will be improved:   
   
   http://download.java.net/media/jai/builds/release/
      
   Both a JDK and JRE installer are available:
   
   * jai-1_1_3-lib-windows-i586-jdk.exe
   * jai-1_1_3-lib-windows-i586-jre.exe
     
#. Optional: ImageIO Is used to read and write raster files. GeoTools uses version 1_1 of the
   ImageIO library:
   
   http://download.java.net/media/jai-imageio/builds/
   
   Both a JDK and JRE installer are available:   
   
   * jai_imageio-1_1-lib-windows-i586-jdk.exe 
   * jai_imageio-1_1-lib-windows-i586-jre.exe

Quickstart
==========

The GeoTools development community uses the build tool Maven which is integrated into the latest
releases of NetBeans.

The advantages of using Maven are:

* You only download as much of GeoTools as your application requires Jars are downloaded to a single
  location in your home directory (in a hidden folder called .m2/repository)

* You can work on several open source projects at once; the build process will
  "install" the jars into the local maven repository allowing you to share
  between local work and community projects seemlessly 

Although Maven is a build tool it works by describing the contents of a project. This is a different
approach then used by the Make or Ant tools which list the steps required to build.

Part of the description of a project is the required jars and a repository on the internet where
they can be downloaded from. We will be using these facilities to bring GeoTools jars as needed
into our project.

Maven Command Line
-------------------

The maven build tool works directly on the command line.

1. Download Maven from http://maven.apache.org/download.html 
   
   The last version we tested with was: Maven 2.2.1
   
2. Unzip the file apache-maven-2.2.1-bin.zip to C:\java\apache-maven-2.2.1
3. You need to have a couple of environmental variables set for maven to work. Use
   :menuselection:`Control Panel --> System --> Advanced --> Environmental Variables` to set the following.

   * JAVA_HOME = :file:`C:\Program Files\Java\jdk1.6.0_16`
   * M2_HOME = :file:`C:\java\apache-maven-2.2.1`
   * PATH = :file:`%JAVA_HOME%\bin;%M2_HOME%\bin`

   .. image:: images/env-variables.jpg
      :scale: 60
   
4. Open up a commands prompt :menuselection:`Accessories --> Command Prompt`
5. Type the following command to confirm you are set up correctly::
   
      C:java> mvn -version
      
6. This should produce the following output

   .. image:: images/maven-version.png
      :scale: 60
   
7. We can now create our project with::

      C:>cd C:\java
      C:java> mvn archetype:create -DgroupId=org.geotools -DartifactId=tutorial

8. The above command creates a simple proejct including ...
   
   Several soruce directories:
   
   * src/main/java
   * src/main/resources
   * src/test/java
   * src/test/resources
   
   And one target directory with a similar structure.
   
9. During the build progress your local maven repository will be used to store both
   downloaded jars; and those you build locally.
   
   Your local Maven repository is located in your home folder.
    
    ==================  ========================================================
       PLATFORM           LOCAL REPOSITORY
    ==================  ========================================================
       Windows XP:      :file:`C:\\Documents and Settings\\Jody\\.m2\\repository`
       Windows:         :file:`C:\\Users\\Jody\.m2\\repository`
       Linux and Mac:   :file:`~/.m2/repository`
    ==================  ========================================================

10. Open up the pom.xml file in your editor.
11. We are going to start by defining the version number of GeoTools we wish to use. This workbook
    was written for |release| although you may wish to try a newer version, or make use of a
    nightly build by using 8-SNAPSHOT.
    
.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>

12. The following dependencies:

.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>

13. Finally several repositories to download from:
   
.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </dependencies>
        :end-before: </project>

14. You may find it easier to cut and paste into your existing file; or just
    :download:`download pom.xml<artifacts/pom.xml>` directly.
   
    And easy way to pick up typing mistakes with tags is to turn your editor into
    an "xml" mode if it supports such things.
    
15. Return to the command line and maven to download the required jars and build your project::
    
      C:\java\example> mvn install
    
Tips:

* If maven has trouble downloading any jar; you can try again by selecting
  :menuselection:`Project --> Update All Maven Dependencies`.
    
  If it really cannot connect you will need to switch to 8-SNAPSHOT and add the following
  snap shot repository.
    
  .. literalinclude:: artifacts/pom2.xml
     :language: xml
     :start-after: </dependencies>
     :end-before: </project>
   
Quickstart Application
----------------------

Now that your environment is setup we can put together a simple Quickstart. This example will
display a shapefile on screen.

1. Create the org.geotools.tutorial.Quickstart class using your text editor.
   
2. Fill in the following code:
  
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/Quickstart.java
        :language: java
        
3. We need to download some sample data to work with. The http://www.naturalearthdata.com/ project
   is a great project supported by the North American Cartographic Information Society.
   
   * `50m-cultural.zip <http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/50m/cultural/50m-cultural.zip>`_ 

   Please unzip the above data into a location you can find easily such as the desktop.

4. We can run the applicaiton using maven on the command line:
   
   mvn exec:java -Dexec.mainClass="org.geotools.tutorial.quickstart.Quickstart"
   
5. The application will connect to your shapefile, produce a map context, and display the shapefile.

   .. image:: images/QuickstartMap.png
      :scale: 60
   
6. A couple of things to note about the code example:
   
* The shapefile is not loaded into memory. Instead it is read from disk each and every time it is needed
  This approach allows you to work with data sets larger than available memory.
      
* We are using a very basic display style here that just shows feature outlines. In the examples
  that follow we will see how to specify more sophisticated styles.

Things to Try
=============

Each tutorial consists of very detailed steps followed by a series of extra questions. If you get
stuck at any point please ask your instructor; or sign up to the geotools-users email list.

Here are some additional challenges for you to try:

* Try out the different sample data sets

* You can zoom in, zoom out and show the full extents and Use the select tool to examine individual
  countries in the sample countries.shp file

* Download the largest shapefile you can find and see how quickly it can be rendered. You should
  find that the very first time it will take a while as a spatial index is generated. After that
  performance should be very good when zoomed in.
  
* Fast: We know that one of the ways people select a spatial library is based on speed. By design
  GeoTools does not load the above shapefile into memory (instead it streams it off of disk
  each time it is drawn using a spatial index to only bring the content required for display).
  
  If you would like to ask GeoTools to cache the shapefile in memory try the following code:

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartCache.java
     :language: java
     :start-after: // docs start cache
     :end-before:  // docs end cache
  
  For the above example to compile hit :kbd:`Control-Shift-O` to organise imports; it will pull
  in the following import:
    
  .. code-block:: java

     import org.geotools.data.CachingFeatureSource;

..  The ability to grab figure out what classes to import is a key skill; we are
    starting off here with a simple example with a single import.
  
* Try and sort out what all the different “side car” files are – and what they are for. The sample
  data set includes “shp”, “dbf” and “shx”. How many other side car files are there?

.. This exercise asks users to locate the geotools user guide or wikipedia
  
* Advanced: The use of FileDataStoreFinder allows us to work easily with files. The other way to do
  things is with a map of connection parameters. This techniques gives us a little more control over
  how we work with a shapefile and also allows us to connect to databases and web feature servers.

.. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartNotes.java
   :language: java
   :start-after: // start datastore
   :end-before:  // end datastore
     

* Important: GeoTools is an active open source project – you can quickly use maven to try out the
  latest nightly build by changing your pom.xml file to use a “SNAPSHOT” release.
  
  At the time of writing |version|-SNAPSHOT under active development.

  .. literalinclude:: artifacts/pom2.xml
   :language: xml
   :start-after: <url>http://maven.apache.org</url>
   :end-before: <dependencies>

  You will also need to change your pom.xml file to include the following snapshot repository:

.. literalinclude:: artifacts/pom2.xml
   :language: xml
   :start-after: </dependencies>
   :end-before: </project>

* So what jars did maven actually use for the Quickstart application? Try the following on the
  command line:
  
  mvn dependency:tree
  
  We will be making use of some of the project in greater depth in the remaining tutorials.
