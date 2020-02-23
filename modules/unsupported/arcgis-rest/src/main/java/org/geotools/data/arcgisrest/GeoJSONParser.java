package org.geotools.data.arcgisrest;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.arcgisrest.schema.catalog.Error__1;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * GeoJSON parsing of simple ,mbi-dimensional features using a streaming parser
 *
 * @author lmorandini
 */
public class GeoJSONParser implements SimpleFeatureIterator {

    /** GeoJSON format constants */
    public static final String ENCODING = "UTF-8";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"; // ISO-8601
    public static final String ERROR = "error";
    public static final String ERROR_CODE = "code";
    public static final String ERROR_MESSAGE = "message";
    public static final String ERROR_DETAILS = "details";
    public static final String GEOJSON_TYPE = "type";
    public static final String GEOJSON_TYPE_VALUE_FC = "FeatureCollection";
    public static final String GEOJSON_PROPERTIES = "properties";
    public static final String GEOJSON_TOTALFEATURES = "totalFeatures";
    public static final String FEATURES = "features";
    public static final String CRS = "crs";
    public static final String CRS_TYPE = "type";
    public static final String CRS_TYPE_VALUE = "name";
    public static final String CRS_PROPERTIES = "properties";
    public static final String CRS_PROPERTIES_NAME = "name";
    public static final String FEATURE_TYPE = "type";
    public static final String FEATURE_ID = "id";
    public static final String FEATURE_GEOMETRY = "geometry";
    public static final String FEATURE_GEOMETRY_TYPE = "type";
    public static final String FEATURE_TYPE_VALUE = "Feature";
    public static final String FEATURE_GEOMETRY_COORDINATES = "coordinates";
    public static final String FEATURE_PROPERTIES = "properties";

    public static final String GEOMETRY_POINT = "Point";
    public static final String GEOMETRY_MULTIPOINT = "MultiPoint";
    public static final String GEOMETRY_LINE = "LineString";
    public static final String GEOMETRY_MULTILINE = "MultiLineString";
    public static final String GEOMETRY_POLYGON = "Polygon";
    public static final String GEOMETRY_MULTIPOLYGON = "MultiPolygon";

    protected static final String ATTRIBUTES = "attributes";
    protected static final String GEOMETRY = "geometry";

    protected Logger LOGGER = null;

    // Reader from which features are read
    protected JsonReader reader;

    // Type of the features to be read
    protected SimpleFeatureType featureType;

    // Flag that shows whether the reader is in the middle of a feature collection
    protected boolean inFeatureCollection = false;

    /**
     * Constructor
     *
     * @param iStream the stream to read features from
     * @param featureTypeIn the feature type the features conform to
     * @param loggerIn the logger to use
     */
    public GeoJSONParser(InputStream iStream, SimpleFeatureType featureTypeIn, Logger loggerIn)
            throws UnsupportedEncodingException {
        this.reader = new JsonReader(new InputStreamReader(iStream, ENCODING));
        LOGGER = loggerIn;
        this.featureType = featureTypeIn;
    }

    /** Makes sure resources are released */
    protected void finalize() throws Throwable {
        try {
            this.inFeatureCollection = false;
            this.close();
        } finally {
            super.finalize();
        }
    }

