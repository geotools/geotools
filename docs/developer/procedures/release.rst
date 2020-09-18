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

When performing a release we don't require a "code freeze" in which no developers can commit to the repository. Instead we release from a revision that is known to pass all tests, including unit/integration tests on the GeoServer side. These instructions are valid in case you are making a release in combination with GeoServer, if you are making a stand alone release it's up to you to choose the proper GIT revision number for the GeoTools released to be picked from.

To obtain the GeoServer and GeoTools revisions that have passed testing, navigate to `geoserver.org <http://geoserver.org>`__ and download a "binary" nightly build. From the download check the :file:`VERSION.txt` file. For example:

.. code-block:: none

    version = 2.17-SNAPSHOT
    git revision = 1ee183d9af205080f1543dc94616bbe3b3e4f890
    git branch = origin/2.17.x
    build date = 19-Jul-2020 04:41
    geotools version = 23-SNAPSHOT
    geotools revision = 3bde6940610d228e01aec9de7c222823a2638664
    geowebcache version = 1.17-SNAPSHOT
    geowebcache revision = 27eec3fb31b8b4064ce8cc0894fa84d0ff97be61/27eec
    hudson build = -1

Since we don't make any release from master, ensure you select the right nightly download page to obtain the right revision.

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

* Checkout the master branch and update the version in all ``pom.xml`` files and a few miscellaneous files; for example, if changing master from ``17-SNAPSHOT`` to ``18-SNAPSHOT``::

    git checkout master
    ant -f build/release.xml -Drelease=24-SNAPSHOT
    
  This replaces::

    find . -name ``pom.xml`` -exec sed -i 's/17-SNAPSHOT/18-SNAPSHOT/g' {} \;
    sed -i 's/17-SNAPSHOT/18-SNAPSHOT/g' \
        build/rename.xml \
        docs/build.xml \
        docs/common.py \
        docs/user/artifacts/xml/pom3.xml \
        docs/user/tutorial/quickstart/artifacts/pom2.xml \
        modules/library/metadata/src/main/java/org/geotools/factory/GeoTools.java

  .. note:: If you are on macOS, you will need to add ``''`` after the ``-i`` argument for each ``sed`` command.
     
     ::
  
        find . -name ``pom.xml`` -exec sed -i '' 's/17-SNAPSHOT/18-SNAPSHOT/g' {} \;

* Commit the changes and push to the master branch on GitHub::

    git commit -am "Update version to 24-SNAPSHOT"
    git push geotools master
      
* Create the new release candidate version in `JIRA <https://osgeo-org.atlassian.net/projects/GEOT>`_ for issues on master; for example, if master is now ``24-SNAPSHOT``, create a Jira version ``24-RC1`` for the first release of the ``24.x`` series

* Create the new ``GeoTools $VER Releases`` (e.g. ``GeoTools 22 Releases``) folder in `SourceForge <https://sourceforge.net/projects/geotools/files/>`__

* Update the jobs on build.geoserver.org:
  
  * Disable the maintenance jobs, and remove them from the geotools view
  * Create new jobs, create from the existing master jobs, editing the branch and the DIST=stable configuration. Remember to also create the new docs jobs.
  * Edit the previous stable branch, changing to DIST=maintenance

* Announce on the developer mailing list that the new stable branch has been created.

* This is the time to update the README.md, README.html and documentation links
  
  For the new stable branch:
  
  * common.py - update the external links block changing 'latest' to 'stable'
  * README.md - update the user guide links changing 'latest' to 'stable'  
  
  ::
      sed -i 's/docs.geotools.org\/latest/docs.geotools.org\/stable/g' README.md docs/common.py
      sed -i 's/docs.geoserver.org\/latest/docs.geoserver.org\/stable/g' docs/common.py

  For the new maintenance branch:
  
  * common.py - update the external links block changing 'stable' to 'maintenance' (the geoserver link will change to 'maintain').
  * README.md - update the user guide links changing 'stable' to 'maintenance'  
  
  ::
      sed -i 's/docs.geotools.org\/stable/docs.geotools.org\/maintenance/g' README.md docs/common.py
      sed -i 's/docs.geoserver.org\/stable/docs.geoserver.org\/maintain/g' docs/common.py

Build the Release
-----------------

Run the `geotools-release <https://build.geoserver.org/view/geotools/job/geotools-release/>`_ job in Jenkins. The job takes the following parameters:

**BRANCH**

  The branch to release from, "8.x", "9.x", etc... This must be a stable branch. Releases are not performed from master.
     
**REV**

  The Git revision number to release from. eg, "24ae10fe662c....". If left blank the latest revision (i.e. HEAD) on the ``BRANCH`` being released is used.
  
**VERSION**
   
  The version/name of the release to build, "8.5", "9.1", etc...
  
**GIT_USER**

  The Git username to use for the release.

**GIT_EMAIL**

  The Git email to use for the release.	 
     
This job will checkout the specified branch/revision and build the GeoTools
release artifacts. When successfully complete all release artifacts will be 
uploaded to the following location::

   https://build.geoserver.org/view/release/job/geotools-release/<JOB-NO>

There is also a link at the top of the completed job page.

Test the Artifacts
------------------


Download and try out some of the artifacts from the above location and do a 
quick smoke test that there are no issues. Engage other developers to help 
test on the developer list.

Check the artifacts by:

*  Unpacking the sources
*  Checking the README.html links go to the correct stable or maintenance user guide

The Jenkins job will perform a build of the source artifacts on an empty Maven
repository to make sure any random user out there can do the same. If you want
you can still manually test the artifacts by:

*  Temporarily moving the ``$HOME/.m2/repository`` to a different location, so that Maven will be forced to build from an empty repo. 
*  Do a full build using ``mvn install -Dall -T1C``
*  On a successful build, delete ``$HOME/.m2/repository`` and restore the old maven repository backed up at the beginning
* If you don't want to fiddle with your main repo just use ``mvn -Dmaven.repo.local=/tmp/m2 install -Dall -T1C`` where it points to any empty directory.

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
