:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

**********************
 Netbeans Quickstart 
**********************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>
   
Welcome NetBeans Developers
===========================

Welcome to Geospatial for Java. This workbook is aimed at Java developers who are new to geospatial
and would like to get started.

We are going to start out carefully with the steps needed to set up your Netbeans IDE.
This workbook is also available for Eclipse or Maven command line use.  The build tool Maven
(http://maven.apache.org/) is our preferred option for downloading and managing jars. GeoTools
projects tend to use a large number of jars and an automated solution is preferable.

If you are already familiar with Maven that is an advantage but if not, don't worry, we will be
explaining things step by step and we will also document how to set up things by hand as an
alternative to using Maven.

These are visual tutorials that allows you to see what you are working with while you learn.
These examples make use of Swing; be assured that this is only to make the examples easy and
fun to use. These sessions are applicable to both server side and client side development.

We would like thank members of the GeoTools users list for their feedback while were preparing the
course material, with special thanks to Tom Williamson for reviewing early drafts.

Java Install
============

We are going to be making use of Java so if you don't have a Java Development Kit installed now is
the time to do so. Even if you have Java installed already check out the optional Java Advanced
Imaging and Java Image IO section – both of these libraries are used by GeoTools.

#. Download the latest JDK from the java.sun.com website:

   http://java.sun.com/javase/downloads/index.jsp
   
#. At the time of writing the latest JDK was:
   
   ``jdk-8u91-windows-i586.exe``
   
#. Click through the installer you will need to set an acceptance a license agreement and so forth.
   By default this will install to:     
   
   ``C:\\Program Files\\Java\\jdk1.8.0_91\\``
     
#. **Optional**: Java Advanced Imaging is used by GeoTools for raster support. If you install JAI 1.1.3 
   performance will be improved:   
   
   http://www.oracle.com/technetwork/java/javase/tech/jai-142803.html
   
   Both a JDK and JRE installer are available:
   
   * ``jai-1_1_3-lib-windows-i586-jdk.exe``
   * ``jai-1_1_3-lib-windows-i586-jre.exe``
     
#. **Optional**: ImageIO Is used to read and write raster files. GeoTools uses version 1_1 of the
   ImageIO library:
   
  https://docs.oracle.com/javase/6/docs/technotes/guides/imageio/index.html
   
   Both a JDK and JRE installer are available:   
   
   * ``jai_imageio-1_1-lib-windows-i586-jdk.exe``
   * ``jai_imageio-1_1-lib-windows-i586-jre.exe``
   
For more details of how to install these packages see `this page <http://docs.geoserver.org/latest/en/user/production/java.html#install-native-jai-and-imageio-extensions>`_
   
NetBeans Install
================

The NetBeans IDE is a popular choice for Java development and features excellent Maven integration.

#. Download NetBeans (The Java SE download will be fine).

     http://www.netbeans.org/ 

#. At the time of ``netbeans-7.0.1-ml-javase-windows.exe`` was the latest installer.

#. Click through the steps of the installer. You will notice it will pick up on the JDK you
   installed earlier.

   .. image:: images/netbeansInstall.png
      :scale: 60
   
Quickstart
==========

The GeoTools development community uses the build tool Maven which is integrated into the latest
releases of NetBeans.

The advantages of using Maven are:

* You only download as much of GeoTools as your application requires
* Jars are downloaded to a single location in your home directory
  (in a hidden folder called ``.m2/repository``)
* Source code and javadocs are automatically downloaded and hooked up

Although Maven is a build tool it works by describing the contents of a project. This is a different
approach then used by the Make or Ant tools which list the steps required to build.

The description of a project includes the required jars (called dependencies) and a repository
on the internet where the jars can be downloaded. We will be using these facilities to bring
GeoTools jars into our project as needed.

Creating the Project
--------------------

Let's get started:

1. Start with :menuselection:`File --> New Project` to open the **New Project** wizard
2. Select the Maven category; choose Maven Project and press **Next**.

   .. image:: images/nbNewProject.png
      :scale: 60
      
3. On the Maven Archetype page select "Maven Quickstart Archetype" and press Next.

   .. image:: images/nbNewProjectArchetype.png
      :scale: 60

4. We can now fill in the blanks

   * Project name: ``tutorial``
   * GroupId: ``org.geotools``

   .. image:: images/nbNameAndLocation.png
      :Scale: 60

5. Click on the Finish button and the new project will be created.

6. If this is your first time using Maven with NetBeans it will want to confirm that it is okay to
   use the copy of Maven included with NetBeans (it is also possible to use an external Maven
   executable from within Netbeans which is convenient if, for instance, you want to work with the
   same version of Maven within the IDE and from the command line).

Adding Jars to Your Project
---------------------------

.. sidebar:: Lab

   Your local maven repository has already been
   populated with GeoTools allowing the use of "offline" mode.
   
   #. Open :menuselection:`Windows --> Preferences`
   #. Select :guilabel:`Maven` preference page
   #. Ensure :guilabel:`offline` is checked

The ``pom.xml`` file is used to describe the care and feeding of your maven project; we are going to
focus on the dependencies needed for your project 

When downloading jars maven makes use of a "local repository" to store jars.

  ==================  ========================================================
     PLATFORM           LOCAL REPOSITORY
  ==================  ========================================================
     Windows XP:      :file:`C:\\Documents and Settings\\You\\.m2\\repository`
     Windows:         :file:`C:\\Users\\You\\.m2\\repository`
     Linux and Mac:   :file:`~/.m2/repository`
  ==================  ========================================================

When downloading jars maven makes use of public maven repositories on the internet where projects
such as GeoTools publish their work.

1. The next step is for us to make it a GeoTools project by adding information to Maven's project
   description file ("project object model" in Maven-speak) - ``pom.xml``
   
   In the Projects panel open up the Project Files folder and double click on ``pom.xml`` to open it.
   
2. We are going to start by defining the version number of GeoTools we wish to use. This workbook
   was written for |release| although you may wish to try a different version.
   
   For production a stable release of |branch| should be used for `geotools.version`:
    
   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>
   
   To make use of a nightly build set the `geotools.version` property to |branch|-SNAPSHOT .
    
   .. literalinclude:: artifacts/pom2.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>
  
   If you make any mistakes when editing the xml file you'll see that your project will be renamed
   "<Badly formed Maven project>" in the Projects window. You can choose "Format" as a quick way to
   check if the tags line up. Or just hit undo and try again. 
  
3. Next we add two GeoTools modules to the dependencies section:
   ``gt-shapefile`` and ``gt-swing``.

   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>
  
4. And the repositories where these jars can be downloaded from:

   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </dependencies>
        :end-before: <build>

   .. note:: Note the snapshot repository above is only required if you are using a nightly build (such as |branch|-SNAPSHOT)

5. If you'd like to use Java 8 language level features (e.g. lambdas), you need to tell Maven to use the 1.8 source level

   .. literalinclude:: artifacts/pom2.xml
      :language: xml
      :start-after: </repositories>
      :end-before: </project>
    
6. You can now right click on Libraries in the Projects window, then Download missing Dependencies
   from the pop-up menu. When downloading it will check the repositories we have listed
   above.

7. We will continue to add dependencies on different parts of the GeoTools library as we work through these exercises; this fine grain control and the ability to download exactly what is needed is one of the advantages of using Maven.

8. Here is what the completed :file:`pom.xml` looks like:

   .. literalinclude:: artifacts/pom.xml
        :language: xml
   
   * You may find cutting and pasting from the documentation to be easier then typing.
   
   * You may also :download:`download this file <artifacts/pom.xml>`

Quickstart Application
-----------------------

Now that your environment is setup we can put together a simple Quickstart. This example will display a shapefile on screen.

#. Create the package ``org.geotools.tutorial.quickstart.``

#. Create the ``org.geotools.tutorial.quickstart.Quickstart`` class using your IDE.
   
#. Fill in the following code:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/Quickstart.java
        :language: java

#. Build the application and check that all is well in the Output window.

   .. image:: images/nbQuickstart.png
      :Scale: 60
   
   A fair bit of time will be spent downloading the libraries required.

Running the Application
------------------------

#. We need to download some sample data to work with. The http://www.naturalearthdata.com/ project
   is a great project supported by the North American Cartographic Information Society. Head to the link below and download some cultural vectors. You can use the 'Download all 50m cultural themes' at top.

   * `1:50m Cultural Vectors <http://www.naturalearthdata.com/downloads/50m-cultural-vectors/>`_

   Please unzip the above data into a location you can find easily such as the desktop.

#. Run the application to open a file chooser. Choose a shapefile from the example data set.

   .. image:: images/QuickstartOpen.jpg
      :scale: 60
      
#. The application will connect to your shapefile, 1.produce a map context and display the shapefile.

   .. image:: images/QuickstartMap.jpg
      :scale: 60
      
#. A couple of things to note about the code example:
   
   * The shapefile is not loaded into memory – instead it is read from disk each and every time it is needed
     This approach allows you to work with data sets larger then available memory.
   
   * We are using a very basic display style here that just shows feature outlines. In the examples that follow we will see how to specify more sophisticated styles.

   
Things to Try
=============

Each tutorial consists of very detailed steps followed by a series of extra questions. If you get
stuck at any point please ask your instructor; or sign up to the geotools-users_ email list.

.. _geotools-users: http://docs.geotools.org/latest/developer/communication.html

Here are some additional challenges for you to try:

* Try out the different sample data sets

* You can zoom in, zoom out and show the full extents and Use the select tool to examine individual
  countries in the sample ``countries.shp`` file

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
  

  For the above example to compile you will need to add the necessary imports.

     
* Try and sort out what all the different "side car" files are – and what they are for. The sample
  data set includes ``shp``, ``dbf`` and ``shx``. How many other side car files are there?

  .. This exercise asks users to locate the geotools user guide or wikipedia
  
* Advanced: The use of ``FileDataStoreFinder`` allows us to work easily with files. The other way to do
  things is with a map of connection parameters. This techniques gives us a little more control over
  how we work with a shapefile and also allows us to connect to databases and web feature servers.

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartNotes.java
     :language: java
     :start-after: // start datastore
     :end-before:  // end datastore
     

* Important: GeoTools is an active open source project – you can quickly use maven to try out the
  latest nightly build by changing your ``pom.xml`` file to use a "SNAPSHOT" release.
  
  At the time of writing |branch|-SNAPSHOT is under active development.
  
  .. literalinclude:: artifacts/pom2.xml
     :language: xml
     :start-after: <url>http://maven.apache.org</url>
     :end-before: <dependencies>
    
  You will also need to change your ``pom.xml`` file to include the following snapshot repository:
  
  .. literalinclude:: artifacts/pom2.xml
     :language: xml
     :start-after: </dependencies>
     :end-before: </project>
        
* NetBeans has an interesting feature to show how the dependency system works - Right click on
  Libraries and choose Show Dependency
  
  .. image:: images/nbGraph.png
   
  We will be making use of some of the project is greater depth in the remaining tutorials.

Maven Alternative
=================

The alternative to using Maven to download and manage jars for you is to manually install them.
To start with we will obtain GeoTools from the website:

1. Download the GeoTools binary release from http://sourceforge.net/projects/geotools/files 
2. Extract the ``geotools-2.6.0-bin.zip`` file to :file:`C:\\java\\geotools-2.6.0` folder.
3. If you open up the folder and have a look you will see GeoTools and all of the other jars that
   it uses including those from other libraries such as GeoAPI and JTS.

   .. image:: images/gtunzipped.jpg

4. We can now set up GeoTools as a library in NetBeans:

   From the menu bar choose Tools > Libraries to open the Library Manager.
   
5. From the Library Manager press the New Library button.

6. Enter "GeoTools" for the Library Name and press OK

7. You can now press the Add JAR/Folder button and add in all the jars from C:\\java\\GeoTools-|release|
   
8. GeoTools includes a copy of the "EPSG" map projections database; but also allows you to hook up
   your own copy of the EPSG database as an option. However, only one copy can be used at a time
   so we will need to remove the following jars from the Library Manager:
   
.. sidebar:: EPSG

   The EPSG database is distributed as an Access database and has been converted into the pure java
   database HSQL for our use.
   
   * ``gt-epsg-h2``
   * ``gt-epsg-oracle``
   * ``gt-epsg-postgresql``
   * ``gt-epsg-wkt-2.6``

9. GeoTools allows you to work with many different databases; however to make them work you will
   need to download jdbc drivers from the manufacturer.

   For now remove the following plugins from the Library Manager:

   * ``gt-db2``
   * ``gt-jdbc-db2``
   * ``gt-oracle-spatial``
   * ``gt-jdbc-oracle``

10. We are now ready to proceed with creating an example project. Select :menuselection:`File > New Project`

11. Choose the default "Java Application"

12. Fill in "Tutorial" as the project name; and our initial Main class will be called "Quickstart".

13. Open up Example in the Projects window, right click on Libraries and select Add Libraries.
    Choose GeoTools from the Add Library dialog.
   
14. Congratulations ! You can now return to Quickstart or any of the other tutorials
