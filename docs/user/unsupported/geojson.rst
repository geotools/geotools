GeoJSON Plugin
--------------

.. warning:: This module has not been maintained for a long time, please use the gt-geojson-core module instead.

Unsupported module illustrating how to encode and decode GeoJSON
files into GeoTools Feature Collections.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-geojson</artifactId>
      <version>${geotools.version}</version>
    </dependency>


Here is a small example from the test cases::

    GeometryJSON gjson = new GeometryJSON();
    // be sure to strip whitespace
    String json = "{'type':'Point','coordinates':[100.1,0.1]}";
    
    Reader reader = new StringReader(json); 
    Point p = gjson.readPoint( reader );

Working with Features is also supported::

    FeatureJSON fjson = new FeatureJSON();
    StringWriter writer = new StringWriter();
    
    fjson.writeFeature(feature(1), writer);
    
    String json = writer.toString();
