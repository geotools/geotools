WFS-NG Plugin
-------------

Web feature server "next generation". This plugin allows the GeoTools library to interact with a Web Feature Server using the normal DataStore API.

The WFS-NG Plugin is currently unsupported.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-wfs-ng</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

The following connection parameters are defined for WFS.

+--------------------------------------------+------------------------------------------------------------------+
| Param                                      | Description                                                      |
+============================================+==================================================================+
| "WFSDataStoreFactory:GET_CAPABILITIES_URL" | Link to capabilities document.                                   |
|                                            | The implementation supports both WFS 1.0 (read/write) and        |
|                                            | WFS 1.1 (read-only).                                             |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:PROTOCOL"             | Optional: True for Post, False for GET, null for auto            |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:USERNAME"             | Optional                                                         |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:PASSWORD"             | Optional                                                         |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:ENCODING"             | Optional with a default of UTF-8                                 |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:TIMEOUT"              | Optional with a 3000ms default                                   |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:BUFFER_SIZE"          | Optional number of features to read in one gulp, defaults of 10  |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:TRY_GZIP"             | Optional with a default of true, try compression if available    |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:LENIENT"              | Optional default of true.                                        |
|                                            | WFS implementations are terrible for actually obeying their      |
|                                            | DescribeFeatureType schema, setting this to true will try a few  |
|                                            | tricks to support implementations that are mostly correct:       |
|                                            |                                                                  |
|                                            | * Accepting the data in any order                                |
|                                            | * Not getting too upset if the case of the attributes is wrong   |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:MAXFEATURES"          | Limit on the number of features                                  |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:WFS_STRATEGY"         | Optional used to engage specific work arounds for known servers. |
|                                            |                                                                  |
|                                            | * "arcgis"                                                       |
|                                            | * "cuberx"                                                       |
|                                            | * "geoserver"                                                    |
|                                            | * "ionic"                                                        |
|                                            | * "mapserver"                                                    |
|                                            | * "nonstrict"                                                    |
|                                            | * "strict"                                                       |
|                                            | * null - automatic based GET_CAPABILITIES url                    |
|                                            |                                                                  |
|                                            | You may need use this override if you are using mapserver        |
|                                            | with a custom URL not recognised by auto detection.              |
|                                            | WFS1.1 supports autodetection based on full capabilities doc for |
|                                            | greater accuracy.                                                |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:FILTER_COMPLIANCE"    | Optional used override how GetFeature operations encodes filters |
|                                            |                                                                  |
|                                            | * 0 (low compliance) full range of geotools filters represented  |
|                                            | * 1 (medium compliance) Id filters mixed with bbox only          |
|                                            | * 2 (strict compliance) Id filters cannot be combined at all     |
|                                            |                                                                  |
|                                            | In general compliance levels stress the handling of Id filters   |
|                                            | which are not allowed with other filters (AND / OR / NOT).       |
|                                            | You may relax this constraint when working with some WFS         |
|                                            | implementations such as GeoServer.                               |
+--------------------------------------------+------------------------------------------------------------------+
| "WFSDataStoreFactory:USEDEFAULTSRS"        | Optional used override how GetFeature operations send srs to wfs |
|                                            | server                                                           |
|                                            |                                                                  |
|                                            | * false use othersrs if query matches one of them                |
|                                            | * true always use defaultsrs and reproject locally to query crs  |
|                                            |                                                                  |
|                                            | Choose if you prefer to always use DefaultSRS declared in        |
|                                            | capabilities and reproject using GeoTools if necessary, or       |
|                                            | use also OtherSRS if available.                                  |
|                                            | The false value is currently supported in 1.1.0 protocol only.   |
+--------------------------------------------+------------------------------------------------------------------+

Historical Note: We apologise for the long connection parameter keys, WFS was one of the first DataStores written and we were unsure at the
time if they keys for each datastore would need to be unique or not. On the plus side you can see our devotion to stability.

Hints:

* The WFSDataStoreFactory provides keys as static constants if you need to look up the key,
  definition or expected data type programatically
* The capabilities URL you provide is important:
  
  * Q: Does it include "VERSION=1.0.0"?
    If so you are using well tested code parsing GML2, and if available WFS-T will be supported.
  * Q: does it include "VERSION=1.1.0" ? If so you are using newer code parsing GML3, and only WFS is implemented at this time.

* You really should spend some time adjusting these parameters for your data source and application;
  in particular the performance is greatly effected by changing the BUFFER_SIZE.
  
  * Some servers that can serve up lots of small features benefit from higher values such as 100.
  * Slower servers respond well to smaller numbers.
  * You are advised that no features will be returned to code until the buffer is filled, and that the higher the number the more memory the WFS will use.

Web Feature Server
^^^^^^^^^^^^^^^^^^

Support for **Web Feature Server Example (WFS)** offers access to the raw features being served up from an external server.

* You are advised that when using read/write access you will need to use a transaction - see the section of WFS-T for more details.

The following is a quick example; only the connection parameter code is specific to the WFSDataStore.

You can connect to a Web Feature Server via the DataStore API; the connection parameters are as follows::

  String getCapabilities = "http://localhost:8080/geoserver/wfs?REQUEST=GetCapabilities";
  
  Map connectionParameters = new HashMap();
  connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
  
  // Step 2 - connection
  DataStore data = DataStoreFinder.getDataStore( connectionParameters );
  
  // Step 3 - discouvery
  String typeNames[] = data.getTypeNames();
  String typeName = typeNames[0];
  SimpleFeatureType schema = data.getSchema( typeName );
  
  // Step 4 - target
  FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( typeName );
  System.out.println( "Metadata Bounds:"+ source.getBounds() );
  
  // Step 5 - query
  String geomName = schema.getDefaultGeometry().getLocalName();
  Envelope bbox = new Envelope( -100.0, -70, 25, 40 );
  
  FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints() );
  Object polygon = JTS.toGeometry( bbox );
  Intersects filter = ff.intersects( ff.property( geomName ), ff.literal( polygon ) );
  
  Query query = new DefaultQuery( typeName, filter, new String[]{ geomName } );
  FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures( query );
  
  ReferencedEnvelope bounds = new ReferencedEnvelope();
  Iterator<SimpleFeature> iterator = features.iterator();
  try {
      while( iterator.hasNext() ){
          Feature feature = (Feature) iterator.next();
      bounds.include( feature.getBounds() );
  }
      System.out.println( "Calculated Bounds:"+ bounds );
  }
  finally {
      features.close( iterator );
  }
