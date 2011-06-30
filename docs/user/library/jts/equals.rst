Testing equality of Geometry objects
------------------------------------

JTS has a number of different equals methods for comparing Geometry objects. If you are doing a lot
of comparisons with large, complex Geometry objects it's important to understand the differences
between these methods to get the best runtime performance in your application.

.. Hint::
   If this page looks long and scary, the important bit is try never to use 
   Geometry.equals( Geometry g ) in your code, but use equalsExact or equalsTopo instead.

Geometry.equalsExact( Geometry g )
    This method tests for **structural equality** of Geometry objects. In simple terms, this means
    that they must have the same number of vertices, in the same locations, and in the same order.
    The latter condition is the tricky one.  If two Polygons have matching vertices, but one is
    arranged clockwise while the other is counter-clockwise, then then this method will return
    **false**. It's important to know this because vertex order can change when objects are being
    stored in, and later retrieved from, data stores.

Geometry.equalsExact( Geometry g, double tolerance )
    This is just like the previous method but lets you specify a tolerance for the comparison of
    vertex coordinates.

Geometry.equalsNorm( Geometry g )
    This method frees you from the vertex order problem mentioned above by *normalizing* the
    Geometry objects (ie. putting each into a standard or *canonical* form), before comparison. It
    is equivalent to::

      geomA.normalize();
      geomB.normalize();
      boolean result = geomA.equalsExact( geomB );

    Vertex order will is guaranteed to be the same, but the price is additional computation which,
    for complex Geometry objects, can be expensive.

Geometry.equalsTopo( Geometry g )
    This method tests for **topological equality** which is equivalent to drawing the two Geometry
    objects and seeing if all of their component edges overlap. It is the most robust kind of
    comparison but also the most computationally expensive.

Geometry.equals( Object o )
    This method is a synonym for Geometry.equalsExact and lets you use Geometry objects in
    Java Collections.

Geometry.equals( Geometry g )
    This method is a synonym for Geometry.equalsTopo. It should really come with a health warning
    because its presence means that you can unknowingly be doing computationally expensive
    comparisons when quick cheap ones are all you need.  For example::

      Geometry geomA = ...
      Geometry geomB = ...

      // If geomA and geomB are complex, this will be slow:
      boolean result = geomA.equals( geomB );

      // If you know that a structural comparison is all you need, do 
      // this instead:
      result = geomA.equalsExact( geomB );

    The best thing approach you can take with this method is vow never to use it.

