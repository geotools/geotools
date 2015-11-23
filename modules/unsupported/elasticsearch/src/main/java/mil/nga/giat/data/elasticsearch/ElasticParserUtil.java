/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.ElasticsearchIllegalArgumentException;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.geo.GeoPoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Utilities for parsing Elasticsearch document source and field content to 
 * extract values and create geometries.
 *
 */
public class ElasticParserUtil {

    private static final Pattern GEO_POINT_PATTERN;
    static {
        GEO_POINT_PATTERN = Pattern.compile("(-*\\d*\\.*\\d*?),(-*\\d*\\.*\\d*?)");
    }

    private final GeometryFactory geometryFactory;

    public ElasticParserUtil() {
        this.geometryFactory = new GeometryFactory();
    }

    /**
     * Create point geometry given geo_point or geo_shape definition. GeoPoint
     * can be defined by string, geohash, coordinate array or properties map.
     * GeoShape is defined by properties map.
     * @param obj GeoPoint or GeoShape definition
     * @return Geometry
     */
    public Geometry createGeometry(Object obj) {
        final Geometry geometry;
        if (obj instanceof String) {
            // geo_point by string
            final Matcher listMatcher = GEO_POINT_PATTERN.matcher((String) obj);
            if (listMatcher.matches()) {
                // coordinates
                final double y = Double.valueOf(listMatcher.group(1));
                final double x = Double.valueOf(listMatcher.group(2));
                geometry = geometryFactory.createPoint(new Coordinate(x,y));
            } else {
                // try geohash
                GeoPoint geoPoint = null;
                try {
                    geoPoint = GeoHashUtils.decode((String) obj);
                } catch (ElasticsearchIllegalArgumentException e) {
                    // not a geohash
                }
                if (geoPoint != null) {
                    final double lat = geoPoint.lat();
                    final double lon = geoPoint.lon();
                    geometry = geometryFactory.createPoint(new Coordinate(lon,lat));
                } else {
                    geometry = null;
                }
            }
        } else if (obj instanceof List && ((List<?>) obj).size()==2) {
            // geo_point by coordinate array
            final List<?> values = (List<?>) obj;
            if (Number.class.isAssignableFrom(values.get(0).getClass())) {
                final double x = ((Number) values.get(0)).doubleValue();
                final double y = ((Number) values.get(1)).doubleValue();
                geometry = geometryFactory.createPoint(new Coordinate(x,y));
            } else if (values.get(0) instanceof String) {
                final double x = Double.valueOf((String) values.get(0));
                final double y = Double.valueOf((String) values.get(1));
                geometry = geometryFactory.createPoint(new Coordinate(x,y));
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
     * Create geometry given property map defining geo_shape type and coordinates
     * or geo_point lat and lon.
     * @param properties Properties
     * @return Geometry
     */
    public Geometry createGeometry(final Map<String, Object> properties) {
        final Geometry geometry;
        switch (String.valueOf(properties.get("type")).toUpperCase()) {
        case "POINT": {
            final List posList;
            posList = (List) properties.get("coordinates");
            final Coordinate coordinate = createCoordinate(posList);
            geometry = geometryFactory.createPoint(coordinate);
            break;
        } case "LINESTRING": {
            final List<List> posList;
            posList = (List) properties.get("coordinates");
            final Coordinate[] coordinates = createCoordinates(posList);
            geometry = geometryFactory.createLineString(coordinates);
            break;
        } case "POLYGON": {
            final List<List<List>> posList;
            posList = (List) properties.get("coordinates");
            geometry = createPolygon(posList);
            break;
        } case "MULTIPOINT": {
            final List<List> posList;
            posList = (List) properties.get("coordinates");
            final Coordinate[] coordinates = createCoordinates(posList);
            geometry = geometryFactory.createMultiPoint(coordinates);
            break;
        } case "MULTILINESTRING": {
            final List<List<List>> posList;
            posList = (List) properties.get("coordinates");
            final LineString[] lineStrings = new LineString[posList.size()];
            for (int i=0; i<posList.size(); i++) {
                final Coordinate[] coordinates = createCoordinates(posList.get(i));
                lineStrings[i] = geometryFactory.createLineString(coordinates);
            }
            geometry = geometryFactory.createMultiLineString(lineStrings);
            break;
        } case "MULTIPOLYGON": {
            final List<List<List<List>>> posList;
            posList = (List) properties.get("coordinates");
            final Polygon[] polygons = new Polygon[posList.size()];
            for (int i=0; i<posList.size(); i++) {
                polygons[i] = createPolygon(posList.get(i));
            }
            geometry = geometryFactory.createMultiPolygon(polygons);
            break;
        } case "GEOMETRYCOLLECTION": {
            final List<Map<String,Object>> list;
            list = (List) properties.get("geometries");
            final Geometry[] geometries = new Geometry[list.size()];
            for (int i=0; i<geometries.length; i++) {
                geometries[i] = createGeometry(list.get(i));
            }
            geometry = geometryFactory.createGeometryCollection(geometries);
            break;
        } case "ENVELOPE": {
            final List<List> posList;
            posList = (List) properties.get("coordinates");
            final Coordinate[] coords = createCoordinates(posList);
            final Envelope envelope = new Envelope(coords[0], coords[1]);
            geometry = geometryFactory.toGeometry(envelope);
            break;
        } default:
            // check if this is a geo_point
            final Object latObj = properties.get("lat");
            final Object lonObj = properties.get("lon");
            if (latObj != null && lonObj != null) {
                final Double lat;
                if (latObj instanceof Double) {
                    lat = (Double)latObj;
                } else if (latObj instanceof String) {
                    lat = new Double((String)latObj);
                } else {
                    lat = null;
                }

                final Double lon;
                if (lonObj instanceof Double) {
                    lon = (Double)lonObj;
                } else if (lonObj instanceof String) {
                    lon = new Double((String)lonObj);
                } else {
                    lon = null;
                }

                if (lat != null && lon != null) {
                    geometry = geometryFactory.createPoint(new Coordinate(lon,lat));
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

    private Polygon createPolygon(final List<List<List>> posList) {
        final Coordinate[] shellCoordinates = createCoordinates(posList.get(0));
        final LinearRing shell = geometryFactory.createLinearRing(shellCoordinates);
        final LinearRing[] holes = new LinearRing[posList.size()-1];
        for (int i=1; i<posList.size(); i++) {
            final Coordinate[] coordinates = createCoordinates(posList.get(i));
            holes[i-1] = geometryFactory.createLinearRing(coordinates);
        }
        return geometryFactory.createPolygon(shell, holes);
    }
    
    private Coordinate[] createCoordinates(final List<List> posList) {
        final Coordinate[] coordinates = new Coordinate[posList.size()];
        for (int i=0; i<posList.size(); i++) {
            coordinates[i] = createCoordinate(posList.get(i));
        }
        return coordinates;
    }

    private Coordinate createCoordinate(final List posList) {
        final double x;
        final double y;
        if (Number.class.isAssignableFrom(posList.get(0).getClass())) {
            x = ((Number) posList.get(0)).doubleValue();
            y = ((Number) posList.get(1)).doubleValue();
        } else {
            x = Double.valueOf(posList.get(0).toString());
            y = Double.valueOf(posList.get(1).toString());
        }
        return new Coordinate(x,y);
    }

    /**
     * Read field from document source.
     * @param source Source
     * @param name Field to extract.
     * @return List of values or empty list if not found
     */
    public List<Object> readField(Map<String, Object> source, String name) {
        final List<String> keys = Arrays.asList(name.split("\\."));
        List<Object> values = new ArrayList<>();
        if (!keys.isEmpty()) {
            readField(source.get(keys.get(0)), keys.subList(1, keys.size()), values);
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
            final Object nextEntry = ((Map<?,?>) entry).get(keys.get(0));
            final List<String> newKeys = keys.subList(1, keys.size());
            readField(nextEntry, newKeys, values);
        } else if (entry != null) {
            values.add(entry);
        }
    }

}
