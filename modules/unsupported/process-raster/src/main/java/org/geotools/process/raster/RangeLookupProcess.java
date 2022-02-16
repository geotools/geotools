/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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

// import it.geosolutions.jaiext.rlookup.RangeLookupTable;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.List;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ColorUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.factory.GeoTools;
import org.jaitools.numeric.Range;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;

/**
 * A raster reclassified process
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Emanuele Tajariol (GeoSolutions)
 * @author Simone Giannecchini (GeoSolutions)
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 */
@DescribeProcess(
        title = "Reclassify",
        description =
                "Reclassifies a continous raster into integer values defined by a set of ranges")
public class RangeLookupProcess implements RasterProcess {

    private static final double DEFAULT_NODATA = 0d;

    @DescribeResult(name = "reclassified", description = "The reclassified raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input raster")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "band",
                            description = "Source band to use for classification (default is 0)",
                            min = 0,
                            defaultValue = "0")
                    Integer classificationBand,
            @DescribeParameter(
                            name = "ranges",
                            description =
                                    "Specifier for a value range in the format ( START ; END ).  START and END values are optional. [ and ] can also be used as brackets, to indicate inclusion of the relevant range endpoint.",
                            collectionType = Range.class)
                    List<Range> classificationRanges,
            @DescribeParameter(
                            name = "outputPixelValues",
                            description = "Value to be assigned to corresponding range",
                            min = 0)
                    int[] outputPixelValues,
            @DescribeParameter(
                            name = "noData",
                            description =
                                    "Value to be assigned to pixels outside any range (defaults to 0)",
                            min = 0,
                            defaultValue = "0")
                    Double noData,
            ProgressListener listener)
            throws ProcessException {

        //
        // initial checks
        //
        if (coverage == null) {
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "coverage"));
        }
        if (classificationRanges == null) {
            throw new ProcessException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "classificationRanges"));
        }
        double nd = DEFAULT_NODATA;
        NoDataContainer noDataProperty =
                org.geotools.coverage.util.CoverageUtilities.getNoDataProperty(coverage);
        if (noData != null) {
            nd = noData.doubleValue();
        } else if (noDataProperty != null) {
            nd = noDataProperty.getAsSingleValue();
        }

        if (outputPixelValues != null && outputPixelValues.length > 0) {
            final int ranges = classificationRanges.size();
            if (ranges != outputPixelValues.length) {
                throw new ProcessException(
                        Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH, "outputPixelValues"));
            }
        }

        RenderedImage sourceImage = coverage.getRenderedImage();

        ImageWorker worker = new ImageWorker(sourceImage);

        // parse the band
        if (classificationBand != null) {
            final int band = classificationBand;
            final int numbands = sourceImage.getSampleModel().getNumBands();
            if (band < 0 || numbands <= band) {
                throw new ProcessException(
                        Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "band", band));
            }

            if (band == 0 && numbands > 0 || band > 0) {
                worker.retainBands(new int[] {band});
            }
        }

        //
        // Check the number of ranges we have in order to decide which type we can use for the
        // output values.
        // Our goal is to use the smallest possible data type that can hold the image values.
        //
        Object lookupTable;
        final int size = classificationRanges.size();
        int transferType = ColorUtilities.getTransferType(size);
        if (JAIExt.isJAIExtOperation("RLookup")) {
            lookupTable =
                    CoverageUtilities.getRangeLookupTableJAIEXT(
                            classificationRanges, outputPixelValues, nd, transferType);
        } else {
            // Builds the range lookup table
            // final RangeLookupTable lookupTable;

            switch (transferType) {
                case DataBuffer.TYPE_BYTE:
                    lookupTable =
                            CoverageUtilities.getRangeLookupTable(
                                    classificationRanges, outputPixelValues, (byte) nd);
                    break;
                case DataBuffer.TYPE_USHORT:
                    lookupTable =
                            CoverageUtilities.getRangeLookupTable(
                                    classificationRanges, outputPixelValues, (short) nd);
                    break;
                case DataBuffer.TYPE_INT:
                    lookupTable =
                            CoverageUtilities.getRangeLookupTable(
                                    classificationRanges, outputPixelValues, nd);
                    break;
                default:
                    throw new IllegalArgumentException(
                            org.geotools.metadata.i18n.Errors.format(
                                    ErrorKeys.ILLEGAL_ARGUMENT_$2,
                                    "classification ranges size",
                                    size));
            }
        }
        worker.setROI(org.geotools.coverage.util.CoverageUtilities.getROIProperty(coverage));
        worker.setBackground(new double[] {nd});
        final RenderedOp indexedClassification =
                worker.rangeLookup(lookupTable).getRenderedOperation();

        //
        // build the output coverage
        //

        // build the output sample dimensions, use the default value ( 0 ) as the no data
        final GridSampleDimension outSampleDimension =
                new GridSampleDimension("classification", new Category[] {Category.NODATA}, null);
        final GridCoverageFactory factory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        HashMap<String, Object> properties =
                new HashMap<String, Object>() {
                    {
                        put(NoDataContainer.GC_NODATA, new NoDataContainer(0d));
                    }
                };
        org.geotools.coverage.util.CoverageUtilities.setROIProperty(properties, worker.getROI());
        final GridCoverage2D output =
                factory.create(
                        "reclassified",
                        indexedClassification,
                        coverage.getGridGeometry(),
                        new GridSampleDimension[] {outSampleDimension},
                        new GridCoverage[] {coverage},
                        properties);
        return output;
    }

    /**
     * Execute the RangeLookupProcess on the provided coverage (left for backwards compatibility)
     *
     * @param coverage The continuous coverage to be reclassified
     * @param classificationBand The band to be used for classification
     * @param classificationRanges The list of ranges to be applied
     * @param listener The progress listener
     * @return The reclassified coverage
     */
    public GridCoverage2D execute(
            GridCoverage2D coverage,
            Integer classificationBand,
            List<Range> classificationRanges,
            ProgressListener listener)
            throws ProcessException {
        return execute(coverage, classificationBand, classificationRanges, null, 0d, listener);
    }
}
