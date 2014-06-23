/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;

import org.geotools.coverage.processing.BaseScaleOperationJAI;
import org.geotools.factory.GeoTools;
import org.geotools.image.ImageWorker;

/**
 * This operation is simply a wrapper for the JAI Affine operation
 * 
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 * @since 12.0
 * 
 * @see javax.media.jai.operator.AffineDescriptor
 */
public class Affine extends BaseScaleOperationJAI {

    /** serialVersionUID */
    private static final long serialVersionUID = 1699623079343108288L;

    /**
     * Default constructor.
     */
    public Affine() {
        super("Affine");

    }

    @Override
    protected RenderedImage createRenderedImage(ParameterBlockJAI parameters, RenderingHints hints) {
        final RenderedImage source = (RenderedImage) parameters.getSource(0);
        if(hints== null){
            hints= GeoTools.getDefaultHints().clone();
        }
                
        ////
        //
        // Interpolation
        //
        ////
        final Interpolation interpolation;
        if (parameters.getObjectParameter("interpolation") != null){
            interpolation = (Interpolation) parameters.getObjectParameter("interpolation");
        }else if (hints.get(JAI.KEY_INTERPOLATION) != null){
            interpolation = (Interpolation) hints.get(JAI.KEY_INTERPOLATION);
        }
        else {
            // I am pretty sure this should not happen. However I am not sure we should throw an error
            interpolation = null;
        }
        
        
        ////
        //
        // ImageWorker
        //
        ////
        final ImageWorker worker= new ImageWorker(source);
        worker.setRenderingHints(hints);
        worker.affine(
                (AffineTransform)parameters.getObjectParameter("transform") , 
                interpolation, 
                (double[])parameters.getObjectParameter("backgroundValues"));
        return worker.getRenderedImage();
    }

}
