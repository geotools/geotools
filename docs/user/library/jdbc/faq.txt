JDBC FAQ
--------

Q: How to access a database from Java Enterprise Edition?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Depending on your application container (and organisational polices) you may be restricted to only using database connection pools under control of your administrator.

Your administrator will register the database connection
pool with the application server and configure your application with the Java Naming and Directory Interface (JNDI) reference.

To use GeoTools with a JNDI name::
  
  Map map = new HashMap();
  map.put( "dbtype", "postgis");
  map.put( "jndiReferenceName", "java:comp/env/jdbc/geotools");
  
  DataStore store =  DataStoreFinder.getDataStore(map);

The specific "dbtype" marks the plugin you will use to communicate with the database.