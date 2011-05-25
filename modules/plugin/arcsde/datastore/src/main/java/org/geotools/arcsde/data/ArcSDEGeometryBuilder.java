/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.util.logging.Logging;

import com.esri.sde.sdk.client.SDEPoint;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeShape;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Creates propper JTS Geometry objects from <code>SeShape</code> objects and viceversa.
 * <p>
 * <code>SeShape</code>'s are gathered from an <code>SeRow</code> ArcSDE API's result object and
 * holds it's geometry attributes as a three dimensional array of <code>double</code> primitives as
 * explained bellow.
 * </p>
 * <p>
 * By this way, we avoid the creation of ArcSDE's java implementation of OGC geometries for later
 * translation to JTS, avoiding too the dependency on the ArcSDE native library wich the geometry
 * package of the ArcSDE Java API depends on.
 * </p>
 * <p>
 * Given <code>double [][][]coords</code> the meaning of this array is as follow:
 * <ul>
 * <li>coords.length reprsents the number of geometries this geometry is composed of. In deed, this
 * only applies for multipolygon geometries, for all other geometry types, this will be allways
 * <code>1</code></li>
 * <li>coords[n] holds the coordinate arrays of the n'th geometry this geometry is composed of.
 * Except for multipolygons, this will allways be <code>coords[0]</code>.</li>
 * <li>coords[n][m] holds the coordinates array for a given geometry. (i.e. [0][m] for a
 * multilinestring or [2][m] for a multipolygon composed of 3 polygons)</li>
 * <li>coords[n][m][l] holds the {x1, y1, x2, y2, ...,Xn, Yn} coordinates for a given geometry part</li>
 * </ul>
 * </p>
 * <p>
 * This abstract class will use specialized subclass for constructing the propper geometry type
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcSDEGeometryBuilder.java $
 * @version $Id$
 */
public abstract class ArcSDEGeometryBuilder {

    private static final Logger LOGGER = Logging.getLogger(ArcSDEGeometryBuilder.class.getName());

    /** lookup specialized geometry builders classes by it's geometry type */
    private static final Map<Class<?>, ArcSDEGeometryBuilder> builders = new HashMap<Class<?>, ArcSDEGeometryBuilder>();

    /** Look up "empty" geometry instances based on geometry class */
    private static final Map<Class<?>, Geometry> nullGeometries = new HashMap<Class<?>, Geometry>();

    static {
        builders.put(Geometry.class, GenericGeometryBuilder.getInstance());
        builders.put(GeometryCollection.class, GenericGeometryBuilder.getInstance());
        builders.put(Point.class, PointBuilder.getInstance());
        builders.put(MultiPoint.class, MultiPointBuilder.getInstance());
        builders.put(LineString.class, LineStringBuilder.getInstance());
        builders.put(MultiLineString.class, MultiLineStringBuilder.getInstance());
        builders.put(Polygon.class, PolygonBuilder.getInstance());
        builders.put(MultiPolygon.class, MultiPolygonBuilder.getInstance());

        nullGeometries.put(Geometry.class, new GenericGeometryBuilder().getEmpty());
        nullGeometries.put(Point.class, new PointBuilder().getEmpty());
        nullGeometries.put(MultiPoint.class, new MultiPointBuilder().getEmpty());
        nullGeometries.put(LineString.class, new LineStringBuilder().getEmpty());
        nullGeometries.put(MultiLineString.class, new MultiLineStringBuilder().getEmpty());
        nullGeometries.put(Polygon.class, new PolygonBuilder().getEmpty());
        nullGeometries.put(MultiPolygon.class, new MultiPolygonBuilder().getEmpty());
    }

    /**
     * Private empty constructor to obligate using this class as factory.
     */
    private ArcSDEGeometryBuilder() {
        // intentionally blank
    }

    /**
     * Takes an ArcSDE's <code>SeShape</code> and builds a JTS Geometry. The geometry type
     * constructed depends on this <code>ArcSDEGeometryBuilder</code> specialized subclass
     * 
     * @param shape
     *            the ESRI's ArcSDE java api shape upon wich to create the new JTS geometry
     * @param geometryFactory
     * @return the type of JTS Geometry this subclass instance is specialized for or an empty
     *         geometry of the same class if <code>shape.isNil()</code>
     * @throws SeException
     *             if it occurs fetching the coordinates array from <code>shape</code>
     * @throws DataSourceException
     *             if the <code>com.vividsolutions.jts.geom.GeometryFactory</code> this builder is
     *             backed by can't create the <code>com.vividsolutions.jts.geom.Geometry</code> with
     *             the <code>com.vividsolutions.jts.geom.Coordinate[]</code> provided by
     *             <code>newGeometry</code>
     */
    public Geometry construct(final SeShape shape, final GeometryFactory geometryFactory)
            throws SeException, DataSourceException {
        if (shape == null || shape.isNil()) {
            return getEmpty();
        }
        double[][][] allCoords = shape.getAllCoords();
        return newGeometry(allCoords, geometryFactory);
    }

