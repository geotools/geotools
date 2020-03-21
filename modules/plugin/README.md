GeoTools Plugins
================

GeoTools offers plug-ins to support additional data formats, different coordinate reference system authorities and so on.

At least one plug-in is needed for each architectural layer for GeoTools to function. As an example every time you use the `gt-referencing` module ensure you have an EPSG plugin around (or the referencing module will not have access to a definition for ``EPSG:4326`` ).

For more information see the [GeoTools User Guide](https://docs.geotools.org/latest/userguide/welcome/architecture.html).

Service Provider Interface
--------------------------

GeoTools uses Java's built-in ServiceLocator functionality to discovery plugin functionality on the classpath. To support discovery plugins advertise their functionality using `META-INF/servcies`.

