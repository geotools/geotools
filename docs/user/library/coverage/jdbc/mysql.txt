Prepare for mysql
-----------------

1. Create the tile table::
     
     CREATE TABLE TILES0(location CHAR(64) NOT NULL ,data LONGBLOB,CONSTRAINT TILES1_PK PRIMARY KEY(location))

2. Create the spatial table::
     
     CREATE TABLE SPATIAL0 ( location CHAR(64) NOT NULL, GEOM MULTIPOLYGON NOT NULL ,CONSTRAINT SPATIAL0_PK PRIMARY KEY(location))

3. Or, all in one table::
     
     CREATE TABLE SPATIAL0 ( location CHAR(64) NOT NULL, GEOM MULTIPOLYGON NOT NULL, data LONGBLOB ,CONSTRAINT SPATIAL0_PK PRIMARY KEY(location))
