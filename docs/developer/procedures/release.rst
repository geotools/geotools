.. _release_guide:

Release Guide
=============

This guide details the process of performing a GeoTools release.   

Before you start
----------------

Notify developer list
^^^^^^^^^^^^^^^^^^^^^

It is good practice to notify the `GeoTools developer list <https://lists.sourceforge.net/lists/listinfo/geotools-devel>`_ of the intention to make the release a few days in advance, even though the release date has been agreed upon before hand. 

Prerequisites
-------------

The following are necessary to perform a GeoTools release:

#. Commit access to the GeoTools `Git repository <https://Github.com/geotools/geotools>`_
#. Build access to `Hudson <http://hudson.opengeo.org/hudson>`_
#. Edit access to the GeoTools `Blog <http://www.blogger.com/blogger.g?blogID=5176900881057973693#overview>`_
#. Administration rights to `GeoTools JIRA <https://jira.codehaus.org/browse/GEOT>`__
#. Release/file management privileges in `SourceForge <https://sourceforge.net/projects/geotools/>`_

Versions and revisions
----------------------

When performing a release we don't require a "code freeze" in which no developers can commit to the repository. Instead we release from a revision that is known to pass all tests, including unit/integration tests as well as CITE tests on the GeoServer side. These instructions are valid in case you are making a release in combination with GeoServer, if you are making a stand alone release it's up to you to choose the proper GIT revision number for the GeoTools released to be picked from.

To obtain the GeoServer and Geotools revisions that have passed the `CITE test <http://hudson.opengeo.org/hudson/view/cite/>`_, navigate to the latest Hudson run of the CITE test  and view it's console output and select to view its full log. For example::
	
	 http://hudson.opengeo.org/hudson/view/cite/job/cite-wfs-1.1/813/consoleFull
	
Perform a search on the log for 'Git revision' and you should obtain the following.::

	version = 2.2-SNAPSHOT
	Git revision = 4ea8d3fdcdbb130892a03f27ab086068b95a3b01
	Git branch = 4ea8d3fdcdbb130892a03f27ab086068b95a3b01
	build date = 03-Aug-2012 03:39
	geotools version = 8-SNAPSHOT
	geotools revision = 73e8d0746a4527e46a46e5e8bc778ca92ca89130
	
Since we don't make any release from master, ensure you select the right CITE test that passed to obtain the right revision.	

Release in JIRA
---------------

Run the `geotools-release-jira <http://hudson.opengeo.org/hudson/job/geotools-release-jira/>`_ job in Hudson. The job takes the following parameters:

**VERSION**

  The version to release, same as in the previous section. This version must match a version in JIRA.

**NEXT_VERSION**

  The next version in the series. All unresolved issues currently fils against ``VERSION`` will be transitioned to this version.

**JIRA_USER** 

  A JIRA user name that has release privileges. This user  will be used to perform the release in JIRA, via the SOAP api.

**JIRA_PASSWD**

  The password for the ``JIRA_USER``.
     
This job will perform the tasks in JIRA to release ``VERSION``. Navigate to `JIRA <http://jira.codehaus.org/browse/GEOT>`_ and verify that the version has actually been released.

Build the Release
-----------------

Run the `geotools-release <http://hudson.opengeo.org/hudson/job/geotools-release/>`_ job in Hudson. The job takes the following parameters:

**BRANCH**

  The branch to release from, "8.x", "9.x", etc... This must be a stable branch. Releases are not performed from master.
     
**REV**

  The Git revision number to release from. eg, "24ae10fe662c....". If left blank the latest revision (ie HEAD) on the ``BRANCH`` being released is used.
  
**VERSION**
   
  The version/name of the release to build, "8.5", "9.1", etc...
  
**GIT_USER**

  The Git username to use for the release.

**GIT_EMAIL**

  The Git email to use for the release.	 
     
This job will checkout the specified branch/revision and build the GeoTools
release artifacts. When successfully complete all release artifacts will be 
uploaded to the following location::

   http://gridlock.opengeo.org/geotools/release/<RELEASE> 

Test the Artifacts
------------------

Download and try out some of the artifacts from the above location and do a 
quick smoke test that there are no issues. Engage other developers to help 
test on the developer list.

In particular, you can downlad the source artifacts and build them locally on an empty Maven repository to make sure
any random user out there can do the same.
A simple way to do so is:

*  Unpack the sources
*  Temporarily move the ``$HOME/.m2/repository`` to a different location, so that Maven will be forced to build from an empty repo
*  Do a full build using ``mvn install -Dall -T1C``
*  On a successfull build, delete ``$HOME/.m2/repository`` and restore the old maven repository backed up at the beginning
 
Publish the Release
-------------------

Run the `geotools-release-publish <http://hudson.opengeo.org/hudson/job/geotools-release-publish/>`_ in Hudson. The job takes the following parameters:

**VERSION** 

  The version being released. The same value s specified for ``VERSION`` when running the ``geoserver-release`` job.
  
