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

import org.geotools.renderer.GTRenderer;
import org.geotools.map.MapContent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for JMapPane methods that can run in a headless build.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/test/java/org/geotools/swing/MultiRepTestRunner.java $
 * @version $Id: MultiRepTestRunner.java 37668 2011-07-19 07:17:25Z mbedward $
 */
public class JMapPaneHeadlessTest {
    
    private JMapPane pane = new JMapPane();
    
    @Test
    public void defaultRenderingExecutorCreated() {
        RenderingExecutor executor = pane.getRenderingExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof SingleTaskRenderingExecutor);
    }
    
    @Test
    public void rendererLinkedToMapContent() {
        MapContent mapContent = new MapContent();
        pane.setMapContent(mapContent);
        
        GTRenderer renderer = new MockRenderer();
        pane.setRenderer(renderer);
        
        assertTrue(renderer.getMapContent() == mapContent);
    }
}
