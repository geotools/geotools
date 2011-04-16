Prepare with gdal_retile
------------------------

**Introduction to gdal_retily.py**

Attention: These examples were executed on a linux, box, if you have a Microsoft platform, you must change your path names.
Additionally, the executable is gdal_retile.bat instead of gdal_retile.py

The following examples assume we have a big image called big.tif.

* First simple example::
    
    gdal_retile.py -targetDir /tmp/test  big.tif
  
   Result: In the directory /temp/test are many georeferenced tiles,  pixelsize 256x256, which will mosaic to the original image.

* Example of specific size::
    
    gdal_retile.py -ps 512 512 -targetDir /tmp/test big.tif
  
  Result: The tiles are 512 pixel x 512 pixel

* Sometimes you need to specify the coordinate system::
    
    gdal_retile.py -s_srs EPSG:4326 -ps 512 512 -targetDir /tmp/test  big.tif
  
* Now, we build additional pyramids::
    
    gdal_retile.py -levels 3 -ps 512 512 -targetDir /tmp/test big.tif
  
  Result: There are 3 additional subdirectories  /tmp/test/1,/tmp/test/2,/tmp/test/3 containing the tiles for 3 pyramid levels.

* If we have already a set of tiles (\*.tif) and need only the tiled pyramids::
    
    gdal_retile.py -levels 3 -pyramidOnly -ps 512 512 -targetDir /tmp/test *.tif
  
* If we have already a set of tiles (\*.tif or \*.png  or ...) and need to retile and to build  pyramids::
    
    gdal_retile.py -levels 3  -ps 512 512 -targetDir /tmp/test *.tif

* There some more switches (e.g. image format ), please consult the man page.

Common problems:

* If you have many tiles , the \*.tif pattern could result in a "command line buffer exceeded" error. This is not a problem of the utility.
  
  In this situation you have to create a file listing all the tif files with absolute path names (Lets call it tilelist.txt ) 
  and then change the above command to::
    
    gdal_retile.py -levels 3  -ps 512 512 -targetDir /tmp/test --optfile tilelist.txt
  
**Exporting the georeferencing information**

Now we can decide how to prepare the georeferencing information:

* Exporting the georeferencing information into a csv file::
    
    gdal_retile.py -levels 3  -ps 512 512 -csv index.csv -targetDir /tmp/test --optfile tilelist.txt
  
  This produces a csv file for for each image level.
  
  * The csv has 5 columns: filename,minx,maxx,miny,maxy.
  * Each record describes the georeferencing information for a tile.
 
* Exporting the georeferencing information into a shape file::
    
    gdal_retile.py -levels 3  -ps 512 512 -tileIndex index.shp -tileIndexField LOCATION -targetDir /tmp/test --optfile tilelist.txt
  
  This produces shape files named index.shp with a geometry attribute LOCATION.
  Use http://www.gdal.org/ogr/ogrinfo.html to check the content.

* Exporting the georeferencing information into world files::
    
    gdal_retile.py -levels 3  -ps 512 512 -co "WORLDFILE=YES" -targetDir /tmp/test --optfile tilelist.txt
