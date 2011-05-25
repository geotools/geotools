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
package org.geotools.legend;

import java.awt.Color;

import javax.swing.Icon;

import org.geotools.map.MapLayer;
import org.geotools.styling.Rule;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Used to draw the little pictures that appear in a Legend.
 * <p>
 * We are making this an interface so that applications can implement their
 * own icons as needed.
 * </p>
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public interface GlyphFactory {
    
    /**
     * Produce a simple Icon representing a point.
     * @param point Color of the Point
     * @param fill Color inside the Point
     * @return Icon representing a Point
     */
    Icon point(Color point, Color fill);
    
    /**
     * Produce a simple Icon representing a point.
     * <p>
     * At a minimum this code is the same as:<pre><code>
     * PointSymbolizer symbolizer = SLD.pointSymbolizer( rule );
     * return glyphFactory.point( SLD.pointColor( symbolizer ), SLD.fillColor( symbolizer ) );
     * </code></pre>
     * <p>
     * Implementations have the option of going into greater detail, picking up on
     * TextSymbolizers and so on.
     * </p> 
     * @param rule Rule used to render a Point
     * @return Icon representing a Point
     */
    Icon point(Rule rule);
    
    /**
     * Produces a simple Icon representing a line.
     * @param line Line colour
     * @param width Line width
     * @return
     */
    Icon line(Color line, int width);
    
    /**
     * Produce a simple Icon representing a point.
     * <p>
     * At a minimum this code is the same as:<pre><code>
     * LineSymbolizer symbolizer = SLD.lineSymbolizer( rule );
     * return glyphFactory.point( SLD.lineColor( symbolizer ), SLD.lineWidth( symbolizer ) );
     * </code></pre>
     * <p>
     * Implementations have the option of going into greater detail, picking up on
     * TextSymbolizers and so on.
     * </p> 
     * @param rule Rule used to render a Point
     * @return Icon representing a Point
     */
    Icon line(Rule rule);
    
    Icon geometry(Color color, Color fill);
    Icon geometry(Rule rule);
    
    Icon polygon(Color color, Color fill, int width);
    Icon polygon(Rule rule);
    
    Icon grid(Color color1, Color color2, Color color3, Color color4);
    
    Icon swatch(Color color);
    
    Icon palette(Color[] colors);
    
    /**
     * Make a basic representation of the provided FeatureType.
     * 
     * @param schema
     * @return
     */
    Icon icon(SimpleFeatureType schema);
    
    /**
     * Glyph for the provided layer.
     * <p>
     * At a minimum the icon will be based on:
     * <ul>
     * <li>layer schema, will be considered a generic geometry if not recognized
     * <li>layer style, defaults will be used if not recognized
     * </ul>
     * 
     * @param layer
     * @return Icon For the provided layer
     */
    Icon icon(MapLayer layer);
}
