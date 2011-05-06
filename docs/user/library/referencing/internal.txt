Internal
--------

Please note that this page is optional and only for the curious. It covers some of the
implementation classes.

A **CoordinateReferenceSystem** is a **gt-opengis** interface describing how a set of
ordinates is to be interpreted as a three dimensional point. This definition is standardised,
mathematical and generally not of interest unless something goes wrong.

CoordinateReferenceSystem
^^^^^^^^^^^^^^^^^^^^^^^^^

For most cases you are only interested in using a CoordinateReferenceSystem as a parameter to a
mathematical calculation (distance along the surface of the earth and "re-projection"
being the most common).

Creating a CoordinateReferenceSystem:

.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // createFromEPSGCode start
   :end-before: // createFromEPSGCode end

You will need to ensure GeoTools is configured with an appropriate plugin for this example to work.
This plugin will provide an CRSAuthorityFactory registered for "EPSG" codes.

CRSAuthorityFactory
^^^^^^^^^^^^^^^^^^^

Internally the **CRS** class makes use of a **CRSAuthorityFactory** to provide the definition for
the indicated code. If you wish you can make use of the same facilities directly:

.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // createFromEPSGCode2 start
   :end-before: // createFromEPSGCode2 end

To create the actual **CoordinateReferenceSystem** object a **CRSFactory** is used, for example when parsing
a "well known text" (WKT) definition:

.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // creatCRSFromWKT start
   :end-before: // creatCRSFromWKT end

Where the code above corresponds to this definition::
  
  GEOGCS[
    "WGS 84",
    DATUM[
      "WGS_1984",
      SPHEROID["WGS 84",6378137,298.257223563,AUTHORITY["EPSG","7030"]],
      TOWGS84[0,0,0,0,0,0,0],
      AUTHORITY["EPSG","6326"]],
    PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]],
    UNIT["DMSH",0.0174532925199433,AUTHORITY["EPSG","9108"]],
    AXIS["Lat",NORTH],
    AXIS["Long",EAST],
    AUTHORITY["EPSG","4326"]]


Creating a CoordinateReferenceSystem
''''''''''''''''''''''''''''''''''''

You can use factories defined by the referencing system to create things by hand using java code.

This example shows the creation of a WGS84 / UTM 10N CoordinateReferenceSystem:
  
.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // createCRSByHand1 start
   :end-before: // createCRSByHand1 end

The next example shows NAD 27 geographic CRS being defined, with a couple of interesting things to note:

* The datum factory automatically adds alias names to the datum. 
  (The DatumAliasesTable.txt file inside gt-referencing has an entry for "North American Datum 1927").
* The toWGS84 information being supplied for use in a datum transform is added to the Datum

Here is the example:

.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // createCRSByHand2 start
   :end-before: // createCRSByHand2 end

Finally, here is no-holds-barred creation of a CoordianteReferenceSystem with all the usual helper classes
stripped away.  It does not use any of the static objects available in GeoTools. The following example
creates a CoordianteReferenceSystem to represent WGS84.

.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // createCRSByHand3 start
   :end-before: // createCRSByHand3 end

Referencing Factories
^^^^^^^^^^^^^^^^^^^^^

These are the "real" factories - interfaces that actually create stuff. All are gt-opengis interfaces, so you will need
to use ReferencingFactoryFinder to get started:

* DatumFactory:
  Makes datums!
* CSFactory
  Makes CoordinateSystem instances, and many more
* CRSFactory
  Makes CoordinateReferenceSystem instances, and many more
* MathTransformFactory
  Makes MathTransform instances, and many more

You can quickly grab all four factories at once using **ReferencingFactoryContainer**:

.. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
   :language: java
   :start-after: // factories start
   :end-before: // factories end

* ReferencingFactoryFinder
  
  As is custom we have included a "FactoryFinder" so you can look up a good
  implementation on the CLASSPATH::
    
    DatumFactory datumFactory = ReferencingFactoryFinder.getDatumFactory(null);

  The ReferencingFactoryFinder returns a couple of GeoTools implementations right now, in
  the future we hope to replace these defaults with an implementation from JScience. 

* ReferencingFactoryContainer
  
  You may have noticed that to actually do anything you need several factories. We have gathered
  these together into a "container" for you. The container also adds a few more methods which use a
  couple of factories to gang up on a problem

  You can set up ReferencingFactoryContainer to use your own custom factory using hints as shown
  below:
  
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // referencingFactoryContainer start
     :end-before: // referencingFactoryContainer end
  
  Please note that ReferencingFactoryContainer is not strictly needed, it just makes things easier.
  ReferencingFactoryFinder will be smart and recycle instances where possible:
  
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // referencingFactoryContainer2 start
     :end-before: // referencingFactoryContainer2 end

Referencing Authority Factories
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Deep behind that CRS utility class is an amazing constellation of "factories" used to define what a
CoordinateReferenceSystem is, actually create the parts, and finally stitch them all together in a
unified whole.

These things are factories in name only; their real job is to supply the definitions (in pattern
speak they would be called *builders*).

* DatumAuthorityFactory:
  Defines a Datum using a code provided by a authority such as EPSG
* CSAuthorityFactory:
  Defines a CoordinateSystem using a code provided by an authority such as EPSG
* CRSAuthorityFactory:
  Defines a CoordinateReferenceSystem for a given authority (such as EPSG)
* CoordinateOperationAuthorityFactory:
  Defines coordinate operations from codes, backed by math transforms

To actually perform their function these authorities acquire a definition internally and then call a "real" factory class from ReferencingFactoryContainer.

* Getting the EPSG AuthorityFactory
  
  You can make direct use of the CRSAuthorityFactory configured to handle "EPSG" codes::
    
    CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
    CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("4326");

You will need to make sure that one of the **epsg** plugins is on your CLASSPATH (such as epsg-hsql).

* Finding the available EPSG Codes::
  
    CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
    Set<String> authorityCodes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);

* Getting Other AuthorityFactory Instances
  
  Here are several more examples that are understood by GeoTools::
    
    Hints hints = null; // Put optional hints here.
    CRSAuthorityFactory crsAuthority  = ReferencingFactoryFinder.getCRSAuthorityFactory("CRS",   hints);
    CRSAuthorityFactory wms2Authority = ReferencingFactoryFinder.getCRSAuthorityFactory("AUTO",  hints);
    CRSAuthorityFactory wms3Authority = ReferencingFactoryFinder.getCRSAuthorityFactory("AUTO2", hints);

* IdentifiedObject Finder for Controlled Searching
  
  One bit of functionality that is not available via the CRSAuthority interfaces directly
  is the ability to carefully search through all the available definitions.::
     
     CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
     AbstractAuthorityFactory custom = (AbstractAuthorityFactory) factory;
     
     IdentifiedObjectFinder finder = custom.getIdentifiedObjectFinder(CoordinateReferenceSystem.class);
     
     finder.setFullScanAllowed(true); // will search everything ever defined (may be slow)
     IdentifiedObject find = finder.find(crs);

     finder.setFullScanAllowed(false); // will limit search to what has been cached in memory
     IdentifiedObject find = finder.find(crs);
  
  As shown above this is additional functionality made available through
  AbstractAuthorityFactory - it is not part of the normal gt-opengis interfaces.

You can construct finders to search through other categories of referencing Objects (like Datum and ReferencingSystem).
