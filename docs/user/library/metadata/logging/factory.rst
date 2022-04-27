LoggerFactory
^^^^^^^^^^^^^

If you are working in your own application, you can teach GeoTools to use your application logging facilities (rather than Java logging which it uses by default).

GeoTools provides LoggerFactories for:

* ``org.geotools.util.logging.LogbackLoggerFactory`` - Logback via SLF4J API
* ``org.geotools.util.logging.Log4j2LoggerFactory`` - Log4J 2 API
* ``org.geotools.util.logging.Log4j1LoggerFactory`` - Log4J 1 API (maintained by Reload4J project)
* ``org.geotools.util.logging.CommonsLoggerFactory`` - Apache's Common Logging framework
* ``org.geotools.util.logging.DefaultLoggerFactory`` - java util logging

GeoTools.init()
'''''''''''''''

As mentioned in :doc:`../geotools`, the ``GeoTools.init()`` call sets up the library for use, and will do its best to determine which logging library your application is using.

Keep in mind that ``GeoTools.init()`` must be called prior to Logger creation:

.. literalinclude:: /../src/main/java/org/geotools/tutorial/logging/Example.java 
   :language: java
   :start-at: package

A reliable way to do this is using static initializer (before ``main()`` method is called):

.. literalinclude:: /../src/main/java/org/geotools/tutorial/logging/Example2.java 
   :language: java
   :start-at: package
   
You may find it more readable to do this using a static method defining a LOGGER:

.. literalinclude:: /../src/main/java/org/geotools/tutorial/logging/Example3.java 
   :language: java
   :start-at: package

This method configures:

* ``Logging.ALL``
* ``Logging.GEOTOOLS``
* ``Logging.JAI``

This is the the most flexible way to set up logging, allowing users of your application to choose
the logging system based on configuration files and jars supplied at runtime.

GeoTools.setLoggerFactory()
'''''''''''''''''''''''''''

If you know the LoggerFactory you wish to use:

.. literalinclude:: /../src/main/java/org/geotools/tutorial/logging/Example4.java 
   :language: java
   :start-at: package

The above example uses the ``LogbackLoggerFactory.getInstance()`` singleton, which writes messages using the SLF4J API be available at runtime.

Using the ``GeoTools.setLoggerFactory(LoggerFactory)`` method updates ``Logging.ALL``, ``Logging.GEOTOOLS`` and ``Logging.JAI``.  When subsequently called ``GeoTools.init()`` will check that  ``Logging.ALL.getLoggerFactory()`` is not {@code null} - recognizing logging has already been configured, and will not replace your setting.

Logging.setLoggerFactory()
''''''''''''''''''''''''''

You may also call ``Logger.setLoggerFactory()`` directly to name the logger factory you wish to use:

.. literalinclude:: /../src/main/java/org/geotools/tutorial/logging/Welcome.java 
   :language: java
   :start-at: package
  
In the above code try/catch is used if Log4J2LoggerFactory is was not able to be constructed (due to Log4J not being available on the CLASSAPTH). GeoTools continue to rely on the Java logging system instead, so the WARNING is shown.

::

    Apr 09, 2022 6:11:07 PM org.geotools.tutorial.logging.Welcome initDefaultLogger
    WARNING: Unable to setup "org.geotools.util.logging.Log4J2LoggerFactory", is log4j2 available?
    java.lang.ClassNotFoundException: No factory of kind "org.geotools.util.logging.Log4JLoggerFactory" found.
       at org.geotools.util.logging.Logging.factoryNotFound(Logging.java:371)
       at org.geotools.util.logging.Logging.setLoggerFactory(Logging.java:357)
       at org.geotools.tutorial.logging.Welcome.initDefaultLogger(Welcome.java:33)
       at org.geotools.tutorial.logging.Welcome.<clinit>(Welcome.java:20)
    Caused by: java.lang.NoClassDefFoundError: org/apache/log4j/Logger
       at org.geotools.util.logging.Log4JLoggerFactory.<init>(Log4JLoggerFactory.java:42)
       at org.geotools.util.logging.Log4JLoggerFactory.getInstance(Log4JLoggerFactory.java:52)
       at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
       at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
       at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
       at java.lang.reflect.Method.invoke(Method.java:498)
       at org.geotools.util.logging.Logging.setLoggerFactory(Logging.java:341)
       ... 2 more
    Caused by: java.lang.ClassNotFoundException: org.apache.log4j.Logger
       at java.net.URLClassLoader.findClass(URLClassLoader.java:382)
       at java.lang.ClassLoader.loadClass(ClassLoader.java:418)
       at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:355)
       at java.lang.ClassLoader.loadClass(ClassLoader.java:351)
       ... 9 more

    Apr 09, 2022 6:11:07 PM org.geotools.tutorial.logging.Welcome main
    INFO: Welcome

If you know the LoggerFactory you wish to use there is a method to pass in the instance:

.. literalinclude:: /../src/main/java/org/geotools/tutorial/logging/Welcome2.java 
   :language: java
   :start-at: package

.. note:: You can create your own ``LoggerFactory`` if you wish to bridge GeoTools logging to your own tracking facilities. As an example an OSGi application could check bundle trace settings "trace" settings to determine appropriate level of detail to log.


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
  