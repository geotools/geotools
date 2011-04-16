Prepare db2 with spatial extender
---------------------------------

Preparing a db2 database needs the following steps:

1. After you created the db, use the db2se utility to do the following::
     
     db2se enable_db db_name

2. Next you need a Spatial Reference System (SRS).

   Pick an existing one::
     
     select srs_id,srs_name,coordsys_name from db2gse.st_spatial_reference_systems

   Or register your own using db2 spatial extender user guide (This is beyond of the scope of this documentation)::
     
     db2se create_srs .....

3. Now you should have a SRS_ID and SRS_NAME which will be used for table creation.::
     
     CREATE TABLE TILES0(location CHAR(64) NOT NULL ,data BLOB,CONSTRAINT TILES0_PK PRIMARY KEY(location))
     CREATE TABLE SPATIAL0 ( location CHAR(64) NOT NULL, GEOM db2gse.st_multipolygon NOT NULL ,CONSTRAINT SPATIAL0_PK PRIMARY KEY(location))
   
   If you prefer one table::
     
     CREATE TABLE SPATIAL0 ( location CHAR(64) NOT NULL, GEOM db2gse.st_multipolygon NOT NULL , data BLOB,CONSTRAINT SPATIAL0_PK PRIMARY KEY(location))

4. Register your geometry column::
     
     Usage: db2se register_spatial_column db_name
              [-userId user_id -pw password]
              [-tableSchema schema]
              -tableName SPATIAL0
              -columnName GEOM
              -srsName srs_name

5. And check it with::
     
     select srs_id,srs_name from db2gse.st_geometry_columns where table_schema='schema' and table_name='SPATIAL0' and column_name='GEOM'
