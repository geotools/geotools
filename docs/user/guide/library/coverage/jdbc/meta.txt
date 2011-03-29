Create the meta table
---------------------

Creating the meta table is common for all databases::
  
  CREATE TABLE META(Name CHARACTER (64)  NOT NULL,SpatialTable VARCHAR (256)  NOT NULL,TileTable VARCHAR (256)  NOT NULL,
          ResX DOUBLE,RresY  DOUBLE,MinX DOUBLE,MinY DOUBLE,MaxX DOUBLE,MaxY DOUBLE,
          CONSTRAINT MASTER_PK PRIMARY KEY (Name,SpatialTable,TileTable))

Filling the meta table::
  
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL0','TILES0')
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL1','TILES1')
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL2','TILES2')
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL3','TILES3')

If you prefer to join the spatial tables and the tile tables into one table::
  
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL0','SPATIAL0')
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL1','SPATIAL1')
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL2','SPATIAL2')
  INSERT INTO META(Name,TileTable,SpatialTable) VALUES ('oek','SPATIAL3','SPATIAL3')
