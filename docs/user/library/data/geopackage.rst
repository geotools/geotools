GeoPackage Plugin
-----------------

Supports direct access to a GeoPackage database.

GeoPackage is an open, standards-based, platform-independent, portable, self-describing, compact format for transferring geospatial information.

The module supports both vector data, as GeoPackage features, and raster data, as GeoPackage tiles. Access to a GeoPackage
can be performed either low level, using the GeoPackage class, or high level, using the GeoPackage JDBC data store
for vector data, and the ``GeoPackageReader`` for raster data.

References:

  * http://www.sqlite.org/
  * http://www.geopackage.org/

**Maven**

.. code-block:: xml

   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-geopkg</artifactId>
      <version>${geotools.version}</version>
    </dependency>


DataStore API
^^^^^^^^^^^^^

GeoTools provides direct access to JDBCDataStore implementation for accessing feature contents as expressed in the specification:

> A GeoPackage with a ``gpkg_contents`` table row with a "features" data_type SHALL contain a ``gpkg_geometry_columns`` table per Table 5 and ``gpkg_geometry_columns`` Table Definition SQL.

Spatial index is supported with the use of ``gpkg_rtree_index``.

The ``JDBCDataStore.createVirtualTable`` functionality is not supported (as the ``gpkg_geometry_columns`` information is not available for ad-hoc SQL queries).

Connection Parameters
'''''''''''''''''''''

.. list-table:: Connection Parameters
   :widths: 30 79
   :header-rows: 1

   * - Parameter
     - Description
   * - ``dbtype``
     - Must be the string ``geopkg``
   * - ``database``
     - The database filename to connect to (either complete path or relative path).
   * - ``read_only``
     - Use Boolean.TRUE to open in read-only mode (optional)
   * - ``memory map size``
     - SQLite memory map size in MB

Use ``read-only`` for best performance, allowing SQLite to ignore the complexity of transactions.

The ``database`` parameter above is specified as a path to the GeoPackage database. If using a relative path a base directory can be provided to the ``GeoPkgDataStoreFactory`` instance prior to use:

.. code-block:: java
   
   for( DataStoreFactorySPI factory : DataStoreFinder.getAvailableDataStores() ){
      if( factory instanceof GeoPkgDataStoreFactory){
          GeoPkgDataStoreFactory geopkgFactory = (GeoPkgDataStoreFactory) factory;
          geopkgFactory.setBaseDirectory( directory );
      }
   }


Feature Access
''''''''''''''

Example use:

.. code-block:: java
  
   Map params = new HashMap();
   params.put("dbtype", "geopkg");
   params.put("database", "test.gkpg");
   params.put("read-only", true);
  
   DataStore datastore = DataStoreFinder.getDataStore(params);

Care should be taken when using `DataStore.create( schema )` as the GeoPackage specification requires features to be stored in XYZM order.

GridCoverage API
^^^^^^^^^^^^^^^^

The high level coverage reader can access all tile entries found in the package, exposing each one as
a separate coverage.

.. code-block:: java

        GeoPackageReader reader = new GeoPackageReader(getClass().getResource("world_lakes.gpkg"), null);
        
        System.out.println(Arrays.asList(reader.getGridCoverageNames()));
        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(new Rectangle(500,500)), new ReferencedEnvelope(0,180.0,-85.0,0,WGS_84));
        parameters[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read("World_Lakes", parameters);  

GeoPackage API
^^^^^^^^^^^^^^

In addition to the GeoTools DataStore and GridCoverage access a low-level API is provided to directly manage the contents of a GeoPackage.

Adding a feature entry
^^^^^^^^^^^^^^^^^^^^^^

A GeoPackage with a feature entry can be created using the following low level code:

.. code-block:: java

        GeoPackage geopkg = new GeoPackage(File.createTempFile("geopkg", "db", new File("target")));
        geopkg.init();
        
        FeatureEntry entry = new FeatureEntry();
        entry.setDescription("Cities of the world");
        geopkg.add(entry, featureCollection);
        geopkg.createSpatialIndex(entry);

Note:

* This example shows direct access to additional features and extensions, such as the ``createSpatialIndex(entry)`` above.
* GeoPackage requires that features are stored in XYZM order, the featureCollection used as the initial contents will be written to disk in this order.

Once created, the features in the entry can be read using a SimpleFeatureReader:
        
.. code-block:: java        
        
        try(SimpleFeatureReader r = geopkg.reader(entry, null, null)) {
          while(r.hasNext()) {
              SimpleFeature sf = r.next());
          }
        }

The parallel ``writer`` method can be used to acquire a SimpleFeatureWriter to modify existing features.

Adding a tile entry
^^^^^^^^^^^^^^^^^^^

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

Using GeoPackage Extensions
^^^^^^^^^^^^^^^^^^^^^^^^^^^

The GeoPackage specification is modular using the concepts of extensions to support additional functionality:

* ``GeoPkgExtension`` - base class for geopackage extensions
* ``GeoPkgExtensionFactory`` - used to advertise additional extensions provided by client code

The GeoPackage module supports the following extensions:

* ``GeoPkgMetadataExtension`` - Uses ``geopkg_metadata`` and and ``geopkg_metadata_reference`` to store metadata.
* ``GeoPkgSchemaExtension`` - Allows additional description of table columns.

GeoPackageProcessRequest
^^^^^^^^^^^^^^^^^^^^^^^^

This java bean (and xml bindings) is used to support the GeoServer WPS GeoPackage process which supports the creation of GeoPackages with additional extensions.

* ``GeoPackageProcessRequest.FeatureLayer``
* ``GeoPackageProcessRequest.TileLayer``

These classes cannot directly be used by GeoTools code.