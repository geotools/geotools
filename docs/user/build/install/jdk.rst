Java Install
-------------

GeoTools is written in the Java Programming Language. The library is targeted for Java 8.

Java Run-time Environment:

* Java 11 - GeoTools 21.x and above (OpenJDK tested)
* Java 8 - GeoTools 15.x and above (OpenJDK and Oracle JRE tested)
* Java 7 - GeoTools 11.x to GeoTools 14.x (OpenJDK and Oracle JRE tested)
* Java 6 - GeoTools 8.x to GeoTools 10.x (Oracle JRE tested)
* Java 5 - GeoTools 2.5.x to GeoTools 8.x (Sun JRE tested)
* Java 1.4 - GeoTools 2.x to GeoTools 2.4.x (Sun JRE tested)

When developing GeoTools please change your compile options to:

* IDE: Produce Java 8 compliant code
* Maven: source=1.8

.. warning:: When building with Java 11 the result can only be used with Java 11 (even with source=1.8) due to class incompatibilities between Java 11 and Java 8.

Building GeoTools with Java 8
'''''''''''''''''''''''''''''

GeoTools requires a Java 1.8 SDK for versions 15.x and above. If your project uses an older version of Java use an appropriate version of GeoTools as shown in the table below.

========= ================ ================ ================= ===============
Java      Initial          Final            Compiler Setting  Compatibility
========= ================ ================ ================= ===============
Java 8    GeoTools 21.x    And Above        compiler=1.8      Java 8, Java 11
Java 8    GeoTools 15.x    GeoTools 20.x    compiler=1.8      Java 8
Java 7    GeoTools 11.x    GeoTools 14.x    compiler=1.7      Java 7
Java 6    GeoTools 8.x     GeoTools 10.x    compiler=1.6      Java 6
Java 5    GeoTools 2.5.x   GeoTools 8.x     compiler=1.5      Java 5
Java 1.4  GeoTools 2.x     GeoTools 2.4.x   compiler=1.4      Java 1.4
========= ================ ================ ================= ===============

GeoTools 21.x built with Java 8 can be used in a Java 11 environment. Each jar includes an automatic module name for use on the Java 11 module path.

GeoTools Java 8 development is supported on both OpenJDK and Oracle JDK as downloaded from:

========================= =================== ===== ====== ======= ======= ==============
Java 8 Provider           License             Linux macOS  Solaris Windows Free Updates
========================= =================== ===== ====== ======= ======= ==============
Oracle JDK                Binary Code License x     x      x       x       2019 January
Oracle OpenJDK            GPL                 x                            reference only
Oracle OpenJDK            Binary Code License x                    x       reference only
RedHat OpenJDK            GPL                 x                            2023 June
Adopt OpenJDK             GPL                 x     x              x       2023 September
========================= =================== ===== ====== ======= ======= ==============

Although the closed source Oracle Technology Network Oracle JDK 8 reached end-of-life in January 2019 `free Java 8 updates <https://medium.com/@javachampions/java-is-still-free-c02aef8c9e04>`__ are provided by Linux distributions, AdoptOpenJDK, Azul, IBM, Red Hat, and others.

.. note:: Windows users, when installing a JDK do not forget to install matching JRE:

   1. Download a JDK
   2. Use the one click installer
   3. When it asks if you want to install a JRE you can say yes


Building GeoTools with Java 11
''''''''''''''''''''''''''''''

Java introduced a number of changes to the JVM, most notably the module system (Project Jigsaw). Refer to `The State of the Module System <http://openjdk.java.net/projects/jigsaw/spec/sotms/>`_ for more details.

========= ================ ================ ================= ===============
Java      Initial          Final            Compiler Setting  Compatibility
========= ================ ================ ================= ===============
Java 11   GeoTools 21.x    And Above        compiler=1.8      Java 11
========= ================ ================ ================= ===============

GeoTools 21.x built with Java 11 can only be used in a Java 11 environment (and is not compatible with Java 8). Each jar includes an automatic module name for use on the Java 11 module path.

GeoTools Java 11 development is supported on both OpenJDK and Oracle JDK as downloaded from:

========================= =================== ===== ====== ======= ======= ==============
Java 11 Provider          License             Linux macOS  Solaris Windows Free Updates
========================= =================== ===== ====== ======= ======= ==============
Oracle JDK                Binary Code License x     x      x       x       2019 March
Oracle OpenJDK            GPL                 x     x              x       2019 March
RedHat OpenJDK            GPL                 x                            2024 October
Adopt OpenJDK             GPL                 x     x              x       2022 September
========================= =================== ===== ====== ======= ======= ==============

.. warning:: Since the API changes from Java version to version, building a GeoTools version with a newer Java SDK is risky (you may accidentally use a new method). Pull requests are tested against Java 8, but we do ask you to be careful.

Why JAVA_HOME does not work on Windows
''''''''''''''''''''''''''''''''''''''

How to use different versions of Java for building and running on windows.

Several projects expect to make use of the latest JRE run-time environment
(for speed or new features). If your computer is set up with both a
stable JDK for building GeoTools; and an experimental JDK for your other
projects you will need to sort out how to switch between them.

One technique is to set up a batch file similar to the following:

1. Hunt down the ``cmd.exe`` ( Start menu > Accessories > Command Prompt) and right click to send it to the desktop
2. Edit the desktop ``cmd.exe`` short cut and change the target to::
      
      %SystemRoot%\system32\cmd.exe /k C:\java\java8.bat

3. Create the ``C:\java\java8.bat`` file mentioned above::
   
      set ANT_HOME=C:\java\apache-ant-1.9.4
      set M2_HOME=C:\java\maven-3.0.5
      set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_66
      
      set PATH=%JAVA_HOME%\bin;%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem;C:\Program Files\Subversion\bin;%M2_HOME%\bin;%ANT_HOME%\bin

4. Please note that the construction of the PATH above is very important; ``JAVA_HOME\bin`` must
   appear before ``SystemRoot\system32`` as the ``system32`` contains a stub ``java.exe`` that looks up
   the correct version of Java to run in the registry.
   
   .. image:: /images/jdk.png
   
5. You can see in the above screen snap that the
   ``My Computer\HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft > Java Development Kit > CurrentVersion``
   is set to **1.8**.
   
   The **1.8** entry documents the path to the version of Java to run.
   
   Placing JAVA_HOME on the path before ``System32`` shortcuts this annoying "feature".

Java 8 Extensions
^^^^^^^^^^^^^^^^^

The image processing library used by GeoTools is capable of making use of native code.

.. note:: The Java extension system used for these native libraries was replaced in Java 9 with a module system. As a result these extensions may only be used in a Java 8 environment.

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
   
   Example: ``jai-1_1_3-lib-windows-i586-jdk.exe``

3. Use the one click installer to install JAI into your JDK
4. Download JAI for your JRE by clicking on the link for your platform:
   
   Example: ``jai-1_1_3-lib-windows-i586-jre.exe``

5. Use the one click installer to install JAI into your JRE

.. note::
   
   If you are working on Linux you will of course need to choose the appropriate download.

.. note::
   
   Separate installers are provided for both the JDK and the JRE above:
   
   * JDK Installation: Installs native extension into JDK and JRE.
   * JRE Installation: Installs native extension into JRE only.
   
   These installers unpack jars into the JRE lib folder, and native code into the bin folder.

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
   
   Example: ``jai_imageio-1_1-lib-windows-i586-jdk.exe``

3. Use the one click installer to install ImageIO into your JDK
   
   (Depending on your configuration this may be sufficient for your needs)

4. Download ImageIO for your JRE by clicking on the link for your platform
   
   Example: ``jai_imageio-1_1-lib-windows-i586-jre.exe``

5. Use the one click installer to install the ImageIO into your JRE.
   
   (If you are working on Linux you will of course need to choose the appropriate download)

.. note::
   
   Separate installers are provided for both the JDK and the JRE above:
   
   * JDK Installation: Installs native extension into JDK and JRE.
   * JRE Installation: Installs native extension into JRE only.
   
   These installers unpack jars into the JRE lib folder, and native code into the bin folder.

ImageIO-Ext Install
'''''''''''''''''''

.. note::
   
   The installer provided here will install JAI and ImageIO if needed

The installer from the ImageIO-Ext website can be used to install into your JAVA_HOME (i.e. the JDK). If you like you can use this to install the software; and then copy the required jars into your JRE by hand (they end up in ``JAVA_HOME/jre/ext/libs`` and need to be copied into ``JRE/ext/libs``).

1. Download the appropriate version of ImageIO-ext:
   
   * ImageIO-EXT 1.0.10
   * http://java.net/projects/imageio-ext
   
   Tip: You can check the version of ImageIO-EXT used in the root ``pom.xml`` file.
   
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

The goal is to place the required jars into your ``lib/ext`` directory of both your JDK (for compiling) and your JRE (for running).

Optional: mac OS ImageIO
''''''''''''''''''''''''

The JAI ImageIO extension is not available as a download for mac OS. However, you can use the jar from the Linux/windows download to get “pure Java” functionality without hardware acceleration:

1. Copy the jars to ``~/Library/Java/Extensions``
2. Check that the files are present as expected:
   
   * ``clibwrapper_jiio.jar``
   * ``jai_core.jar``
   * ``mlibwrapper_jai.jar``
   * ``jai_codec.jar``
   * ``jai_imageio.jar``
