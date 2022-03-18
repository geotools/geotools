LoggerFactory
^^^^^^^^^^^^^

If you are working in your own application, you can teach GeoTools to use your application logging facilities (rather than Java logging which it uses by internal default).::
  
  GeoTools.setLoggerFactory( loggerFactory );

GeoTools provides LoggerFactories for:

* ``org.geotools.util.logging.LogbackLoggerFactory``
* ``org.geotools.util.logging.Log4j2LoggerFactory`` - Log4J 2 API
* ``org.geotools.util.logging.Log4j1LoggerFactory`` - Log4J 1 API (maintained by Reload4J project)
* ``org.geotools.util.logging.CommonsLoggerFactory`` - Apache's Common Logging framework

Using a logger factory:

.. code-block::
   
   package net.fun.example;
   
   import java.util.logging.Logger;
   import org.geotools.util.logging.Logging;
   
   class Welcome {
   
      public static final Logger DEFAULT = initDefaultLogger();

      /**
       * Setup DEFAULT Logger (taking care to initialize GeoTools with preferred logging framework).
       *
       * @return Logger for this application, and the net.fun.example package.
       */
      private static Logger initDefaultLogger() {
          Throwable troubleSettingUpLogging = null;
          try {
              Logging.ALL.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");
          } catch (Throwable trouble) {
              troubleSettingUpLogging = trouble;
          }
          Logger logger = Logging.getLogger("net.fun.example");
          logger.config("Default Logger:" + logger);
          if (troubleSettingUpLogging != null) {
              logger.log(
                      Level.WARNNING,
                      "Unable to init \"org.geotools.util.logging.Log4JLoggerFactory\":"
                              + troubleSettingUpLogging,
                      troubleSettingUpLogging);
          }
          return logger;
      }

In the above code try/catch is used if Log4JLoggerFactory is was not able to be constructed (due to Log4J not being available on the CLASSAPTH). GeoTools continue to rely on the Java logging system instead, so the WARNING is shown.

You can create your own ``LoggerFactory`` if you wish to bridge GeoTools logging to your own tracking facilities. As an example an OSGi application could check bundle trace settings "trace" settings to determine appropriate level of detail to log.

GeoTools.init()
'''''''''''''''

As mentioned in :doc:`../geotools`, the ``GeoTools.init()`` method will do its best to determine which logging library your application is using.

Keep in mind that ``GeoTools.init()`` must be called prior to Logger creation in your main method:

.. code-block::
   
   public static void main(String args[]){
        GeoTools.init();
        Logger LOGGER = Logging.getLogger("org.geotools.tutorial");
        LOGGER.fine("Application started - first post!")
   }

Or during class initialization:

.. code-block::
   
   static final Logger LOGGER = defaultLogger();

   public static void main(String args[]){
        LOGGER.fine("Application started - first post!")
   }
   
   private static final Logger defaultLogger(){
       GeoTools.init();
       return Logging.getLogger("org.geotools.tutorial");
   }

Keep in mind this method configures Logging.GEOTOOLS Logger only, if you are using Logging to configure for additional Loggger instances during startup you may do so:

.. code-block::
   
   GeoTools.init(); // setup org.geotools
   Logging.ALL.setLoggerFactory(Logging.GEOTOOLS.getLoggerFactory());
   Logging.JAI.setLoggerFactory(Logging.GEOTOOLS.getLoggerFactory());

JAI Logging
'''''''''''

GeoTools Logging will listen to ``JAI`` errors and log them appropriately. It does this by first checking if your application has registered an ``ImagingListener``, and if not it will register a ``LoggingImagingListener`` to redirect JAI warnings. Common ``JAI`` errors (such as "Continuing in pure Java mode") are logged as ``Level.TRACE`` messages, all other errors are logged as ``Level.INFO``.

If you would like to check this bootstrapping process use the system property `-DLOGGING_TRACE=true`.

To completely filter JAI messages from your application set `javax.media.jai` group to ``Level.WARNING``::
   
   Logging.getLogger("javax.media.jai").setLevel(Level.WARNING);

Why not common-logging or other framework directly?
'''''''''''''''''''''''''''''''''''''''''''''''''''

GeoTools provides its own system to bridge to different logging libraries rather than use commons-logging, Log4J, or SLF4J directly.

As a good component application it is our role to smoothly integrate into a wide range of applications.

* commons-logging: 

  The common-logging API is little more than a set of println functions with name (info, trace, debug, etc.). Java logging API provides the same convenience methods, but is also richer. We use some of its extra capabilities in GeoTools code base:

  * ResourceBundle support for localization.
  * Logging of stack traces.
  * Information on source class and method names.
  * Information about which thread produced the logging.
  * Can be used through Java Monitoring and Management system.
  
  Keep in mind that commons-logging has additional error levels including FATAL.

* Log4J 1 API / Reload4J
  
  Offered similar functionality with a wider range of handler implementations. On the other hand, Java logging is more closely tied to the JVM, which avoid some ClassLoader problems that prevent usage of Log4J in some environments.
  
  Apache phased out Log4J 1 API, and if not for the Reload4J project this would no longer be an option.
  
* Log4J 2 API
  
  Log4J offers similar functionality and perhaps better performance and memory management than Java logging.
  
* SLF4J 
  
  This SLF4J is the closest replacement to our LoggingFactory approach of bridging between logging systems.
  
* Logback
  
  A direct implementation of SLF4J api Logback is great solution enjoyed by the spring boot community.
  