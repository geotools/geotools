Prepare for postgis
-------------------

1. Install PostGIS
   
   Postgis setup is described here:
   
   * http://www.postgis.org/docs/ch02.html

2. Spatially enabling a postgres db needs the following steps from the command line::
     
     createlang plpgsql [yourdatabase]
     psql -d [yourdatabase] -f lwpostgis.sql
     psql -d [yourdatabase] -f spatial_ref_sys.sql

3. Creating the tile table::
     
     CREATE TABLE tiles0(location CHAR(64) NOT NULL ,data BYTEA,CONSTRAINT tiles0_PK PRIMARY KEY(location))
     Creating the spatial table
     
     CREATE TABLE spatial0 (location CHAR(64) NOT NULL ,CONSTRAINT spatial0_PK PRIMARY KEY(location))
   
4. or creating all in one table::
     
     CREATE TABLE spatial0 (location CHAR(64) NOT NULL ,CONSTRAINT spatial0_PK PRIMARY KEY(location))

5. Now register the geometry attribute, the third value is the EPSG Code for your coordinate reference system, 4326 is WGS84::
     
     SELECT AddGeometryColumn('spatial0','geom',4326,'MULTIPOLYGON',2)

