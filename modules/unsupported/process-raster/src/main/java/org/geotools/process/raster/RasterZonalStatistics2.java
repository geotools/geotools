/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import com.sun.media.jai.util.SunTileCache;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.stats.Statistics.StatsType;
import it.geosolutions.jaiext.zonal.ZonalStatsDescriptor;
import it.geosolutions.jaiext.zonal.ZoneGeometry;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.util.List;
import javax.media.jai.JAI;
import javax.media.jai.operator.NullDescriptor;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.GridCoverage2DRIA;
import org.geotools.coverage.processing.operation.ZonalStatistics;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.ParameterValueGroup;

/**
 * This class wraps the "ZonalStats2" OperationJAI and executes the selected operation with the
 * defined parameters. No transformation of the optional classifier image is needed because inside
 * the process the classifier image is already transformed by the {@link GridCoverage2DRIA}
 * operation. By default the input band is set 0, and the statistics to MEAN, MAX, MIN, EXTREMA,
 * VARIANCE, STANDARD DEVIATION.
 *
 * <p>The main difference between this class and {@link RasterZonalStatistics} is the fact that this
 * operation will calculate the requested statistics for all the {@link SimpleFeature}s in a single
 * step, without having to iterate on the features.
 *
 * <p>This Process is a simple wrapper of the {@link ZonalStatistics} operation
 *
 * @author geosolutions
 */
@DescribeProcess(
        title = "Raster Zonal Statistics",
        description =
                "Computes statistics for the distribution of a certain quantity in a set of polygonal zones.")
public class RasterZonalStatistics2 implements RasterProcess {

    /** Default processor used for executing the operations. */
    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    /** Default statistics to calculate. */
    private static final StatsType[] DEFAULT_STATISTICS = {
        StatsType.MEAN,
        StatsType.MAX,
        StatsType.MIN,
        StatsType.EXTREMA,
        StatsType.VARIANCE,
        StatsType.DEV_STD
    };

    @DescribeResult(
            name = "zonal statistics",
            description =
                    "A feature collection with the attributes of the zone layer (prefixed by 'z_') and the statistics fields min,max,sum,avg,stddev")
    public List<ZoneGeometry> execute(
            @DescribeParameter(
                            name = "source",
                            description = "Input raster to compute statistics for")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "bands",
                            description = "Source band used to compute statistics (default is 0)")
                    int[] bands,
            @DescribeParameter(
                            name = "zones",
                            description = "Zone polygon features for which to compute statistics")
                    List<SimpleFeature> zones,
            @DescribeParameter(
                            name = "classifier",
                            description =
                                    "Raster whose values will be used as classes for the statistical analysis. Each zone reports statistics partitioned "
                                            + "by classes according to the values of the raster. Must be a single band raster with integer values.",
                            min = 0)
                    GridCoverage2D classifier,
            @DescribeParameter(name = "nodata", description = "Input Range for NoData")
                    Range nodata,
            @DescribeParameter(
                            name = "mask",
                            description = "Optional mask for the statistic calculations")
                    Geometry mask,
            @DescribeParameter(
                            name = "useROIAccessor",
                            description =
                                    "Boolean indicating if a RasterAccessor associated to the Mask should be used for calculating statistics. (Only with Mask field present)",
                            defaultValue = "false")
                    boolean useROIAccessor,
            @DescribeParameter(
                            name = "roi",
                            description = "Optional roi object, if the zones parameter is not used")
                    Polygon roi,
            @DescribeParameter(
                            name = "statistics",
                            description =
                                    "Statistics to calculate (default are min,max,sum,avg,stddev)")
                    StatsType[] stats,
            @DescribeParameter(
                            name = "minbounds",
                            description =
                                    "Minimum bounds used for calculating Histogram, median and mode operations (for each band)")
                    double[] minbounds,
            @DescribeParameter(
                            name = "maxbounds",
                            description =
                                    "Maximum bounds used for calculating Histogram, median and mode operations (for each band)")
                    double[] maxbounds,
            @DescribeParameter(
                            name = "numbins",
                            description =
                                    "Number of Bins used for calculating Histogram, median and mode operations (for each band)")
                    int[] numbins,
            @DescribeParameter(
                            name = "rangeData",
                            description =
                                    "Maximum bounds used for calculating Histogram, median and mode operations (for each band)")
                    List<Range> rangeData,
            @DescribeParameter(
                            name = "localStats",
                            description =
                                    "Number of Bins used for calculating Histogram, median and mode operations (for each band)")
                    boolean localStats) {

        // If no band is indicated, then the first band is taken
        int[] ibands = {0};
        if (bands == null) {
            bands = ibands;
        }

        // If no statistic is defined, then the default statistics are taken
        if (stats == null) {
            stats = DEFAULT_STATISTICS;
        }

        RenderedImage classificationRaster = null;

        // prepare the classification image if necessary
        if (classifier != null) {
            // find nodata values
            GridSampleDimension sampleDimension = classifier.getSampleDimension(0);
            double[] nodataarr = sampleDimension.getNoDataValues();
            // Setting of the No Data values
            double[] noDataClassifier = nodataarr != null ? nodataarr : new double[] {Double.NaN};

            // this will adapt the classification image to the projection and image layout
            // of the data coverage
            classificationRaster = GridCoverage2DRIA.create(classifier, coverage, noDataClassifier);
            // Definition of the JAI TileCache to use
            RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, new SunTileCache());
            // Wrap of the classification raster with a NullDescriptor for adding the TileCache
            // hints
            classificationRaster = NullDescriptor.create(classificationRaster, hints);
        }

        // Selection of the operation
        final ParameterValueGroup param = PROCESSOR.getOperation("Zonal").getParameters();
        // Setting of the parameters
        param.parameter("Source").setValue(coverage);
        param.parameter("bands").setValue(bands);
        param.parameter("classifier").setValue(classificationRaster);
        param.parameter("roi").setValue(roi);
        param.parameter("roilist").setValue(zones);
        param.parameter("NoData").setValue(nodata);
        param.parameter("mask").setValue(mask);
        param.parameter("useROIAccessor").setValue(useROIAccessor);
        param.parameter("stats").setValue(stats);
        param.parameter("minbound").setValue(minbounds);
        param.parameter("maxbound").setValue(maxbounds);
        param.parameter("numbin").setValue(numbins);
        param.parameter("rangeData").setValue(rangeData);
        param.parameter("localstats").setValue(localStats);
        // Operation execution
        GridCoverage2D output = (GridCoverage2D) PROCESSOR.doOperation(param);
        // Retrieval of the result
        @SuppressWarnings("unchecked")
        List<ZoneGeometry> value =
                (List<ZoneGeometry>) output.getProperty(ZonalStatsDescriptor.ZS_PROPERTY);
        return value;
    }
}
