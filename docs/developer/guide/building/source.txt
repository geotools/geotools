Source Code
============

The GeoTools source code is organised into the following structure:

======================== =========================================================================
``build/``               java projects that help with our build process
``demo/``                example code and demos
``docs/``                documentation and website source in rich structured text
``modules/library/``     the core library allowing your application to be spatial
``modules/extensions/``  extensions built on top of the library that do useful things
``modules/plugins/``     plugins that work with the core library to support additional formats
``modules/unsupported/`` community code that is not ready yet, but you may find interesting
``spike/``               scratch space for ideas and experiments and collaboration
======================== =========================================================================

Download Source Code
^^^^^^^^^^^^^^^^^^^^^

Source code releases are made available on a monthly basis and are available on the downloads page:

* http://sourceforge.net/projects/geotools/files

Source code encoding is UTF-8.

Subversion Checkout of Source Code
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

GeoTools makes use of the Subversion revision control system (as mentioned in downloads above). It is an advanced version management tool with the same command line syntax as CVS.

You do not need any special permission to have read-only access to the source code. Please just check out the code and have fun. If you are interested in getting commit permission later you can look into Developers Guide Roles and Responsibilities.

1. Our Subversion Install page contains detailed instructions for setting up subversion
   on different platforms.
2. Copy :download:`config</artifacts/config>` into the following location
   
   ============= ===========================================================================
   Plartform     Config File Location
   ============= ===========================================================================
   Windows XP:   ``C:\Documents and Settings\ %USERID% \Application Data\Subversion\config``
   Vista:        ``C:\Users\ %USERID% \App Data\Roaming\Subversion\config``
   Windows7:     ``C:\Users\ %USERID% \App Data\Roaming\Subversion\config``
   Linux:        ``~/.subversion/config``
   Mac:          ``~/.subversion/config``
   ============= ===========================================================================
   
3. Danger - please set up the config file - If you do not do this binary and xml files may get messed up
   (subversion uses this config file to tell when to change linefeeds between windows and linux)

4. Navigate to where you want the checkout with the command line::
     
     C:\java>

5. Checkout geotools using svn (a new directory "trunk" will be created)::
     
     C:\java>svn co http://svn.osgeo.org/geotools/trunk trunk
     
6. This will create a trunk directory that contains the source code for this project

Notes:

* You may also use subversion to checkout a stable version of geotools::
    
    C:\java>svn co http://svn.osgeo.org/geotools/branches/2.7.x stable
    
* Take the time to read the subversion book before diving into subversion:
  http://svnbook.red-bean.com/svnbook/index.html 
* Although links to various IDE interfaces will be made available, no GUI will substitute for an understanding of the underlying subversion versioning model, and how the system is actually doing work.
* If you are interested you can also use https::
    
    C:\java> svn co https://svn.osgeo.org/geotools/trunk
