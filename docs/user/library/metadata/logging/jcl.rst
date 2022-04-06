Java Commons Logging Integration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The logging output can also be redirected to commons-logging:

.. code-block:: java

   Logging.ALL.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");


  ============= ================
  Java Level	Commons-Logging
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