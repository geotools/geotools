Logback integration
^^^^^^^^^^^^^^^^^^^

The logging output can also be redirected to Logback (via SL4J API):

.. code-block:: xml

   <dependency> 
     <groupId>ch.qos.logback</groupId>
     <artifactId>logback-classic</artifactId>
     <version>${slf4j.version}</version>
   </dependency>
   
If your application uses logback directly during compile time, you can safely set up with:

.. code-block:: java

   GeoTools.setLoggerFactory(LogbackLoggerFactory.getInstance());

If your application only depends logback being provided at runtime:

.. code-block:: java
   
   Logging.ALL.setLoggerFactory("org.geotools.util.logging.LogbackLoggerFactory");

.. note::
   
   If logback is not found on the CLASSPATH LogbackLoggerFactory results in unpredictable behavior!
   
   It will typically throws a ``NoClassDefFoundError`` (the unchecked error, not the checked exception) at some future point. The error may not be thrown at the moment ``setLoggerFactory`` is invoked, but rather be delayed until a message is first logged, which may surprise the user.


============= ============= ========
Java Level    LogBack Level Marker
============= ============= ========
OFF           OFF
FATAL         ERROR         FATAL
SEVERE        ERROR
WARNING       WARN
INFO          INFO
CONFIG        INFO          CONFIG
FINE          DEBUG
FINER         TRACE
FINEST        TRACE         FINEST
ALL           ALL
============= ============= ========

GeoTools logback.xml example:

.. code-block:: xml

   <?xml version="1.0" encoding="UTF-8"?>
   <configuration>
       <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
           <encoder>
               <pattern>%d{HH:mm:ss.SSS} %-6level %logger{36} - %msg%n</pattern>
           </encoder>
       </appender>

       <turboFilter class="ch.qos.logback.classic.turbo.MarkerFilter">
           <Name>CONFIG_FILTER</Name>
           <Marker>CONFIG</Marker>
           <OnMatch>DENY</OnMatch>
       </turboFilter>

       <logger name="org.geotools.tutorial.logging" level="ALL"/>
       <logger name="org.geotools" level="DEBUG"/>
       <root level="error">
           <appender-ref ref="STDOUT" />
       </root>
   </configuration>
   
The above example shows how to "DENY" messages that have the ``CONFIG`` marker applied; this provides a way to filter the Java CONFIG level messages that are published to SLF4J as INFO messages, with a ``CONFIG`` marker.

Reference:

* http://logback.qos.ch
* http://slf4j.org
* :api:`org/geotools/util/logging/LogbackLoggerFactory.html`

Use with SLF4J Enviornment
''''''''''''''''''''''''''

Logback natively uses the SLF4J API, allowing LogbackLoggerFactory to be used with application configured for SLF4J:

* SLF4J is designed for external configuration, and the ``Logger.setLelvel( Level )`` method will silently do nothing (which is allowed by the Logger javadoc api contract).

* When using logback-classic the ``Logger.setLelvel( Level )`` works as expected.

Logback Guidance
''''''''''''''''

* Include ``<shutdownHook/>`` in your configuration, or register a shutdown hook yourself:

  .. code-block:: java
  
     Runtime.getRuntime().addShutdownHook(new Thread(() -> stopLogging()));
  
  .. code-block:: java
  
     /**
      * Allow logback to finish 
      */
     private static void stopLogging(){
         LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
         loggerContext.stop();
     }

In a more complicated setup using multiple libraries you may also end up including:

* jul-to-slf4j: used to bridge any components using java util logging to sl4j.

  .. code-block::
  
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>jul-to-slf4j</artifactId>
          <version>${slf4j.version}</version>
      </dependency>
  
  Please read the instructions on use of `LevelChangePropagator <https://logback.qos.ch/manual/configuration.html#LevelChangePropagator>`__.
  
  .. note:: Use of ``jul-to-slf4j`` combined with ``LevelChangePropagator`` is an acceptable alternative to using GeoTools ``LogbackLoggerFactory``.
  
     This approach offers only minor regression in functionality, no mapping is provided for CONFIG and FINNER levels.

* log4j-to-slf4j: Apache Log4J 2 provides its own bridge to Log4J:

  .. code-block:: xml
     
     <dependency>
         <groupId>org.apache.logging.log4j</groupId>
         <artifactId>log4j-to-slf4j</artifactId>
         <version>${log4j.version}</version>
     </dependency>

* jcl-over-sl4j: used to bridge any components using commons-logging to sl4j (which can be bridged to reload4j above).
  
  .. code-block::
  
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>jcl-over-slf4j</artifactId>
          <version>${slf4j.version}</version>
      </dependency>

* slf4j-reload4j: used to bridge any components using slf4j api

  .. code-block::
  
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-reload4j</artifactId>
          <version>${slf4j.version}</version>
      </dependency>
      
* Use of multiple logging frameworks prevents ``GeoTools.init()`` ability to determine which API to use requiring the use of:

  .. code-block:: java

     Logging.ALL.setLoggerFactory("org.geotools.util.logging.LogbackLoggerFactory");

Logback Integration
'''''''''''''''''''

The following example is taken from our integration testing, this test *only* has slf4j api available so ``GeoTools.init()` is able to unambiguously determine ``LogbackLoggerFactory`` can be used.

1. Setup :file:`pom.xml` with dependencies on geotools and Logback:

   .. literalinclude:: /../../modules/library/metadata/src/it/logback/pom.xml
      :language: xml
      
2. Configure logback with :download:`logback.xml </../../modules/library/metadata/src/it/logback/src/main/resources/logback.xml>` added to :file:`src/main/resources`:
   
   .. literalinclude:: /../../modules/library/metadata/src/it/logback/src/main/resources/logback.xml
      :language: xml
   
   Of interest above is the mapping of CONFIG and FINEST to logback markers, something not offered by ``jul-to-slf4j`` bridge.
   
3. During startup logback will search for :file:`logback.xml` on the CLASSPATH (or :file:`logback-test.xml` for testing).

   To use a different file ``-Dlogback.configurationFile=logback-custom.xml``.

4. Application :download:`LogbackJIntegration.java </../../modules/library/metadata/src/it/logback/src/main/java/org/geotools/tutorial/logging/LogbackIntegration.java>` startup example for :file:`src/min/java`.

   Example is taking care to call ``GeoTools.init()`` prior to logger use:
   
   .. literalinclude:: /../../modules/library/metadata/src/it/logback/src/main/java/org/geotools/tutorial/logging/LogbackIntegration.java
      :language: java

4. An ``exec:exec`` target is provided to make this easier to test:

   .. code-block::
      
      mvn exec:exec

   Is the equivalent of: 
   
   .. code-block::
       
      java -Djava.awt.headless=true \\
           org.geotools.tutorial.logging.LogbackIntegration
           
   .. note:: Avoid testing with ``exec:java`` which uses maven java runtime environment (already pre-configured for logging).