    /**
     * Creates the ArcSDE Java API representation of a <code>Geometry</code> object in its shape
     * format, suitable to filter expressions as the SDE API expects
     * 
     * @param geometry
     *            the JTS Geometry object to get the SDE representation from
     * @param seSrs
     *            Coordinate Reference System of the underlying <code>SeLayer</code> object for wich
     *            the <code>SeShape</code> is constructed.
     * @return the <code>SeShape</code> representation of passed <code>Geometry</code>
     * @throws ArcSDEGeometryBuildingException
     */
    public final SeShape constructShape(final Geometry geometry, SeCoordinateReference seSrs)
            throws ArcSdeException {
        if (geometry == null) {
            return null;
        }
        SeShape shape = null;

        try {
            shape = new SeShape(seSrs);
        } catch (SeException ex) {
            ArcSdeException e = new ArcSdeException("Can't create SeShape with SeCrs " + seSrs, ex);
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw e;
        }

        if (geometry.isEmpty()) {
            return shape;
        }

        // REVISIT: this may be worth considering. If not, at least shape.generateFromWKB
        // final String wkt = geometry.toText();
        // try {
        // shape.generateFromText(wkt);
        // } catch (SeException e) {
        // ArcSdeException sdeEx = new ArcSdeException("Can't generate SeShape from " + geometry
        // + "\n", e);
        // LOGGER.log(Level.WARNING, sdeEx.getMessage());
        // throw sdeEx;
        // }

        int numParts;
        GeometryCollection gcol = null;

        if (geometry instanceof GeometryCollection) {
            gcol = (GeometryCollection) geometry;
        } else {
            Geometry[] geoms = { geometry };
            gcol = new GeometryFactory().createGeometryCollection(geoms);
        }

        List<SDEPoint> allPoints = new ArrayList<SDEPoint>();
        numParts = gcol.getNumGeometries();

        int[] partOffsets = new int[numParts];
        Geometry geom;
        Coordinate[] coords;
        Coordinate c;

        for (int currGeom = 0; currGeom < numParts; currGeom++) {
            partOffsets[currGeom] = allPoints.size();
            geom = gcol.getGeometryN(currGeom);

            coords = geom.getCoordinates();

            for (int i = 0; i < coords.length; i++) {
                c = coords[i];
                allPoints.add(new SDEPoint(c.x, c.y));
            }
        }

        SDEPoint[] points = new SDEPoint[allPoints.size()];
        allPoints.toArray(points);

        try {
            if (geometry instanceof Point || gcol instanceof MultiPoint) {
                shape.generatePoint(points.length, points);
            } else if (geometry instanceof LineString || geometry instanceof MultiLineString) {
                shape.generateLine(points.length, numParts, partOffsets, points);
            } else {
                shape.generatePolygon(points.length, numParts, partOffsets, points);
            }
        } catch (SeException e) {
            ArcSdeException sdeEx = new ArcSdeException("Can't generate SeShape from " + geometry
                    + "\n", e);
            LOGGER.log(Level.WARNING, sdeEx.getMessage());
            throw sdeEx;
        }

        return shape;
    }

    /**
     * Builds a JTS Geometry who't type is given by the <code>ArcSDEGeometryBuilder</code> subclass
     * instance specialization that implements it
     * 
     * @param coords
     *            <code>SeShape</code>'s coordinate array to build the geometry from
     * @param geometryFactory
     * @return the JST form of the passed geometry coordinates
     * @throws DataSourceException
     *             if an error occurs while creating the JTS Geometry
     */
    protected abstract Geometry newGeometry(final double[][][] coords,
            final GeometryFactory geometryFactory) throws DataSourceException;

