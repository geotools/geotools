Source Code
============

The GeoTools source code is organized into the following structure:

======================== =========================================================================
``build/``               java projects that help with our build process
``docs/``                documentation and website source in rich structured text
``modules/library/``     the core library allowing your application to be spatial
``modules/extensions/``  extensions built on top of the library that do useful things
``modules/ogc/``         OGC schemas and data structures
``modules/plugins/``     plugins that work with the core library to support additional formats
``modules/unsupported/`` community code that is not ready yet, but you may find interesting
``spike/``               scratch space for ideas and experiments and collaboration
======================== =========================================================================

Download Source Code
^^^^^^^^^^^^^^^^^^^^^

Source code releases are made available on a monthly basis and are available on the downloads page:

* http://sourceforge.net/projects/geotools/files

Source code encoding is UTF-8.

Git Checkout of Source Code
^^^^^^^^^^^^^^^^^^^^^^^^^^^

GeoTools makes use of the Git revision control system (as mentioned in downloads above). 
It is an advanced version management tool with a different workflow than Subversion or
CVS.

You do not need any special permission to have read-only access to the source code which
is located on `github <https://github.com/geotools/geotools/>`. You are encouraged to 
clone the repository or fork it into your own account and issue pull requests.

Please just check out the code and have fun. If you are interested in getting commit permission later you can look into Developers Guide Roles and Responsibilities.

#. Our Git Install page contains detailed instructions for setting up git
   on different platforms.
   
#. Ensure your git configuration is a friendly one to cross platform projects::

     git config --global core.autocrlf input
   
   This option may also be set on a repository by repository basis if for some reason 
   you require a different global default. You can verify your git configuration with::
   
     git config --list
   
#. Navigate to where you want the checkout with the command line::
     
     C:\java>

#. Checkout geotools using git (a new directory "geotools" will be created)::
     
     C:\java> git clone git://github.com/geotools/geotools.git

#. This will create a geotools directory that contains the source code for this project

Notes:

* You can switch between the master and stable branches easily::

     C:\java\geotools> git checkout master
     C:\java\geotools> git checkout 8.x 
     C:\java\geotools> git checkout 2.7.x

* Take the time to read the git book before diving into git:

    http://git-scm.com/book/

  Also an excellent introduction to git:
  
    http://www.sbf5.com/~cduan/technical/git/

* Although links to various IDE interfaces will be made available, no GUI will substitute for an understanding of the underlying git versioning model, and how the system is actually doing work.
