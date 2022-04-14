Log4J 2 Interoperability
^^^^^^^^^^^^^^^^^^^^^^^^

Log4J is split into ``log4j-api`` and ``log4j-core`` jars:

.. code-block:: xml

   <dependency>
     <groupId>org.apache.logging.log4j</groupId>
     <artifactId>log4j-api</artifactId>
     <version>${log4j2.version}</version>
   </dependency>
   <dependency>
     <groupId>org.apache.logging.log4j</groupId>
     <artifactId>log4j-core</artifactId>
     <version>${log4j2.version}</version>
   </dependency>

To configure GeoTools to use Log4J API:

.. code-block:: java

   GeoTools.setLoggerFactory("org.geotools.util.logging.Log4J2LoggerFactory");

Reference:

* https://logging.apache.org/log4j/2.x/index.html
* :api:`org/geotools/util/logging/Log4J2LoggerFactory.html`


Log4j Guidance
''''''''''''''

Communication from different Logging frameworks have to Log4J 2 API:

To bridge slf4j to Log4J:

* Include the following jar:

  .. code-block:: xml

     <dependency>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-slf4j18-impl</artifactId>
       <version>${log4j2.version}</version>
     </dependency>
   
* This routes slf4j api calls to ``log4j-core``.

To bridge java util logging to Log4J:

* Include the following jar:
  
  .. code-block:: xml
     
     <dependency>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-jul</artifactId>
       <version>${log4j2.version}</version>
     </dependency>
  
* This bridge provides the following mapping:
  
  * java.util.logging.Filter: yes
  * java.util.logging.Handler: no
  
  Levels:
  
  ============= ================
  Java Level	Log4j Level
  ============= ================
  OFF           OFF
  SEVERE        ERROR
  WARNING       WARN
  INFO          INFO
  CONFIG        CONFIG
  FINE          DEBUG
  FINER         TRACE
  FINEST        FINEST
  ALL           ALL
  ============= ================


* Lots of ways to enable java util logging bridge to Log4J:

  * System property:
  
    .. code-block:: bash
  
       -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager

  * System property during application init:
  
    .. code-block:: java
  
       System.setProperty("java.util.logging.manager","org.apache.logging.log4j.jul.LogManager");
     
  * Setup configure application ``logging.properties`` with the following:
  
    .. code-block:: properties
    
       handlers = org.apache.logging.log4j.jul.Log4jBridgeHandler
       org.apache.logging.log4j.jul.Log4jBridgeHandler.propagateLevels = true
     
  * Explicitly call ``Log4jBridgeHandler.install()`` during application init:

  .. code-block:: java
   
     Log4jBridgeHandler.install();
     
* To bridge Log4J 1.x to Log4J (replacing the need for Reload4J):

  .. code-block:: xml

     <dependency>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-1.2-api</artifactId>
       <version>${log4j2.version}</version>
     </dependency>
   
  Reference: https://logging.apache.org/log4j/2.x/manual/migration.html
  
Log4j Integration
'''''''''''''''''''

The following example is taken from our integration testing, this test *only* has Log4j 2 API in play
so ``GeoTools.init()` is able to unambiguously determine ``Log4JLoggerFactory`` can be used.

1. Setup :file:`pom.xml` with dependencies on geotools and Log4J:

   .. literalinclude:: /../../modules/library/metadata/src/it/log4j/pom.xml
      :language: xml
      
2. Configure log4j wtih :download:`log4j2.xml </../../modules/library/metadata/src/it/log4j/src/main/resources/log4j2.xml>` added to :file:`src/main/resources`:
   
   .. literalinclude:: /../../modules/library/metadata/src/it/log4j/src/main/resources/log4j2.xml
      :language: xml
   
   Of interest above is defining the CONFIG and FINEST custom levels.
   
3. During startup logback will search for :file:`log4j2.xml` on the CLASSPATH.

   To search for a different file on the classpath use ``-Dlog4j2.configurationFile=log4j2-production.xml``.

4. Application :download:`Log4JIntegration.java </../../modules/library/metadata/src/it/log4j/src/main/java/org/geotools/tutorial/logging/Log4JIntegration.java>` startup example for :file:`src/min/java`.

   Example is taking care to call ``GeoTools.init()`` prior to logger use:
   
   .. literalinclude:: /../../modules/library/metadata/src/it/log4j/src/main/java/org/geotools/tutorial/logging/Log4JIntegration.java
      :language: java

5. An ``exec:exec`` target is provided to make this easier to test:

   .. code-block::
      
      mvn exec:exec
      
   Is the equivalent of: 
   
   .. code-block::
       
      java -Djava.awt.headless=true \\
           org.geotools.tutorial.logging.Log4JIntegration

6. An ``exec:exec@jul`` target is provided to try out a more realistic production setting.

   .. code-block::
      
      mvn exec:exec@jul
      
   Is the equivalent of: 
   
   .. code-block::
       
      java -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager \\
           -Dlog4j2.configurationFile=log4j2-production.xml \\
           -Djava.awt.headless=true \\
           org.geotools.tutorial.logging.Log4JIntegration
   
   This makes use of the :download:`log4j2-production.xml </../../modules/library/metadata/src/it/log4j/src/main/resources/log4j2-production.xml>` configuration, and sets up log4j jul bridge.
   
   .. literalinclude:: /../../modules/library/metadata/src/it/log4j/src/main/resources/log4j2-production.xml
      :language: xml
   
   This logging configuration reduces the levels recorded.
      