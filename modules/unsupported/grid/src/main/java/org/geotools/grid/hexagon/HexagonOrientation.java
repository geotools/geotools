/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid.hexagon;

/**
 * Constants to describe the orientation of a {@code Hexagon}.
 * <ul>
 * <li>
 * An {@code ANGLED} element has a "pointy" top with a single vertex
 * touching the upper edge of its bounding rectangle.
 * </li>
 * <li>
 * A {@code FLAT} element has edges that run along the upper and
 * lower edges of its bounding rectangle
 * </li>
 * </ul>
 *
 * @author michael
 */
public enum HexagonOrientation {
    /**
     * An {@code ANGLED} element has a "pointy" top with a single vertex
     * touching the upper edge of its bounding rectangle.
     */
    ANGLED, 
    
    /**
     * A {@code FLAT} element has edges that run along the upper and
     * lower edges of its bounding rectangle
     */ 
    FLAT
    
}
