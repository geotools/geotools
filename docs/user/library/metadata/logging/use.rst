How to use Logging 
------------------

GeoTools provides extensive logging facilities, similar to commons-logging or sl4j, that allow applications to choose which logging implementation they wish to use.

* ``Logger``: The java util logging api used to log messages and exceptions.
* ``Logging``: GeoTools utility class used to produce Loggers
* ``LoggerFactory``: Provide a bridge between logging implementation and Logger API.

Example use:

.. code-block:: java
   
   package org.geotools.tutorial;
   
   import java.util.logging.Logger;
   import org.geotools.util.logging.Logging:

   public class Example {
     void method() {
        final Logger LOGGER = Logging.getLogger("org.geotools.tutorial");
        LOGGER.config("Welcome to GeoTools");
     }
   }

The logger may be declared in the class's static fields or returned by a class's static method. This is not mandatory but suggested if it is going to be used is several methods:

.. code-block:: java
   
   package org.geotools.image;
   
   import java.util.logging.Logger;
   import org.geotools.util.logging.Logging:
   
   public class ImageWorker {
       /** The logger ImageWorker.class used is "org.geotools.image" */
       private static final Logger LOGGER = Logging.getLogger(ImageWorker.class);
   }

Logger
^^^^^^

Messages are logged using one of the predefined levels:

========== ================================ ====================================================
Level      Displayed on standard output     Comments 
========== ================================ ====================================================
Severe     yes by default                   highest value
Warning    yes by default                   non-fatal warning to bring to user attention
Info       yes by default                   message for end users (not debugging information)
Config     no unless configured             configuration information (services available, etc.)
Fine       no unless configured             information for developers (high level)
Finer      no unless configured             common when entering, returning, or an exception
Finest     no unless configured             most verbose output
========== ================================ ====================================================

Levels are used with the ``log`` methods when logging messages:

.. code-block:: java

   LOGGER.log(Level.INFO,"Hello world");
   
   LOGGER.log(Level.INFO,"Welcome ",System.getProperty("user.name","Friend"));

The INFO level is for end users. Use the FINE, FINER or FINEST levels for debug information, and setup yours :file:`logging.properties` file accordingly (see Logging Configuration below).

Convenience method exists in Logger for each of these levels.

.. code-block:: java
   
   LOGGER.severe("Et tu, Brute?" );

.. code-block:: java

   LOGGER.warnning("Birds Aren't Real");
   
.. code-block:: java
   
   LOGGER.info("Hello world");
   
.. code-block:: java

   LOGGER.config("Application settings loaded");
   
.. code-block:: java

   LOGGER.fine("Starting to process the internet");

.. code-block:: java

   LOGGER.finer("The internet is full of cat pictures");

.. code-block:: java
   
   if( LOGGER.isLoggableLevel(Level.FINEST)){
       LOGGER.finest("percent processed:"+progress);   
   }

There are three more FINER convenience methods for tracing program execution:

.. code-block:: java
   
   public Object myMethod(String myArgument) {
       LOGGER.entering("MyClass", "MyMethod", myArgument);
       try {
          // ... do some process here
          LOGGER.exiting("MyClass", "MyMethod", myReturnValue);
          return myReturnValue;
       }
       catch (Throwable myThrowable){
          LOGGER.throwing("MyClass", "MyMethod", myThrowable);
       }
   }

Commons Logging Interoperability
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The logging output can also be redirected to commons-logging:

.. code-block:: java

   Logging.ALL.setLoggerFactory("org.geotools.util.logging.CommonsLoggerFactory");
