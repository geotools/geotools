Referencing FAQ
---------------

Q: Why can't I display shapefile over a WMS layer?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This can happen when the axis definition (ie. which way is X ?) differs between the coordinate reference system used by
the shapefile and that used by the WMS layer.  It can be fixed by requesting GeoTools to enforce lon - lat axis order by 
including this statement in your code prior to displaying layers...

.. sourcecode:: java

  Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE); 

If this doesn't work you can try this more brutal, System wide approach...

.. sourcecode:: java

  System.setProperty("org.geotools.referencing.forceXY", "true"); 

See also:

* :doc:`/library/referencing/order`
* A `Jira issue <http://jira.codehaus.org/browse/GEOT-2995>`_ discussing this problem

Q: How to choose an EPSG Authority?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The referencing module does not do very much out of the box - it needs someone
 to tell it what all the funny codes mean (such as "EPSG:4326").

You need to choose a single "epsg" jar to have on your classpath; if you have
several epsg jars on your classpath you will get a FactoryException.
  
For most needs just use the **gt-epsg-hsql** plugin:

* **gt-epsgh-hsql**: will unpack an hsql database containing the official epsg
  database into a temp directory, a great solution for desktop applications.

There are several alternatives:

* **gt-epsg-wkt**: uses an internal property file and is lightweight rather than
  official and correct. A great solution for applets
* **gt-epsg-postgres**: uses the official epsg database which you have to load
  into PostgreSQL yourself. A great solution for Java EE applications.  
* **gt-epsg-access**: irectly use an the official epsg database as distributed.
  A great solution for windows users that need the latest official database.

Unsupported:

* **gt-epsg-oracle**: Load the official epsg database into oracle to use this plugin
* **gt-epsgh-h2**: use this popular pure java database

Q: Are other authorities other than EPSG supported?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The main module includes definitions of AUTO and AUTO2 and several other OGC
inspired ideas.

Q: What Jars does gt-referencing need?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As an example to use **gt-epsg-hsql** you will need::
    
    C:\geotools\trunk\modules\plugin\epsg-hsql>mvn dependency:tree
    ...
    ------------------------------------------------------------------------
    Building EPSG Authority Service using HSQL database
       task-segment: [dependency:tree]
    ------------------------------------------------------------------------
    [dependency:tree]
    org.geotools:gt2-epsg-hsql:jar:2.5-SNAPSHOT
    +- junit:junit:jar:3.8.1:test
    +- javax.media:jai_core:jar:1.1.3:provided
    +- org.geotools:gt2-referencing:jar:2.5-SNAPSHOT:compile
    |  +- java3d:vecmath:jar:1.3.1:compile
    |  +- commons-pool:commons-pool:jar:1.3:compile
    |  \- org.geotools:gt2-metadata:jar:2.5-SNAPSHOT:compile
    |     +- org.opengis:geoapi:jar:2.2-SNAPSHOT:compile
    |     +- javax.units:jsr108:jar:0.01:compile
    |     \- edu.oswego:concurrent:jar:1.3.4:compile
    +- org.geotools:gt2-sample-data:jar:2.5-SNAPSHOT:test
    +- hsqldb:hsqldb:jar:1.8.0.7:compile
    +- net.sourceforge.groboutils:groboutils-core:jar:5:test
    \- commons-dbcp:commons-dbcp:jar:1.2.2:test
    ------------------------------------------------------------------------

Q: Bursa-Wolf Parameters Required?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

GeoTools performs datum shift using Bursa-Wolf parameters at this time. If
these cannot be determined from your CoordinateReferenceSystem we will be
unable to sort out a transform.
  
Most of the time this does not matter as users work with their information in
the same datum it was collected in.

* A: Lenient
  
  A quick fix involves setting "lenient " to true when searching for a
  MathTransform.::
      
      MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
  
  This approach is good enough for display, but not recommended for editing or
  careful analysis work.
  
* A: Match your PRJ file
  
  Usually this occurs when you have loaded a CoordinateReferenceSystem from a
  "prj" file included with your shapefile.
  
  To fix look up the complete definition in the EPSG database using the CRS
  utility class. The EPSG database contains more information than can be
  Expressed in your "prj" file.
  
* A: Datum shift by Grid
  
  Finally in the rare case of a Datum shift backed by grids are only partially
  implemented. Rueben Shulz has implemented data shift backed by grids for
  NADCON with the following limitations:

  * Use of NADCON grids has not been integrated with
    DefaultCoordinateOperationFactory (so you would need to set it up by hand)
  * The general case of a Datum shift provided by a grid is not covered, for
    example Spanish Datum Changes ED50-ETRS89 will not work

Q: What does gt-referencing do?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You will eventually care about this as it defines how 2D data actually means 3D
data.

You can take a measurement (ie a coordinate) plus a coordinate reference system (the
meaning) and figure out where a position on the earth is in 3D space.

It is easy to assume that Coordinates are recorded in double[] as repeating x/y values
as is often done in CAD programs.

In a GIS application we can only wish they are X,Y. They are actually LAT/LONG or
LONG/LAT or angle,angle,angle or something crazy like time.
  
