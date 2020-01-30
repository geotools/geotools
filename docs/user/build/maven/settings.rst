Maven Local Settings
--------------------

On your machine you will find a directory in called 'm2'. This is where Maven stores all downloaded jars and installed projects.

========== ============================================
Platform   Directory
========== ============================================
Linux:     ``~/.m2``
Windows XP ``C:\Documents and Settings\USER\.m2``
Windows 7  ``C:\Users\USER\.m2``
========== ============================================

The format of this directory is:

  * ``.m2/repository`` - this is your local repository where jars have been
    downloaded or installed 
  * ``.m2/settings.xml`` - configuration file for site specific settings such as
    mirror to download from 
  * ``.m2/profiles.xml`` - configuration file for profiles (not often used)

The ``settings.xml`` file
^^^^^^^^^^^^^^^^^^^^^^^^^

You can provide an optional ``settings.xml`` file in this directory that is used
to describe configuration and resources available on your machine.

Examples of use:

* set a proxy server
* turn on / off online tests (same as ``-o`` command line option)::
     
     <offline>true</offline>

* select testing profiles (so you can test against a local database, or
  GeoServer install)
* set up a mirror for better performance
  
Local Repository
^^^^^^^^^^^^^^^^

You should see that any third party jars, such as JTS, will have been installed
in this repository. You should also see that all successful module builds (e.g.
main, referencing...) have had their jars installed in a directory called
``org/geotools``.

Tip:

* Consider placing the repository on a different drive than that used for compiling for a faster build::
     
     <localRepository>D:\java\\repository\</localRepository>


Configuring the heap size
^^^^^^^^^^^^^^^^^^^^^^^^^

The Maven build may requires a fair amount of memory. For example javadoc generation requires a large heap size. If you need the maximal heap size increased check the root :file:`pom.xml` for settings.

To change the heap size of the Maven command itself::
   
   set MAVEN_OPTS=-Xmx384M

Configuring a proxy server
^^^^^^^^^^^^^^^^^^^^^^^^^^
If you are behind a firewall, you will need Maven to use a proxy server.

  * http://maven.apache.org/guides/mini/guide-proxies.html

The above link shows how modify the  ``settings.xml`` file in your ``.m2``
directory.
