/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swt.control;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Abstract base class for text field controls that work with a simple
 * value such as {@code JIntegerField}.
 *
 * @author Michael Bedward
 * @author Andrea Antonello (www.hydrologis.com)
 */
public abstract class JValueField extends Text {

    private final Set<ValueChangedListener> listeners;

    public JValueField( Composite parent, int style ) {
        super(parent, style);
        listeners = new HashSet<ValueChangedListener>();
    }

    /**
     * Register a new value changed listener. 
     *
     * @param listener the listener to register.
     */
    public void addValueChangedListener( ValueChangedListener listener ) {
        listeners.add(listener);
    }

    /**
     * Remove the given listener.
     *
     * @param listener the listener to unregister.
     */
    public void removeValueChangedListener( ValueChangedListener listener ) {
        listeners.remove(listener);
    }

    /**
     * Notify listeners of a value change.
     *
     * @param ev the event with details of the value change.
     */
    protected void fireValueChangedEvent( ValueChangedEvent< ? > ev ) {
        for( ValueChangedListener listener : listeners ) {
            listener.onValueChanged(ev);
        }
    }

}
