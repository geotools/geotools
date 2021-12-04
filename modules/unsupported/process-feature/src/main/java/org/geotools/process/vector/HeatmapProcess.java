/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.ProgressListener;

/**
 * A Process that uses a {@link HeatmapSurface} to compute a heatmap surface over a set of irregular
 * data points as a {@link GridCoverage}. Heatmaps are known more formally as <i>Multivariate Kernel
 * Density Estimation</i>.
 *
 * <p>The appearance of the heatmap is controlled by the kernel radius, which determines the "radius
 * of influence" of input points. The radius is specified by the radiusPixels parameter, which is in
 * output pixels. Using pixels allows easy estimation of a value which will give a visually
 * effective result, and ensures the heatmap appearance changes to match the zoom level.
 *
 * <p>By default each input point has weight 1. Optionally the weights of points may be supplied by
 * an attribute specified by the <code>weightAttr</code> parameter.
 *
 * <p>All geometry types are allowed as input. For non-point geometries the centroid is used.
 *
 * <p>To improve performance, the surface grid can be computed at a lower resolution than the
 * requested output image using the <code>pixelsPerCell</code> parameter. The grid is upsampled to
 * match the required image size. Upsampling uses Bilinear Interpolation to maintain visual quality.
 * This gives a large improvement in performance, with minimal impact on visual quality for small
 * cell sizes (for instance, 10 pixels or less).
 *
 * <p>To ensure that the computed surface is stable (i.e. does not display obvious edge artifacts
 * under zooming and panning), the data extent is expanded to be larger than the specified output
 * extent. The expansion distance is equal to the size of <code>radiusPixels</code> in the input
 * CRS.
 *
 * <h3>Parameters</h3>
 *
 * <i>M = mandatory, O = optional</i>
 *
 * <ul>
 *   <li><b>data</b> (M) - the FeatureCollection containing the point observations
 *   <li><b>radiusPixels</b> (M)- the density kernel radius, in pixels
 *   <li><b>weightAttr</b> (M)- the feature type attribute containing the observed surface value
 *   <li><b>pixelsPerCell</b> (O) - The pixels-per-cell value determines the resolution of the
 *       computed grid. Larger values improve performance, but degrade appearance. (Default = 1)
 *   <li><b>outputBBOX</b> (M) - The georeferenced bounding box of the output area
 *   <li><b>outputWidth</b> (M) - The width of the output raster
 *   <li><b>outputHeight</b> (M) - The height of the output raster
 * </ul>
 *
 * The output of the process is a {@linkplain GridCoverage2D} with a single band, with cell values
 * in the range [0, 1].
 *
 * <p>Computation of the surface takes places in the CRS of the output. If the data CRS is different
 * to the output CRS, the input points are transformed into the output CRS.
 *
 * <h3>Using the process as a Rendering Transformation</h3>
 *
 * This process can be used as a RenderingTransformation, since it implements the
 * <tt>invertQuery(... Query, GridGeometry)</tt> method. In this case the <code>queryBuffer</code>
 * parameter should be specified to expand the query extent appropriately. The output raster
 * parameters may be provided from the request extents, using the following SLD environment
 * variables:
 *
 * <ul>
 *   <li><b>outputBBOX</b> - env var = <tt>wms_bbox</tt>
 *   <li><b>outputWidth</b> - env var = <tt>wms_width</tt>
 *   <li><b>outputHeight</b> - env var = <tt>wms_height</tt>
 * </ul>
 *
 * When used as an Rendering Transformation the data query is rewritten to expand the query BBOX, to
 * ensure that enough data points are queried to make the computed surface stable under panning and
 * zooming.
 *
 * <p>
 *
 * @author Martin Davis - OpenGeo
 */
@DescribeProcess(
        title = "Heatmap",
        description =
                "Computes a heatmap surface over a set of data points and outputs as a single-band raster.")
public class HeatmapProcess implements VectorProcess {

