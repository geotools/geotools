How to cut a release
====================

The instructions are meant for those making a formal milestone, a release candidate, a final
release, or a point release of the GeoTools project itself. However, the instructions can also
be used by any project or anyone that needs to make their own release, for example to use a new
release with http://geoserver.org/ or http://udig.refractions.net/.

* Do not use the Maven "release" plugin
  
  The instructions in this page invoke "svn copy", "mvn install" and "mvn deploy" manually.
  
  Those operations were supposed to be done automatically by "mvn release", but the later appears
  to be a difficult experience. It is time consuming (in case of failure, every new attempt
  implies a new checkout from svn), clobbers every pom.xml files by stripping comments and
  enumerating all possible versions of each maven plug-in (a lot), advances version number in a
  way that is not what we would like, make it difficult to use a javadoc tool more recent than
  javac, etc.
  
  Invoking "svn copy", "mvn install" and "mvn deploy" explicitly is both faster and safer than
  "mvn release", since it allows human checks before to continue to the next step and avoid to
  redo some successful steps after a problem occured and has been fixed.

* Many steps of building the release involve doing a full build of GeoTools. Often maven can fail
  in the middle of a build due to a failed test case, failing to upload an artifact, etc...
  
  The ``-rf`` (resume from) parameter of the mvn command is extremely useful in these cases. It
  is used to resume a multi module build from a specific location and prevents you from having to
  rebuild those modules that have already been successfully built.
  
  For instance, consider trying to build and test the entire code base::
    
    mvn clean install
  
  But let's say in the library/data module a failed test occurs. After fixing the test you can
  restart the build from library/data with the command::
    
    mvn clean install -rf library/data

Before You Start
^^^^^^^^^^^^^^^^

* Know the version numbers
  
  You will need to know the version number of the release you wish to make as well as the version
  number of the next release that will be made. The former is needed to correctly name the files
  being released; the latter is needed to add a new task in the JIRA issue tracker so all tasks
  which are still open move up to be addressed in the next version.

* Time and Space Requirements
  
  The release process will take a few hours of time, will require a Gigabyte of free disk space,
  and requires continuous network access.

* Software and Environment Requirements
  
  Check quickly that you are using the java and maven versions required:
  
  * Java
    
    GeoTools currently uses the JDK 1.5 version of Java(tm). The build will also need all the
    dependencies, presented earlier in this manual, which are required to build GeoTools.::
      
      java -version 
    
    Should return, for example: 1.5.0_11
    
    ========= ====================================================
    Java 6    Not supported for release at this time
    Java 5    GeoTools 2.5 and later
    Java 1.4  GeoTools 2.4 and earlier
    ========= ====================================================
    
    GeoTools 2.4 releases and earlier must be made with a 1.4 JDK and not with a 1.5 JDK in
    compatibility mode. There are enough subtle glitches in the latter case that we run into many
    user problems.::
      
      java -version 
    
    should give, for example: 1.4.2_13
  
  * Maven 2
    
    The build process uses Maven 2 - the following table indicates what version of maven is safe
    to use for the release process.
    
    ========== ====================================================
    Maven      Status
    ========== ====================================================
    3.0        does not build
    2.2.1      builds; have not tried release
    2.2.0      untested
    2.1.0      GeoTools 2.5 - 2.6 series
    2.0.10     GeoTools 2.5 - 2.6 series
    2.0.9      GeoTools 2.4
    2.0.8      GeoTools 2.4
    2.0.7      known problems
    2.0.6      known problems
    2.0.5      GeoTools 2.3
    2.0.5      GeoTools 2.2
    ========== ====================================================
    
    Latest tested release:
    
    * Maven 2.2.1
    
    To check::
       
       mvn --version
    
    Should give, for example: 2.0.10
    
    * Be sure to provide enough memory to Maven, if not already done:
      
      linux::
        
        export MAVEN_OPTS=-Xmx512M
      
      win32::
        
        set MAVEN_OPTS=-Xmx1024m

