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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A decorator that logs the Input to the Logger as input is accessed.
 * 
 * @author Jesse
 * @since 1.1.0
 * @deprecated just remove this class, no longer in use
 */
class LogInputStream extends InputStream {
    private InputStream delegate;
    private Level level;

    StringBuffer buffer=new StringBuffer("Input: ");
    private Logger logger; 
    
    public LogInputStream( InputStream in, Logger logger, Level logLevel ) {
        this.delegate=in;
        this.logger=logger;
        this.level=logLevel;
    }

    public void close() throws IOException {
        delegate.close();
        logger.log(level, buffer.toString());
        buffer=new StringBuffer("Input: ");
    }

    public int read() throws IOException {
        int result=delegate.read();
        buffer.append((char) result); 
        return result;
    }


}
