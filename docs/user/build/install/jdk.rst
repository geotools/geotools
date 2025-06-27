Java Install
-------------

GeoTools is written in the Java Programming Language. The library is targeted for Java 17.

Java Run-time Environment:

* Java 17 - GeoTools 34.x and above (OpenJDK tested)
* Java 11 - GeoTools 21.x to 33.x (OpenJDK tested)
* Java 8 - GeoTools 15.x up to and including GeoTools 28.x (OpenJDK and Oracle JRE tested)
* Java 7 - GeoTools 11.x to GeoTools 14.x (OpenJDK and Oracle JRE tested)
* Java 6 - GeoTools 8.x to GeoTools 10.x (Oracle JRE tested)
* Java 5 - GeoTools 2.5.x to GeoTools 8.x (Sun JRE tested)
* Java 1.4 - GeoTools 2.x to GeoTools 2.4.x (Sun JRE tested)

When developing GeoTools please change your compile options to:

* IDE: Produce Java 17 compliant code
* Maven: source=17

GeoTools Java 17 Minimum
'''''''''''''''''''''''''

Java introduced a number of changes to the JVM, most notably "Strongly Encapsulate JDK Internals" and "Sealed Classes" enhancing security. This will produce some warnings on startup which we intend to address over the course of GeoTools 34.x development.

========= ================ ================ ================= ===============
Java      Initial          Final            Compiler Setting  Compatibility
========= ================ ================ ================= ===============
Java 17   GeoTools 34.x    And Above        compiler=17       Java 17
Java 11   GeoTools 31.x    GeoTools 33.x    compiler=11       Java 11
Java 8    GeoTools 15.x    GeoTools 30.x    compiler=8        Java 1.8
========= ================ ================ ================= ===============

GeoTools 34.x built with Java 17 can only be used in a Java 17 environment (and is not compatible with Java 11). Each jar includes an automatic module name for use on the module path (refer to `The State of the Module System <http://openjdk.java.net/projects/jigsaw/spec/sotms/>`_ for more details on Java module system).

GeoTools Java 17 development is supported on both OpenJDK and Oracle JDK as downloaded from:

========================= =================== ===== ====== ======= ======= ==============
Java 17 Provider          License             Linux macOS  Solaris Windows Free Updates
========================= =================== ===== ====== ======= ======= ==============
Oracle JDK                Binary Code License x     x      x       x       2024-10*
Oracle OpenJDK            GPL                 x     x              x       2029-09
RedHat OpenJDK            GPL                 x                            2027-11
Eclipse Adoptium          GPL                 x     x              x       2029+**
========================= =================== ===== ====== ======= ======= ==============

.. note:: 
   \* Oracle JDK 17 free updates ended October 2024; continued updates require paid Oracle Java SE subscription.
   
   \*\* Eclipse Adoptium provides free updates as long as upstream OpenJDK is actively maintained (expected through at least Oct 2027 for Java 17 and Dec 2029 for Java 21).

.. warning:: Since the API changes from Java version to version, building a GeoTools version with a newer Java SDK is risky (you may accidentally use a new method). Pull requests are tested against Java 17 and Java 21, but we do ask you to be careful.

SDKMan!
^^^^^^^

Recommended: The `SDKMAN! <https://sdkman.io>`__ recommended for downloading installing, and swapping between versions of build tools and environments:

1. Java Development Kit 17 (JDK 17)
     
   .. code-block:: bash
   
      # list to determine latest Temurin JDK 17
      sdk list java | grep "17.*-tem"
   
      # Installing latest Temurin JDK 17 shown above
      sdk install java 17.0.15-tem

2. Optional: Java Development Kit 21 (JDK 21)
     
   .. code-block:: bash
   
      # list to determine latest Temurin JDK 21
      sdk list java | grep "21.*-tem"
   
      # Installing latest Temurin JDK 21 shown above
      sdk install java 21.0.7-tem
   
3. GeoTools build process takes advantage of quality assurance tools compatible with the JDK you have installed.
   
   To change to JDK 17 (using tab completion to review available installs):
   
   .. code-block:: bash
   
      sdk use java 17<tab>

   To change to JDK 21 (using tab completion to review available installs):
   
   .. code-block:: bash
   
      sdk use java 21<tab>

Windows or macOS Install
^^^^^^^^^^^^^^^^^^^^^^^^

1. Download an OpenJDK release for your platform:

   * https://adoptium.net/temurin/releases/?version=17 Temurin 17 (LTS) - Recommended
   
2. Choose the options to:
   
   * Updating the ``JAVA_HOME`` environment variable
   * Add the installation to the ``PATH`` environment variable

Package Manager
^^^^^^^^^^^^^^^

Installing OpenJDK using your ***apt-get** or another package manager allows Java to managed and patched
alongside your operating system updates.

* Ubuntu:
  
   .. code-block:: bash
   
     sudo apt-get update
     sudo apt-get install openjdk-17-jdk
  
If you find that you end up installing an older version (or you'd like to use the very latest), consider use of :command:`sdk` above (or manual installation).

MacOS Homebrew
^^^^^^^^^^^^^^

On macOS the `Homebrew <https://brew.sh>`__ package manager provides a "formula" to install OpenJDK:

.. code-block:: bash

   brew install openjdk@17

Setup ``JAVA_HOME`` and ``PATH`` environmental variables.

Troubleshooting
'''''''''''''''

Why JAVA_HOME does not work on Windows
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

How to use different versions of Java for building and running on windows.

Several projects expect to make use of the latest JRE run-time environment
(for speed or new features). If your computer is set up with both a
stable JDK for building GeoTools; and an experimental JDK for your other
projects you will need to sort out how to switch between them.

One technique is to set up a batch file similar to the following:

1. Hunt down the ``cmd.exe`` ( Start menu > Accessories > Command Prompt) and right click to send it to the desktop
2. Edit the desktop ``cmd.exe`` short cut and change the target to::
      
      %SystemRoot%\system32\cmd.exe /k C:\java\java17.bat

3. Create the ``C:\java\java17.bat`` file mentioned above, e.g. (actual versions may vary, if you have spaces in paths short paths might be required)::
   
      set MAVEN_HOME=C:\java\maven-3.8.6
      set JAVA_HOME="C:\PROGRA~1\ECLIPS~1\jdk-17.0.15-hotspot"
      
      set PATH=%JAVA_HOME%\bin;%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem

4. Please note that the construction of the PATH above is very important; ``JAVA_HOME\bin`` must
   appear before ``SystemRoot\system32`` as the ``system32`` contains a stub ``java.exe`` that looks up
   the correct version of Java to run in the registry.
   
   .. image:: /images/jdk.png
   
5. You can see in the above screen snap that the
   ``My Computer\HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft > Java Development Kit > CurrentVersion``
   is set to **17**.
   
   The **17** entry documents the path to the version of Java to run.
   
   Placing JAVA_HOME on the path before ``System32`` shortcuts this annoying "feature".

