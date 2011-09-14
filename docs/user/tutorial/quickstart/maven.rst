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
Don't worry if you're not familiar with Maven because we will explain everythign step by step.

The example application is the same one used for the NetBeans and Eclipse Quickstart tutorials: a
simple program to load and display a shapefile.

We would like thank members of the `GeoTools User mailing list
<https://lists.sourceforge.net/lists/listinfo/geotools-gt2-users>`_ for their feedback while we were
preparing the course material, with special thanks to Tom Williamson for reviewing early drafts. If
you have any questions or comments about this tutorial, please post them to the user list.

Java Install
============

We are going to be making use of Java so if you don't have a Java Development Kit (JDK) installed now is
the time to do so. 

#. Download the latest JDK from the the java.sun.com website:

   http://java.sun.com/javase/downloads/index.jsp
   
#. At the time of writing the latest JDK was:
   
   jdk-6u20-windows-i586.exe
   
#. Click through the installer you will need to set an acceptance a license agreement and so forth.
   By default this will install to:     
   
   C:\\Program Files\\Java\\jdk1.6.0_20\\
     
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
 
* Jars are downloaded into a single location in your home directory (e.g. C:\\Documents and
  Settings\<user\>\\.m2\\ on Windows). This is your *local repository*.

* The correct versions of all of the third-party jars required by GeoTools will be downloaded for
  you. This helps you to avoid obscure errors than can be caused by mis-matched dependencies which
  can be very difficult to track down.

* The single *local repository* makes it easier to work on other multiple open source projects.


Installing Maven
----------------

#. Download Maven from http://maven.apache.org/download.html 
   
   In this tutorial we refer to Maven version 2.2.1 which is the one used by many GeoTools
   developers and users, at the time of writing. You can also use Maven version 3 but we don't have
   as much experience using that version with GeoTools yet.
   
#. Unzip the file apache-maven-2.2.1-bin.zip to C:\java\apache-maven-2.2.1

#. You need to have a couple of environmental variables set for maven to work. Use
   :menuselection:`Control Panel --> System --> Advanced --> Environmental Variables` to set the following.

   * JAVA_HOME = :file:`C:\Program Files\Java\jdk1.6.0_16`
   * M2_HOME = :file:`C:\java\apache-maven-2.2.1`
   * PATH = :file:`%JAVA_HOME%\bin;%M2_HOME%\bin`

   .. image:: images/env-variables.jpg
      :scale: 60
   
#. Open up a commands prompt :menuselection:`Accessories --> Command Prompt`

#. Type the following command to confirm you are set up correctly::
   
      C:java> mvn --version
      
#. This should produce something similar to the following output

   .. image:: images/maven-version.png
      :scale: 60


Creating a new project
----------------------
   
#. We can now create our project with::

      C:>cd C:\java
      C:java> mvn archetype:create -DgroupId=org.geotools -DartifactId=tutorial

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

   App.java and AppTest.java are just placeholder files not used in this tutorial.
   
#. During the build progress your local maven repository will be used to store both
   downloaded jars; and those you build locally.
   
   Your local Maven repository is located in your home folder.
    
    ==================  ========================================================
       PLATFORM           LOCAL REPOSITORY
    ==================  ========================================================
       Windows XP:      :file:`C:\\Documents and Settings\\Jody\\.m2\\repository`
       Windows:         :file:`C:\\Users\\Jody\.m2\\repository`
       Linux and Mac:   :file:`~/.m2/repository`
    ==================  ========================================================

#. Open the **pom.xml** file in your favourite text editor. If your editor has an XML syntax mode
   switch into that now because it will make it a lot easier to find errors such as mis-matched
   brackets. Some editors, such as `vim <http://www.vim.org/>`_, will do this automatically on
   loading the file.

#. We are going to start by defining the version number of GeoTools we wish to use. This workbook
   was written for |release| although you may wish to try a newer version, or make use of a nightly
   build by using 8-SNAPSHOT.
    
   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>

#. We specify the following dependencies (GeoTools modules which your application will need):

   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>

#. Finally, we tell maven which repositories to download jars from:
   
   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </dependencies>
        :end-before: </project>

#. Return to the command line and get maven to download the required jars for your project with this
   command::
    
      C:\java\example> mvn install
    
#. If maven has trouble downloading any jar; you can try again by selecting
   :menuselection:`Project --> Update All Maven Dependencies`.
    
   If it really cannot connect you can switch to edit the `geotools.version` property in your
   pom.xml to 8-SNAPSHOT (GeoTools development version) and then add a reference to the snapshot
   repository as shown below:
    
   .. literalinclude:: artifacts/pom2.xml
        :language: xml
        :start-after: </dependencies>
        :end-before: </project>
   

Creating the Quickstart application
-----------------------------------

Now we are ready to create the application.

#. Go to the directory tutorial\\src\\main\\java\\org\\geotools and create a **tutorial** sub-directory.

#. In the new sub-directory, create a new file **Quickstart.java** using your text editor.

#. Fill in the following code:
  
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/Quickstart.java
        :language: java

#. Go back to the top project directory (the one that contains your pom.xml file) and build the
   application with the command::

     mvn clean install


Running the application
-----------------------
        
#. If you need some shapefiles to work with you will find a selection of data at the
   http://www.naturalearthdata.com/ project which is supported by the North American Cartographic
   Information Society.
   
   * `50m-cultural.zip <http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/50m/cultural/50m-cultural.zip>`_ 

   Unzip the above data into a location you can find easily such as the desktop.

#. You can run the application using Maven on the command line::
   
     mvn exec:java -Dexec.mainClass=org.geotools.tutorial.Quickstart
   
#. The application will connect to your shapefile, produce a map context, and display the shapefile.

   .. image:: images/QuickstartMap.png
      :scale: 60
   
#. A couple of things to note about the code example:
   
* The shapefile is not loaded into memory. Instead it is read from disk each and every time it is needed
  This approach allows you to work with data sets larger than available memory.
      
* We are using a very basic display style here that just shows feature outlines. In the examples
  that follow we will see how to specify more sophisticated styles.


Things to Try
=============

* Try out the different sample data sets.

* You can zoom in, zoom out and show the full extent and use the info tool to examine individual
  countries in the sample countries.shp file.

* Download the largest shapefile you can find and see how quickly it can be rendered. You should
  find that the very first time it will take a while as a spatial index is generated. After that
  rendering will become much faster.
  
* Fast: We know that one of the ways people select a spatial library is based on speed. By design
  GeoTools does not load the above shapefile into memory (instead it streams it off of disk
  each time it is drawn using a spatial index to only bring the content required for display).
  
  If you would like to ask GeoTools to cache the shapefile in memory try the following code:

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartCache.java
     :language: java
     :start-after: // docs start cache
     :end-before:  // docs end cache
  
  You will also need to add this import statement:

  .. code-block:: java

     import org.geotools.data.CachingFeatureSource;


  .. Hint::
     When working in a text editor instead of an IDE use the `GeoTools javadocs 
     <http://docs.geotools.org/latest/javadocs/>`_ to work out what import statements are required
     in your source. The javadocs also list the GeoTools module in which each class is found.

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
