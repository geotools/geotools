/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.awt.image.BufferedImage;
import java.util.List;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Graphic;

/**
 *
 * @author  jamesm
 *
 * @source $URL$
 */
public interface GlyphRenderer {
 
    public boolean canRender(String format);
    public List getFormats();
    /**
     * 
     * @param graphic
     * @param eg
     * @param feature
     * @param height use <=0 if you dont want any scaling done.  THIS MIGHT BE IGNORED by the renderer!
     */
    public BufferedImage render(Graphic graphic, ExternalGraphic eg, Object feature, int height);
    
}
