Oracle Plugin
-------------

.. note::
   
   Unsupported. Use of this plugin is discouraged as a rewrite is available
   in the form of **gt-jdbc-oracle**.
   
   The rewrite uses the same connection parameters allowing you an
   easy transition.

Plugin providing direct access to the Oracle Spatial database.

Please keep in mind this module is in popular use but is unsupported as it does not have a module maintainer available to answer your questions and apply patches.

The following shows how to connect to a Oracle databse, and read a table (in this case called RIVERS).::

    java.util.Map params = new java.util.HashMap();
    
    // required parameters
    params.put("dbtype", "oracle");
    params.put("host", "localhost");
    params.put("port", 1521);
    params.put("user", "fred");
    params.put("passwd","abc123");
    params.put("instance","example");
    
    // optional parameters
    params.put("schema", "GEOTOOLS"); // schema name to narrow down exposed table must be uppercase
    params.put("namespace", "http://foo.org/census" ); // namespace when constructing features
    
    // optional connection handling parameters
    params.put("max connections", 5); // default is 10
    params.put("min connections", 2); // default is 4
    params.put("validate connections", true ); // default is false
    
    DataStore dataStore=DataStoreFinder.getDataStore(params);
    String typeName = dataStore.getTypeNames()[0];
    
    FeatureSource featureSource = dataStore.getFeatureSource( typeName );
    The informatoin about connection parameters is available at runtime:
    
    OracleDataStoreFactory factory = new OracleDataStoreFactory();
    Param parms[] = factory.getParametersInfo();
    
I am afraid this being an unsupported module the javadocs are a bit out of date.
