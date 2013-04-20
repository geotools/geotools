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

Spring Framework Configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Spring typically provides all services through Dependency Injection (DI). Geotools uses the FactoryFinder strategy to look up factories. Both can be combined by injecting Geotools factories as Spring bean factories or simply looking up Geotools factories in the bean constructor.

Geotools transaction management can be performed programmatically in the application code, but Spring provides its own transaction support as well. 
With the declarative model, developers typically write little or no code related to transaction management, and even do not depend on the Spring Framework transaction API, or any other transaction API.
For using the declarative model with Geotools, it is necessary that Geotools simply uses the transaction/connection state provided by the Spring transaction manager and does not try to intervene with it.
This is a possibly problem for JDBC datastores, but fortunately Geotools provides an api to pass external connections to it's internal transaction state (using the buildTransaction() method), which is ideally suited for this case.

To use Geotools in conjunction with Spring transaction management, datastore operations in transactional methods must call the setTransaction() method with a request-scoped transaction object.
This transaction should be created when an actual Spring transaction is started and contain the current connection of the active transaction.
Possible approaches are creating a custom TransactionManager or implementing a TransactionSynchronization object that works irrespective of the actual transaction manager.
Example code for the latter can be found `here <https://svn.geomajas.org/majas/trunk/plugin/geomajas-layer-geotools/geotools/src/main/java/org/geomajas/layer/geotools/GeoToolsTransactionSynchronization.java>`_. 
Application code should call the synchTransaction() method.