* Permissions
  
  You will need access to several accounts and to the osgeo webdav repository to properly create
  and announce the release.
  
  * Subversion Access - http://svn.osgeo.org/geotools/
    
    Contact PMC member on geotools-devel
    
    You will need our osgeo id in order to properly create a release. Your account name/password
    is used to create tags and so on.
  
  * WebDav Access - http://downloads.osgeo.org/geotools
    
    Your osgeo id is also used here, contact the PMC to ensure you are a member of the
    project.
    
    The jars get uploaded to the maven repository during the deploy phase. Those jars will then
    be pulled in by those running maven.
    
    The account name and password for webdav are stored in the settings.xml file (located in your
    user folder ``~/.m2/settings.xml``). The file can remain indefinitely and,if it is still in 
    the same place, the file will be used during a future release.::
      
      <settings>
        <servers>
         <server>
           <id>refractions</id>
           <username>username</username>
           <password>...</password>
         </server> 
         <server>
           <id>osgeo</id>
           <username>username</username>
           <password>...</password>
         </server> 
         <server>
           <id>opengeo</id>
           <username>username</username>
           <password>...</password>
         </server>
        </servers>
      </settings>
  
  * Administration rights on the GeoTools sourceforge site.
    
    This will be required to upload the finished files and to create the download page for the
    release.
    
    First make sure you have a sourceforge account. You can create an account if you need to.
    Then you need to join the geotools project and ask one of the other administrators to give
    you administration rights on the project.

  * Administration rights to Geotools Confluence, JIRA and codehaus xircles.
    
    The JIRA authority is required to create a new release number for the future, to bump all
    unfinished tasks to that future release, and thereby to collect a list of changes in the
    current release.
    
    The Confluence edit permission is required to make a release page.
    
    The codehaus xircles login is now required for the previous two. See the bottom of the
    GeoTools wiki for instructions on how to obtain these permissions.

Sanity Check
^^^^^^^^^^^^^

1. Check that the build is currently working.
   
   You can check the build status on hudson here:
   
   * http://hudson.opengeo.org/hudson/view/geotools/
   

2. Email the List
   
   Email the geotools-devel list that a release is underway; there may be a developer
   in the middle of something who will ask you to hold off for a couple of hours.

3. Sanity Check of Codebase
   
   The code tree used for the build should be up to date and clean of any local modifications.
   This can be done by doing an update and making sure there are no significant local changes.
  
   First, change the default directory according the release to be performed:
   
   ================= =================================================
   GeoTools 8.x      http://svn.osgeo.org/geotools/trunk
   GeoTools 2.7.x    http://svn.osgeo.org/geotools/branches/2.7.x
   GeoTools 2.6.x    http://svn.osgeo.org/geotools/branches/2.6.x
   GeoTools 2.5.x    http://svn.osgeo.org/geotools/branches/2.5.x
   GeoTools 2.2.x    http://svn.osgeo.org/geotools/branches/2.2.x
   ================= =================================================
   
   The default directory should contains the main pom.xml file. Then update the local repository
   and note the revision number returned by svn update (you will need it later).::
     
     C:\java\geotools> cd 2.7.x
     C:\java\geotools\2.7.x> svn up
   
   We can then check the svn status which should immediately return with a blank line.::
      
      C:\java\geotools\2.7.x>svn status
   
   If there are local modifications or files that svn does not track, you may get something
   resembling the following::
      
      $ svn status
      ?      modules/plugin/arcsde/.classpath
      ?      modules/plugin/arcsde/.project
      ?      modules/plugin/arcsde/.settings
   
   These modifications should not hurt, but you may want to wipe them out for safety. If there is
   pending modifications in files tracked by svn, you should commit them (after making sure that
   they do not break the build) before to continue. Take note again of the new revision number
   given by svn.

4. Test extensively the code base
   
   This is to make sure there are no previously built classes still hanging around.::
     
     mvn clean
   
   
   This is the build that will stress the library as much as possible to see if we can break
   things. The interactive.tests profile will open and close a series of windows as part of its
   work.::
      
      mvn -Pextensive.tests,interactive.tests install
   
   These tests go beyond the normal nightly build tests; so there is every chance that they
   will turn up a failure. If so go to the email list and arrange for a fix.

   * Hint - check your network settings
     
     If you're connected to the internet through DHCP and the DHCP server is providing your 
     hostname, ensure your hostname matches the one in /etc/hostname and you have an entry in for
     it in /etc/hosts, or the coverage module tests will fail.
   
   If there is any build or test failure, report the errors on the geotools developers mailing
   list (attach the appropriate file from the target/surefire-reports directory to the email) and
   wait until those errors are fixed. Then perform a new svn update, note the revision number and
   try again the above-cited mvn install command.