    @DescribeResult(name = "result", description = "Output raster")
    public GridCoverage2D execute(

            // process data
            @DescribeParameter(name = "data", description = "Input features")
                    SimpleFeatureCollection obsFeatures,

            // process parameters
            @DescribeParameter(
                            name = "radiusPixels",
                            description = "Radius of the density kernel in pixels")
                    Integer argRadiusPixels,
            @DescribeParameter(
                            name = "weightAttr",
                            description = "Name of the attribute to use for data point weight",
                            min = 0,
                            max = 1)
                    String valueAttr,
            @DescribeParameter(
                            name = "pixelsPerCell",
                            description =
                                    "Resolution at which to compute the heatmap (in pixels). Default = 1",
                            defaultValue = "1",
                            min = 0,
                            max = 1)
                    Integer argPixelsPerCell,

            // output image parameters
            @DescribeParameter(name = "outputBBOX", description = "Bounding box of the output")
                    ReferencedEnvelope argOutputEnv,
            @DescribeParameter(
                            name = "outputWidth",
                            description = "Width of output raster in pixels")
                    Integer argOutputWidth,
            @DescribeParameter(
                            name = "outputHeight",
                            description = "Height of output raster in pixels")
                    Integer argOutputHeight,
            ProgressListener monitor)
            throws ProcessException {

        /** -------- Extract required information from process arguments ------------- */
        int pixelsPerCell = 1;
        if (argPixelsPerCell != null && argPixelsPerCell > 1) {
            pixelsPerCell = argPixelsPerCell;
        }
        int outputWidth = argOutputWidth;
        int outputHeight = argOutputHeight;
        int gridWidth = outputWidth;
        int gridHeight = outputHeight;
        if (pixelsPerCell > 1) {
            gridWidth = outputWidth / pixelsPerCell;
            gridHeight = outputHeight / pixelsPerCell;
        }

        /** Compute transform to convert input coords into output CRS */
        CoordinateReferenceSystem srcCRS = obsFeatures.getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem dstCRS = argOutputEnv.getCoordinateReferenceSystem();
        MathTransform trans = null;
        try {
            trans = CRS.findMathTransform(srcCRS, dstCRS);
        } catch (FactoryException e) {
            throw new ProcessException(e);
        }

        // ------------ Kernel Radius
        /*
         * // not used for now - only pixel radius values are supported double
         * distanceConversionFactor = distanceConversionFactor(srcCRS, dstCRS); double dstRadius =
         * argRadius * distanceConversionFactor;
         */
        int radiusCells = 100;
        if (argRadiusPixels != null) radiusCells = argRadiusPixels;
        if (pixelsPerCell > 1) {
            radiusCells /= pixelsPerCell;
        }

        /** -------------- Extract the input observation points ----------- */
        HeatmapSurface heatMap =
                new HeatmapSurface(radiusCells, argOutputEnv, gridWidth, gridHeight);
        try {
            extractPoints(obsFeatures, valueAttr, trans, heatMap);
        } catch (CQLException e) {
            throw new ProcessException(e);
        }

        /** --------------- Do the processing ------------------------------ */
        // Stopwatch sw = new Stopwatch();
        // compute the heatmap at the specified resolution
        float[][] heatMapGrid = heatMap.computeSurface();

        // flip now, since grid size may be smaller
        heatMapGrid = flipXY(heatMapGrid);

        // upsample to output resolution if necessary
        float[][] outGrid = heatMapGrid;
        if (pixelsPerCell > 1) outGrid = upsample(heatMapGrid, -999, outputWidth, outputHeight);

        // convert to the GridCoverage2D required for output
        GridCoverageFactory gcf =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        GridCoverage2D gridCov = gcf.create("Process Results", outGrid, argOutputEnv);

        // System.out.println("**************  Heatmap computed in " + sw.getTimeString());

        return gridCov;
    }

    /**
     * Flips an XY matrix along the X=Y axis, and inverts the Y axis. Used to convert from "map
     * orientation" into the "image orientation" used by GridCoverageFactory. The surface
     * interpolation is done on an XY grid, with Y=0 being the bottom of the space. GridCoverages
     * are stored in an image format, in a YX grid with Y=0 being the top.
     *
     * @param grid the grid to flip
     * @return the flipped grid
     */
    private float[][] flipXY(float[][] grid) {
        int xsize = grid.length;
        int ysize = grid[0].length;

        float[][] grid2 = new float[ysize][xsize];
        for (int ix = 0; ix < xsize; ix++) {
            for (int iy = 0; iy < ysize; iy++) {
                int iy2 = ysize - iy - 1;
                grid2[iy2][ix] = grid[ix][iy];
            }
        }
        return grid2;
    }

    private float[][] upsample(float[][] grid, float noDataValue, int width, int height) {
        BilinearInterpolator bi = new BilinearInterpolator(grid, noDataValue);
        float[][] outGrid = bi.interpolate(width, height, false);
        return outGrid;
    }

