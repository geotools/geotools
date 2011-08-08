Java Install
-------------

GeoTools is written in the Java Programming Language. The library is targeted for Java 6.

Java Runtime Environment:

* Java 7 - currently untested (if you try it email the list)
* Java 6 - Geotools 8.0.x and above
* Java 5 - GeoTools 2.5.x and above
* Java 1.4 - GeoTools versions 2.4.x and below
* GeoTools is known to work with JRE provided by IBM, Apple and OpenJDK.

Java Extension:

* Java Advanced Imaging is used to process rasters. If you have installed the native support into your JRE you can take advantage of hardware acceleration.
* Java Image IO - used to support additional raster formats
* ImageIO-Ext - used to support additional geospatial raster formats

When developing GeoTools please change your compile options to:

* Produce 6.0 compliant code 

Java 2 Standard Edition Software Developers Kit
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Download a JDK
2. Use the one click installer
3. When it asks if you want to install a JRE you can say yes

Use Java 6 starting with geotools 8.0
'''''''''''''''''''''''''''''''''''''

Since the API changes from Java version to version, building a geotools version with a newer Java SDK is risky (you may accidentally use a new method).

GeoTools requires a Java 1.6 SDK for versions 8.0 and above and Java 1.5 SDK for versions 2.5 and above, for older versions of Java please use GeoTools 2.4.

Why JAVA_HOME does not work on Windows
''''''''''''''''''''''''''''''''''''''

How to Build using Java 5 and run using Java 6 on windows.

Several projects expect to make use of a JRE 6 runtime environment (simply for the speed benefit). If your computer is set up with both a JDK 1.5 for building GeoTools; and a JDK 6 for your other projects you will need to sort out how to switch between them.

One technique is to set up a batch file similar to the following:

1. Hunt down the cmd.exe ( Start menu > Accessories > Command Prompt) and right click to send it to the desktop
2. Edit the desktop cmd.exe short cut and change the target to::
      
      %SystemRoot%\system32\cmd.exe /k C:\java\java15.bat

3. Create the C:\java\java15.bat file mentioned above::
   
      set ANT_HOME=C:\java\apache-ant-1.6.5
      set M2_HOME=C:\java\maven-2.0.9
      set JAVA_HOME=C:\Program Files\Java\jdk1.5.0_19
      
      set PATH=%JAVA_HOME%\bin;%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem;C:\Program Files\Subversion\bin;%M2_HOME%\bin;%ANT_HOME%\bin

4. Please note that the construction of the PATH above is very important; ``JAVA_HOME\bin`` must
   appear before ``SystemRoot\system32`` as the system32 contains a stub java.exe that looks up
   the correct version of java to run in the registry.
   
   .. image:: /images/jdk.png
   
5. You can see in the above screen snap that the
   ``My Computer\HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft > Java Development Kit > CurrentVersion``
   is set to **1.6**.
   
   The **1.6** entry documents the path to the version of java to run.
   
   Placing JAVA_HOME on the path before System32 shortcuts this annoying "feature".

Java Extensions (Optional)
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. note::
   Optional
   
   GeoTools is now able to build with just a normal Java Software Developers Kit.

You should be aware that several of our raster formats are capable of making use of native code (packaged up as Java extensions).

Java Developers Kit:
* Can make use of JAI and ImageIO if they have been installed into your JDK
Java Runtime Environment:
* Can make use of JAI and ImageIO if you have installed them into your JRE

These extensions end up adding:
* some jars into your lib folder
* some dlls into your bin folder

Please follow the installation instructions carefully.

