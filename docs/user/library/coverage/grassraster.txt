GrassRaster Plugin
------------------

The module supplies support for reading and writing GRASS rasters. The metadata contain both the colortable and the categories (if available) of the raster map. The original colortable of the GRASS raster is read into the geotools coverage Categories and can as such be visualized if using a default styler.

Data structure of a rastermap
-----------------------------

Raster data in GRASS are stricktly bound to their workspace. Since a workspace is something more on userlevel, I will from now on use grass database for the folder structure and workspce for what the user will interact with at application level.

A GRASS raster map consists of several files in several subdirectories in a mapset, organized as follows:

* cellhd/: map header including projection code, coordinates representing the spatial extent of the raster map, number of rows and columns, resolution, and information about map compression;
* cell/, fcell/ or grid3/: generic matrix of values in a compressed, portable format which depends on the raster data type (integer, floating point or 3D grid);
* hist/: history file which contains metadata such as the data source, the command that was used to generate the raster map, or other information provided by the user;
* cats/: optional category file which contains text or numeric labels assigned to the raster map categories;
* colr/: optional color table;
* cell_misc/: optional timestamp, range of values, quantization rules (for floating point maps) and null (no-data) files;
* WIND: contains the active processing region and the resolution
* PROJ_INFO: contains the information about the projection
* PROJ_UNITS: contains the information about projection units used
* vector/: contain the vector data since GRASS 6

Usage
-----

.. literalinclude:: /../../modules/plugin/grassraster/src/test/java/org/geotools/gce/grassraster/AdvancedReaderTest.java
	:language: java
	:start-after: // readgrassraster start
	:end-before: // readgrassraster stop

