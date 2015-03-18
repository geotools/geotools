package mil.nga.giat.data.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.geotools.geojson.geom.GeometryJSON;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class RandomGeometryBuilder {

    private final GeometryFactory geometryFactory;

    private final Random random;
    
    private final GeometryJSON geometryJson;
    
    private final int decimals;
        
    private int numPoints;
    
    private int numGeometries;
    
    public RandomGeometryBuilder() {
        geometryFactory = new GeometryFactory();
        random = new Random(123456789l);
        decimals = 4;
        numPoints = 10;
        numGeometries = 2;
        geometryJson = new GeometryJSON(decimals);
   }

    public Point createRandomPoint() {
        return geometryFactory.createPoint(createRandomCoord());
    }

    public LineString createRandomLineString() {
        Coordinate[] coords = new Coordinate[numPoints];
        for (int i=0; i<numPoints; i++) {
            coords[i] = createRandomCoord();
        }
        return geometryFactory.createLineString(coords);
    }

    public Polygon createRandomPolygon() {
        Coordinate[] coords = new Coordinate[numPoints+1];
        for (int i=0; i<numPoints; i++) {
            coords[i] = createRandomCoord();
        }
        coords[numPoints] = coords[0];
        return geometryFactory.createPolygon(coords);
    }

    public MultiPoint createRandomMultiPoint() {
        Point[] points = new Point[numGeometries];
        for (int i=0; i<numGeometries; i++) {
            points[i] = createRandomPoint();
        }
        return geometryFactory.createMultiPoint(points);
    }

    public MultiLineString createRandomMultiLineString() {
        LineString[] lineStrings = new LineString[numGeometries];
        for (int i=0; i<numGeometries; i++) {
            lineStrings[i] = createRandomLineString();
        }
        return geometryFactory.createMultiLineString(lineStrings);
    }

    public MultiPolygon createRandomMultiPolygon() {
        Polygon[] polygons = new Polygon[numGeometries];
        for (int i=0; i<numGeometries; i++) {
            polygons[i] = createRandomPolygon();
        }
        return geometryFactory.createMultiPolygon(polygons);
    }

    public GeometryCollection createRandomGeometryCollection() {
        Geometry[] geometries = new Geometry[numGeometries];
        for (int i=0; i<numGeometries; i++) {
            switch (random.nextInt(3)) {
            case 0:
                geometries[i] = createRandomPoint();
                break;
            case 1:
                geometries[i] = createRandomLineString();
                break;
            default:
                geometries[i] = createRandomPolygon();
            }
        }
        return geometryFactory.createGeometryCollection(geometries);
    }
    
    public Envelope createRandomEnvelope() {
        Coordinate coord1 = createRandomCoord();
        while (coord1.x > 179 || coord1.y > 89) {
            coord1 = createRandomCoord();
        }
        final Coordinate coord2 = createRandomCoord((int) (coord1.x+0.5), 180, (int) (coord1.y+0.5), 90);
        return new Envelope(coord1, coord2);
    }

    public Coordinate createRandomCoord() {
        return createRandomCoord(-180, 180, -90, 90);
    }

    public Coordinate createRandomCoord(int minx, int maxx, int miny, int maxy) {
        int dx = maxx-minx;
        int dy = maxy-miny;
        final int factor = (int) Math.pow(10, decimals);
        final double lon = (random.nextInt(dx*factor)+minx*factor)/((double) factor);
        final double lat = (random.nextInt(dy*factor)+miny*factor)/((double) factor);
        return new Coordinate(lon, lat);
    }
    
    public Map<String,Object> toMap(Geometry geometry) throws JsonParseException, JsonMappingException, IOException {
        final String json = geometryJson.toString(geometry);
        return new ObjectMapper().readValue(json, HashMap.class);
    }
    
    public Map<String,Object> toMap(Envelope envelope) throws JsonParseException, JsonMappingException, IOException {
        final Map<String,Object> properties = new HashMap<>();
        final List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(Arrays.asList(new Double[] {envelope.getMinX(), envelope.getMaxY()}));
        coordinates.add(Arrays.asList(new Double[] {envelope.getMaxX(), envelope.getMinY()}));
        properties.put("type", "envelope");
        properties.put("coordinates", coordinates);
        return properties;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public int getNumGeometries() {
        return numGeometries;
    }

    public void setNumGeometries(int numGeometries) {
        this.numGeometries = numGeometries;
    }
    
}
