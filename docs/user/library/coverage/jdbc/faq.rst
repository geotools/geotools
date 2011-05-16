Image Moasic JDBC FAQ
---------------------

Q: How to Build image pyramids and tiles?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Creating the pyramids and tiles is not part of this module.

As a recommendation you can use gdal_retile which has been developed exactly for this requirement:

* http://www.gdal.org/gdal_retile.html

A good idea is to download the fwtools containing gdal_retile.py:

* http://fwtools.maptools.org/.

The output of gdal_retile.py can be imported into the database with the import utility (described later).

A good practice is to use a color table for your source image(s). Less memory consumption and a
better performance are the results. If you have an already tiled source image, be sure that each
source tile uses the same color table, otherwise use gdal_merge.py (Gdal utility) to produce a big
single image and apply rgb2pct.py (Gdal utility).

Q: How to import the tiles and the georeferencing information?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Importing the data is database dependent. If you have a spatial extension, the gdal_retile.py utility
produces the proper world,shape or csv files which you can import with a database utility.
