CoordinateFilter
----------------

The CoordinateFilter interface provides a useful handle for implementing your own querying and modifying operations. The interface defines a single method::

   public void filter(Coordinate coord);


The following example shows a simple class to determine the bounds of a set of Geometry objects.::
  
  class BoundsFilter implements CoordinateFilter {
  
    double minx,  miny,  maxx,  maxy;
    boolean first = true;
  
    /**
     * First coordinate visited initializes the min and max fields.
     * Subsequent coordinates are compared to current bounds.
     */
    public void filter(Coordinate c) {
        if (first) {
            minx = maxx = c.x;
            miny = maxy = c.y;
            first = false;
        } else {
            minx = Math.min(minx, c.x);
            miny = Math.min(miny, c.y);
            maxx = Math.max(maxx, c.x);
            maxy = Math.max(maxy, c.y);
        }
    }
  
    /**
     * Return bounds as a Rectangle2D object
     */
    Rectangle2D getBounds() {
        return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny);
    }
  }

And here is the filter being applied to a list of Geometry objects.::
  
  List<Geometry> geoms = ...
  
  BoundsFilter bf = new BoundsFilter();
  for (Geometry g : geoms) {
      g.apply(bf);
  }
  
  Rectangle2D bounds = bf.getBounds();

The CoordinateFilter acts as a "visitor" allowing you to traverse all the coordinates in a Geometry.