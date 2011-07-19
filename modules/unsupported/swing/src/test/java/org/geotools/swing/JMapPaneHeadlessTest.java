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
import org.geotools.renderer.GTRenderer;
import org.geotools.map.MapContent;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for JMapPane methods that can run in a headless build.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class JMapPaneHeadlessTest {
    private JMapPane pane;
    
    @Before
    public void setup() {
        pane = new JMapPane();
    }
    
    @Test
    public void defaultRenderingExecutorCreated() {
        RenderingExecutor executor = pane.getRenderingExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof SingleTaskRenderingExecutor);
    }
    
    @Test
    public void settingRendererLinksToMapContent() {
        MapContent mapContent = new MapContent();
        pane.setMapContent(mapContent);
        
        GTRenderer renderer = new MockRenderer();
        pane.setRenderer(renderer);
        
        assertTrue(renderer.getMapContent() == mapContent);
    }
    
    @Test
    public void settingMapContentLinksToRenderer() {
        GTRenderer renderer = new MockRenderer();
        pane.setRenderer(renderer);
        
        MapContent mapContent = new MapContent();
        pane.setMapContent(mapContent);
        
        assertTrue(renderer.getMapContent() == mapContent);
    }
    
    @Test
    public void setRendererEvent() {
        
        
        GTRenderer renderer = new MockRenderer();
        pane.setRenderer(renderer);
        
    }
}
