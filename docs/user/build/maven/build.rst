Maven Build
------------

Several GeoTools modules depend on other GeoTools modules, so the first thing you will want to do is perform a full build so that you have a jar from each module installed in your local repository.

Your First Build
^^^^^^^^^^^^^^^^

1. Start by going to where you have the source code::

     cd C:\java\geotools\trunk

2. And check that we actually have the source code::

     C:\java\geotools\trunk>dir
      Volume in drive C is INTERNAL
      Volume Serial Number is 3CA5-71DD
      Directory of C:\java\geotools\trunk
     26/04/2007  11:12 AM    <DIR>          .
     26/04/2007  11:12 AM    <DIR>          ..
     11/01/2007  12:25 AM    <DIR>          build
     01/12/2006  01:27 AM    <DIR>          demo
     04/11/2006  01:04 PM    <DIR>          doc
     16/07/2006  07:56 AM    <DIR>          licenses
     07/04/2007  10:36 AM    <DIR>          modules
     26/04/2007  11:12 AM            52,450 pom.xml
     22/10/2006  09:11 AM             3,705 README.txt
     26/04/2007  10:08 AM    <DIR>          target
                    2 File(s)         56,155 bytes
                    8 Dir(s)  15,264,776,192 bytes free

3. Make sure you are connected to the internet
4. Start your first build::

     C:\java\geotools\trunk>mvn install

5. If all is well, Maven should download the required .jar files and build GeoTools modules.
6. At the end of this process it will display a list of all the modules which were built and installed correctly.::

      [INFO] ------------------------------------------------------------------------
      [INFO] BUILD SUCCESSFUL
      [INFO] ------------------------------------------------------------------------
      [INFO] Total time: 9 months, 3 weeks, 12 hours, 3 minuets, and 43 seconds
      [INFO] Finished at: Sat Feb 12 16:05:08 EST 2011
      [INFO] Final Memory: 41M/87M
      [INFO] ------------------------------------------------------------------------

7. The first build takes a while due to the download time for the .jar files.

If you have any trouble check the common build failures at the bottom of this page.

Build Failure
^^^^^^^^^^^^^

It is all well and good to recognise a successful build, but how do you recognise a build that has failed?

1. If your build fails you will get feedback like this::

     [INFO] ------------------------------------------------------------------------
     [ERROR] BUILD FAILURE
     [INFO] ------------------------------------------------------------------------
     [INFO] There are test failures.
     [INFO] ------------------------------------------------------------------------
     [INFO] For more information, run Maven with the -e switch
     [INFO] ------------------------------------------------------------------------
     [INFO] Total time: 7 minutes 56 seconds
     [INFO] Finished at: Mon Nov 20 12:15:48 PST 2006
     [INFO] Final Memory: 23M/42M
     [INFO] ------------------------------------------------------------------------
2. You need to scan back through the output and find the "<<< FAILURE!"::

     Running org.geotools.data.mif.MIFDataStoreTest
     Tests run: 9, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 6.703 sec <<< FAILURE!

3. You can open the test results of the indicated failure in order to see what went wrong.
   Test results are contained in the target directory.

Expected Build times
^^^^^^^^^^^^^^^^^^^^

Depending on your hardware and internet connection:

* Building the first time, where maven needs to download everything, may take 20 to 30 minuets.
* Future builds check for the most recent .jar files from the internet. The checking is based of md5 checksums and does not take long. Building subsequently may take 10 minuets depending on your hardware and internet connection.
* After everything is downloaded can build "offline" and avoid the checking of mdf5 checksums resulting in a faster build of 5-7 minuets.
* Finally you can turn off tests (danger!) and build offline to get a build under 2 minuets

Tips to speed up a build:

* Do not do a "clean" build if you do not have to
* Experiment with the best use of Maven 3 "threading" for your computer
* Rebuild a single module after you have modified it
* Update your "settings.xml" file to point to a "mirror" in your country - allowing you to download closer to home
* Build offline (when everything is downloaded to your local repository)

Common Build Problems
^^^^^^^^^^^^^^^^^^^^^

The following common problems occur during a::
   mvn -U clean install

