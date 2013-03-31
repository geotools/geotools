/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.GraphicLegend;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class StyledShapePainterTest extends TestCase {

    public void testGraphicLegendNullLegend() throws Exception {
        StyledShapePainter painter = new StyledShapePainter();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform);
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 10));
        LiteShape2 pointShape = new LiteShape2(point, transform, decimator, false);
        try {
            painter.paint(g2, pointShape, (GraphicLegend) null, 1, false);
            fail();
        } catch (NullPointerException e) {
            assertEquals("ShapePainter has been asked to paint a null legend!!", e.getMessage());
        }
    }

    public void testGraphicLegend() throws Exception {

        StyledShapePainter painter = new StyledShapePainter();
        GraphicImpl legend = new GraphicImpl(CommonFactoryFinder.getFilterFactory(GeoTools
                .getDefaultHints()));
        legend.setRotation(0);
        legend.setOpacity(1);
        Style pStyle = RendererBaseTest.loadStyle(this, "externalGraphic.sld");
        URL url = StreamingRenderer.class.getResource("test-data/");
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        ExternalGraphic eg = sf.createExternalGraphic(url + "icon64.png", "image/png");
        legend.addExternalGraphic(eg);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        MathTransform transform = ProjectiveTransform.create(new AffineTransform());
        Decimator decimator = new Decimator(transform);
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 10));
        LiteShape2 pointShape = new LiteShape2(point, transform, decimator, false);
        painter.paint(g2, pointShape, legend, 1, false);
        // check it is correctly painted
        assertEquals(0, image.getRaster().getSample(0, 0, 0));
        assertEquals(64, image.getRaster().getSample(0, 0, 1));
        assertEquals(128, image.getRaster().getSample(0, 0, 2));

        assertEquals(255, image.getRaster().getSample(30, 30, 0));
        assertEquals(255, image.getRaster().getSample(30, 30, 1));
        assertEquals(255, image.getRaster().getSample(30, 30, 2));

        assertEquals(0, image.getRaster().getSample(90, 30, 0));
        assertEquals(0, image.getRaster().getSample(90, 30, 1));
        assertEquals(0, image.getRaster().getSample(90, 30, 2));
        
        //check with a scaling factor
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) image.getGraphics();
        painter.paint(g2, pointShape, legend, 2, false);
        assertEquals(255, image.getRaster().getSample(0, 0, 0));
        assertEquals(255, image.getRaster().getSample(0, 0, 1));
        assertEquals(192, image.getRaster().getSample(0, 0, 2));

        assertEquals(0, image.getRaster().getSample(30, 30, 0));
        assertEquals(0, image.getRaster().getSample(30, 30, 1));
        assertEquals(0, image.getRaster().getSample(30, 30, 2));

        assertEquals(0, image.getRaster().getSample(90, 30, 0));
        assertEquals(0, image.getRaster().getSample(90, 30, 1));
        assertEquals(0, image.getRaster().getSample(90, 30, 2));


    }

}