    /**
     * returns an empty JTS geometry who's type is given by the <code>ArcSDEGeometryBuilder</code>
     * subclass instance specialization that implements it.
     * <p>
     * this method is called in case that <code>SeShape.isNil() == true</code>
     * </p>
     * 
     * @return an empty JTS geometry
     * @throws UnsupportedOperationException
     */
    protected Geometry getEmpty() {
        throw new UnsupportedOperationException(
                "this method sholdn't be called directly, it's intended pourpose"
                        + " is to be implemented by subclasses so they provide propper "
                        + " null Geometries");
    }

    /**
     * Builds an array of JTS <code>Coordinate</code> instances that's geometrically equals to the
     * <code>SeShape</code> single coordinates array passed as argument.
     * 
     * @param coordList
     *            array of coordinates of a single shape part to build a <code>Coordinate</code>
     *            from
     * @return a geometrically equal to <code>coordList</code> array of <code>Coordinate</code>
     *         instances
     */
    protected final CoordinateSequence toCoords(double[] coordList,
            final CoordinateSequenceFactory csFact) {

        final int dimension = 2;

        CoordinateSequence cs;

        if (csFact instanceof LiteCoordinateSequenceFactory) {
            cs = ((LiteCoordinateSequenceFactory) csFact).create(coordList, dimension);
        } else {
            final int nCoords = coordList.length / dimension;
            cs = csFact.create(nCoords, dimension);
            for (int coordN = 0; coordN < nCoords; coordN++) {
                cs.setOrdinate(coordN, 0, coordList[dimension * coordN]);
                cs.setOrdinate(coordN, 1, coordList[dimension * coordN + 1]);
            }
        }
        return cs;
    }

    protected SDEPoint[] toPointsArray(Coordinate[] coords) {
        int nCoords = coords.length;

        SDEPoint[] points = new SDEPoint[nCoords];

        Coordinate c;

        for (int i = 0; i < nCoords; i++) {
            c = coords[i];

            points[i] = new SDEPoint(c.x, c.y);
        }

        return points;
    }

    /**
     * Factory method that returns an instance of <code>ArcSDEGeometryBuilder</code> specialized in
     * contructing JTS geometries of the JTS Geometry class passed as argument. Note that
     * <code>jtsGeometryClass</code> must be one of the supported concrete JTS Geometry classes.
     * 
     * @param jtsGeometryClass
     * @throws IllegalArgumentException
     *             if <code>jtsGeometryClass</code> is not a concrete JTS <code>Geometry</code>
     *             class (like <code>com.vividsolutions.jts.geom.MultiPoint.class</code> i.e.)
     */
    public static ArcSDEGeometryBuilder builderFor(Class<? extends Geometry> jtsGeometryClass)
            throws IllegalArgumentException {
        ArcSDEGeometryBuilder builder = (ArcSDEGeometryBuilder) builders.get(jtsGeometryClass);

        if (builder == null) {
            String msg = "no geometry builder is defined to construct " + jtsGeometryClass
                    + " instances.";
            throw new IllegalArgumentException(msg);
        }

        return builder;
    }

