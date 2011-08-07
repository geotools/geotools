**************
Documentation
**************

The chapter covers some writing guidelines for GeoTools documentation, along with some tips and
tricks for working with Sphinx.

.. toctree::
   :maxdepth: 1
   
   reference
   welcome
   tutorial

To build docs you will need the optional sphinx install:

You can build with maven:

1. You can build everything::
   
      maven install
   
   This will build the Java code; followed by the documentation.

2. Build just specific documentation::
      
      mvn install -Puser
      mvn install -Pdeveloper
      mvn install -Pweb
      mvn install -Pindex

3. The results are available in the target folder:
   
   * target/user/html/index.html
   * target/developer/html/index.html
   * target/web/html/index.html
   * target/index/html/index.html


You can also build use ant; which is faster to start up (and will not build the Java code):

1. You can build everything::
     
     ant full

2. You can also build specifc documentation::
     
     ant index
     ant user
     ant web
     ant developer

3. The results are available in the target folder:
   
   * target/user/html/index.html
   * target/developer/html/index.html
   * target/web/html/index.html
   * target/index/html/index.html
