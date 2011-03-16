Geometry Operations
-------------------

JTS Geometry operations are used to perform a range of spatial calculations; from finding an intersection to determining the centriod.

* Buffer
* Intersection
* ConvexHull
* Intersection
* Union
* Difference
* SymDifference

The JTS Geometry operations closely follow the simple features for sql specification; so if you have any questions on exactly what is going on please review the related specification.

Buffer
^^^^^^

Creates a polygon or multi polygon containing all points within a within a set distance:
distance of the Geometry::
   
   Geometry buffer = geometry.buffer( 2.0 ); // note distance is in same units as geoemtry

Please keep in mind that the buffer is defined using the same distance units as used for your coordinates and is calculated in 2D only. You may wish to transform your geometry, buffer, and then transform the result back when working in real world units such as DefaultGeographicCRS.WGS84.

Intersection
^^^^^^^^^^^^

Provides the common shape between two geometries::
    
   Geometry intersection = polygon.intersection( line );
