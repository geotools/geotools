Using the java import utility
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The classpath for the utility is set in the manifest file of gt-imagemosiac-jdbc.jar, so you
do not need to bother about dependencies.

The only problem are the jdbc drivers for the the database. First you need to download the jar files.

* DB2
  
  We need db2jcc.jar and db2jcc_license.jar. Download from:
  
  https://www14.software.ibm.com/webapp/iwm/web/preLogin.do?lang=en_US&source=swg-idsjs11

* Oracle
  
  We need ojdbc14.jar. Download from:
  
  http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/htdocs/jdbc9201.html

* Postgres
  
  http://jdbc.postgresql.org/download.html

* MySql
  
  http://www.mysql.com/products/connector/j/

* Other DB Systems
  
  Google is your friend

The problem is to put the jars in the classpath. According to the Sun documentation:

* http://java.sun.com/j2se/1.5.0/docs/tooldocs/findingclasses.html
  
  a class path in a manifest overrides the CLASSPATH environment variable and the -cp or
  -classpath java option.

Solutions:

* A solution is to copy the jar files into /lib/ext of your java runtime. In this case the extension
  class loader is responsible to load your jdbc drivers.
* Another approach is to investigate into the -X options of your java
  runimte. java -X gives help. if there is an option -Xbootclasspath/a: you can avoid copying the
  jdbc jar file into the /lib/ext directory and call "java -Xbootclasspath/a:<location of jdbc.jar> -jar ...."

Call Syntax
'''''''''''

Common options:

* -config
  
  the URL or the file name of the xml config file , e.g "file:/temp/mymap.xml"

* -commitCount
  
  (optional), default 100, insert records in chunks to improve performance

Importing excactly one level

* -spatialTN
  
  the name of the spatial table which must already exist

* -tileTN
  
  the name of the tile table which must already exist. When using a joined table, the tile table
  name has to be equal to the spatial table name

Importing all tiles including the pyramids as produced by gdal_retile.py
  
* -spatialTNPrefix
  
  the prefix of spatial table names (produced by the ddl generation utility)
* -tileTNPrefix
  
  the prefix of tiles table names (produced by the ddl generation utility)

To import the output of gdal_retile.py using world files (one level)::
  
  java -jar gt-imagemosaic-jdbc.jar import -config URLOrFileName -spatialTN spatialTableName -tileTN tileTableName
  [-commitCount commitCount] -dir dir -ext extension

To import the output of gdal_retile.py using world files (all levels)::
  
  java -jar gt-imagemosaic-jdbc.jar import -config URLOrFileName -spatialTNPrefix spatialTableNamePrefix
  -tileTNPrefix tileTableNamePrefix [-commitCount commitCount] -dir dir -ext extension

Options

* -dir
  
  The directory containing tiles
* -ext
  
  The file extension (png,jpg,tif,..). The extension must have exactly 3 chars. The world files
  must have an extension of "wld" or an extension according to the standard for building word files
  (removing the 2. character and appending a 'w').
  
  image.tif -> image.tfw, image.png -> image.pgw,....
  
To import the output of gdal_retile.py using a shape file (one level)::
  
  java -jar gt-imagemosaic-jdbc.jar import -config URLOrFileName -spatialTN spatialTableName -tileTN tileTableName
  [-commitCount commitCount] -shape shapeURLOrFileName -shapeKeyField shapeKeyField
  
To import the output of gdal_retile.py using a shape file (all levels)::
  
  java -jar gt-imagemosaic-jdbc.jar import -config URLOrFileName -spatialTNPrefix spatialTableNamePrefix -tileTNPrefix tileTableNamePrefix
  [-commitCount commitCount] -shape shapeURLOrFileName -shapeKeyField shapeKeyField

Options

* -shape
  
  the URL or the file name of the shape file , e.g "file:/temp/index.shp"
* -shapeKeyField
  
  the name of the attribute in the shape file holding the file names for the tiles

To import the output of gdal_retile.py using a csv file (one level)::

    java -jar gt-imagemosaic-jdbc.jar import -config URLOrFileName -spatialTN spatialTableName -tileTN tileTableName
    [-commitCount commitCount] -csv csvURLOrFileName -csvDelim csvDelim

To import the output of gdal_retile.py using a csv file (all levels)::

    java -jar gt-imagemosaic-jdbc.jar import -config URLOrFileName -spatialTNPrefix spatialTableNamePrefix -tileTNPrefix tileTableNamePrefix
    [-commitCount commitCount] -csv csvURLOrFileName -csvDelim csvDelim
    
Options

* -csv
  
  the URL or the file name of the csv file , e.g "file:/temp/index.csv"
* -csvDelim
  
  The column delimiter in the csv file.

The csv file has exactly 5 columns.

1. Filename of the tile
2. minx
3. maxx
4. miny
5. maxy

Ensure using a point as decimal separator and do not use grouping speparators for the
coordinates. "12345.6789" is ok, "12.345,6789" is not supported.

A csv example::

    oek4000_1_1.tif;109546.250000;429601.536800;333297.122340;573436.750000
    oek4000_1_2.tif;429601.536800;688046.180892;333297.122340;573436.750000
    oek4000_2_1.tif;109546.250000;429601.536800;272461.750000;333297.122340
    oek4000_2_2.tif;429601.536800;688046.180892;272461.750000;333297.122340
