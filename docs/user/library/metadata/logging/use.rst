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
       /** ImageWorker.class package name "org.geotools.image" used for Logger */
       private static final Logger LOGGER = Logging.getLogger(ImageWorker.class);
   }

Logger
^^^^^^

Messages are logged using one of the predefined levels:

========== ================================ ====================================================
Level      Displayed on standard output     Comments 
========== ================================ ====================================================
OFF        yes by default                   highest value, used turn off all output
FATAL      yes by default                   fatal problem preventing application from continuing
SEVERE     yes by default                   serious failure, operation unable to continue
WARNING    yes by default                   non-fatal warning to bring to user attention
INFO       yes by default                   message for end users (not debugging information)
OPERATION  no unless configured             operation configuration
CONFIG     no unless configured             configuration information (services available, etc.)
FINE       no unless configured             information for developers (high level)
FINER      no unless configured             common when entering, returning, or an exception
FINEST     no unless configured             most verbose output
ALL        no unless configured             lowest value, used to enable all output
========== ================================ ====================================================

Levels are used with the ``log`` methods when logging messages:

.. code-block:: java

   LOGGER.log(Level.INFO,"Hello world");
   
   LOGGER.log(Level.INFO,"Welcome ", System.getProperty("user.name","Friend"));

This technique can be used with the custom level Logging.FATAL:

.. code-block:: java

   LOGGER.log(Logging.FATAL, "I’m sorry Dave, I’m afraid I can’t do that.");

The INFO level is for end users. Use the FINE, FINER or FINEST levels for debug information, and setup yours :file:`logging.properties` file accordingly (see Logging Configuration below).

Convenience method exists in Logger for each of buil-in levels.

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