Preparing the Release
^^^^^^^^^^^^^^^^^^^^^
   
The release preparation will create a tag, change the version numbers, build the library and
perform the full testing of the library to set everything up for release.
   
1. Create the branch (if needed) and the tag
   
   The tag name must be the same than the GeoTools version to be released. The following
   instructions assume that we are releasing GeoTools 2.6-M2. For other releases, update the
   version number accordingly.
   
    You will need the revision number from latest svn update. The following examples use revision
    number 24652, but this number needs to be replaced by the one provided by svn. We use the
    revision number in order to create a branch or a tag from the revision tested above. If some
    changes were commited after the latest successful mvn install, they will not be part of this
    release.

2. Depending on the release you are performing choose the appropriate svn commands below.
   
   * Releasing a milestone from trunk:
     
     Creates the tag directly from the trunk::
       
       svn copy http://svn.osgeo.org/geotools/trunk
                http://svn.osgeo.org/geotools/tags/8.0-M2
                -m "Created 8.0-M2 tag from revision 33797." -r 33797
     
     The reason we are using -r 33797 is so we don't get tripped up by anyone who committed while
     between when we tested trunk and now.

   * Releasing first release candidate
     
     First creates the branch::
        
        svn copy http://svn.osgeo.org/geotools/trunk
                 http://svn.osgeo.org/geotools/branches/8.x
                 -m "Created 8.x branch from revision trunk 34232." -r 34232
     
     Then the tag::
        
        svn copy http://svn.osgeo.org/geotools/branches/8.x
                 http://svn.osgeo.org/geotools/tags/8.0-RC0
                 -m "Created 8.0-RC0 release candidate"
     
   * Releasing additional releases from a branch (release candidate, release or patch):
     
     Creates an additional release candidate from the branch::
       
       svn copy http://svn.osgeo.org/geotools/branches/8.x
                http://svn.osgeo.org/geotools/tags/8.0-RC1
                -m "Created 8.0-RC1 release candidate" -r 37232

     Creates a release::
       
       svn copy http://svn.osgeo.org/geotools/branches/8.x
                http://svn.osgeo.org/geotools/tags/8.0.0
                -m "Created 8.0.0 release" -r 37232

     Creates a patch release for bug fixes::
       
       svn copy http://svn.osgeo.org/geotools/branches/8.x
                http://svn.osgeo.org/geotools/tags/8.0.1
                -m "Created 8.0.1 patch release" -r 37232

     Creates a dot release for api or feature addition::
       
       svn copy http://svn.osgeo.org/geotools/branches/8.x
                http://svn.osgeo.org/geotools/tags/8.1.0
                -m "Created 8.1.0 patch release" -r 37232

   * The odd scenario out is creating a patch release from a previous tag after the branch
     has already moved on.
        
     This only occurs via customer request and is an unusual circumstance.
    
       svn copy http://svn.osgeo.org/geotools/tags/8.0.1
                http://svn.osgeo.org/geotools/tags/8.0.2
                -m "Created 8.0.2 patch release" -r 37232
       
     We would then expect the developer to apply the required fixes to this tag
     and issue a release.

       svn commit -m "Released 8.0.2 patch release" -r 37232

3. Verify that the tag has been correctly created by visiting the repository web page:
   
   *  http://svn.osgeo.org/geotools/tags/

Change version number
^^^^^^^^^^^^^^^^^^^^^

1. After you have created the tag in the previous section please change over to it for release.
   
   * You can just grab a fresh checkout::
       
       cd ../tags
       svn checkout http://svn.osgeo.org/geotools/tags/8.0-M3/
       cd 8.0-M3
   
   * Our just "switch" your existing directory to the new tag::
       
       svn switch http://svn.osgeo.org/geotools/tags/8.0-M3

2. From this point, all remaining operations in this page should be performed from this tag
   directory. You should not need to change directories to a different checkout anymore.

3. The next step is to replace every occurrences of 2.6-SNAPSHOT by 2.6-M2 in all pom.xml files.
   
   An ant file can be found in build/rename.xml to update the pom.xml files and the GeoTools.java
   file to the new version number.

4. Type the following from the command line.::
     
     C:\java\geotools\2.6-M2>copy build\rename.xml .

