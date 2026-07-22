GeoTools
--------

The first utility class we have is helpfully called ``GeoTools``. This class is used to configure the library for your application.

It also provides the version number of the library, in case you want to check at runtime.::
  
  GeoTools.getVersion(); // Example 15.0

``Hints``
^^^^^^^^^

``Hints`` are used to configure the GeoTools library for use in your application. The value provided by ``GeoTools.getDefaultHints()`` can be configured as part of your application startup:

.. code-block:: java

   Hints.putSystemDefault(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);
   Hints.removeSystemDefault(Hints.ENTITY_RESOLVER);

GeoTools ``Hints`` are similar to a ``Map<GeoTools.Key,Object>``, the ``GeoTools.Key`` instances each provide javadocs, and some control over the values that may be used. For example the ``Hints.ENTITY_RESOLVER`` values must be an instance of ``EntityResolver``.

By contrast Java system properties are similar to a ``Map<String,String>`` and may be specified programmatically or on the command line. The following bindings to system properties are defined (each as static constants in the ``GeoTools`` class):

===================================== ===============================================
``CRS_AUTHORITY_EXTRA_DIRECTORY``     ``org.geotools.referencing.crs-directory``
``EPSG_DATA_SOURCE``                  ``org.geotools.referencing.epsg-datasource``
``FORCE_LONGITUDE_FIRST_AXIS_ORDER``  ``org.geotools.referencing.forceXY``
``LOCAL_DATE_TIME_HANDLING``          ``org.geotools.localDateTimeHandling``
``RESAMPLE_TOLERANCE``                ``org.geotools.referencing.resampleTolerance``
``ENTITY_RESOLVER``                   ``org.xml.sax.EntityResolver``
===================================== ===============================================


The bound system properties can also be used to configure Hints:

.. code-block:: java
   
   // Allow access to local dtd and xsd files
   System.getProperties.put(GeoTools.ENTITY_RESOLVER, "org.geotools.util.NullEntityResolver");
   Hints.scanSystemProperties();

Plug-ins
^^^^^^^^

Increasingly GeoTools is being used in carefully managed plug-in systems such as SpringBoot and Spring Framework. In order to allow GeoTools to locate its own plug-ins you may need to configure the ``GeoTools`` class with additional class loaders provided by your environment.::
  
  GeoTools.addClassloader( loader );

Out of the box GeoTools searches on the ``CLASSPATH`` available, in order to find plug-in and wire them into the library. It does this by looking in the jar's ``META-INF/services`` folder which lists plug-in classes.

In rare cases, such as OSGi plug-in system, adding additional jars to the ``CLASSPATH`` is not enough. OSGi blocks access to the ``META-INF/services`` folder. In these cases you will need to provide access to the classes yourself.::
  
  GeoTools.addFactoryIteratorProvider( provider );

JNDI
^^^^

To configure GeoTools to look up services in a specific context use the following:

.. code-block:: java
  
   GeoTools.init( context ); // JNDI configuration

For JNDI lookup GeooTools uses:

.. code-block:: java

   DataSource dataSource = (DataSource) GeoTools.jndiLookup(name);
   
The ``jndiLookup(String)`` is to safe lookups by default. The default use of ``GeoTools.DEFAULT_JNDI_VALIDATOR`` ensures only no-schema and java schema lookups are allowed. To relax this policy you may supply your own approach using ``GeoTools.setJNDINameValidator(Predicate<String>)``.

XML
^^^

When embedding GeoTools in your own application you may wish to configure the library to use a specific ``EntityResolver`` (to access any XML Schema files included in your application, or to restrict access based on security policies).

GeoTools uses a range of XML technologies when implementing both format and protocol support - where possible these are configured based on the ``Hints.ENTITY_RESOLVER`` described above.

To access the configured ``ENTITY_RESOLVER``:

.. code-block:: java
   
   parser.setEntityResolver( GeoTools.getEntityResolver(hints) );

