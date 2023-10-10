Maven Install
-------------

We use Maven for our build environment, this page describes the development environment and
configuration. Actual build instructions will happen later.

Reference:

* http://maven.apache.org/
* http://maven.apache.org/download.html
* https://maven.apache.org/install.html

SDKMan!
^^^^^^^

Recommended: The `SDKMAN! <https://sdkman.io>`__ recommended for downloading installing, and swapping between versions of build tools and environments:

.. code-block:: bash

   sdk install maven

Package Manager
^^^^^^^^^^^^^^^

Installing maven using your ***apt-get** or another package manager allows maven to managed and patched
alongside your operating system updates.

* Ubuntu:
  
   .. code-block:: bash

     sudo apt-get update
     sudo apt-get install maven
  
If you find that you end up installing an older version (or you'd like to use the very latest), consider use of :command:`sdk` above (or manual installation).

MacOS Homebrew
^^^^^^^^^^^^^^

On macOS the `Homebrew <https://brew.sh>`__ package manager provides a "formula" to install Maven:

.. code-block:: bash

   brew install maven
   
Linux Manual install
^^^^^^^^^^^^^^^^^^^^

The official `official installation instructions <https://maven.apache.org/install.html>`_ provide an outline of unzipping the download bundle and adding it to your ``M2_HOME`` and ``PATH`` environmental variables.

For more detailed instructions:

* Ubuntu: https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu
* Debian: 

Windows manual install
^^^^^^^^^^^^^^^^^^^^^^

1. Download Maven. Last tested with Maven 3.9.5.

2. Unzip the Maven download to your computer:
   
   Example: ``C:\java\apache-maven-3.9.5``.
   
   If you do not have an unzip program may we recommend: http://www.7-zip.org/

3. Set the following environmental variables for Maven to work (note your paths may differ based on Java or Maven revision numbers):
   
   * ``JAVA_HOME = C:\PROGRA~1\ECLIPS~1\jdk-11.0.17.8-hotspot``
    
     Location of your JDK installation
   
   * ``MAVEN_HOME = C:\java\apache-maven-3.8.6``
     
     Location of your Maven installation
   
   * ``PATH = %PATH%;%JAVA_HOME%\bin;%M2_HOME%\bin``
     
     Include Java and Maven bin directory in your PATH

4. Open up a ``cmd`` window and type the following::
     
      > mvn -version
    
      Apache Maven 3.9.5(84538c9988a25aec085021c365c560670ad80f63)
      Maven home: C:\devel\apache-maven-3.9.5
      Java version: 11.0.17, vendor: Eclipse Adoptium, runtime: C:\PROGRA~1\ECLIPS~1\jdk-11.0.17.8-hotspot
      Default locale: it_IT, platform encoding: Cp1252
      OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
