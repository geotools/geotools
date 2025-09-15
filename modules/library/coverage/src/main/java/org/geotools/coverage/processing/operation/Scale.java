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

import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.imagen.Interpolation;
import org.eclipse.imagen.InterpolationNearest;
import org.eclipse.imagen.JAI;
import org.eclipse.imagen.ParameterBlockJAI;
import org.eclipse.imagen.PlanarImage;
import org.eclipse.imagen.PropertyGenerator;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.registry.RenderedRegistryMode;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.coverage.util.CoverageUtilities;

/**
 * This operation is simply a wrapper for the JAI scale operation which allows me to arbitrarily scale and translate a
 * rendered image.
 *
 * @version $Id$
 * @author Simone Giannecchini
 * @since 2.3
 * @see org.eclipse.imagen.media.scale.ScaleDescriptor
 */
public class Scale extends BaseScaleOperationJAI {

    /** Serial number for cross-version compatibility. */
    @Serial
    private static final long serialVersionUID = -3212656385631097713L;

    /** Default constructor. */
    public Scale() {
        super("Scale");
    }

    @Override
    protected RenderedImage createRenderedImage(ParameterBlockJAI parameters, RenderingHints hints) {
        final RenderedImage source = (RenderedImage) parameters.getSource(0);
        final Interpolation interpolation;
        if (parameters.getObjectParameter("interpolation") != null)
            interpolation = (Interpolation) parameters.getObjectParameter("interpolation");
        else if (hints.get(JAI.KEY_INTERPOLATION) != null)
            interpolation = (Interpolation) hints.get(JAI.KEY_INTERPOLATION);
        else {
            // I am pretty sure this should not happen. However I am not sure we should throw an
            // error
            interpolation = null;
        }
        final int transferType = source.getSampleModel().getDataType();

        @SuppressWarnings("PMD.CloseResource")
        final JAI processor = OperationJAI.getJAI(hints);
        PlanarImage image;
        if (interpolation != null
                && !(interpolation instanceof InterpolationNearest)
                && (transferType == DataBuffer.TYPE_FLOAT || transferType == DataBuffer.TYPE_DOUBLE)) {
            image = processor.createNS(getName(), parameters, hints).getRendering();

        } else image = processor.createNS(getName(), parameters, hints);

        return image;
    }

    @Override
    protected void handleJAIEXTParams(ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source =
                (GridCoverage2D) parameters2.parameter("source0").getValue();
        handleROINoDataInternal(parameters, source, SCALE, 5, 7);
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

        if (parameters.parameters.getNumParameters() > 5 && parameters.parameters.getObjectParameter(8) != null) {
            // Setting NoData property if needed
            Object bkgProp = parameters.parameters.getObjectParameter(8);
            if (bkgProp != null && bkgProp instanceof double[] background) {
                CoverageUtilities.setNoDataProperty(properties, background);
            }
        }

        // Setting ROI if present
        if (data instanceof RenderedOp op) {
            String operationName = op.getOperationName();
            PropertyGenerator propertyGenerator = null;
            if (operationName.equalsIgnoreCase(SCALE) || operationName.equalsIgnoreCase(TRANSLATE)) {
                propertyGenerator =
                        getOperationDescriptor(operationName).getPropertyGenerators(RenderedRegistryMode.MODE_NAME)[0];
            }
            if (propertyGenerator != null) {
                Object roiProp = propertyGenerator.getProperty(ROI, data);
                if (roiProp != null && roiProp instanceof ROI oI) {
                    CoverageUtilities.setROIProperty(properties, oI);
                }
            }
        }

        return properties;
    }
}
