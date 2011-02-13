How to add a 3rd party jar
==========================

This short document answers the question "So, how do I let everyone have a copy of a jar that my code depends on?".

Reference:

* http://maven.apache.org/guides/mini/guide-3rd-party-jars-remote.html

Before You Start
^^^^^^^^^^^^^^^^

Please check the jar you are looking for may already be in a repository out there on the big bad internet:

Review a couple of the repository search websites:

* http://mvnrepository.com/ and search 
* http://maven.ozacc.com/

1. If you get a hit - confirm it is the jar you want: 
    
    Example: http://mvnrepository.com/artifact/net.java.dev.swing-layout/swing-layout
  
2. Navigate to the correct version and cut and paste the dependency information from the
   website into your modules pom.xml::
      
      <dependency>
        <groupId>net.java.dev.swing-layout</groupId>
        <artifactId>swing-layout</artifactId>
        <version>1.0.2</version>
      </dependency>

3. Navigate to the root pom.xml for the project and go to the dependency management section.
   
   Cut and past paste the dependency information here as well::
      
      <dependency>
        <groupId>net.java.dev.swing-layout</groupId>
        <artifactId>swing-layout</artifactId>
        <version>1.0.2</version>
      </dependency>

3. You can then adjust your pom.xml to not include the version number (as it will be retrieved
   from the dependency management section).::
      
      <dependency>
        <groupId>net.java.dev.swing-layout</groupId>
        <artifactId>swing-layout</artifactId>
        <!-- version retrieved from parent -->
      </dependency>

Recommended reading on Dependencies
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you are not familiar with the way to declare a dependency in a Maven pom.xml file, see "How do I use external dependencies?" in the Maven Getting started guide. More information can also be found in the Guide to deploying 3rd party JARs to remote repository in Maven documentation.

References:

* http://maven.apache.org/guides/getting-started/index.html
* http://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

Our build process does not include jar files inside the subversion repository, instead Maven downloads jar files it needs from remote repositories (web sites). The location of these web sites is specified in the parent pom.xml file, which is inherited by all modules. There are mainly three sites available:

* Java.net repository
  
  http://download.java.net/maven/2

* Open Source Geospatial Foundation Repository
  
  Jars specific to us such as JTS.
  
  http://download.osgeo.org/webdav/geotools/

* OpenGeo Maven Repository
  
  This is our SNAPSHOT repository, offering better performance and often
  contains the same jars as osgeo foundation repository.
  
  http://repo.opengeo.org

* http://www.ibiblio.org/maven2/
  
  General utility open source projects, especially apache related

Take a look at these sites and some of the "mystery" out of how Maven works. You may notice that the directory structure matches the dependency entries that you see in the pom.xml files. If the dependency entry has a groupId tag then this will be the name of the folder, if it just has an id tag then this will be used for the name of the folder and the jar within it.

It is always worth taking a look at these sites (particularly the maven one) just to check that a version of the jar you want to use is not already available.

It really is not available - how to upload?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Assuming the jar you want is not already hosted on one of these sites you need to upload it and add a dependency entry to your pom.xml file.

* Upload with Maven (not by copy-and-paste)
  
  Uploading a jar file is not enough. A clean repository also needs the associated jar.md5,
  jar.sha1, pom, pom.md5, pom.sha1 files to be uploaded, and needs the various
  maven-metadata.xml files to be updated (as well as their associated md5 and sha1 files).
  
  This is very tedious, so don't do that by hand! Maven 2 will do this automatically for you.

Deploying to Open Source Geospatial Foundation Maven 2 Repository:
   
1. This is the GeoTools repository used for stable releases; as such it is the best place to host
   extra 3rd party dependencies. You can use your OSGeo ID login credentials when deploying;
   provided you are a signed up contributor to the GeoTools project.
   
   * http://download.osgeo.org/webdav/geotools/
   
3. Create or update your ~/.m2/settings.xml file as below (~ is your home directory)::
     
     <?xml version="1.0" encoding="ISO-8859-1"?>
     <settings>
       <servers>
         <server>
           <id>osgeo</id>
           <username>your osgeo id</username>
           <password>your osgeo password</password>
         </server>
       </servers>
     </settings>

3. Now you can deploy your jar::
     
     mvn deploy:deploy-file -DgroupId=<group-id>         \
                            -DartifactId=<artifact-id>   \
                            -Dversion=<version>          \
                            -Dfile=<path-to-file>        \
                            -Dpackaging=jar              \
                            -DrepositoryId=osgeo   \
                            -Durl=dav:http://download.osgeo.org/webdav/geotools/

4. Or if you have a pom file::
     
     mvn deploy:deploy-file -DpomFile=<path-to-pom>      \
                            -Dfile=<path-to-file>        \
                            -DrepositoryId=osgeo   \
                            -Durl=dav:http://download.osgeo.org/webdav/geotools/

5. Elements in bracket (<foo>) need to be replaced by their actual values.

Deploying to OpenGeo Maven 2 Repository:

1. This is the repository used for SNAPSHOT releases; you can ask on the email list for access.
2. Although this really is more the target for a "mvn deploy" then something to upload to by
   hand.
   
   * http://repo.opengeo.org

Uploading to Ibiblio

1. You can also upload your new jar to ibiblio - in case lists.refractions.net is hacked again.
   
2. Unfortunately only a limited number of GeoTools developers have ibiblio access so unless you
   are one of the lucky few you will have to ask someone else to do that part for you.
3. To do this create a JIRA task requesting that your jar be uploaded/
   
   * http://jira.codehaus.org/secure/CreateIssue!default.jspa

4. In this task request include the following information:
   
   * Location where the jar can be obtained from
   * A version number for the jar (this should match what you specify in the pom.xml)
   * The license under which the jar can be re-distributed

Examples of Updating JTS Jar
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Change into one of the GeoTools directories (the geotools pom.xml has all the
   repository definitions so changing directories is easier than editing your settings.xml)::
     
     C:\> cd java\geotools\trunk

2. Here is an example of how to deploy the JTS binary jar::
     
      C:\java\geotools\trunk>mvn deploy:deploy-file -DgroupId=com.vividsolutions -DartifactId=jts -Dversion=1.12 -Dfile=C:\java\jts\lib\jts-1.12.jar -Dpackaging=jar -DrepositoryId=osgeo -Durl=dav:http://download.osgeo.org/webdav/geotools/

3. And the source code (you will need to zip this up first since JTS does not provide a source download)::
    
    C:\java\geotools\trunk>mvn deploy:deploy-file -DgroupId=com.vividsolutions -DartifactId=jts -Dversion=1.12 -Dfile=C:\java\jts\jts-1.12-src.zip -Dpackaging=java-source -DrepositoryId=osgeo -Durl=dav:http://download.osgeo.org/webdav/geotools/ -DgeneratePom=false
