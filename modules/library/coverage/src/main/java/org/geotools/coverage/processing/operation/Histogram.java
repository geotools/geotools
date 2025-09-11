/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing.operation;

import java.awt.Shape;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.imagen.JAI;
import org.eclipse.imagen.ParameterBlockJAI;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.media.stats.HistogramWrapper;
import org.eclipse.imagen.media.stats.Statistics;
import org.eclipse.imagen.media.stats.Statistics.StatsType;
import org.geotools.api.coverage.processing.OperationNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseStatisticsOperationJAI;
import org.geotools.coverage.util.CoverageUtilities;

/**
 * This operation simply wraps ImageN Stats operation (Histogram) inside a GeoTools operation in order to make it
 * spatial-aware.
 *
 * <p>For the moment this is a very simple wrap. Plans on the 2.4 and successive versions of this operation are to add
 * the ability to use spatial ROIs and to specific Spatial subsampling. As of now, ROI has to be a Java2D {@link Shape}
 * subclass and the parameters to control x and y subsamplings got to be Integer, which means pixel-aware.
 *
 * <p>For more information on how the underlying {@link JAI} operators works you can have a look here: <a
 * href="http://download.java.net/media/jai/javadoc/1.1.3/jai-apidocs/javax/media/jai/operator/HistogramDescriptor.html">HistogramDescriptor</a>
 * <a href="http://download.java.net/media/jai/javadoc/1.1.3/jai-apidocs/javax/media/jai/Histogram.html>Histogram</a>
 *
 * <p><strong>How to use this operation</strong> Here is a very simple example on how to use this operation in order to
 * get the {@link org.eclipse.imagen.Histogram} of the source coverage. <code>
 * final OperationJAI op=new OperationJAI("Histogram");
 * ParameterValueGroup params = op.getParameters();
 * params.parameter("Source").setValue(coverage);
 * coverage=(GridCoverage2D) op.doOperation(params,null);
 * System.out.println(((double[])coverage.getProperty("histogram")));
 * </code>
 *
 * @author Simone Giannecchini
 * @since 2.4
 * @see org.eclipse.imagen.Histogram
 */
public class Histogram extends BaseStatisticsOperationJAI {

    private static final String STATS = "Stats";

    private static final String HISTOGRAM = "Histogram";

    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -4256576399698278701L;

    /** {@link String} key for getting the {@link org.eclipse.imagen.Histogram} object. */
    public static final String GT_SYNTHETIC_PROPERTY_HISTOGRAM = "histogram";

    /** Default constructor for the {@link Histogram} operation. */
    public Histogram() throws OperationNotFoundException {
        super(HISTOGRAM, getOperationDescriptor("Stats"));
    }

    @Override
    public String getName() {
        return HISTOGRAM;
    }

    /**
     * Prepare the {@link org.eclipse.imagen.Histogram} property for this histogram operation.
     *
     * @see OperationJAI#getProperties(RenderedImage, CoordinateReferenceSystem, InternationalString, MathTransform,
     *     GridCoverage2D[], org.geotools.coverage.processing.OperationJAI.Parameters),
     */
    @Override
    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform toCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        // /////////////////////////////////////////////////////////////////////
        //
        // If and only if data is a RenderedOp we prepare the properties for
        // histogram as the output of the histogram operation.
        //
        // /////////////////////////////////////////////////////////////////////
        if (data instanceof RenderedOp) {
            // XXX remove me with 1.5
            final RenderedOp result = (RenderedOp) data;

            final Map<String, Object> synthProp = new HashMap<>();

            // get the properties
            Statistics[][] results = (Statistics[][]) result.getProperty(Statistics.STATS_PROPERTY);
            // Extracting the bins
            int numBands = result.getNumBands();
            int[][] bins = new int[numBands][];

            // Cycle on the bands
            for (int i = 0; i < results.length; i++) {
                Statistics stat = results[i][0];
                double[] binsDouble = (double[]) stat.getResult();
                bins[i] = new int[binsDouble.length];
                for (int j = 0; j < binsDouble.length; j++) {
                    bins[i][j] = (int) binsDouble[j];
                }
            }
            // Getting numBins, LowBounds, MaxBounds parameters
            ParameterBlock parameterBlock = result.getParameterBlock();
            double[] lowValues = (double[]) parameterBlock.getObjectParameter(7);
            double[] highValues = (double[]) parameterBlock.getObjectParameter(8);
            int[] numBins = (int[]) parameterBlock.getObjectParameter(9);

            HistogramWrapper wrapper = new HistogramWrapper(numBins, lowValues, highValues, bins);

            // return the map
            synthProp.put(GT_SYNTHETIC_PROPERTY_HISTOGRAM, wrapper);
            // Addition of the ROI property and NoData property
            GridCoverage2D source = sources[0];
            CoverageUtilities.setROIProperty(synthProp, CoverageUtilities.getROIProperty(source));
            CoverageUtilities.setNoDataProperty(synthProp, CoverageUtilities.getNoDataProperty(source));
            return Collections.unmodifiableMap(synthProp);
        }
        return super.getProperties(data, crs, name, toCRS, sources, parameters);
    }

    @Override
    protected ParameterBlockJAI prepareParameters(ParameterValueGroup parameters) {
        ParameterBlockJAI block = super.prepareParameters(parameters);
        block.setParameter("lowValue", parameters.parameter("lowValue").getValue());
        block.setParameter("highValue", parameters.parameter("highValue").getValue());
        block.setParameter("numBins", parameters.parameter("numBins").getValue());
        GridCoverage2D source = (GridCoverage2D) parameters.parameter("source0").getValue();
        // Handle ROI and NoData
        handleROINoDataInternal(block, source, STATS, 2, 3);
        // Setting the Statistic operation
        block.set(new StatsType[] {StatsType.HISTOGRAM}, 6);
        // Check on the band numnber
        int b = source.getRenderedImage().getSampleModel().getNumBands();
        int[] indexes = new int[b];
        for (int i = 0; i < b; i++) {
            indexes[i] = i;
        }
        block.set(indexes, 5);
        return block;
    }
}
