Maven Testing
-------------

To just run tests for a module::
   
   cd modules/library/main
   mvn test

Several of the "profiles" described on this page can be used on conjuction::
   
   mvn test -o -P interactive,stress

Logging
^^^^^^^

By default logging information is considered a test result:

* the logs from testing end up in target/surefire-reports sub-directory of each module
* There should be XML and TXT files for the results of each test. Be sure to check both, as one of the wrinkles of maven makes it so the output isn't always exactly the same.

Run a Single Test
^^^^^^^^^^^^^^^^^

To run a single test::
   
   mvn -Dtest=Rendering2DTest test

If you are having problems with logging output levels be sure to read the logging section under coding conventions.

Test Coverage
^^^^^^^^^^^^^

You can use maven to measure test coverage::

   mvn -o clean cobertura:cobertura

The resulting report is located in :file:`target/site/cobertura/index.html``.

Online Testing
^^^^^^^^^^^^^^

Many tests require an online resource to run such as a remote server.

These tests are excluded from the default build as often these online resources are not available and as a result case tests to fail.

The naming convention for these tests is to name them as "OnlineTest".

Examples:

* PostgisDataStoreAPIOnlineTest.java
* WMS1_0_0_OnlineTest.java

1. To execute online tests during a build, the online profiles is used::
   
      mvn test -P online

2. Additionally many of these online tests depend on test fixtures (i.e. connection parameters) being supplied describing the server to connect to.
3. The first time you run they will often create a series of "example" test fixtures
   
   * ``~/.geotools/postgis.properties.example``
   * ``~/.geotools/db2.properties.example``
   * ``~/.geotools/wps.properties.example``

4. Rename one of the files to not end with "example" (i.e. rename to postgis.properties)
5. edit the file to fill in the connection parameters for the server you are testing against, and save.
6. You can now run the online test against your server
   
   * Note many of the database tests are good and create a table, perform a test, and then remove the table when finished.

For more information on the use of these "test fixture" files please see the project contentions section on online tests.

Image tests
^^^^^^^^^^^

Some tests in raster and rendering handling needs to compare images using a human perception model instead of a raw pixel by pixel comparison.

If these tests are failing you can also have the system show you the expected and actual image in a GUI popup, to enable it you'll have to use the following profile::

   mvn test -P interactive.image
   
Interactive Tests
^^^^^^^^^^^^^^^^^

Some tests (particularly of rendering technology) like to pop up images and the practice drawing. 

To run these tests::
   
   mvn test -P interactive.tests

Stress Testing
^^^^^^^^^^^^^^

Stress tests can roughly be defined as tests that do not add much test coverage to a module, and take a extended amount of time to run.

These tests are excluded from the default build as they are time consuming. The naming convention for these tests is to name them as StressTest.

Examples:

* GMLParserStressTest.java
* WFSIonicStressTest.java

To execute stress tests during a build, the stress profiles is used::
   
   mvn test -P stress
