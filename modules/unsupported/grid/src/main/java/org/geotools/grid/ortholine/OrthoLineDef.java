/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.ortholine;

/**
 * Defines how to generate a set of regularly-spaced, ortho-line elements with
 * given orientation and level.
 * 
 * @author mbedward
 * @since 8.0
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/grid/src/main/java/org/geotools/grid/ortholine/OrthoLineDef.java $
 * @version $Id: Grids.java 37149 2011-05-10 11:47:02Z mbedward $
 */
public class OrthoLineDef {

    private final int level;
    private final LineOrientation orientation;
    private final double spacing;

    /**
     * Creates a new ortho-line definition.
     * 
     * @param orientation line orientation
     * @param level an integer level (user-defined values) indicating line precedence
     * @param spacing the spacing between lines in world distance units
     */
    public OrthoLineDef(LineOrientation orientation, int level, double spacing) {
        this.level = level;
        this.orientation = orientation;
        this.spacing = spacing;
    }
    
    /**
     * Creates a copy of an existing line definition.
     * 
     * @param lineDef the definition to copy
     * @throws IllegalArgumentException if {@code lineDef} is {@code null}
     */
    public OrthoLineDef(OrthoLineDef lineDef) {
        if (lineDef == null) {
            throw new IllegalArgumentException("lineDef arg must not be null");
        }
        this.level = lineDef.level;
        this.orientation = lineDef.orientation;
        this.spacing = lineDef.spacing;
    }

    /**
     * Gets the integer level (line precedence).
     * 
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the orientation.
     * 
     * @return orientation
     */
    public LineOrientation getOrientation() {
        return orientation;
    }

    /**
     * Gets the spacing between lines.
     * 
     * @return line spacing
     */
    public double getSpacing() {
        return spacing;
    }

}
