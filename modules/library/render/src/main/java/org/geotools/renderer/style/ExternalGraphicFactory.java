/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import javax.swing.Icon;

import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Symbol handler for an external symbolizers.
 *
 *
 * @source $URL$
 */
public interface ExternalGraphicFactory {
    /**
     * Turns the specified URL into an Icon, eventually using the Feature
     * attributes to evaluate CQL expressions embedded in the url.<br>
     * The <code>size</code> parameter defines the size of the image (so that
     * vector based symbols can be drawn at the specified size directly), or may
     * be zero or negative if the size was not specified (in that case the "natural" size of
     * the image will be used, which is the size in pixels for raster images, and
     * 16 for any format that does not have a specific size, according to the SLD spec).<br>
     * <code>null</code> will be returned if this factory cannot handle the
     * provided url.
     */
    public Icon getIcon(Feature feature, Expression url, String format, int size) throws Exception;
}
