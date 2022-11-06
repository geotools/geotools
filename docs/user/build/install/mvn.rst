Maven Install
-------------

We use Maven for our build environment, this page describes the development environment and
configuration. Actual build instructions will happen later.

Reference:

* http://maven.apache.org/
* http://maven.apache.org/download.html

Download and Install Maven
^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Download Maven. Last tested with Maven 3.8.6.
2. Unzip the Maven download to your computer:
   
   Example: ``C:\java\apache-maven-3.8.6``.
   
   If you do not have an unzip program may we recommend: http://www.7-zip.org/

3. You need to have the following environmental variables set for Maven to work (note your paths may differ based on Java or Maven revision numbers):
   
   * ``JAVA_HOME = C:\PROGRA~1\ECLIPS~1\jdk-11.0.17.8-hotspot``
    
     Location of your JDK installation
   
   * ``MAVEN_HOME = C:\java\apache-maven-3.8.6``
     
     Location of your Maven installation
   
   * ``PATH = %PATH%;%JAVA_HOME%\bin;%M2_HOME%\bin``
     
     Include Java and Maven bin directory in your PATH

4. Open up a ``cmd`` window and type the following::
     
     > mvn -version
    Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
    Maven home: C:\devel\apache-maven-3.8.6
    Java version: 11.0.17, vendor: Eclipse Adoptium, runtime: C:\PROGRA~1\ECLIPS~1\jdk-11.0.17.8-hotspot
    Default locale: it_IT, platform encoding: Cp1252
    OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

Maven and Apt-Get
^^^^^^^^^^^^^^^^^

Current versions of Ubuntu provide Maven 3; however if you're using an older version or you'd like to use the very latest, please
install the latest version manually by following the `official installation instructions <https://maven.apache.org/install.html>`_.

Maven and Home Brew
^^^^^^^^^^^^^^^^^^^

On macOS the homebrew package manager provides a "formula" to install Maven::

  brew install maven
