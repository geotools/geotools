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
#. Build access to `Jenkins <https://build.geoserver.org>`_
#. Edit access to the GeoTools `Blog <http://www.blogger.com/blogger.g?blogID=5176900881057973693#overview>`_
#. Administration rights to `GeoTools JIRA <https://osgeo-org.atlassian.net/projects/GEOT/>`_
#. Release/file management privileges in `SourceForge <https://sourceforge.net/projects/geotools/>`_

Versions and revisions
----------------------

When performing a release we don't require a "code freeze" in which no developers can commit to the repository. Instead we release from a revision that is known to pass all tests, including unit/integration tests as well as CITE tests on the GeoServer side. These instructions are valid in case you are making a release in combination with GeoServer, if you are making a stand alone release it's up to you to choose the proper GIT revision number for the GeoTools released to be picked from.

To obtain the GeoServer and Geotools revisions that have passed the `CITE test <https://build.geoserver.org/view/testing-cite/>`_, navigate to the latest Jenkins run of the CITE test  and view it's console output and select to view its full log. For example:

    https://build.geoserver.org/job/2.11-cite-wms-1.1/286/consoleText

Perform a search on the log for 'git revision' (this is the GeoServer revision) and you should obtain the following:

.. code-block:: none

    version = 2.11-SNAPSHOT
    git revision = 08f43fa77fdcd0698640d823065b6dfda7f87497
    git branch = origin/2.11.x
    build date = 18-Dec-2017 19:51
    geotools version = 17-SNAPSHOT
    geotools revision = a91a88002c7b2958140321fbba4d5ed0fa85b78d
    geowebcache version = 1.11-SNAPSHOT
    geowebcache revision = 0f1cbe9466e424621fae9fefdab4ac5a7e26bd8b/0f1cb

Since we don't make any release from master, ensure you select the right CITE test that passed to obtain the right revision.

Release in JIRA
---------------

1. Navigate to the `GeoTools project page <https://osgeo-org.atlassian.net/projects/GEOT?selectedItem=com.atlassian.jira.jira-projects-plugin:release-page&status=released-unreleased>`_ in JIRA.

2. Add a new version for the next version to be released after the current release. For example, if you are releasing GeoTools 17.5, create version 17.6.

3. Click in the Actions column for the version you are releasing and select Release. Enter the release date when prompted. If there are still unsolved issues remaining in this release, you will be prompted to move them to an unreleased version. If so, choose the new version you created in step 2.

If you are cutting the first RC of a series, create the stable branch
---------------------------------------------------------------------

.. note:: The RC is the first release of a series, released one month before the .0 release. This replaces the beta release, which no longer exists.

When creating the first release candidate of a series, there are some extra steps to create the new stable branch and update the version on master.

* Checkout the master branch and make sure it is up to date and that there are no changes in your local workspace::

    git checkout master
    git pull
    git status

* Create the new stable branch and push it to GitHub; for example, if master is ``17-SNAPSHOT`` and the remote for the official GeoTools is called ``geotools``::

    git checkout -b 17.x
    git push geotools 17.x

* Enable `GitHub branch protection <https://github.com/geotools/geotools/settings/branches>`_ for the new stable branch: tick "Protect this branch" (only) and press "Save changes".

* Checkout the master branch and update the version in all pom.xml files and a few miscellaneous files; for example, if changing master from ``17-SNAPSHOT`` to ``18-SNAPSHOT``::

    git checkout master
    find . -name pom.xml -exec sed -i 's/17-SNAPSHOT/18-SNAPSHOT/g' {} \;
    sed -i 's/17-SNAPSHOT/18-SNAPSHOT/g' \
        build/rename.xml \
        docs/build.xml \
        docs/common.py \
        docs/user/artifacts/xml/pom3.xml \
        docs/user/tutorial/quickstart/artifacts/pom2.xml \
        modules/library/metadata/src/main/java/org/geotools/factory/GeoTools.java

.. note:: If you are on macOS, you will need to add ``''`` after the ``-i`` argument for each ``sed`` command.

* Commit the changes and push to the master branch on GitHub::

      git commit -am "Update version to 18-SNAPSHOT"
      git push geotools master
      
* Create the new release candidate version in `JIRA <https://osgeo-org.atlassian.net/projects/GEOT>`_ for issues on master; for example, if master is now ``18-SNAPSHOT``, create a Jira version ``18-RC1`` for the first release of the ``18.x`` series

* Update the jobs on build.geoserver.org:
  
  * disable the maintenance jobs, and remove them from the geotools view
  * create new jobs, create from the exsisting master jobs, editing the branch and the DIST=stable configuration. Remember to also create the new docs jobs.
  * edit the previous stable branch, changing to DIST=maintenance

* Announce on the developer mailing list that the new stable branch has been created.

* This is the time to update the README.md, README.html and documentation links
  
  For the new stable branch:
  
  * common.py - update the external links block changing 'latest' to 'stable'
  * README.md and README.html - update the user guide links changing 'latest' to 'stable'  
  
  For the new maintenance branch:
  
  * common.py - update the external links block changing 'stable' to 'maintenance' (the geoserver link will change to 'maintain').
  * README.md and README.html - update the user guide links changing 'stable' to 'maintenance'  

Build the Release
-----------------

Run the `geotools-release <https://build.geoserver.org/view/geotools/job/geotools-release/>`_ job in Jenkins. The job takes the following parameters:

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

   http://build.geoserver.org/geotools/release/<RELEASE> 

Test the Artifacts
------------------

Download and try out some of the artifacts from the above location and do a 
quick smoke test that there are no issues. Engage other developers to help 
test on the developer list.

In particular, you can download the source artifacts and build them locally on an empty Maven repository to make sure any random user out there can do the same.

A simple way to do so is:

*  Unpack the sources
*  Check the README.html links go to the correct stable or maintenance user guide
*  Temporarily move the ``$HOME/.m2/repository`` to a different location, so that Maven will be forced to build from an empty repo. If you don't want to fiddle with your main repo just use ``mvn -Dmaven.repo.local=/tmp/m2 install -Dall -T1C`` where it points to any empty directory.
*  Do a full build using ``mvn install -Dall -T1C``
*  On a successfull build, delete ``$HOME/.m2/repository`` and restore the old maven repository backed up at the beginning

Download the user guide:

* Check the eclipse quickstart section on `geotools.version`, should reference the correct release tag and snapshot tag.
 
Publish the Release
-------------------

Run the `geotools-release-publish <https://build.geoserver.org/view/geotools/job/geotools-release-publish/>`_ in Jenkins. The job takes the following parameters:

**VERSION** 

  The version being released. The same value specified for ``VERSION`` when running the ``geotools-release`` job.
  
**BRANCH** 

  The branch being released from.  The same value specified for ``BRANCH`` when running the ``geotools-release`` job.

**GIT_USER**

  The Git username to use for the release.

**GIT_EMAIL**

  The Git email to use for the release.


This job will rsync all the artifacts located at::

     http://build.geoserver.org/geotools/release/<RELEASE>

to the SourceForge FRS server, and also deploy the artifacts to the public geotools maven repository.

#. Navigate to `Sourceforge <http://sourceforge.net/projects/geotools/>`__ and verify that the artifacts have been uploaded properly.
#. If this is the latest stable release, make its ``-bin.zip`` the default download for all platforms (use the "i" button).

Announce the Release
--------------------

Announce on GeoTools Blog
^^^^^^^^^^^^^^^^^^^^^^^^^

#. Navigate to Blogger and sign in: https://www.blogger.com/
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
