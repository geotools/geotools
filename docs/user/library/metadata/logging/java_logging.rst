Java Util Logging Configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The java util logging configuration is defined by a file :file:`logging.properties`:

* ``java.util.logging.config.file`` system property
* ``java.util.logging.config.class`` identifying a class in your application responsible for configuration
* Java 11: :file:`$JAVA_HOME/conf/logging.properties`

And can be detected during application startup if necessary:

.. code-block:: java

   public void static main(String ...){
      File logging = new File("logging.properties");
      if( logging.exists() && !System.getProperties().hasKey("java.util.logging.config.file")){
        System.setProperty("java.util.logging.config.file", path);
      }
      else {
        System.setProperty("java.util.logging.config.class", "ApplicationDefaultLogging");
      }
   }

Falling back to ApplicationDefaultLogging (reading :file:`logging.properties` from `src/main/resources/logging.properties` resource included in jar:

.. code-block:: java

   class ApplicationDefaultLogging {
      public ApplicationDefaultLogging(){
          try( Inputstream stream : ApplicationDefaultLogging.class.getResourceAsStream("/logging.properties")){
             LogManager.readConfiguration(stream);
          }
      }
   }

To define a default configuration level provide a the **.level** property to the minimal level of interest for you:

.. code-block:: properties

   .level= FINER

You can specify a different level to be shown to the console (than is saved out to xml). To define the java.util.logging.ConsoleHandler.level property to the minimal level you want to see on the console:

.. code-block:: properties
   
   # Limit the message that are printed on the console to FINE and above.
   java.util.logging.ConsoleHandler.level = FINE
   java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter

For Windows user, check the value displayed by :command:`chcp` on the command line - you may need to add:

.. code-block:: properties
   
   # windows users may need to provide character encoding
   java.util.logging.ConsoleHandler.encoding = Cp850


To list detailed messages for a specific module you can define a different logging level may be specified for each module.:

.. code-block:: properties
   
   org.geotools.gml.level = FINE
   org.geotools.referencing.level = INFO

Provides fairly detailed logging message from the GML module, but not from the referencing module.

Custom Levels
'''''''''''''

Logging defines two custom levels to better map to logging frameworks:

* ``Logging.FATAL`` - unrecoverable error that will cause application or operation to fail
* ``Logging.OPERATION`` - operation configuration

MonolineFormatter
'''''''''''''''''

GeoTools can produces a console output similar to the Log4J one (single-line instead of multi-line log message) if the following code is invoked once at application starting time:

.. code-block:: java

   Logging.ALL.forceMonolineConsoleOutput();

Alternatively, this formatter can also be configured in the :file:`logging.properties` without the need for the above-cited method call:

.. code-block:: properties

   java.util.logging.ConsoleHandler.formatter = org.geotools.util.logging.MonolineFormatter
   java.util.logging.ConsoleHandler.level = FINE

   # Optional
   # org.geotools.util.logging.MonolineFormatter.time = HH:mm:ss.SSS
   # org.geotools.util.logging.MonolineFormatter.source = class:short

See the **MonolineFormatter** javadoc for details.

Java Util Logging Guidance
''''''''''''''''''''''''''

Logging frameworks mechanism to delegate to ``java.util.logging`` as a backend.

* SL4J: Add :file:`slf4j-jdk14.jar` to classpath:

  .. code-block:: xml
     
     <dependency>
       <groupId>org.slf4j</groupId>
       <artifactId>slf4j-jdk14</artifactId>
       <version>${sl4j.version}</version>
     </dependency>

* Log4J 1.2: configure an appender route to java util logging
  
  .. code-block:: xml
  
     <appender name="jul" class="org.apache.log4j.JulAppender"> 
         <layout class="org.apache.log4j.PatternLayout"> 
             <param name="ConversionPattern" value="%d %-5p %c - %m%n "/> 
         </layout> 
     </appender> 

* commons-logging:

  Use :file:`commons-logging.properties`:
  
  .. code-block:: properties
     
     org.apache.commons.logging.Log=org.apache.commons.logging.impl.Jdk14Logger
     
Java Util Logging Integration
'''''''''''''''''''''''''''''

The following example is taken from our integration testing, this test has no additional libraries in play so ``GeoTools.init()`` defaults to direct use of Java Logger implementation.

1. Setup :file:`pom.xml` with dependencies on geotools:

   .. literalinclude:: /../../modules/integration/src/it/logging/pom.xml
      :language: xml
      
2. Configure ``java.util.logging`` with :download:`logging.properties </../../modules/integration/src/it/logging/logging.properties>`:
   
   .. literalinclude:: /../../modules/integration/src/it/logging/logging.properties
      :language: xml
   
   .. warning:: Only Loggers that are used are configured, this can be frustrating if you assume a parent logger has been setup and will provide an expected default level.
   
3. During startup java util logging will use:
   
   * The :file:`logging.properties` included in your Java Runtime Environment.

   * The :file:`/WEB-INF/logging.properties` included in web application

   You can override this behaviour with system property:
   
   .. code-block:: bash
      
      -Djava.util.logging.config.file=logging.properties

4. Application :download:`LogbackJIntegration.java </../../modules/integration/src/it/logging/src/main/java/org/geotools/tutorial/logging/LoggingIntegration.java>` startup example for :file:`src/min/java`.

   Example is taking care to call ``GeoTools.init()`` prior to logger use:
   
   .. literalinclude:: /../../modules/integration/src/it/logging/src/main/java/org/geotools/tutorial/logging/LoggingIntegration.java
      :language: java

4. An ``exec:exec`` target is provided to make this easier to test:

   .. code-block::
      
      mvn exec:exec
   
   The `exec:exec` goal was configured with ``-Djava.util.logging.config.file=logging.properties``.
   
   .. note:: Avoid testing with ``exec:java`` which uses maven java runtime environment (already pre-configured for logging).