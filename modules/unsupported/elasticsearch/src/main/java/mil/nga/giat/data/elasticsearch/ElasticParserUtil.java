/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.util.logging.Logging;

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.awt.geom.Point2D;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.datum.DefaultEllipsoid;

/**
 * Utilities for parsing Elasticsearch document source and field content to
 * extract values and create geometries.
 *
 */
public class ElasticParserUtil {

    private final static Logger LOGGER = Logging.getLogger(ElasticParserUtil.class);

    private static final Pattern GEO_POINT_PATTERN;

    static {
        GEO_POINT_PATTERN = Pattern.compile("\\s*([-+]?\\d*\\.?\\d*)[^-+\\d\\.]+([-+]?\\d*\\.?\\d*)\\s*");
    }

    private static final Pattern GEO_HASH_PATTERN;

    static {
        GEO_HASH_PATTERN = Pattern.compile("[0123456789bcdefghjkmnpqrstuvwxyz]+");
    }

    private static final Pattern ELASTIC_DISTANCE_PATTERN;

    static {
        ELASTIC_DISTANCE_PATTERN = Pattern.compile("([0-9]+(\\.[0-9]+)?)([a-zA-Z]*)");
    }

    private static final double CIRCLE_INTERPOLATION_INTERVAL = 500.0;
    private static final int MAX_CIRCLE_POINTS = 500;
    private static final int MIN_CIRCLE_POINTS = 40;
    private static final double MIN_CIRCLE_RADIUS_M = 0.001;

    private final GeodeticCalculator geodeticCalculator;

    private static final Pattern WKT_PATTERN;
    static {
        WKT_PATTERN = Pattern.compile("POINT.*|LINESTRING.*|POLYGON.*|MULTIPOINT.*|MULTILINESTRING.*|MULTIPOLYGON.*|GEOMETRYCOLLECTION.*");
    }

    private final GeometryFactory geometryFactory;

    private final WKTReader wktReader;

    private boolean unsupportedEncodingMessage;

    public ElasticParserUtil() {
        this.geometryFactory = new GeometryFactory();
        this.unsupportedEncodingMessage = false;
        this.geodeticCalculator = new GeodeticCalculator(DefaultEllipsoid.WGS84);
        this.wktReader = new WKTReader();
    }

    /**
     * Create point geometry given geo_point or geo_shape definition. GeoPoint
     * can be defined by string, geohash, coordinate array or properties map.
     * GeoShape is defined by properties map.
     *
     * @param obj GeoPoint or GeoShape definition
     * @return Geometry
     */
    @SuppressWarnings("unchecked")
    public Geometry createGeometry(Object obj) {
        final Geometry geometry;
        if (obj instanceof String) {
            // geo_point by string
            final Matcher listMatcher = GEO_POINT_PATTERN.matcher((String) obj);
            if (listMatcher.matches()) {
                // coordinate
                final double y = Double.valueOf(listMatcher.group(1));
                final double x = Double.valueOf(listMatcher.group(2));
                geometry = geometryFactory.createPoint(new Coordinate(x, y));
            } else if (GEO_HASH_PATTERN.matcher((String) obj).matches()) {
                // geohash
                final LatLong latLon = GeoHash.decodeHash((String) obj);
                final Coordinate geoPoint = new Coordinate(latLon.getLon(), latLon.getLat());
                final double lat = geoPoint.y;
                final double lon = geoPoint.x;
                geometry = geometryFactory.createPoint(new Coordinate(lon, lat));
            } else if (WKT_PATTERN.matcher((String) obj).matches()) {
                // geoshape wkt
                Geometry geom;
                try {
                    geom = wktReader.read((String) obj);
                } catch (ParseException e) {
                    geom = null;
                }
                geometry = geom;
            } else {
                geometry = null;
            }
        } else if (obj instanceof List && ((List<?>) obj).size() == 2) {
            // geo_point by coordinate array
            final List<?> values = (List<?>) obj;
            if (Number.class.isAssignableFrom(values.get(0).getClass())) {
                final double x = ((Number) values.get(0)).doubleValue();
                final double y = ((Number) values.get(1)).doubleValue();
                geometry = geometryFactory.createPoint(new Coordinate(x, y));
            } else if (values.get(0) instanceof String) {
                final double x = Double.valueOf((String) values.get(0));
                final double y = Double.valueOf((String) values.get(1));
                geometry = geometryFactory.createPoint(new Coordinate(x, y));
            } else {
                geometry = null;
            }
        } else if (obj instanceof Map) {
            // geo_shape or geo_point by properties
            geometry = createGeometry((Map<String, Object>) obj);
        } else {
            geometry = null;
        }
        return geometry;
    }

