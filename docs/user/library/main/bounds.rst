Bounds
------

Bounds are used to represent the extent of a geometry, and are used very frequently as a way to quickly check if a geometry is in the area you are interested in.

.. image:: /images/envelopes_all.png

GeoTools, through virtue of reusing code, has seveal "Bounds" implementations to contend with:

* ``gt-api`` module ``Bounds`` and ``Bounds3D`` representing a spatial extent, complete with ``CoordinateReferenceSystem``.

  The Coverage API in ``gt-api`` package ``org.geotools.api.coverage`` makes use of ``Bounds`` and ``Bounds3D`` extensively.

* ``gt-api`` module ``BoundingBox`` and ``BoundingBox3D`` represent a spatial extent, assuming ``CoordinateReferenceSystem`` defines axis in ``X/Y`` order.

* ``gt-referencing`` implements ``AbstractBounds`` and ``GeneralBounds``, to the area of validity for a coordinate reference system.

* ``gt-main`` defines ``ReferencedEnvelope`` which is adds a ``CoordinateReferenceSystem`` to a JTS Topology Suite ``Envelope``. The class ``ReferencedEnvelope3D`` supports a third dimension on top of the regular two dimensions.

  DataStore API ``gt-main`` packages ``org.geotools.api.data`` makes use of ``ReferencedEnvelope`` and ``ReferencedEnvelope3D`` extensively (integrating well with use of JTS ``Geometry``).

You will find other "Rectangles" around as you make use of GeoTools in a real world application.

* Java ``Rectangles``
  
  Java ``Rectangles`` record x,y,w,h:

* ``Rectangle2D``
  
  * ``Rectangle2D.Double`` rectangle working with doubles
  * ``Rectangle2D.Float`` rectangle working with floats

* ``GeneralBounds`` we have a spatial specific version of ``Rectangle2D`` that implements ISO Geometry Envelope

* ``Rectangle`` the original ``java.awt`` rectangle for working on the screen, measured in integer pixels.

ReferencedEnvelope and ReferencedEnvelope3D
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

GeoTools ``ReferencedEnvelope`` extends JTS ``Envelope`` to implement the ``gt-api`` module ``Bounds`` interface,
and subclass ``ReferencedEnvelope3D`` implementing ``Bounds3D``.

.. image:: /images/envelope.png

Use of ``ReferencedEnvelope`` is the most common representation of a bounds in GeoTools:

.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleReferencedEnvelope start
   :end-before: // exampleReferencedEnvelope end


Bounds and GeneralBounds
^^^^^^^^^^^^^^^^^^^^^^^^

The ``gt-api`` module records ``Bounds`` as an extent measured along each axis mentioned by the ``CoordinateReferenceSystem`` object. You can use this approach to record a simple rectangle in space, a height range,  and a range in time as needed.

The ``GeneralBounds`` implementation records spans in multiple dimensions (think depth, height or time).

.. image:: /images/general_bounds.png

Since``Bounds`` is an interface we will work with GeneralBounds in this example:
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleGeneralBounds start
   :end-before: // exampleGeneralBounds end

Even in a simple example we should consult the CRS to define what each axis is measuring (the variables showing xMin and yMin hold unexpected values if the ``CoordinateReferencSystem`` defines Axis ``0`` as ``NORTHING``)

BoundingBox and BoundingBox3D
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you are confident that you are working with data in X/Y order you can directly make use of ``BoundingBox`` box. ``BoundingBox`` is an extension of ``Envelope`` for working with 2D data, and it has been made method compatible with JTS ``Envelope`` where possible.

Since ``BoundingBox`` is just an interface, so we will use ``ReferencedEnvelope`` for this example:
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleBoundingBox start
   :end-before: // exampleBoundingBox end

JTS Envelope
^^^^^^^^^^^^

The JTS Topology Suite has the concept of an ``Envelope`` recorded in ``x1,x2, y1,y2`` order.

You can see that the use of JTS ``Envelope`` has the same "assumptions" as the use of ``BoundingBox`` above.:
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // exampleEnvelope start
   :end-before: // exampleEnvelope end

Transform an ``Envelope`` using the JTS ``Utility`` class:
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :dedent: 8
   :start-after: // transformEnvelope start
   :end-before: // transformEnvelope end
