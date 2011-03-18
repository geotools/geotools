Envelope
--------

Envelopes are used to represent the bounds of a geometry, and are used very frequently as a way to quickly check if a geometry is in the area you are interested in.

.. image:: /images/envelope.PNG

GeoTools, through virtue of reusing code, has two "Envelope" implementations to contend with:

* Java Rectangle2D - we have **Envelope2D** which is spatial extension of the Java Rectangle2D class. Our implementation makes use of doubles to store coordinates and holds on to a CoordinateReferenceSystem allowing us to tell where the coordinates are located.

* JTS Envelope - we have *ReferencedEnvelope* which is adds a CoordinateReferenceSystem to a traditional JTS Envelope.

As you can see **ReferencedEnvelope** is a bit of a olive branch between the JTS Geometry model and ISO Geometry model.

You will find other "Rectangles" around as you make use of GeoTools in a real world application.

* Java Rectangles
  
  Java Rectangles record x,y,w,h:

* Rectangle2D
  
  * Rectangle2D.Double rectangle working with doubles
  * Rectangle2D.Float rectangle working with floats

* Envelope2D we have a spatial specific version of Rectangle2D that implements ISO Geometry Envelope

* Rectangle the original rectangle for working on the screen, measured in integers

ReferencedEnvelope
^^^^^^^^^^^^^^^^^^

ReferencedEnvelope is both:

* **Envelope** - as defined by the JTS Topology System ( a Simple Feature for SQL concept)
* **BoundingBox** - 2D bounds as defined by the ISO 19107 Geometry
* **Envelope** - captures 3D bounds as defined by ISO 19107 Geometry.

In short this is the class to use when you want to represent a bounds in GeoTools. The only other thing of note is the that the constructor expects the input in xMin,xMax,yMin,yMax order and expects a crs.:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :start-after: // exampleReferencedEnvelope start
   :end-before: // exampleReferencedEnvelope end

ReferencedEnvelope does one thing very well; it is an Envelope that has a CoordinateReferenceSystem. Because it has a CoordinateReferenceSystem you can quickly transform it between projections.:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :start-after: // transformReferencedEnvelope start
   :end-before: // transformReferencedEnvelope end

**ReferencedEnvelope** is used in a lot of GeoTools interfaces, basically anywhere we can get away with it. Simply put without knowing the the CoordinateReferenceSystem a raw JTS Envelope is incomplete; and difficult to use safely without making assumptions.

Some of our older interfaces that you are forced to read the javadocs in order to figure out the CoordinateReferenceSystem for a returned Envelope.

* Using a FeatureSource without ReferencedEnvelope example::
  
    Envelope bounds = featureSource.getBounds();
    
    CoordinateReferenceSystem crs = featureSource.getSchema().getDefaultGeometry().getCoordinateSystem();

* Using a FeatureSource with ReferencedEnvelope::
   
   ReferencedEnvelope bounds = (ReferencedEnvelope) featureSource.getBounds();
   
   CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();

OpenGIS Envelope
^^^^^^^^^^^^^^^^

OpenGIS records a "rectangle" as a bounds along the axis mentioned by the CoordinateReferneceSystem object. You can use this idea to record a simple rectangle in space, a height range and and a range in time as needed.

.. image:: /images/envelope2.PNG

* Envelope2D - was introduced above; used to bridge to Java2D
* ReferencedEnvelope - was introduced above; used to bridge to JTS Geometry
* GeneralEnvelope - allows you to record spans in multiple dimensions (think depth, height or time)

Since **Envelope** is just and interface, so we will use **RefernecedEnvelope** for the example:
  
  .. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
     :language: java
     :start-after: // exampleISOEnvelope start
     :end-before: // exampleISOEnvelope end

You can see even in a simple example we should be looking at the CRS to figure out what the axis is actually measuring.

If you are super confident that you are working with data in X/Y order you can directly make use of BoundingBox box. BoundingBox is an extension of Envelope for working with 2D data, and it has been made method compatible with JTS Envelope where possible.

Since **BoundingBox** is just an interface, so we will use **RefernecedEnvelope**
for this example:
  
  .. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
     :language: java
     :start-after: // exampleBoundingBox start
     :end-before: // exampleBoundingBox end

JTS Envelope
^^^^^^^^^^^^

The JTS Topology Suite has the concept of an Envelope recorded in x1,x2, y1,y2 order.

You can see that the use of JTS Envelope has the same "assumptions" as the use of BoundingBox above.:
  
  .. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
     :language: java
     :start-after: // exampleEnvelope start
     :end-before: // exampleEnvelope end

Transform an Envelope using the JTS Utility class:
  
  .. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
     :language: java
     :start-after: // transformEnvelope start
     :end-before: // transformEnvelope end

