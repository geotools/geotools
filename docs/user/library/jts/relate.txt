Geometry Relationships
----------------------

One of the major points of having Geometry represented as an object is to be able to establish relationships to answer questions like "Are two geometries touching each other?"
[]
Equals
^^^^^^

You can use JTS to check if two objects are equal::
    
    GeometryFactory geometryFactory = FactoryFinder.getGeometryFactory( null );
    
    WKTReader reader = new WKTReader( geometryFactory );
    LineString geometry = (LineString) reader.read("LINESTRING(0 0, 2 0, 5 0)");
    LineString geometry = (LineString) reader.read("LINESTRING(5 0, 0 0)");
        
    return geometry.equals( (Geometry) geometry2 );

Please note that equals is not a quick easy check as you might expect - but a real full on spatial comparison of what the data structures mean. The above code example will return true as the two line strings define exactly the same shape.

Force yourself to learn the habit of putting that (Geometry) cast in there; only habit can avoid the mistake of getting confused with Object equals.

This method will fail if called on an invalid geometry.

* Common Mistake - Getting confused with Object Equals
  
  Here is an example of this common mistake::
    
    return geometry.equals( other ); // will use Object.equals( obj ) which is the same as the == operator
    
  Here is the correction::
    
    return geometry.equals( (Geometry) other );

* Equals Exact Relationship
  
  You can check if two geometries are exactly equal; right down to the coordinate level::
    
    return geometry.equalsExact( (Geometry) geometry2 );

  This method is faster than equals( geometry ) and is closer to what normal Java programs assume for a data
  object equals method implementation. We are checking the internal structure; rather than the meaning.

  The equalsExact method is able to function on invalid geometries.

* Alternative - Identity Operator
  
  Normal Java identity operator also has its place; don't forget about it::
    
    return geometry == geometry2;

Disjoint
^^^^^^^^

The geometries have no points in common.::
    
    return geometry.disjoint( geometry2 );

Intersects
^^^^^^^^^^

The geometries have at least one point in common.::
    
    return geometry.intersects( geometry2 );

This will test if any point on the boundary or within a geometry is
part of the boundary or within a second geometry.

* Alternative - not disjoint
  
  This is the opposite of disjoint::
    
    return !geometryA.disjoint( geometry2 );

Touches
^^^^^^^

The geometries only touch edges and do not overlap in any way::
    
    return geometryA.touches( geometry2 );

Crosses
^^^^^^^^

The geometries do more than touch, they actually overlap edges:

    return geometryA.crosses( geometry2 );

Within
^^^^^^

One geometry is completely within another (no touching edges)::

    return geometryA.within( geometry2 );

Contains
^^^^^^^^

One geometry contains another::

    return geometryA.contains( geometry2 );

Overlaps
^^^^^^^^

The geometries have some points in common; but not all points in common (so if one geometry is inside the other overlaps would be false). The overlapping section must be the same kind of shape as the two geometries; so two polygons that touch on a point are not considered to be overlapping.::

    return geometryA.overlaps( geometry2 );

The definition of the overlaps relationship is a little bit different than that used in common English (normally you would assume that a geometry contained inside another geometry is "overlapping"; to test for that situation use Intersects)

Relates
^^^^^^^

Computes the "DE-9IM Matrix" for two Geometies allowing you to study exactly how they interact with each other.::
   
   IntersectionMatrix m = a.relate(b);

The IntersectionMatrix allows you to separately test how the interior, exterior and edges of two geometries interact. All the above operations can be viewed as a summary of this IntersectionMatrix.
