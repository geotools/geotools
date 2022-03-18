Reload4J Interoperability
^^^^^^^^^^^^^^^^^^^^^^^^^

Apache has announced Log4J 1 has reached end-of-life, the Reload4J project has taken over maintaining the API:

.. code-block:: xml

   <reload4j.version>1.2.19</reload4j.version>

   <dependency>
     <groupId>ch.qos.reload4j</groupId>
     <artifactId>reload4j</artifactId>
     <version>${reload4j.version}</version>
   </dependency>
 
If your application uses Log4J directly during compile time, you can safely set up with:

.. code-block:: java

   GeoTools.setLoggerFactory(Log4JLoggerFactory.getInstance());

If your application only depends reload4j being provided at runtime:

.. code-block:: java

   Logging.ALL.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");

.. note::
   
   If reload4j is not found on the CLASSPATH Log4JLoggerFactory results in unpredictable behavior!
   
   It will typically throws a ``NoClassDefFoundError`` (the unchecked error, not the checked exception) at some future point. The error may not be thrown at the moment ``setLoggerFactory`` is invoked, but rather be delayed until a message is first logged, which may surprise the user.

Reference:

* http://reload4j.qos.ch
* https://logging.apache.org/log4j/1.2/
* :api:`org/geotools/util/logging/Log4JLoggerFactory.html`

Reload4J Integration
''''''''''''''''''''

The following example is taken from our integration testing, this test *only* has the reload4j
in play so ``GeoTools.init()` is able to unambiguously determine ``Log4JLoggerFactory`` can be used.

1. Setup :file:`pom.xml` with dependencies on geotools and reload4j:

   .. literalinclude:: /../../modules/library/metadata/src/it/reload4j/pom.xml
      :language: xml
      
2. Configure reload4j wtih :download:`log4j.properties </../../modules/library/metadata/src/it/reload4j/src/main/resources/log4j.properties>` added to :file:`src/main/resources`:
   
   .. literalinclude:: /../../modules/library/metadata/src/it/reload4j/src/main/resources/log4j.properties
      :language: ini
      
3. During startup log4j will search for :file:`log4j.properties` on the CLASSPATH, or to search for a different file use the system property:
   
   .. code-block:: bash
      
      -Dlog4j.configuration=log4-debug.properties

4. Application :download:`Reload4Integration.java </../../modules/library/metadata/src/it/reload4j/src/main/java/org/geotools/tutorial/reload/Reload4JIntegration.java>` startup example for :file:`src/min/java`.

   Example is taking care to call ``GeoTools.init()`` prior to logger use:
   
   .. literalinclude:: /../../modules/library/metadata/src/it/reload4j/src/main/java/org/geotools/tutorial/reload/Reload4JIntegration.java
      :language: java

4. An ``exec:exec`` target is provided to make this easier to test:

   .. code-block::
      
      mvn exec:exec
      
   .. note:: Avoid testing with ``exec:java`` which uses maven java runtime environment (already pre-configured for logging).

Reload4J Guidance
'''''''''''''''''

In a more complicated setup using multiple libraries you may also end up including:

* slf4j-reload4j: used to bridge any components using slf4j api

  .. code-block::
  
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>slf4j-reload4j</artifactId>
          <version>${slf4j.version}</version>
      </dependency>

* jcl-over-sl4j: used to bridge any components using commons-logging to sl4j (which can be bridged to reload4j above).
  
  .. code-block::
  
      <dependency>
          <groupId>org.slf4j</groupId>
          <artifactId>jcl-over-slf4j</artifactId>
          <version>${slf4j.version}</version>
      </dependency>

* commons-logging: Assume Log4J 1 API

  Use :file:`commons-logging.properties`:
  
  .. code-block:: properties
     
     org.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger
     

* Use of multiple logging frameworks prevents ``GeoTools.init()`` ability to determine which API to use requiring the use of:

  .. code-block:: java

     Logging.ALL.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");