Envelope
--------

GeoTools supports working with JTS Topology Suite Envelopes (used to represent the extent of a Geometry).

The ``git-main`` module defines ``ReferencedEnvelope`` and ``ReferencedEnvelope3D`` as an integration between JTS Geometry model and ``gt-api`` ``Bounds`` and ``Bound3D`` interfaces.

.. image:: /images/reference_envelope.png

JTS Envelope
^^^^^^^^^^^^

The JTS Topology Suite has the concept of an ``Envelope`` recorded in ``x1,x2, y1,y2`` order.
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleEnvelope start
   :end-before: // exampleEnvelope end

Envelope Transform
''''''''''''''''''

Transform an ``Envelope`` using the JTS ``Utility`` class:
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8   
   :start-after: // transformEnvelope start
   :end-before: // transformEnvelope end

ReferencedEnvelope
^^^^^^^^^^^^^^^^^^

GeoTools ``ReferencedEnvelope`` extends JTS ``Envelope`` to implement the ``gt-api`` module ``Bounds`` interface.

``ReferencedEnvelope`` is all of these:

* ``org.locationtech.jts.geom.Envelope`` - as defined by the JTS Topology System ( a Simple Feature for SQL concept)
* ``org.geotools.api.geometry.BoundingBox`` - 2D bounds as defined by the ISO 19107 Geometry
* ``org.geotools.api.geometry.Bounds`` - captures 3D bounds as defined by ISO 19107 Geometry.

To support 3D bounds (and use a 3D Coordinate Reference System) we must create an instance of the child class ``ReferencedEnvelope3D`` (see below).

Use of ``ReferencedEnvelope`` is the most common representation of a bounds in GeoTools. The constructor expects the extent to be defined in ``xMin,xMax,yMin,yMax`` order for a 2D ``CoordianteReferenceSystem``:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleReferencedEnvelope start
   :end-before: // exampleReferencedEnvelope end

ReferencedEnvelope Transform
''''''''''''''''''''''''''''

``ReferencedEnvelope`` does one thing very well; it is an JTS ``Envelope`` that has a ``CoordinateReferenceSystem``. Using this ``CoordinateReferenceSystem`` you can quickly transform it between projections.:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // transformReferencedEnvelope start
   :end-before: // transformReferencedEnvelope end

``ReferencedEnvelope`` is used in a lot of GeoTools interfaces to represent the requirement to have a ``CoordianteReferenceSystem``.

Using a raw JTS ``Envelope`` without knowing the ``CoordinateReferenceSystem`` presents difficulty -
due to incomplete information client code is forced to make an assumption. Some code may assumes
the envelope is in ``WGS84`` while other code may assumes it is in the same "native" ``CoordinateReferenceSystem`` as
the data being worked on.

When working with older code examples, you may need to read the javadocs to determine another method to used
to define the ``CoordinateReferenceSystem`` for a returned Envelope.

* Using a ``FeatureSource`` without ``ReferencedEnvelope`` example::
  
    Envelope bounds = featureSource.getBounds();
    
    CoordinateReferenceSystem crs = featureSource.getSchema().getDefaultGeometry().getCoordinateSystem();

* Using a ``FeatureSource`` with ``ReferencedEnvelope``::
   
   ReferencedEnvelope bounds = (ReferencedEnvelope) featureSource.getBounds();
   
   CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();

ReferencedEnvelope3D
^^^^^^^^^^^^^^^^^^^^

GeoTools ``ReferencedEnvelope3D`` extends JTS ``Envelope`` to implement the ``gt-api`` module ``Bounds3D`` interface.

``ReferencedEnvelope3D`` is all of these:

* ``ReferencedEnvelope`` including all parent classes and interfaces
* ``org.geotools.api.geometry.BoundingBox3D`` - 3D bounds as defined by the ISO 19107 Geometry

This is the class to use when you want to represent a 3D bounds in GeoTools. The constructor expects the input in ``xMin,xMax,yMin,yMax,zMin,zMax`` order and expects a 3D CRS:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleReferencedEnvelope3D start
   :end-before: // exampleReferencedEnvelope3D end

ReferencedEnvelope utility methods
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

When using a 3D ``CoordinateReferenceSystem`` we must create an instance of ``ReferencedEnvelope3D`` and not of its parent ``ReferencedEnvelope`` class.

If we are not sure what dimension we are dealing with, there are safe ways to create, copy, convert or reference ``ReferencedEnvelope`` instances:

* ``create()`` methods: safely create a new ReferencedEnvelope instance (always makes a copy)
* ``rect()`` methods: safely create from a java.awt ``Rectangle``
* ``envelope()`` methods: safely create from a jts ``Envelope``
* ``reference()`` methods: safely "cast" an existing object to ``ReferencedEnvelope`` (only making a copy if needed)

Example use of ReferencedEnvelope utility methods:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleReferencedEnvelopeStaticMethods start
   :end-before: // exampleReferencedEnvelopeStaticMethods end
