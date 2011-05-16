Setup Example
^^^^^^^^^^^^^

1. Prepare Image Data
   
   Prepare your image data in advance. The size of the images has a big influence on performance.
   
   Here, we are not talking about the size of an encoded image (jpeg,png), the size of the decoded
   image requesting java heap memory is interesting.
   
   * One of the best possibilities is to use an image with one band and  an indexed color table.
   * A not so good solution is using a RGBA image needing four bands and requesting 3-4 times more
     memory than an image with an indexed color table.
   
   If your are not sure, use http://www.gdal.org/gdalinfo.htmlto get the color model of the image data.

   At the beginning we have a large image or a set of tiles which compose a large image. We will use
   http://www.gdal.org/gdal_retile.html to prepare our image and image pyramid tiles.

   .. toctree::
      :maxdepth: 1
      
      prepare

   As a result, we have our tiles, csv or shape files ready to import into a database.

2. The next step is to create the table for the meta info.
   
   Attention: All the table and attribute names have to correspond to the xml mapping fragment,
   otherwise the plugin will not work. If there is a valid xml config, you can use the following
   utility to create ddl script templates.
   
   .. toctree::
      :maxdepth: 1
      
      ddl
      meta

3. Next you have to choose on of the following options.
   
   * The first one will work with any jdbc database and does not require a spatial extension.
     
   .. toctree::
      :maxdepth: 1
      
      jdbc
   
   * The other options are dedicated to selected databases using their spatial extension.
     
     The spatialExtension element in the xml mapping fragment is used to specify the option to use.
     
   .. toctree::
      :maxdepth: 1
      
      db2
      postgis
      mysql
      oracle

4. The next step is importing the data. Each database has its own utilities for loading bulk data,
   discussion is beyond the scope of this documentation.
   
   As an alternative, there is a java utility doing the job.
   
   .. toctree::
      :maxdepth: 1
      
      import

5. Last not least, we have to speed up access times.
   
     .. toctree::
      :maxdepth: 1
      
      performance

