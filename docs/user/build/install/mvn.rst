Maven Install
-------------

We use Maven for our build environment, this page describes the development environment and
configuration. Actual build instructions will happen later.

Reference:

* http://maven.apache.org/
* http://maven.apache.org/download.html

Download and Install Maven
^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Download Maven. Last tested with Maven 3.3.9.
2. Unzip the maven download to your computer:
   
   Example: C:\java\apache-maven-3.3.9.
   
   If you do not have an unzip program may we recommend: http://www.7-zip.org/

3. You need to have the following environmental variables set for maven to work (note your paths may differ based on Java or Maven revision numbers):
   
   * JAVA_HOME = C:\Program Files\java\jdk1.8.0_91\
    
     Location of your JDK installation
   
   * M2_HOME = C:\java\apache-maven-3.0.5
     
     Location of your maven installation
   
   * PATH = %PATH%;%JAVA_HOME%\bin;%M2_HOME%\bin
     
     Include java and maven bin directory in your PATH

4. Open up a cmd window and type the following::
     
     > mvn -version
     Apache Maven 3.0.5 (r01de14724cdef164cd33c7c8c2fe155faf9602da; 2013-02-20 00:51:28+1100)
     Maven home: /opt/apache-maven-3.0.5
     Java version: 1.8.0_91, vendor: Oracle Corporation
     Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/jre
     Default locale: en_US, platform encoding: UTF-8
     OS name: "mac os x", version: "10.9.3", arch: "x86_64", family: "mac"

Maven and Apt-Get
^^^^^^^^^^^^^^^^^

Current versions of Ubuntu provide Maven 3; however if you're using an older version or you'd like to use the very latest, please
install the latest version manually by following the `official installation instructions <https://maven.apache.org/install.html>`_.

Maven and Home Brew
^^^^^^^^^^^^^^^^^^^

On MacOS the homebrew package manager provides a "formula" to install maven::

  brew install maven
