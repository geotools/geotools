:Author: Jody Garnett
:Author: Micheal Bedward
:Author: Ian Turton
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

**********************
  Eclipse Quickstart 
**********************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>

Welcome Eclipse Developers
==========================

Welcome to Geospatial for Java. This workbook is aimed at Java developers who
are new to geospatial and would like to get started.

We are going to start out carefully with the steps needed to set up your Eclipse IDE. This workbook
is also available for NetBeans or Maven command line use. If you are comfortable with the build tool
Maven, it is our preferred option for downloading and managing jars but we will also document how
to set up things by hand.

These are visual tutorials that allows you to see what you are working with while you learn.
These examples make use of Swing; be assured that this is only to make the examples easy and
fun to use. These sessions are applicable to both server side and client side development.

Java Install
============

.. sidebar:: Lab

   If you are following this workbook in a lab setting you will find installers on the DVD.
   
We are going to be making use of Java, so if you don't have a Java Development Kit installed now is
the time to do so. Even if you have Java installed already check out the optional Java Advanced
Imaging and Java Image IO section.
   
#. Download the latest Java Development Kit (JDK) from the Oracle website:

   http://www.oracle.com/technetwork/java/javase/downloads/
   
#. At the time of writing the latest was JDK 8. Choose a download for your platform, for example:
   
   `jdk-8u66-windows-i586.exe`
   
   For Windows 32bit platforms.
   
#. Click through the installer. You will need to accept a license agreement, choose a directory
   and so forth.
   
   By default this will install to:     
   
   ``C:\\Program Files\\Java\\jdk1.8.0\\``
     
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

Eclipse
=======

.. sidebar:: Lab

   In a lab setting your instructor will have downloaded these files for you, and often have a ready
   to go Eclipse zipped up and ready to use.
   
Eclipse is a popular integrated development environment most often used for all kinds of Java
development. For this tutorial we are doing straight up Java programming using the smallest
download available - if you already have an Eclipse download please go ahead and use it and
switch to the "Java Perspective".
   
