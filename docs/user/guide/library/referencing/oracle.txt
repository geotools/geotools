EPSG Oracle Plugin
------------------

The gt-epsg-oracle module makes available to your application a CRSAuthority implementation that is backed by an Oracle database. You will need to load your oracle database with the tables distributed by the www.epsg.org.

Notes:

* The gt-epsg-oracle module is unsupported:
  
  * There is no module maintainer available for assistance (please use the user list for any problem discussions)
  * It is not subject to GeoTools quality assurance standards prior to release

* Please remember that only one authority for "EPSG" should be on your CLASSPATH at a time in order to avoid conflict.

Loading EPSG Tables
^^^^^^^^^^^^^^^^^^^

You can now directly download the EPSG database in oracle form (previously you had to run a script to convert the access database into a format Oracle could import).

1. Go to: http://www.epsg.org/CurrentDB.html
2. Go down to the shipping List and find a file similar to the following:
   
   * Version 6.12: epsg-v6_12sql-Oracle.zip

3. Use the EPSG_v6_12.mdb_Tables_Oracle.sql file to SQL CREATE all the tables
4. Use the EPSG_v6_12.mdb_Data_Oracle.sql file to SQL INSERT all the data
5. Use the EPSG_v6_12.mdb_FKeys_Oracle.sql file to SQL ALTER get all the keys straightened out

Configuration
^^^^^^^^^^^^^^

The gt-epsg-oracle module is set up to use a DataSource that you provide.::
    
    ReferencingFactoryContainer container = new ReferencingFactoryContainer( hints );


**Oracle DataSource**

The first example is the use of **OracleDataSource** - the one provided by your oracle driver.

Direct use of OracleDataSource::
    
    OracleDataSource source = new OracleDataSource();
    source.setUser( user );
    source.setPassword( password );
    source.setURL( url );
    
    Hints hints = new Hints( Hints.EPSG_DATA_SOURCE, source );

Although the above example shows creating an OracleDataSource; usually the instance is configured by your application container and needs to be obtained using a JNDI lookup.

**BasicDataSource**

The next example is the use of BasicDataSource as provided as part of the commons dbcp project. This is a great choice when using a desktop application.

The following connection management facilities are provided:
  
  Hints.EPSG_DATA_SOURCE
    A DataSource instance, or JNDI name
  
  Hints.AUTHORITY_MAX_ACTIVE
    Maximum number of connections used
    
    Controls the number of database connections the **gt-epsg-module** will use at one time.

  Hints.AUTHORITY_MAX_IDLE
   Max number of connection at rest
  
  Hints.AUTHORITY_MIN_EVICT_IDLETIME
    How long before to wait before reclaiming an unused connection
  
  Hints.AUTHORITY_TIME_BETWEEN_EVICTION_RUNS
    How often we check for idle connections

  **Reserved Connections**
  
  Hints.AUTHORITY_MIN_IDLE
    Minimum number of connection at rest
  
  Hints.AUTHORITY_SOFTMIN_EVICT_IDLETIME
    How do we ensure we have this many connections
  
  **Cache Control**
  
  Hints.CACHE_POLICY
    Use "weak", "all", "fixed" or "none"
  
  Hints.CACHE_LIMIT
    Limit on the number of cached results
  
Example using the popular commons-dbcp implementation of DataSource::
    
    BasicDataSource source = new BasicDataSource();
    source.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    source.setUsername( user );
    source.setPassword( password );
    source.setUrl( url );
    source.setMaxActive(10);
    source.setMaxIdle(1); 
    
    Map config = new HashMap();
    config.put( Hints.EPSG_DATA_SOURCE, source );
    config.put( Hints.AUTHORITY_MAX_ACTIVE, new Intenger( 3 ));
    config.put( Hints.AUTHORITY_MAX_IDLE, new Integer( 1 ));
    config.put( Hints.AUTHORITY_MIN_IDLE, new Integer( 0 ));
    

Please be careful can configure your BasicDataSource to provide more connections then the gt-epsg-oracle module will ask for.:

* Number of available connections: source.setMaxActive(10)
* Number of connections used::
    
    new Hints(Hints.AUTHORITY_MAX_ACTIVE, new Integer(3) );

