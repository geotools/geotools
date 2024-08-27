Mosaic Plugin
-------------

This format make use of a shapefile to act as a kind of index, the features in this shapefile
list the filename of the "images" to display and the location in which they should be displayed.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-imagemosaic</artifactId>
      <version>${geotools.version}</version>
    </dependency>


To create the shapefile you simply provide the location of a folder with "images" to the constructor of ImageMosaicReader.
Those image files must conatin it's own world-file to be a part of the image mosaic.

To display the mosaic within a map, you will use a GridReaderLayer.

.. literalinclude:: /../src/main/java/org/geotools/imagemosaic/MosaicViewer.java
  :language: java
  :start-after: // start Create ImageMosaicReader example
  :end-before: // end Create ImageMosaicReader example

The simplest style to provide would be like this:

.. literalinclude:: /../src/main/java/org/geotools/imagemosaic/MosaicViewer.java
  :language: java
  :start-after: // start Create Raster Style
  :end-before: // end Create Raster Style

To use mulitthreading you must provide a threading pool to the mosaic reader, and also tell the grid reader layer to allow multithreading.

.. literalinclude:: /../src/main/java/org/geotools/imagemosaic/MosaicViewer.java
  :language: java
  :start-after: // start Create Multithreaded ImageMosaicReader example
  :end-before: // end Create Multithreaded ImageMosaicReader example

Example Image Mosaic files
^^^^^^^^^^^^^^^^^^^^^^^^^^

An example data set looks something like::
  
  bluemarble.shp
  bluemarble.prj
  bluemarble.properties
  sample_image.dat
  ... a bunch of raster files ...

* Shapefile file
  
  The schema of your shapefile is important:
  
  You MUST have a attribute called "location", this will be used to look up the raster files

* PRJ file
  
  The usual projection file associated with a shapefile, using the WKT format for a
  CoordinateReferenceSystem.

* Sample image file

  The :file:`sample_image.dat` (legacy :file:`sample_image`) is a sample image used to determine color model
  upfront without requiring a full scan.
  
  By default the sample image file is limited to built-in image formats and color models. This restriction may be relaxed using the system property ``org.geotools.gce.imagemosaic.sampleimage.allowlist``:
  
  .. code-block:: properties
     
     org.geotools.gce.imagemosaic.sampleimage.allowlist=^some\.package\.MyCustomColorModel$

* Properties File
  
  The property file is REQUIRED and use to provide a bunch of settings:
  
  Example ``bluemarble.properties``::
    
    #
    #Thu Jan 11 14:53:30 CET 2007
    Name=modis
    ExpandToRGB=true
    Levels=10000.0,10000.0
    LevelsNum=1
    GeneralBounds=-3637013.0,-1158091.0 1019969.0,4092819.0
    NumFiles=3
  
  Where the following are required:
  
  * Name
  * ``ExpandToRGB``: true if we need to expand the color model from indexed to
    ``rgba``
    
    * If all your images use the same indexed palette you can set this to false
      and get a large performance gain
  
  * ``Levels``: list of resolutions
    
    * ``Format``: level_0_x_resolution, level_0_y_resolution, level_1_x_resolution,
      level_1_y_resolution
    * This measure of resolution describes how big each pixel is in real world units
    * Sample calculation: ``envelope.getLength(0) / image.getWidth()``
    * You can define several levels in order to capture overview files if you have them
    
  * ``LevelsNum``: the number of levels mentioned above
  * ``GeneralBounds``: bounds in the CRS specified in the ``.prj`` file
    
  * ``Format``: ``minX,minY maxX,maxY``
    
  * ``NumFiles``: should be the same as the number of features in your shapefile

**Reference**

* `ImageMosaicFormat <https://docs.geotools.org/latest/javadocs/org/geotools/gce/imagemosaic/ImageMosaicFormat.html>`_ (javadoc)
* `ImageMosaicJDBCReader <https://docs.geotools.org/latest/javadocs/org/geotools/gce/imagemosaic/jdbc/ImageMosaicJDBCReader.html>`_ (javadoc)
* :doc:`pyramid`
* `ImageMosaic plugin <https://docs.geoserver.org/stable/en/user/data/raster/imagemosaic/index.html>`_
* `Using the ImageMosaic plugin for raster time-series data <https://docs.geoserver.org/stable/en/user/tutorials/imagemosaic_timeseries/imagemosaic_timeseries.html>`_
* `Using the ImageMosaic plugin for raster with time and elevation data <https://docs.geoserver.org/stable/en/user/tutorials/imagemosaic_timeseries/imagemosaic_time-elevationseries.html>`_
* `Using the ImageMosaic plugin with footprint management <https://docs.geoserver.org/stable/en/user/tutorials/imagemosaic_footprint/imagemosaic_footprint.html>`_
