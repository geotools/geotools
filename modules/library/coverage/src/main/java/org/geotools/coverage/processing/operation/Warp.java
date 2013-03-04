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

import javax.media.jai.operator.WarpDescriptor;

import org.geotools.coverage.processing.BaseScaleOperationJAI;


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

    /** serialVersionUID */
    private static final long serialVersionUID = -9077795909705065389L;

    /**
     * Default constructor.
     */
    public Warp() {
        super("Warp");
    }

}
