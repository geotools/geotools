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
package org.geotools.data.efeature.internal;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;

/**
 * @author kengu - 6. juni 2011
 *
 *
 * @source $URL$
 */
public class EFeatureLogHandler extends ConsoleHandler {
    @Override
    protected void setOutputStream(OutputStream out) throws SecurityException {
        super.setOutputStream(System.out); // kitten killed here :-(
    }
}
