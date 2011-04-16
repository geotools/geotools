Oracle GeoRaster Plugin
-----------------------

This plugin is an extension to the Image Mosaicing Pyramidal JDBC Plugin. Since an Oracle Raster Object contains tiles and pyramids, configuration is simple.

Credits

* Thanks to Steve Way and Pablo Najarro, who started with this module.
* Baskar Dhanapal developed a lot of code for getting image metadata from Oracle GeoRaster and constructing a Java image from pure raster data.
  Studying this code, I decided to request image data in TIFF format from GeoRaster and construct the Java image using JAI and AWT.

Prerequisites

* Oracle Spatial available
* Oracle JDBC Driver jar file in your classpath

Setup Oracle GeoRaster
^^^^^^^^^^^^^^^^^^^^^^

Since this modules requires a coverage name, a table containing a column for the coverage name is needed. This table also needs a RASTER column for the image. The values for the coverage name must be unique. Of course, this table can have other additional columns.

**One row in this table corresponds to one coverage**

1. An example setup for a table called RASTER, coverage name column NAME and a raster column IMAGE::
     
     CREATE TABLE RASTER (NAME VARCHAR(64) ,  IMAGE SDO_GEORASTER);
     
     call sdo_geor_utl.createDMLTrigger('RASTER', 'IMAGE');
     
     CREATE TABLE RASTER_RDT OF SDO_RASTER
     (PRIMARY KEY (rasterID, pyramidLevel, bandBlockNumber
      wBlockNumber, columnBlockNumber))
      LOB(rasterBlock) STORE AS rdt_1_rbseg
        (
        CHUNK 8192
        CACHE READS
        NOLOGGING
        PCTVERSION 0
        STORAGE (PCTINCREASE 0)
        );

2. Now insert a record for a coverage named oek.
   
   The raster- and pyramid tiles are stored in a table called RASTER_RDT.::
     
     INSERT INTO RASTER VALUES ('oek', sdo_geor.init('RASTER_RDT'));

3. Import the image. Look here in case of problems Oracle Georaster Import::
     
     DECLARE   
        geor SDO_GEORASTER;
     BEGIN  
     -- Import the TIFF image and world file 
     SELECT georaster INTO geor from RASTER
        where NAME = 'oek' FOR UPDATE; 
     sdo_geor.importFrom(geor, NULL, 'TIFF', 'file', 
     '/georaster/data/oek.tif',
     'WORLDFILE','FILE','/georaster/data/oek.tfw');  
     UPDATE RASTER SET georaster = geor where NAME = 'oek';  
     COMMIT;
     END;

4. Create the pyramids::
     
     DECLARE
       gr mdsys.sdo_georaster;
     BEGIN
       select IMAGE into gr from RASTER
           where NAME = 'oek' for update;
        sdo_geor.generatePyramid(gr, 'rLevel=2 resampling=NN');
        update RASTER set IMAGE = gr where NAME='oek';
     COMMIT;
     END;

Configuration
^^^^^^^^^^^^^

The Config file::
  
  <?xml version="1.0" encoding="UTF-8" standalone="no"?>
  <config version="1.0">
    coverageName name="oek"/>
    <coordsys name="EPSG:4326"/>
    <!-- interpolation 1 = nearest neighbour, 2 = bipolar, 3 = bicubic -->
    <scaleop  interpolation="1"/>
    <spatialExtension name="georaster"/>
        <mapping>
            <masterTable name="RASTER" >
            <geoRasterAttribute name="IMAGE"/>
            <coverageNameAttribute name="NAME"/>
        </masterTable>
    </mapping>
    <connect>
        <!-- value DBCP or JNDI -->
        <dstype value="DBCP"/>
     <!-- <jndiReferenceName value=""/> -->
        
        <username value="geotools" />
        <password value="geotools" />
        
        <jdbcUrl value="jdbc:oracle:thin:@ux-mc01.ux-home.local:1521:geotools102" />
        <driverClassName value="oracle.jdbc.OracleDriver"/> 
        <maxActive value="10"/>
        <maxIdle value="0"/>
    </connect>
  </config>

Most elements are self explanatory, the detailed documentation is in Image Mosaicing Pyramidal JDBC Plugin.

Use
^^^

For a Java example of how to use your new coverage see Image Mosaicing Pyramidal JDBC Plugin.
