/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import jaitools.media.jai.zonalstats.ZonalStatsDescriptor;
import jaitools.media.jai.zonalstats.ZonalStatsRIF;
import jaitools.numeric.Statistic;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderedImageFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RIFRegistry;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.AbstractStatisticsOperationJAI;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * This operation simply wraps Jai-tools Zonalstats operations described by
 * {@link ZonalStatsDescriptor} inside a GeoTools operation in order to make it
 * spatial-aware.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/jai-tools/src/main/java/org/geotools/coverage/processing/operation/ZonalStats.java $
 */
public class ZonalStats extends AbstractStatisticsOperationJAI {
    
    static {
        try {
            OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();
            or.registerDescriptor(new ZonalStatsDescriptor());
            RenderedImageFactory rif = new ZonalStatsRIF();
            RIFRegistry.register(or, "ZonalStats", "jaitools.media.jai", rif);
        } catch (Exception e) {
            // if already registered (in other cases error will be thrown anyway)
        }
    }
  
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2027327652181137851L;

    /** {@link Logger} for this class. */
    public final static Logger LOGGER = Logging
            .getLogger("org.geotools.coverage.processing.operation");

    /** {@link String} key for getting the min vector. */
    public final static String GT_SYNTHETIC_PROPERTY_MIN = Statistic.MIN.toString();
    /** {@link String} key for getting the max vector. */
    public final static String GT_SYNTHETIC_PROPERTY_MAX = Statistic.MAX.toString();
    /** {@link String} key for getting the mean vector. */
    public final static String GT_SYNTHETIC_PROPERTY_MEAN = Statistic.MEAN.toString();
    /** {@link String} key for getting the variance vector. */
    public final static String GT_SYNTHETIC_PROPERTY_VAR = Statistic.VARIANCE.toString();
    /** {@link String} key for getting the standard deviation vector. */
    public final static String GT_SYNTHETIC_PROPERTY_SDEV = Statistic.SDEV.toString();
    /** {@link String} key for getting the range vector. */
    public final static String GT_SYNTHETIC_PROPERTY_RANGE = Statistic.RANGE.toString();
    /** {@link String} key for getting the median vector. */
    public final static String GT_SYNTHETIC_PROPERTY_MEDIAN = Statistic.MEDIAN.toString();
    /** {@link String} key for getting the approx median vector. */
    public final static String GT_SYNTHETIC_PROPERTY_APPROX_MEDIAN = Statistic.APPROX_MEDIAN
            .toString();
    /** {@link String} key for getting the sum vector. */
    public final static String GT_SYNTHETIC_PROPERTY_SUM = Statistic.SUM.toString();

    /**
     * Constructs a default {@code "ZonalStats"} operation.
     */
    public ZonalStats() throws OperationNotFoundException {
        super(getOperationDescriptor("ZonalStats"));
    }

    /**
     * This operation MUST be performed on the geophysics data for this
     * {@link GridCoverage2D}.
     * 
     * @param parameters
     *            {@link ParameterValueGroup} that describes this operation
     * @return always true.
     */
    protected boolean computeOnGeophysicsValues( ParameterValueGroup parameters ) {
        return true;
    }

    /**
     * Prepare the properties for this ZonalStats operation.
     * 
     * @see OperationJAI#getProperties(RenderedImage, CoordinateReferenceSystem,
     *      InternationalString, MathTransform, GridCoverage2D[],
     *      org.geotools.coverage.processing.OperationJAI.Parameters),
     */
    protected Map<String, ? > getProperties( RenderedImage data, CoordinateReferenceSystem crs,
            InternationalString name, MathTransform toCRS, GridCoverage2D[] sources,
            Parameters parameters ) {
        // /////////////////////////////////////////////////////////////////////
        //
        // If and only if data is a RenderedOp we prepare the properties for
        // minimum and maximum as the output of the extrema operation.
        //
        // /////////////////////////////////////////////////////////////////////
        if (data instanceof RenderedOp) {
            final RenderedOp result = (RenderedOp) data;
            final Map<String, Object> synthProp = new HashMap<String, Object>();

            // get the properties
            final double[] minimums = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_MIN);
            if (minimums != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_MIN, minimums);
            final double[] maximums = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_MAX);
            if (maximums != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_MAX, maximums);
            final double[] means = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_MEAN);
            if (means != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_MEAN, means);
            final double[] var = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_VAR);
            if (var != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_VAR, var);
            final double[] sdev = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_SDEV);
            if (sdev != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_SDEV, sdev);
            final double[] range = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_RANGE);
            if (range != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_RANGE, range);
            final double[] median = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_MEDIAN);
            if (median != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_MEDIAN, median);
            final double[] approx_median = (double[]) result
                    .getProperty(GT_SYNTHETIC_PROPERTY_APPROX_MEDIAN);
            if (approx_median != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_APPROX_MEDIAN, approx_median);
            final double[] sum = (double[]) result.getProperty(GT_SYNTHETIC_PROPERTY_SUM);
            if (sum != null)
                synthProp.put(GT_SYNTHETIC_PROPERTY_SUM, sum);

            // return the map
            return Collections.unmodifiableMap(synthProp);
        }
        return super.getProperties(data, crs, name, toCRS, sources, parameters);
    }
}
