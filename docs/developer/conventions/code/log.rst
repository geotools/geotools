Logging
-------

We use the logging package bundled into J2SE 1.4 and above (java.util.logging). An overview is available on line in the Sun's SDK documentation. Another valuable source of inspiration is Logging in NetBeans.

GeoTools typically (but not always) uses one logger per package and is named after the package name. Private classes or implementations sometime use the name of their public counterpart.

Getting a Logger
^^^^^^^^^^^^^^^^

Java provides a factory method Logger.getLogger(String), however in the GeoTools library we use org.geotools.util.logging.Logging.getLogger(String) to take advantage of our own LoggerFactory system:

.. code-block:: java
   
   package org.geotools.mypackage;
   
   import java.util.logging.Logger;
   import org.geotools.util.logging.Logging:

   public class Example {
     void method() {
        final Logger LOGGER = Logging.getLogger("org.geotools.mypackage");
        LOGGER.config("There is some configuration information.");
     }
   }

Logger Declaration
^^^^^^^^^^^^^^^^^^

The logger may be declared in the class's static fields or returned by a class's static method. This is not mandatory but suggested if it is going to be used is several methods:

.. code-block:: java
   
   package org.geotools.image;
   
   import java.util.logging.Logger;
   import org.geotools.util.logging.Logging:
   
   public class ImageWorker {
   
       /** The logger ImageWorker.class used is "org.geotools.image" */
       private static final Logger LOGGER = Logging.getLogger(ImageWorker.class);
   }

Abstract classes may define a protected logger for their subclasses to use:

Logging Messages
^^^^^^^^^^^^^^^^

Message can be conveniently logged using one of 7 predefined levels. The levels in descending order are:

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

A convenience method exists in Logger for each of those levels:

.. code-block:: java
   
   LOGGER.info("There is a message of interest for ordinary user");
   
Do not use the logging info level as a replacement of System.out.println for displaying debug information to the console.
   
The INFO level is for end users. Use the FINE, FINER or FINEST levels for debug information.

Logging configuration
^^^^^^^^^^^^^^^^^^^^^

There are :file:`logging.properties`` files available in the project directory:

* :file:`quiet-logging.properties` - default for maven builds
* :file:`info-logging.properties` - use ``-Dlogging-profile=info-logging`` to enable

To provide custom logging when testing:

1. Add `src/test/resources/logging.properties`
2. In your project :file:`pom.xml` update ``maven-surefire-plugin`` configuration:

   .. code-block:: xml
   
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <systemPropertyVariables>
            <java.util.logging.config.file>src/test/resources/logging.properties</java.util.logging.config.file>
          </systemPropertyVariables>
        </configuration>
      </plugin>
      
See the :user:`user guide for details <library/metadata/logging/java_logging.html>` for examples of configuration, and bridging to other logging systems.

Entering/Existing Logger
^^^^^^^^^^^^^^^^^^^^^^^^

These buil-in tracing methods log details at FINER level.

There are three more convenience methods: entering, exiting and throwing when entering and exiting a method:

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


Minimising Logger output
^^^^^^^^^^^^^^^^^^^^^^^^^

When logging a message, the logger will include detailed information such as date and time, source class and method name, current thread, etc.

In order to minimise  the amount of information logged, it may be useful to merge consecutive logging into a single log statement.

This is especially appropriate if the many logs are actually different parts of a multi-lines message. Using distinct logger calls can result in an output interleaved with the logging from an other thread. Merging the logging is not appropriate if the log messages are conceptually unrelated.

Wasteful use of logging::
   
   LOGGER.finer("Value for A is "+A);
   LOGGER.finer("Value for B is "+B);
   LOGGER.finer("Value for C is "+C);

Good use of logging::
   LOGGER.finer("Computed values: A="+A+"; B="+B+"; C="+C);

Selective Logging
^^^^^^^^^^^^^^^^^^

If the log message is expensive to construct, then consider enclosing it in an if statement.::
   
   if (LOGGER.isLoggable(Level.FINER)) {
      LOGGER.finer("Current state = "+someVeryExpensiveMethodCall());
   }

