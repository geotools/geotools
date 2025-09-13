/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.imagen.Interpolation;
import org.eclipse.imagen.JAI;
import org.eclipse.imagen.ParameterBlockJAI;
import org.eclipse.imagen.PropertyGenerator;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.media.range.Range;
import org.eclipse.imagen.registry.RenderedRegistryMode;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.util.InternationalString;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.util.factory.GeoTools;

/**
 * This operation is simply a wrapper for the JAI Affine operation
 *
 * @version $Id$
 * @author Simone Giannecchini
 * @since 12.0
 * @see org.eclipse.imagen.media.affine.AffineDescriptor
 */
public class Affine extends BaseScaleOperationJAI {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 1699623079343108288L;

    /** Default constructor. */
    public Affine() {
        super(AFFINE);
    }

    @Override
    protected RenderedImage createRenderedImage(ParameterBlockJAI parameters, RenderingHints hints) {
        final RenderedImage source = (RenderedImage) parameters.getSource(0);
        if (hints == null) {
            hints = GeoTools.getDefaultHints().clone();
        }

        ////
        //
        // Interpolation
        //
        ////
        final Interpolation interpolation;
        if (parameters.getObjectParameter("interpolation") != null) {
            interpolation = (Interpolation) parameters.getObjectParameter("interpolation");
        } else if (hints.get(JAI.KEY_INTERPOLATION) != null) {
            interpolation = (Interpolation) hints.get(JAI.KEY_INTERPOLATION);
        } else {
            // I am pretty sure this should not happen. However I am not sure we should throw an
            // error
            interpolation = null;
        }

        ////
        //
        // ROI
        //
        ////
        ROI roi = null;
        Object param = CoverageProcessor.getParameter(parameters, ROI);
        if (param != null) {
            roi = (ROI) param;
        }

        ////
        //
        // NoData
        //
        ////
        Range nodata = null;
        param = CoverageProcessor.getParameter(parameters, "nodata");
        if (param != null) {
            nodata = (Range) param;
        }

        ////
        //
        // ImageWorker
        //
        ////
        final ImageWorker worker = new ImageWorker(source);
        worker.setRenderingHints(hints);
        worker.setROI(roi);
        worker.setNoData(nodata);
        worker.affine((AffineTransform) parameters.getObjectParameter("transform"), interpolation, (double[])
                parameters.getObjectParameter("backgroundValues"));
        return worker.getRenderedImage();
    }

    @Override
    protected void handleJAIEXTParams(ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source =
                (GridCoverage2D) parameters2.parameter("source0").getValue();
        handleROINoDataInternal(parameters, source, AFFINE, 3, 6);
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
        if (parameters.parameters.getNumParameters() > 3 && parameters.parameters.getObjectParameter(6) != null) {
            CoverageUtilities.setNoDataProperty(properties, background);
        }

        // Setting ROI if present
        PropertyGenerator propertyGenerator = null;
        if (data instanceof RenderedOp op) {
            String operationName = op.getOperationName();
            if (operationName.equalsIgnoreCase(AFFINE)
                    || operationName.equalsIgnoreCase(SCALE)
                    || operationName.equalsIgnoreCase(TRANSLATE)) {
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