5.  Edit the rename.xml file - changing 2.6-SNAPSHOT and 2.6-M2 version numbers to match the
    release you are making.
    
    This script mostly updates pom.xml files, and the GeoTools.java class (which is used by
    applications to check the version of GeoTools they are working with at runtime).:
    
   .. literalinclude:: /../../build/rename.xml
      :language: xml

6. Run the file (the default build target will update the pom.xml files)::
     
     C:\java\geotools\2.6-M2>ant -f rename.xml

6. Run the file with the "doc" build target to update the tutorials::
     
     C:\java\geotools\2.6-M2>ant -f rename.xml doc

7. Check that the version numbers were updated as expected::
     
     svn status
     svn diff

8. Do not commit yet. We will commit only after a successful build from the tag directory.
   
   * Fixing number change error
     
     If the numbers are not changed like expected (for example because of a mistake while using
     sed), revert the changes::
       
       svn revert . --recursive
     
     Then run again sed or ant, and check with svn diff.

9. If you're releasing from a stable branch, the branch version numbers must be updated as well.
   
   If, for example, the branch version number was 2.7.x, you'll have to alter all pom file to
   state 2.7-SNAPSHOT instead.
   
   1. enter the branch checkout directory;
   2. rename the pom.xml files as required (adapting rename.xml as required)
   3. check the rename was done properly;
   4. commit back to the branch
   5. go back to the tag checkout

Get those SNAPSHOT dependencies out of our build
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. BEFORE (under root pom.xml dependency management tag)::
       
       <dependency>
         <groupId>jfree</groupId>
         <artifactId>eastwood</artifactId>
         <version>1.1.1-SNAPSHOT</version>
       </dependency>
   
2. AFTER::
       
       <dependency>
         <groupId>jfree</groupId>
         <artifactId>eastwood</artifactId>
         <version>1.1.2</version>
       </dependency>

Build and Test Extensively
^^^^^^^^^^^^^^^^^^^^^^^^^^

1. We already tested the library before to create the tag, so testing again here is more a safety
   measure; it should give the same result.::
      
      mvn -Pextensive.tests,interactive.tests install

2. In case of build failure, report to the Geotools developers mailing list as we did for the
   build on trunk.

3. If the build is successful, commit the pending version number changes. We commit only now
   because the tests after the version number change may reveal some bad configurations in
   pom.xml files, which should be fixed before the commit.::
      
      svn status
      svn commit -m "Changed version number from 2.6-SNAPHOT to 2.6-M2."

Update the README
^^^^^^^^^^^^^^^^^

1. The skeleton README contains tags which must be updated to reflect the release.
   
   These tags include:
   
   ============== ====================================================================
   @VERSION@      geotools version
   @DATE@         release date
   ============== ====================================================================

2. Substitute the appropriate values for in the file and then commit it. Remember, you are
   committing to the tag and not the development branch.::
      
      svn status
      svn commit -m "Updated README for 2.6-M2."

Performing the Release
^^^^^^^^^^^^^^^^^^^^^^

1. Please deploy the release to maven; including unsupported modules::
      
      mvn deploy -DskipTests -Dall
   
2. You can verify during the process if the files are uploaded as expected by watching the
   repository web page (the Geotools metadata module should be among the first ones to be
   uploaded).        * http://downloads.osgeo.org/geotools/
   
3. Not Authorized
   
   If you run into problems deploying the jars to http://downloads.osgeo.org/geotools::
      
      [INFO] ------------------------------------------------------------------------
      [ERROR] BUILD ERROR
      WARNING: No credentials available for the 'GEOTOOLS' authentication realm at
      downloads.osgeo.org 
   
   Ensure that you have provided the correct credentials in your **settings.xml** file.
   
4. If this step fails for some other reason after getting started, it may be caused by
   some network problem. Try running "mvn deploy" again.

Assemble the Distribution Archives
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Now we need to create the binary, source and javadoc archives that users can download.

1. Since we do not want to include the unsupported jars produced by -Dall we need to::
      
      mvn clean -Dall
      mvn install -DskipTests

2. Assemble the bin and source Archives
   
   We use the maven assembly plug-in to do this for us.::
      
      mvn -DskipTests assembly:assembly

3. If you look in target/ directory, you will see source and binary zip files.