**BRANCH** 

  The branch being released from.  The same value specified for ``BRANCH`` when running the ``geoserver-release`` job.

**GIT_USER**

  The Git username to use for the release.

**GIT_EMAIL**

  The Git email to use for the release.	 


This job will rsync all the artifacts located at::

     http://gridlock.opengeo.org/geotools/release/<RELEASE>

to the SourceForge FRS server. Navigate to `Sourceforge <http://sourceforge.net/projects/geotools/>`__ and verify that the artifacts have been uploaded properly.


Announce the Release
--------------------

Anounce on GeoTools Blog
^^^^^^^^^^^^^^^^^^^^^^^^

1. Navigate to the GeoTools blog; and sign in: http://geotoolsnews.blogspot.com/
2. Create a new blog post anouncing your release; you can cut and paste the following as a starting
   point::
   
        The GeoTools community is pleased to announce the availability of GeoTools 8.0-M3 for <a
        href="https://sourceforge.net/projects/geotools/files/GeoTools%208.0%20Releases/8.0-M3/">download
        from source forge</a>:
        <ul>
        <li><a href="http://sourceforge.net/projects/geotools/files/GeoTools%208.0%20Releases/8.0-M3/geotools-8.0-M3-bin.zip/download">geotools-8.0-M3-bin.zip</a></li>
        <li><a href="http://sourceforge.net/projects/geotools/files/GeoTools%208.0%20Releases/8.0-M3/geotools-8.0-M3-doc.zip/download">geotools-8.0-M3-doc.zip</a></li>
        <li><a href="http://sourceforge.net/projects/geotools/files/GeoTools%208.0%20Releases/8.0-M3/geotools-8.0-M3-userguide.zip/download">geotools-2.7.3-userguide.zip</a></li>
        <li><a href="http://sourceforge.net/projects/geotools/files/GeoTools%208.0%20Releases/8.0-M3/geotools-8.0-M3-project.zip/download">geotools-8.0-M3-project.zip/download</a></li>
        </ul>
        If you are using Maven this release is deployed to our OSGeo Maven Repository: For more information on setting up your project with Maven
        see the <a href="http://docs.geotools.org/latest/userguide/tutorial/quickstart/index.html">Quickstart</a> (included
        in the userguide documentation pack above).
        <br/>
        <br/>
        This is a milestone release made in conjunction with the (OPTIONAL LINK TO PRODUCT, CONFERENCE).
        <br/>
        <br/>BLURB ABOUT RELEASE CONTENTS<br/>
        <ul>
        <li>THANKS LINKING TO ANY BLOG POSTS, DOCS OR JIRA</li>
        <li>THANKS LINKING TO ANY BLOG POSTS, DOCS OR JIRA</li>
        <li></li>
        <li>And XX more
        in the <a href="https://jira.codehaus.org/secure/ReleaseNote.jspa?projectId=10270&version=17864">GeoTools
        8.0-M3  Release Notes</a></li>
        </ul>
        Finally thanks to YOU and ORGANISATION for putting this release out.
        
        <br/>Enjoy,
        <br/>The GeoTools Community
        <br/><a href="http://geotools.org/">http://geotools.org</a>

6. You will need to correct the following information:
   
   * Update the Source forge links above to reflect the release by following this link
   * Update the Release Notes by choosing the the correct version from `JIRA changelogs <https://jira.codehaus.org/browse/GEOT#selectedTab=com.atlassian.jira.plugin.system.project:changelog-panel&allVersions=false>`_
   * Fill in the BLURB ABOUT RELEASE CONTENTS
   * Thank those involved with the relese (listing any completed proposals, docs or jira items)
  
Tell the World
^^^^^^^^^^^^^^

After the list has had a chance to try things out - make an announcement.

Cut and paste from the blog post to the following:

1. geotools-devel@lists.sourceforge.net
   
   * To: geotools-devel@lists.sourceforge.net
   * Subject: 8.0-RC1 Released
   
2. geotools-gt2-users@lists.sourceforge.net
   
   Let the user list know:
   
   * To: geotools-gt2-users@lists.sourceforge.net
   * Subject: GeoTools 8.0-RC1 Released

3. Open Source Geospatial Foundation
   
    Only to be used for "significant" releases (Major release only, not for milestone
    or point releases)
    
    https://www.osgeo.org/content/news/submit_news.html
    
4. Post a message to the osgeo news email list (you are subscribed right?)
   
   * To: news_item@osgeo.org
   * Subject: GeoTools 8.0-RC1 Released

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
   * You can also update the screen snapshot to reflect a current GeoTools application.
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
   
   * Use form at: http://slashgeo.org/ (gotta login!)
   * Use your profile page (example: http://docs.codehaus.org/display/~jive) for Home page
   * Section: Technology Topic: Open Source Community
   * Warning: You may wish to change to HTML Formatted, and insert a few links in!
