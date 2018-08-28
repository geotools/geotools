package org.geotools.geometry;

import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeometryExamples {

    public void createCurve() {
        // createCurve start
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        CurvedGeometryFactory curvedFactory =
                new CurvedGeometryFactory(geometryFactory, Double.MAX_VALUE);

        CoordinateSequence coords =
                PackedCoordinateSequenceFactory.DOUBLE_FACTORY.create(
                        new double[] {10, 14, 6, 10, 14, 10}, 2);

        CircularString arc = (CircularString) curvedFactory.createCurvedGeometry(coords);
        // createCurve end
    }

    public void wktCurve() throws Exception {
        // wktCurve start
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        CurvedGeometryFactory curvedfactory = new CurvedGeometryFactory(Double.MAX_VALUE);

        WKTReader2 reader = new WKTReader2(curvedfactory);
        CircularString arc = (CircularString) reader.read("CIRCULARSTRING(10 14,6 10,14 10)");
        // wktCurve end
    }

    public void createPoint() {
        // createPoint start
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        Coordinate coord = new Coordinate(1, 1);
        Point point = geometryFactory.createPoint(coord);
        // createPoint end
    }

    public void createPointWKT() throws ParseException {
        // createPointWKT start
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        WKTReader reader = new WKTReader(geometryFactory);
        Point point = (Point) reader.read("POINT (1 1)");
        // createPointWKT end
    }

    // geometries start
    public boolean hit(Point point, Geometry geometry) {
        final double MAX_DISTANCE = 0.001;

        switch (Geometries.get(geometry)) {
            case POINT:
            case MULTIPOINT:
            case LINESTRING:
            case MULTILINESTRING:
                // Test if p is within a threshold distance
                return geometry.isWithinDistance(point, MAX_DISTANCE);

            case POLYGON:
            case MULTIPOLYGON:
                // Test if the polygonal geometry contains p
                return geometry.contains(point);

            default:
                // For simplicity we just assume distance check will work for other
                // types (e.g. GeometryCollection) in this example
                return geometry.isWithinDistance(point, MAX_DISTANCE);
        }
    }
    // geometries end

}
