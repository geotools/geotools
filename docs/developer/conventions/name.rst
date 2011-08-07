Naming Conventions
===================

Naming conventions is another one of those things that are fun to learn working with a new codebase. We tend to follow normal Java naming for things; we have had some fun with unicode variable names over the years so please be careful and think of others.

Naming Files and Directories
^^^^^^^^^^^^^^^^^^^^^^^^^^^^
GeoTools makes use of the following naming conventions.

* Directory
  
  ext/validation
  
  Directory names shall be all lower case with no spaces.

* Package
  
  org.geotools.filter
  
  Packages are all lower case with no spaces and should be located in *org.geotools package*

* Interface
  
  DataStore
  
  GeoTools interfaces are usually located in the *gt-opengis* or *gt-api* modules. Interfaces should be called XXX.java

* Implementation
  
  ShapefileDataStore
  
  Append the name of the interface being implemented

* Default
  
  DefaultQuery
  QueryImpl
  
  Default implementations should be called DefaultXXX.java or XXXImpl.java

* Abstract
  
  AbstractDataStore
  
  Abstract implementations should be called AbstractXXX.java

* Javadoc
  
  doc-files/
  
  Javadoc makes use of doc-files directories in the package hierarchy to allow the inclusion of rich content in generated api documentation.

* JUnit
  
  test-data/
  
  JUnit test cases make use of test-data directories in the package hierarchy

* Test
  
  SampleTest
  
  JUnit test, picked up by maven build process

* online test
  
  ServerOnlineTest
  
  JUnit test making use of line resource such as a web service

Some versions of windows do not distinguish between upper and lower case, and in unix, writing spaces in filenames is painful.

Notes:

* both test-data and doc-files do not follow the package naming convention allowing them to be easily distinguished from normal code
* both Test and OnineTest are picked out by the maven build process for special treatment

Naming Conventions
^^^^^^^^^^^^^^^^^^

We tend to follow normal java development naming conventions. Classes start with Capital letters and use CamelCase, variable start with a lower case letter and use camelCase, constants and enumerations are ALL_CAPITALS etc..

* Interface
  
  Renderer
  
  Use camel case and start with a capital letter

* Interface
  
  RendererFactory
  
  When defining an interface you often need a factory to handle construction

* Class
  
  DefaultRenderer
  
  Use camel case and start with a capital letter. Try and end with the interface name

* Class
  
  RendererImpl
  
  Used to quickly implement an interface when we expect only one implementation

* Variable
  
  xDelta
  
  Start with lower case using camel case to separate words

* Constant
  
  MAX_LIMIT
  
  Use all capitals and an underscore to separate words. This applies to enumeration constants as well

Tools that can Help
^^^^^^^^^^^^^^^^^^^^

* FindBugs	http://findbugs.sourceforge.net/
* PMD	http://pmd.sourceforge.net/

Running these static analysis tools on your code will find all sorts of mistakes; in addition to checking that your names follow accepted practice.