Given two CoordinateReferenceSystems you can make a math transform from one to the
other. So you could take angle/angle/angle and munch it into something you like for
3D or 2D display.

We will get into this again when you actually have some data - the CoordinateReferenceSystem will tells you what the data "means".

 Much like 3.0 and "three meters". The first is a number, and the second one means a length.

Q: Can I just use Referencing without the rest of GeoTools?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Yes, you will need to use the metadata module, and one of the epsg modules. Along with
their dependencies such as units.
  
Q: I cannot find an EPSG Code?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You need to have one, and only one, of the epsg plugins on your CLASSPATH. We
recommend epsg-hsql for most uses.

Q: I cannot re-project my shapefile (missing "Bursa wolf parameters")?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This error occurs when you try to re-project a shapefile with a custom coordinate
reference system::
     
     MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, moroccoCRS);
  
This problem often occurs when working with a custom coordinate reference system, if
you look in your shapefile's prj file you will see that the contents is normal "well
known text".

The "Bursa-Wolf parameters" define the relationship (and a transformation) between the
spheroid being used by your shapefile and the normal WGS84 spheroid.

The opengis javadocs has some help for you here:

* http://docs.geotools.org/stable/javadocs/org/opengis/referencing/doc-files/WKT.html#TOWGS84

Your best bet is to look up the normal EPSG code for your area; and update your "prj"
file to include the official definition.

Q: How do I Transform?
^^^^^^^^^^^^^^^^^^^^^^

The MathTransform interface is used to transform (or "re-project") one DirectPosition
at a time.

You can also use the utility class **JTS** which has helper methods to transform
a Geometry.

Q: How to Transform a Geometry?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  
You can use the JTS utility class to create a new geometry in the desired projection::
    
    MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
    Geometry targetGeometry = JTS.transform( sourceGeometry, transform);

Q: How to Transform an Envelope?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

For a referenced envelope you can transform directly:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :start-after: // transformReferencedEnvelope start
   :end-before: // transformReferencedEnvelope end

For a JTS Envelope use the **JTS** utility class:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :start-after: // transformEnvelope start
   :end-before: // transformEnvelope end

Q: How to Transform a GridCoverage?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can use a resample operation to produce a GridCoverage in the desired projection.

Q: How do I extend the system with my own custom CRS?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  
If you wish to act as your own authority you can register an additional factory with
the system.

This is often used by those working with a national standards body that maintains its
own set of official codes.

You can use the **gt-epsg-wkt** plugin as an example of the following options:

* Through a property file and programatic registration of the factory
  
  You can create a file with your CRS in the WKT format, instantiate a 
  PropertyAuthorityFactory with that CRS:
  
  1. Create a property file with your CRS definitions in Well-Known Text (WKT) format.
  2. Each line of the file should be "someUniqueCodeValue = crsInWKT".
  3. Place that file as a resource available from your code at run time
  4. Create a org.geotools.referencing.factory.PropertyAuthorityFactory with a custom
     code for the authority such as "CUSTOM" and a URI to your file
  5. Register your factory with the ReferencingFactoryFinder.addAuthorityFactory(..)
     method
  
  After that, the desired CRS can be invoked as "CUSTOM:someUniqueCodeValue" so, for
  example, a CRS object can be created using the CRS.decode(..) method with the string
  nomenclature of authority-colon-code. The CRS's defined in this way will also be taken
  into consideration by the rest of the referencing subsystem.

* Through a property file and automatic registration of the factory
  
  A more sophisticated approach changes step 3 to create a new class which both extends
  PropertyAuthorityFactory and has a no argument constructor which calls the parent
  with the right URI argument. Such a class will be picked up automatically when the
  factory system is initialized so step 4 in the list above is no longer necessary.

Q: How to I add my own EPSG Codes?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The gt-epsg-wkt plugin is intended to be used on its own and should not be combined
with any of the other gt-epsg-h2, or gt-epsg-hsql plugins as they will end up in
conflict.

If you simply want to add a few more definitions over and above those provided by the
official database (ie epsg-hsql or epsg-h2) please use the following (taken from the
uDig application)::
  
     URL url = new URL(url, "epsg.properties");
     // application directory or user directory?
     // where you are looking for epsg.properties
     if ("file".equals(proposed.getProtocol())) {
         File file = new File(proposed.toURI());
         if (file.exists()) {
             epsg = file.toURI().toURL();
         }
     }
     // you may try several locations...
     if (epsg != null) {
         Hints hints = new Hints(Hints.CRS_AUTHORITY_FACTORY, PropertyAuthorityFactory.class);
         ReferencingFactoryContainer referencingFactoryContainer =
                    ReferencingFactoryContainer.instance(hints);
        
        PropertyAuthorityFactory factory = new PropertyAuthorityFactory(
                           referencingFactoryContainer, Citations.fromName("EPSG"), epsg);
        
        ReferencingFactoryFinder.addAuthorityFactory(factory);
        ReferencingFactoryFinder.scanForPlugins(); // hook everything up
     }
  
Here is an example ::download::`epsg.properties </artifacts/epsg.properties>`. file used by uDig:
  
   .. literalinclude:: /artifacts/epsg.properties