    /**
     * Create geometry given property map defining geo_shape type and
     * coordinates or geo_point lat and lon.
     *
     * @param properties Properties
     * @return Geometry
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Geometry createGeometry(final Map<String, Object> properties) {
        final Geometry geometry;
        switch (String.valueOf(properties.get("type")).toUpperCase()) {
        case "POINT": {
            final List posList;
            posList = (List) properties.get("coordinates");
            final Coordinate coordinate = createCoordinate(posList);
            geometry = geometryFactory.createPoint(coordinate);
            break;
        }
        case "LINESTRING": {
            final List<List<Object>> posList;
            posList = (List) properties.get("coordinates");
            final Coordinate[] coordinates = createCoordinates(posList);
            geometry = geometryFactory.createLineString(coordinates);
            break;
        }
        case "POLYGON": {
            final List<List<List<Object>>> posList;
            posList = (List) properties.get("coordinates");
            geometry = createPolygon(posList);
            break;
        }
        case "MULTIPOINT": {
            final List<List<Object>> posList;
            posList = (List) properties.get("coordinates");
            final Coordinate[] coordinates = createCoordinates(posList);
            geometry = geometryFactory.createMultiPoint(coordinates);
            break;
        }
        case "MULTILINESTRING": {
            final List<List<List<Object>>> posList;
            posList = (List) properties.get("coordinates");
            final LineString[] lineStrings = new LineString[posList.size()];
            for (int i = 0; i < posList.size(); i++) {
                final Coordinate[] coordinates = createCoordinates(posList.get(i));
                lineStrings[i] = geometryFactory.createLineString(coordinates);
            }
            geometry = geometryFactory.createMultiLineString(lineStrings);
            break;
        }
        case "MULTIPOLYGON": {
            final List<List<List<List<Object>>>> posList;
            posList = (List) properties.get("coordinates");
            final Polygon[] polygons = new Polygon[posList.size()];
            for (int i = 0; i < posList.size(); i++) {
                polygons[i] = createPolygon(posList.get(i));
            }
            geometry = geometryFactory.createMultiPolygon(polygons);
            break;
        }
        case "GEOMETRYCOLLECTION": {
            final List<Map<String, Object>> list;
            list = (List) properties.get("geometries");
            final Geometry[] geometries = new Geometry[list.size()];
            for (int i = 0; i < geometries.length; i++) {
                geometries[i] = createGeometry(list.get(i));
            }
            geometry = geometryFactory.createGeometryCollection(geometries);
            break;
        }
        case "ENVELOPE": {
            final List<List<Object>> posList;
            posList = (List) properties.get("coordinates");
            final Coordinate[] coords = createCoordinates(posList);
            final Envelope envelope = new Envelope(coords[0], coords[1]);
            geometry = geometryFactory.toGeometry(envelope);
            break;
        }
        case "CIRCLE": {
            final List posList;
            posList = (List) properties.get("coordinates");
            final String radius = (String) properties.get("radius");
            final Coordinate coordinate = createCoordinate(posList);
            geometry = createCircle(coordinate, radius);
            break;
        }
        default:
            // check if this is a geo_point
            final Object latObj = properties.get("lat");
            final Object lonObj = properties.get("lon");
            if (latObj != null && lonObj != null) {
                final Double lat;
                if (latObj instanceof Number) {
                    lat = ((Number) latObj).doubleValue();
                } else if (latObj instanceof String) {
                    lat = new Double((String) latObj);
                } else {
                    lat = null;
                }

                final Double lon;
                if (lonObj instanceof Number) {
                    lon = ((Number) lonObj).doubleValue();
                } else if (lonObj instanceof String) {
                    lon = new Double((String) lonObj);
                } else {
                    lon = null;
                }

                if (lat != null && lon != null) {
                    geometry = geometryFactory.createPoint(new Coordinate(lon, lat));
                } else {
                    geometry = null;
                }
            } else {
                geometry = null;
            }
            break;
        }
        return geometry;
    }

    private Polygon createPolygon(final List<List<List<Object>>> posList) {
        final Coordinate[] shellCoordinates = createCoordinates(posList.get(0));
        final LinearRing shell = geometryFactory.createLinearRing(shellCoordinates);
        final LinearRing[] holes = new LinearRing[posList.size() - 1];
        for (int i = 1; i < posList.size(); i++) {
            final Coordinate[] coordinates = createCoordinates(posList.get(i));
            holes[i - 1] = geometryFactory.createLinearRing(coordinates);
        }
        return geometryFactory.createPolygon(shell, holes);
    }

    private Coordinate[] createCoordinates(final List<List<Object>> posList) {
        final Coordinate[] coordinates = new Coordinate[posList.size()];
        for (int i = 0; i < posList.size(); i++) {
            coordinates[i] = createCoordinate(posList.get(i));
        }
        return coordinates;
    }

    private Coordinate createCoordinate(final List<Object> posList) {
        final double x;
        final double y;
        if (Number.class.isAssignableFrom(posList.get(0).getClass())) {
            x = ((Number) posList.get(0)).doubleValue();
            y = ((Number) posList.get(1)).doubleValue();
        } else {
            x = Double.valueOf(posList.get(0).toString());
            y = Double.valueOf(posList.get(1).toString());
        }
        return new Coordinate(x, y);
    }

    /**
     * Read field from document source.
     *
     * @param source Source
     * @param name Field to extract.
     * @return List of values or empty list if not found
     */
    public List<Object> readField(Map<String, Object> source, String name) {
        final List<String> keys = Arrays.asList(name.split("\\."));
        List<Object> values = new ArrayList<>();
        if (!keys.isEmpty()) {

            final Object entry = source.get(keys.get(0));

            if (entry == null) {
                readField(source.get(name), Collections.EMPTY_LIST, values);;
            } else {
                readField(entry, keys.subList(1, keys.size()), values);
            }

        }
        final List<Object> result;
        if (!values.isEmpty()) {
            result = values;
        } else {
            result = null;
        }
        return result;
    }

