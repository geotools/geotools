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

import org.geotools.factory.Hints;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * A collection of utility methods for use with JTS and the shapefile package.
 * 
 * @author aaime
 * @author Ian Schneider
 *
 * @source $URL$
 */
public class JTSUtilities {

    static final CGAlgorithms cga = new CGAlgorithms();

    private JTSUtilities() {
    }

    /**
     * Determine the min and max "z" values in an array of Coordinates.
     * 
     * @param cs
     *            The array to search.
     * @return An array of size 2, index 0 is min, index 1 is max.
     * @deprecated use zMinMax(CoordinateSequence)
     */
    public static final double[] zMinMax(final Coordinate[] cs) {
        double []result = {Double.NaN, Double.NaN};
        zMinMax(new CoordinateArraySequence(cs), result);
        return result;
    }

    /**
     * Determine the min and max "z" values in an array of Coordinates.
     * 
     * @param cs
     *            The array to search.
     * @param target
     *            array with at least two elements where to hold the min and max
     *            zvalues. target[0] will be filled with the minimum zvalue,
     *            target[1] with the maximum. The array current values, if not
     *            NaN, will be taken into acount in the computation.
     */
    public static final void zMinMax(final CoordinateSequence cs, double[] target) {
        if (cs.getDimension() < 3) {
            return;
        }
        double zmin;
        double zmax;
        boolean validZFound = false;

        zmin = Double.NaN;
        zmax = Double.NaN;

        double z;
        final int size = cs.size();
        for (int t = size - 1; t >= 0; t--) {
            z = cs.getOrdinate(t, 2);

            if (!(Double.isNaN(z))) {
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

        if(!Double.isNaN(zmin)){
            target[0] = zmin;
        }
        if(!Double.isNaN(zmax)){
            target[1] = (zmax);
        }
    }

    /**
     * Determine the best ShapeType for a given Geometry.
     * 
     * @param geom
     *                The Geometry to analyze.
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

    public static final Class findBestGeometryClass(ShapeType type) {
        Class best;
        if (type == null || type == ShapeType.NULL) {
            best = Geometry.class;
        } else if (type.isLineType()) {
            best = MultiLineString.class;
        } else if (type.isMultiPointType()) {
            best = MultiPoint.class;
        } else if (type.isPointType()) {
            best = Point.class;
        } else if (type.isPolygonType()) {
            best = MultiPolygon.class;
        } else {
            throw new RuntimeException("Unknown ShapeType->GeometryClass : "
                    + type);
        }
        return best;
    }

    /**
     * Does what it says, reverses the order of the Coordinates in the ring.
     * <p>
     * This is different then lr.reverses() in that a copy is produced using a
     * new coordinate sequence.
     * </p>
     * @param lr
     *                The ring to reverse.
     * @return A new ring with the reversed Coordinates.
     */
    public static final LinearRing reverseRing(LinearRing lr) {
		GeometryFactory gf = lr.getFactory();
		CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();

		CoordinateSequence csOrig = lr.getCoordinateSequence();
		int numPoints = csOrig.size();
		int dimensions = csOrig.getDimension();
		CoordinateSequence csNew = csf.create(numPoints, dimensions);

		for (int i = 0; i < numPoints; i++) {
			for (int j = 0; j < dimensions; j++) {
				csNew.setOrdinate(numPoints-1-i, j, csOrig.getOrdinate(i, j));
			}
		}
		
		return gf.createLinearRing(csNew);
    }

    /**
     * Create a nice Polygon from the given Polygon. Will ensure that shells are
     * clockwise and holes are counter-clockwise. Capiche?
     * 
     * @param p
     *                The Polygon to make "nice".
     * @return The "nice" Polygon.
     */
    public static final Polygon makeGoodShapePolygon(Polygon p) {
        GeometryFactory factory = p.getFactory();
    	LinearRing outer;
        LinearRing[] holes = new LinearRing[p.getNumInteriorRing()];
        Coordinate[] coords;

        coords = p.getExteriorRing().getCoordinates();

        if (CGAlgorithms.isCCW(coords)) {
            outer = reverseRing((LinearRing) p.getExteriorRing());
        } else {
            outer = (LinearRing) p.getExteriorRing();
        }

        for (int t = 0, tt = p.getNumInteriorRing(); t < tt; t++) {
            coords = p.getInteriorRingN(t).getCoordinates();

            if (!(CGAlgorithms.isCCW(coords))) {
                holes[t] = reverseRing((LinearRing) p.getInteriorRingN(t));
            } else {
                holes[t] = (LinearRing) p.getInteriorRingN(t);
            }
        }

        return factory.createPolygon(outer, holes);
    }

    /**
     * Like makeGoodShapePolygon, but applied towards a multi polygon.
     * 
     * @param mp
     *                The MultiPolygon to "niceify".
     * @return The "nicified" MultiPolygon.
     */
    public static final MultiPolygon makeGoodShapeMultiPolygon(MultiPolygon mp) {
        MultiPolygon result;
        Polygon[] ps = new Polygon[mp.getNumGeometries()];

        // check each sub-polygon
        for (int t = 0; t < mp.getNumGeometries(); t++) {
            ps[t] = makeGoodShapePolygon((Polygon) mp.getGeometryN(t));
        }

        result = mp.getFactory().createMultiPolygon(ps);

        return result;
    }

    /**
     * Returns: <br>
     * 2 for 2d (default) <br>
     * 4 for 3d - one of the oordinates has a non-NaN z value <br>
     * (3 is for x,y,m but thats not supported yet) <br>
     * 
     * @param cs
     *                The array of Coordinates to search.
     * @return The dimension.
     */
    public static final int guessCoorinateDims(final Coordinate[] cs) {
        int dims = 2;

        for (int t = cs.length - 1; t >= 0; t--) {
            if (!(Double.isNaN(cs[t].z))) {
                dims = 4;
                break;
            }
        }

        return dims;
    }

    public static Geometry convertToCollection(Geometry geom, ShapeType type) {
        Geometry retVal = null;
        
        if(geom == null)
        	return null;
        
    	GeometryFactory factory = geom.getFactory();

        if (type.isPointType()) {
            if ((geom instanceof Point)) {
                retVal = geom;
            } else {
                Point[] pNull = null;
                retVal = factory.createMultiPoint(pNull);
            }
        } else if (type.isLineType()) {
            if ((geom instanceof LineString)) {
                retVal = factory
                        .createMultiLineString(new LineString[] { (LineString) geom });
            } else if (geom instanceof MultiLineString) {
                retVal = geom;
            } else {
                retVal = factory.createMultiLineString(null);
            }
        } else if (type.isPolygonType()) {
            if (geom instanceof Polygon) {
                Polygon p = makeGoodShapePolygon((Polygon) geom);
                retVal = factory.createMultiPolygon(new Polygon[] { p });
            } else if (geom instanceof MultiPolygon) {
                retVal = JTSUtilities
                        .makeGoodShapeMultiPolygon((MultiPolygon) geom);
            } else {
                retVal = factory.createMultiPolygon(null);
            }
        } else if (type.isMultiPointType()) {
            if ((geom instanceof Point)) {
                retVal = factory.createMultiPoint(new Point[] { (Point) geom });
            } else if (geom instanceof MultiPoint) {
                retVal = geom;
            } else {
                Point[] pNull = null;
                retVal = factory.createMultiPoint(pNull);
            }
        } else
            throw new RuntimeException("Could not convert " + geom.getClass()
                    + " to " + type);

        return retVal;
    }

    /**
     * Determine the best ShapeType for a geometry with the given dimension.
     * 
     * @param geom
     *                The Geometry to examine.
     * @param shapeFileDimentions
     *                The dimension 2,3 or 4.
     * @throws ShapefileException
     *                 If theres a problem, like a bogus Geometry.
     * @return The best ShapeType.
     */
    public static final ShapeType getShapeType(Geometry geom,
            int shapeFileDimentions) throws ShapefileException {

        ShapeType type = null;

        if (geom instanceof Point) {
            switch (shapeFileDimentions) {
            case 2:
                type = ShapeType.POINT;
                break;
            case 3:
                type = ShapeType.POINTM;
                break;
            case 4:
                type = ShapeType.POINTZ;
                break;
            default:
                throw new ShapefileException(
                        "Too many dimensions for shapefile : "
                                + shapeFileDimentions);
            }
        } else if (geom instanceof MultiPoint) {
            switch (shapeFileDimentions) {
            case 2:
                type = ShapeType.MULTIPOINT;
                break;
            case 3:
                type = ShapeType.MULTIPOINTM;
                break;
            case 4:
                type = ShapeType.MULTIPOINTZ;
                break;
            default:
                throw new ShapefileException(
                        "Too many dimensions for shapefile : "
                                + shapeFileDimentions);
            }
        } else if ((geom instanceof Polygon) || (geom instanceof MultiPolygon)) {
            switch (shapeFileDimentions) {
            case 2:
                type = ShapeType.POLYGON;
                break;
            case 3:
                type = ShapeType.POLYGONM;
                break;
            case 4:
                type = ShapeType.POLYGONZ;
                break;
            default:
                throw new ShapefileException(
                        "Too many dimensions for shapefile : "
                                + shapeFileDimentions);
            }
        } else if ((geom instanceof LineString)
                || (geom instanceof MultiLineString)) {
            switch (shapeFileDimentions) {
            case 2:
                type = ShapeType.ARC;
                break;
            case 3:
                type = ShapeType.ARCM;
                break;
            case 4:
                type = ShapeType.ARCZ;
                break;
            default:
                throw new ShapefileException(
                        "Too many dimensions for shapefile : "
                                + shapeFileDimentions);
            }
        }

        if (type == null) {
            throw new ShapefileException("Cannot handle geometry type : "
                    + (geom == null ? "null" : geom.getClass().getName()));
        }
        return type;
    }

    /**
     * Determine the default ShapeType for a featureClass. Shapetype will be the
     * 2D shapetype.
     * 
     * @param featureClass
     *                The Geometry to examine.
     * @return The best ShapeType.
     * @throws ShapefileException
     *                 If theres a problem, like a bogus feature class.
     */
    public static final ShapeType getShapeType(Class featureClass)
            throws ShapefileException {

        ShapeType type = null;

        if (Point.class.equals(featureClass)) {
            type = ShapeType.POINT;
        } else if (MultiPoint.class.equals(featureClass)) {
            type = ShapeType.MULTIPOINT;
        } else if (Polygon.class.equals(featureClass)
                || MultiPolygon.class.equals(featureClass)) {
            type = ShapeType.POLYGON;
        } else if (LineString.class.equals(featureClass)
                || MultiLineString.class.equals(featureClass)) {
            type = ShapeType.ARC;
        }

        if (type == null) {
            throw new ShapefileException("Cannot handle geometry class : "
                    + (featureClass == null ? "null" : featureClass.getName()));
        }
        return type;
    }
    
    /**
     * Determine the default ShapeType using the descriptor and eventually the
     * geometry to guess the coordinate dimensions if not reported in the descriptor
     * hints
     * @param gd
     * @param g
     * @return
     */
    public static final ShapeType getShapeType(GeometryDescriptor gd) throws ShapefileException {
        Class featureClass = gd.getType().getBinding();
        Integer dimension = (Integer) gd.getUserData().get(Hints.COORDINATE_DIMENSION);
        
        ShapeType type = null;
        if (Point.class.equals(featureClass)) {
            if(dimension != null && dimension == 3)
                type = ShapeType.POINTZ;
            else
                type = ShapeType.POINT;
        } else if (MultiPoint.class.equals(featureClass)) {
            if(dimension != null && dimension == 3)
                type = ShapeType.MULTIPOINTZ;
            else
                type = ShapeType.MULTIPOINT;
        } else if (Polygon.class.equals(featureClass)
                || MultiPolygon.class.equals(featureClass)) {
            if(dimension != null && dimension == 3)
                type = ShapeType.POLYGON;
            else
                type = ShapeType.POLYGONZ;
        } else if (LineString.class.equals(featureClass)
                || MultiLineString.class.equals(featureClass)) {
            if(dimension != null && dimension == 3)
                type = ShapeType.ARC;
            else
                type = ShapeType.ARCZ;
        }
        
        if (type == null) {
            throw new ShapefileException("Cannot handle geometry class : "
                    + (featureClass == null ? "null" : featureClass.getName()));
        }
        
        return type;
    }

}
