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
package org.geotools.data.couchdb.client;

import java.io.IOException;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBException extends Exception {

    public CouchDBException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
    public IOException wrap() throws IOException {
        IOException ex = new IOException(getMessage());
        ex.setStackTrace(getStackTrace());
        if (getCause() != null) {
            ex.initCause(getCause());
        }
        throw ex;
    }
    
}
