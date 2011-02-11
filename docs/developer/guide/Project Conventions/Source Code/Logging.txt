Logging
-------

We use the logging package bundled into J2SE 1.4 and above (java.util.logging). An overview is available on line in the Sun's SDK documentation. An other valuable source of inspiration is Logging in NetBeans.

GeoTools typically (but not always) uses one logger per package and is named after the package name. Private classes or implementations sometime use the name of their public counterpart.

Getting a Logger
^^^^^^^^^^^^^^^^

The Java way to get a logger is to invoke Logger.getLogger(String). However in the GeoTools library, this call is replaced by a call to Logging.setLogger(String) where Logging is a class in the org.geotools.util.logging package.

Example::
   
   import org.geotools.util.logging.Logging;
   
   class MyClass {
      void method() {
        Logger logger = Logging.getLogger("org.geotools.mypackage");
        logger.config("There is some configuration information.");
     }
   }

Logger Declaration
^^^^^^^^^^^^^^^^^^

The logger may be declared in the class's static fields or returned by a class's static method. This is not mandatory but suggested if it is going to be used in many places.

Example::
   
   import java.util.logging.Logger;
   import org.geotools.util.logging.Logging:
   
   public class GMLDataStore {
       /** The logger for the GML Data Store */
       private static final Logger LOGGER = Logging.getLogger("org.geotools.data.gml.GMLDataStore");
   }

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

A convenience method exists in Logger for each of those levels::
   
   LOGGER.info("There is a message of interest for ordinary user");
   

Do not use the logging info level as a replacement of System.out.println for displaying debug information to the console.
   
The info level is for end users. Use the fine, finer or finest levels for debug information, and setup yours $JAVA_HOME/jre/lib/logging.properties file accordingly (see Logging Configuration below).

Entering/Existing Logger
^^^^^^^^^^^^^^^^^^^^^^^^

There is three more convenience methods: entering, exiting and throwing when entering and exiting a method, or when we are about to terminate a method with an exception.::
   
   public Object myMethod(String myArgument) {
       LOGGER.entering("MyClass", "MyMethod", myArgument);
       // ... do some process here
       LOGGER.exiting("MyClass", "MyMethod", myReturnValue);
       return myReturnValue;
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

If the log message is expensive to construct, then consider enclosing it in a if statement.::
   
   if (LOGGER.isLoggable(Level.FINER)) {
      LOGGER.finer("Current state = "+someVeryExpensiveMethodCall());
   }

Logging Configuration
^^^^^^^^^^^^^^^^^^^^^^

To change the default logging setting, edit the following file:

* $JAVA_HOME/jre/lib/logging.properties

To define a default configuration level provide a the **.level** property to the minimal level of interest for you::
   
   .level= FINER

You can specify a different level to be shown to the console (than is saved out to xml). To define the java.util.logging.ConsoleHandler.level property to the minimal level you want to see on the console::
   
   # Limit the message that are printed on the console to FINE and above.
   java.util.logging.ConsoleHandler.level = FINE
   java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter
   java.util.logging.ConsoleHandler.encoding = Cp850

Note the **encoding** property. For Windows user, it should be set to the value displayed by chcp on the command line. Linux and Unix users may ignore this line since Unix systems do a more intelligent work with page codes.

To list detailed messages for a specific module you can define a different logging level may be specified for each module.::
   
   org.geotools.gml.level = FINE
   org.geotools.referencing.level = INFO

Provides fairly detailed logging message from the GML module, but not from the referencing module.

Log4J interoperability
^^^^^^^^^^^^^^^^^^^^^^

Geotools can produces a console output similar to the Log4J one (single-line instead of multi-line log message) if the following code is invoked once at application starting time::
   
   Logging.ALL.forceMonolineConsoleOutput();
   

Alternatively, this formatter can also be configured in the logging.properties without the need for the above-cited method call. See the **MonolineFormatter** javadoc for details.

The logging output can also be redirected to the real Log4J framework (or any other framework supported by Apache's Common Logging) if the following code is invoked once at application starting time:

Logging.ALL.setLoggerFactory("org.geotools.util.logging.Log4JLoggerFactory");

Why not common-logging?
^^^^^^^^^^^^^^^^^^^^^^^

The common-logging API is basically a set of println functions with name (info, trace, debug, etc.). Java logging API provides the same convenience methods, but is also richer. We use some of its extra capabilities in GeoTools code base:

* ResourceBundle support for localization.
* Logging of stack traces.
* Information on source class and method names.
* Information about which thread produced the logging.
* Can be used through Java Monitoring and Management system.

Log4J offers similar functionalities with a wider range of handler implementations. On the other hand, Java logging is more closely tied to the JVM, which avoid some ClassLoader problems that prevent usage of Log4J in some environments.

We are not claiming that Java logging in superior to Log4J, neither we are forcing peoples to use Java logging. We push for usage of Java logging API, which may very well redirect to Log4J under the hood through java.util.logging.Log4JLoggerFactory implementations.

Commons-logging is widely used in server containers, but other communities like scientists face a different picture. For example the NetCDF library developped by University Corporation for Atmospheric Research (UCAR) uses SLF4J, yet other logging framework that aims to be a replacement for commons-logging.