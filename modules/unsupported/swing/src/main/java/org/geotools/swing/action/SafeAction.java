/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swing.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.geotools.swing.dialog.JExceptionReporter;

/**
 * A safe version of AbstractAction that will log any problems encountered.
 *
 * <p>This is not generally a good practice - we are just using it as an excuse to not mess up code
 * examples with exception handling code (gasp!). TODO: provide a background Runnable...
 */
public abstract class SafeAction extends AbstractAction {
    private static final long serialVersionUID = 1118122797759176800L;

    /**
     * Constructor
     *
     * @param name name for the associated control
     */
    public SafeAction(String name) {
        super(name);
    }

    /**
     * Sub-classes (usually anonymous) must override this method instead of the usual {@linkplain
     * javax.swing.Action#actionPerformed}
     *
     * @param e the action event
     * @throws Throwable on error
     */
    public abstract void action(ActionEvent e) throws Throwable;

    /**
     * Calls the {@linkplain #action } method
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            action(e);
        } catch (Throwable t) {
            JExceptionReporter.showDialog(t);
        }
    }
}
