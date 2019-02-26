# PostGIS Raster Coverage Reader

This module contains a coverage reader for reading rasters from a PostGIS Database.

## Configuration

The coverage reader is configured with an xml file containing information about the 
database and table to read from. The format of the file is as follows:

    <!-- 
      postgis raster config format, * = required element 
    -->
    <pgraster>
      <name></name>                       <!-- * name of the resulting coverage -->

      <!-- 
      database connection info, either name,host,port,etc... is required or jndi
      -->
      <database>                  
        <host></host>                     <!-- db hostname, defaults to 'localhost' -->
        <port></port>                     <!-- db port, defaults to '5432' -->
        <name></name>                     <!-- database name -->
        <user></user>                     <!-- database user -->
        <passwd></passwd>.                <!-- database password -->
        <jndi></jndi>                     <!-- JNDI data source name -->
      </database>

      <!--
      raster table / column config
      -->
      <raster>
        <table></table>                   <!-- * raster table name -->
        <schema></schema>                 <!-- raster table schema, defaults to 'public' -->
        <column></schema>                 <!-- raster table column, defaults to 'rast' -->
      </raster>

      <!--
      time column config
      -->
      <time>                              <!-- time dimension configuration -->
        <column>dstamp</column>           <!-- name of data/time column -->
      </time>

      <!-- 
      https://postgis.net/docs/postgis_gdal_enabled_drivers.html 
      -->
      <enableDrivers></enabledDrivers>.   <!-- Example: ENABLE_ALL -->
    </pgraster>


## Test Data

The tests in this module rely on some test data from the imagemosaic module. raster2pgsql was used to load it:

Grayscale test:

    raster2pgsql -s 32633 D220361A.tif pgraster.gray

RGB test:

    raster2pgsql -s 4326 world.200405.3x5400x2700.tiff pgraster.world

Time test:

    raster2pgsql -s 4326 -F world.*.tiff pgraster.world_with_time
    ALTER TABLE pgraster.world_with_time ADD dstamp DATE;
    UPDATE pgraster.world_with_time SET dstamp = TO_DATE(SUBSTRING(filename from 7 for 6), 'YYYYMM');

