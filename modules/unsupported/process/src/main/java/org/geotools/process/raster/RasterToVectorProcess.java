/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.process.raster;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.AbstractProcess;
import org.geotools.referencing.CRS;
import org.geotools.util.NullProgressListener;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.SubProgressListener;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.algorithm.InteriorPointArea;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

/**
 * Vectorizes discrete regions of uniform value in the specified band of a GridCoverage2D
 * object. Data are treated as double values regardless of the data type of the input grid
 * coverage.
 * <p>
 * Example of use via the GeoTools process API
 * 
 * <pre><code>
 * GridCoverage2D cov = ...
 *
 * final Map&lt;String, Object> params = new HashMap&lt;String, Object>();
 * params.put(RasterToVectorFactory.RASTER.key, cov);
 * params.put(RasterToVectorFactory.BAND.key, Integer.valueOf(0));
 * params.put(RasterToVectorFactory.BOUNDS.key, env);
 * params.put(RasterToVectorFactory.OUTSIDE.key, Collections.singleton(0.0d));
 * params.put(RasterToVectorFactory.INSIDE_EDGES.key, Boolean.TRUE);
 *
 * final Process r2v = Processors.createProcess(new NameImpl(ProcessFactory.GT_NAMESPACE, "RasterToVectorProcess"));
 *
 * // run the process on a thread other than the event-dispatch thread
 * // (here we use SwingWorker to accomplish this)
 * SwingWorker worker = new SwingWorker&lt;Map&lt;String, Object>, Void>() {
 *     protected Map&lt;String, Object> doInBackground() throws Exception {
 *         ProgressWindow pw = new ProgressWindow(null);
 *         pw.setTitle("Vectorizing coverage");
 *         return r2v.execute(params, pw);
 *     }
 *
 *     protected void done() {
 *         Map&lt;String, Object> vectorizingResults = null;
 *         try {
 *             vectorizingResults = get();
 *         } catch (Exception ignore) {}
 *
 *         if (vectorizingResults != null) {
 *             FeatureCollection&lt;SimpleFeatureType, SimpleFeature> fc =
 *                 (FeatureCollection&lt;SimpleFeatureType, SimpleFeature>) vectorizingResults.get(
 *                         RasterToVectorFactory.RESULT_FEATURES.key);
 *
 *             // do something with features...
 *         }
 *     }
 * };
 *
 * worker.execute();
 * </code></pre>
 *
 * Example of "manual" use with the static helper method:
 * <pre><code>
 * GridCoverage2D cov = ...
 * int band = ...
 * Envelope bounds = ...
 * List&lt;Double> outsideValues = ...
 * boolean insideEdges = ...
 *
 * FeatureCollection&lt;SimpleFeatureType,SimpleFeature> features =
 *     RasterToVectorProcess.process(cov, band, bounds, outsideValues, insideEdges, null);
 * </code></pre>
 * 
 * @author Jody Garnett
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class RasterToVectorProcess extends AbstractProcess {

    /* the JTS object that does all the topological work for us */
    private Polygonizer polygonizer;

    /* the coverage presently being processed */
    private GridCoverage2D coverage;

    /**
     * The transform to use to convert pixel lower right corner positions to real world coordinates
     */
    private MathTransform2D transformLR;

    /* Image bounds for vectorizing */
    private Rectangle imageBounds;

    /* Input coverage cell width in the X and Y directions */
    private double cellWidthX;
    private double cellWidthY;

    // positions in curData matrix just to avoid confusion
    private static final int TL = 0;
    private static final int TR = 1;
    private static final int BL = 2;
    private static final int BR = 3;

    // these are used to identify the orientation of corner touches
    // between possibly separate polygons with the same value
    private static final int TL_BR = 4;
    private static final int TR_BL = 5;
    private static final int CROSS = 6;

    // Precision of comparison in the function different(a, b)
    private static final double EPSILON = 1.0e-8d;

    // Set of values that indicate 'outside' or 'no data' areas in the raster
    private SortedSet<Double> outside;

    // Proxy value used when inside edges are not being vectorized
    private Double inside = null;

    // Flag value used to code polygons when inside edges are not
    // being vectorized
    private static final int INSIDE_FLAG_VALUE = 1;

    /*
     * array of Coor objects that store end-points of vertical lines under construction
     */
    private Map<Integer, LineSegment> vertLines;

    /*
     * end-points of horizontal line under construction
     */
    private LineSegment horizLine;

    /*
     * collection of line strings on the boundary of raster regions
     */
    private List<LineString> lines;
    
    /*
     * Factory for construction of JTS Geometry objects
     */
    private GeometryFactory geomFactory;

    /*
     * list of corner touches between possibly separate polygons of
     * the same value. Each Coordinate has x:y = col:row and z set
     * to either TL_BR or TR_BL to indicate the orientation of the
     * corner touch.
     */
    List<Coordinate> cornerTouches;

    /*
     * input image
     */
    private RenderedImage image;
    
    /**
     * Package-access constructor. Client code should use the public
     * {@linkplain RasterToVectorFactory#create } method
     * @param factory
     */
    RasterToVectorProcess(ProcessFactory factory) {
        super(factory);
    }

    /**
     * Run the raster to vector process.
     * The returned {@code Map} will contain a single object: the FeatureCollection
     * of vector polygons which can be retrieved as follows
     * <pre><code>
     * FeatureCollection&lt;SimpleFeatureType, SimpleFeature> features =
     *     (FeatureCollection&lt;SimpleFeatureType, SimpleFeature>) resultsMap.get(
     *         RasterToVectorFactory.RESULT_FEATURES.key);
     * </code></pre>
     *
     * @param input a map of input parameters (see {@linkplain RasterToVectorFactory} for details)
     *
     * @param monitor an optional {@code ProgressListener} (may be {@code null})
     *
     * @return a {@code Map} containing the vectorized features
     */
    public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor)
            throws ProcessException {

        Object o;

        GridCoverage2D cov = (GridCoverage2D) input.get(RasterToVectorFactory.RASTER.key);

        int band = 0;
        o = input.get(RasterToVectorFactory.BAND.key);
        if (o != null) {
            band = (Integer) o;
        }

        Envelope bounds = (Envelope) input.get(RasterToVectorFactory.BOUNDS.key);

        Collection<Double> outsideValues = (Collection<Double>) input.get(
                RasterToVectorFactory.OUTSIDE.key);
        if(outsideValues == null) {
        	outsideValues = Collections.emptyList();
        }

        boolean insideEdges = true;
        o = input.get(RasterToVectorFactory.INSIDE_EDGES.key);
        if (o != null) {
            insideEdges = (Boolean) o;
        }

        FeatureCollection features = convert(cov, band, bounds, outsideValues, insideEdges, monitor);

        Map<String, Object> results = new HashMap<String, Object>();
        results.put(RasterToVectorFactory.RESULT_FEATURES.key, features);
        return results;
    }
    
    /**
     * A static helper method that can be called directy to run the process.
     * <p>
     * The process interface is useful for advertising functionality to
     * dynamic applications, but for 'hands on' coding this method is much more
     * convenient than working via {@linkplain org.geotools.process.Process#execute }.
     *
     * @param cov the input coverage
     * @param band the index of the band to be vectorized
     * @param bounds bounds of the area (in world coordinates) to vectorize; if {@code null}
     *        the whole coverage
     * @param outside a collection of one or more values which represent 'outside' or no data
     *        (may be {@code null} or empty)
     * @param insideEdges whether to vectorize inside edges (those separating grid regions
     *        with non-outside values)
     * @param monitor an optional ProgressListener (may be {@code null})
     *
     * @return a FeatureCollection containing simple polygon features
     */
    public static SimpleFeatureCollection process(
            GridCoverage2D cov,
            int band,
            Envelope bounds,
            Collection<Double> outsideValues,
            boolean insideEdges,
            ProgressListener monitor) throws ProcessException {

        RasterToVectorFactory factory = new RasterToVectorFactory();
        RasterToVectorProcess process =  factory.create();
        return process.convert(cov, band, bounds, outsideValues, insideEdges, monitor);
    }

    /**
     * Convert the input raster coverage to vector polygons. 
     *
     * @param cov the input coverage
     * @param band the index of the band to be vectorized
     * @param bounds of the area to vectorize in world coords (null means whole coverage)
     * @param outside a collection of one or more values which represent 'outside' or no data
     *        (may be {@code null} or empty)
     * @param insideEdges whether to vectorize inside edges (those separating grid regions
     *        with non-outside values)
     * @param monitor a progress listener (may be {@code null})
     *
     * @return a FeatureCollection containing simple polygon features
     */
    private SimpleFeatureCollection convert(
            GridCoverage2D cov,
            int band,
            Envelope bounds,
            Collection<Double> outsideValues,
            boolean insideEdges,
            ProgressListener monitor) throws ProcessException {

        if (monitor == null) {
            monitor = new NullProgressListener();
        } else {
            monitor.started();
        }

        ReferencedEnvelope workingBounds = null;

        if (bounds == null) {
            workingBounds = new ReferencedEnvelope(cov.getEnvelope());

        } else {
            CoordinateReferenceSystem sourceCRS = bounds.getCoordinateReferenceSystem();
            CoordinateReferenceSystem targetCRS = cov.getCoordinateReferenceSystem();
            if (sourceCRS != null) {
                if (!CRS.equalsIgnoreMetadata(sourceCRS, targetCRS)) {
                    throw new ProcessException("CRS of bounds must match that of the coverage");
                }
            }

            ReferencedEnvelope inputBounds = new ReferencedEnvelope(bounds);
            ReferencedEnvelope covBounds = new ReferencedEnvelope(cov.getEnvelope());
            workingBounds = new ReferencedEnvelope(covBounds.intersection(inputBounds), targetCRS);
            if (workingBounds == null || workingBounds.isEmpty()) {
                throw new ProcessException("Specified bounds lie wholly outside of coverage");
            }
        }


        try {
            monitor.setTask(new SimpleInternationalString("Initializing"));
            initialize(cov, workingBounds, new SubProgressListener(monitor, 10));

            monitor.setTask(new SimpleInternationalString("Vectorizing"));
            vectorizeAndCollectBoundaries(band, outsideValues, insideEdges, new SubProgressListener(monitor, 70));

            /***********************************************************
             * Assemble the LineStringss into Polygons, and create the
             * collection of features to return
             ***********************************************************/
            SimpleFeatureType schema = RasterToVectorFactory.getSchema(cov
                    .getCoordinateReferenceSystem());

            monitor.setTask(new SimpleInternationalString("Creating polygon features"));
            SimpleFeatureCollection features = 
                    assembleFeatures(cov, band, insideEdges, schema, new SubProgressListener(monitor, 20));

            return features;

        } catch (Exception ex) {
            throw new ProcessException(ex);

        } finally {
            monitor.complete();
        }
    }

    /**
     * Assemble a feature collection by polygonizing the boundary segments that
     * have been collected by the vectorizing algorithm.
     * 
     * @param grid the input grid coverage
     * @param band the band containing the data to vectorize
     * @param insideEdges whether to vectorize inside edges (those separating grid regions
     *        with non-outside values)
     * @param type feature type
     * @param monitor a progress listener (may be {@code null})
     * @return a new FeatureCollection containing the boundary polygons
     */
    private SimpleFeatureCollection assembleFeatures(GridCoverage2D grid, int band,
            boolean insideEdges, SimpleFeatureType type, ProgressListener monitor) {
        if (monitor == null) {
            monitor = new NullProgressListener();
        }

        SimpleFeatureCollection features = FeatureCollections.newCollection();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);

        Point2D p = new Point2D.Double();
        double[] bandData = new double[grid.getNumSampleDimensions()];

        polygonizer.add(lines);
        Collection polygons = polygonizer.getPolygons();
        final int size = polygons.size();
        try {
            float progressScale = 100.0f / size;
            monitor.started();

            int index = 0;
            for (Iterator i = polygons.iterator(); i.hasNext(); index++) {

                if (monitor.isCanceled()) {
                    throw new CancellationException();
                }
                monitor.progress(progressScale * index);

                Polygon poly = (Polygon) i.next();
                InteriorPointArea ipa = new InteriorPointArea(poly);
                Coordinate c = ipa.getInteriorPoint();
                Point insidePt = geomFactory.createPoint(c);

                if (!poly.contains(insidePt)) {
                    // try another method to generate an interior point
                    boolean found = false;
                    for (Coordinate ringC : poly.getExteriorRing().getCoordinates()) {
                        c.x = ringC.x + cellWidthX / 2;
                        c.y = ringC.y;
                        insidePt = geomFactory.createPoint(c);
                        if (poly.contains(insidePt)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalStateException("Can't locate interior point for polygon");
                    }
                }

                p.setLocation(c.x, c.y);
                bandData = grid.evaluate(p, bandData);

                if (!isOutside(bandData[band])) {
                    builder.add(poly);

                    if (insideEdges) {
                        builder.add(bandData[band]);
                    } else {
                        builder.add(INSIDE_FLAG_VALUE);
                    }
                    features.add(builder.buildFeature(null));
                }
            }
            return features;
        } finally {
            monitor.complete();
        }
    }

    /**
     * Set various data fields used to control the vectorizing process.
     *
     * @param coverage the input grid coverage
     * @param bounds bounds (world coords) of the area to be vectorized
     * @param monitor a progress listener (may be {@code null})
     */
    private void initialize(GridCoverage2D coverage, Envelope bounds, ProgressListener monitor)
            throws TransformException, InvalidGridGeometryException {

        if (monitor == null)
            monitor = new NullProgressListener();

        try {
            monitor.started();
            this.coverage = coverage;
            GridGeometry2D gridGeom = coverage.getGridGeometry();

            // image used to sample the grid coverage
            image = coverage.getRenderedImage();

            this.transformLR = coverage.getGridGeometry().getGridToCRS2D(
                    PixelOrientation.LOWER_RIGHT);
            monitor.progress(30);

            imageBounds = coverage.getGridGeometry().worldToGrid(new Envelope2D(bounds));

            cellWidthX = gridGeom.getEnvelope2D().getSpan(gridGeom.axisDimensionX) /
                         gridGeom.getGridRange2D().getSpan(gridGeom.gridDimensionX);

            cellWidthY = gridGeom.getEnvelope2D().getSpan(gridGeom.axisDimensionY) /
                         gridGeom.getGridRange2D().getSpan(gridGeom.gridDimensionY);

            lines = new ArrayList<LineString>();
            geomFactory = new GeometryFactory();
            polygonizer = new Polygonizer();

            monitor.progress(80);

            vertLines = new HashMap<Integer, LineSegment>();

            cornerTouches = new ArrayList<Coordinate>();

        } finally {
            monitor.complete();
        }
    }

    /**
     * Vectorize the boundaries of regions of uniform value in the input grid coverage
     * and collect the boundaries as LineStrings
     *
     * @param band index of the band which contains the data to be vectorized
     * @param outsideValues a collection of one or more values which represent 'outside'
     *        or no data (may be {@code null} or empty)
     * @param insideEdges whether to vectorize inside edges (those separating grid regions
     *        with non-outside values)
     * @param monitor a progress listener (may be {@code null})
     */
    private void vectorizeAndCollectBoundaries(int band, Collection<Double> outsideValues, boolean insideEdges, ProgressListener monitor) {
        if (monitor == null) {
            monitor = new NullProgressListener();
        }

        try {
            // a 2x2 matrix of double values used as a moving window
            double[] curData = new double[4];
            RandomIter imageIter = RandomIterFactory.create(image, null);

            outside = new TreeSet<Double>();
            if (outsideValues == null || outsideValues.isEmpty()) {
                outside.add(Double.NaN);
            } else {
                outside.addAll(outsideValues);
            }

            if (!insideEdges) {
                setInsideValue();
            }

            // we add a virtual border, one cell wide, coded as 'outside'
            // around the raster
            float progressScale = 100.0f / (imageBounds.y + imageBounds.height - 1);
            for (int row = imageBounds.y - 1; row < imageBounds.y + imageBounds.height; row++) {
                if (monitor.isCanceled()) {
                    throw new CancellationException();
                }

                monitor.progress(progressScale * row);
                curData[TR] = curData[BR] = outside.first();

                for (int col = imageBounds.x - 1; col < imageBounds.x + imageBounds.width; col++) {
                    boolean[] ok = inDataWindow(row, col);

                    curData[TL] = curData[TR];
                    curData[BL] = curData[BR];

                    curData[TR] = (ok[TR] ? imageIter.getSampleDouble(col + 1, row, band) : outside.first());
                    if (isOutside(curData[TR])) {
                        curData[TR] = outside.first();
                    } else if (!insideEdges) {
                        curData[TR] = inside;
                    }

                    curData[BR] = (ok[BR] ? imageIter.getSampleDouble(col + 1, row + 1, band) : outside.first());
                    if (isOutside(curData[BR])) {
                        curData[BR] = outside.first();
                    } else if (!insideEdges) {
                        curData[BR] = inside;
                    }

                    updateCoordList(row, col, curData);
                }
            }
        } finally {
            monitor.complete();
        }
    }

    /**
     * Sets the proxy value used for "inside" cells when inside edges
     * are not being vectorized.
     */
    private void setInsideValue() {
        Double maxFinite = null;

        for (Double d : outside) {
            if (!(d.isInfinite() || d.isNaN())) {
                maxFinite = d;
            }
        }

        if (maxFinite != null) {
            inside = maxFinite + 1;
        } else {
            inside = (double) INSIDE_FLAG_VALUE;
        }
    }

    /**
     * Check the position of the data window with regard to the grid coverage
     * boundaries. We do this because a virtual, single-cell-width border is
     * placed around the input data.
     *
     * @param row index of the image row in the top left cell of the data window
     * @param col index of the image col in the top left cell of the data window
     * @return an array of four boolean values to be indexed with the TL, TR, BL
     * and BR constants.
     */
    private boolean[] inDataWindow(int row, int col) {
        boolean[] ok = new boolean[4];

        int rowflag = (row < imageBounds.y ? -1 : (row >= imageBounds.y + imageBounds.height - 1 ? 1 : 0));
        int colflag = (col < imageBounds.x ? -1 : (col >= imageBounds.x + imageBounds.width - 1 ? 1 : 0));

        ok[TL] = rowflag >= 0 && colflag >= 0;
        ok[TR] = rowflag >= 0 && colflag < 1;
        ok[BL] = rowflag < 1 && colflag >= 0;
        ok[BR] = rowflag < 1 && colflag < 1;

        return ok;
    }

    /**
     * This method controls the construction of line segments that border regions of uniform data
     * in the raster. See the {@linkplain #nbrConfig} method for more details.
     *
     * @param row index of the image row in the top left cell of the 2x2 data window
     * @param col index of the image col in the top left cell of the 2x2 data window
     * @param curData values in the current data window
     */
    private void updateCoordList(int row, int col, double[] curData) {
        LineSegment seg;

        switch (nbrConfig(curData)) {
        case 0:
            // vertical line continuing
            // nothing to do
            break;

        case 1:
            // bottom right corner
            // new horizontal and vertical lines
            horizLine = new LineSegment();
            horizLine.p0.x = col;

            seg = new LineSegment();
            seg.p0.y = row;
            vertLines.put(col, seg);
            break;

        case 2:
            // horizontal line continuing
            // nothing to do
            break;

        case 3:
            // bottom left corner
            // end of horizontal line; start of new vertical line
            horizLine.p1.x = col;
            addHorizLine(row);
            horizLine = null;

            seg = new LineSegment();
            seg.p0.y = row;
            vertLines.put(col, seg);
            break;

        case 4:
            // top left corner
            // end of horizontal line; end of vertical line
            horizLine.p1.x = col;
            addHorizLine(row);
            horizLine = null;

            seg = vertLines.get(col);
            seg.p1.y = row;
            addVertLine(col);
            vertLines.remove(col);
            break;

        case 5:
            // top right corner
            // start horiztonal line; end vertical line
            horizLine = new LineSegment();
            horizLine.p0.x = col;

            seg = vertLines.get(col);
            seg.p1.y = row;
            addVertLine(col);
            vertLines.remove(col);
            break;

        case 6:
            // inverted T in upper half
            // end horiztonal line; start new horizontal line; end vertical line
            horizLine.p1.x = col;
            addHorizLine(row);

            horizLine.p0.x = col;

            seg = vertLines.get(col);
            seg.p1.y = row;
            addVertLine(col);
            vertLines.remove(col);
            break;

        case 7:
            // T in lower half
            // end horizontal line; start new horizontal line; start new vertical line
            horizLine.p1.x = col;
            addHorizLine(row);

            horizLine.p0.x = col;

            seg = new LineSegment();
            seg.p0.y = row;
            vertLines.put(col, seg);
            break;

        case 8:
            // T pointing left
            // end horizontal line; end vertical line; start new vertical line
            horizLine.p1.x = col;
            addHorizLine(row);
            horizLine = null;

            seg = vertLines.get(col);
            seg.p1.y = row;
            addVertLine(col);

            seg = new LineSegment();
            seg.p0.y = row;
            vertLines.put(col, seg);
            break;

        case 9:
            // T pointing right
            // start new horizontal line; end vertical line; start new vertical line
            horizLine = new LineSegment();
            horizLine.p0.x = col;

            seg = vertLines.get(col);
            seg.p1.y = row;
            addVertLine(col);

            seg = new LineSegment();
            seg.p0.y = row;
            vertLines.put(col, seg);
            break;

        case 10:
            // cross
            // end horizontal line; start new horizontal line
            // end vertical line; start new vertical line
            horizLine.p1.x = col;
            addHorizLine(row);

            horizLine.p0.x = col;

            seg = vertLines.get(col);
            seg.p1.y = row;
            addVertLine(col);

            seg = new LineSegment();
            seg.p0.y = row;
            vertLines.put(col, seg);

            int z = -1;
            if (isDifferent(curData[TL], curData[BR])) {
                if (!isDifferent(curData[TR], curData[BL])) {
                    z = CROSS;
                }
            } else {
                if (isDifferent(curData[TR], curData[BL])) {
                    z = TL_BR;
                } else {
                    z = TR_BL;
                }
            }
            if (z != -1) {
                cornerTouches.add(new Coordinate(col, row, z));
            }
            break;

        case 11:
            // uniform
            // nothing to do
            break;
        }
    }

    /**
     * Examine the values in the 2x2 kernel and match to one of
     * the cases in the table below:
     * <pre>
     *  0) AB   1) AA   2) AA   3) AA
     *     AB      AB      BB      BA
     *
     *  4) AB   5) AB   6) AB   7) AA
     *     BB      AA      CC      BC
     *
     *  8) AB   9) AB  10) AB  11) AA
     *     CB      AC      CD      AA
     * </pre>
     * These patterns are those used in the GRASS raster to vector routine.
     * @param curData array of current data window values
     * @return integer id of the matching configuration
     */
    private int nbrConfig(double[] curData) {
        if (isDifferent(curData[TL], curData[TR])) { // 0, 4, 5, 6, 8, 9, 10
            if (isDifferent(curData[TL], curData[BL])) { // 4, 6, 8, 10
                if (isDifferent(curData[BL], curData[BR])) { // 8, 10
                    if (isDifferent(curData[TR], curData[BR])) {
                        return 10;
                    } else {
                        return 8;
                    }
                } else { // 4, 6
                    if (isDifferent(curData[TR], curData[BR])) {
                        return 6;
                    } else {
                        return 4;
                    }
                }
            } else { // 0, 5, 9
                if (isDifferent(curData[BL], curData[BR])) { // 0, 9
                    if (isDifferent(curData[TR], curData[BR])) {
                        return 9;
                    } else {
                        return 0;
                    }
                } else {
                    return 5;
                }
            }
        } else { // 1, 2, 3, 7, 11
            if (isDifferent(curData[TL], curData[BL])) { // 2, 3, 7
                if (isDifferent(curData[BL], curData[BR])) { // 3, 7
                    if (isDifferent(curData[TR], curData[BR])) {
                        return 7;
                    } else {
                        return 3;
                    }
                } else {
                    return 2;
                }
            } else { // 1, 11
                if (isDifferent(curData[TR], curData[BR])) {
                    return 1;
                } else {
                    return 11;
                }
            }
        }
    }

    /**
     * Create a LineString for a newly constructed horizontal border segment
     * @param row index of the image row in the top left cell of the current data window
     */
    private void addHorizLine(int row) {
        Point2D pixelStart = new Point2D.Double(horizLine.p0.x, row);
        Point2D pixelEnd = new Point2D.Double(horizLine.p1.x, row);
        Point2D rwStart = new Point2D.Double();
        Point2D rwEnd = new Point2D.Double();

        try {
            transformLR.transform(pixelStart, rwStart);
            transformLR.transform(pixelEnd, rwEnd);
        } catch (TransformException ex) {
            Logger.getLogger(RasterToVectorProcess.class.getName()).log(Level.SEVERE, null, ex);
        }

        Coordinate[] coords = new Coordinate[] { new Coordinate(rwStart.getX(), rwStart.getY()),
                new Coordinate(rwEnd.getX(), rwEnd.getY()) };

        lines.add(geomFactory.createLineString(coords));
    }

    /**
     * Create a LineString for a newly constructed vertical border segment
     * @param col index of the image column in the top-left cell of the current data window
     */
    private void addVertLine(int col) {
        Point2D pixelStart = new Point2D.Double(col, vertLines.get(col).p0.y);
        Point2D pixelEnd = new Point2D.Double(col, vertLines.get(col).p1.y);
        Point2D rwStart = new Point2D.Double();
        Point2D rwEnd = new Point2D.Double();

        try {
            transformLR.transform(pixelStart, rwStart);
            transformLR.transform(pixelEnd, rwEnd);
        } catch (TransformException ex) {
            Logger.getLogger(RasterToVectorProcess.class.getName()).log(Level.SEVERE, null, ex);
        }

        Coordinate[] coords = new Coordinate[] { new Coordinate(rwStart.getX(), rwStart.getY()),
                new Coordinate(rwEnd.getX(), rwEnd.getY()) };

        GeometryFactory gf = new GeometryFactory();
        lines.add(gf.createLineString(coords));
    }

    private boolean isOutside(double value) {
        for (Double d : outside) {
            if (!isDifferent(d, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test if two double values are different. Uses an absolute tolerance and
     * checks for NaN values.
     *
     * @param a first value
     * @param b second value
     * @return true if the values are different; false otherwise
     */
    private boolean isDifferent(double a, double b) {
        if (Double.isNaN(a) ^ Double.isNaN(b)) {
            return true;
        } else if (Double.isNaN(a) && Double.isNaN(b)) {
            return false;
        }

        if (Math.abs(a - b) > EPSILON) {
            return true;
        } else {
            return false;
        }
    }
    
}
