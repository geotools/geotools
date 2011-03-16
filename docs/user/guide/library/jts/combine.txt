Combine Geometry
----------------

Doing the union operation on two geometries is one of the most expensive operations you can possibly do; while it is reasonable for small numbers (say 5-10) when you start to get up to hundreds of geometries the cost can be measured in minuets.

* Using GeometryCollection union()
  
  In JTS 1.9 there is a new union() method that will "do the right thing"::
    
    static Geometry combineIntoOneGeometry( Collection<Geometry> geometryCollection ){
         GeometryFactory factory = FactoryFinder.getGeometryFactory( null );
         
         // note the following geometry collection may be invalid (say with overlapping polygons)
         GeometryCollection geometryCollection =
              (GeometryCollection) factory.buildGeometry( geometryCollection );
         
         return geometryCollection.union();
     }

* Using buffer( 0 )
  
  You can get the same effect in JTS 1.8 using buffer(0)::
    
    GeometryFactory factory = FactoryFinder.getGeometryFactory( null );
    
    // note the following geometry collection may be invalid (say with overlapping polygons)
    GeometryCollection geometryCollection =
             (GeometryCollection) factory.buildGeometry( geometryCollection );
    
    Geometry union = geometryCollection.buffer(0);

* Using union( geometry )
  
  Using JTS versions prior to 1.9 you will need to combine the geometries one by one using geometry.union( geometry )::
    
        static Geometry combineIntoOneGeometry( Collection<Geometry> geometryCollection ){
            Geometry all = null;
            for( Iterator<Geometry> i = geometryCollection.iterator(); i.hasNext(); ){
            Geometry geometry = i.next();
            if( geometry == null ) continue;
            if( all == null ){
                all = geometry;
            }
            else {
                all = all.union( geometry );
            }
        }
        return all;
    }
  
  The above code is pretty much too simple to live; the correct way to do things is to break up your data into regions, union all the geometries in
  one region together; and then combine these at the end into one big geometry (this is the approach used above by the union() method).
