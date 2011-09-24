Postgis Raster Plug in
----------------------

This plug in is an extension to the Image Mosaicing Pyramidal JDBC Plugin. Since a Postgis raster object offers spatial functions and predicates,  configuration is simple.


Prerequisites

* Postgis 2.0 available

Setup Postgis Raster
^^^^^^^^^^^^^^^^^^^^

This plugin expects the following table layout:

* A table having a Postgis raster column
* Each row in the table represents a tile
* The tiles are rectangles
* A new table is needed for each overview (pyramid level)  
* The name of the raster column is identical in all tables

.. note::

   Of course, it is possible to use sql views instead of tables for
   creating the best physical table layout for the local deployment

.. warning::

   At the time of developing this module, Postgis 2.0 was not a final version.
   The support for OUT-DB rasters is not working, IN-DB rasters work fine.

Prepare the tiles and overviews using the gdal tool box http://www.gdal.org. 
A good candidate is http://www.gdal.org/gdal_retile.html.

The utility for importing the tiles is described here 
http://postgis.refractions.net/documentation/manual-svn/RT_reference.html#RT_Loading_Rasters
and is included in Postgis 2.0


An example setup assumes the existense of 3 tables, **rtable1**, **rtable2** **rtable3**. **rtable1** is populated with the tiles from the base image,
**rtable2** and **rtable3** are tables for overviews. The raster column in all tables is named **rast**. The plugin needs exactly one meta data table. Table name
and column names must match with the xml configuration (example follows) . If the table is not present, create it with::
     
   create table MOSAIC (NAME varchar(254) not null, 
            TileTable varchar(254)not null, 
            minX FLOAT8,minY FLOAT8, maxX FLOAT8, maxY FLOAT8,resX FLOAT8, resY FLOAT8," 
            primary key (NAME,TileTable))


Now insert a record for a coverage named **oek**, which is stored in **rtable1**, **rtable2** **rtable3**. **oek** is the geotools name of the coverage::
   
     insert into MOSAIC(NAME,TileTable) values ('oek',''rtable1)     
     insert into MOSAIC(NAME,TileTable) values ('oek',''rtable2)     
     insert into MOSAIC(NAME,TileTable) values ('oek',''rtable3)     


Configuration
^^^^^^^^^^^^^

The plugin needs a xml configuration file. Since connect and mapping info can be reused by many coverages, it is better to have the configuration splitted into 3 parts.

1. JDBC connect configuration stored as  **connect.pgraster.xml.inc** in this example setup.

.. code-block:: xml

  <connect>
     <dstype value="DBCP"/>
     <username value="postgres"/>
     <password value="postgres"/>
     <jdbcUrl value="jdbc:postgresql://ux-server02:5432/geo"/>
     <driverClassName value="org.postgresql.Driver"/>
     <maxActive value="10"/>
     <maxIdle value="0"/>
  </connect>

2. JDBC mapping configuration stored as **mapping.pgraster.xml.inc** in this example setup.

.. code-block:: xml

   <spatialExtension name="pgraster"/>		
   <mapping>		
 	<masterTable name="MOSAIC" >
		<coverageNameAttribute name="NAME"/>
		<maxXAttribute name="maxX"/>
		<maxYAttribute name="maxY"/>
		<minXAttribute name="minX"/>
		<minYAttribute name="minY"/>
		<resXAttribute name="resX"/>
		<resYAttribute name="resY"/>
		<tileTableNameAtribute	name="TileTable" />
	</masterTable>
	<tileTable>
		<blobAttributeName name="rast" />
	</tileTable>
  </mapping>


3. Coverage configuration including the above XML fragments, stored as **oek.pgraster.xml** in this example setup.

.. code-block:: xml

  <?xml version="1.0" encoding="UTF-8" standalone="no"?>
  <!DOCTYPE ImageMosaicJDBCConfig [
	<!ENTITY mapping PUBLIC "mapping"  "mapping.pgraster.xml.inc">
	<!ENTITY connect PUBLIC "connect"  "connect.pgraster.xml.inc">
  ]>

  <config version="1.0">
	<coverageName name="oek"/>
	<coordsys name="EPSG:4326"/>
	<!-- interpolation 1 = nearest neighbour, 2 = bipolar, 3 = bicubic -->
	<scaleop  interpolation="1"/>
	&mapping;
	&connect;
  </config>


Store all thee files in the same folder. Most elements are self explanatory, the detailed documentation is in :ref:`Image Mosaicing Pyramidal JDBC Plugin <im-jdbc>`.

Use
^^^

For a Java example of how to use your new coverage see :ref:`Image Mosaicing Pyramidal JDBC Plugin <im-jdbc>`.
