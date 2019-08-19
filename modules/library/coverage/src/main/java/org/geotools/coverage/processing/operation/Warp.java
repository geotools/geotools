/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2015, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.jaiext.warp.WarpDescriptor;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PropertyGenerator;
import javax.media.jai.ROI;
import javax.media.jai.registry.RenderedRegistryMode;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.util.CoverageUtilities;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * This operation is simply a wrapper for the JAI Warp operation
 *
 * @version $Id$
 * @author Simone Giannecchini
 * @since 9.0
 * @see WarpDescriptor
 */
public class Warp extends BaseScaleOperationJAI {

    /** serialVersionUID */
    private static final long serialVersionUID = -9077795909705065389L;

    /** Default constructor. */
    public Warp() {
        super(WARP);
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source = (GridCoverage2D) parameters2.parameter("source0").getValue();
        handleROINoDataInternal(parameters, source, WARP, 3, 4);
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        Map props = sources[PRIMARY_SOURCE_INDEX].getProperties();

        Map properties = new HashMap<>();
        if (props != null) {
            properties.putAll(props);
        }

        // Setting NoData property if needed
        double[] background = (double[]) parameters.parameters.getObjectParameter(2);
        if (parameters.parameters.getNumParameters() > 3
                && parameters.parameters.getObjectParameter(4) != null) {
            CoverageUtilities.setNoDataProperty(properties, background);
        }

        // Setting ROI if present
        PropertyGenerator propertyGenerator =
                getOperationDescriptor(WARP)
                        .getPropertyGenerators(RenderedRegistryMode.MODE_NAME)[0];
        Object roiProp = propertyGenerator.getProperty(ROI, data);
        if (roiProp != null && roiProp instanceof ROI) {
            CoverageUtilities.setROIProperty(properties, (ROI) roiProp);
        }

        return properties;
    }
}
