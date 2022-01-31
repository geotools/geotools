.. _release_guide:

Release Guide
=============

This guide details the process of performing a GeoTools release.   

Before you start
----------------

Notify developer list
^^^^^^^^^^^^^^^^^^^^^

It is good practice to notify the `GeoTools developer list <https://lists.sourceforge.net/lists/listinfo/geotools-devel>`__ of the intention to make the release a few days in advance, even though the release date has been agreed upon before hand. 

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

Since we don't make any release from main, ensure you select the right nightly download page to obtain the right revision.

To obtain information about the current environment::

   ant -f build/build.xml info
   
:: 

     [echo] version: '27-SNAPSHOT'
     [echo] date: 'September 10 2021'
     [echo] series: 'latest' (used for url references)
     [echo] 
     [echo] The `release` target requires next release version:
     [echo]   ant release -Drelease='27.0'
     [echo] 
     [echo] The `latest` target requires next snapshot version:
     [echo]   ant latest -Drelease='28-SNAPSHOT'

Release in JIRA
---------------

1. Navigate to the `GeoTools project page <https://osgeo-org.atlassian.net/projects/GEOT?selectedItem=com.atlassian.jira.jira-projects-plugin:release-page&status=released-unreleased>`_ in JIRA.

2. Add a new version for the next version to be released after the current release. For example, if you are releasing GeoTools 27.1, create version 27.2.

3. Click in the Actions column for the version you are releasing and select Release. Enter the release date when prompted. If there are still unsolved issues remaining in this release, you will be prompted to move them to an unreleased version. If so, choose the new version you created in step 

If you are cutting the first RC of a series, create the stable branch
---------------------------------------------------------------------

.. note:: The RC is the first release of a series, released prior to the .0 release.

When creating the first release candidate of a series, there are some extra steps to create the new stable branch and update the version on main.

* Checkout the main branch and make sure it is up to date and that there are no changes in your local workspace::

    git checkout main
    git pull
    git status

* Create the new stable branch and push it to GitHub; for example, if main is ``27-SNAPSHOT`` and the remote for the official GeoTools is called ``upstream``::

    git checkout -b 27.x
    git push upstream 27.x

* `GitHub branch protection <https://github.com/geotools/geotools/settings/branches>`_ uses wild cards to protect the new branch (so no further configuration is required).

* Checkout the main branch and use `branch.xml` to update the version in all ``pom.xml`` files and a few miscellaneous files; for example, if changing main from ``27-SNAPSHOT`` to ``28-SNAPSHOT``::

    git checkout main
    ant -f build/build.xml latest -Drelease=28-SNAPSHOT

* Commit the changes and push to the main branch on GitHub::

    git commit -am "Update version to 28-SNAPSHOT"
    git push geotools main
      
* Create the new release candidate version in `JIRA <https://osgeo-org.atlassian.net/projects/GEOT>`_ for issues on main; for example, if `main` branch is now ``28-SNAPSHOT``, create a Jira version ``28-RC1`` for the first release of the ``28.x`` series

* Create the new ``GeoTools $VER Releases`` (e.g. ``GeoTools 27 Releases``) folder in `SourceForge <https://sourceforge.net/projects/geotools/files/>`__

* Update the jobs on build.geoserver.org:
  
  * Disable the previous maintenance jobs, and remove them from the geotools view

  * For the new stable branch create new jobs, duplicate from the existing ``main`` jobs, editing branch specifier to the new branch (e.g. `27.x`)
    
  * Special care is needed when setting up java11 build which uses `A`, `B` and `C` groups.
    
    For example if the next group in the rotation is group ``A``:
    
    * Carefully set Multi-Project Throttle Category to the next available groups
      
      ``Build A``
      
    * Adjust custom workspace (used as a shared workspace and local maven repo location) to match the throttle category groups
      
      :file:`workspace/java11a`

* Announce on the developer mailing list that the new stable branch has been created.

* This is the time to update the README.md, README.html and documentation links
  
  For the new `stable` branch::
  
    git checkout 27.x
    git pull
    ant -f build/build.xml stable
    git add .
    git commit -m "Change 27.x to stable branch"
    git push geotools 27.x

  For the new `maintenance` branch::
  
    git checkout 26.x
    git pull
    ant -f build/build.xml maintenance
    git add .
    git commit -m "Change 26.x to stable branch"
    git push geotools 26.x
  
  This change will update the `pom.xml` series used to determine where documentation from the branch is published.

Build the Release
-----------------

Run the `geotools-release <https://build.geoserver.org/view/geotools/job/geotools-release/>`_ job in Jenkins. The job takes the following parameters:

