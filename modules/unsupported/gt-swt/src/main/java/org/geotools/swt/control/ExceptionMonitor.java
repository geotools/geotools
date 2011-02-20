/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility which enables exception messages to be displayed.
 *
 * @since 2.7
 * @author Andrea Antonello (www.hydrologis.com)
 */
public final class ExceptionMonitor {
    /**
     * The creation of {@code ExceptionMonitor} class objects is forbidden.
     */
    private ExceptionMonitor() {
    }

    /**
     * Displays an error message for the specified exception. Note that this method can
     * be called from any thread (not necessarily the <cite>Swing</cite> thread).
     *
     * @param parent The parent {@link Shell}.
     * @param exception Exception which has been thrown and is to be reported to the user.
     */
    public static void show( final Shell parent, final Throwable exception ) {
        show(parent, exception, exception.getLocalizedMessage());
    }

    /**
     * Displays an error message for the specified exception. Note that this method can
     * be called from any thread (not necessarily the <cite>Swing</cite> thread).
     *
     * @param parent The parent {@link Shell}.
     * @param exception Exception which has been thrown and is to be reported to the user.
     * @param message Message to display. 
     */
    @SuppressWarnings("nls")
    public static void show( final Shell parent, final Throwable exception, final String message ) {
        if (Display.getCurrent() != null) {
            Status status = new Status(IStatus.ERROR, "My Plug-in ID", 0, "Status Error Message", exception);
            ErrorDialog.openError(parent, "", message, status); 
        } else {
            Display.getDefault().asyncExec(new Runnable(){
                public void run() {
                    Status status = new Status(IStatus.ERROR, "My Plug-in ID", 0, "Status Error Message", exception);
                    ErrorDialog.openError(parent, "", message, status);
                }
            });
        }
    }

    /**
     * Display a dummy exception. This method is provided only as an easy
     * way to test the dialog appearance from the command line.
     * @param args ignored
     */
    public static void main( final String[] args ) {
        show(new Shell(Display.getDefault()), new Exception());
    }
}