1. Visit the Eclipse download page (http://www.eclipse.org/downloads/eclipse-packages/) and download "Eclipse IDE for Java developers".
2. Eclipse now provides an installer; however this tutorial targets the binary packages that 
   you simply extract and run.
3. To start out with create the folder C:\\java to keep all our java development in one spot.
4. Unzip the downloaded ``eclipse-java-mars-R-win32.zip`` file to your ``C:\\java`` directory - the
   folder ``C:\\java\\eclipse`` will be created.
5. Navigate to ``C:\\java\\eclipse`` and right-click on the ``eclipse.exe`` file and select
   Send To -> Desktop (create shortcut).
6. Open up the ``eclipse.ini`` file.
   
   * Use our JDK directly by providing a ``-vm`` argument
   
7. Double click on your desktop shortcut to start up eclipse.
8. When you start up eclipse for the first time it will prompt you for a workspace. To keep our
   java work in one spot you can type in:
   
   ``C:\\java\\workspace``
   
9. On the Welcome view press Workbench along the right hand side and we can get started

.. _eclipse-m2eclipse:

M2E
-----
  
Maven is a build system for Java which is very good at managing dependencies. The GeoTools library
is plugin based and you get to pick and choose what features you need for your application. While
this is useful when determining just what is needed for delivery - it can be a pain to manage by
hand so we encourage the use of a tool such as maven.

In previous years we used the command line (gasp!) when working with maven. This year we are going
to be using the M2E plugin from Sonyatype.

The M2E plugin is included by default since Eclipse 3.7.

Quickstart
==========

For this Quickstart we are going to produce a simple maven project, hook it up to GeoTools, and
then display a shapefile.

This tutorial is really focused on your development environment and making sure you have GeoTools
ready to go. We will cover what a shapefile is and how the map is displayed shortly.

Creating a Simple Maven project
-------------------------------

Maven works by asking you to describe your project, the name, the version number, where the source
code is, how you want it packaged, and what libraries it makes use of. Based on the description it
can figure out most things: how to compile your code, creating javadocs, or even downloading the
library jars for you.

To use M2E plugin to create a create a new maven project:

#. File > New > Other from the menu bar

#. Select the wizard *Maven > Maven Project* and press *Next* to open the *New Maven Project* wizard

#. The *New Maven project* page defaults are fine, press *Next*

   .. image:: images/newmaven.png
      :scale: 60
   
#. The default of *maven-archtype-quickstart* is fine, press *Next*
 
   .. image:: images/archetype.png
      :scale: 60

#. The archetype acts a template using the parameters we supply to create the project.
   
   * Group Id: ``org.geotools``
   * Artifact Id: ``tutorial``
   * Version: ``0.0.1-SNAPSHOT`` (default)
   * Package: ``org.geotools.tutorial``
   
   .. image:: images/artifact.png
      :scale: 60   
      
#. Press *Finish* to create the new project.
#. You can see that an application has been created; complete with ``App.java`` and a JUnit test case
#. Open up ``src/main/java`` and select ``org.geotools.tutorial.App`` and press the *Run* button in the
   toolbar::
   
     Hello World!

#. You may also open up ``src/main/test`` and run ``org.geotools.tutorial.AppTest`` as a **JUnit Test**.	 
	 
   
Adding Jars to your Project
---------------------------

.. sidebar:: Lab

   We are going to cheat in order to save time; the local maven repository has already been
   populated with the latest copy of GeoTools allowing us to use offline mode.
   
   To turn on offline mode:
   
   #. Open :menuselection:`Windows --> Preferences`
   #. Select :guilabel:`Maven` preference page
   #. Ensure :guilabel:`offline` is checked
    
   This setting is useful when wanting to work quickly once everything is downloaded.
    
The ``pom.xml`` file is used to describe the care and feeding of your maven project; we are going to
focus on the dependencies needed for your project 

When downloading jars maven makes use of a "local repository" to store jars.

  ==================  ========================================================
     PLATFORM           LOCAL REPOSITORY
  ==================  ========================================================
     Windows XP:      :file:`C:\\Documents and Settings\\You\\.m2\\repository`
     Windows:         :file:`C:\\Users\\You\\.m2\repository`
     Linux and Mac:   :file:`~/.m2/repository`
  ==================  ========================================================

To download jars maven makes use of public maven repositories on the internet where projects
such as GeoTools publish their work.

1. Open up :file:`pom.xml` in your new project. You can see some of the information we entered
   earlier.
   
   .. image:: images/pomOverview.jpg
      :scale: 60
 
2. This editor allows you to describe all kinds of things; in the interest of time we are going to
   skip the long drawn out explanation and ask you to click on the :guilabel:`pom.xml` tab.

3. To make use of GeoTools we are going to add three things to this ``pom.xml`` file.
   
4. At the top after ``moduleVersion`` add a ``properties`` element defining the version of GeoTools we
   want to use. This workbook was written for |release| although you may wish to try a different
   version.
   
   For production a stable release of |branch| should be used for `geotools.version`:
    
   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>
   
   To make use of a nightly build set the ``geotools.version`` property to |branch|-SNAPSHOT .
    
   .. literalinclude:: artifacts/pom2.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>
        
5. We are going to add a dependence to GeoTools :file:`gt-main` and :file:`gt-swing` jars. Note use of `geotools.version` defined above.
   
   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>
    
6. Finally we need to list the external *repositories* where maven can download GeoTools and 
   other required jars from.

   .. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </dependencies>
        :end-before: <build>
   
   .. note:: Note the snapshot repository above is only required if you are using a nightly build (such as |branch|-SNAPSHOT)

7. GeoTools now requires Java 8 language level features (e.g. lambdas) - you need to tell Maven to use the 1.8 source level.

   .. literalinclude:: artifacts/pom2.xml
      :language: xml
      :start-after: </repositories>
      :end-before: </project>

8. For comparison here is the completed :download:`pom.xml <artifacts/pom.xml>` file for download.

   You may find cutting and pasting to be easier than typing, you can choose Source --> Format to
   fix indentation

Tips:

* If maven has trouble downloading any jar; you can try again by selecting
  :menuselection:`Project --> Update All Maven Dependencies`.
   
* If the dependencies do not update automatically
  use :menuselection:`Project --> Clean`
   
Quickstart Application
----------------------

Now that your environment is setup we can put together a simple Quickstart. This example will display a shapefile on screen.

#. Create the package ``org.geotools.tutorial.quickstart`` using your IDE.

#. Create the ``org.geotools.tutorial.quickstart.Quickstart`` class using your IDE.
   
   .. image:: images/class.png
      :scale: 60
   
#. Fill in the following code:
  
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/Quickstart.java
        :language: java
        
#. We need to download some sample data to work with. The http://www.naturalearthdata.com/ project
   is a great project supported by the North American Cartographic Information Society.  Head to the link below and download some cultural vectors. You can use the 'Download all 50m cultural themes' at top.

   * `1:50m Cultural Vectors <http://www.naturalearthdata.com/downloads/50m-cultural-vectors/>`_

   Please unzip the above data into a location you can find easily such as the desktop.

#. Run the application to open a file chooser. Choose a shapefile from the example data set.

   .. image:: images/QuickstartOpen.png
      :scale: 60
   
#. The application will connect to your shapefile, produce a map content, and display the shapefile.

   .. image:: images/QuickstartMap.png
      :scale: 60
   
#. A couple of things to note about the code example:
   
* The shapefile is not loaded into memory - instead it is read from disk each and every time it is needed
  This approach allows you to work with data sets larger than available memory.
      
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
  
* Performance: We know that one of the ways people select a spatial library is based on speed.
  By design GeoTools does not load the above shapefile into memory (instead it streams it off
  of disk each time it is drawn using a spatial index to only bring the content required for
  display).
  
  If you would like to ask GeoTools to cache the shapefile in memory try the following code:

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartCache.java
     :language: java
     :start-after: // docs start cache
     :end-before:  // docs end cache
  

  For the above example to compile hit :kbd:`Control-Shift-O` to organise imports.


..  The ability to grab figure out what classes to import is a key skill; we are
    starting off here with a simple example with a single import.
  
* Try and sort out what all the different "side car" files are - and what they are for. The sample
  data set includes ``shp``, ``dbf`` and ``shx``. How many other side car files are there?

.. This exercise asks users to locate the geotools user guide or wikipedia
  
* Advanced: The use of ``FileDataStoreFinder`` allows us to work easily with files. The other way to do
  things is with a map of connection parameters. This techniques gives us a little more control over
  how we work with a shapefile and also allows us to connect to databases and web feature servers.

.. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartNotes.java
   :language: java
   :start-after: // start datastore
   :end-before:  // end datastore
     

* Important: GeoTools is an active open source project - you can quickly use maven to try out the
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

* So what jars did maven actually use for the Quickstart application? Open up your :file:`pom.xml`
  and switch to the :guilabel:`dependency hierarchy` or :guilabel:`dependency graph` tabs to see
  what is going on.
  
  .. image:: images/quickstart-dependency.png
     :scale: 60
  
  We will be making use of some of the project in greater depth in the remaining tutorials.
  
Alternatives to M2Eclipse
=========================

There are two alternatives to the use of the M2Eclipse plugin; you may find these better suit the
needs of your organization.

* :ref:`eclipse-mvn-start`
* :ref:`eclipse-download-start`

.. _eclipse-mvn-start:

Maven Plugin
------------

The first alternative to putting maven into eclipse is to put eclipse into maven.

The maven build tool also works directly on the command line; and includes a plugin for
generating eclipse :file:`.project` and :file:`.classpath` files.

1. Download Maven from http://maven.apache.org/download.html 
   
   The last version we tested with was: Maven 3.3.3
   
2. Unzip the file ``apache-maven-3.3.3-bin.zip`` to ``C:\java\apache-maven-3.3.3``
3. You need to have a couple of environmental variables set for maven to work. Use
   :menuselection:`Control Panel --> System --> Advanced --> Environmental Variables` to set the following System Variables:

   Add the following system variables:
   
   * ``JAVA_HOME = :file:`C:\\Program Files (x86)\\Java\\jdk1.8.0_66```
   * ``M2_HOME = :file:`C:\\java\\apache-maven-3.2.3```
   
   And add the following to your ``PATH``:
   
   * ``PATH = :file:`%JAVA_HOME%\\bin;%M2_HOME%\\bin```
   
   .. image:: images/env-variables.png
      :scale: 60
   
4. Open up a commands prompt :menuselection:`Accessories --> Command Prompt`
5. Type the following command to confirm you are set up correctly::
   
      C:\\java> mvn -version
   
   If the `mvn` command is not found check your path using::
      
      C:\\java> echo %PATH%
   
6. This should produce the following output

   .. image:: images/maven-version.png
      :scale: 60
   
7. We can now generate our project from ``maven-archetype-quickstart`` with::

      C:>cd C:\java
      C:java>  mvn archetype:generate -DgroupId=org.geotools -DartifactId=tutorial -Dversion=1.0-SNAPSHOT -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart

8. And ask for our project to be set up for eclipse::

      C:java> cd tutorial
      C:java\tutorial> mvn eclipse:eclipse

9. You can now give Eclipse the background information it needs to talk to your "maven repository"
   (maven downloaded something like 30 jars for you)
10. Return to Eclipse
11. Use :menuselection:`Windows --> Preferences` to open the Preference Dialog. 
    Using the tree on the left navigate to the Java > Build path > Classpath Variables preference
    Page.
   
   .. image:: images/classpath-variables.png
      :scale: 60
   
12. Add an ``M2_REPO`` classpath variable pointing to your "local repository" 

    ==================  ========================================================
       PLATFORM           LOCAL REPOSITORY
    ==================  ========================================================
       Windows XP:      :file:`C:\\Documents and Settings\\Jody\\.m2\\repository`
       Windows:         :file:`C:\\Users\\Jody\.m2\\repository`
       Linux and Mac:   :file:`~/.m2/repository`
    ==================  ========================================================

13. We can now import your new project into eclipse using :menuselection:`File --> Import`
14. Choose *Existing Projects into Workspace* from the list, and press :guilabel:`Next`

    .. image:: images/import-existing.png
       :scale: 60

15. Select the project you created: :file:`C:\java\tutorial`
16. Press :guilabel:`Finish` to import your project
17. Navigate to the ``pom.xml`` file and double click to open it up.
18. We are going to start by defining the version number of GeoTools we wish to use. This workbook
    was written for |release| although you may wish to try a newer version, or make use of a
    nightly build by using something like ``15-SNAPSHOT``.
    
.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: <url>http://maven.apache.org</url>
        :end-before: <dependencies>


19. The following dependencies:


.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>


20. Finally several repositories to download from:

   
.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </dependencies>
        :end-before: </project>

21. You may find it easier to cut and paste into your existing file; or just
    :download:`download pom.xml<artifacts/pom.xml>` directly.
   
    An easy way to pick up typing mistakes with tags is to Eclipse to format the XML file.
   
22. Return to the command line and maven to download the required jars and tell eclipse about it::
    
     C:\java\example> mvn eclipse:eclipse
    
23. Return to eclipse and select the project folder. Refresh your project using the context menu
    or by pressing :kbd:`F5`. If you open up referenced libraries you will see the required jars 
    listed.
   
    .. image:: images/maven-refresh.png
       :scale: 60

24. Using this technique of running ``mvn eclipse:eclipse`` and refreshing in eclipse you can proceed
    through all the tutorial examples.
   
.. _eclipse-download-start:

Download GeoTools
-----------------

.. sidebar:: Caution

    This procedure is tricky and can often lead to problems with missing jars or too many jars. GeoTools is really too large and complex to consider building without the use of maven. Please reconsider before proceeding with this process.

We can also download the GeoTools project bundle from source forge and set up our project to use
them. Please follow these steps carefully as not all the GeoTools jars can be used at the same
time.

1. Download the GeoTools binary release from http://sourceforge.net/projects/geotools/files 

2. We are now going to make a project for the required jars. By placing the jars into their own project is is easier to upgrade GeoTools.

   Select File > New > Java Project to open the New Java Project wizard

3. Type in "GeoTools Download" as the name of the project and press Finish.

4. Choose File > Import to open the Import Wizard.

5. Select General > Archive File and press Next

6. Navigate to the ``geotools-bin.zip`` download and import the contents into your project.

7. GeoTools includes a copy of the "EPSG" database; but also allows you to hook up your own copy of the EPSG database as an option..

   However only one copy can be used at a time so we will need to remove the following jars from the Library Manager:

   * ``gt-epsg-hsql``
   * ``gt-epsg-oracle``
   * ``gt-epsg-postgresql``
   * ``gt-epsg-wkt``
      
8. GeoTools allows you to work with many different databases; however to make them work you will need to download JDBC drivers from the manufacturer.

   For now remove the follow plugins from your Library Manager definition:

   * ``gt-jdbc-db2``
   * ``gt-jdbc-oracle``

9. Next we update our Java build path to include the remaining jars. Choose Project > Properties from 
   the menu bar

10. Select Java Build Path property page; and switch to the library tab.

11. Press ``Add JAR``\ s button and add all the jars

12. Switch to the Order and Export tab and press Select All

13. We can now create a new Example project to get going on our Example.

14. Use Project > Properties on your new Example project to open up the Java Build Path page.

15. Switch to the Projects tab and use the ``Add..`` button to add GeoTools Downloads to the build path.

16. Our example project can now use all the GeoTools jars.

17. Please proceed to the Quickstart.
