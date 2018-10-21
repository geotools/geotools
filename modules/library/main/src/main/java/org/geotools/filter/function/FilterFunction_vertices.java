package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.opengis.filter.capability.FunctionName;

public class FilterFunction_vertices extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "vertices",
                    parameter("vertices", MultiPoint.class),
                    parameter("geometry", Geometry.class));

    public FilterFunction_vertices() {
        super(NAME);
    }

    public Object evaluate(Object feature, Class context) {
        Geometry g = getExpression(0).evaluate(feature, Geometry.class);
        if (g == null) return null;

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
            Coordinate[] coorArray =
                    (Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]);
            return new GeometryFactory().createMultiPoint(coorArray);
        }
    }
}
