Prepare oracle location based services
--------------------------------------

1. Create the tile table::
     
     CREATE TABLE TILES0(location CHAR(64) NOT NULL ,data BLOB,CONSTRAINT TILES0_PK PRIMARY KEY(location))

2. Create the spatial table
     
     CREATE TABLE SPATIAL0 ( location CHAR(64) NOT NULL, GEOM MDSYS.SDO_GEOMETRY NOT NULL ,CONSTRAINT SPATIAL0_PK PRIMARY KEY(location))
   
3. Or, all in one table
     
     CREATE TABLE SPATIAL0 ( location CHAR(64) NOT NULL, GEOM MDSYS.SDO_GEOMETRY NOT NULL,data BLOB, ,CONSTRAINT SPATIAL0_PK PRIMARY KEY(location))

4. Register the spatial attribute. Here the SRS_ID is 31287 (AUSTRIA_LAMBERT), the x coordinate is
   between 0 and 1000000, the y coordinate is between 0 and 1000000 and the granularity is 0.1 meter.::
     
     INSERT INTO user_sdo_geom_metadata (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
     VALUES('SPATIAL0','GEOM',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',0,1000000,0.1),
                                             MDSYS.SDO_DIM_ELEMENT('Y',0,1000000,0.1)),31287)

5. Existing Coordinate Referencing Systems can be queried::
     
     select * from MDSYS.CS_SRS
