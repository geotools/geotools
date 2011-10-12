/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

/**
 * Implemented by objects that wish to receive events published
 * by a {@code JTextReporter}.
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $URL$
 */
public interface TextReporterListener {

    /**
     * Called by the reporter when it is being closed
     */
    public void onReporterClosed();

    /**
     * Called by the text reporter when text has been appended
     */
    public void onReporterUpdated();
}