    /**
     * Given a target query and a target grid geometry returns the query to be used to read the
     * input data of the process involved in rendering. In this process this method is used to:
     *
     * <ul>
     *   <li>determine the extent & CRS of the output grid
     *   <li>expand the query envelope to ensure stable surface generation
     *   <li>modify the query hints to ensure point features are returned
     * </ul>
     *
     * Note that in order to pass validation, all parameters named here must also appear in the
     * parameter list of the <tt>execute</tt> method, even if they are not used there.
     *
     * @param argRadiusPixels the feature type attribute that contains the observed surface value
     * @param targetQuery the query used against the data source
     * @param targetGridGeometry the grid geometry of the destination image
     * @return The transformed query
     */
    public Query invertQuery(
            @DescribeParameter(
                            name = "radiusPixels",
                            description = "Radius to use for the kernel",
                            min = 0,
                            max = 1)
                    Integer argRadiusPixels,
            // output image parameters
            @DescribeParameter(
                            name = "outputBBOX",
                            description = "Georeferenced bounding box of the output")
                    ReferencedEnvelope argOutputEnv,
            @DescribeParameter(name = "outputWidth", description = "Width of the output raster")
                    Integer argOutputWidth,
            @DescribeParameter(name = "outputHeight", description = "Height of the output raster")
                    Integer argOutputHeight,
            Query targetQuery,
            GridGeometry targetGridGeometry)
            throws ProcessException {

        // TODO: handle different CRSes in input and output

        int radiusPixels = argRadiusPixels > 0 ? argRadiusPixels : 0;
        // input parameters are required, so should be non-null
        double queryBuffer =
                radiusPixels / pixelSize(argOutputEnv, argOutputWidth, argOutputHeight);
        /*
         * if (argQueryBuffer != null) { queryBuffer = argQueryBuffer; }
         */
        targetQuery.setFilter(expandBBox(targetQuery.getFilter(), queryBuffer));

        // clear properties to force all attributes to be read
        // (required because the SLD processor cannot see the value attribute specified in the
        // transformation)
        // TODO: set the properties to read only the specified value attribute
        targetQuery.setProperties(null);

        // set the decimation hint to ensure points are read
        Hints hints = targetQuery.getHints();
        hints.put(Hints.GEOMETRY_DISTANCE, 0.0);

        return targetQuery;
    }

    private double pixelSize(ReferencedEnvelope outputEnv, int outputWidth, int outputHeight) {
        // error-proofing
        if (outputEnv.getWidth() <= 0) return 0;
        // assume view is isotropic
        return outputWidth / outputEnv.getWidth();
    }

    protected Filter expandBBox(Filter filter, double distance) {
        return (Filter)
                filter.accept(
                        new BBOXExpandingFilterVisitor(distance, distance, distance, distance),
                        null);
    }

    /**
     * Extract points from a feature collection, and stores them in the heatmap
     *
     * @param obsPoints features to extract
     * @param attrName expression or property name used to evaluate the geometry from a feature
     * @param trans transform for extracted points
     * @param heatMap heatmap to add points to
     * @throws CQLException if attrName can't be parsed
     */
    protected void extractPoints(
            SimpleFeatureCollection obsPoints,
            String attrName,
            MathTransform trans,
            HeatmapSurface heatMap)
            throws CQLException {
        Expression attrExpr = null;
        if (attrName != null) {
            attrExpr = ECQL.toExpression(attrName);
        }

        try (SimpleFeatureIterator obsIt = obsPoints.features()) {
            double[] srcPt = new double[2];
            double[] dstPt = new double[2];
            while (obsIt.hasNext()) {
                SimpleFeature feature = obsIt.next();

                try {
                    // get the weight value, if any
                    double val = 1;
                    if (attrExpr != null) {
                        val = getPointValue(feature, attrExpr);
                    }

                    // get the point location from the geometry
                    Geometry geom = (Geometry) feature.getDefaultGeometry();
                    Coordinate p = getPoint(geom);
                    srcPt[0] = p.x;
                    srcPt[1] = p.y;
                    trans.transform(srcPt, 0, dstPt, 0, 1);
                    Coordinate pobs = new Coordinate(dstPt[0], dstPt[1], val);

                    heatMap.addPoint(pobs.x, pobs.y, val);
                } catch (Exception e) {
                    // just carry on for now (debugging)
                    // throw new ProcessException("Expression " + attrExpr +
                    // " failed to evaluate to a numeric value", e);
                }
            }
        }
    }

    /**
     * Gets a point to represent the Geometry. If the Geometry is a point, this is returned.
     * Otherwise, the centroid is used.
     *
     * @param g the geometry to find a point for
     * @return a point representing the Geometry
     */
    private static Coordinate getPoint(Geometry g) {
        if (g.getNumPoints() == 1) return g.getCoordinate();
        return g.getCentroid().getCoordinate();
    }

    /**
     * Gets the value for a point from the supplied attribute. The value is checked for validity,
     * and a default of 1 is used if necessary.
     *
     * @param feature the feature to extract the value from
     * @param attrExpr the expression specifying the attribute to read
     * @return the value for the point
     */
    private static double getPointValue(SimpleFeature feature, Expression attrExpr) {
        Double valObj = attrExpr.evaluate(feature, Double.class);
        if (valObj != null) {
            return valObj;
        }
        return 1;
    }
}
