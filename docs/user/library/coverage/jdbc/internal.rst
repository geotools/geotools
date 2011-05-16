Image Moasic JDBC Design
------------------------


Outline of the multithreaded architecture
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The multithreaded architecture is based on synchronized Queue available since SDK 5.0. The following threads will be used:

* One ImageComposer thread which reads GridCoverages from the Queue, mosaicing the image and finally rescales the image for the requested dimension.
* For each tile, a TileDecoder thread which decodes the tile, crops the tile if necessary und puts the result as GridCoverage in the queue.
* The Main thread for controlling this threads

The Main threads performs the following steps in chronological order
If the requested CRS ist not equal to the CRS of the tiles, transform the requested CRS

1. Calculate the best pyramid and query the tiles needed from the database
2. Create the queue and start the ImageComposer Thread
3. For each tile fetch the image data and immediately start an ImageDecoder Thread
4. Wait for all ImageDecoder Threads
5. Put a predefined End Message in the queue which causes the ImageComposer Thread to finish its work
6. Wait for the ImageComposer Thread
7. If the requested CRS ist not equal to the CRS of the tiles, reproject the image
8. Return the GridCoverage

Database Design
^^^^^^^^^^^^^^^

First, we need a Meta Table for holding Information for each pyramid of the maps. This table has 9 columns as described below

* Name (String): The name of the map
* SpatialTable (String): The name of the table where to find the georeferencing information
* TileTable (String): The name of the table where to find the tile data stored as BLOB.
* ResX (Double): The resolution of the x-axis
* ResY (Double): The resolution of the y-axis
* MinX (Double): The minimum X of the extent
* MinY (Double): The minimum Y of the extent
* MaxX (Double): The maximum X of the extent
* MaxY(Double): The maximum Y of the extent

**Important**

The only fields you have to fill are Name,SpatialTable and TileTable. These three fields together are the primary key. All other fields will be calculated by the plugin. It is not necessary to do calculations. The plugin implements the following mechanism during the first request:

1. Check if ResX or ResY is null, if true, calculate the values from a random chosen tile from the corresponding tile table.
2. Check if one of MinX,MinY,MaxX,MaxY is null, if true, calculate the values from the corresponding spatial table.
3. If something has been calculated, store these values into to db to avoid recalculation at the next restart

This approach assures that the probably costly calculation is only executed once, but updating,adding or removing tiles requires you to nullify one of the MinX,MinY,MaxX,MaxY Attributes to force a new extent calculation.

**First example**

Assume we have a Map called Europe with 2 Pyramids. Since we have 3 levels, we need 3 tile tables and 3 spatial tables. Lets start filling the META table.

====== ============= ========== ===== ===== ===== ===== ===== =====
Name   SpatialTable  TileTable  ResX  ResY  MinX  MinY  MaxX  MaxY 
====== ============= ========== ===== ===== ===== ===== ===== =====
Europe EUSPAT0       EUTILE0    .     .     .     .     .     .
Europe EUSPAT1       EUTILE1    .     .     .     .     .     .
Europe EUSPAT2       EUTILE2    .     .     .     .     .     .
====== ============= ========== ===== ===== ===== ===== ===== =====

Remember: You do not have to fill the other attributes.

A tile table needs the following attributes:

* Location (String) : A unique identifier for the tile
* Data (Blob) : The image data (Encoded in a format readable by JAI, e. g. png,jpeg,...)

The table EUTILE0 for example ( 0x.... indicates binary data)

========== ===============
Location   Data
========== ===============
Tile_1_1   0x10011...
Tile_1_2   0x00011..
========== ===============

A spatial table contains the georeferencing information. Depending on your database installation, there are 2 possibilites.

1. You have no spatial db extension implies storing the extent in 4 Double fields
2. Having a spatial extension offers the possiblity to use a geometry column

A spatial Table without using a spatial extension needs the following attributes:

* Location (String) : A unique identifier used for joining into the tiles table
* MinX (Double): min x of the tile extent
* MinY (Double): min y of the tile extent
* MaxX (Double): max x of the tile extent
* MaxY (Double): max y of the tile extent

The table EUSPAT0 for example.

=========== ===== ===== ===== =====
Location    MinX  MinY  MaxX  MaxY
=========== ===== ===== ===== =====
Tile_1_1    10    10    20    20 
Tile_1_2    20    10    30    20
=========== ===== ===== ===== =====

A spatial Table using a spatial extension needs the following attributes:

* Location (String) : A unique identifier used for joining into the tiles table
* Geometry (Geometry): A Multipoligon type provided by your db

The table EUTILE0 for example

========== ===========
Location   Geom
========== ===========
Tile_1_1   0x00111...
Tile_1_2   0x10011...
========== ===========

Reducing the number of tables

Since the number of rows in a spatial table has to be equal to the number of rows in a tile table, there is a one to one relationship. It is no problem to join these tables. Applying this redesign to our samples results in the following table structure:

====== ============= ========== ===== ===== ===== ===== ===== =====
Name   SpatialTable  TileTable  ResX  ResY  MinX  MinY  MaxX  MaxY 
====== ============= ========== ===== ===== ===== ===== ===== =====
Europe EU0           EU0        .     .     .     .     .     .
Europe EU1           EU1        .     .     .     .     .     .
Europe EU2           EU2        .     .     .     .     .     .
====== ============= ========== ===== ===== ===== ===== ===== =====

The EU0 Table 

========== ===== ===== ===== ===== ==========
Location   MinX  MinY  MaxX  MaxY  Data
========== ===== ===== ===== ===== ==========
Tile_1_1   10    10    20    20    0x10011..
Tile_1_2   20    10    30    20    0x00011..
========== ===== ===== ===== ===== ==========

or using a geometry column

========== =========== ============
Location   Geom        Data
========== =========== ============
Tile_1_1   0x00111...  0x10011..
Tile_1_2   0x10011...  0x00011..
========== =========== ============

Further DB Design Rules

* It is possible to add custom columns to your tables
* It is possible to create one meta table for all your maps and pyramid levels , or create one meta table for each map containing records for the map and its pyramids, or do any mixture of these two approaches, the only rule is to store a map and its pyramids level together in the same meta table.
* All primary key attributes will be handled as JDBC String, the exact DB Type and length depends on your needs.
* All numerical fields will be handled as JDBC Double.
* Tile image data has to be a BLOB