* Ensure that we return the connections (before the DataSource reclaims them)::
    
    Hints.AUTHORITY_MIN_IDLE, new Integer( 0 );

* If you do not do this the gt-epsg-oracle module will encounter problems of the following form.
    
    Database failure while creating a 'CoordinateReferenceSystem' object for code "4326"

**JNDI**

If you are working in an JNDI environment (like a J2EE application) you can specify the name used to lookup the DataSource.::
  
  Hints hints = new Hints( Hints.EPSG_DATA_SOURCE, "jdbc/EPSG" );

You may want to be careful and use a proper JNDI Name::
  
  Name name = initialContext.combineName( "jdbc", "EPSG" );
  Hints hints = new Hints( Hints.EPSG_DATA_SOURCE, name );

Performance
^^^^^^^^^^^

The following hints effect the performance of epsg-oracle plugin and may be used for performance tuning.

* Desktop
  
  There are only about 8000 things in the EPSG database, you may want to cache them all if you are a desktop
  application::
    
    BasicDataSource source = new BasicDataSource();
    source.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    source.setUsername( user );
    source.setPassword( password );
    source.setUrl( url );
    source.setMaxActive(5);
    source.setMaxIdle(1); 
    
    Map config = new HashMap();
    config.put( Hints.EPSG_DATA_SOURCE, source );
    config.put( Hints.AUTHORITY_MAX_ACTIVE, 5 );
    config.put( Hints.AUTHORITY_MAX_IDLE, 1 );
    config.put( Hints.CACHE_POLICY, "all" );
    
    Hints hints = new Hints( config );
  
  We are going to keep one one "idle" connection available (until it times out) on the off chance we need it again in a
  hurry. A single client is not going to need many connections at once - and after a while the cache gradually take over
  and prevent us using the database at all.
  
  The cache policy of all does have the risk of using up a lot of memory (MathTransforms and so on are cached as you use
  CoordinateReferenceSystems and so on).

* Server
  
  In a "proper" server environment we need to return the connections as soon as possible. The following settings will
  keep 3 connections in reserve (until they time out) in order to quickly respond to multiple threads.::
    
    BasicDataSource source = new BasicDataSource();
    source.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    source.setUsername( user );
    source.setPassword( password );
    source.setUrl( url );
    source.setMaxActive(20);
    source.setMaxIdle(3); 
    
    Map config = new HashMap();
    config.put( Hints.EPSG_DATA_SOURCE, source );
    config.put( Hints.AUTHORITY_MAX_ACTIVE, 20 );
    config.put( Hints.AUTHORITY_MAX_IDLE, 3 );
    config.put( Hints.CACHE_POLICY, "weak" );
    config.put( Hints.CACHE_LIMT, 1000 );
    
    Hints hints = new Hints( config );
  
  We are using a "weak" cache that will return memory used by coordinate reference systems objects when they are no
  longer in use by any thread. For this server we are expecting only 100 coordinate reference systems to be used (WSG84
  and the UTM zones), but we have chosen a CACHE_LIMIT of 1000 in order to account for all the MathTransforms between
  these projections.

* Memory
  
  In this configuration we are going to hold a connection open and not cache anything.::
    
    BasicDataSource source = new BasicDataSource();
    source.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    source.setUsername( user );
    source.setPassword( password );
    source.setUrl( url );
    source.setMaxActive(5);
    source.setMaxIdle(2);  
    
    Map config = new HashMap();
    config.put( Hints.EPSG_DATA_SOURCE, source );
    config.put( Hints.AUTHORITY_MAX_ACTIVE, 5 );
    config.put( Hints.AUTHORITY_MAX_IDLE, 2 );
    config.put( Hints.AUTHORITY_MIN_IDLE, 1 );
    config.put( Hints.CACHE_POLICY, "none" );
    
    Hints hints = new Hints( config );
  
  Because we are always going to get a cache miss we are going to hold at least one connection open in order to respond
  quickly to requests. When working with an open connection the OracleDialectEPSGFactory is quite quick. Please note
  that we are only retrieving the definitions from the database, the referencing subsystem will still "intern"
  CoordinateReferenceSystem objects (it remembers what objects are in use so that it can prevent the creation of
  duplicates).
