Testing equality of Geometry objects
------------------------------------

JTS has a number of different equals methods for comparing Geometry objects. If you are working with
large, complex Geometry objects and doing a lot of comparisons, it's important to understand the
how the various methods differ in order to get the best runtime performance for your application.

.. Hint::
   If this page looks too long and scary, the important bit is don't ever
   use Geometry.equals( Geometry g ) in your code. Instead, use equalsExact or equalsTopo.

Geometry.equalsExact( Geometry g )
    This method tests for **structural equality** of Geometry objects. In simple terms, this means
    that they must have the same number of vertices, in the same locations, and in the same order.
    The latter condition is the tricky one.  For example, if two Polygons have matching vertices
    but in one they are arranged clockwise while in the other they are counter-clockwise, then they
    are not **structurally** equal. This situation can easily arise when objects are being stored
    in, and later retrieved from, data stores.

Geometry.equalsExact( Geometry g, double tolerance )
    This is just like the previous method but lets you specify a tolerance for the comparison of
    vertex coordinates.

Geometry.equalsNorm( Geometry g )
    This method frees you from the vertex order trap. It first *normalizes* the Geometry objects,
    ie. puts them into a standard or *canonical* form, and then compares them using the equalsExact
    method. In other words, it is a short-cut for::

      geomA.normalize();
      geomB.normalize();
      boolean result = geomA.equalsExact( geomB );

    For lineal and polygonal objects this means that the order of vertices will be the same. You pay
    for this in terms of additional computation which, for complex Geometry objects, can be
    considerable.

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

