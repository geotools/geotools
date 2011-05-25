/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.exc;

import java.io.IOException;


/**
 * VPFDataException.java Created: Mon Mar 03 21:32:32 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 *
 * @source $URL$
 * @version $Id$
 */
public class VPFDataException extends IOException {
    /**
     * Creates a new VPFDataException object.
     */
    public VPFDataException() {
        super();
    }

    // VPFDataException constructor

    /**
     * Creates a new VPFDataException object.
     *
     * @param msg DOCUMENT ME!
     */
    public VPFDataException(String msg) {
        super(msg);
    }
}
