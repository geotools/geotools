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

package org.geotools.swing.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * Static utility methods for common dialog and GUI related tasks.
 * 
 * @author Michael Bedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class DialogUtils {

    /**
     * Shows a dialog centred on the screen. May be called safely
     * from any thread.
     * 
     * @param dialog the dialog
     */
    public static void showCentred(final Window dialog) {
        showCentredOnParent(null, dialog);
    }
    
    /**
     * Shows a dialog centred on its parent. May be called safely
     * from any thread. If {@code parent} is {@code null} the dialog
     * is centred on the screen.
     * 
     * @param parent the parent component
     * @param dialog the dialog
     */
    public static void showCentredOnParent(final Window parent, final Window dialog) {
        if (EventQueue.isDispatchThread()) {
            doShowCentred(parent, dialog);
            
        } else {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    doShowCentred(parent, dialog);
                }
            });
        }
    }

    /**
     * Gets all child components that are, or derive from, the given class.
     * This method is adapted from the SwingUtils class written by Darryl Burke.
     * (Accessed from: http://tips4java.wordpress.com/2008/11/13/swing-utils/).
     *
     * @param <T> Swing type derived from JComponent
     * @param clazz the component class
     * @param parent the parent container
     * @param includeNested whether to recursively collect nested components
     *
     * @return list of child components
     */
    public static <T extends JComponent> List<T> getChildComponents(
            Class<T> clazz, Container parent, boolean includeNested) {

        List<T> children = new ArrayList<T>();

        for (Component c : parent.getComponents()) {
            boolean isClazz = clazz.isAssignableFrom(c.getClass());
            if (isClazz) {
                children.add(clazz.cast(c));
            }
            if (includeNested && c instanceof Container) {
                children.addAll(getChildComponents(clazz, (Container) c, includeNested));
            }
        }

        return children;
    }

    private static void doShowCentred(Window parent, Window dialog) {
        if (parent == null) {
            doCentre(dialog, Toolkit.getDefaultToolkit().getScreenSize());
        } else {
            doCentre(dialog, parent.getSize());
        }
        dialog.setVisible(true);
    }
     
    private static void doCentre(Window dialog, Dimension parentDim) {
        Dimension dialogDim = dialog.getSize();
        int x = Math.max(0, parentDim.width / 2 - dialogDim.width / 2);
        int y = Math.max(0, parentDim.height / 2 - dialogDim.height / 2);
        dialog.setLocation(x, y);
    }
}
