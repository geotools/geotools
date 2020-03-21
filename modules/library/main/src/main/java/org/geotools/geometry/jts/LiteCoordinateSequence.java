/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;

/**
 * @TODO class description
 *
 * @author jeichar
 * @since 2.1.x
 */
public class LiteCoordinateSequence extends PackedCoordinateSequence implements Cloneable {

    private static final GeometryFactory geomFac =
            new GeometryFactory(new LiteCoordinateSequenceFactory());

    /** The packed coordinate array */
    private double[] coords;

    /**
     * Cached size, getSize() gets called an incredible number of times during rendering (a profile
     * shows 2 million calls when rendering 90.000 linear features)
     */
    private int size;

    /** Builds a new packed coordinate sequence */
    public LiteCoordinateSequence(double[] coords, int dimensions) {
        this(coords, dimensions, 0);
    }

    /** Builds a new packed coordinate sequence */
    public LiteCoordinateSequence(double[] coords, int dimensions, int measures) {
        super(dimensions, measures);
        init(coords, dimensions);
    }

    /** Private initializer, allows sharing code between constructors */
    void init(double[] coords, int dimensions) {
        this.dimension = dimensions;
        if (dimensions < 2)
            throw new IllegalArgumentException("Invalid dimensions, must be at least 2");
        if (coords.length % dimension != 0) {
            throw new IllegalArgumentException(
                    "Packed array does not contain an integral number of coordinates");
        }
        this.coords = coords;
        this.size = coords.length / dimensions;
    }

    /** Builds a new packed coordinate sequence */
    public LiteCoordinateSequence(double... coords) {
        this(coords, 2, 0);
    }

    /** Builds a new packed coordinate sequence out of a float coordinate array */
    public LiteCoordinateSequence(float[] coordinates, int dimension) {
        super(dimension, 0);
        double[] dcoords = new double[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            dcoords[i] = coordinates[i];
        }
        init(dcoords, dimension);
    }

    /** Builds a new packed coordinate sequence out of a float coordinate array */
    public LiteCoordinateSequence(float[] coordinates) {
        this(coordinates, 2);
    }

    /** Builds a new packed coordinate sequence out of a coordinate array */
    public LiteCoordinateSequence(Coordinate[] coordinates) {
        super(guessDimension(coordinates == null ? new Coordinate[0] : coordinates), 0);
        if (coordinates == null) coordinates = new Coordinate[0];
        this.dimension = guessDimension(coordinates);

        coords = new double[coordinates.length * this.dimension];
        for (int i = 0; i < coordinates.length; i++) {
            coords[i * this.dimension] = coordinates[i].x;
            if (this.dimension > 2) {
                coords[i * this.dimension + 1] = coordinates[i].y;
                coords[i * this.dimension + 2] = coordinates[i].getZ();
            } else if (this.dimension > 1) {
                coords[i * this.dimension + 1] = coordinates[i].y;
            }
        }
        this.size = coordinates.length;
    }

    private static int guessDimension(Coordinate[] coordinates) {
        for (Coordinate c : coordinates) {
            if (!java.lang.Double.isNaN(c.getZ())) {
                return 3;
            }
        }

        return 2;
    }

    /** Builds a new empty packed coordinate sequence of a given size and dimension */
    public LiteCoordinateSequence(int size, int dimension) {
        this(size, dimension, 0);
    }

    /** Builds a new empty packed coordinate sequence of a given size and dimension */
    public LiteCoordinateSequence(int size, int dimension, int measures) {
        super(dimension, measures);
        coords = new double[size * this.dimension];
        this.size = coords.length / dimension;
    }

    /** Copy constructor */
    public LiteCoordinateSequence(LiteCoordinateSequence seq) {
        super(seq.getDimension(), seq.getMeasures());
        // a trivial benchmark can show that cloning arrays like this is actually faster
        // than calling clone on the array.
        this.dimension = seq.dimension;
        this.size = seq.size;
        double[] orig = seq.getArray();
        this.coords = new double[orig.length];
        System.arraycopy(orig, 0, coords, 0, coords.length);
    }