Java Advanced Imaging
'''''''''''''''''''''

Java Advanced Imaging is an image processing library allowing you to form chains of operations
to process rasters in a manner similar to functional programming.

References:

* http://java.net/projects/jai-core

1. Download this Version of JAI
   
   * Java Advanced Imaging API 1.1.3
     
   At the time of writing Oracle is migrating java projects around - try the following:
     
   * http://download.java.net/media/jai/builds/release/1_1_3/
   * http://download.java.net/media/jai/builds/release/1_1_3/INSTALL.html

2. Download JAI for your JDK by clicking on the link for your platform:
   
   Example: jai-1_1_3-lib-windows-i586-jdk.exe

3. Use the one click installer to install JAI into your JDK
4. Download JAI for your JRE by clicking on the link for your platform:
   
   Example: jai-1_1_3-lib-windows-i586-jre.exe

5. Use the one click installer to install JAI into your JRE

   (If you are working on linux you will of course need to choose the appropriate download)

Java Image IO
'''''''''''''

Java ImageIO provides the raw "formats" that allow both Java and JAI to read in additional image
files. This is similar in practice to the JDBC library allowing data vendors to supply JDBC drivers.


References:

* http://java.net/projects/imageio

1. Download this Version of ImageIO:
   
   * JAI Image I/O Tools 1.1
   
   At the time of writing Oracle is migrating java projects around - try the following:
   
   * http://download.java.net/media/jai-imageio/builds/release/1.1/
   * http://download.java.net/media/jai-imageio/builds/release/1.1/INSTALL-jai_imageio.html

2. Download ImageIO for your JDK by clicking on the above link.
   
   Example: jai_imageio-1_1-lib-windows-i586-jdk.exe

3. Use the one click installer to install ImageIO into your JDK
   
   (Depending on your configuration this may be sufficient for your needs)

4. Download ImageIO for your JRE by clicking on the link for your platform
   
   Example: jai_imageio-1_1-lib-windows-i586-jre.exe

5. Use the one click installer to install the ImageIO into your JRE.
   
   (If you are working on linux you will of course need to choose the appropriate download)

ImageIO-Ext Install
'''''''''''''''''''

.. note::
   
   The installer provided here will install JAI and ImageIO if needed

The installer from the ImageIO-Ext website can be used to install into your JAVA_HOME (ie the JDK). If you like you can use this to install the software; and then copy the required jars into your JRE by hand (they end up in JAVA_HOME/jre/ext/libs and need to be copied into JRE/ext/libs).

1. Download this version of ImageIO-ext:
   
   * ImageIO-EXT 1.0.8
   * http://java.net/projects/imageio-ext

2. Download ImageIO for your JDK by clicking on the above link.
   
   Example: `windows32-imageio-ext-installer-gdal-mrsid-1.0.8.zip <http://java.net/projects/imageio-ext/downloads/download/Releases/ImageIO-Ext/1.0.x/1.0.8/windows32-installer/windows32-imageio-ext-installer-gdal-mrsid-1.0.8.zip>`_
   
   Example: `windows32-imageio-ext-installer-gdal-mrsid-ecw-1.0.8.zip <http://java.net/projects/imageio-ext/downloads/download/Releases/ImageIO-Ext/1.0.x/1.0.8/windows32-installer/windows32-imageio-ext-installer-gdal-mrsid-ecw-1.0.8.zip>`_

3. This will install the required extension into your **JAVA_HOME/jre/ext/libs**

4. Make a copy of these files into your *JRE_HOME/ext/libs**

Alternate CLASSPATH Install
'''''''''''''''''''''''''''

This is only needed if the windows one-click installers don't work for you:

* Perhaps you have several JDKs installed on your system?
* Perhaps you are on Linux?
* Perhaps you are on Mac and the version of JAI/ImageIO included with your operating system is out of date?

The goal is to place the required jars into your lib/ext directory of both your JDK (for compiling) and your JRE (for running).

Optional: Mac ImageIO
'''''''''''''''''''''

Java Advanced Imaging is included with recent releases of Mac OS (but that may be changing in the future).

The JAI ImageIO extension is not available as a download for the mac. However, you can use the jar from the Linux/windows download to get “pure java” functionality without hardware acceleration:

1. Copy the jars to ~/Library/Java/Extensions
2. Check that the files are present as expected:
   
   * clibwrapper_jiio.jar 
   * jai_core.jar
   * mlibwrapper_jai.jar
   * jai_codec.jar
   * jai_imageio.jar
