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
package org.geotools.data.shapefile.shp;

import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.geom.*;

/**
 * A collection of utility methods for use with JTS and the shapefile package.
 *
 * @author aaime
 * @author Ian Schneider
 */
public class JTSUtilities {

    private JTSUtilities() {}

    /**
     * Determine the min and max "z" values in an array of Coordinates.
     *
     * @param cs The array to search.
     * @param target array with at least two elements where to hold the min and max zvalues. target[0] will be filled
     *     with the minimum zvalue, target[1] with the maximum. The array current values, if not NaN, will be taken into
     *     acount in the computation.
     */
    public static final void zMinMax(final CoordinateSequence cs, double[] target) {
        if (cs.getDimension() < 3) {
            return;
        }
        boolean validZFound = false;

        double zmin = Double.NaN;
        double zmax = Double.NaN;

        double z;
        final int size = cs.size();
        for (int t = size - 1; t >= 0; t--) {
            z = cs.getOrdinate(t, 2);

            if (!Double.isNaN(z)) {
                if (validZFound) {
                    if (z < zmin) {
                        zmin = z;
                    }

                    if (z > zmax) {
                        zmax = z;
                    }
                } else {
                    validZFound = true;
                    zmin = z;
                    zmax = z;
                }
            }
        }

        if (!Double.isNaN(zmin)) {
            target[0] = zmin;
        }
        if (!Double.isNaN(zmax)) {
            target[1] = zmax;
        }
    }

    /**
     * Determine the best ShapeType for a given Geometry.
     *
     * @param geom The Geometry to analyze.
     * @return The best ShapeType for the Geometry.
     */
    public static final ShapeType findBestGeometryType(Geometry geom) {
        ShapeType type = ShapeType.UNDEFINED;

        if (geom instanceof Point) {
            type = ShapeType.POINT;
        } else if (geom instanceof MultiPoint) {
            type = ShapeType.MULTIPOINT;
        } else if (geom instanceof Polygon) {
            type = ShapeType.POLYGON;
        } else if (geom instanceof MultiPolygon) {
            type = ShapeType.POLYGON;
        } else if (geom instanceof LineString) {
            type = ShapeType.ARC;
        } else if (geom instanceof MultiLineString) {
            type = ShapeType.ARC;
        }
        return type;
    }

    public static final Class<? extends Geometry> findBestGeometryClass(ShapeType type) {
        if (type == null || type == ShapeType.NULL) {
            return Geometry.class;
        } else if (type.isLineType()) {
            return MultiLineString.class;
        } else if (type.isMultiPointType()) {
            return MultiPoint.class;
        } else if (type.isPointType()) {
            return Point.class;
        } else if (type.isPolygonType()) {
            return MultiPolygon.class;
        }
        throw new RuntimeException("Unknown ShapeType->GeometryClass : " + type);
    }