4. Remove unnecessary jars from the bin archive. This includes velocity and junit jars, as well
   as any jdbc driver stubs.::
     
     cd target
     unzip geotools-2.6-M4-bin.zip
     cd geotools-2.6-M4
     rm junit*.jar
     rm *dummy-*
     cd ..
     rm geotools-2.6-M4-bin.zip
     zip -r geotools-2.6-M4-bin.zip geotools-2.6-M4
     rm -rf geotools-2.6-M4

5. Assemble the javadocs
   
   This will use the standard Maven javadoc plugin to create the javadocs. The javadoc build
   uses Java 5 constructs, thus it is recommanded to build it with a more recent JDK than 1.4.
   
   The build creates a slew of warnings (13000+) and errors (100) and may exits with an error
   code. Nonetheless, the build produces the document tree. If you experience building problems,
   check out the GeoTools javadoc page too.::
      
      cd modules
      mvn javadoc:aggregate

5. Bundle the files by hand::
      
      cd target/site/
      zip -9 -r ../../../target/geotools-2.7-M1-doc.zip apidocs/
      cd ../../..

6. The javadoc plugin usage and configuration is explained in more details there.

7. Generate sphinx documentation::
      
      cd docs
      mvn install
      cd target/user
      zip -9 -r ../../../target/geotools-2.6.5-userguide.zip html/

Test the src release
^^^^^^^^^^^^^^^^^^^^

If you like you can ask on the developer list for a volunteer to perform these steps.

1. Unzip the geotools-2.6-M4-bin.zip archive to a clean directory.

2. Move or rename your ``.m2/repository`` directory.
   
   * We do this in case a required module was accidentally excluded from the list of modules to
     be included in the release. You would see no error during the release process but the
     generated src archive would not be buildable.

3. Run maven from the root of the directory you unzipped to::
      
      mvn install

Export out the User Guide from the wiki
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Login to confluence and visit the user guide: http://docs.codehaus.org/display/GEOTDOC/Home
2. go to the Browse Space > Advanced
3. Choose Export Space
4. Select HTML and don't include the comments
5. It will take a few moments for the zip file to be ready
6. Rename this zip to gt-2.6-M4-guide.zip for later upload

Update JIRA and create a changelog
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Any unresolved issues that did not make the release version need to be bumped back to the next release. Fortunately, JIRA allows you to do this en masse:

1. Login to Jira and head to the administration screen for the GeoTools project
2. Click on manage versions
3. Create the next release number (after 2.6-M4 we would have 2.6-M5)
4. Release 2.6-M4
   
   * it will ask you where you want to move unresolved issues
     select the next release number you created above.
     
     In this example 2.6-M5

5.  This will update the changelog file to show what has been fixed since the last release.
    
6.  For example, see:
    
    http://jira.codehaus.org/browse/GEOT#selectedTab=com.atlassian.jira.plugin.system.project%3Achangelog-panel

7. From that page you can click on the "release notes" link; you will be using these
   release notes when you make an announcement.

Upload Distribution to SourceForge
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

SourceForge now allows various methods for uploading files:

* web form (uploads should be less than 20 megs)
* webdav (uploads should be less than 20 megs)
* sftp
* rsynch + ssh

Details on how to use them can be found here:

* http://sourceforge.net/apps/trac/sourceforge/wiki/Release%20files%20for%20download

1. Command-line client on Linux using sftp::
     
     sftp youUserName@frs.sourceforge.net
     cd /home/frs/project/g/ge/geotools
     cd "GeoTools 2.6 Releases"
     mkdir 2.6-M4
     cd 2.6-M4
     put geotools*.zip

2. As the last sanity check email the geotools list and ask people to try it out.

Update the Downloads Page
^^^^^^^^^^^^^^^^^^^^^^^^^

1. Navigate to the Downloads Page; and choose the download page for the version you are releasing
2. Press Add Child Page
3. Enter in the title of the release (it is important to use '.' and '-' correctly for the sorting order)
   
   ======= ==================================================================================
   2.7-M4  Milestone release - adding a planned feature from a RnD branch
   2.7-RC2 Release candidate - feature complete, waiting on fixes, docs and QA checks
   2.7.0   Major release - formal release we API committed to supporting
   2.2.1   Patch release - remember that support? this is an examples of fixes
   ======= ==================================================================================

4. Press the 'select a template page' link and choose Geotools Release from the list
5. Press next to view the generated page
6. You will need to correct the following information:
   
   * Update the date (between the excerpt macros).
   * Update the Source forge links above to reflect the release by following this link
   * Update the Release Notes by choosing the the correct version from this link.
   * Fill in a blurb about the release
   * List any completed proposals or interesting new features provided by the release

