package org.geotools.filter.function;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.FunctionExpressionImpl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;

public class FilterFunction_vertices extends FunctionExpressionImpl {

    public FilterFunction_vertices() {
        super("vertices");
    }

    @Override
    public int getArgCount() {
        return 1;
    }

    public Object evaluate(Object feature, Class context) {
        Geometry g = getExpression(0).evaluate(feature, Geometry.class);
        if(g == null)
            return null;
        
        MultiPointExtractor filter = new MultiPointExtractor();
        g.apply(filter);
        return filter.getMultiPoint();
    }
    
    static class MultiPointExtractor implements CoordinateFilter {
        List<Coordinate> coordinates = new ArrayList();

        public void filter(Coordinate c) {
            coordinates.add(c);
        }
        
        MultiPoint getMultiPoint() {
            Coordinate[] coorArray = (Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]);
            return new GeometryFactory().createMultiPoint(coorArray);
        }
        
    }

}
