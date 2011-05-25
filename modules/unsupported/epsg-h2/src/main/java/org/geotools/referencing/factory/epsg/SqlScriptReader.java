/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Utility class extracting SQL statement out of a script file with multiline
 * statements (works with EPSG distributed scripts)
 *
 *
 *
 * @source $URL$
 */
public class SqlScriptReader {
    boolean fetched = true;
    StringBuilder builder = new StringBuilder();
    BufferedReader reader;

    public SqlScriptReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }
    
    public boolean hasNext() throws IOException {
        // do we have an un-fetched command?
        if(!fetched) {
            return builder.length() > 0;
        }
        
        builder.setLength(0);
        String line = null;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if(!"".equals(line))
                builder.append(line).append("\n");
            if(line.endsWith(";")) {
                fetched = false;
                break;
            }
        }
        
        if(line == null && builder.length() > 0) {
            throw new IOException("The file ends with a non ; terminated command");
        }
        
        return line != null;
    }
    
    public String next() throws IOException  {
        if(fetched)
            throw new IOException("hasNext was not called, or was called and it returned false");
            
        fetched = true;
        return builder.toString();
    }
    
    public void dispose() {
        try {
            reader.close();
        } catch(IOException e) {
            // never mind
        }
    }
    

}
