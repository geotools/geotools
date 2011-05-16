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

geotools-commits
^^^^^^^^^^^^^^^^

A read only list to which all SVN (was CVS) commit statements are posted.

* http://lists.sourceforge.net/lists/listinfo/geotools-commits
* http://sourceforge.net/mailarchive/forum.php?forum_name=geotools-commits

Issue Tracker
---------------

GeoTools tracks tasks, issues and bugs with its JIRA tracker, generously provided by Atlassian and hosted by CodeHaus.

This is where all bugs should be reported, in addition to requested features and improvements.

* http://jira.codehaus.org/secure/BrowseProject.jspa?id=10270

Filling out all fields is specially important for bug reports:

* including component(s)
* affected versions of geotools ( a release or code from SVN? )
* A description of what caused the problem (example code that reproduces the problem) along with any stack traces
* The Java Version and the type of operating system you are using
* You may wish to attach log files, or screen snapshots to the Jira task
* If you are reporting failed tests during the maven build, check the test reports in
  ``<module>/target/surefire-reports`` for further details

Internet Relay Chat
--------------------

GeoTools developers use an IRC channel for real time collaboration (for situations where email is
too slow) - this is also a suitable venue for questions.

If you are new to IRC, you will need to find an IRC client. The later versions of Netscape and
Mozilla have IRC built in, and you can connect to a GeoTools meeting simply by using the URL:
irc://irc.freenode.net/geotools.

The information you need to configure your IRC client are:

* Server - Pick one from Freenode Servers
* Channel - #geotools
* Port - 6667

Logs from IRC meetings are stored on this wiki:

* http://docs.codehaus.org/pages/viewrecentblogposts.action?key=GEOTOOLS

IRC Breakout Meetings

* Occasionally breakout IRC meetings will be announced on the geotools-devel mailing list around
  specific topics of interest.
  
  Anyone is free to start a meeting; we ask that an email be sent out and a time negotiated on the
  email list allowing interested parties to attend.

Websites
--------

GeoTools maintains a number of public websites:

* http://geotools.org/
     Website generated using sphinx operated by OSGeo

* http://geotoolsnews.blogspot.com/   
  Blog operated by GeoTools on blogger

* http://docs.codehaus.org/display/GEOTOOLS/Home
  
  http://docs.codehaus.org/display/GEOTDOC/Home  
  http://docs.codehaus.org/display/GEOT/Home
     Confluence wiki used for developer collaboration operated by CodeHaus

* http://jira.codehaus.org/secure/BrowseProject.jspa?id=10270      JIRA Issue Tracker operated by CodeHaus

* http://xircles.codehaus.org/projects/geotools   
  CodeHaus project page (for confluence and jira project permissions)

* http://svn.osgeo.org/geotools/trunk
  
  svn source code repository operated by OSGeo

* http://sourceforge.net/projects/geotools/   
  Used for project downloads.

We have a number of facilities we no longer use:

* http://svn.geotools.org/ * http://javadoc.geotools.fr/
* http://geotools.fr/

GeoTools has entries on a number of other public websites:

* http://freshmeat.net/projects/geotools/
* http://www.ohloh.net/p/geotools

Confluence
----------

Confluence has been used since March 2004 to allow anyone (developers and users) to create and update GeoTools documentation.

Because of spammers our procedure to get read/write access to the wiki has gotten a tad annoying.

It is documented at the bottom of our home wiki page:

1. Create an account for confluence: http://docs.codehaus.org/signup.action
2. Create an account for codehaus: http://xircles.codehaus.org/signup
3. Go to your personal details page: http://xircles.codehaus.org/my/details
4. Use the form to fill in your Confluence Username from step one.
   
   http://xircles.codehaus.org/projects/geotools

5. Go to the GeoTools project page: http://xircles.codehaus.org/projects/geotools
6. And click on Apply to join as a developer
7. Wait for a GeoTools Project Management Committee Member to grant you permission
8. Once you have an account, you just need to login and click on the edit button to modify a page.

Tips:

* Confluence uses a simple markup language, and a quick reference is given on the right side of
  pages you are editing.
* One way to start contributing to the wiki documentation is to fix mistakes, clarify confusing
  content or by contributing to the documentation for areas of the GeoTools code base you are
  familiar with.