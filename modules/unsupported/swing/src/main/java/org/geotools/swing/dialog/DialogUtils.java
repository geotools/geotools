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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Static utility methods for common dialog-related tasks.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: $
 * @version $Id: $
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
                @Override
                public void run() {
                    doShowCentred(parent, dialog);
                }
            });
        }
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