**BRANCH**

  The branch to release from, "8.x", "9.x", etc... This must be a stable branch. Releases are not performed from main.
     
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

1. Source download: The Jenkins job will perform a build of the source artifacts on an empty Maven
   repository to make sure any random user out there can do the same. If you want
   you can still manually test the artifacts by:

   * Unpacking the sources
   * Temporarily moving the ``$HOME/.m2/repository`` to a different location, so that Maven will be forced to build from an empty repo. 
   * Do a full build using ``mvn install -Dall -T1C``
   * On a successful build, delete ``$HOME/.m2/repository`` and restore the old maven repository backed up at the beginning
   
   If you don't want to fiddle with your main repo just use ``mvn -Dmaven.repo.local=/tmp/m2 install -Dall -T1C`` where it points to any empty directory.

3. Userguide: check the eclipse quickstart section on `geotools.version`, should reference the correct release tag and snapshot tag.

4. Binary download:
   
   * Checking the README.html links go to the correct stable or maintenance user guide
   
   * Check library loads:
     
     .. code-block:: bash
        
        java -cp "lib/*" org.geotools.util.factory.GeoTools
   
   * Run quickstart:
     
     .. code-block:: bash
     
        mkdir bin
        javac -cp "lib/*" -d bin src/org/geotools/tutorial/quickstart/Quickstart.java 
        java -cp "lib/*:bin" org.geotools.tutorial.quickstart.Quickstart
 
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

Release notes
-------------

Publish release notes to github tag:

#. Select the correct release from `JIRA Releases <https://osgeo-org.atlassian.net/projects/GEOT?orderField=RANK&selectedItem=com.atlassian.jira.jira-projects-plugin%3Arelease-page&status=released>`__ page.

#. From the release page, locate the :guilabel:`Release notes` button at the top of the page to open the release notes edit
  
#. Generate release notes as markdown:
   
   * Select format `Markdown`
   * Layout: Issue key with link
   * Issue types: `Bug` and `Improvement`
   
   Change the heading from :kbd:`Release notes - GeoTools - Version 26.1` to :kbd:`Release notes`, and apply the change with :guilabel:`Done`.

   Use :guilabel:`Copy to clipboard` to obtain the markdown, similar to the following:
   
   .. code-block:: text
   
      # Release notes

      ### Bug

      [GEOT-7001](https://osgeo-org.atlassian.net/browse/GEOT-7001) XmlComplexFeatureParser gives wrong name for ComplexAttribute

      ### Improvement

      [GEOT-7020](https://osgeo-org.atlassian.net/browse/GEOT-7020) Add ProjectionHandler for orthographic

      [GEOT-7007](https://osgeo-org.atlassian.net/browse/GEOT-7007) Shapefile set files search may take very long on big shapefile directories

#. Navigate to github tags https://github.com/geotools/geotools/tags
   
   Locate the new tag from the list, and use :menuselection:`... --> Create release`
   
   * Release title: `GeoTools 26.1`
   * Write: Paste the markdown from Jira release notes editor
   
   Use :guilabel:`Publish release` button to publish the release notes.
   
Announce the Release
--------------------

Announce on GeoTools Blog
^^^^^^^^^^^^^^^^^^^^^^^^^

#. Navigate to Blogger and sign in: https://www.blogger.com/
#. Select the GeoTools blog from the list (if not listed, get someone to add you)
#. Create a new blog post anouncing your release; copy and paste a previous blog post preserving series information unless this is the first of a new series
#. You will need to correct the following information: 

   * Update the Sourceforge links above to reflect the release
   * Update the Release Notes with link to GitHub release URL: https://github.com/geotools/geotools/releases/tag/26.1
   * For a new stable series, be sure to thank those involved with the release (testing, completed proposals, docs, and so on)

#. The public entry point will be here: http://geotoolsnews.blogspot.com/
  
Tell the World
^^^^^^^^^^^^^^

After the list has had a chance to try things out - make an announcement.

Cut and paste from the blog post to the following:

1. geotools-devel@lists.sourceforge.net
   
   * To: geotools-devel@lists.sourceforge.net
   * Subject: 26.1 Released
   
2. geotools-gt2-users@lists.sourceforge.net
   
   Let the user list know:
   
   * To: geotools-gt2-users@lists.sourceforge.net
   * Subject: GeoTools 26.1 Released

3. Open Source Geospatial Foundation
   
    Only to be used for "significant" releases (Major release only, not for milestone
    or point releases)
    
    https://www.osgeo.org/content/news/submit_news.html
    
4. Post a message to the osgeo news email list (you are subscribed right?)
   
   * To: news_item@osgeo.org
   * Subject: GeoTools 26.1 Released
