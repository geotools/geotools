Integration
-----------

The GeoTools library allows you to integrate your own content using the Factory pattern. You can "register" a class (called a Factory) that the library will use to produce instances of your code.

The code you provide will be usually be in the form of one of those interfaces we learned about when using the library (say DataStore if you are connecting the library up to a new form of content).

The exact form of registration depends on the environment the library is used in, in the simple case of dropping a jar on the class path we use the "Factory SPI" system provided as part of Java. In a complicated case like Eclipse we must hook into their plug-in system provided by the OSGi standards body. Depending on the system used you may be able to take further control (perhaps choosing definitions provided by the epsg-hsql module over those provided by epsg-wkt etc...).

These pages document hosting a GeoTools application in different environments, and provide guidelines for taking control of the process of lookup.

Service Provide Interface
^^^^^^^^^^^^^^^^^^^^^^^^^

Out of the box the GeoTools library operates using Java's built in "Factory SPI" look up mechanism to find plug-ins on the CLASSPATH. The Factory SPI system makes use of additional files in a jars MANIFEST/ directory, the GeoTools library looks at these files to find out what is available.

Using Hints
'''''''''''

A hint is most useful to control the "default" behaviour of the library you expect out of the library.

Example code (for your application)::
  
  private static final Hints MY_APPLICATION_SETTINGS = new Hints();
  static {
      MY_APPLICATION_SETTINGS .put(Hints.CRS_AUTHORITY_FACTORY, myOwnCrsFactory);
      MY_APPLICATION_SETTINGS .put(Hints.CRS_GEOMETRY_FACTORY, myOwnGeomFactory);
      MY_APPLICATION_SETTINGS .put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
      MY_APPLICATION_SETTINGS .put(Hints.EPSG_DATA_SOURCE, "jdbc/MyOwnEPSG");
      // etc.
  }
  
  void anyUserMethodWantingFactory() {
      FeatureFactory f = FactoryFinder.getFeatureFactory(MY_APPLICATION_SETTINGS);
      Feature fe = f.createFoo(...);
      // etc.
  } 

You can have more then one set of Hints in the same JVM so several applications can work at once in a Java EE setting.

Command Line
''''''''''''

1. Quick and Dirty Using System Properties
   
   You can change defaults using System properties as your program starts up::
      
      /**
       * Use: java toWKT 4326
       */
      public class ToWKT {
          public void main(String args[]){
              CoordianteReferenceSystem crs = CRS.decode( "EPSG:"+args[0] );
              System.out.println( crs );
          }
      }

2. We can specify that the FactoryUsingWKT authority is used::
      
      java -Porg.opengis.referencing.crs.CRSAuthorityFactory=org.geotools.referencing.factory.epsg.FactoryUsingWKT PrintWSG84

GeoTools Class
'''''''''''''''

The defaults provided above via System Properties can be accessed using the GeoTools from your program::
  
  Hints hints = GeoTools.getDefaultHints();
  FilterFactory filter = CommonFactoryFinder.getFilterFactory( hints );

You can also set the default hints yourself::
  
  interface Foo implements Applet {
     static {
        Hints hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, FactoryUsingWKT.class);
        GeoTools.init( hints ); // Set FactoryUsingWKT as the default
     }
  }

This is useful when working with Applets where where access to the System properties is not available.

Factory control of GeoTools
'''''''''''''''''''''''''''

The plugin system for geotools allows you to write your plugin; and then register it in your jar as a service so that the rest of the library can use it. To register make a text file with the complete name of your function class in META-INF/services/

When adding a "Function" to GeoTools the name of the text file is the interface being implemented (in this case "org.opengis.filter.expression.Function"), the contents is a list of functions, one per line. I cannot remember if you need an extra line feed at the end of the file or not.

For a good working example look at the cql module:

* http://svn.osgeo.org/geotools/trunk/modules/library/cql/

It advertises one function here:

* http://svn.osgeo.org/geotools/trunk/modules/library/cql/src/main/resources/META-INF/services/org.opengis.filter.expression.Function

* And implements it here:
  
  http://svn.osgeo.org/geotools/trunk/modules/library/cql/src/main/java/org/geotools/filter/function/PropertyExistsFunction.java

Combining the two Techniques
''''''''''''''''''''''''''''