    /**
     * Create an empty geometry for the indicated class
     * 
     */
    public static Geometry defaultValueFor(Class<?> geoClass) {
        if (geoClass == null) {
            throw new NullPointerException("got null geometry class");
        }

        Geometry emptyGeom = (Geometry) nullGeometries.get(geoClass);

        return emptyGeom;
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> which can create any type of JTS geometry from
     * <code>SeShape</code>'s and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class GenericGeometryBuilder extends ArcSDEGeometryBuilder {

        private static Geometry EMPTY;

        /** singleton for generic geometry building */
        private static final ArcSDEGeometryBuilder instance = new GenericGeometryBuilder();

        /**
         * Returns an instance of this geometry builder. Currently implemented as a singleton since
         * it is completely thread safe.
         * 
         * @return the <code>GenericGeometryBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createGeometryCollection(new Geometry[] {});
            }
            return EMPTY;
        }

        /**
         * @param shape
         *            the shape to create its JTS geometry equivalent. Can't be null.
         * @see ArcSDEGeometryBuilder#construct(SeShape)
         */
        @Override
        public Geometry construct(SeShape shape, final GeometryFactory geometryFactory)
                throws SeException, DataSourceException {
            if (shape == null || shape.isNil()) {
                return getEmpty();
            }
            Class<? extends Geometry> realGeomClass = ArcSDEAdapter
                    .getGeometryTypeFromSeShape(shape);
            if (realGeomClass == null) {
                return null;
            }
            ArcSDEGeometryBuilder realBuilder = builderFor(realGeomClass);

            return realBuilder.construct(shape, geometryFactory);
        }

        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            throw new UnsupportedOperationException("This method should not "
                    + "be called for this builder. It should be mapped to the "
                    + "one capable of constructing the actual geometry type");
        }
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> specialized in creating JTS <code>Point</code> s from
     * <code>SeShape</code> points and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class PointBuilder extends ArcSDEGeometryBuilder {
        /** the empty point singleton */
        private static Geometry EMPTY;

        /** singleton for point building */
        private static final ArcSDEGeometryBuilder instance = new PointBuilder();

        /**
         * Returns an instance of this geometry builder for Point geometries. Currently implemented
         * as a singleton since it is completely thread safe.
         * 
         * @return the <code>PointBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createPoint((Coordinate) null);
            }

            return EMPTY;
        }

        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            final double x = coords[0][0][0];
            final double y = coords[0][0][1];
            return geometryFactory.createPoint(new Coordinate(x, y));
        }
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> specialized in creating JTS <code>MultiPoint</code> s from
     * <code>SeShape</code> multipoints and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class MultiPointBuilder extends ArcSDEGeometryBuilder {
        /** the empty multipoint singleton */
        private static Geometry EMPTY;

        /** singleton for multipoint building */
        private static final ArcSDEGeometryBuilder instance = new MultiPointBuilder();

        /**
         * Returns an instance of this geometry builder for MultiPoint geometries. Currently
         * implemented as a singleton since it is completely thread safe.
         * 
         * @return the <code>MultiPointBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createMultiPoint((Point[]) null);
            }

            return EMPTY;
        }

        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            int nPoints = coords.length;

            Coordinate[] points = new Coordinate[nPoints];

            for (int i = 0; i < nPoints; i++) {
                double x = coords[i][0][0];
                double y = coords[i][0][1];
                points[i] = new Coordinate(x, y);
            }

            return geometryFactory.createMultiPoint(points);
        }
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> specialized in creating JTS <code>LineString</code> s from
     * <code>SeShape</code> linestring and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class LineStringBuilder extends ArcSDEGeometryBuilder {
        /** the empty linestring singleton */
        private static Geometry EMPTY;

        /** singleton for linestring building */
        private static final ArcSDEGeometryBuilder instance = new LineStringBuilder();

        /**
         * Returns an instance of this geometry builder for LineString geometries. Currently
         * implemented as a singleton since it is completely thread safe.
         * 
         * @return the <code>LineStringBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createLineString((Coordinate[]) null);
            }

            return EMPTY;
        }

        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            return constructLineString(coords[0][0], geometryFactory);
        }

        protected final LineString constructLineString(final double[] linearCoords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            LineString ls = null;

            CoordinateSequence coords = toCoords(linearCoords,
                    geometryFactory.getCoordinateSequenceFactory());

            ls = geometryFactory.createLineString(coords);

            return ls;
        }
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> specialized in creating JTS <code>MultiLineString</code> s
     * from <code>SeShape</code> multilinestrings and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class MultiLineStringBuilder extends LineStringBuilder {
        /** the empty multilinestring singleton */
        private static Geometry EMPTY;

        /** singleton for multilinestring building */
        private static final ArcSDEGeometryBuilder instance = new MultiLineStringBuilder();

        /**
         * Returns an instance of this geometry builder for MultiLineString geometries. Currently
         * implemented as a singleton since it is completely thread safe.
         * 
         * @return the <code>MultiLineStringBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createMultiLineString(null);
            }

            return EMPTY;
        }

        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            MultiLineString mls = null;

            LineString[] lineStrings = null;

            int nLines = coords.length;

            lineStrings = new LineString[nLines];

            for (int i = 0; i < nLines; i++) {
                lineStrings[i] = constructLineString(coords[i][0], geometryFactory);
            }

            mls = geometryFactory.createMultiLineString(lineStrings);

            return mls;
        }
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> specialized in creating JTS <code>Polygon</code> s from
     * <code>SeShape</code> polygon and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class PolygonBuilder extends ArcSDEGeometryBuilder {
        /** the empty polygon singleton */
        private static Geometry EMPTY;

        /** singleton for polygon building */
        private static final ArcSDEGeometryBuilder instance = new PolygonBuilder();

        /**
         * Returns an instance of this geometry builder for Polygon geometries. Currently
         * implemented as a singleton since it is completely thread safe.
         * 
         * @return the <code>PolygonBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createPolygon(null, null);
            }

            return EMPTY;
        }

        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            // /////
            /*
             * for (int i = 0; i < coords.length; i++) { for (int j = 0; j < coords[i].length; j++)
             * { double[] ds = coords[i][j]; //System.out.println("coords[" + i + "][" + j + "]=" +
             * Arrays.toString(ds)); } } //System.out.println("-----");
             */
            // ///////
            double[] shell = coords[0][0];
            int nParts = coords[0].length;
            int nHoles = nParts - 1;
            double[][] holes = new double[nHoles][1];
            for (int i = 0; i < nHoles; i++) {
                holes[i] = coords[0][i + 1];
            }
            return buildPolygon(shell, holes, geometryFactory);
        }

