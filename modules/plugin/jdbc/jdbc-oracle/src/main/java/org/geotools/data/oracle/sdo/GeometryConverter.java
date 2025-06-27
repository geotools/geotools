/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.data.oracle.sdo;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStruct;
import oracle.sql.CHAR;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Sample use of SDO class for simple JTS Geometry.
 *
 * <p>If needed I can make a LRSGeometryConverter that allows JTS Geometries with additional ordinates beyond xyz.
 *
 * @author jgarnett
 * @author Mark Prins, B3Partners
 */
public class GeometryConverter {
    protected OracleConnection connection;
    GeometryFactory geometryFactory;

    public GeometryConverter(OracleConnection connection) {
        this(connection, new GeometryFactory());
    }

    public GeometryConverter(OracleConnection connection, GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        this.connection = connection;
    }

    public static final String DATATYPE = "MDSYS.SDO_GEOMETRY";
    /**
     * Used to handle MDSYS.SDO_GEOMETRY.
     *
     * @return {@code MDSYS.SDO_GEOMETRY}
     * @see #DATATYPE
     */
    public String getDataTypeName() {
        return DATATYPE;
    }

    /**
     * Ensure that obj is a JTS Geometry (2D or 3D) with no LRS measures.
     *
     * <p>This Converter does not support SpatialCoordinates
     *
     * @param geom the Geometry to be converted
     * @return {@code true} if {@code obj} is a JTS Geometry
     */
    public boolean isCapable(Geometry geom) {
        if (geom == null) return true;
        if (geom instanceof Point
                || geom instanceof MultiPoint
                || geom instanceof LineString
                || geom instanceof MultiLineString
                || geom instanceof Polygon
                || geom instanceof MultiPolygon
                || geom instanceof GeometryCollection) {
            int d = SDO.D(geom);
            int l = SDO.L(geom);
            return l == 0 && (d == 2 || d == 3);
        }
        return false;
    }

    /**
     * Convert provided SDO_GEOMETRY to JTS Geometry.
     *
     * <p>Will return {@code null} as {@code null}.
     *
     * @param sdoGeometry datum STRUCT to be converted to a geometry
     * @return JTS {@code Geometry} representing the provided {@code datum}
     */
    public Geometry asGeometry(OracleStruct sdoGeometry) throws SQLException {
        // Note Returning null for null Datum
        if (sdoGeometry == null) return null;

        Object[] data = sdoGeometry.getAttributes();
        final int GTYPE = asInteger((Number) data[0], 0);
        final int SRID = asInteger((Number) data[1], SDO.SRID_NULL);
        final double[] POINT = asDoubleArray((OracleStruct) data[2], Double.NaN);
        final int[] ELEMINFO = asIntArray((OracleArray) data[3], 0);
        final double[] ORDINATES = asDoubleArray((OracleArray) data[4], Double.NaN);

        return SDO.create(geometryFactory, GTYPE, SRID, POINT, ELEMINFO, ORDINATES);
    }

    /**
     * Used to convert double[] to SDO_ODINATE_ARRAY.
     *
     * <p>Will return {@code null} as an empty {@code SDO_GEOMETRY}
     *
     * @param geom Map to be represented as a STRUCT
     * @return Struct representing provided Map
     */
    public OracleStruct toSDO(Geometry geom) throws SQLException {
        return toSDO(geom, geom.getSRID());
    }