    /** Closes associated resources (such as the inpout stream) */
    @Override
    public void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Checks whether there is another feature to read
     *
     * @return true if there is another featuere to read, false otherwise
     */
    @Override
    public boolean hasNext() {

        try {
            if (this.inFeatureCollection == true && this.reader.peek() == JsonToken.BEGIN_OBJECT) {
                return true;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        return false;
    }

    /**
     * Returns the next feature in the feature collection (if any)
     *
     * @return the next feature in the collection, null if at the end of it
     */
    @Override
    public SimpleFeature next() throws NoSuchElementException {

        if (this.hasNext() != true) {
            throw new NoSuchElementException();
        }

        return this.parseFeature();
    }

    /**
     * Returns an iterator to navigate the features in the GeoJSON input stream. Since ArcGIS ReST
     * API may return an error message as a JSON (not a GeoJSON), this case is handled by throwing
     * an exception
     *
     * @return A simple feature collection iterator
     */
    public void parseFeatureCollection() throws IOException {

        this.reader.beginObject();

        while (this.reader.hasNext()) {

            switch (this.reader.nextName()) {
                case ERROR:
                    // Deals with an error as reported by the ArcGIS ReST API
                    throw this.parseError();

                case GEOJSON_PROPERTIES:
                    // TODO: ESRI extension to GeoJSON
                    this.reader.beginObject();
                    while (this.reader.hasNext()) {
                        this.reader.skipValue();
                    }
                    this.reader.endObject();
                    break;

                case GEOJSON_TOTALFEATURES:
                    // TODO: ESRI extension to GeoJSON
                    this.reader.skipValue();
                    break;

                case CRS:
                    this.reader.beginObject();
                    while (this.reader.hasNext()) {
                        // TODO: this should be checked against the CRS of the feature type
                        this.reader.skipValue();
                    }
                    this.reader.endObject();
                    break;

                case GEOJSON_TYPE:
                    this.checkPropertyValue(GEOJSON_TYPE_VALUE_FC);
                    break;

                case FEATURES:
                    this.reader.beginArray();
                    this.inFeatureCollection = true;

                    // If there is the features array to read, we can leave this method
                    return;

                default:
                    this.LOGGER.log(Level.WARNING, "Unrecognised property");
            }
        }

        // If we come to this, the GeoJSON is not correctly formatted
        this.reader.beginObject();
        this.inFeatureCollection = false;
    }

    /** Helper function to convert a List of double to an array of doubles */
    public static double[] listToArray(List<Double> coords) {

        double[] arr = new double[coords.size()];
        int i = 0;
        for (Double d : coords) {
            arr[i++] = d.doubleValue();
        }
        return arr;
    }

    /**
     * Utility methof that parses a Point GeoJSON coordinates array and adds them to coords
     *
     * @param coords List to add coordinates to
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    protected void parsePointCoordinates(List<Double> coords)
            throws JsonSyntaxException, IOException, IllegalStateException {

        this.reader.beginArray();

        // Reads the point/vertex coordinates
        while (this.reader.hasNext()) {

            // Read X and Y
            coords.add(this.reader.nextDouble());
            coords.add(this.reader.nextDouble());

            // TODO: for the time being it discards Z
            if (this.reader.peek() == JsonToken.NUMBER) {
                this.reader.skipValue();
            }
        }

        this.reader.endArray();
    }

    /**
     * Parses a GeoJSON coordinates array (it is an Array of point coordinates expressed as Array)
     * and returns it a simple double arrays
     *
     * @return array with coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public double[] parseCoordinateArray()
            throws JsonSyntaxException, IOException, IllegalStateException {

        List<Double> coords = new ArrayList<Double>();

        this.reader.beginArray();

        while (this.reader.hasNext()) {
            this.parsePointCoordinates(coords);
        }

        this.reader.endArray();

        return GeoJSONParser.listToArray(coords);
    }

    /**
     * Parses a Point GeoJSON coordinates array and returns them in an array
     *
     * @return array with coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public double[] parsePointCoordinates()
            throws JsonSyntaxException, IOException, IllegalStateException {

        List<Double> coords = new ArrayList<Double>();
        this.parsePointCoordinates(coords);
        return GeoJSONParser.listToArray(coords);
    }

    /**
     * Parses a MultiPoint GeoJSON coordinates array and adds them to coords
     *
     * @return list of arrays with coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public List<double[]> parseMultiPointCoordinates()
            throws JsonSyntaxException, IOException, IllegalStateException {

        List<double[]> points = new ArrayList<double[]>();

        this.reader.beginArray();
        while (this.reader.hasNext()) {
            points.add(this.parsePointCoordinates());
        }
        this.reader.endArray();

        return points;
    }

    /**
     * Parses a Line GeoJSON coordinates array and adds them to coords
     *
     * @return array with coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateExceptionadds them to coords
     */
    public double[] parseLineStringCoordinates()
            throws JsonSyntaxException, IOException, IllegalStateException {

        return this.parseCoordinateArray();
    }

    /**
     * Parses a MultiLine GeoJSON coordinates array and adds them to coords
     *
     * @return list of arrays with coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public List<double[]> parseMultiLineStringCoordinates()
            throws JsonSyntaxException, IOException, IllegalStateException {

        List<double[]> lines = new ArrayList<double[]>();

        this.reader.beginArray();
        while (this.reader.hasNext()) {
            lines.add(this.parseLineStringCoordinates());
        }
        this.reader.endArray();
        return lines;
    }

    /**
     * Parses a Polygon GeoJSON coordinates array and adds them to coords
     *
     * @return list of arrays with coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public List<double[]> parsePolygonCoordinates()
            throws JsonSyntaxException, IOException, IllegalStateException {

        List<double[]> rings = new ArrayList<double[]>();

        this.reader.beginArray();
        while (this.reader.hasNext()) {
            rings.add(this.parseLineStringCoordinates());
        }
        this.reader.endArray();
        return rings;
    }

    /**
     * Parses a MultiPolygon GeoJSON coordinates array and adds them to coords
     *
     * @return list of arrays with ring coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public List<List<double[]>> parseMultiPolygonCoordinates()
            throws JsonSyntaxException, IOException, IllegalStateException {

        List<List<double[]>> polys = new ArrayList<List<double[]>>();

        this.reader.beginArray();
        while (this.reader.hasNext()) {
            polys.add(this.parsePolygonCoordinates());
        }
        this.reader.endArray();
        return polys;
    }

    /**
     * Parses a Geometry in GeoJSON format
     *
     * @return list of arrays with ring coordinates
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public Geometry parseGeometry() throws JsonSyntaxException, IOException, IllegalStateException {

        double[] coords;
        GeometryBuilder builder = new GeometryBuilder();
        GeometryFactory geomFactory = new GeometryFactory();

        // If geometry is null, returns a null point
        try {
            if (this.reader.peek() == JsonToken.NULL) {
                this.reader.nextNull();
                throw (new MalformedJsonException(
                        "just here to avoid repeating the return statement"));
            }
        } catch (IllegalStateException | MalformedJsonException e) {
            return builder.point();
        }

        this.reader.beginObject();

        // Check the presence of feature type
        if (!reader.nextName().equals(FEATURE_TYPE)) {
            throw (new JsonSyntaxException("Geometry type expected"));
        }

        switch (reader.nextString()) {
            case GEOMETRY_POINT:
                this.checkPropertyName(FEATURE_GEOMETRY_COORDINATES);
                coords = this.parsePointCoordinates();
                this.reader.endObject();
                return (Geometry) builder.point(coords[0], coords[1]);

            case GEOMETRY_MULTIPOINT:
                this.checkPropertyName(FEATURE_GEOMETRY_COORDINATES);
                List<double[]> pointCoords = this.parseMultiPointCoordinates();
                ;
                Point[] points = new Point[pointCoords.size()];
                for (int i = 0; i < pointCoords.size(); i++) {
                    points[i] = (Point) builder.point(pointCoords.get(i)[0], pointCoords.get(i)[1]);
                }
                this.reader.endObject();
                return (Geometry) new MultiPoint(points, geomFactory);

            case GEOMETRY_LINE:
                this.checkPropertyName(FEATURE_GEOMETRY_COORDINATES);
                coords = this.parseLineStringCoordinates();
                this.reader.endObject();
                return (Geometry) builder.lineString(coords);

            case GEOMETRY_MULTILINE:
                this.checkPropertyName(FEATURE_GEOMETRY_COORDINATES);
                List<double[]> lineArrays = this.parseMultiLineStringCoordinates();
                LineString[] lines = new LineString[lineArrays.size()];
                int i = 0;
                for (double[] array : lineArrays) {
                    lines[i++] = builder.lineString(array);
                }
                this.reader.endObject();
                return (Geometry) builder.multiLineString(lines);

            case GEOMETRY_POLYGON:
                this.checkPropertyName(FEATURE_GEOMETRY_COORDINATES);
                List<double[]> rings = this.parsePolygonCoordinates();
                this.reader.endObject();
                return (Geometry) builder.polygon(rings.get(0)); // FIXME: what about
                // holes?

            case GEOMETRY_MULTIPOLYGON:
                this.checkPropertyName(FEATURE_GEOMETRY_COORDINATES);
                List<List<double[]>> polyArrays = this.parseMultiPolygonCoordinates();
                Polygon[] polys = new Polygon[polyArrays.size()];
                int j = 0;
                for (List<double[]> array : polyArrays) {
                    polys[j++] = builder.polygon(array.get(0)); // FIXME: what about holes?
                }
                this.reader.endObject();
                return (Geometry) builder.multiPolygon(polys);

            default:
                throw (new JsonSyntaxException("Unrecognized geometry type"));
        }
    }

    /**
     * Parses a GeoJSON feature properties. The values returned in a map is a Boolean, a String, or
     * a Double (for every numeric values)
     *
     * @return A map with property names as keys, and property values as values
     * @throws IOException, JsonSyntaxException, IllegalStateException
     */
    public Map<String, Object> parseProperties()
            throws JsonSyntaxException, IOException, IllegalStateException {

        Map<String, Object> props = new HashMap<String, Object>();
        String name;

        // If properties is null, returns a null point
        // If geometry is null, returns a null point
        try {
            if (this.reader.peek() == JsonToken.NULL) {
                this.reader.nextNull();
                throw (new MalformedJsonException(
                        "just here to avoid repeating the return statement"));
            }
        } catch (IllegalStateException | MalformedJsonException e) {
            return props;
        }

        this.reader.beginObject();

        try {
            while (this.reader.hasNext()) {
                name = this.reader.nextName();

                switch (this.reader.peek()) {
                    case BOOLEAN:
                        props.put(name, this.reader.nextBoolean());
                        break;

                    case NUMBER:
                        // Numbers could be just that, or datetimes expressed in POSIX time
                        if (this.featureType.getDescriptor(name) != null
                                && this.featureType
                                        .getDescriptor(name)
                                        .getType()
                                        .getBinding()
                                        .getName()
                                        .equalsIgnoreCase("java.util.Date")) {
                            props.put(
                                    name,
                                    (new SimpleDateFormat(GeoJSONParser.DATETIME_FORMAT))
                                            .format(new Date(this.reader.nextLong())));
                        } else {
                            props.put(name, this.reader.nextDouble());
                        }
                        break;

                    case STRING:
                        props.put(name, this.reader.nextString());
                        break;

                    case NULL:
                        this.reader.nextNull();
                        props.put(name, null);
                        break;

                    default:
                        throw (new JsonSyntaxException("Value expected"));
                }
            }
        } catch (IOException | IllegalStateException e) {
            throw (new NoSuchElementException(e.getMessage()));
        }

        this.reader.endObject();

        return props;
    }

    /**
     * Parses a GeoJSON feature that conforms to the given FeatureType
     *
     * @return the parsed feature
     */
    public SimpleFeature parseFeature() {

        Geometry geom = null;
        String id = SimpleFeatureBuilder.createDefaultFeatureIdentifier(FEATURES).getID();
        Map<String, Object> props = new HashMap<String, Object>();
        List<Object> values = new ArrayList();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(this.featureType);

        // Parses the feature
        try {
            this.reader.beginObject();

            while (this.reader.hasNext()) {

                switch (this.reader.nextName()) {
                    case FEATURE_TYPE:
                        this.checkPropertyValue(FEATURE_TYPE_VALUE);
                        break;

                    case FEATURE_GEOMETRY:
                        geom = this.parseGeometry();
                        break;

                    case FEATURE_PROPERTIES:
                        props = this.parseProperties();
                        break;

                    case FEATURE_ID:
                        id = this.reader.nextString();
                        break;

                    default:
                        this.LOGGER.log(Level.WARNING, "Unrecognized feature format");
                        this.reader.skipValue();
                }
            }

            this.reader.endObject();

        } catch (IOException | IllegalStateException e) {
            throw (new NoSuchElementException(e.getMessage()));
        }

        // Builds the feature, inserting the properties in the same
        // order of the atterbiutes in the feature type
        for (AttributeDescriptor attr : this.featureType.getAttributeDescriptors()) {

            if (this.featureType
                    .getGeometryDescriptor()
                    .getLocalName()
                    .equals(attr.getLocalName())) {
                builder.add(geom);
            } else {
                builder.add(props.get(attr.getLocalName()));
            }
        }

        return builder.buildFeature(id);
    }

    /**
     * Parses an ArcGIS ReST API error message
     *
     * @return the exception reflecting the error
     */
    public IOException parseError() throws IOException {

        Error__1 err = new Error__1();

        try {
            this.reader.beginObject();

            while (this.reader.hasNext()) {

                switch (this.reader.nextName()) {
                    case ERROR_CODE:
                        err.setCode(this.reader.nextInt());
                        break;

                    case ERROR_MESSAGE:
                        err.setMessage(this.reader.nextString());
                        break;

                    case ERROR_DETAILS:
                        List<String> details = new ArrayList<String>();
                        this.reader.beginArray();
                        while (this.reader.hasNext()) {
                            details.add(this.reader.nextString());
                        }
                        this.reader.endArray();
                        err.setDetails(details);
                }
            }

            this.reader.endObject();

        } catch (IOException | IllegalStateException e) {
            throw (new NoSuchElementException(e.getMessage()));
        }

        this.close();
        return new IOException(
                "ArcGIS ReST API Error: "
                        + err.getCode()
                        + " "
                        + err.getMessage()
                        + " "
                        + String.join(",", err.getDetails()));
    }

    /**
     * Checks the next token is expProp, trows an exception if not
     *
     * @param expProp expected property name
     * @throws JsonSyntaxException ,IoException
     */
    protected void checkPropertyName(String expProp) throws JsonSyntaxException, IOException {

        if (!expProp.equals(this.reader.nextName())) {
            throw (new JsonSyntaxException("'" + expProp + "' property expected"));
        }
    }

    /**
     * Checks the next strig value is expValue, trows an exception if not
     *
     * @param expValue expected property value
     * @throws JsonSyntaxException ,IoException
     */
    protected void checkPropertyValue(String expValue) throws JsonSyntaxException, IOException {

        if (!expValue.equals(this.reader.nextString())) {
            throw (new JsonSyntaxException("'" + expValue + "' value expected"));
        }
    }
}
