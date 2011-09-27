package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;

/**
 * 
 *
 * @source $URL$
 */
public class FilterFunction_vertices extends FunctionExpressionImpl {

    //public static FunctionName NAME = new FunctionNameImpl("vertices","geometry");
    public static FunctionName NAME = new FunctionNameImpl("vertices",
            parameter("vertices", MultiPoint.class),
            parameter("geometry", Geometry.class));

    public FilterFunction_vertices() {
        super(NAME);
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