The library uses ``DefaultEntityResolver`` by default, if you wish to work with a local XML file (referencing local DTD and XMLSchema) please use the following during application setup:

.. code-block:: java

   Hints.putSystemDefault(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

GeoTools defines `EntityResolver3` interface providing the `EntityResolver3.getAccess()` method listing the protocols required for the Entity Resolver to function. This information is used by XMLUtils when configuring xml services for use by the library.

GeoTools also includes several ``EntityResolver3`` implementations:

.. list-table:: EntityResolver3
   :widths: 25 25 50
   :header-rows: 1

   * - Implementation
     - Access
     - Intended Use
   * - ``DefaultEntityResolver.INSTANCE``
     - ``"http,jar,jar:file,vfs"``
     - For working with OGC and INSPIRE XML documents, only allows DTD and XML Schema references to Open Geospatial and INSPIRE registries.
   * - ``PreventLocalEntityResolver.INSTANCE``
     - ``"http,jar,jar:file,vfs"``
     -  For use when working with external XML documents, only allows DTD and XML Schema references to remote resources.
   * - ``NullEntityResolver.INSTANCE``
     - ``"all"``
     - Allowing the default ``SAXParser`` access-anything behavior.
   * - ``PreventEntityResolver.INSTANCE``
     - ``""``
     - Any use results in SAXException blocking all entity resolution, does not allow any protocols.
   * - ``InternalEntityResolver.INSTANCE``
     - ``"http,jar,jar:file,vfs,file"``
     - Supports IDE development where ``target/classe`` and ``target/test-classes`` may be used during testing.

When resolving an allow-listed ``http(s)`` reference, ``PreventLocalEntityResolver`` follows HTTP redirects itself, validating each intermediate location against the same allow-list before following it.

To use ``InternalEntityResolver`` during a test case to allow access to internal content during test (from IDE or maven) while limiting "http" locations to OGC and INSPIRE registries.

.. code-block:: java

   @Before
   public void setup(){
        Hints.putSystemDefault(Hints.ENTITY_RESOLVER, InternalEntityResolver.INSTANCE);
   }
   @Before
   public void setup(){
        Hints.removeSystemDefault(Hints.ENTITY_RESOLVER);
   }
   @Test
   public void test(){
        EntityResolver entityResolver = GeoTools.getEntityResolver();
        if (entityResolver instanceof EntityResolver3 entityResolver3) {
             assertTrue(entityResolver3.getAccess().contains("file"));
        }
        
        
        DocumentBuilderFactory docFactory = XMLUtils.newDocumentBuilderFactory();
        docFactory.setNamespaceAware(true);

        InputStream gml = Example.class.getResourceAsStream("features.gml"))
        document = docFactory.newDocumentBuilder().parse(gml);
   }

For a more general online test configure ``InternalEntityResolver.INSTANCE`` to delegate to 
``PreventLocalEntityResolver`` which allows unrestricted "http" access:

.. code-block:: java

   @Before
   public void setup(){
        Hints.putSystemDefault(Hints.ENTITY_RESOLVER, new InternalEntityResolver(PreventLocalEntityResolver.INSTANCE));
   }

To limit access to only internal content configure ``InternalEntityResolver.INSTANCE`` to delegate to ``PreventEntityResolver``:

.. code-block:: java

   @Before
   public void setup(){
        Hints.putSystemDefault(Hints.ENTITY_RESOLVER, new InternalEntityResolver(PreventEntityResolver.INSTANCE));
   }


Logging
^^^^^^^

If you are working in your own application, you can teach GeoTools to use your application logging facilities (rather than Java logging which it uses by internal default).

The ``GeoTools.init()`` method will do its best to determine which logging implementation your library is using:

  .. code-block:: java
  
     GeoTools.init();

This method tries the following:

* logback
* log4j
* reload4j
* commons-logging

For more information see :doc:`logging/factory`.