        protected final Polygon buildPolygon(final double[] shellCoords, final double[][] holes,
                final GeometryFactory geometryFactory) {
            Polygon p = null;

            final CoordinateSequenceFactory sequenceFactory = geometryFactory
                    .getCoordinateSequenceFactory();
            final CoordinateSequence coords = toCoords(shellCoords, sequenceFactory);
            final LinearRing shell = geometryFactory.createLinearRing(coords);
            final int nHoles = holes.length;

            LinearRing[] polygonHoles = new LinearRing[nHoles];

            if (nHoles > 0) {
                for (int i = 0; i < nHoles; i++) {
                    double hole[] = holes[i];
                    polygonHoles[i] = geometryFactory.createLinearRing(toCoords(hole,
                            sequenceFactory));
                }
            }

            p = geometryFactory.createPolygon(shell, polygonHoles);

            return p;
        }

        @Deprecated
        protected Polygon buildPolygon(final double[][] parts, final GeometryFactory geometryFactory) {
            Polygon p = null;

            double[] linearCoordArray = parts[0];

            int nHoles = parts.length - 1;

            final CoordinateSequenceFactory coordinateSequenceFactory = geometryFactory
                    .getCoordinateSequenceFactory();
            LinearRing shell = geometryFactory.createLinearRing(toCoords(linearCoordArray,
                    coordinateSequenceFactory));

            LinearRing[] holes = new LinearRing[nHoles];

            if (nHoles > 0) {
                for (int i = 0; i < nHoles; i++) {
                    linearCoordArray = parts[i + 1];

                    holes[i] = geometryFactory.createLinearRing(toCoords(linearCoordArray,
                            coordinateSequenceFactory));
                }
            }

            p = geometryFactory.createPolygon(shell, holes);

            return p;
        }
    }

    /**
     * <code>ArcSDEGeometryBuilder</code> specialized in creating JTS <code>MultiPolygon</code> s
     * from <code>SeShape</code> multipolygons and viceversa
     * 
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class MultiPolygonBuilder extends PolygonBuilder {
        /** the empty multipolygon singleton */
        private static Geometry EMPTY;

        /** singleton for multipolygon building */
        private static final ArcSDEGeometryBuilder instance = new MultiPolygonBuilder();

        /**
         * Returns an instance of this geometry builder for MultiPolygon geometries. Currently
         * implemented as a singleton since it is completely thread safe.
         * 
         * @return the <code>PointBuilder</code> singleton.
         */
        public static ArcSDEGeometryBuilder getInstance() {
            return instance;
        }

        @Override
        protected Geometry getEmpty() {
            if (EMPTY == null) {
                EMPTY = new GeometryFactory().createMultiPolygon(null);
            }

            return EMPTY;
        }

        /**
         * 
         * @param coords
         *            the SeShape's multipolygon coordinates array
         * @return a <code>MultiPolygon</code> constructed based on the SDE shape, or the empty
         *         geometry if the <code>shape == null ||
         *         shape.isNil()</code>
         * @throws DataSourceException
         *             if it is not possible to obtain the shape's coordinate arrays or an exception
         *             occurs while building the Geometry
         */
        @Override
        protected Geometry newGeometry(final double[][][] coords,
                final GeometryFactory geometryFactory) throws DataSourceException {
            Polygon[] polys = null;

            int numPolys = coords.length;

            polys = new Polygon[numPolys];

            for (int i = 0; i < numPolys; i++) {
                try {
                    polys[i] = buildPolygon(coords[i], geometryFactory);
                } catch (Exception ex) {
                    throw new DataSourceException(ex.getMessage(), ex);
                }
            }

            MultiPolygon multiPoly = geometryFactory.createMultiPolygon(polys);

            return multiPoly;
        }
    }
}
