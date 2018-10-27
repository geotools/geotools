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

import it.geosolutions.jaiext.range.Range;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PropertyGenerator;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.ImageWorker;
import org.geotools.util.factory.GeoTools;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * This operation is simply a wrapper for the JAI Affine operation
 *
 * @version $Id$
 * @author Simone Giannecchini
 * @since 12.0
 * @see javax.media.jai.operator.AffineDescriptor
 */
public class Affine extends BaseScaleOperationJAI {

    /** serialVersionUID */
    private static final long serialVersionUID = 1699623079343108288L;

    /** Default constructor. */
    public Affine() {
        super(AFFINE);
    }

    @Override
    protected RenderedImage createRenderedImage(
            ParameterBlockJAI parameters, RenderingHints hints) {
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
        worker.affine(
                (AffineTransform) parameters.getObjectParameter("transform"),
                interpolation,
                (double[]) parameters.getObjectParameter("backgroundValues"));
        return worker.getRenderedImage();
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source = (GridCoverage2D) parameters2.parameter("source0").getValue();
        handleROINoDataInternal(parameters, source, AFFINE, 3, 6);
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
                && parameters.parameters.getObjectParameter(6) != null) {
            CoverageUtilities.setNoDataProperty(properties, background);
        }

        // Setting ROI if present
        PropertyGenerator propertyGenerator = null;
        if (data instanceof RenderedOp) {
            String operationName = ((RenderedOp) data).getOperationName();
            if (operationName.equalsIgnoreCase(AFFINE)
                    || operationName.equalsIgnoreCase(SCALE)
                    || operationName.equalsIgnoreCase(TRANSLATE)) {
                propertyGenerator =
                        getOperationDescriptor(operationName)
                                .getPropertyGenerators(RenderedRegistryMode.MODE_NAME)[0];
            }
            if (propertyGenerator != null) {
                Object roiProp = propertyGenerator.getProperty(ROI, data);
                if (roiProp != null && roiProp instanceof ROI) {
                    CoverageUtilities.setROIProperty(properties, (ROI) roiProp);
                }
            }
        }

        return properties;
    }
}
