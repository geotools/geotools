package org.geotools.data.sqlserver;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

class GeometryDimensionFinder implements GeometryComponentFilter {
    
    boolean z = false;

    @Override
    public void filter(Geometry geom) {
        CoordinateSequence cs = null;
        if(geom instanceof Point) {
            cs = ((Point) geom).getCoordinateSequence();
        } else if(geom instanceof LineString) {
            cs = ((LineString) geom).getCoordinateSequence();
        }
        
        if(cs != null) {
            if(cs instanceof CoordinateArraySequence) {
                CoordinateArraySequence cas = (CoordinateArraySequence) cs;
                if(!Double.isNaN(cas.getCoordinate(0).z)) {
                    z = true;
                }
            } else {
                z |= cs.getDimension() > 2;
            }
        }
        
    }

    public boolean hasZ() {
        return z;
    }
    
    

}
