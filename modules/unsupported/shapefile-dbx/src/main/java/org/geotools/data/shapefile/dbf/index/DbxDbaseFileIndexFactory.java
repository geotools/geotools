/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.TimeZone;

import org.geotools.data.shapefile.dbf.index.DbaseFileIndex;
import org.geotools.data.shapefile.dbf.index.DbaseFileIndexFactory;
import org.geotools.data.shapefile.dbf.index.DbaseFileIndexFactoryImpl;

/**
 * Factory class of a Dbase DBX-index file manager.
 * 
 * @author Alvaro Huarte
 */
public class DbxDbaseFileIndexFactory extends DbaseFileIndexFactoryImpl implements DbaseFileIndexFactory {
    
    /**
     * Returns a supported index manager of the specified Dbase file.
     * @throws IOException
     */
    @Override
    public DbaseFileIndex getIndexManager(URL urlDbaseFile, Charset charset, TimeZone timeZone) throws IOException {
        
        URL urlIndexFile = null;
        
        /* Exist a supported Dbase DBX-index file? 
         */
        if ((urlIndexFile = extractUrlIndexFile(urlDbaseFile, ".dbx"))!=null) {
            DbaseFileIndex dbaseIndex = new DbxDbaseFileIndex(urlDbaseFile, urlIndexFile, charset, timeZone);
            return dbaseIndex;
        }
        return null;
    }
}
