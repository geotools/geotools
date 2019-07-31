/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Test suite for {@link SubProgressListener}
 *
 * @author groldan
 * @version $Id$
 */
public class SubProgressListenerTest {

    @Test
    public void testSubProgressStartComplete() {
        SimpleProgressListener parent = new SimpleProgressListener();
        SubProgressListener sub = new SubProgressListener(parent, 50);

        sub.started();
        assertEquals(0F, sub.getProgress(), 0f);
        sub.complete();
        assertEquals(100F, sub.getProgress(), 0f);
    }

    @Test
    public void testSubProgressStartFirstListener() {
        SimpleProgressListener parent = new SimpleProgressListener();
        SubProgressListener sub = new SubProgressListener(parent, 0, 50);
        sub.started();
        assertTrue(parent.getStarted());
    }

    @Test
    public void testSubProgressStartSubsequentListener() {
        SimpleProgressListener parent = new SimpleProgressListener();
        SubProgressListener sub = new SubProgressListener(parent, 50, 50);
        sub.started();
        assertFalse(parent.getStarted());
    }

    @Test
    public void testSubProgressBounds() {
        SimpleProgressListener parent = new SimpleProgressListener();

        parent.progress(50f);

        SubProgressListener sub = new SubProgressListener(parent, 50);

        sub.started();
        sub.progress(50f);

        assertEquals(50f, sub.getProgress(), 0f);
        assertEquals(75f, parent.getProgress(), 0f);

        sub.progress(100f);

        assertEquals(100f, sub.getProgress(), 0f);
        assertEquals(100f, parent.getProgress(), 0f);
    }

    private static class SimpleProgressListener implements ProgressListener {

        private float progress;
        private boolean startedCalled;

        public SimpleProgressListener() {
            this.startedCalled = false;
        }

        public void progress(float percent) {
            this.progress = percent;
        }

        public float getProgress() {
            return this.progress;
        }

        public void complete() {}

        public boolean getStarted() {
            return this.startedCalled;
        }

        public void started() {
            this.startedCalled = true;
        }

        public void dispose() {}

        public void exceptionOccurred(Throwable exception) {}

        public String getDescription() {
            return null;
        }

        public InternationalString getTask() {
            return null;
        }

        public boolean isCanceled() {
            return false;
        }

        public void setCanceled(boolean cancel) {}

        public void setDescription(String description) {}

        public void setTask(InternationalString task) {}

        public void warningOccurred(String source, String location, String warning) {}
    }
}
