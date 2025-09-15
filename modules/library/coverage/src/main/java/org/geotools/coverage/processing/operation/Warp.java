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

import java.awt.image.RenderedImage;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.imagen.ParameterBlockJAI;
import org.eclipse.imagen.PropertyGenerator;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.media.warp.WarpDescriptor;
import org.eclipse.imagen.registry.RenderedRegistryMode;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.util.CoverageUtilities;

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
    @Serial
    private static final long serialVersionUID = -9077795909705065389L;

    /** Default constructor. */
    public Warp() {
        super(WARP);
    }

    @Override
    protected void handleJAIEXTParams(ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source =
                (GridCoverage2D) parameters2.parameter("source0").getValue();
        handleROINoDataInternal(parameters, source, WARP, 3, 4);
    }

    @Override
    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Parameters parameters) {
        @SuppressWarnings("unchecked")
        Map<String, Object> props = sources[PRIMARY_SOURCE_INDEX].getProperties();

        Map<String, Object> properties = new HashMap<>();
        if (props != null) {
            properties.putAll(props);
        }

        // Setting NoData property if needed
        double[] background = (double[]) parameters.parameters.getObjectParameter(2);
        if (parameters.parameters.getNumParameters() > 3 && parameters.parameters.getObjectParameter(4) != null) {
            CoverageUtilities.setNoDataProperty(properties, background);
        }

        // Setting ROI if present
        PropertyGenerator propertyGenerator =
                getOperationDescriptor(WARP).getPropertyGenerators(RenderedRegistryMode.MODE_NAME)[0];
        Object roiProp = propertyGenerator.getProperty(ROI, data);
        if (roiProp != null && roiProp instanceof ROI oI) {
            CoverageUtilities.setROIProperty(properties, oI);
        }

        return properties;
    }
}