You can combine the two techniques:

1. Add an entry to your MANIFEST/ so that GeoTools can find your Factory
2. Use a hint to ensure your Factory is chosen as the default

Spring
^^^^^^

You can use Spring as the look up environment for GeoTools (rather then the default Factory SPI approach). This has several advantages:

* Spring can find every instance of a Factory on the CLASSPATH
* If you are already using Spring to wire your application, you can operate
  with GeoTools in the same manner

You do need to make one call as part of your application's startup in order to use Spring, we have isolated the library "lookup" technique to a single location and ask you to provide the following in your Spring context.

OSGi
^^^^

Using OSGi with GeoTools is a great idea, and one we are still working on. OSGi is the plugin environment used by Eclipse and Spring deployment environment.

Single Plugin
'''''''''''''

The udig project uses this technique as a stopgap solution: place all the GeoTools jars
into a single plugin will allow the Factory SPI system to function.

The Eclipse environment uses OSGi to manage the loading and unloading of "bundles" of resources,
such as the classes and raw data shipped as part of GeoTools. The environment is very safe and is
careful to use separate classloaders for each bundle; at a pragmatic level this means you will get
class cast exceptions even when everything else looks correct.

GeoTools makes use of a Factory SPI system to tie our modules together, this works by examining each
jar for a META-INF/services/\*.txt files. This works out of the box when all jars are loaded via the
same classloader (e.g. outside of OSGi with all jars on the CLASSPATH).

One of the main points of OSGi is providing a module system with strict visibility rules, so putting
a collection of jars into one bundle rather defeats the purpose of modularity. It would be more
natural to have a separate bundle for each Geotools jar, but this requires some trickery to make the
Factory SPI system work.

Hopefully, this will be supported out of the box in a future Geotools release. The next section
explains how to create a bundle per jar manually.

One Bundle per Jar
''''''''''''''''''

In most cases, OSGi bundles are delivered as JAR files. The only difference between an OSGi bundle
and a plain old JAR file is a number of special headers in the manifest required by the OSGi
standard. Given a plain old JAR, you can wrap it in a bundle by creating an OSGi compliant
manifest, putting your JAR on the Bundle-Classpath and creating a bundle JAR containing your
new manifest and the plain old JAR. 

However, this is not recommended, since a JAR-in-a-JAR means extra work for the classloader
to retrieve classes from the inner JAR. To make a plain old JAR OSGi-compliant, you can unzip
the JAR, add the required OSGi headers to the manifest and then rezip the whole lot.

After rebundling, any resources from the plain old JAR are now first-class citizens of the bundle
JAR. This includes any files in META-INF/services, and this is in fact the first step to make the
Factory SPI system work.

.. note::
   
   Eclipse 3.4 has a new wizard for rebundling JARs. Search the Eclipse Help
   for Convert Jars to Plug-in Projects.

There are runtime dependencies between the Geotools JARs, e.g. gt-main.jar depends on gt-metadata.jar. These need to be translated to corresponding Import-Bundle header in the bundle manifest. For instance, if you turn these two JARs into bundles org.geotools.main and org.geotools.metadata, then the MANIFEST.MF of org.geotools.main will have to contain the following information::
  
  Bundle-SymbolicName: org.geotools.main
  Bundle-Version: 2.6.0
  Export-Package: org.geotools.catalog,
   org.geotools.data,
   org.geotools.data.collection,
   ...
  Require-Bundle: org.geotools.metadata;bundle-version="2.6.0",
   ...

.. note::
   
   It is considered good practice for OSGi to use Import-Package rather than
   Require-Bundle to minimize coupling between bundles. Unfortunately, it is
   currently difficult to make this work with Geotools, due to a considerable
   number of split packages. A split package is a Java package occurring in
   more than one bundle, like org.geotools.factory occurring both in
   gt-main.jar and gt-metadata.jar. So for the time being, you should use
   Require-Bundle to define the dependencies between Geotools bundles.

