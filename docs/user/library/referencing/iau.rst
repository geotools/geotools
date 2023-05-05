IAU planetary CRS Plugin
^^^^^^^^^^^^^^^^^^^^^^^^

The plugin adds a Coordinate Reference System Authority with CRSs for various planetary bodies
in the solar system (Sun, planets and moons). 
Internally it uses the same java property file as the :doc:`wkt` plugin.

The Plugin will work out of the box, just include it in your CLASSPATH path.

References
'''''''''''

* `iau.properties <https://github.com/geotools/geotools/blob/main/modules/plugin/iau-wkt/src/main/resources/org/geotools/referencing/iau/iau.properties>`_ IAU WKT definitions

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-iau-wkt</artifactId>
      <version>${geotools.version}</version>
    </dependency>
