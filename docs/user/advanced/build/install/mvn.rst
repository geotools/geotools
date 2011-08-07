Maven Install
-------------

We use Maven 2 for our build environment, this page simply set's it up for you. Actual build instructions will happen later.

Reference:

* http://maven.apache.org/
* http://maven.apache.org/start/install.html

Download and Install Maven
^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Download Maven. Last tested with Maven 2.2.1
2. Unzip the maven download to your computer:
   
   Example: C:\java\apache-maven-2.2.1.
   
   If you do not have an unzip program may we recommend: http://www.7-zip.org/

3. You need to have the following environmental variables set for maven to work:
   
   * JAVA_HOME = C:\j2sdk1.4.2_07\
    
     Location of your JDK installation
   
   * M2_HOME = C:\java\apache-maven-2.2.1
     
     Location of your maven installation
   
   * PATH = %PATH%;%JAVA_HOME%\bin;%M2_HOME%\bin
     
     Include java and maven bin directory in your PATH

4. Open up a cmd window and type the following::
     
     >mvn --version
     Apache Maven 2.2.1 (r801777; 2009-08-07 05:16:01+1000)
     Java version: 1.6.0_17
     Java home: /System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home
     Default locale: en_US, platform encoding: MacRoman
     OS name: "mac os x" version: "10.6.3" arch: "x86_64" Family: "mac"

Do not use Apt-Get
^^^^^^^^^^^^^^^^^^

It is very tempting to use apt-get to install maven, ubuntu users I am looking at you!

Please be careful of the maven provided out of the box by unbuntu::
   
   Apache Maven 2.2.1 (rdebian-1)

It is not actually apache maven as provided by apache; and it has a build failure with::
   
   [INFO] ------------------------------------------------------------------------
   [INFO] Building Cross-modules javadoc
   [INFO]    task-segment: [install]
   [INFO] ------------------------------------------------------------------------
   [INFO] [plugin:descriptor {execution: default-descriptor}]
   [WARNING] Using platform encoding (UTF-8 actually) to read mojo metadata, i.e. build is platform dependent!
   [INFO] Applying mojo extractor for language: java
   [INFO] Mojo extractor for language: java found 0 mojo descriptors.
   [INFO] Applying mojo extractor for language: bsh
   [INFO] Mojo extractor for language: bsh found 0 mojo descriptors.
   [INFO] ------------------------------------------------------------------------
   [ERROR] BUILD ERROR
   [INFO] ------------------------------------------------------------------------
   [INFO] Error extracting plugin descriptor: 'No mojo definitions were found for plugin: org.geotools.maven:javadoc.

Kenneth Gulbrandsoy has supplied the following notes on unbuntu:

References:

* http://usingmaven.bachew.net/manually-install-maven-on-linux

1. Download apache-maven-2.2.1-bin.tar.gz
   
   * http://www.apache.org/dyn/closer.cgi/maven/binaries/apache-maven-2.2.1-bin.tar.gz

2. Unpack into /usr/local/lib (or any other path you choose)::
     
     > cd /usr/local/lib
     > sudo tar -xzf apache-maven-2.2.1-bin.tar.gz

   This unpacks the folder apache-maven-2.2.1 to pwd

   ..note::
     
     At this point > usr/local/lib/apache-maven-2.2.1/bin/mvn --version will complain::
   
       Warning: JAVA_HOME environment variable is not set. 

     Step 4 solves this using a application specific environment (profile.d) script. This script
     also set the M2_HOME and PATH variables. Note that step 5 registers the script with
     current session.

3. Link apache-maven-2.2.1 to apache-maven (optional)::
     
     > ln -sT apache-maven-2.2.1 apache-maven
     
   This makes it easier to upgrade later on

4. Add maven script that set environment variables (su required)::
     
     > cat > /etc/profile.d/apache-maven.sh
     export JAVA_HOME=/usr/lib/jvm/java-6-sun/jre
     export M2_HOME=/usr/local/lib/apache-maven
     export PATH=$PATH:$M2_HOME/bin
     <Ctrl-D>
     > . /etc/profile 

5. Test Maven2. The following command should not produce any warnings::
     
     > mvn --version
     Apache Maven 2.2.1 (r801777; 2009-08-06 21:16:01+0200)
     Java version: 1.6.0_24
     Java home: /usr/lib/jvm/java-6-sun-1.6.0.24/jre
     Default locale: nb_NO, platform encoding: UTF-8
     OS name: "linux" version: "2.6.32-30-generic" arch: "amd64" Family: "unix"
