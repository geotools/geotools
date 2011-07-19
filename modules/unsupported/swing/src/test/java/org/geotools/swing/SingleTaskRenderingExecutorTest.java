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


import org.geotools.map.MapContent;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for SingleTaskRenderingExecutor.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class SingleTaskRenderingExecutorTest extends RenderingExecutorTestBase {
    
    @Before
    public void setup() {
        super.setup();
    }
    
    @Test
    public void shutdownExecutor() {
        assertFalse(executor.isShutdown());
        executor.shutdown();
        assertTrue(executor.isShutdown());
    }
    
    @Test(expected=IllegalStateException.class)
    public void submitAfterShutdown() {
        executor.shutdown();
        createSubmitObjects();
        executor.submit(mapContent, renderer, graphics, listener);
    }
    
    @Test
    public void setAndGetPollingInterval() {
        long poll = executor.getPollingInterval();
        executor.setPollingInterval(poll * 2);
        assertEquals(poll * 2, executor.getPollingInterval());
    }
    
    @Test
    public void invalidPollingInterval() {
        long poll = executor.getPollingInterval();
        
        // should be ignored
        executor.setPollingInterval(-1);
        assertEquals(poll, executor.getPollingInterval());
    }
    
}
