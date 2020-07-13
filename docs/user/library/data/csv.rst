CSV Plugin
----------

The ``gt-csv`` module is a plugin which provides a ``CSVDataStore``. It is pluggable into GeoServer's importer, allowing read/write capabilities for :file:`.csv` files. The plugin supports three strategies: ``AttributesOnly``, ``LatLon``, and ``SpecifiedWKT``. Essentially, each strategy will read and store data in a slightly different way.

**Maven**::

   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-csv</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

The following connection parameters are available:

+--------------------------+---------------------------------------------------+
| Parameter                | Description                                       |
+==========================+===================================================+
| ``url``                  | A URL of the file ending in ``csv``               |
+--------------------------+---------------------------------------------------+
| ``strategy``             | The strategy to use in processing the input file  |
|                          |                                                   |
|                          | 1. ``guess`` - try to figure out best strategy    |
|                          |                                                   |
|                          | 2. ``AttributesOnly`` - read in all fields as is  |
|                          |                                                   |
|                          | 3. ``specify`` - use the specified lat/lon columns|
|                          |                                                   |
|                          | 4. ``wkt`` - use a field containing WKT           |
+--------------------------+---------------------------------------------------+
| ``latField``             | The field containing the latitude value in        |
|                          | ``specify`` strategy.                             |
+--------------------------+---------------------------------------------------+
| ``lngField``             | The field containing the longitude value in       |
|                          | ``specify`` strategy.                             |
+--------------------------+---------------------------------------------------+
| ``wktField``             | The field containing the WKT value in             |
|                          | ``WKT`` strategy.                                 |
+--------------------------+---------------------------------------------------+
| ``writeprj``             | write out a ``.prj`` file with the CRS (Optional) |
+--------------------------+---------------------------------------------------+
| ``quoteAll``             | Should all fields be quoted when writing, default |
|                          | is `false` (Optional).                            |
+--------------------------+---------------------------------------------------+
| ``quoteChar``            | Character to use to quote field, default is ``"`` |
|                          | (Optional).                                       |
+--------------------------+---------------------------------------------------+
| ``separator``            | Character to use to separate fields, default is   |
|                          | ``,`` (Optional).                                 |
+--------------------------+---------------------------------------------------+
| ``lineSeperator``        | String used to separate lines, default is the     |
|                          | system line ending (Optional).                    |
+--------------------------+---------------------------------------------------+
| ``escapeChar``           | Character used to escape quotes in fields, default|
|                          | ``\\`` (Optional).                                |
+--------------------------+---------------------------------------------------+
| ``namespace``            | URI to the namespace (Optional).                  |
+--------------------------+---------------------------------------------------+

Strategies 
^^^^^^^^^^

LatLon Strategy
'''''''''''''''

This strategy uses the two field names set in ``latField`` and ``lngField`` and
converts the values in these fields into a ``Point`` object in the feature
called ``location``.

Guess Strategy
''''''''''''''

This strategy looks through the field names and picks one that is equal to
``longitude``, ``lon``, ``long`` or ``lng`` to use for longitude. For latitude
it looks for fields called ``latitude`` or ``lat``.
It then
converts the values in these fields into a ``Point`` object in the feature
called ``location``.

WKT Strategy
''''''''''''

This strategy parses the value in the field specified in ``wktField`` into a
WGS84 geometry object in the features with the same name as the field. 

AttributesOnly Strategy
'''''''''''''''''''''''

This strategy reads all the fields in the file in unmodified.


Access
^^^^^^

Reading in an existing file::

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("file", "locations.csv");
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params.put(CSVDataStoreFactory.LATFIELDP.key, "LAT");
        params.put(CSVDataStoreFactory.LnGFIELDP.key, "LON");
        DataStore store = DataStoreFinder.getDataStore(params);

        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source =
                dataStore.getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                System.out.print(feature.getID());
                System.out.print(": ");
                System.out.println(feature.getDefaultGeometryProperty().getValue());
            }
        }


Writing
^^^^^^^^

To write to a CSV file::

        File file2 = File.createTempFile("CSVTest", ".csv");
        Map<String, Serializable> params2 = new HashMap<String, Serializable>();
        params2.put("file", file2);
        params2.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params2.put(CSVDataStoreFactory.WKTP.key, "the_geom_wkt");

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore datastore = factory.createNewDataStore(params2);
        SimpleFeatureType featureType = store.getSchema();

        datastore.createSchema(featureType);
        SimpleFeatureSource source = datastore.getFeatureSource();
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;
            outStore.addFeatures(store.getFeatureSource().getFeatures());
        } else {
            fail("Can't write to new CSVDatastore");
        }