    /**
     * Used to convert double[] to SDO_ODINATE_ARRAY.
     *
     * <p>Will return {@code null} as an empty {@code SDO_GEOMETRY}
     *
     * @param geom Map to be represented as a STRUCT
     * @return STRUCT representing provided Map
     */
    public OracleStruct toSDO(Geometry geom, int srid) throws SQLException {
        if (geom == null || geom.isEmpty()) return asEmptyDataType();

        int gtype = SDO.gType(geom);
        NUMBER SDO_GTYPE = new NUMBER(gtype);

        NUMBER SDO_SRID = srid == SDO.SRID_NULL || srid == 0 ? null : new NUMBER(srid);

        double[] point = SDO.point(geom);
        OracleStruct SDO_POINT;

        OracleArray SDO_ELEM_INFO;
        OracleArray SDO_ORDINATES;

        if (point == null) {
            final Envelope env = geom.getEnvelopeInternal();
            if (env.getWidth() > 0
                    && env.getHeight() > 0
                    && !(geom instanceof GeometryCollection)
                    && geom.isRectangle()) {
                // rectangle optimization. Actually, more than an optimization. A few operators
                // do not work properly if they don't get rectangular geoms encoded as rectangles
                // SDO_FILTER is an example of this silly situation
                int[] elemInfo = {1, 1003, 3};
                double[] ordinates;
                if (SDO.D(geom) == 2)
                    ordinates = new double[] {env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY()};
                else ordinates = new double[] {env.getMinX(), env.getMinY(), 0, env.getMaxX(), env.getMaxY(), 0};

                SDO_POINT = null;
                SDO_ELEM_INFO = toARRAY(elemInfo, "MDSYS.SDO_ELEM_INFO_ARRAY");
                SDO_ORDINATES = toARRAY(ordinates, "MDSYS.SDO_ORDINATE_ARRAY");
            } else {
                int[] elemInfo = SDO.elemInfo(geom);
                double[] ordinates = SDO.ordinates(geom);

                SDO_POINT = null;
                SDO_ELEM_INFO = toARRAY(elemInfo, "MDSYS.SDO_ELEM_INFO_ARRAY");
                SDO_ORDINATES = toARRAY(ordinates, "MDSYS.SDO_ORDINATE_ARRAY");
            }
        } else { // Point Optimization
            Datum[] data = {
                toNUMBER(point[0]), toNUMBER(point[1]), toNUMBER(point[2]),
            };
            SDO_POINT = toSTRUCT(data, "MDSYS.SDO_POINT_TYPE");
            SDO_ELEM_INFO = null;
            SDO_ORDINATES = null;
        }
        Object[] attributes = {SDO_GTYPE, SDO_SRID, SDO_POINT, SDO_ELEM_INFO, SDO_ORDINATES};
        return toSTRUCT(attributes, DATATYPE);
    }

    /**
     * Representation of {@code null} as an Empty {@code SDO_GEOMETRY}.
     *
     * @return {@code null} as a SDO_GEOMETRY
     */
    protected OracleStruct asEmptyDataType() throws SQLException {
        return toSTRUCT(null, DATATYPE);
    }

    /** Convenience method for OracleStruct construction. */
    protected final OracleStruct toSTRUCT(Datum[] attributes, String dataType) throws SQLException {
        if (dataType.startsWith("*.")) {
            dataType = "DRA." + dataType.substring(2); // TODO here
        }
        return (OracleStruct) connection.createStruct(dataType, attributes);
    }

    /** Convenience method for OracleStruct construction. */
    private OracleStruct toSTRUCT(Object[] attributes, String dataType) throws SQLException {
        if (dataType.startsWith("*.")) {
            dataType = "DRA." + dataType.substring(2); // TODO here
        }
        return (OracleStruct) connection.createStruct(dataType, attributes);
    }

    /**
     * Convenience method for OracleArray construction.
     *
     * <p>Compare and contrast with toORDINATE - which treats {@code Double.NaN} as {@code NULL}
     */
    protected final OracleArray toARRAY(double[] doubles, String dataType) throws SQLException {
        return connection.createARRAY(dataType, doubles);
    }

    /**
     * Convenience method for OracleArray construction.
     *
     * <p>Forced to burn memory here - only way to actually place {@code NULL} numbers in the
     * ordinate stream.
     *
     * <ul>
     *   <li>JTS: records lack of data as {@code Double.NaN}
     *   <li>SDO: records lack of data as {@code NULL}
     * </ul>
     *
     * <p>The alternative is to construct the array from a array of doubles, which does not record
     * {@code NULL} NUMBERs.
     *
     * <p>The results is an "MDSYS.SDO_ORDINATE_ARRAY"
     * <pre> {@code
     * list     = c1(1,2,0), c2(3,4,Double.NaN)
     * measures = {{5,6},{7,8}
     *
     * toORDINATE( list, measures, 2 )
     * = (1,2,5,7, 3,4,6,8)
     *
     * toORDINATE( list, measures, 3 )
     * = (1,2,0,5,7, 3,4,NULL,6,8)
     *
     * toORDINATE( list, null, 2 )
     * = (1,2, 3,4)
     * }
     * </pre>
     *
     * @param list CoordinateList to be represented
     * @param measures Per Coordinate Measures, {@code null} if not required
     * @param D Dimension of Coordinates (limited to 2d, 3d)
     */
    protected final OracleArray toORDINATE(CoordinateList list, double[][] measures, final int D) throws SQLException {

        final int LENGTH = measures != null ? measures.length : 0;
        final int LEN = D + LENGTH;
        Datum[] data = new Datum[list.size() * LEN];
        int offset = 0;
        int index = 0;

        for (Coordinate coord : list) {
            data[offset++] = toNUMBER(coord.x);
            data[offset++] = toNUMBER(coord.y);
            if (D == 3) {
                data[offset++] = toNUMBER(coord.x);
            }
            for (int j = 0; j < LENGTH; j++) {
                data[offset++] = toNUMBER(measures[j][index]);
            }
        }
        return connection.createARRAY("MDSYS.SDO_ORDINATE_ARRAY", data);
    }