The central method of the Factory SPI system is FactoryRegistry.scanForPlugins() in bundle org.geotools.metadata. You need to ensure that this bundle will have access to all META-INF/services resources from service provider bundles like org.geotools.main and others.

This is some kind of callback dependency of org.geotools.metadata on org.geotools.main (and any other service provider bundles). It is a major concern of OSGi to prevent cyclic dependencies, so you cannot have two bundles requiring each other.

There are two solutions (or rather, workarounds) for this situation:

* buddy policies (a non-standard feature of Equinox, the Eclipse OSGi implementation)
* fragments (OSGi-standard compliant, but less flexible)

Adding the following line to the manifest of org.geotools.metadata::

    Eclipse-BuddyPolicy: registered

This effectively means "If I cannot find a class or resource locally or in my
required bundles, I will ask my buddies, i.e. all bundles which depend on me
and declare themselves to be a buddy of mine".

To turn org.geotools.main into a buddy of org.geotools.metadata, add the following header to the manifest of org.geotools.main::
  
    Eclipse-RegisterBuddy: org.geotools.metadata

If your OSGi framework is not Equinox, you may try to use fragments instead. (This has not yet been tested with Geotools, and it may not be supported by all OSGi implementations, even though this is a standard feature.)

A fragment looks like a bundle, but it depends on a bundle host. Fragments are a way of adding classes or resources to the host bundle.

Defining a fragment org.geotools.factory.extensions with the following manifest::
  
  Bundle-SymbolicName: org.geotools.factory.extensions
  Fragment-Host: org.geotools.metadata
  Require-Bundle: org.geotools.main, ...

This should also solve the Factory SPI problem. The fragment requires the service provider bundles and contributes their resources to the factory bundle. This is another of way of modelling callback dependencies in OSGi.

.. note:: 
   
   All of this should be regarded as a mere workaround to make legacy code
   work in an OSGi environment in a way that is backward compatible, i.e. you
   can still use your bundle JARs as plain old JARs on the classpath.

If at some point in future Geotools should decide to go the OSGi way (and allow itself to become
dependent on OSGi), the Factory SPI approach should be dropped in favour of the OSGi service
registry. Service providers would register their services under the class name of the implemented
interface. Clients would use the OSGi service registry to look up the available services for an
interface, possibly using additional parameters to select a specific implementation.

Third-Party Dependencies
''''''''''''''''''''''''

In either approach, all-in-one or bundle-per-JAR, you also have to deal with external dependencies
of Geotools, like vecmath, jdom, geoapi, and many others.

You could further blow up your all-in-one bundle by also including the JARs for these external
dependencies. Chances are high that some of these are also used by other non-Geotools bundles in
your application, so this is likely to cause classloader problems, say if you already have a JDOM
bundle in your system.

Thus, you should really follow the bundle-per-JAR approach and OSGify each third-party dependency
into a separate bundle. Actually, there is no need to do all the work on your own: The SpringSource
Enterprise Bundle Repository provides OSGified versions of many popular Java libraries.

Eclipse-BuddyPolicy: ext
''''''''''''''''''''''''

The GeoTools library makes use of Java Advanced Imaging - which is a Java extension. Just as OSGi
is very careful about dependencies between bundles; it is also careful to ensure you do not
accidentally depend on a Java extension that may not be present.

A normal application works like this:

1. Java Classes - like String
2. Java Extension Classes - like JAI
3. Classpath - system environmental variable, or -cp command line option 
   default: .;bin\..\classes;bin\..\lib\classes.zip

OSGi takes over and forces you to choose what you are doing:

1. Java Classes - like Stirng
2. everything that is "published" by the bundles you depends on
   
If you add the following to your plugin manifest.mf::

    Eclipse-BuddyPolicy: ext

OSGi will start you up with the following:

1. Java Classes - like String
2. Java Extension Classes - like JAI
3. everything that is "published" by the bundles you depends on

Which will enable GeoTools code to work (yeah!).
