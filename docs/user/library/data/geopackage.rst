GeoPackage Plugin
-----------------

Supports direct access to a GeoPackage database.

GeoPackage is an open, standards-based, platform-independent, portable, self-describing, compact format for transferring geospatial information.

The module supports both vector data, as GeoPackage features, and raster data, as GeoPackage tiles. Access to a GeoPackage
can be performed either low level, using the GeoPackage class, or high level, using the GeoPackage JDBC data store
for vector data, and the GeoPackageReader for raster data.

References:

  * http://www.sqlite.org/
  * http://www.geopackage.org/

**Maven**

::

   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-geopkg</artifactId>
      <version>${geotools.version}</version>
    </dependency>


DataStore Connection Parameters
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "geopkg"
"database"     The database to connect to
"user"         User name (optional)
============== ============================================

Access
^^^^^^

Example use::
  
  Map params = new HashMap();
  params.put("dbtype", "geopkg");
  params.put("database", "test.gkpg");
  
  DataStore datastore = DataStoreFinder.getDataStore(params);


High level coverage reader
^^^^^^^^^^^^^^^^^^^^^^^^^^

The high level coverage reader can access all tile entries found in the package, exposing each one as
a separate coverage.

.. code-block:: java

        GeoPackageReader reader = new GeoPackageReader(getClass().getResource("world_lakes.gpkg"), null);
        System.out.println(Arrays.asList(reader.getGridCoverageNames()));
        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(500,500)), new ReferencedEnvelope(0,180.0,-85.0,0,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("World_Lakes", parameters);  

Adding a feature entry using the low level API
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A GeoPackage with a feature entry can be created using the following low level code:

.. code-block:: java

        GeoPackage geopkg = new GeoPackage(File.createTempFile("geopkg", "db", new File("target")));
        geopkg.init();
        
        ShapefileDataStore shp = ...

        FeatureEntry entry = new FeatureEntry();
        geopkg.add(entry, shp.getFeatureSource(), null);

Once created, the features in the entry can be read using a SimpleFeatureReader:
        
.. code-block:: java        
        
        try(SimpleFeatureReader r = geopkg.reader(entry, null, null)) {
          while(r.hasNext()) {
              SimpleFeature sf = r.next());
          }
        }

The parallel ``writer`` method can be used to grab a SimpleFeatureWriter to modify existing features.

Adding a tile entry using the low level API
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A GeoPackage with a tile entry can be created using the following low level code:

.. code-block:: java

        GeoPackage geopkg = new GeoPackage(File.createTempFile("geopkg", "db", new File("target")));
        geopkg.init();

        TileEntry e = new TileEntry();
        e.setTableName("foo");
        e.setBounds(new ReferencedEnvelope(-180,180,-90,90,DefaultGeographicCRS.WGS84));
        e.getTileMatricies().add(new TileMatrix(0, 1, 1, 256, 256, 0.1, 0.1));
        e.getTileMatricies().add(new TileMatrix(1, 2, 2, 256, 256, 0.1, 0.1));

        geopkg.create(e);
        assertTileEntry(e);

        List<Tile> tiles = new ArrayList();
        tiles.add(new Tile(0,0,0,new byte[]{...}));
        tiles.add(new Tile(1,0,0,new byte[]{...}));
        tiles.add(new Tile(1,0,1,new byte[]{...}));
        tiles.add(new Tile(1,1,0,new byte[]{...}));
        tiles.add(new Tile(1,1,1,new byte[]{...}));

        for (Tile t : tiles) {
            geopkg.add(e, t);
        }

Tile can then be read back using a ``TileReader``, as follows (the zoom and row/col limits can be set to null to read everything):

.. code-block:: java

        try(TileReader r = geopkg.reader(e, lowZoom, highZoom, lowCol, highCol, lowRow, highRow)) {
            while(r.hasNext()) {
              Tile a = r.next();
              // do something with the tile
            }
        }

