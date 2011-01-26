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

package org.geotools.swing.control;

import java.awt.Component;

/**
 * An event published when the value of a control derived from {@code JValueField}
 * changes.
 *
 * @see JValueField
 * @see ValueChangedListener
 *
 * @author Michael Bedward
 * @since 2.6.1
 * @source $URL$
 * @version $Id$
 */
public class ValueChangedEvent<T> {

    private Component source;
    private T newValue;

    /**
     * Create a value changed event
     * @param source the control holding the value
     * @param newValue the updated value
     */
    public ValueChangedEvent(Component source, T newValue) {
        this.newValue = newValue;
        this.source = source;
    }

    /**
     * Get the control that invoked this event
     *
     * @return the invoking control
     */
    public Component getSource() {
        return source;
    }

    /**
     * Get the updated value
     *
     * @return the updated value
     */
    public T getValue() {
        return newValue;
    }
}
