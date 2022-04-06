Log4J 2 Interoperability
^^^^^^^^^^^^^^^^^^^^^^^^


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

.. code-block:: java

   Logging.ALL.setLoggerFactory("org.geotools.util.logging.Log4J2LoggerFactory");

Reference:

* https://logging.apache.org/log4j/2.x/index.html
* :api:`org/geotools/util/logging/Log4J2LoggerFactory.html`


Log4j Guidance
''''''''''''''

All logging frameworks have some mechanism to delegate to java util logging as a backend.

* To bridge slf4j to Log4J:

  .. code-block:: xml
  
     <dependency>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-slf4j18-impl</artifactId>
       <version>${log4j2.version}</version>
     </dependency>

* To bridge java util logging to Log4J:
  
  .. code-block:: xml
     
     <dependency>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-jul</artifactId>
       <version>${log4j2.version}</version>
     </dependency>
  
  Compatibility:
  
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
  
  System wide setup:
  
  * ``-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager``
  * ``System.setProperty("java.util.logging.manager","org.apache.logging.log4j.jul.LogManager")``
  
  Or configure application ``logging.properties`` with the following:
  
  .. code-block:: properties
     
     handlers = org.apache.logging.log4j.jul.Log4jBridgeHandler
     org.apache.logging.log4j.jul.Log4jBridgeHandler.propagateLevels = true
     
  Or call ``Log4jBridgeHandler.install()`` during application init:
  
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
