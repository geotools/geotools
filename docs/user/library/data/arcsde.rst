ArcSDE Plugin
-------------

The ArcSDE plugin allows the GeoTools developer to work with the `ArcSDE <http://en.wikipedia.org/wiki/ArcSDE>`_ middle ware. The ArcSDE plugin has been with us since GeoTools 2.0 and is much loved.

Supported Features:

* `Connection Pooling`: Allows to configure a DataStore to hold a number of connections to ArcSDE in order to improve performance under concurrency
* `Transactions`: Support for native ArcSDE transactions is embedded into the GeoTools ArcSDE DataStore. Transaction support is available for any registered ArcSDE featureclass with a proper "unique identifier", either `SDE_MANAGED` or `USER_MANAGED`. Featureclasses with no `row id` are available read-only, as GeoTools needs a steady way of assigning Featrure Identifiers.
* `Direct Connect`: Allows clients to directly connect to an SDE GEODB 9.2+ without a need of an SDE server instance           
* `Versioning`:  An ArcSDEDataStore instance may connect to a specific ArcSDE database `version <http://help.arcgis.com/en/arcgisserver/10.0/help/arcgis_server_dotnet_help/index.html#/What_is_a_version/009300001612000000/>`_
* `Geometry-less` Feature Types: "Non spatial" registered ArcSDE tables can be accessed as GeoTools FeatureTypes
* `Spatial views`: Registered ArcSDE spatial-views are supported read-only.

**References**

* :doc:`gt-coverage arcsde <../coverage/arcsde>` raster code examples
* `Esri ArcSDE documentation <http://help.arcgis.com/en/arcgisserver/10.0/help/arcgis_server_dotnet_help/index.html#/What_is_ArcSDE/009300000115000000/>`_

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-arcsde</artifactId>
      <version>${geotools.version}</version>
    </dependency>
   
Configuration Parameters
^^^^^^^^^^^^^^^^^^^^^^^^

To connect to an ArcSDE instance you must have the following parameters:

+-----------------+-------------------------------------------------------------------+
| Parameter       | Description                                                       |
+=================+===================================================================+
| "dbtype"        | Required: must be "arcsde"                                        |
+-----------------+-------------------------------------------------------------------+
|"server"         | Required: The machine which the ArcSDE instance is running on.    |
+-----------------+-------------------------------------------------------------------+
|"port"           | Required: The port the ArcSDE instance is running on.             |
|                 | The default is 5151, or if you're using Direct Connect, check the |
|                 | :ref:`direct-connect`                                             |
|                 | section for further information on how to format this parameter.  |
+-----------------+-------------------------------------------------------------------+
|"instance"       | Optional: The ArcSDE instance name (generally "sde", or whatever  |
|                 | you called the database).                                         |
+-----------------+-------------------------------------------------------------------+
|"user"           | Required: The name of the user to connect with.                   |
+-----------------+-------------------------------------------------------------------+
|"password"       | Required: The password of the user you are connecting with.       |
+-----------------+-------------------------------------------------------------------+

Access
^^^^^^

There's nothing special from the GeoTools API point of view in accessing an ArcSDE server. Just use the regular GeoTools DataStore creation mechanism with the correct parameters::
  
  Map map = new HashMap();
  map.put( "dbtype", "arcsde" );
  map.put( "server", "my.arcsdeserver.com" );
  map.put( "port", "5151" );
  map.put( "instance", "sde" );
  map.put( "user", "sde" );
  map.put( "password", "secret" );

  DataStore dataStore = DataStoreFinder.getDataStore( Map map );
  String typeName = dataStore.getTypeNames()[0];
  
  FeatureSource source = dataStore( typeName );
  
  Filter filter = CQL.toFilter("BBOX(SHAPE, -180,-90,0,0)");
  FeatureCollection collection = source.getFeatures( filter );
  FeatureIterator iterator = collection.iterator();
  try {
      while( iterator.hasNext() ){
           Feature feature = (Feature) iterator.next();
           ...
      }
  }
  finally {
     collection.close( iterator );
  }

Advanced
^^^^^^^^