    protected final OracleArray toORDINATE(double[] ords) throws SQLException {
        final int LENGTH = ords.length;

        Datum[] data = new Datum[LENGTH];

        for (int i = 0; i < LENGTH; i++) {
            data[i] = toNUMBER(ords[i]);
        }
        return connection.createARRAY("MDSYS.SDO_ORDINATE_ARRAY", data);
    }

    protected final OracleArray toATTRIBUTE(double[] ords, String desc) throws SQLException {
        final int LENGTH = ords.length;

        Datum[] data = new Datum[LENGTH];

        for (int i = 0; i < LENGTH; i++) {
            data[i] = toNUMBER(ords[i]);
        }
        return connection.createARRAY(desc, data);
    }

    /**
     * Convenience method for NUMBER construction.
     *
     * <p>Double.NaN is represented as {@code NULL} to agree with JTS use.
     */
    protected final NUMBER toNUMBER(double number) throws SQLException {
        if (Double.isNaN(number)) {
            return null;
        }
        return new NUMBER(number);
    }

    /** Convenience method for OracleArray construction. */
    protected final OracleArray toARRAY(int[] ints, String dataType) throws SQLException {
        return connection.createARRAY(dataType, ints);
    }

    /** Convenience method for NUMBER construction */
    protected final NUMBER toNUMBER(int number) {
        return new NUMBER(number);
    }

    /** Convenience method for CHAR construction */
    protected final CHAR toCHAR(String s) {

        // make sure if the string is larger than one character, only take the first character
        if (s.length() > 1) s = String.valueOf(s.charAt(0));
        try {
            // BUG: make sure I am correct
            return new CHAR(s, CharacterSet.make(CharacterSet.ISO_LATIN_1_CHARSET));
        } catch (SQLException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        return null;
    }

    //
    // These functions present Datum as a Java type
    //
    /** Presents datum as an int */
    protected int asInteger(Datum datum, final int DEFAULT) throws SQLException {
        if (datum == null) return DEFAULT;
        return datum.intValue();
    }
    /**
     * Presents Number as an int with optional default value in case the {@code datum} argument is {@code null}.
     *
     * @see #asInteger(Datum, int)
     */
    private int asInteger(Number datum, final int DEFAULT) throws SQLException {
        if (datum == null) return DEFAULT;
        return datum.intValue();
    }
    /** Presents datum as a double */
    protected double asDouble(Datum datum, final double DEFAULT) throws SQLException {
        if (datum == null) return DEFAULT;
        return ((NUMBER) datum).doubleValue();
    }
    /**
     * Presents Number as a double with optional default value in case the {@code datum} argument is {@code null}.
     *
     * @see #asDouble(Datum, double)
     */
    private double asDouble(Number datum, final double DEFAULT) throws SQLException {
        if (datum == null) return DEFAULT;
        return datum.doubleValue();
    }

    /** Presents struct as a double[] */
    protected double[] asDoubleArray(OracleStruct struct, final double DEFAULT) throws SQLException {
        if (struct == null) return null;
        // cannot cast Object[] to Number[]
        return asDoubleArray(
                Arrays.copyOf(struct.getAttributes(), struct.getAttributes().length, Number[].class), DEFAULT);
    }

    /** Presents array as a double[] */
    protected double[] asDoubleArray(OracleArray array, final double DEFAULT) throws SQLException {
        if (array == null) return null;
        if (DEFAULT == 0) return array.getDoubleArray();

        return asDoubleArray((Number[]) array.getArray(), DEFAULT);
    }

    /** Presents Datum[] as a double[] */
    protected double[] asDoubleArray(Datum[] data, final double DEFAULT) throws SQLException {
        if (data == null) return null;
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = asDouble(data[i], DEFAULT);
        }
        return array;
    }
    /**
     * Presents Number[] as a double[] with optional default value in case an {@code data[n]} argument is {@code null}.
     */
    private double[] asDoubleArray(Number[] data, final double DEFAULT) throws SQLException {
        if (data == null) return null;
        double[] array = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = asDouble(data[i], DEFAULT);
        }
        return array;
    }

    protected int[] asIntArray(OracleArray array, int DEFAULT) throws SQLException {
        if (array == null) return null;
        if (DEFAULT == 0) return array.getIntArray();
        // call with Datum[]
        return asIntArray((Datum[]) array.getArray(), DEFAULT);
    }

    /** Presents Datum[] as a int[] */
    protected int[] asIntArray(Datum[] data, final int DEFAULT) throws SQLException {
        if (data == null) return null;
        int[] array = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = asInteger(data[i], DEFAULT);
        }
        return array;
    }
}