Tell the World
^^^^^^^^^^^^^^

After the list has had a chance to try things out - make an announcement.

Here is an example; we try to include download links to the release artifacts, the release notes
and documentation associated with the release.::
   
   The GeoTools 2.6-M4 release is now available for download:
   
   geotools-2.6-M4-bin.zip
   geotools-2.6-M4-project.zip
   geotools-2.6-M4-doc.zip
   geotools-2.6-M4-welcome.zip
   geotools-2.6-M4-guide.zip
   
   This is a bug fix release made in conjunction with uDig 1.2-RC3.
   
   This release adds support for Oracle Georaster access as the result of a productive
   collaboration between Christian and Baskar. It is great to see developers from different
   organisations combine forces.
   
   There are many small but interesting improvements in the release notes. I am exited by the
   new interpolate functions which will be very useful when styling maps, generated SLD files
   no longer write out "default" values which will make for a more readable result.
   
   For more information please review the Release Notes:
   
   http://jira.codehaus.org/secure/ConfigureReleaseNote.jspa?projectId=10270&version=16316
   
   http://geotools.org
   http://docs.codehaus.org/display/GEOTOOLS/2.6.x
   http://docs.codehaus.org/display/GEOTOOLS/Upgrade+to+2.6
   
   Enjoy,
   The GeoTools Community

1. geotools-devel@lists.sourceforge.net
   
   * To: geotools-devel@lists.sourceforge.net
   * Subject: 2.6-M4 Released
   
2. http://geotoolsnews.blogspot.com/
   
   Sign in and make a new blog post.
   
   * News Title: GeoTools 2.6-M4 released
   * Content: allows wiki links 

3. geotools-gt2-users@lists.sourceforge.net
   
   Let the user list know:
   
   * To: geotools-gt2-users@lists.sourceforge.net
   * Subject: GeoTools 2.6-M4 Released

4. Open Source Geospatial Foundation
   
    Only to be used for "significant" releases (Major release only, not for milestone
    or point releases)
    
    https://www.osgeo.org/content/news/submit_news.html
    
5. Post a message to the osgeo news email list (you are subscribed right?)
   
   * To: news_item@osgeo.org
   * Subject: GeoTools 2.6-M4 Released

Tell More of the World!
^^^^^^^^^^^^^^^^^^^^^^^

Well that was not very much of the world was it? Lets do freshmeat, sf.net, geotools.org and freegis.

1. Do it in the Morning
   
   Please don't announce releases on a Friday or weekend. And try to make it in the mornings as
   well. If it's late then just finish it up the next day. This will ensure that a lot more
   people will see the announcements.
  
   http://freshmeat.net/projects/geotools/

2. Add release: http://freshmeat.net/projects/geotools/
   
   * Branch: GT2
   * Version: 2.6-M4
   * Changes: Grab the notes from the above release anouncement
   * You can also update the screen snapshot to reflect a current GeoTOols application.
     GeoServer and UDIG have been highlighted in the past. If you are making the release
     to support a project this is your big chance!

3. http://sourceforge.net/
   
   * Add a news article: http://sourceforge.net/news/submit.php?group_id=4091
   * Subject: GeoTools 2.6-M4 Released
   * Details: allows http links
   * The format of the subject is important it gets the message included on the
     http://sourceforge.net/ Home Page.
     
     This is a one shot deal, if you go back and fix any mistakes it is kicked off the Home Page.

4. http://freegis.org/
   
   Email Jan-Oliver Wagner
   
   * To: jan@intevation.de
   * Subject: GeoTools update for FreeGIS site

5.  http://java.net/
    
    Submit a news article
    
    * Use form at: http://today.java.net/cs/user/create/n
    * Source: geotools.org
    * URL: http://geotools.org/
    * Link to article: http://geotools.org/News
    * Note Membership required

5. http://slashgisrs.org/
   
   Submit a news article
   
   * Use form at: http://slashgisrs.org/submit.pl (gotta login!)
   * Use your profile page (example: http://docs.codehaus.org/display/~jive) for Home page
   * Section: Technology Topic: Open Source Community
   * Warning: You may wish to change to HTML Formatted, and insert a few links in!
