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
#. Build access to `Jenkins <http://ares.boundlessgeo.com/jenkins/>`_
#. Edit access to the GeoTools `Blog <http://www.blogger.com/blogger.g?blogID=5176900881057973693#overview>`_
#. Administration rights to `GeoTools JIRA <https://osgeo-org.atlassian.net/projects/GEOT/>`__
#. Release/file management privileges in `SourceForge <https://sourceforge.net/projects/geotools/>`_

Versions and revisions
----------------------

When performing a release we don't require a "code freeze" in which no developers can commit to the repository. Instead we release from a revision that is known to pass all tests, including unit/integration tests as well as CITE tests on the GeoServer side. These instructions are valid in case you are making a release in combination with GeoServer, if you are making a stand alone release it's up to you to choose the proper GIT revision number for the GeoTools released to be picked from.

To obtain the GeoServer and Geotools revisions that have passed the `CITE test <http://ares.boundlessgeo.com/jenkins/view/geoserver-cite/>`_, navigate to the latest Jenkins run of the CITE test  and view it's console output and select to view its full log. For example::
	
	 http://ares.boundlessgeo.com/jenkins/view/geoserver-cite/job/2.4-cite-wfs-1.1/8/consoleText
	
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

1. Navigate to the `GeoTools project page <https://osgeo-org.atlassian.net/projects/GEOT?selectedItem=com.atlassian.jira.jira-projects-plugin:release-page&status=released-unreleased>`_ in JIRA.

2. Click ``Manage Versions``. Create a new version for the next version to be released after the current release (For example, if you are releasing GeoTools 15.1, create version 15.2).

3. Return to the project page, and click on the version you are releasing. Click the ``Release`` button, and enter the release date when prompted. If there are still unsolved issues remaining in this release, you will be prompted to move them to an unreleased version. If so, choose the new version you created in step 2.

If you are cutting the first RC of a series, create the stable branch
---------------------------------------------------------------------

When creating the first release candidate of a series, there are some extra steps to create the new stable branch and update the version on master.

* Checkout the master branch and make sure it is up to date and that there are no changes in your local workspace::

    git checkout master
    git pull
    git status

* Create the new stable branch and push it to GitHub; for example, if master is ``13-SNAPSHOT`` and the remote for the official GeoTools is called ``geotools``::

    git checkout -b 13.x
    git push geotools 13.x

* Checkout the master branch and update the version in all pom.xml files and a few miscellaneous files; for example, if changing master from ``13-SNAPSHOT`` to ``14-SNAPSHOT``::

    git checkout master
    find . -name pom.xml -exec sed -i 's/13-SNAPSHOT/14-SNAPSHOT/g' {} \;
    sed -i 's/13-SNAPSHOT/14-SNAPSHOT/g' \
        build/rename.xml \
        docs/build.xml \
        docs/common.py \
        docs/user/tutorial/quickstart/artifacts/pom2.xml \
        modules/library/metadata/src/main/java/org/geotools/factory/GeoTools.java

* Commit the changes and push to the master branch on GitHub::

      git commit -am "Updated version to 14-SNAPSHOT"
      git push geotools master
      
* Create the new beta version in `JIRA <https://osgeo-org.atlassian.net/projects/GEOT>`_ for issues on master; for example, if master is now ``14-SNAPSHOT``, create a Jira version ``14-beta`` for the first release of the ``14.x`` series

* Update the jobs on ares:
  
  * disable the maintenance jobs, and remove them from the geotools view
  * create new jobs, create from the exsisting master jobs, editing the branch and the DIST=stable configuration
  * edit the previous stable branch, changing to DIST=maintenance

* Announce on the developer mailing list that the new stable branch has been created and that the feature freeze on master is over

Build the Release
-----------------

Run the `geotools-release <http://ares.boundlessgeo.com/jenkins/job/geotools-release/>`_ job in Jenkins. The job takes the following parameters:

**BRANCH**

  The branch to release from, "8.x", "9.x", etc... This must be a stable branch. Releases are not performed from master, with the notable exception of beta releases, which are indeed cut from master.
     
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

   http://ares.boundlessgeo.com/geotools/release/<RELEASE> 

Test the Artifacts
------------------

Download and try out some of the artifacts from the above location and do a 
quick smoke test that there are no issues. Engage other developers to help 
test on the developer list.

In particular, you can downlad the source artifacts and build them locally on an empty Maven repository to make sure
any random user out there can do the same.
A simple way to do so is:

*  Unpack the sources
*  Temporarily move the ``$HOME/.m2/repository`` to a different location, so that Maven will be forced to build from an empty repo. If you don't want to fiddle with your main repo just use ``mvn -Dmaven.repo.local=/tmp/m2 install -Dall -T1C`` where it points to any empty directory.
*  Do a full build using ``mvn install -Dall -T1C``
*  On a successfull build, delete ``$HOME/.m2/repository`` and restore the old maven repository backed up at the beginning
 
Publish the Release
-------------------

Run the `geotools-release-publish <http://ares.boundlessgeo.com/jenkins/job/geotools-release-publish/>`_ in Jenkins. The job takes the following parameters:

**VERSION** 

  The version being released. The same value specified for ``VERSION`` when running the ``geotools-release`` job.
  
**BRANCH** 

  The branch being released from.  The same value specified for ``BRANCH`` when running the ``geotools-release`` job.

**GIT_USER**

  The Git username to use for the release.

**GIT_EMAIL**

  The Git email to use for the release.


This job will rsync all the artifacts located at::

     http://ares.boundlessgeo.com/geotools/release/<RELEASE>

to the SourceForge FRS server, and also deploy the artifacts to the public geotools maven repository.

#. Navigate to `Sourceforge <http://sourceforge.net/projects/geotools/>`__ and verify that the artifacts have been uploaded properly.
#. If this is the latest stable release, make its ``-bin.zip`` the default download for all platforms (use the "i" button).

Announce the Release
--------------------

Announce on GeoTools Blog
^^^^^^^^^^^^^^^^^^^^^^^^^

#. Navigate to Blogspot and sign in: http://blogspot.com/
#. Select the GeoTools blog from the list (if not listed, get someone to add you)
#. Create a new blog post anouncing your release; copy and paste a previous blog post preserving series information unless this is the first of a new series
#. You will need to correct the following information: 

   * Update the Sourceforge links above to reflect the release
   * Update the Release Notes by choosing the the correct version from `JIRA changelogs <https://osgeo-org.atlassian.net/projects/GEOT?selectedItem=com.atlassian.jira.jira-projects-plugin:release-page>`_
   * For a new stable series, be sure to thank those involved with the release (testing, completed proposals, docs, and so on)

#. The public entry point will be here: http://geotoolsnews.blogspot.com/
  
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
