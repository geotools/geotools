Java Commons Logging Integration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The logging output can also be redirected to commons-logging:

.. code-block:: java

   Logging.ALL.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");

This requires:

.. code-block:: xml

   <dependency>
     <groupId>commons-logging</groupId>
     <artifactId>commons-logging</artifactId>
     <version>1.2</version>
   </dependency>

Commons logging has slightly different concept of levels, using method calls to check if a level is enabled:

============= ================
Java Level	Commons-Logging
============= ================
OFF           
FATAL         isFatalEnabled()
SEVERE        isErrorEnabled()
WARNING       isWarnEnabled()
INFO          isInfoEnabled()
CONFIG        
FINE          isDebugEnabled()
FINER         
FINEST        isTraceEnabled()
ALL           
============= ================

Because commons logging is so simple, and the commons logging API is often available as a transitive
dependency required by components that use it, or implemented directly as part of Log4J, Logback or
the spring-framework:

* If you have configured ``CommonsLoggerFactory`` above, and it detects that commons logging is setup to directly use ``Jdk14Logger`` (which directly calls the java util logging api) it will act as a no-operation returning ``null`` to indicate java ``Logger`` is to be used directly.

* A similar check is performed by ``GeoTools.init()``, it checks with ``CommonsLoggerFactory`` and will change to ``DefaultLoggerFactory`` if commons logging is setup to use ``Jdk14Logger``.

* These steps are taken to avoid forcing your code to be limited by the minimal set of levels above.

Reference:

* :api:`org/geotools/util/logging/CommonsLoggerFactory.html`
* https://commons.apache.org/proper/commons-logging/guide.html#Configuration

Commons Logging Integration
'''''''''''''''''''''''''''

The following example is taken from our integration testing, this test has no additional libraries in play so ``GeoTools.init()`` defaults to direct use of Java Logger implementation.

1. Setup :file:`pom.xml` with dependencies on geotools:

   .. literalinclude:: /../../modules/integration/src/it/commons/pom.xml
      :language: xml
      
2. Configure commons logging with :download:`commons-logging.properties </../../modules/integration/src/it/commons/src/main/resources/commons-logging.properties>` added to :file:`src/main/resources`:
   
   .. literalinclude:: /../../modules/integration/src/it/commons/src/main/resources/commons-logging.properties
      :language: xml

3. During startup commons-logging will use:

   * Check the classpath for :file:`commons-logging.properties`.
   
   * Read the ``org.apache.commons.logging.Log`` property to determine logger to use.
     
   * Check the system property ``org.apache.commons.logging.Log`` logger not yet defined.
   
   * Try looking for first the ``Log4JLogger`` or ``Jdk14Logger`` if available.
     
     Log4JLogger is provided as part of the Log4J library.
     
     Jdk14Logger is provided by ``commons-logging``, but if you are using an alternate implementation such as the spring-framework this will not be available.

   * ``SimpleLog`` making use of system err.
   

4. The :file:`commons-logging.properties` was setup to use ``SimpleLog``.

   Configure ``SimpleLog`` using :download:`simplelog.properties </../../modules/integration/src/it/commons/src/main/resources/simplelog.properties>` added to :file:`src/main/resources`:
   
   .. literalinclude:: /../../modules/integration/src/it/commons/src/main/resources/simplelog.properties
      :language: xml

4. Application :download:`CommonsIntegration.java </../../modules/integration/src/it/commons/src/main/java/org/geotools/tutorial/logging/CommonsIntegration.java>` startup example for :file:`src/min/java`.

   Example is taking care to call ``GeoTools.init()`` prior to logger use:
   
   .. literalinclude:: /../../modules/integration/src/it/commons/src/main/java/org/geotools/tutorial/logging/CommonsIntegration.java
      :language: java

4. An ``exec:exec`` target is provided to make this easier to test:

   .. code-block::
      
      mvn exec:exec
   
   Is the equivalent of: 
   
   .. code-block::
       
      java -Djava.awt.headless=true \\
           org.geotools.tutorial.logging.CommonsIntegration
           