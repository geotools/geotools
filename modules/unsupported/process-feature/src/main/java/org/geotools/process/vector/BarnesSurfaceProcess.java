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

import java.util.ArrayList;
import java.util.List;
import javax.measure.Unit;
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
import org.locationtech.jts.geom.CoordinateArrays;
import org.locationtech.jts.geom.Envelope;
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
import si.uom.NonSI;
import si.uom.SI;

/**
 * A Process that uses a {@link BarnesSurfaceInterpolator} to compute an interpolated surface over a
 * set of irregular data points as a {@link GridCoverage}.
 *
 * <p>The implementation allows limiting the radius of influence of observations, in order to
 * prevent extrapolation into unsupported areas, and to increase performance (by reducing the number
 * of observations considered).
 *
 * <p>To improve performance, the surface grid can be computed at a lower resolution than the
 * requested output image. The grid is upsampled to match the required image size. Upsampling uses
 * Bilinear Interpolation to maintain visual quality. This gives a large improvement in performance,
 * with minimal impact on visual quality for small cell sizes (for instance, 10 pixels or less).
 *
 * <p>To ensure that the computed surface is stable (i.e. does not display obvious edge artifacts
 * during zooming, panning and tiling), the data query extent should be expanded to be larger than
 * the specified output extent. This includes "nearby" points which may affect the value of the
 * surface. The expansion distance depends on the length scale, convergence factor, and data spacing
 * in a complex way, so must be manually determined. It does NOT depend on the output window extent.
 * (A good heuristic is to set it to expand by at least the size of the length scale.)
 *
 * <p>To prevent excessive CPU consumption, the process allows limiting the number of data points to
 * process. If the limit is exceeded the output is computed consuming and using only the maximum
 * number of points specified.
 *
 * <h3>Parameters</h3>
 *
 * <i>M = mandatory, O = optional</i>
 *
 * <p>
 *
 * <ul>
 *   <li><b>data</b> (M) - the FeatureCollection containing the point observations
 *   <li><b>valueAttr</b> (M)- the feature type attribute containing the observed surface value
 *   <li><b>dataLimit</b> (O)- the maximum number of input points to process
 *   <li><b>scale</b> (M) - the Length Scale for the interpolation. In units of the input data CRS.
 *   <li><b>convergence</b> (O) - the convergence factor for refinement. Between 0 and 1 (values
 *       below 0.4 are safest). (Default = 0.3)
 *   <li><b>passes</b> (O) - the number of passes to compute. 1 or greater. (Default = 2)
 *   <li><b>minObservations</b> (O) - The minimum number of observations required to support a grid
 *       cell. (Default = 2)
 *   <li><b>maxObservationDistance</b> (O) - The maximum distance to an observation for it to
 *       support a grid cell. 0 means all observations are used. In units of the input data CRS.
 *       (Default = 0)
 *   <li><b>noDataValue</b> (O) - The NO_DATA value to use for unsupported grid cells in the output
 *       coverage. (Default = -999)
 *   <li><b>pixelsPerCell</b> (O) - The pixels-per-cell value determines the resolution of the
 *       computed grid. Larger values improve performance, but may degrade appearance. (Default = 1)
 *   <li><b>queryBuffer</b> (O) - The distance to expand the query envelope by. Larger values
 *       provide a more stable surface. In units of the input data CRS. (Default = 0)
 *   <li><b>outputBBOX</b> (M) - The georeferenced bounding box of the output area
 *   <li><b>outputWidth</b> (M) - The width of the output raster
 *   <li><b>outputHeight</b> (M) - The height of the output raster
 * </ul>
 *
 * The output of the process is a {@linkplain GridCoverage2D} with a single band, with cell values
 * in the same domain as the input observation field specified by <code>valueAttr</code>.
 *
 * <p>Computation of the surface takes places in the CRS of the output. If the data CRS is geodetic
 * and the output CRS is planar, or vice-versa, the input points are transformed into the output
 * CRS. A simple technique is used to convert the surface distance parameters <code>scale</code> and
 * <code>maxObservationDistance</code> into the output CRS units.
 *
 * <h3>Using the process as a Rendering Transformation</h3>
 *
 * This process can be used as a RenderingTransformation, since it implements the
 * <tt>invertQuery(... Query, GridGeometry)</tt> method.
 *
 * <p>When used as an Rendering Transformation the process rewrites data query to expand the query
 * BBOX. This includes "nearby" data points to make the computed surface stable under panning and
 * zooming. To support this the <code>queryBuffer</code> parameter should be specified to expand the
 * query extent appropriately.
 *
 * <p>The output raster parameters can be determined from the request extents, using the following
 * SLD environment variables:
 *
 * <p>
 *
 * <ul>
 *   <li><b>outputBBOX</b> - env var = <tt>wms_bbox</tt>
 *   <li><b>outputWidth</b> - env var = <tt>wms_width</tt>
 *   <li><b>outputHeight</b> - env var = <tt>wms_height</tt>
 * </ul>
 *
 * <p>
 *
 * @author Martin Davis - OpenGeo
 */
