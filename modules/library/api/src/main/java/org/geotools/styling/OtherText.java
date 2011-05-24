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
package org.geotools.styling;

import org.opengis.filter.expression.Expression;

/**
 * Allows open ended extensions for text oriented rendering formats. 
 * For example, with target="kml:extrude" one could specify the height of a building in KML
 * and activate 3D rendering, or with "kml:time" one could specify the time or timespan 
 * associated to the 
 * @author Andrea Aime - TOPP
 *
 *
 *
 * @source $URL$
 */
public interface OtherText {
    /**
     * The target location for the text. It can be anything, but it's up to the renderer 
     * to decide whether it can be used or not (some paths can be used, others are not understood)
     * @return
     */
    public String getTarget();
    
    public void setTarget(String target);
    
    /**
     * The text expression to be used in the specified location. Normally it should be a string,
     * but a renderer may accept something different (a number, a date)
     * @return
     */
    public Expression getText();
    
    public void setText(Expression otherText);
}
