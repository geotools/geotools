Creating indexes for performance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To achieve performance you must create indexes, depending on your spatial extension.

Universal
'''''''''

Here is a proposal if you have more tiles in the x direction than in the y direction. If the
opposite is true, switch Minx / Miny and Max/ Maxy::

    CREATE INDEX IX_SPATIAL0_1 ON SPATIAL0(MinX,MinY)
    CREATE INDEX IX_SPATIAL0_2 ON SPATIAL0(MaxX,MaxY)

DB2 Spatial Extender
''''''''''''''''''''

Use gseidx utility (ADVISE) to get a proposal for the last 3 parameters (grid sizes)::
   
   CREATE INDEX IX_SPATIAL0 ON SPATIAL0(GEOM) EXTEND USING db2gse.spatial_index (10000.0, 100000.0, 1000000.0)

PostGIS
'''''''

Use::

   CREATE INDEX IX_spatial0 ON spatial0 USING gist(geom)

MySQL
'''''

Use::

    CREATE SPATIAL INDEX IX_SPATIAL0 ON SPATIAL0(GEOM) 

Oracle Location Based Services
''''''''''''''''''''''''''''''

Use::
    
    CREATE INDEX IX_SPATIAL0 ON SPATIAL0(GEOM) INDEXTYPE IS MDSYS.SPATIAL_INDEX