@DescribeProcess(
        title = "BarnesSurface",
        description =
                "Uses Barnes Analysis to compute an interpolated surface over a set of irregular data points.")
public class BarnesSurfaceProcess implements VectorProcess {

    // no process state is defined, since RenderingTransformation processes must be stateless

    @DescribeResult(name = "result", description = "Output raster")
    public GridCoverage2D execute(

            // process data
            @DescribeParameter(name = "data", description = "Input features")
                    SimpleFeatureCollection obsFeatures,
            @DescribeParameter(
                            name = "valueAttr",
                            description =
                                    "Name of attribute containing the data value to be interpolated")
                    String valueAttr,
            @DescribeParameter(
                            name = "dataLimit",
                            description = "Limit for the number of input features processed",
                            min = 0,
                            max = 1)
                    Integer argDataLimit,

            // process parameters
            @DescribeParameter(
                            name = "scale",
                            description =
                                    "Length scale for the interpolation, in units of the source data CRS",
                            min = 1,
                            max = 1)
                    Double argScale,
            @DescribeParameter(
                            name = "convergence",
                            description =
                                    "Convergence factor for refinement (between 0 and 1, default 0.3)",
                            min = 0,
                            max = 1,
                            defaultValue = "0.3")
                    Double argConvergence,
            @DescribeParameter(
                            name = "passes",
                            description = "Number of passes to compute (default = 2)",
                            min = 0,
                            max = 1)
                    Integer argPasses,
            @DescribeParameter(
                            name = "minObservations",
                            description =
                                    "Minimum number of observations required to support a grid cell (default = 2)",
                            min = 0,
                            max = 1,
                            defaultValue = "2")
                    Integer argMinObsCount,
            @DescribeParameter(
                            name = "maxObservationDistance",
                            description =
                                    "Maximum distance to an observation for it to support a grid cell, in units of the source CRS (default = 0, meaning all observations used)",
                            defaultValue = "0",
                            min = 0,
                            max = 1)
                    Double argMaxObsDistance,
            @DescribeParameter(
                            name = "noDataValue",
                            description = "Value to use for NO_DATA cells (default = -999)",
                            defaultValue = "-999",
                            min = 0,
                            max = 1)
                    Double argNoDataValue,
            @DescribeParameter(
                            name = "pixelsPerCell",
                            description =
                                    "Resolution of the computed grid in pixels per grid cell (default = 1)",
                            defaultValue = "1",
                            min = 0,
                            max = 1)
                    Integer argPixelsPerCell,

            // query modification parameters
            @DescribeParameter(
                            name = "queryBuffer",
                            description =
                                    "Distance to expand the query envelope by, in units of the source CRS (larger values provide a more stable surface)",
                            min = 0,
                            max = 1)
                    Double argQueryBuffer,

            // output image parameters
            @DescribeParameter(name = "outputBBOX", description = "Bounding box for output")
                    ReferencedEnvelope outputEnv,
            @DescribeParameter(
                            name = "outputWidth",
                            description = "Width of the output raster in pixels")
                    Integer outputWidth,
            @DescribeParameter(
                            name = "outputHeight",
                            description = "Height of the output raster in pixels")
                    Integer outputHeight,
            ProgressListener monitor)
            throws ProcessException {

        /**
         * --------------------------------------------- Check that process arguments are valid
         * ---------------------------------------------
         */
        if (valueAttr == null || valueAttr.length() <= 0) {
            throw new IllegalArgumentException("Value attribute must be specified");
        }

        /**
         * --------------------------------------------- Set up required information from process
         * arguments. ---------------------------------------------
         */
        int dataLimit = 0;
        if (argDataLimit != null) dataLimit = argDataLimit;

        double lengthScale = argScale;
        double convergenceFactor = argConvergence != null ? argConvergence : 0.3;
        int passes = argPasses != null ? argPasses : 2;
        int minObsCount = argMinObsCount != null ? argMinObsCount : 2;
        double maxObsDistance = argMaxObsDistance != null ? argMaxObsDistance : 0.0;
        float noDataValue = (float) (argNoDataValue != null ? argNoDataValue : -999);
        int pixelsPerCell = 1;
        if (argPixelsPerCell != null && argPixelsPerCell > 1) {
            pixelsPerCell = argPixelsPerCell;
        }
        int gridWidth = outputWidth;
        int gridHeight = outputHeight;
        if (pixelsPerCell > 1) {
            gridWidth = outputWidth / pixelsPerCell;
            gridHeight = outputHeight / pixelsPerCell;
        }

        CoordinateReferenceSystem srcCRS = obsFeatures.getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem dstCRS = outputEnv.getCoordinateReferenceSystem();
        MathTransform trans = null;
        try {
            trans = CRS.findMathTransform(srcCRS, dstCRS);
        } catch (FactoryException e) {
            throw new ProcessException(e);
        }
        /**
         * --------------------------------------------- Convert distance parameters to units of the
         * destination CRS. ---------------------------------------------
         */
        double distanceConversionFactor = distanceConversionFactor(srcCRS, dstCRS);
        double dstLengthScale = lengthScale * distanceConversionFactor;
        double dstMaxObsDistance = maxObsDistance * distanceConversionFactor;

        /**
         * --------------------------------------------- Extract the input observation points
         * ---------------------------------------------
         */
        Coordinate[] pts = null;
        try {
            pts = extractPoints(obsFeatures, valueAttr, trans, dataLimit);
        } catch (CQLException e) {
            throw new ProcessException(e);
        }

        /**
         * --------------------------------------------- Do the processing
         * ---------------------------------------------
         */
        // Stopwatch sw = new Stopwatch();
        // interpolate the surface at the specified resolution
        float[][] barnesGrid =
                createBarnesGrid(
                        pts,
                        dstLengthScale,
                        convergenceFactor,
                        passes,
                        minObsCount,
                        dstMaxObsDistance,
                        noDataValue,
                        outputEnv,
                        gridWidth,
                        gridHeight);

        // flip now, since grid size may be smaller
        barnesGrid = flipXY(barnesGrid);

        // upsample to output resolution if necessary
        float[][] outGrid = barnesGrid;
        if (pixelsPerCell > 1)
            outGrid = upsample(barnesGrid, noDataValue, outputWidth, outputHeight);

        // convert to the GridCoverage2D required for output
        GridCoverageFactory gcf =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        GridCoverage2D gridCov = gcf.create("values", outGrid, outputEnv);

        // System.out.println("**************  Barnes Surface computed in " + sw.getTimeString());

        return gridCov;
    }

