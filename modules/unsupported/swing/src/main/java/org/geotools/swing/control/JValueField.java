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

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextField;

/**
 * Abstract base class for text field controls that work with a simple value such as {@code
 * JIntegerField}. Provides methods to add and remove listeners for value changes offering a simpler
 * API than messing about with Swing {@code KeyListener} and {@code DocumentListener} classes.
 *
 * @author Michael Bedward
 * @since 2.6.1
 * @version $Id$
 */
public abstract class JValueField extends JTextField {

    private final Set<ValueChangedListener> listeners;

    /** Default constructor */
    public JValueField() {
        listeners = new HashSet<ValueChangedListener>();
    }

    /**
     * Register a new listener. If the listener is already registered this method does nothing.
     *
     * @param listener the listener to register
     */
    public void addValueChangedListener(ValueChangedListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove the given listener. If the listener is not registered this method does nothing.
     *
     * @param listener the listener to remove
     */
    public void removeValueChangedListener(ValueChangedListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify listeners of a value change
     *
     * @param ev the event with details of the value change
     */
    protected void fireValueChangedEvent(ValueChangedEvent ev) {
        for (ValueChangedListener listener : listeners) {
            listener.onValueChanged(ev);
        }
    }
}
