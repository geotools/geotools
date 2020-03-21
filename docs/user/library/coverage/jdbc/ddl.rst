Using the java ddl generation utility
-------------------------------------

Call Syntax::
  
  java -jar gt-imagemosaicjdbc ddl   -config URLOrFileName -spatialTNPrefix spatialTNPrefix [-tileTNPrefix tileTNPrefix]
        [-pyramids pyramids] -statementDelim statementDelim [-srs srs ] -targetDir

Options:

* ``-config``  
  The URL or the file name of your XML configuration

* ``-spatialTNPrefix``  
  The Prefix of the spatial tables. A prefix of "SPAT" results in tables SPAT_0,SPAT_1,....

* ``-tileTNPrefix``
  
  The Prefix of the tile tables. Only needed if you plan to store image data in a separate table.

* ``-pyramids``
  
  Number of pyramid tables

* ``-statementDelim``
  
  The SQL statement delimiter used in the scripts

* ``-srs``
  
  The SRS needed for a spatial extension. Not required for universal and MySQL.

* ``-targetDir``
  
  The directory where to put the script templates.

Generated Scripts
^^^^^^^^^^^^^^^^^

``createmeta.sql``
  The create table statement for the meta table

  ``dropmeta.sql``
  The drop table statement for the meta table

  ``add_<coverage_name>.sql``
  All statements for adding a coverage named <coverage_name> (taken from the configuration)

  ``remove_<coverage_name>.sql``
  All statements for removing a coverage named <coverage_name> (taken from the configuration
  )
