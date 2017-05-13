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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.TimeZone;

import org.geotools.data.DataUtilities;

/**
 * Basic Factory class of Dbase index file managers (To speedup alphanumeric filters in a DBF-file).
 * 
 * @author Alvaro Huarte
 */
public abstract class DbaseFileIndexFactoryImpl implements DbaseFileIndexFactory {
    
    /**
     * Returns a valid URL of the specified Dbase index file. 
     * @throws MalformedURLException 
     */
    protected static java.net.URL extractUrlIndexFile(URL urlDbaseFile, String fileExtension) throws MalformedURLException {
        
        if (urlDbaseFile.getProtocol().equalsIgnoreCase("file")) { //-> It is Local?
            
            File   dbaseFile     = DataUtilities.urlToFile(urlDbaseFile);
            String dbaseFileName = dbaseFile.getPath();
            String dbaseFilePath = dbaseFileName.substring(0, dbaseFileName.lastIndexOf('.'));
            File   indexFile     = null;
            
            // We check if the index file has a previous modified date than the main dbf file.
            if ((indexFile = new File(dbaseFilePath+fileExtension)).exists()) {
                long dbfLastModifiedTime = dbaseFile.lastModified();
                long idxLastModifiedTime = indexFile.lastModified();
                if (dbfLastModifiedTime<=idxLastModifiedTime || Math.abs(dbfLastModifiedTime-idxLastModifiedTime)<500)
                return indexFile.toURI().toURL();
            }
        }
        else {
            String dbaseFileName = urlDbaseFile.toString();
            String dbaseFilePath = dbaseFileName.substring(0, dbaseFileName.lastIndexOf('.'));
            
            try {
                URL urlIndexFile = new URL(dbaseFilePath+fileExtension);
                InputStream ixstream = urlIndexFile.openConnection().getInputStream();
                ixstream.close();
                
                return urlIndexFile;
            }
            catch (IOException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Returns a supported index manager of the specified Dbase file.
     * @throws IOException
     */
    public abstract DbaseFileIndex getIndexManager(URL urlDbaseFile, Charset charset, TimeZone timeZone) throws IOException;
}