    /*
     * An approximate value for the length of a degree at the equator in meters.
     * This doesn't have to be precise, since it is only used to convert
     * values which are themselves rough approximations.
     */
    private static final double METRES_PER_DEGREE = 111320;

    private static double distanceConversionFactor(
            CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem dstCRS) {
        Unit<?> srcUnit = srcCRS.getCoordinateSystem().getAxis(0).getUnit();
        Unit<?> dstUnit = dstCRS.getCoordinateSystem().getAxis(0).getUnit();
        if (srcUnit == dstUnit) {
            return 1;
        } else if (srcUnit == NonSI.DEGREE_ANGLE && dstUnit == SI.METRE) {
            return METRES_PER_DEGREE;
        } else if (srcUnit == SI.METRE && dstUnit == NonSI.DEGREE_ANGLE) {
            return 1.0 / METRES_PER_DEGREE;
        }
        throw new IllegalStateException(
                "Unable to convert distances from " + srcUnit + " to " + dstUnit);
    }

    /**
     * Flips an XY matrix along the X=Y axis, and inverts the Y axis. Used to convert from "map
     * orientation" into the "image orientation" used by GridCoverageFactory. The surface
     * interpolation is done on an XY grid, with Y=0 being the bottom of the space. GridCoverages
     * are stored in an image format, in a YX grid with 0 being the top.
     *
     * @param grid the grid to flip
     * @return the flipped grid
     */
    private static float[][] flipXY(float[][] grid) {
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

    private float[][] createBarnesGrid(
            Coordinate[] pts,
            double lengthScale,
            double convergenceFactor,
            int passes,
            int minObservationCount,
            double maxObservationDistance,
            float noDataValue,
            Envelope destEnv,
            int width,
            int height) {
        BarnesSurfaceInterpolator barnesInterp = new BarnesSurfaceInterpolator(pts);
        barnesInterp.setLengthScale(lengthScale);
        barnesInterp.setConvergenceFactor(convergenceFactor);
        barnesInterp.setPassCount(passes);
        barnesInterp.setMinObservationCount(minObservationCount);
        barnesInterp.setMaxObservationDistance(maxObservationDistance);
        barnesInterp.setNoData(noDataValue);

        float[][] grid = barnesInterp.computeSurface(destEnv, width, height);

        return grid;
    }

    private float[][] upsample(float[][] grid, float noDataValue, int width, int height) {
        BilinearInterpolator bi = new BilinearInterpolator(grid, noDataValue);
        float[][] outGrid = bi.interpolate(width, height, true);
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
     * @param argQueryBuffer the distance by which to expand the query window
     * @param targetQuery the query used against the data source
     * @param targetGridGeometry the grid geometry of the destination image
     * @return The transformed query
     */
    public Query invertQuery(
            @DescribeParameter(
                            name = "queryBuffer",
                            description = "The distance by which to expand the query window",
                            min = 0,
                            max = 1)
                    Double argQueryBuffer,
            Query targetQuery,
            GridGeometry targetGridGeometry)
            throws ProcessException {

        // default is no expansion
        double queryBuffer = 0;
        if (argQueryBuffer != null) {
            queryBuffer = argQueryBuffer;
        }

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

    private Filter expandBBox(Filter filter, double distance) {
        return (Filter)
                filter.accept(
                        new BBOXExpandingFilterVisitor(distance, distance, distance, distance),
                        null);
    }

    public static Coordinate[] extractPoints(
            SimpleFeatureCollection obsPoints, String attrName, MathTransform trans, int dataLimit)
            throws CQLException {
        Expression attrExpr = ECQL.toExpression(attrName);
        List<Coordinate> ptList = new ArrayList<>();

        try (SimpleFeatureIterator obsIt = obsPoints.features()) {
            double[] srcPt = new double[2];
            double[] dstPt = new double[2];
            int i = 0;
            while (obsIt.hasNext()) {
                SimpleFeature feature = obsIt.next();

                double val = 0;

                try {

                    if (dataLimit > 0 && i >= dataLimit) {
                        // TODO: log this situation
                        break;
                    }
                    i++;
                    // get the observation value from the attribute (if non-null)
                    Object valObj = attrExpr.evaluate(feature);
                    if (valObj != null) {
                        // System.out.println(evaluate);
                        Number valNum = (Number) valObj;
                        val = valNum.doubleValue();

                        // get the point location from the geometry
                        Geometry geom = (Geometry) feature.getDefaultGeometry();
                        Coordinate p = geom.getCoordinate();
                        srcPt[0] = p.x;
                        srcPt[1] = p.y;
                        trans.transform(srcPt, 0, dstPt, 0, 1);
                        Coordinate pobs = new Coordinate(dstPt[0], dstPt[1], val);
                        // Coordinate pobs = new Coordinate(p.x, p.y, val);
                        ptList.add(pobs);
                    }
                } catch (Exception e) {
                    // just carry on for now (debugging)
                    // throw new ProcessException("Expression " + attrExpr + " failed to evaluate to
                    // a numeric value", e);
                }
            }
        }

        Coordinate[] pts = CoordinateArrays.toCoordinateArray(ptList);
        return pts;
    }
}
