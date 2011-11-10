package org.geotools.geometry;

import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryExamples {

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