There are also a number of optional parameters to configure the ArcSDE connection 'pool'. GeoTools makes use of a number of connections, but does a decent job of managing them, so that new connections need not be made for each request. The big reason for this is that some ArcSDE licenses only allow a limited number of connections, so these values can be adjusted to minimize the number of simultaneous connections. GeoTools will share the active connections instead of making new ones. Right now it requires at least two connections, as we were having some nasty bugs with ArcSDE connections going stale (only with certain instances, and only with spatial queries, which made debugging a big hassle). With some funded development work this could easily be improved, ArcSDE could definitely benefit from some more effort. If you have more connections available we do recommend upping the pool.maxConnections parameter. The meaning of these optional parameters is as follows:

+----------------------------------+-----------------------------------------------------------------+
| Parameter                       | Description                                                      |
+=================================+==================================================================+
|"pool.minConnections"            | The number of connections the pool makes on start up. If needed  |
|                                 | these will be incremented.                                       |
+---------------------------------+------------------------------------------------------------------+
|"pool.maxConnections"            | The maximum number of connections that a pool is allowed to      |
|                                 | make. This should be as high as possible, but there may be       |
|                                 | license limitations.                                             |
+---------------------------------+------------------------------------------------------------------+
|"pool.timeOut"                   | The amount of time that a connection request should wait for an  |
|                                 | unused connection before failing.                                |
+---------------------------------+------------------------------------------------------------------+
|"namespace"                      | A String literal representing the namespace URL FeatureTypes     |
|                                 | created by this DataStore will be assigned to. E.g.:             |
|                                 | ``http://my.company.com/testNamespace``                          |
+---------------------------------+------------------------------------------------------------------+
|"database.version"               | The ArcSDE database version to use.                              |
+---------------------------------+------------------------------------------------------------------+
|"datastore.allowNonSpatialTables"| ``true|false`` If enabled, registered non-spatial tables         |
|                                 |  are also available.                                             |
+---------------------------------+------------------------------------------------------------------+

.. _direct-connect:

Configuring with Direct Connect
'''''''''''''''''''''''''''''''

ESRI Direct Connect[ESRI DC] allows clients to directly connect to an SDE GEODB 9.2+ without a need of an SDE server instance, and is recommended for high availability environments, as it removes the ArcSDE gateway server as a single point of failure.
ESRI DC needs additional platform dependent binary drivers and a working Oracle Client ENVIRONMENT (if connecting to an ORACLE DB). See `Properties of a direct connection to an ArcSDE geodatabase <http://webhelp.esri.com/arcgisserver/9.3/java/index.htm#geodatabases/setting1995868008.htm>`_ in the ESRI ArcSDE documentation for more information on Direct Connect, and `Setting up clients for a direct connection <http://webhelp.esri.com/arcgisserver/9.3/java/index.htm#geodatabases/setting1995868008.htm>`_ for information about connecting to the different databases supported by ArcSDE.

The GeoTools ArcSDE configuration parameters are the same as in the `Configuration Parameters` section above, with a couple differences in how to format the parameters:

 * server: In ESRI Direct Connect Mode a value must be given or the Direct Connect Driver will throw an error, so just put a 'none' there - any String will work!
 * port: In ESRI Direct Connect Mode the port has a String representation: `sde:oracle10g`, `sde:oracle11g:/:test`, etc. For further information check `ArcSDE connection syntax <http://webhelp.esri.com/arcgisserver/9.3/java/geodatabases/arcsde-2034353163.htm>`_ at the official ArcSDE documentation from ESRI.
 * instance: In ESRI Direct Connect Mode a value must be given or the Direct Connect Driver will throw an error, so just put a 'none' there - any String will work!
 * user: The username to authenticate with the geo database.
 * password: The password associated with the above username for authentication with the geo database.

.. note:: Be sure to assemble the password like: password@<Oracle Net Service name> for Oracle


