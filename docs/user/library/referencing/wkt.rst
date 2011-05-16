EPSG WKT Plugin
^^^^^^^^^^^^^^^

The plugin provides an Coordinate Reference System Authority based on a text file (actually a Java **epsg.properties** file).

This epsg plugin is well suited for use in situations where your application does not have write access to the filesystem (and thus gt-epsh-hsql is not available).

It is not quite as good as the official epsg database; as the codes represented here do not express the full range of bursa wolf parameters available in the official epsg database. Some liberty has also been taken with axis order with the contents often presented is "x/y" order as expected by a simple drawing application.

This plugin is well suited for use in a Java Applet or other restricted environment.

EPSG Definitions
^^^^^^^^^^^^^^^^

The range of represented EPSG codes is not complete, it is limited by the projections supported by GeoTools, and allowances have been made to go with the axis order used by WMS 1.1.1.

The Plugin will work out of the box, simply drop it on your path.

Beyond EPSG Database
^^^^^^^^^^^^^^^^^^^^

The **epsg.properties** file does contain definitions that go beyond those defined by the EPSG database. Traditionally this has been limited to those needed to work by the module maintainer for the Canadian Region; and a few donated on the mailing list for areas like New Zealand.

If you have any additions or corrections please raise a bug report.

The complete definitions can be read directly out of svn:

 * http://svn.osgeo.org/geotools/trunk/modules/plugin/epsg-wkt/src/main/resources/org/geotools/referencing/crs/epsg.properties
