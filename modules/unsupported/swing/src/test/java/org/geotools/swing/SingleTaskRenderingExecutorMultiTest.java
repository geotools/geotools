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

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Tests for SingleTaskRenderingExecutor that involve multi-threading.
 * These are run using the MultiRepTestRunner class. Edit the constant field
 * in that class to set the number of replicates to run.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(MultiRepTestRunner.class)
public class SingleTaskRenderingExecutorMultiTest extends RenderingExecutorTestBase {
    
    private static final Random rand = new Random();
    
    @Before
    public void setup() {
        super.setup();
    }
    
    @After
    public void cleanup() {
        //executor.shutdown();
    }
    
    @Test
    public void submitAndGetStartedEvent() {
        createSubmitObjects();
        listener.setExpected(WaitingListener.EventType.STARTED);
        executor.submit(mapContent, renderer, graphics, listener);
        boolean gotEvent = listener.await(WaitingListener.EventType.STARTED, WAIT_TIMEOUT);
        assertTrue(gotEvent);
    }
    
    @Test
    public void submitAndGetCompletedEvent() {
        createSubmitObjects();
        listener.setExpected(WaitingListener.EventType.COMPLETED);
        executor.submit(mapContent, renderer, graphics, listener);
        boolean gotEvent = listener.await(WaitingListener.EventType.COMPLETED, WAIT_TIMEOUT);
        assertTrue(gotEvent);
    }

    @Test
    public void cancelTaskAndGetEvent() {
        createSubmitObjects();
        
        // set random painting time
        long time = (long) (WAIT_TIMEOUT * 0.5 * rand.nextDouble());
        renderer.setPaintTime(time);
        renderer.setVerbose(false);
        
        listener.setExpected(WaitingListener.EventType.STARTED);
        listener.setExpected(WaitingListener.EventType.CANCELLED);
        
        long id = executor.submit(mapContent, renderer, graphics, listener);
        assertTrue(listener.await(WaitingListener.EventType.STARTED, WAIT_TIMEOUT));
        
        executor.cancel(id);
        boolean gotCancel = listener.await(WaitingListener.EventType.CANCELLED, WAIT_TIMEOUT);
        assertTrue(gotCancel || listener.gotEvent(WaitingListener.EventType.COMPLETED));
    }

}
