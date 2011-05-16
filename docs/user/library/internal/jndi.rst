Java EE
-------

When writing a GeoTools Java Enterprise Edition Application you will mostly be concerned the configuration of GeoTools to use global services provided by your application container.

JNDI Configuration
^^^^^^^^^^^^^^^^^^

To start out with you will need to tell GeoTools about the JNDI Initial Context provided by your application server.::
  
  GeoTools.init( initialConext );

When GeoTools does a JNDI lookup it will now use your application servers context. If you do nothing GeoTools would use the default JRE InitialContext; which your application server may have correctly set up.

Please test in your environment - you may find that this step is not required.

DataSource Configuration
^^^^^^^^^^^^^^^^^^^^^^^^

One of the most seriously managed resources in a Java EE application is the database connections. You need to ensure that they are managed by your application server administrator (and not hard coded into your application).

For the parts of GeoTools that use databases they have provided two mechanisms for you to accomplish this goal:

* You can look up the DataSource yourself, and provide the raw javax.sql.DataSource as:
  
  * Aa configuration Hint when creating an EpsgAuthority
  * As a connection parameter when creating a DataStore

* You can provide the name and let GeoTools look up the DataSource using your GeoTools.getInitialConext()
  
  * As a configuration Hint when creating an EpsgAuthority
  * As a connection parameter when creating a DataStore