Unable to find org.geotools.maven:javadoc:jar
'''''''''''''''''''''''''''''''''''''''''''''

We have a little of a chicken-and-the-egg problem here when building a tag for the first time.

To fix you need to build the javadoc jar by hand.

1. Change to the module directory::

      cd build/maven/javadoc

2. Build the javadoc module

      mvn install

3. You can now return to the root of the project and restart your build.

Note that this plugin requires your JAVA_HOME to be set to a JDK as it makes use of the tools.jar (in order to build javadocs).

Failure of Metadata RangeSetTest
''''''''''''''''''''''''''''''''

This looks like the following::

   [INFO] ----------------------------------------------------------------------------
   [INFO] Building Metadata
   [INFO]    task-segment: [clean, install]
   [INFO] ----------------------------------------------------------------------------
   [INFO] [clean:clean]
   ...
   Running org.geotools.util.RangeSetTest
   Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0.031 sec <<< FAILURE!

Navigating into the directory to look at the actual error::

   C:\java\geotools\trunk\modules\library\metadata\target\surefire-reports>more *RangeSetTest.txt
   -------------------------------------------------------------------------------
   Test set: org.geotools.util.RangeSetTest
   -------------------------------------------------------------------------------
   Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0.031 sec <<< FAILURE!
   testRangeRemoval(org.geotools.util.RangeSetTest)  Time elapsed: 0 sec  <<< ERROR!
   java.lang.NoClassDefFoundError: javax/media/jai/util/Range
           at org.geotools.util.RangeSetTest.testRangeRemoval(RangeSetTest.java:58)

This indicates that Java Advanced Imaging has not been installed into the JRE (please see the dependencies section and try again).

On GeoTools trunk you can try the following experimental option. This will download and use just the JAI jar files, you wont get native performance - but for a build do you even care?::

   mvn install -Pnojai

Failure of GridCoverageRendererTest
'''''''''''''''''''''''''''''''''''

This looks like the following::

   [INFO] ----------------------------------------------------------------------------
   [INFO] Building Render
   [INFO]    task-segment: [install]
   [INFO] ----------------------------------------------------------------------------
   ...
   Running org.geotools.renderer.lite.GridCoverageRendererTest
   Tests run: 2, Failures: 0, Errors: 2, Skipped: 0, Time elapsed: 0.062 sec <<< FAILURE!
   Details:

   C:\java\geotools\trunk\modules\library\render\target\surefire-reports>more *GridCoverageRendererTest.txt
   -------------------------------------------------------------------------------
   Test set: org.geotools.renderer.lite.GridCoverageRendererTest
   -------------------------------------------------------------------------------
   Tests run: 2, Failures: 0, Errors: 2, Skipped: 0, Time elapsed: 0.062 sec <<< FAILURE!
   testPaint(org.geotools.renderer.lite.GridCoverageRendererTest)  Time elapsed: 0.047 sec  <<< ERROR!
   java.lang.NullPointerException
        at org.geotools.renderer.lite.GridCoverageRendererTest.getGC(GridCoverageRendererTest.java:103)
        at org.geotools.renderer.lite.GridCoverageRendererTest.testPaint(GridCoverageRendererTest.java:163)

   testReproject(org.geotools.renderer.lite.GridCoverageRendererTest)  Time elapsed: 0 sec  <<< ERROR!
   java.lang.NullPointerException
        at org.geotools.renderer.lite.GridCoverageRendererTest.getGC(GridCoverageRendererTest.java:103)
        at org.geotools.renderer.lite.GridCoverageRendererTest.testReproject(GridCoverageRendererTest.java:199)

This indicates that Image IO support has not been installed into the JRE (please see the dependencies section and try again).

Unable to Delete Directory on Windows
'''''''''''''''''''''''''''''''''''''

Build systems like maven (that smash files around for a living) are generally incompatible with Microsoft Indexing Service.
From Lim Goh on email

I would also like to point out for future reference that the Windows
Indexing Service is not 100% compatible with maven, and causes some
maven builds to break. Developers who use Windows 7 64-bit (or
anything close like Vista or 32-bit) may have unsuccessful build due
to "unable to delete directory". If that happens please try to disable
Windows Indexing Service for the entire svn working copy and try
again. Hopefully this will fix the problem.

With this in mind it is also advisable for mac developers to "ignore" build directories from Time Machine (as the files change constantly and Time Machine will burn up your space trying to keep track of it all).