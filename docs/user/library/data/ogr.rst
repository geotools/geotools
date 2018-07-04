OGR Plugin
----------

DataStore making use of OGR to support a slew of additional data formats.

There are two implementations of the OGR plugin.  The first uses OGR's standard Java API.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-ogr-jni</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Your installation of GDAL/OGR needs to be compiled with Java support to use this module.

The other OGR plugin uses Bridj to create Java bindings to OGR.  This version does not require
GDAL/OGR to be compiled with Java support.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-ogr-bridj</artifactId>
      <version>${geotools.version}</version>
    </dependency>

The OGR DataStore does require the GDAL/OGR native library.  Once installed you need to add the location to your PATH on Windows, DYLD_LIBRARY_PATH on Mac, and LD_LIBRARY_PATH on Linux.  If you use gt-ogr-bridj and the dll/dylib/so file is not named gdal you will need to set the GDAL_LIBRARY_NAME.  Often it is something like gdal10 depending on the version of GDAL you installed.  You will also have to set the java.library.path to the GDAL's location when running your program.

You can then access the module by creating a DataStoreFactory.

If you are using Bridj::

    OGRDataStoreFactory factory = new BridjOGRDataStoreFactory();

or if you are using JNI::

    OGRDataStoreFactory factory = new JniOGRDataStoreFactory();

You can list available drivers::

    Set<String> drivers = factory.getAvailableDrivers();
    for (String driver : drivers) {
        System.out.println(driver);
    }

To connect to an OGR layer there are two parameters:

* DriverName = The name of the OGR Driver (http://www.gdal.org/ogr_formats.html). 

* DataSourceName = The name of the file or data source connection. This value depends on what driver you are using.

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
    OGRDataStoreFactory factory = new BridjOGRDataStoreFactory();
    OGRDataStore dataStore = (OGRDataStore) factory.createNewDataStore(connectionParams);
    dataStore.createSchema(features, true, null);

And here is now you would write to and then read a SQLite database::

    SimpleFeatureCollection features = getFeatures();
    File file = new File("features.db");
    Map<String, String> connectionParams = new HashMap<String, String>();
    connectionParams.put("DriverName", "SQLite");
    connectionParams.put("DatasourceName", file.getAbsolutePath());
    OGRDataStoreFactory factory = new BridjOGRDataStoreFactory();
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

