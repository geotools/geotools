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
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.PropertyGenerator;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.image.jai.Registry;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * This operation is simply a wrapper for the JAI scale operation which allows me to arbitrarily
 * scale and translate a rendered image.
 *
 * @version $Id$
 * @author Simone Giannecchini
 * @since 2.3
 * @see javax.media.jai.operator.ScaleDescriptor
 */
public class Scale extends BaseScaleOperationJAI {

    /** Serial number for cross-version compatibility. */
    private static final long serialVersionUID = -3212656385631097713L;

    /** Lock for unsetting native acceleration. */
    private static final int[] lock = new int[1];

    /** Default constructor. */
    public Scale() {
        super("Scale");
    }

    @Override
    protected RenderedImage createRenderedImage(
            ParameterBlockJAI parameters, RenderingHints hints) {
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

        final JAI processor = OperationJAI.getJAI(hints);
        PlanarImage image;
        if (interpolation != null
                && !(interpolation instanceof InterpolationNearest)
                && (transferType == DataBuffer.TYPE_FLOAT
                        || transferType == DataBuffer.TYPE_DOUBLE)) {

            synchronized (lock) {

                /**
                 * Disables the native acceleration for the "Scale" operation. In JAI 1.1.2, the
                 * "Scale" operation on TYPE_FLOAT datatype with INTERP_BILINEAR interpolation cause
                 * an exception in the native code of medialib, which halt the Java Virtual Machine.
                 * Using the pure Java implementation instead resolve the problem.
                 *
                 * @todo Remove this hack when Sun will fix the medialib bug. See
                 *     http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4906854
                 */
                Registry.setNativeAccelerationAllowed(getName(), false);
                image = processor.createNS(getName(), parameters, hints).getRendering();

                /** see above */
                Registry.setNativeAccelerationAllowed(getName(), true);
            }

        } else image = processor.createNS(getName(), parameters, hints);

        return image;
    }

    protected void handleJAIEXTParams(
            ParameterBlockJAI parameters, ParameterValueGroup parameters2) {
        GridCoverage2D source = (GridCoverage2D) parameters2.parameter("source0").getValue();
        handleROINoDataInternal(parameters, source, SCALE, 5, 7);
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

        if (parameters.parameters.getNumParameters() > 5
                && parameters.parameters.getObjectParameter(8) != null) {
            // Setting NoData property if needed
            Object bkgProp = parameters.parameters.getObjectParameter(8);
            if (bkgProp != null && bkgProp instanceof double[]) {
                double[] background = (double[]) bkgProp;
                CoverageUtilities.setNoDataProperty(properties, background);
            }
        }

        // Setting ROI if present
        if (data instanceof RenderedOp) {
            String operationName = ((RenderedOp) data).getOperationName();
            PropertyGenerator propertyGenerator = null;
            if (operationName.equalsIgnoreCase(SCALE)
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
