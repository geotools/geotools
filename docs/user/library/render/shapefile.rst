Shapefile Renderer
------------------

An implementation of ``GTRender`` that has been extended with shapefile specific optimizations. It
will call ``GTRender`` for any layers that are not a shapefile.

Most of the optimizations that were prototyped in ``gt-shapefile`` have been brought back
into ``gt-render``.

As such this module is no longer under development.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-shapefile-renderer</artifactId>
      <version>${geotools.version}</version>
    </dependency>
