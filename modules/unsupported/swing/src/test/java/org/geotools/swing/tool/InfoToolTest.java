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

package org.geotools.swing.tool;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.vividsolutions.jts.geom.Geometry;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.Layer;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.TestDataUtils;

import org.fest.swing.core.MouseButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import static org.junit.Assert.*;

/**
 * Tests for the info cursor tool.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class InfoToolTest extends CursorToolTestBase {

    private static final Random rand = new Random();
    
    private InfoTool tool;
    private CountDownLatch latch;
    private String reporterText;
    
    @Before
    public void setup() {
        // We override onReporterUpdated to give tests access to
        // the reporter text
        tool = new InfoTool() {
            @Override
            public void onReporterUpdated() {
                reporterText = getTextReporterConnection().getText();
                if (latch != null) {
                    latch.countDown();
                }
            }
        };
    }
    
    @Test
    public void doesNotDrawDragBox() throws Exception {
        assertFalse(tool.drawDragBox());
    }
    
    @Test
    public void queryPointFeature() throws Exception {
        Layer layer = mapContent.layers().get(0);
        SimpleFeatureSource fs = (SimpleFeatureSource) layer.getFeatureSource();
        
        final int featureIndex = rand.nextInt( fs.getFeatures().size() );
        SimpleFeatureIterator iter = fs.getFeatures().features();
        SimpleFeature feature = null;
        try {
            int i = 0;
            while (i <= featureIndex) {
                feature = iter.next();
                i++ ;
            }
            
        } finally {
            iter.close();
        }
        
        
        DirectPosition2D queryPos = TestDataUtils.getPosInFeature(feature);
        Point2D p2d = mapPane.getWorldToScreenTransform().transform(queryPos, null);
        
        Point windowOrigin = mapPaneFixture.component().getLocationOnScreen();
        Point screenQueryPos = new Point(
                windowOrigin.x + (int) p2d.getX(),
                windowOrigin.y + (int) p2d.getY());
        
        mapPane.setCursorTool(tool);
        latch = new CountDownLatch(1);
        mapPaneFixture.robot.click(screenQueryPos, MouseButton.LEFT_BUTTON, 1);

        assertTrue(latch.await(EVENT_TIMEOUT, TimeUnit.MILLISECONDS));
        assertNotNull(reporterText);
        assertTrue(reporterText.length() > 0);
        
        String[] lines = reporterText.split("(\r|\n)+");
        
        // line 0 is world position - don't check this because it will
        // probably differ a little to the mouse position set above
        
        // line 1 is layer name
        String name = layer.getTitle();
        if (name == null || name.length() == 0) {
            name = fs.getSchema().getTypeName();
        }
        assertTrue(lines[1].contains(name));
        
        // line 2 is feature id
        assertTrue(lines[2].contains(feature.getID()));
        
        // lines 3 onwards are feature attributes
        int lineNo = 3;
        for (Property p : feature.getProperties()) {
            assertTrue(lines[lineNo].contains(p.getName().getLocalPart()));
            
            Object value = p.getValue();
            if (value instanceof Geometry) {
                assertTrue(lines[lineNo].contains("Point"));
            } else {
                assertTrue(lines[lineNo].contains(value.toString()));
            }
            
            lineNo++ ;
        }
    }

    @Override
    protected Layer getTestLayer() throws Exception {
        return TestDataUtils.getPointLayer();
    }

}
