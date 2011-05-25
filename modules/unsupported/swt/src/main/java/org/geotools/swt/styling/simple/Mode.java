/* uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.swt.styling.simple;

/**
 * Captures the current *mode* of the style configurator making use
 * of a StyleViewer.
 * <p>
 * This is used to let the viewers get modey, and disable fill content
 * when working with linestrings for example.
 * </p>
 * 
 * @author Jody Garnett
 * @since 1.0.0
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swt/src/main/java/org/geotools/swt/styling/simple/Mode.java $
 */
public enum Mode { 
    /** <code>POINT</code> Mode - editing a Point or MultiPoint. */
    POINT, 
    /** <code>LINE</code> Mode - editing a Linestring or MultiLineString. */
    LINE, 
    /** <code>POLYGON</code> Mode - editing a Polygone or MultiPolygon. */
    POLYGON, 
    /** <code>ALL</code> Mode - editing a Geometry. */
    ALL, 
    /** <code>NONE</code> Mode - content cannot be styled by SLD (like scalebar) */
    NONE
}
