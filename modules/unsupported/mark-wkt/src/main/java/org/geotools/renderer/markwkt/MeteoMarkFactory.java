/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.markwkt;

import org.geotools.renderer.style.MarkFactory;
import org.geotools.renderer.style.shape.ExplicitBoundsShape;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Adds to the well-known shapes some symbols the weathermen may find useful. 
 * 
 * @author Luca Morandini lmorandini@ieee.org
 * @version $Id$
 */
public class MeteoMarkFactory implements MarkFactory {

    public static final String SHAPE_PREFIX = "extshape://";

    /** The logger for the rendering module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MeteoMarkFactory.class);

    protected final static Map<String, Shape> WELLKNOWN_SHAPES = new HashMap<String, Shape>();

    static {
        GeneralPath gp = new GeneralPath();
        
        gp = new GeneralPath();
        ExplicitBoundsShape bnd = null;
        
        gp.moveTo(-0.145f, 0.000f);
        gp.lineTo(0.000f, 0.175f);
        gp.lineTo(0.105f, 0.000f);
        gp.closePath();
        bnd= new ExplicitBoundsShape(gp);
        bnd.setBounds(new Rectangle2D.Double(-0.5, -0.5, 0.5, 0.5));
        WELLKNOWN_SHAPES.put("triangle", bnd);

        gp = new GeneralPath();
        gp.moveTo(-0.125f, 0.000f);
        gp.curveTo(-0.125f, 0.000f, 0.000f, 0.250f, 0.125f, 0.000f);
        gp.closePath();
        bnd= new ExplicitBoundsShape(gp);
        bnd.setBounds(new Rectangle2D.Double(-0.5, -0.5, 0.5, 0.5));
        WELLKNOWN_SHAPES.put("emicircle", bnd);

        gp = new GeneralPath();
        gp.moveTo(-0.395f, 0.000f);
        gp.lineTo(-0.250f, -0.175f);
        gp.lineTo(-0.145f, 0.000f);
        gp.moveTo(0.125f, 0.000f);
        gp.curveTo(0.125f, 0.000f, 0.250f, 0.250f, 0.375f, 0.000f);
        gp.closePath();
        bnd= new ExplicitBoundsShape(gp);
        bnd.setBounds(new Rectangle2D.Double(-0.5, -0.5, 1.0, 1.0));
        WELLKNOWN_SHAPES.put("triangleemicircle", bnd);     
        
        gp = new GeneralPath();
        gp.moveTo(0f, 0.5f);
        gp.lineTo(0.5, -0.5f);
        gp.lineTo(0.1f, -0.5f);
        gp.lineTo(0.1f, -2.0f);
        gp.lineTo(-0.1f, -2.0f);
        gp.lineTo(-0.1f, -0.5f);
        gp.lineTo(-0.5f, -0.5f);
        gp.closePath();
        ExplicitBoundsShape narrow = new ExplicitBoundsShape(gp);
        narrow.setBounds(new Rectangle2D.Double(-1.2, -0.3, 1, 0.6));
        WELLKNOWN_SHAPES.put("narrow", narrow);   
     }

    /*
     * Return a shape with the given url.
     * @see org.geotools.renderer.style.MarkFactory#getShape(java.awt.Graphics2D, org.opengis.filter.expression.Expression, org.opengis.feature.Feature)
     */
    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) throws Exception {
        // cannot handle a null url
        if(symbolUrl == null)
            return null;
 
        // see if it's a shape
        if(LOGGER.isLoggable(Level.FINE)){
        	LOGGER.fine("Trying to resolve symbol:"+symbolUrl.toString());
        }
        String wellKnownName = symbolUrl.evaluate(feature, String.class);
        if(wellKnownName==null||!wellKnownName.startsWith(SHAPE_PREFIX)) {
            // see if it's a shape
            if(LOGGER.isLoggable(Level.FINE)){
            	LOGGER.fine("Unable to resolve symbol");
            }        	
            return null;
        }
        
        if(LOGGER.isLoggable(Level.FINE)){
        	LOGGER.fine("Resolved symbol");
        }           
        return WELLKNOWN_SHAPES.get(wellKnownName.substring(SHAPE_PREFIX.length()));
    }

}
