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

package org.geotools.swing;

import org.geotools.swing.testutils.MockRenderer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.testutils.WaitingRenderingExecutorListener;


/**
 * Base class for RenderingExecutor tests.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public abstract class RenderingExecutorTestBase {

    protected static final ReferencedEnvelope WORLD = 
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);
    
    protected static final Rectangle PANE = new Rectangle(200, 150);
    
    protected static final long WAIT_TIMEOUT = 500;
    
    protected RenderingExecutor executor;
    protected Graphics2D graphics;
    protected BufferedImage image;
    protected WaitingRenderingExecutorListener listener;
    protected MapContent mapContent;
    protected MockRenderer renderer;
    
    protected void setup() {
        executor = new DefaultRenderingExecutor();
        listener = new WaitingRenderingExecutorListener();
        mapContent = new MapContent();
        mapContent.getViewport().setBounds(WORLD);
        mapContent.getViewport().setScreenArea(PANE);
    }

    protected void createSubmitObjects() {
        image = new BufferedImage(SingleTaskRenderingExecutorTest.PANE.width, SingleTaskRenderingExecutorTest.PANE.height, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        renderer = new MockRenderer(mapContent);
    }

}
