Prepare a jdbc database
-----------------------

For each image level we have to create a spatial and a tiles table, or a joined table.

Attention: The syntax  may differ depending on your used database. For PostgreSQL
use FLOAT8 instead of DOUBLE and BYTEA instead of BLOB

1. The tile table ::
     
     CREATE TABLE TILES0(Location CHAR(64) NOT NULL ,Data BLOB,CONSTRAINT TILES0_PK PRIMARY KEY(Location))

2. The spatial table::
     
     CREATE TABLE SPATIAL0 ( Location CHAR(64) NOT NULL,MinX DOUBLE NOT NULL,MinY DOUBLE NOT NULL,
          MaxX DOUBLE NOT NULL,MaxY DOUBLE NOT NULL,CONSTRAINT SPATIAL0_PK PRIMARY KEY(Location))

3. or, combined into one table::
    
    CREATE TABLE SPATIAL0 ( Location CHAR(64) NOT NULL,MinX DOUBLE NOT NULL,MinY DOUBLE NOT NULL,MaxX DOUBLE NOT NULL,
          MaxY DOUBLE NOT NULL,Data BLOB,CONSTRAINT SPATIAL0_PK PRIMARY KEY(Location))
