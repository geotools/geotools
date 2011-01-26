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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.logging.Logger;

import org.geotools.renderer.style.shape.ExplicitBoundsShape;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * The WellKnownMarkFactory is used to hold the knolwedge of how to draw
 * all the marks hardboiled into the SLD specification (cross, arrow, triangle etc...)
 * 
 * @author James
 *
 * @source $URL$
 */
public class WellKnownMarkFactory implements MarkFactory {

    /** The logger for the rendering module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.rendering");

    /** Cross general path */
    private static Shape cross;

    /** Star general path */
    private static Shape star;

    /** Triangle general path */
    private static Shape triangle;

    /** Arrow general path */
    private static Shape arrow;

    /** X general path */
    private static Shape X;
    
    /** hatch path */
    static Shape hatch;
    
    /** square */
    private static Shape square;

    static {
    	GeneralPath crossPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        crossPath.moveTo(0.5f, 0.125f);
        crossPath.lineTo(0.125f, 0.125f);
        crossPath.lineTo(0.125f, 0.5f);
        crossPath.lineTo(-0.125f, 0.5f);
        crossPath.lineTo(-0.125f, 0.125f);
        crossPath.lineTo(-0.5f, 0.125f);
        crossPath.lineTo(-0.5f, -0.125f);
        crossPath.lineTo(-0.125f, -0.125f);
        crossPath.lineTo(-0.125f, -0.5f);
        crossPath.lineTo(0.125f, -0.5f);
        crossPath.lineTo(0.125f, -0.125f);
        crossPath.lineTo(0.5f, -0.125f);
        crossPath.lineTo(0.5f, 0.125f);
        
        cross = new ExplicitBoundsShape(crossPath);
        ((ExplicitBoundsShape)cross).setBounds(new Rectangle2D.Double(-.5,.5,1.,1.));

        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / 4.0);
        X = new ExplicitBoundsShape(crossPath.createTransformedShape(at));
        ((ExplicitBoundsShape)X).setBounds(new Rectangle2D.Double(-.5,.5,1.,1.));
        
        GeneralPath starPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        starPath.moveTo(0.191f, 0.0f);
        starPath.lineTo(0.25f, 0.344f);
        starPath.lineTo(0.0f, 0.588f);
        starPath.lineTo(0.346f, 0.638f);
        starPath.lineTo(0.5f, 0.951f);
        starPath.lineTo(0.654f, 0.638f);
        starPath.lineTo(1.0f, 0.588f); // max = 7.887
        starPath.lineTo(0.75f, 0.344f);
        starPath.lineTo(0.89f, 0f);
        starPath.lineTo(0.5f, 0.162f);
        starPath.lineTo(0.191f, 0.0f);
        at = new AffineTransform();
        at.translate(-.5, -.5);
        starPath.transform(at);
        
        star = new ExplicitBoundsShape(starPath);
        ((ExplicitBoundsShape)star).setBounds(new Rectangle2D.Double(-.5,.5,1.,1.));
        
        GeneralPath trianglePath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        trianglePath.moveTo(0f, 1f);
        trianglePath.lineTo(0.866f, -.5f);
        trianglePath.lineTo(-0.866f, -.5f);
        trianglePath.lineTo(0f, 1f);
        at = new AffineTransform();
        at.translate(0, -.25);
        at.scale(.5, .5);
        trianglePath.transform(at);
        
        triangle = new ExplicitBoundsShape(trianglePath);
        ((ExplicitBoundsShape)triangle).setBounds(new Rectangle2D.Double(-.5,.5,1.,1.));

        GeneralPath arrowPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        arrowPath.moveTo(0f, -.5f);
        arrowPath.lineTo(.5f, 0f);
        arrowPath.lineTo(0f, .5f);
        arrowPath.lineTo(0f, .1f);
        arrowPath.lineTo(-.5f, .1f);
        arrowPath.lineTo(-.5f, -.1f);
        arrowPath.lineTo(0f, -.1f);
        arrowPath.lineTo(0f, -.5f);
        
        arrow = new ExplicitBoundsShape(arrowPath);
        ((ExplicitBoundsShape)arrow).setBounds(new Rectangle2D.Double(-.5,.5,1.,1.));

        GeneralPath hatchPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        hatchPath.moveTo(.55f,.57f);
        hatchPath.lineTo(.52f,.57f);
        hatchPath.lineTo(-.57f,-.52f);
        hatchPath.lineTo(-.57f,-.57f);
        hatchPath.lineTo(-.52f, -.57f);
        hatchPath.lineTo(.57f, .52f);
        hatchPath.lineTo(.57f,.57f);
                
        hatchPath.moveTo(.57f,-.49f);
        hatchPath.lineTo(.49f, -.57f);
        hatchPath.lineTo(.57f,-.57f);
        hatchPath.lineTo(.57f,-.49f);
                
        hatchPath.moveTo(-.57f,.5f);
        hatchPath.lineTo(-.5f, .57f);
        hatchPath.lineTo(-.57f,.57f);
        hatchPath.lineTo(-.57f,.5f);
        
        hatch = new ExplicitBoundsShape(hatchPath); 
        ((ExplicitBoundsShape)hatch).setBounds(new Rectangle2D.Double(-.5,.5,1.,1.));
        
        square = new Double(-.5, -.5, 1., 1.);
    }

    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) throws Exception {
        // cannot handle a null url
        if(symbolUrl == null)
            return null;
        
        String wellKnownName = symbolUrl.evaluate(feature, String.class);
        
        LOGGER.finer("fetching mark of name " + wellKnownName);

        if (wellKnownName.equalsIgnoreCase("cross")) {
            LOGGER.finer("returning cross");

            return cross;
        }

        if (wellKnownName.equalsIgnoreCase("circle")) {
            LOGGER.finer("returning circle");

            return new java.awt.geom.Ellipse2D.Double(-.5, -.5, 1., 1.);
        }

        if (wellKnownName.equalsIgnoreCase("triangle")) {
            LOGGER.finer("returning triangle");

            return triangle;
        }

        if (wellKnownName.equalsIgnoreCase("X")) {
            LOGGER.finer("returning X");

            return X;
        }

        if (wellKnownName.equalsIgnoreCase("star")) {
            LOGGER.finer("returning star");

            return star;
        }

        if (wellKnownName.equalsIgnoreCase("arrow")) {
            LOGGER.finer("returning arrow");

            return arrow;
        }
        
        if (wellKnownName.equalsIgnoreCase("hatch")) {
            LOGGER.finer("returning hatch");
             
            return hatch;
        }
        
        if (wellKnownName.equalsIgnoreCase("square")) {
            LOGGER.finer("returning square");
             
            return square;
        }

        // failing that return a square?
        LOGGER.finer("Could not find the symbol, returning null");

        return null;
    }

}
