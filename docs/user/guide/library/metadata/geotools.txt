GeoTools
--------

The first utility class we have is helpfully called GeoTools. This class is used to configure the library for your application.

It also provides the version number of the library, in case you want to check at runtime.::
  
  GeoTools.getVersion(); // Example 2.7.1

Plug-ins
^^^^^^^^

Increasingly GeoTools is being used in carefully managed plug-in systems such as Eclipse or Spring. In oder to allow GeoTools to locate its own plug-ins you may need to configure the GeoTools class with additional class loaders provided by your environment.::
  
  GeoTools.addClassloader( loader );

Out of the box GeoTools searches on the CLASSPATH available, in order to find plug-in and wire them into the library. It does this by looking in the JARs META-INF/services folder which lists plug-in classes.

In rare cases, such as OSGi plug-in system, adding additional jars to the CLASSPATH is not enough. OSGi blocks access to the META-INF/services folder. In these cases you will need to provide access to the classes yourself.::
  
  GeoTools.addFactoryIteratorProvider( provider );

JNDI
^^^^

If you are working in a Java Enterprise Edition environment, and would like to configure GeoTools to look up services in a specific
context use the following::
  
  GeoTools.init( applicationContext ); // JNDI configuration

Logging
^^^^^^^

If you are working in your own application, you can teach GeoTools to use your application logging facilities (rather than Java loggin which it uses by internal default).::
  
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
