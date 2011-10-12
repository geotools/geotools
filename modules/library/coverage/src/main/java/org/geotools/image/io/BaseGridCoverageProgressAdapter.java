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
package org.geotools.image.io;


import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.util.ProgressListener;
/**
 * Base class for GridCoverageProgressAdapter implementations 
 * to reporto progress about I/O operations with GridCoverages.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public abstract class BaseGridCoverageProgressAdapter {

    protected int lastImageIndex;

    protected final ProgressListener monitor;

    protected final int numImages;

    protected float progressStep;

    protected float progress;

    protected BaseGridCoverageProgressAdapter(ProgressListener monitor, int numImages) {
        if(numImages<=0)
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"numImages",numImages));
        this.numImages = numImages;
        if(monitor==null)
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"monitor"));       
        this.monitor = monitor;        
        init();        
    }

    protected void init() {
        progressStep=1.f/numImages;
    }

}