Type Names
''''''''''

ArcSDE plugin's "Feature Type Names" are fully qualified, unlike other GeoTools DataStores.
That means that ``DataStore.getTypeNames()`` will return the list of spatial tables in a qualified form, as per the 
`SeLayer.getQualifiedName() <http://help.arcgis.com/en/geodatabase/10.0/sdk/arcsde/api/japi/docs/com/esri/sde/sdk/client/SeLayer.html#getQualifiedName()>`_ ESRI ArcSDE Java API call.
For example, ``JOHN.GIS.PARCELS`` instead of just ``PARCELS``, where ``JOHN`` is the user name
the plugin connected to ArcSDE with, and ``GIS`` is the name of the database it's connecting to. 

Setup
^^^^^

Supported Versions
''''''''''''''''''

The GeoTools ArcSDE plugin supports ArcSDE versions ``9.2``, ``9.3`` and ``10``, with their respective service packs. Support for prior
versions (8.3 and 9.0/9.1) has been dropped since a long time due to lack of licenses for those versions of ArcSDE.

Dependencies
''''''''''''

If you're building a project that needs the GeoTools ArcSDE plugin you're hopefuly using Apache Maven so you that you only declare a dependency against the GeoTools ArcSDE plugin
and let Maven take care of the transitive dependencies (i.e. libraries the ArcSDE plugin depends on but your project doesn't directly).

If that is not the case, the following are the full dependencies of the GeoTools ArcSDE plugin, plus the ones listed in the ref:`esri-jars` section::

    org.geotools:gt-arcsde:jar:<VERSION>
    +- org.geotools:gt-arcsde-common:jar:<VERSION>
    |  +- commons-pool:commons-pool:jar:1.5.4    
    |  \- com.ibm.icu:icu4j:jar:3.4.4    
    +- jsqlparser:jsqlparser:jar:0.3.14    
    +- org.geotools:gt-jdbc:jar:<VERSION>    
    |  +- org.geotools:gt-api:jar:<VERSION>    
    |  +- org.geotools:gt-main:jar:<VERSION>    
    |  |  \- jdom:jdom:jar:1.0    
    |  +- org.geotools:gt-data:jar:<VERSION>    
    |  +- commons-dbcp:commons-dbcp:jar:1.3    
    |  \- commons-collections:commons-collections:jar:3.1    
    +- org.geotools:gt-coverage:jar:<VERSION>    
    |  +- org.geotools:gt-referencing:jar:<VERSION>    
    |  |  +- java3d:vecmath:jar:1.3.2    
    |  |  \- org.geotools:gt-metadata:jar:<VERSION>    
    |  |     \- org.geotools:gt-opengis:jar:<VERSION>    
    |  |        \- net.java.dev.jsr-275:jsr-275:jar:1.0-beta-2    
    |  +- com.vividsolutions:jts:jar:1.11    
    |  |  \- xerces:xercesImpl:jar:2.7.1     (version managed from 2.4.0)
    |  \- it.geosolutions.imageio-ext:imageio-ext-tiff:jar:1.0.8    
    |     \- it.geosolutions.imageio-ext:imageio-ext-utilities:jar:1.0.8    
    +- org.geotools:gt-epsg-hsql:jar:<VERSION>:provided
    |  \- hsqldb:hsqldb:jar:1.8.0.7:provided
    +- javax.media:jai_core:jar:1.1.3:provided
    +- javax.media:jai_codec:jar:1.1.3:provided
    +- javax.media:jai_imageio:jar:1.1:provided

.. _esri-jars:

**Required Proprietary Libraries**

Additionally, you'll need the following two jar files:

* jsde_sdk.jar
* jpe_sdk.jar

We cannot distribute them with GeoTools. Please make sure the required jars are available on
the CLASSPATH (if not the ArcSDE plugin will report itself as unavailable).

They should be available once you installed the "ArcSDE Java SDK". For example, located in *arcsde install dir**/arcsdesdk/sdeexe92/lib/.
Make sure you use the same version of the ``jsde_sdk.jar`` and ``jpe_sdk.jar`` libraries than your ArcSDE instance. Or at least that's what ESRI recommends, though
in some cases we found using a higher version of those jars against a lower version of the ArcSDE instance does not hurt at all, or can even work or perform better.
But definitely don't use a lower version of the jars against a higher version of ArcSDE.

.. note:: As for version 10.0, ArcSDE is part of the ESRI ArcGIS Server stack, and you may need to request a separate media DVD to ESRI for the ArcSDE Java SDK as it seems it doesn't come with the standard DVD set but you can get it by just asking for it.