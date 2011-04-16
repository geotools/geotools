Operations
----------

References:

* :doc:`/library/jts/dim9`

After creating geometry objects, useful operations can be run on them. For instance, the geometry can be transformed to another coordinate reference system, two geometries can be unioned together, or two geometries can be tested for intersections.

The following code shows some examples of performing operations on geometry objects.

* Spatial relationships::
    
    boolean intersect = surface.intersects(point);

* Spatial operations::
    
    Geometry geom = (Geometry) curve.union(surface);
    int distance = point.distance(surface);

* Transofrmation
    
    // perform a transform to another CRS
    CoordinateReferenceSystem crs2 = CRS.decode("EPSG:3005");
    Point point2 = (Point) point.transform(crs2);

The following is a quick list of some of the more interesting operations and methods available for geometries.

.. note::
   
   TransfiniteSet
   
   For the purpose of reading the following methods TransfiniteSet is a
   superclass of Geometry.

Geometry Methods:

* Geometry.contains(TransfiniteSet)
* Geometry.crosses(TransfiniteSet)
* Geometry.difference(TransfiniteSet)
* Geometry.disjoint(TransfiniteSet)
* Geometry.distance(Geometry)
* Geometry.equals(TransfiniteSet)
* Geometry.getBoundary()
* Geometry.getCentroid()
* Geometry.getClosure()
* Geometry.getConvexHull()
* Geometry.getCoordinateDimension()
* Geometry.getCoordinateReferenceSystem()
* Geometry.getDimension(DirectPosition)
* Geometry.getEnvelope()
* Geometry.getRepresentativePoint()
* Geometry.intersection(TransfiniteSet)
* Geometry.intersects(TransfiniteSet)
* Geometry.isCycle()
* Geometry.overlaps(TransfiniteSet)
* Geometry.relate(Geometry, String)
* Geometry.symmetricDifference(TransfiniteSet)
* Geometry.touches(TransfiniteSet)
* Geometry.transform(CoordinateReferenceSystem)
* Geometry.transform(CoordinateReferenceSystem, MathTransform)
* Geometry.union(TransfiniteSet)
* Geometry.within(TransfiniteSet)
