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
package org.geotools.resources;

import java.awt.event.WindowListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;


/**
 * Wraps a {@link WindowListener} into an {@link InternalFrameListener}. This is used
 * by {@link SwingUtilities} in order to have the same methods working seemless on both
 * {@link java.awt.Frame} and {@link javax.swing.JInternalFrame}.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class InternalWindowListener implements InternalFrameListener {
    /**
     * The underlying {@link WindowListener}.
     */
    private final WindowListener listener;

    /**
     * Wrap the specified {@link WindowListener} into an {@link InternalFrameListener}.
     * If the specified object is already an {@link InternalFrameListener}, then it is
     * returned as-is.
     */
    public static InternalFrameListener wrap(final WindowListener listener) {
        if (listener == null) {
            return null;
        }
        if (listener instanceof InternalFrameListener) {
            return (InternalFrameListener) listener;
        }
        return new InternalWindowListener(listener);
    }

    /**
     * Construct a new {@link InternalFrameListener}
     * wrapping the specified {@link WindowListener}.
     */
    private InternalWindowListener(final WindowListener listener) {
        this.listener = listener;
    }

    /**
     * Invoked when a internal frame has been opened.
     */
    public void internalFrameOpened(InternalFrameEvent event) {
        listener.windowOpened(null);
    }

    /**
     * Invoked when an internal frame is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    public void internalFrameClosing(InternalFrameEvent event) {
        listener.windowClosing(null);
    }

    /**
     * Invoked when an internal frame has been closed.
     */
    public void internalFrameClosed(InternalFrameEvent event) {
        listener.windowClosed(null);
    }

    /**
     * Invoked when an internal frame is iconified.
     */
    public void internalFrameIconified(InternalFrameEvent event) {
        listener.windowIconified(null);
    }

    /**
     * Invoked when an internal frame is de-iconified.
     */
    public void internalFrameDeiconified(InternalFrameEvent event) {
        listener.windowDeiconified(null);
    }

    /**
     * Invoked when an internal frame is activated.
     */
    public void internalFrameActivated(InternalFrameEvent event) {
        listener.windowActivated(null);
    }

    /**
     * Invoked when an internal frame is de-activated.
     */
    public void internalFrameDeactivated(InternalFrameEvent event) {
        listener.windowDeactivated(null);
    }
}