    private void readField(Object entry, List<String> keys, List<Object> values) {
        if (entry == null) {
        } else if (List.class.isAssignableFrom(entry.getClass())) {
            for (Object object : (List<?>) entry) {
                readField(object, keys, values);
            }
        } else if (!keys.isEmpty() && Map.class.isAssignableFrom(entry.getClass())) {
            final Object nextEntry = ((Map<?, ?>) entry).get(keys.get(0));
            final List<String> newKeys = keys.subList(1, keys.size());
            readField(nextEntry, newKeys, values);
        } else if (entry != null) {
            values.add(entry);
        }
    }

    @SuppressWarnings("rawtypes")
    public static boolean isGeoPointFeature(Map<String, Object> map) {
        boolean result = false;
        if (map.size() == 2 && map.containsKey("coordinates")) {
            try {
                result = "geo_point".equals(((Map) map.get("coordinates")).get("type"));
            } catch (Exception e) {
            }
        }
        return result;
    }

    public String urlEncode(String value) {
        try {
            value = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            if (!unsupportedEncodingMessage) {
                LOGGER.warning("Unable to encode value(s): " + e);
                unsupportedEncodingMessage = true;
            }
        }
        return value;
    }

    public static String urlDecode(String value) {
        try {
            value = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            LOGGER.warning("Unable to encode value(s): " + e);
        }
        return value;
    }

    /**
     * Interpolates a JTS polygon from a circle definition. Assumes WGS84 CRS.
     *
     * @param centreCoord The centre of the circle
     * @param radius Consists of a numeric value with a units string appended to
     * it.
     * @return A polygon that is an interpolated form of a circle
     */
    private Geometry createCircle(Coordinate centreCoord, String radius) {

        if (centreCoord == null) {
            return null;
        }

        final double radM;
        try {
            radM = convertToMeters(radius);
        }
        catch(Exception e) {
            return null;
        }

        // Reject circles with radii below an arbitrary minimum.
        if (radM < MIN_CIRCLE_RADIUS_M) {
            return null;
        }

        // Interpolate a circle on the surface of the ellipsoid at an arbitrary
        // interval and then ensure that the number of interpolated points are
        // within a specified range
        final double circumferance = radM * 2.0 * Math.PI;
        int numPoints = (int) (circumferance / CIRCLE_INTERPOLATION_INTERVAL);
        numPoints = Math.max(MIN_CIRCLE_POINTS, numPoints);
        numPoints = Math.min(MAX_CIRCLE_POINTS, numPoints);
        final double angularIncrement = 360.0 / numPoints;
        geodeticCalculator.setStartingGeographicPoint(centreCoord.x, centreCoord.y);
        final Coordinate[] linearRingCoords = new Coordinate[numPoints + 1];
        double angle = 0.0;
        for (int i = 0; i < numPoints; i++) {
            geodeticCalculator.setDirection(angle, radM);
            Point2D point2D = geodeticCalculator.getDestinationGeographicPoint();
            linearRingCoords[i] = new Coordinate(point2D.getX(), point2D.getY());
            angle += angularIncrement;
        }
        linearRingCoords[numPoints] = linearRingCoords[0];
        final LinearRing linearRing = geometryFactory.createLinearRing(linearRingCoords);
        return geometryFactory.createPolygon(linearRing);
    }

    /**
     * Converts an Elasticsearch distance string consisting of value and unit
     * into metres.
     * @param distanceWithUnit String of the form of a decimal number
     * concatenated with a unit string as defined in
     * {@link FilterToElasticHelper#UNITS_MAP}. If the unit string is missing
     * then the number is assumed to be metres.
     * @return distance in metres.
     * @throws IllegalArgumentException
     */
    static final double convertToMeters(String distanceWithUnit) throws IllegalArgumentException {
        if (distanceWithUnit == null || distanceWithUnit.isEmpty()) {
            throw new IllegalArgumentException("Null of zero length distance string argument");
        }
        final Matcher matcher = ELASTIC_DISTANCE_PATTERN.matcher(distanceWithUnit);
        if (matcher.matches()) {
            final double distance = Double.valueOf(matcher.group(1));
            final String unit = matcher.group(3);
            Double conversion = FilterToElasticHelper.UNITS_MAP.get(unit);
            if (conversion == null) {
                if (unit != null && ! unit.isEmpty()) {
                    throw new IllegalArgumentException("Illegal unit: " + unit);
                } else {
                    conversion = new Double(1.0);
                }
            }
            return distance * conversion;
        } else {
            throw new IllegalArgumentException("Distance string argument has incorrect format");
        }
    }
}
