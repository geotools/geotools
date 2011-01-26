Online Testing Fixtures
-----------------------
In order to provide GeoTools online tests with the credentials they need, one must arrange several properties files.  The files provided in this directory are good defaults, but feel free to modify them to point to alternate servers.

These properties files (fixtures) should be located in the user's home directory, in the ".geotools" directory.  To disable all tests which use a certain fixture, simply delete that fixture and tests will not be run.  In order to run online tests, the online test profile must be activated (otherwise all online tests are skipped); please consult the geotools root pom.xml file for further instructions on how to execute online tests.
