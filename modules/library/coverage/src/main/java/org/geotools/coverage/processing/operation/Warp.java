/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.image.RenderedImage;

import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.operator.WarpDescriptor;

import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.coverage.processing.CoverageProcessingException;
import org.geotools.factory.Hints;
import org.opengis.coverage.Coverage;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;


/**
 * This operation is simply a wrapper for the JAI Warp operation
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 * @since 9.0
 * 
 * @see WarpDescriptor
 */
public class Warp extends BaseScaleOperationJAI {

        @Override
    public Coverage doOperation(ParameterValueGroup parameters, Hints hints)
            throws CoverageProcessingException {
        // TODO Auto-generated method stub
        return super.doOperation(parameters, hints);
    }

        /** serialVersionUID */
    private static final long serialVersionUID = -9077795909705065389L;

    /**
     * Default constructor.
     */
    public Warp() {
        super("Warp");
    }

    @Override
    protected ParameterBlockJAI prepareParameters(ParameterValueGroup parameters) {
        // look for background values 
        final ParameterValue<?> parameter = parameters.parameter("backgroundValues");
        return super.prepareParameters(parameters);
    }

    @Override
    protected RenderedImage createRenderedImage(ParameterBlockJAI parameters, RenderingHints hints) {
        // TODO Auto-generated method stub
        return super.createRenderedImage(parameters, hints);
    }

}
