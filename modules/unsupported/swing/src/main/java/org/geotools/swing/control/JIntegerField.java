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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A text field control to work with integer values. It can be constrained to positive values if
 * desired. It also provides an API for listening to value changes that is simpler than messing
 * about with Swing {@code KeyListener} and {@code DocumentListener}.
 * <p>
 * Example of use:
 * <pre><code>
 * int initialValue = ...
 * boolean allowNegatives = false;
 * JIntegerField control = new JIntegerField(initialValue, allowNegatives);
 * control.addValueChangedListener( new ValueChangedListener() {
 *     public void onValueChanged( ValueChangedEvent ev ) {
 *         System.out.println("The new value is " + ev.getValue());
 *     }
 * });
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.6.1
 * @source $URL$
 * @version $Id$
 */
public class JIntegerField extends JValueField {

    private IntegerDocument document;
    private boolean fireEvents;

    /**
     * Creates a new text field that allows negative values and
     * has an initial value of 0.
     */
    public JIntegerField() {
        this(0, true);
    }

    /**
     * Creates a new text field with an initial value of 0.
     *
     * @param allowNegative true if this field should allow negative values to
     *        be entered; false if only positive values are allowed
     */
    public JIntegerField(boolean allowsNegative) {
        this(0, allowsNegative);
    }

    /**
     * Creates a new text field that allows negative values and
     * the given initial value.
     *
     * @param value the initial value to display
     */
    public JIntegerField(int value) {
        this(value, true);
    }

    /**
     * Creates a new text field with the given initial value.
     *
     * @param value the initial value to display
     * @param allowNegative true if this field should allow negative values to
     *        be entered; false if only positive values are allowed
     */
    public JIntegerField(int value, boolean allowNegative) {
        this.document = new IntegerDocument(allowNegative);
        setDocument(document);
        setValue(value);

        document.addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                if (fireEvents) {
                    ValueChangedEvent<Integer> ev = new ValueChangedEvent<Integer>(
                            JIntegerField.this, Integer.valueOf(document.getValue()));

                    fireValueChangedEvent( ev );
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (fireEvents) {
                    ValueChangedEvent<Integer> ev = new ValueChangedEvent<Integer>(
                            JIntegerField.this, Integer.valueOf(document.getValue()));

                    fireValueChangedEvent( ev );
                }
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    /**
     * Get the current value of this control.
     *
     * @return the current value
     */
    public int getValue() {
        return document.getValue();
    }

    /**
     * Set the integer value of this control. A {@code ValueChangedEvent} will be
     * published to all {@code ValueChangedListeners}.
     * 
     * @param value the value to set
     *
     * @throws IllegalArgumentException if {@code value} is negative but the field
     *         only allows positive values
     */
    public void setValue(int value) {
        setValue(value, true);
    }

    /**
     * Set the integer value of this control, optionally skipping notification of
     * the change to listeners.
     * <p>
     * This version is useful when two or more controls are synchronized (ie. changes
     * to the value of one control results in changes to the values of other controls).
     * In such a setting, firing change events can result in an endless cycle or a
     * mutex violation.
     *
     * @param value the value to set
     * @param publishEvent true to notify listeners of this change; false to skip
     *        notification
     *
     * @throws IllegalArgumentException if {@code value} is negative but the field
     *         only allows positive values
     */
    public void setValue(int value, boolean publishEvent) {
        fireEvents = publishEvent;

        if (!document.getAllowsNegative() && value < 0) {
            throw new IllegalArgumentException(
                    String.format("Negative value (%d) but text field set to only allow positive values", value));
        }
        setText(String.valueOf(value));
        fireEvents = true;
    }

}
