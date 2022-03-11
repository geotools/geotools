OGR Plugin
----------

DataStore making use of OGR to support a slew of additional data formats.

In order to use the OGR datastore, add the following dependency:

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-ogr-jni</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Your installation of GDAL/OGR needs to be compiled with Java support to use this module.

The OGR DataStore does require the GDAL/OGR native library:

* Add the location to your ``PATH`` on Windows, or ``LD_LIBRARY_PATH`` on Linux.
* Include this GDAL install location in the ``java.library.path`` system property when running your program.
* macOS users are advised to use ``java.library.path` system property (``DYLD_LIBRARY_PATH`` and ``DYLD_FALLBACK_LIBRARY_PATH`` environmental variables not recommended).

You can then access the module by creating a ``DataStoreFactory``.

    OGRDataStoreFactory factory = new JniOGRDataStoreFactory();

You can list available drivers::

    Set<String> drivers = factory.getAvailableDrivers();
    for (String driver : drivers) {
        System.out.println(driver);
    }

To connect to an OGR layer there are two parameters:

* ``DriverName`` = The name of the OGR Driver (http://www.gdal.org/ogr_formats.html). 

* ``DataSourceName`` = The name of the file or data source connection. This value depends on what driver you are using.

Here is how you would read a shapefile::

    Map<String, String> connectionParams = new HashMap<String, String>();
    connectionParams.put("DriverName", "ESRI Shapefile");
    connectionParams.put("DatasourceName", new File("/Some/where/states.shp").getAbsolutePath());
    DataStore store = factory.createDataStore(connectionParams);
    SimpleFeatureSource source = store.getFeatureSource("states");

    SimpleFeatureIterator it = source.getFeatures().features();
    try {
        while (it.hasNext()) {
            SimpleFeature feature = it.next();
            System.out.println(feature.getAttribute("NAME"));
            System.out.println(((Geometry) feature.getDefaultGeometry()).getCentroid());
        }
    } finally {
       it.close();
    }

Here is how you would count the features in a GeoJSON file. See the `GDAL GeoJSON documentation <http://www.gdal.org/drv_geojson.html>`_ for layer naming details::

    File file = new File("states.geojson");
    Map<String, String> connectionParams = new HashMap<String, String>();
    connectionParams.put("DriverName", "GeoJSON");
    connectionParams.put("DatasourceName", file.getAbsolutePath());
    DataStore dataStore = DataStoreFinder.getDataStore(connectionParams);
    System.out.println(dataStore.getFeatureSource(dataStore.getTypeNames()[0]).getCount(Query.ALL));

Here is how you would write a GeoJSON file::

    SimpleFeatureCollection features = getFeatures();
    File file = new File("my.geojson");
    Map<String, String> connectionParams = new HashMap<String, String>();
    connectionParams.put("DriverName", "GeoJSON");
    connectionParams.put("DatasourceName", file.getAbsolutePath());
    OGRDataStoreFactory factory = new JniOGRDataStoreFactory();
    OGRDataStore dataStore = (OGRDataStore) factory.createNewDataStore(connectionParams);
    dataStore.createSchema(features, true, null);

And here is now you would write to and then read a SQLite database::

    SimpleFeatureCollection features = getFeatures();
    File file = new File("features.db");
    Map<String, String> connectionParams = new HashMap<String, String>();
    connectionParams.put("DriverName", "SQLite");
    connectionParams.put("DatasourceName", file.getAbsolutePath());
    OGRDataStoreFactory factory = new JniOGRDataStoreFactory();
    OGRDataStore dataStore = (OGRDataStore) factory.createNewDataStore(connectionParams);
    dataStore.createSchema(features, false, null);

    SimpleFeatureSource geoJsonSource = dataStore.getFeatureSource("states");
    System.out.println("SQLite Features: " + geoJsonSource.getCount(Query.ALL));
    SimpleFeatureIterator it = geoJsonSource.getFeatures().features();
    try {
        while (it.hasNext()) {
            SimpleFeature feature = it.next();
            System.out.println(feature.getAttribute("NAME"));
            System.out.println(((Geometry) feature.getDefaultGeometry()).getCentroid());
        }
    } finally {
        it.close();
    }
    dataStore.dispose();

