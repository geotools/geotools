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

3. Navigate to the root ``pom.xml`` for the project and go to the dependency management section.
   
   Cut and past paste the dependency information here as well::
      
      <dependency>
        <groupId>net.java.dev.swing-layout</groupId>
        <artifactId>swing-layout</artifactId>
        <version>1.0.2</version>
      </dependency>

3. You can then adjust your ``pom.xml`` to not include the version number (as it will be retrieved
   from the dependency management section).::
      
      <dependency>
        <groupId>net.java.dev.swing-layout</groupId>
        <artifactId>swing-layout</artifactId>
        <!-- version retrieved from parent -->
      </dependency>

Recommended reading on Dependencies
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you are not familiar with the way to declare a dependency in a Maven ``pom.xml`` file, see "How do I use external dependencies?" in the Maven Getting started guide. More information can also be found in the Guide to deploying 3rd party JARs to remote repository in Maven documentation.

References:

* http://maven.apache.org/guides/getting-started/index.html
* http://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html

Our build process does not include jar files inside the source code repository, instead Maven downloads jar files it needs from remote repositories (web sites). The location of these web sites is specified in the parent ``pom.xml`` file, which is inherited by all modules.

.. literalinclude:: /../../pom.xml
   :language: xml
   :start-at:   <repositories>
   :end-at:   </repositories>

There are mainly three repositories available:

* Maven Central repository
  
  Contents from a wide range of organizations:
  
  https://repo1.maven.org/maven2/
  
* OSGeo Nexus Release Repository
  
  Release jars specific to us such as JTS:
  
  https://repo.osgeo.org/repository/release/
  
* OSGeo Nexus Snapshot Repository
  
  Snapshot repository hosting nightly builds:
  
  https://repo.osgeo.org/repository/snapshot

The OSGeo release repository above caches artifacts from a number of repositories used or maintained by communnity members:

* ucar-cache - https://artifacts.unidata.ucar.edu/content/repositories/unidata-releases
* restlet-cache - http://maven.restlet.org
* geo-solutions-cache - http://maven.geo-solutions.it/
* k-int-cache - http://maven.k-int.com/content/repositories/releases/
* seasar-cache - http://maven.seasar.org/maven2
* no.ecc-cache - https://maven.ecc.no/releases

Take a look at these sites and some of the "mystery" out of how Maven works. You may notice that the directory structure matches the dependency entries that you see in the ``pom.xml`` files. If the dependency entry has a `groupId` tag then this will be the name of the folder, if it just has an ``artifactId`` tag then this will be used for the name of the folder and the jar within it.

It is always worth taking a look at these sites to check that a version of the jar you want to use is not already available.

References:

* https://search.maven.org
* http://repo.osgeo.org/
* https://wiki.osgeo.org/wiki/SAC:Repo

It really is not available - how to upload?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Assuming the jar you want is not already hosted on one of these sites you need to upload it and add a dependency entry to your ``pom.xml`` file.

The GeoTools project has access to two repositories for distributing content:

.. literalinclude:: /../../pom.xml
   :language: xml
   :start-at:   <distributionManagement>
   :end-at:   </distributionManagement>

Uploading a jar file is not enough. A clean repository also needs the associated ``jar.md5``,
``jar.sha1``, ``pom``, ``pom.md5``, ``pom.sha1`` files to be uploaded or generated,
and needs the various ``maven-metadata.xml`` files to be updated (as well as their
associated ``md5`` and ``sha1`` files).
  
This is very tedious, so don't do that by hand! Maven will do this automatically for you.

Deploying to Open Source Geospatial Foundation Nexus Repository:
   
1. This is the GeoTools repository used for stable releases; as such it is the best place to host
   extra 3rd party dependencies. Use your OSGeo ID login credentials:
   
   * https://repo.osgeo.org/repository/geotools-releases/
   
2. Create or update your ~/.m2/settings.xml file as below (~ is your home directory)::
     
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
                            -DrepositoryId=osgeo         \
                            -Durl=dav:http://download.osgeo.org/upload/geotools/

4. Or if you have a pom file::
     
     mvn deploy:deploy-file -DpomFile=<path-to-pom>      \
                            -Dfile=<path-to-file>        \
                            -DrepositoryId=osgeo         \
                            -Durl=dav:http://download.osgeo.org/upload/geotools/

5. Elements in bracket (<foo>) need to be replaced by their actual values.

Examples of deploy-file to upload JTS JAR
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Change into one of the GeoTools directories (the geotools ``pom.xml`` has all the
   repository definitions so changing directories is easier than editing your settings.xml)::
     
     C:\> cd java\geotools\trunk

2. Here is an example of how to deploy the JTS binary jar::
     
      C:\java\geotools\trunk>mvn deploy:deploy-file -DgroupId=org.locationtech -DartifactId=jts-core -Dversion=1.13 -Dfile=C:\java\jts\lib\jts-1.13.jar -Dpackaging=jar -DrepositoryId=osgeo -Durl=dav:http://download.osgeo.org/upload/geotools/

3. And the source code (you will need to zip this up first since JTS does not provide a source download)::
    
    C:\java\geotools\trunk>mvn deploy:deploy-file -DgroupId=org.locationtech -DartifactId=jts -Dversion=1.13 -Dfile=C:\java\jts\jts-1.13-src.zip -Dpackaging=java-source -DrepositoryId=osgeo -Durl=dav:http://download.osgeo.org/webdav/geotools/ -DgeneratePom=false

Alternative uploading to Nexus Repository
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Uploading to Open Source Geospatial Foundation Nexus Repository:

1. Login (using your OSGeo User id) to https://repo.osgeo.org/

2. Visit the upload page, and select geotools-releases from the list.

   * https://repo.osgeo.org/#browse/upload:geotools-releases

   If this is not shown you do not have permission, please ask on the developer list.
   
3. Upload your third-party jar browsing for the jar to upload, and providing a classifier and extension if required.
   
   Be sure to:
   
   * Use :guilabel:`Add another assest` button to include a ``src`` or ``javadoc`` zip at the same time using approprite classifier.
   
   * Either upload a ``pom.xml`` as one of the assets, or fill in the component coordinates: groupId, artifactId, and Version.