    /**
     * Does what it says, reverses the order of the Coordinates in the ring.
     *
     * <p>This is different then lr.reverses() in that a copy is produced using a new coordinate sequence.
     *
     * @param lr The ring to reverse.
     * @return A new ring with the reversed Coordinates.
     */
    public static final LinearRing reverseRing(LinearRing lr) {
        GeometryFactory gf = lr.getFactory();
        CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();

        CoordinateSequence csOrig = lr.getCoordinateSequence();
        int numPoints = csOrig.size();
        int dimensions = csOrig.getDimension();

        CoordinateSequence csNew = JTS.createCS(csf, numPoints, dimensions, csOrig.getMeasures());

        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < dimensions; j++) {
                csNew.setOrdinate(numPoints - 1 - i, j, csOrig.getOrdinate(i, j));
            }
        }
        return gf.createLinearRing(csNew);
    }

    /**
     * Create a nice Polygon from the given Polygon. Will ensure that shells are clockwise and holes are
     * counter-clockwise. Capiche?
     *
     * @param p The Polygon to make "nice".
     * @return The "nice" Polygon.
     */
    public static final Polygon makeGoodShapePolygon(Polygon p) {
        GeometryFactory factory = p.getFactory();
        LinearRing outer;
        LinearRing[] holes = new LinearRing[p.getNumInteriorRing()];

        Coordinate[] coords = p.getExteriorRing().getCoordinates();

        if (Orientation.isCCW(coords)) {
            outer = reverseRing(p.getExteriorRing());
        } else {
            outer = p.getExteriorRing();
        }

        for (int t = 0, tt = p.getNumInteriorRing(); t < tt; t++) {
            coords = p.getInteriorRingN(t).getCoordinates();

            if (!Orientation.isCCW(coords)) {
                holes[t] = reverseRing(p.getInteriorRingN(t));
            } else {
                holes[t] = p.getInteriorRingN(t);
            }
        }

        return factory.createPolygon(outer, holes);
    }

    /**
     * Like makeGoodShapePolygon, but applied towards a multi polygon.
     *
     * @param mp The MultiPolygon to "niceify".
     * @return The "nicified" MultiPolygon.
     */
    public static final MultiPolygon makeGoodShapeMultiPolygon(MultiPolygon mp) {
        Polygon[] ps = new Polygon[mp.getNumGeometries()];

        // check each sub-polygon
        for (int t = 0; t < mp.getNumGeometries(); t++) {
            ps[t] = makeGoodShapePolygon((Polygon) mp.getGeometryN(t));
        }

        MultiPolygon result = mp.getFactory().createMultiPolygon(ps);

        return result;
    }

    /**
     * Returns: <br>
     * 2 for 2d (default) <br>
     * 4 for 3d - one of the oordinates has a non-NaN z value <br>
     * (3 is for x,y,m but thats not supported yet) <br>
     *
     * @param cs The array of Coordinates to search.
     * @return The dimension.
     */
    @Deprecated
    public static final int guessCoorinateDims(final Coordinate[] cs) {
        int dims = 2;

        for (int t = cs.length - 1; t >= 0; t--) {
            if (!Double.isNaN(cs[t].getZ())) {
                dims = 4;
                break;
            }
        }

        return dims;
    }

    /**
     * Guess JTS CoordianteSequence dimension based on the provided ShapeType.
     *
     * For 2 dimensional POINT, ARC, POLYGON, MULTIPOINT, and
     * 2 dimensional with measure POINTM, ARCM, POLYGONM, MULTIPOINTM
     * this is {@code 2}.
     *
     * For 3 dimensional POINTZ, ARCZ, POLYGONZ, MULTIPOINTZ we assume {@code 3},
     * although it could be 4 if optional measure is included in Geometry.
     *
     * @param shapeType
     * @return JTS CoordinateSequence dimensions (2, 3 or 4).
     */
    public static int guessCoordinateDimension(ShapeType shapeType) {
        if (shapeType == null) {
            return 3;
        }
        switch (shapeType) {
            case POINT:
            case ARC:
            case POLYGON:
            case MULTIPOINT:
                return 2;
            case POINTM:
            case ARCM:
            case POLYGONM:
            case MULTIPOINTM:
                return 3;
            case POINTZ:
            case ARCZ:
            case POLYGONZ:
            case MULTIPOINTZ:
                // assume Z only as the more common case
                return 3;
            default:
                return 2;
        }
    }
    /**
     * Use a sample Geometry check Coordinate dimensions.
     *
     * Returns: <br>
     * 2 for 2d (default) <br>
     * 3 for 3d - has a 3rd dimension (either z or measure) <br>
     * 4 for 4d - has both Z and M <br>
     *
     * This is done by review at geometry coordinates. See {@link Coordinates#dimension(Coordinate)}.
     *
     * @param cs The array of Coordinates to search.
     * @return The dimension.
     */
    public static int coordinateDimension(final Coordinate[] cs) {
        if (cs == null || cs.length == 0) {
            return 2;
        }
        int dims = 1;
        for (int t = cs.length - 1; t >= 0; t--) {
            if (cs[t] != null) {
                dims = Math.max(dims, Coordinates.dimension(cs[t]));
            }
        }
        return dims;
    }

    /**
     * Use geometry Coordinates to check if Coordinate has Z dimension.
     *
     * Returns: <br>
     * false for 2d (default) <br>
     * true  for 3d - has a 3rd dimension<br>
     * false for 2d with mesure<br>
     * true for 4d - has both Z and M <br>
     *
     * This is done by review at geometry coordinates. See {@link Coordinates#dimension(Coordinate)}.
     *
     * @param cs The array of Coordinates to search.
     * @return The dimension.
     */
    public static boolean coordinateHasM(final Coordinate[] cs) {
        if (cs == null || cs.length == 0) {
            return false;
        }
        for (int t = cs.length - 1; t >= 0; t--) {
            if (cs[t] != null && Coordinates.hasZ(cs[t])) {
                return true;
            }
        }
        return false;
    }



    /**
     * Use a sample Geometry check Coordinate measures.
     *
     * Returns: <br>
     * 0 for 2d (default) <br>
     * 0 for 3d - has a 3rd dimension<br>
     * 1 for XYM - has a 2d with measure<br>
     * 1 for XYZM - has 3d with measure<br>
     *
     * This is done by looking at geometry coordinates. See {@link Coordinates#measures(Coordinate)}.
     *
     * @param cs The array of Coordinates to search.
     * @return The dimension.
     */
    public static int coordinateMeasures(final Coordinate[] cs) {
        if (cs == null || cs.length == 0) {
            return 0;
        }
        int measures = 0;
        for (int t = cs.length - 1; t >= 0; t--) {
            if (cs[t] != null) {
                measures = Math.max(measures, Coordinates.measures(cs[t]));
            }
        }
        return measures;
    }

    public static Geometry convertToCollection(Geometry geom, ShapeType type) {
        Geometry retVal = null;

        if (geom == null) return null;

        GeometryFactory factory = geom.getFactory();

        if (type.isPointType()) {
            if (geom instanceof Point) {
                retVal = geom;
            } else {
                Point[] pNull = null;
                retVal = factory.createMultiPoint(pNull);
            }
        } else if (type.isLineType()) {
            if (geom instanceof LineString string) {
                retVal = factory.createMultiLineString(new LineString[] {string});
            } else if (geom instanceof MultiLineString) {
                retVal = geom;
            } else {
                retVal = factory.createMultiLineString(null);
            }
        } else if (type.isPolygonType()) {
            if (geom instanceof Polygon polygon1) {
                Polygon p = makeGoodShapePolygon(polygon1);
                retVal = factory.createMultiPolygon(new Polygon[] {p});
            } else if (geom instanceof MultiPolygon polygon) {
                retVal = JTSUtilities.makeGoodShapeMultiPolygon(polygon);
            } else {
                retVal = factory.createMultiPolygon(null);
            }
        } else if (type.isMultiPointType()) {
            if (geom instanceof Point point) {
                retVal = factory.createMultiPoint(new Point[] {point});
            } else if (geom instanceof MultiPoint) {
                retVal = geom;
            } else {
                Point[] pNull = null;
                retVal = factory.createMultiPoint(pNull);
            }
        } else throw new RuntimeException("Could not convert " + geom.getClass() + " to " + type);

        return retVal;
    }

    /**
     * Determine the best ShapeType for a geometry with the given dimension.
     *
     * @param geom The Geometry to examine.
     * @param shapeFileDimentions The dimension 2,3 or 4.
     * @throws ShapefileException If theres a problem, like a bogus Geometry.
     * @return The best ShapeType.
     */
    public static final ShapeType getShapeType(Geometry geom) throws ShapefileException {

        var coordinate = geom.getCoordinate();

        var shapeFileDimentions = "xyz";
        if (coordinate instanceof CoordinateXY) {
            shapeFileDimentions = "xy";
        } else if (coordinate instanceof CoordinateXYM) {
            shapeFileDimentions = "xym";
        } else if (coordinate instanceof CoordinateXYZM) {
            shapeFileDimentions = "xyzm";
        } else if (coordinate instanceof Coordinate) {
            shapeFileDimentions = "xyz";
        }

        ShapeType type = null;

        if (geom instanceof Point) {
            switch (shapeFileDimentions) {
                case "xy":
                    type = ShapeType.POINT;
                    break;
                case "xym":
                    type = ShapeType.POINTM;
                    break;
                case "xyz":
                case "xyzm":
                    type = ShapeType.POINTZ;
                    break;
                default:
                    throw new ShapefileException("Too many dimensions for shapefile : " + shapeFileDimentions);
            }
        } else if (geom instanceof MultiPoint) {
            switch (shapeFileDimentions) {
                case "xy":
                    type = ShapeType.MULTIPOINT;
                    break;
                case "xym":
                    type = ShapeType.MULTIPOINTM;
                    break;
                case "xyz":
                case "xyzm":
                    type = ShapeType.MULTIPOINTZ;
                    break;
                default:
                    throw new ShapefileException("Too many dimensions for shapefile : " + shapeFileDimentions);
            }
        } else if (geom instanceof Polygon || geom instanceof MultiPolygon) {
            switch (shapeFileDimentions) {
                case "xy":
                    type = ShapeType.POLYGON;
                    break;
                case "xym":
                    type = ShapeType.POLYGONM;
                    break;
                case "xyz":
                case "xyzm":
                    type = ShapeType.POLYGONZ;
                    break;
                default:
                    throw new ShapefileException("Too many dimensions for shapefile : " + shapeFileDimentions);
            }
        } else if (geom instanceof LineString || geom instanceof MultiLineString) {
            switch (shapeFileDimentions) {
                case "xy":
                    type = ShapeType.ARC;
                    break;
                case "xym":
                    type = ShapeType.ARCM;
                    break;
                case "xyz":
                case "xyzm":
                    type = ShapeType.ARCZ;
                    break;
                default:
                    throw new ShapefileException("Too many dimensions for shapefile : " + shapeFileDimentions);
            }
        }

        if (type == null) {
            throw new ShapefileException("Cannot handle geometry type : "
                    + (geom == null ? "null" : geom.getClass().getName()));
        }
        return type;
    }

    /**
     * Determine the default ShapeType for a featureClass. Shapetype will be the 2D shapetype.
     *
     * @param featureClass The Geometry to examine.
     * @return The best ShapeType.
     * @throws ShapefileException If theres a problem, like a bogus feature class.
     */
    public static final ShapeType getShapeType(Class<? extends Geometry> featureClass) throws ShapefileException {
        if (Point.class.equals(featureClass)) {
            return ShapeType.POINT;
        } else if (MultiPoint.class.equals(featureClass)) {
            return ShapeType.MULTIPOINT;
        } else if (Polygon.class.equals(featureClass) || MultiPolygon.class.equals(featureClass)) {
            return ShapeType.POLYGON;
        } else if (LineString.class.equals(featureClass) || MultiLineString.class.equals(featureClass)) {
            return ShapeType.ARC;
        }
        throw new ShapefileException(
                "Cannot handle geometry class : " + (featureClass == null ? "null" : featureClass.getName()));
    }

    /**
     * Determine the default ShapeType using the descriptor and eventually the geometry to guess the coordinate
     * dimensions if not reported in the descriptor hints
     */
    public static final ShapeType getShapeType(GeometryDescriptor gd) throws ShapefileException {
        @SuppressWarnings("unchecked")
        Class<? extends Geometry> featureClass =
                (Class<? extends Geometry>) gd.getType().getBinding();

        Integer dimension = (Integer) gd.getUserData().get(Hints.COORDINATE_DIMENSION);
        final int dim = dimension == null ? 2 : dimension.intValue();
        if (Point.class.equals(featureClass)) {
            return dim == 3 ? ShapeType.POINTZ : ShapeType.POINT;
        } else if (MultiPoint.class.equals(featureClass)) {
            return dim == 3 ? ShapeType.MULTIPOINTZ : ShapeType.MULTIPOINT;
        } else if (Polygon.class.equals(featureClass) || MultiPolygon.class.equals(featureClass)) {
            return dim == 3 ? ShapeType.POLYGON : ShapeType.POLYGONZ;
        } else if (LineString.class.equals(featureClass) || MultiLineString.class.equals(featureClass)) {
            return dim == 3 ? ShapeType.ARC : ShapeType.ARCZ;
        }
        throw new ShapefileException(
                "Cannot handle geometry class : " + (featureClass == null ? "null" : featureClass.getName()));
    }

}
