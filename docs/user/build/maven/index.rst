Using Maven
============

**Maven** is a Java project management and project comprehension tool, or - in other words - yet another build tool. It can use Ant and a number of other open source utilities and brings them together in an easy-to-use build tool.

.. toctree::
   :maxdepth: 1

   build
   tips
   testing
   snapshots
   javadocs
   eclipse
   pom
   settings
   repositories

Project Files
^^^^^^^^^^^^^

The key part of maven is the use of project files (ie pom.xml). You will find a pom.xml file in all active modules. The project file tells you the name of the module, who maintains it, who develops it, what version it has reached and what it depends on.

Note: that as all the modules have some things in common, the module project files actually extend one which can be found in the GeoTools root directory.

The most important part of the project file is the dependencies section as maven uses this to determine what order to build the modules in and what support jars to download when needed (if we move over to maven exclusively we will no longer need the extbin folder).

Use of Notepad
^^^^^^^^^^^^^^
The windows implementation of notepad messes up **pom.xml** byte order - causing trouble for mac developers working on our project. Please consider using a notepad replacement such as notepad2.

Trouble looks like::

   Reason: Parse error
   reading POM. Reason: only whitespace content allowed before start tag
   and not \ud4 (position: START_DOCUMENT seen \ud4... @1:1)
