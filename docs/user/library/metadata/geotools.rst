GeoTools
--------

The first utility class we have is helpfully called GeoTools. This class is used to configure the library for your application.

It also provides the version number of the library, in case you want to check at runtime.::
  
  GeoTools.getVersion(); // Example 15.0

Hints
^^^^^

Hints are used to configure the GeoTools library for use in your application. The value provided by GeoTools.getDefaultHints() can be configured as part of your application startup:

.. code-block:: java

   Hints.putSystemDefault(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);
   Hints.removeSystemDefault(Hints.ENTITY_RESOLVER);

GeoTools Hints are similar to a Map<GeoTools.Key,Object>, the GeoTools.Key instances each provide javadocs, and some control over the values that may be used. For example the Hints.ENTITY_RESOLVER values must be an instance of EntityResolver.

By contrast java system properties are similar to a Map<String,String> and may be specified programmatically or on the command line. The following bindings to system properties are defined (each as static constants in the GeoTools class):

================================= ===============================================
CRS_AUTHORITY_EXTRA_DIRECTORY     org.geotools.referencing.crs-directory
EPSG_DATA_SOURCE                  org.geotools.referencing.epsg-datasource
FORCE_LONGITUDE_FIRST_AXIS_ORDER  org.geotools.referencing.forceXY
LOCAL_DATE_TIME_HANDLING          org.geotools.localDateTimeHandling
RESAMPLE_TOLERANCE                org.geotools.referencing.resampleTolerance
ENTITY_RESOLVER                   org.xml.sax.EntityResolver
================================= ===============================================

The bound system properties can also be used to configure Hints:

.. code-block:: java
   
   // Allow access to local dtd and xsd files
   System.getProperties.put(GeoTools.ENTITY_RESOLVER, "org.geotools.util.NullEntityResolver");
   Hints.scanSystemProperties();

Plug-ins
^^^^^^^^

Increasingly GeoTools is being used in carefully managed plug-in systems such as Eclipse or Spring. In oder to allow GeoTools to locate its own plug-ins you may need to configure the GeoTools class with additional class loaders provided by your environment.::
  
  GeoTools.addClassloader( loader );

Out of the box GeoTools searches on the CLASSPATH available, in order to find plug-in and wire them into the library. It does this by looking in the JARs META-INF/services folder which lists plug-in classes.

In rare cases, such as OSGi plug-in system, adding additional jars to the CLASSPATH is not enough. OSGi blocks access to the META-INF/services folder. In these cases you will need to provide access to the classes yourself.::
  
  GeoTools.addFactoryIteratorProvider( provider );

JNDI
^^^^

If you are working in a Java Enterprise Edition environment, and would like to configure GeoTools to look up services in a specific context use the following::
  
  GeoTools.init( applicationContext ); // JNDI configuration

GeoTools uses names of the format "jdbc:EPSG" internally these are adapted for use with your applicationContext using the GeoTools fixName methods::

  String name = GeoTools.fixName("jdbc.EPSG");

XML
^^^

When embedding GeoTools in your own application you may wish to configure the library to use a specific EntityResolver (to access any XML Schema files included in your application, or to restrict access based on security policies).

GeoTools uses a range of XML technologies when implementing both format and protocol support - where possible these are configured based on the Hints.ENTITY_RESOLVER described above.

To access the configured ENTITY_RESOLVER:

.. code-block:: java
   
   parser.setEntityResolver( GeoTools.getEntityResolver(hints) );

GeoTools also includes two EntityResolver implementations:

* PreventLocalEntityResolver: For use when working with external XML documents, only allows DTD and XML Schema references to remote resources
* NullEntityResolver: Placeholder allowing the default SAXParser access-anything behaviour.

The library uses PreventLocalEntityResolver by default, if you wish to work with a local XML file (referencing local DTD and XMLSchema) please use the following during application setup:

.. code-block:: java

   Hints.putSystemDefault(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);

Logging
^^^^^^^

If you are working in your own application, you can teach GeoTools to use your application logging facilities (rather than Java logging which it uses by internal default).::
  
  GeoTools.setLoggerFactory( loggerFactory );

GeoTools provides out of the box implementations for:

* CommonsLoggerFactory - Apache's Common Logging framework
* Log4jLoggerFactory - Log4J

Here are a couple of examples of setting things up:

* Do nothing
  
  Out of the box GeoTools will use Java logging

* Setup for the Paranoid
  
  The example below tries to setup Commons-Logging first, and
  fallback on Log4J if the former is not present on the
  CLASSPATH.::
    
    try {
        GeoTools.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");
    } catch (ClassNotFoundException commonsException) {
        try {
                GeoTools.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");
        } catch (ClassNotFoundException log4jException) {
            // Nothing to do, we already tried our best.
        }
    }

  In the above code **ClassNotFoundException** is a checked
  exception thrown if Commons-Logging or Log4J is not available
  on the CLASSPATH, so GeoTools continue to rely on the Java
  logging system instead.

* Log4J
  
  The following is a good approach only if the Log4J framework
  is certain to be present on the CLASSPATH.::
    
    GeoTools.setLoggerFactory(Log4JLoggerFactory.getInstance());
 
  Be warned that if Log4j is not available this method call has
  unpredictable behaviour.
  
  It will typically throws a NoClassDefFoundError (the unchecked
  error, not the checked exception) at some future point. The
  error may not be thrown at the moment setLoggerFactory is
  invoked, but rather be delayed until a message is first logged,
  which may surprise the user.

* Custom
  
  You can create your own LoggerFactory if you need to track
  messages using your own facilities.
  
  This is a good approach if you are making use of Eclipse
  and would like to check bundle "trace" settings.
  
JAI
^^^

GeoTools Logging will listen to **JAI** errors and log them appropriately. It does this by first checking if your application has registered an ImagingListener, and if not it will register a *LoggingImagingListener* to redirect JAI warnings. Common **JAI** errors (such as "Continuing in pure Java mode") are logged as Level.TRACE messages, all other errors are logged as Level.INFO.

If you would like to check this bootstrapping process use the system property `-DLOGGING_TRACE=true`.

To completely filter JAI messages from your application set `javax.media.jai` group to Level.WARNING::
   
   Logging.getLogger("javax.media.jai").setLevel(Level.WARNING);

