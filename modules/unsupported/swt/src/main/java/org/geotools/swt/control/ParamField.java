/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.net.URL;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.geotools.data.Parameter;
import org.locationtech.jts.geom.Geometry;

/**
 * Super class that provides additional helper methods useful when implementing your own
 * ParamWidget.
 *
 * @author gdavis
 */
public abstract class ParamField {

    protected Parameter<?> parameter;
    protected Composite parent;

    /**
     * Holds on to the parameter so implementations can consult the type and metadata information.
     *
     */
    ParamField(Composite parent, Parameter<?> parameter) {
        this.parent = parent;
        this.parameter = parameter;
    }

    /**
     * Called to build the widget, initialize it (setting defaults or whatever) and setup any
     * listeners needed for validation of the widget value. The returned JComponent will contain the
     * widget for editing.
     *
     * @return JComponent or null if error
     */
    public abstract Control doLayout();

    /**
     * Validates the current value of the widget, returns false if not valid, true otherwise
     *
     * @return boolean if validated
     */
    public abstract boolean validate();

    /**
     * Sets the value of the widget.
     *
     * @param Object an object containing the value to set for the widget
     */
    public abstract void setValue(Object value);

    /**
     * Returns the current value of the widget.
     *
     * @return Object representing the current value of the widget
     */
    public abstract Object getValue();

    /**
     * Factory method creating the appropriate ParamField for the supplied Param.
     *
     */
    public static ParamField create(Composite parent, Parameter<?> parameter) {
        if (Double.class.isAssignableFrom(parameter.type)) {
            return new JDoubleField(parent, parameter);
        } else if (URL.class.isAssignableFrom(parameter.type)) {
            if (parameter.metadata != null && parameter.metadata.get(Parameter.EXT) != null) {
                return new JURLField(parent, parameter);
            } else {
                JField field = new JField(parent, parameter);
                field.setSingleLine(true);
                return field;
            }
        } else if (Boolean.class.isAssignableFrom(parameter.type)) {
            JField field = new JField(parent, parameter);
            field.setSingleLine(true);
            return field;
        } else if (Number.class.isAssignableFrom(parameter.type)) {
            JField field = new JField(parent, parameter);
            field.setSingleLine(true);
            return field;
        } else if (File.class.isAssignableFrom(parameter.type)) {
            return new JFileField(parent, parameter);
        } else if (Geometry.class.isAssignableFrom(parameter.type)) {
            return new JGeometryField(parent, parameter);
        } else {
            // We got nothing special hope the converter api can deal
            return new JField(parent, parameter);
        }
    }
}
