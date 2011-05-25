/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.event;

import java.util.EventObject;

import org.geotools.swing.JMapPane;

/**
 * An event class used by {@code JMapPane} to signal changes of
 * state to listeners.
 *
 * @see MapPaneListener
 * 
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class MapPaneEvent extends EventObject {
    /**
     * Type of MapPane event
     */
    public static enum Type {
        /**
         * The map pane has set a new context.
         */
        NEW_CONTEXT,

        /**
         * The map pane has set a new renderer.
         */
        NEW_RENDERER,

        /**
         * The map pane has been resized.
         */
        PANE_RESIZED,

        /**
         * The display area has been changed. This can
         * include both changes in bounds and in the
         * coordinate reference system.
         */
        DISPLAY_AREA_CHANGED,

        /**
         * The map pane has started rendering features.
         */
        RENDERING_STARTED,

        /**
         * The map pane has stopped rendering features.
         */
        RENDERING_STOPPED,

        /**
         * The map pane is rendering features. The event
         * will carry data that can be retrieved as a floating
         * point value between 0 and 1.
         */
        RENDERING_PROGRESS;
    }

    /** Type of mappane event */
    private Type type;

    /** Data associated with some event types */
    private Object data;

    /**
     * Constructor for an event with no associated data
     *
     * @param source the map pane issuing this event
     * @param type the type of event
     */
    public MapPaneEvent(JMapPane source, Type type) {
        super(source);
        this.type = type;
    }

    /**
     * Constructor for an event with associated data. The new event
     * object takes ownership of the data object.
     *
     * @param source the map pane issuing this event
     * @param type the type of event
     * @param data the event data
     */
    public MapPaneEvent(JMapPane source, Type type, Object data) {
        super(source);
        this.type = type;
        this.data = data;
    }

    /**
     * Get the type of this event
     * @return event type 
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the data associated with this event, if any
     *
     * @return event data or {@code null} if not applicable
     */
    public Object getData() {
        return data;
    }
}
