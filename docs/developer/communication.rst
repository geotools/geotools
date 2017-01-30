*************
Communication
*************

The GeoTools Project practices “open development” with project communication taking place using a range of public forums.

Email
-----

geotools-gt2-users
^^^^^^^^^^^^^^^^^^

The users list is for questions regarding the installation or use of the GeoTools2 library. Users of GeoTools are usually java developers building applications with spatial capabilities.

* https://lists.sourceforge.net/lists/listinfo/geotools-gt2-users
* http://sourceforge.net/mailarchive/forum.php?forum_name=geotools-gt2-users

Sometimes, some small applications or server-side code will exist within the GeoTools code base (for example, a Web Map Service implementation or a conversion utility). If these have not yet found a project of their own, then questions about how to use them may be directed to this list.


geotools-devel
^^^^^^^^^^^^^^

Much of the development discussion happens on the geotools-devel mailing list.
If your query is more general, e.g. you need help with how to use GeoTools,
please use the geotools-gt2-users mailing list.

 * http://lists.sourceforge.net/lists/listinfo/geotools-devel
 * http://sourceforge.net/mailarchive/forum.php?forum_name=geotools-devel

The developer list is used by the development team of GeoTools solely to discuss development issues. The list is exclusively for:

1. current members of the GeoTools2 development team.
2. those who are interested in joining the GeoTools development effort.
3. those who wish to follow the discussions surrounding GeoTools development.

Please do not send general questions regarding the use of GeoTools to this list, but to geotools-gt2-users above.

Commit Tracker
--------------

To follow new commits and pull requests to the GeoTools repository, GitHub provides a tracking feature called
`Watching Repositories <https://help.github.com/articles/watching-repositories>`_. Following the instructions provided
by GitHub you are able to customise the feed of information you are able to receive. Note that GeoTools doesn't utilise
the issue feature of GitHub; see the `Issue tracker <http://docs.geotools.org/latest/developer/communication.html#issue-tracker>`_
in the next section.

Other pages that may be helpful in following GeoTools are the `pulse <https://github.com/geotools/geotools/pulse>`_ and
the `commits <https://github.com/geotools/geotools/commits>`_. Both are pages that provide summary information about the
current activity of the project.

Issue Tracker
---------------

GeoTools tracks tasks, issues and bugs with its JIRA tracker, generously provided by OSGeo and hosted by Atlassian.

This is where all bugs should be reported, in addition to requested features and improvements.

* https://osgeo-org.atlassian.net/projects/GEOT

Filling out all fields is specially important for bug reports:

* including component(s)
* affected versions of geotools (a release or git commit hash)
* A description of what caused the problem (example code that reproduces the problem) along with any stack traces
* The Java Version and the type of operating system you are using
* You may wish to attach log files, or screen snapshots to the Jira task
* If you are reporting failed tests during the maven build, check the test reports in
  ``<module>/target/surefire-reports`` for further details

The User Guide has an example of `creating a new issue <http://docs.geotools.org/latest/userguide/welcome/support.html#issue-tracker>`_.

Internet Relay Chat
--------------------

GeoTools developers use an IRC channel for real time collaboration (for situations where email is
too slow) - this is also a suitable venue for questions.

If you are new to IRC, you will need to find an IRC client. If you have configured your browser
to support IRC (Firefox can use the Chatzilla plugin for example), and you can connect to a
GeoTools meeting by clicking on the following URL:
irc://irc.freenode.net/geotools.

The information you need to configure your IRC client are:

* Server - Pick one from Freenode Servers
* Channel - #geotools
* Port - 6667

IRC Breakout Meetings

* Occasionally breakout IRC meetings will be announced on the geotools-devel mailing list around
  specific topics of interest.

  Anyone is free to start a meeting; we ask that an email be sent out and a time negotiated on the
  email list allowing interested parties to attend.


Wiki
----

The development team has migrated to the use of GitHub wiki (in April of 2015) to work on design ideas and change proposals. Prior to this time Confluence was used.

* https://github.com/geotools/geotools/wiki

Developers can edit the wiki directly using the Edit button provided. 

If you do not have commit access and would like to submit a change proposal please email geotools-devel and a committer can post on your behalf.

Websites
--------

GeoTools maintains a number of public websites.

============================================= =========================================== ============
http://geotools.org/                          GeoTools website                            OSGeo
http://geotoolsnews.blogspot.com/             GeoTools Blog                               Blogger
http://sourceforge.net/projects/geotools/     Used for project downloads.                 SourceForge
https://github.com/geotools                   Source code                                 GitHub
https://osgeo-org.atlassian.net/projects/GEOT JIRA Issue Tracker                          Atlassian
https://github.com/geotools/geotools/wiki     Wiki for developer collaboration            GitHub
============================================= =========================================== ============

GeoTools has entries on a number of other public websites:

* https://www.openhub.net/p/geotools
* http://freecode.com/projects/geotools/ (no longer being updated)
* http://live.osgeo.org/en/overview/geotools_overview.html
* http://www.osgeo.org/geotools
* http://gis.stackexchange.com/questions/tagged/geotools

We have archived a number of facilities we no longer use:

* http://old.geotools.org/
* http://docs.codehaus.org/display/GEOTDOC/Home
* http://docs.codehaus.org/display/GEOT/Home
* http://svn.osgeo.org/geotools/trunk
* http://svn.geotools.org/

