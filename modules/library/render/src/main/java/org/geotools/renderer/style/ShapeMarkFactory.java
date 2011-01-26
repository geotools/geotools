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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.renderer.style.shape.ExplicitBoundsShape;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

public class ShapeMarkFactory implements MarkFactory {

    private static final String SHAPE_PREFIX = "shape://";

    /** The logger for the rendering module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.rendering");

    static Map<String, Shape> shapes = new HashMap<String, Shape>();

    static {
       shapes.put("vertline", new Line2D.Double(0, -0.5, 0, 0.5));
       shapes.put("horline", new Line2D.Double(-0.5, 0, 0.5, 0));
       shapes.put("slash", new Line2D.Double(-0.5, -0.5, 0.5, 0.5));
       shapes.put("backslash", new Line2D.Double(-0.5, 0.5, 0.5, -0.5));
       
       ExplicitBoundsShape dotShape = new ExplicitBoundsShape(new Ellipse2D.Double(-0.000001, -0.000001, 0.000001, 0.000001));
       dotShape.setBounds(new Rectangle2D.Double(-0.5,0.5,1.0,1.0));
       shapes.put("dot", dotShape);
       
       GeneralPath gp = new GeneralPath();
       gp.moveTo(-0.5f, 0);
       gp.lineTo(0.5f, 0);
       gp.moveTo(0, -0.5f);
       gp.lineTo(0, 0.5f);
       shapes.put("plus", gp);
       
       gp = new GeneralPath();
       gp.moveTo(-0.5f, 0.5f);
       gp.lineTo(0.5f, -0.5f);
       gp.moveTo(-0.5f, -0.5f);
       gp.lineTo(0.5f, 0.5f);
       shapes.put("times", gp);
       
       gp = new GeneralPath();
       gp.moveTo(-0.5f, 0.2f);
       gp.lineTo(0, 0);
       gp.lineTo(-0.5f, -0.2f);
       ExplicitBoundsShape oarrow = new ExplicitBoundsShape(gp);
       oarrow.setBounds(new Rectangle2D.Double(-0.5, -0.5, 1, 1));
       shapes.put("oarrow", oarrow);
       
       gp = new GeneralPath();
       gp.moveTo(-0.5f, 0.2f);
       gp.lineTo(0, 0);
       gp.lineTo(-0.5f, -0.2f);
       gp.closePath();
       ExplicitBoundsShape carrow = new ExplicitBoundsShape(gp);
       carrow.setBounds(new Rectangle2D.Double(-0.5, -0.5, 1, 1));
       shapes.put("carrow", carrow);
    }

    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) throws Exception {
        // cannot handle a null url
        if(symbolUrl == null)
            return null;

        // see if it's a shape://
        String wellKnownName = symbolUrl.evaluate(feature, String.class);
        if(!wellKnownName.startsWith(SHAPE_PREFIX))
            return null;
        
        String name = wellKnownName.substring(SHAPE_PREFIX.length());
        return shapes.get(name);
    }

}