    public LiteCoordinateSequence(CoordinateSequence cs, int dimension) {
        super(dimension, cs.getMeasures());
        this.size = cs.size();
        this.dimension = dimension;

        if (cs instanceof LiteCoordinateSequence) {
            double[] orig = ((LiteCoordinateSequence) cs).getOrdinateArray(dimension);
            this.coords = new double[orig.length];
            System.arraycopy(orig, 0, coords, 0, coords.length);
        } else {
            this.coords = new double[size * dimension];
            int minDimension = Math.min(dimension, cs.getDimension());
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < minDimension; j++) {
                    coords[i * dimension + j] = cs.getOrdinate(i, j);
                }
            }
        }
    }

    /** @see org.locationtech.jts.geom.CoordinateSequence#getCoordinate(int) */
    public Coordinate getCoordinateInternal(int i) {
        double x = coords[i * dimension];
        double y = coords[i * dimension + 1];
        double z = dimension == 2 ? java.lang.Double.NaN : coords[i * dimension + 2];
        return new Coordinate(x, y, z);
    }

    /** @see org.locationtech.jts.geom.CoordinateSequence#size() */
    public int size() {
        return size;
    }

    @SuppressWarnings("deprecation")
    public Object clone() {
        double[] clone = new double[coords.length];
        System.arraycopy(coords, 0, clone, 0, coords.length);
        return new LiteCoordinateSequence(clone, dimension);
    }

    @Override
    public PackedCoordinateSequence copy() {
        return (PackedCoordinateSequence) clone();
    }

    /**
     * @see org.locationtech.jts.geom.CoordinateSequence#getOrdinate(int, int) Beware, for
     *     performace reasons the ordinate index is not checked, if it's over dimensions you may not
     *     get an exception but a meaningless value.
     */
    public double getOrdinate(int index, int ordinate) {
        return coords[index * dimension + ordinate];
    }

    /** @see org.locationtech.jts.geom.CoordinateSequence#getX(int) */
    public double getX(int index) {
        return coords[index * dimension];
    }

    /** @see org.locationtech.jts.geom.CoordinateSequence#getY(int) */
    public double getY(int index) {
        return coords[index * dimension + 1];
    }

    /** @see org.locationtech.jts.geom.PackedCoordinateSequence#setOrdinate(int, int, double) */
    public void setOrdinate(int index, int ordinate, double value) {
        coordRef = null;
        coords[index * dimension + ordinate] = value;
    }

    public Envelope expandEnvelope(Envelope env) {
        double minx = coords[0];
        double maxx = minx;
        double miny = coords[1];
        double maxy = miny;
        for (int i = 0; i < coords.length; i += dimension) {
            double x = coords[i];
            if (x < minx) minx = x;
            else if (x > maxx) maxx = x;
            double y = coords[i + 1];
            if (y < miny) miny = y;
            else if (y > maxy) maxy = y;
        }
        env.expandToInclude(minx, miny);
        env.expandToInclude(maxx, maxy);
        return env;
    }

    /** */
    public double[] getArray() {
        return coords;
    }

    /** @param coords2 */
    public void setArray(double[] coords2) {
        init(coords2, dimension);
        coordRef = null;
    }

    public void setArray(double[] coords2, int dimension) {
        init(coords2, dimension);
        coordRef = null;
    }

    /**
     * if this is a dimension=2 seq, then this is the same as getArray(). If its >2 dims this will
     * make a new array with dim=2
     */
    public double[] getXYArray() {
        if (dimension == 2) {
            return coords;
        }
        // this should never run, but its here for the future...
        int n = size();
        double[] result = new double[n * 2];
        for (int t = 0; t < n; t++) {
            result[t * 2] = getOrdinate(t, 0);
            result[t * 2 + 1] = getOrdinate(t, 1);
        }
        return result;
    }

    public double[] getOrdinateArray(int dimensions) {
        if (dimensions == this.dimension) {
            return coords;
        }

        int n = size();
        double[] result = new double[n * dimensions];
        int minDimensions = Math.min(dimensions, this.dimension);
        for (int t = 0; t < n; t++) {
            for (int d = 0; d < minDimensions; d++) {
                result[t * 2 + d] = getOrdinate(t, d);
            }
        }
        return result;
    }

    /**
     * Clones the specified geometry using {@link LiteCoordinateSequence} in the result, with the
     * specified number of dimensions
     */
    public static Geometry cloneGeometry(Geometry geom, int dimension) {
        if (dimension < 2) {
            throw new IllegalArgumentException("Invalid dimension value, must be >= 2");
        }

        if (geom == null) return null;

        if (geom instanceof LineString) {
            return cloneGeometry((LineString) geom, dimension);
        } else if (geom instanceof Polygon) {
            return cloneGeometry((Polygon) geom, dimension);
        } else if (geom instanceof Point) {
            return cloneGeometry((Point) geom, dimension);
        } else {
            return cloneGeometry((GeometryCollection) geom, dimension);
        }
    }

    /** Clones the specified geometry using {@link LiteCoordinateSequence} in the result */
    public static final Geometry cloneGeometry(Geometry geom) {
        return cloneGeometry(geom, 2);
    }
    /** changes this to a new CSF -- more efficient than the JTS way */
    private static final Geometry cloneGeometry(Polygon geom, int dimension) {
        LinearRing lr = (LinearRing) cloneGeometry((LinearRing) geom.getExteriorRing(), dimension);
        LinearRing[] rings = new LinearRing[geom.getNumInteriorRing()];
        for (int t = 0; t < rings.length; t++) {
            rings[t] = (LinearRing) cloneGeometry((LinearRing) geom.getInteriorRingN(t), dimension);
        }
        return geomFac.createPolygon(lr, rings);
    }

    private static final Geometry cloneGeometry(Point geom, int dimension) {
        return geomFac.createPoint(
                new LiteCoordinateSequence(geom.getCoordinateSequence(), dimension));
    }

    private static final Geometry cloneGeometry(LineString geom, int dimension) {
        if (geom instanceof SingleCurvedGeometry<?>) {
            SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) geom;
            double[] controlPoints = curved.getControlPoints();
            double[] clonedPoints = new double[controlPoints.length];
            System.arraycopy(controlPoints, 0, clonedPoints, 0, controlPoints.length);

            return new CircularString(clonedPoints, geomFac, curved.getTolerance());
        } else if (geom instanceof CompoundCurvedGeometry<?>) {
            CompoundCurvedGeometry<?> curved = (CompoundCurvedGeometry<?>) geom;
            List<LineString> components = curved.getComponents();
            List<LineString> clonedComponents = new ArrayList<>(components.size());
            for (LineString ls : components) {
                LineString cloned = (LineString) cloneGeometry(ls, dimension);
                clonedComponents.add(cloned);
            }
            return new CompoundCurve(clonedComponents, geomFac, dimension);
        } else {
            return geomFac.createLineString(
                    new LiteCoordinateSequence(geom.getCoordinateSequence(), dimension));
        }
    }

    private static final Geometry cloneGeometry(LinearRing geom, int dimension) {
        if (geom instanceof SingleCurvedGeometry<?>) {
            SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) geom;
            double[] controlPoints = curved.getControlPoints();
            double[] clonedPoints = new double[controlPoints.length];
            System.arraycopy(controlPoints, 0, clonedPoints, 0, controlPoints.length);

            return new CircularRing(clonedPoints, geomFac, curved.getTolerance());
        } else if (geom instanceof CompoundCurvedGeometry<?>) {
            CompoundCurvedGeometry<?> curved = (CompoundCurvedGeometry<?>) geom;
            List<LineString> components = curved.getComponents();
            List<LineString> clonedComponents = new ArrayList<>(components.size());
            for (LineString ls : components) {
                LineString cloned = (LineString) cloneGeometry(ls, dimension);
                clonedComponents.add(cloned);
            }
            return new CompoundRing(clonedComponents, geomFac, dimension);
        } else {
            return geomFac.createLinearRing(
                    new LiteCoordinateSequence(geom.getCoordinateSequence(), dimension));
        }
    }

    private static final Geometry cloneGeometry(GeometryCollection geom, int dimension) {
        if (geom.getNumGeometries() == 0) {
            Geometry[] gs = new Geometry[0];
            return geomFac.createGeometryCollection(gs);
        }

        ArrayList gs = new ArrayList(geom.getNumGeometries());
        int n = geom.getNumGeometries();
        for (int t = 0; t < n; t++) {
            gs.add(cloneGeometry(geom.getGeometryN(t), dimension));
        }
        return geomFac.buildGeometry(gs);
    }

    public String toString() {
        if (size > 0) {
            StringBuffer strBuf = new StringBuffer((9 * dimension) * size);
            strBuf.append('(');
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < dimension; j++) {
                    strBuf.append(coords[i * dimension + j]);
                    if (j < dimension - 1) {
                        strBuf.append(" ");
                    }
                }
                if (i < size - 1) {
                    strBuf.append(", ");
                }
            }
            strBuf.append(')');
            return strBuf.toString();
        } else {
            return "()";
        }
    }
}
