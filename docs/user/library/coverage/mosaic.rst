Mosaic Plugin
-------------

This format make use of a shapefile to act as a kind of index, the features in this shapefile
list the filename of the "images" to display and the location in which they should be displayed.

**Reference**

* `ImageMosaicFormat <http://docs.geotools.org/latest/javadocs/org/geotools/gce/imagemosaic/ImageMosaicFormat.html>`_ (javadoc)
* `ImageMosaicJDBCReader <http://docs.geotools.org/latest/javadocs/org/geotools/gce/imagemosaic/jdbc/ImageMosaicJDBCReader.html>`_ (javadoc)
* :doc:`pyramid`
* `Using the ImageMoasic plugin <http://docs.geoserver.org/stable/en/user/tutorials/image_mosaic_plugin/imagemosaic.html>`_

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-imagemosaic</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Example
^^^^^^^

An example data set looks something like::
  
  bluemarble.shp
  bluemarble.prj
  bluemarble.properties
  ... a bunch of raster files ...

* Shapefile file
  
  The schema of your shapefile is important:
  
  You MUST have a attribute called "location", this will be used to look up the raster files

* PRJ file
  
  The usual projection file associated with a shapefile, using the WKT format for a
  CoordianteReferenceSystem.
  
* Properties File
  
  The property file is REQUIRED and use to provide a bunch of settings:
  
  Example bluemarble.properties::
    
    #
    #Thu Jan 11 14:53:30 CET 2007
    Name=modis
    ExpandToRGB=true
    Levels=10000.0,10000.0
    LevelsNum=1
    Envelope2D=-3637013.0,-1158091.0 1019969.0,4092819.0
    NumFiles=3
  
  Where the following are required:
  
  * Name
  * ExpandToRGB: true if we need to expand the color model from indexed to rgba
    
    * If all your images use the same indexed palette you can set this to false
      and get a large performance gain
  
  * Levels: list of resolutions
    
    * Format: level_0_x_resolution, level_0_y_resolution, level_1_x_resolution,
      level_1_y_resolution
    * This measure of resolution describes how big each pixel is in real world units
    * Sample calculation: envelope.getLength(0) / image.getWidth()
    * You can define several levels in order to capture overview files if you have them
    
  * LevelsNum: the number of levels mentioned above
  * Envelope2D: bounds in the CRS specified in the prj file
    
    * Format: "minX,minY maxX,maxY"
    
  * NumFiles: should be the same as the number of features in your shapefile
