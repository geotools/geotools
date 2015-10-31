EPSG Extension Plugin
^^^^^^^^^^^^^^^^^^^^^^

The plugin an Coordinate Reference System Authority that adds common extra or unoffical EPSG
codes into the mix. Internally it uses the same java property file as the :doc:`wkt` plugin.

This plugin is compatible with a restricted environment and does not require disk access.

The Plugin will work out of the box, inlcude it in your CLASSPATH path.

**References**

* `esri.properties <https://github.com/geotools/geotools/blob/master/modules/plugin/epsg-extension/src/main/resources/org/geotools/referencing/factory/epsg/esri.properties>`_ Additional ESRI Compatibility Codes 
* `unnamed.properties <https://github.com/geotools/geotools/blob/master/modules/plugin/epsg-extension/src/main/resources/org/geotools/referencing/factory/epsg/unnamed.properties>`_ Additional EPSG Codes

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-epsg-extension</artifactId>
      <version>${geotools.version}</version>
    </dependency>
