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

package org.geotools.swing.testutils;

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;

/**
 * Listens for a window of specified class to be activated on the AWT event thread
 * and, when it appears, creates a FEST {@linkplain WindowFixture} object from it.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class WindowActivatedListener<T extends Window> implements AWTEventListener {

    private final Class<? extends Window> windowClass;
    private final CountDownLatch latch;
    private WindowFixture fixture;

    /**
     * Creates a new listener.
     * 
     * @param windowClass the class to listen for.
     */
    public WindowActivatedListener(Class<? extends Window> windowClass) {
        if (Frame.class.isAssignableFrom(windowClass)
                || Dialog.class.isAssignableFrom(windowClass)) {
            this.windowClass = windowClass;

        } else {
            throw new UnsupportedOperationException(
                    windowClass.getName() + " is not supported");
        }

        this.latch = new CountDownLatch(1);
    }

    /**
     * Checks if an event pertains to this listener's target window class
     * and is of type {@linkplain WindowEvent#WINDOW_ACTIVATED}.
     * 
     * @param event an event
     */
    @Override
    public void eventDispatched(AWTEvent event) {
        if (fixture == null) {
            Object source = event.getSource();
            if (windowClass.isAssignableFrom(source.getClass())
                    && event.getID() == WindowEvent.WINDOW_ACTIVATED) {

                if (source instanceof Frame) {
                    fixture = new FrameFixture((Frame) source);
                } else if (source instanceof Dialog) {
                    fixture = new DialogFixture((Dialog) source);
                }

                latch.countDown();
            }
        }
    }

    /**
     * Gets the {@linkplain WindowFixture} created by this listener if available.
     * 
     * @param timeOutMillis maximum waiting time in milliseconds
     * @return the fixture or {@code null} if the time-out expires
     * 
     * @throws InterruptedException on interruption while waiting for the fixture to
     *     become available
     */
    public WindowFixture getFixture(long timeOutMillis) throws InterruptedException {
        if (latch.await(timeOutMillis, TimeUnit.MILLISECONDS)) {
            return fixture;
        } else {
            return null;
        }
    }
}
