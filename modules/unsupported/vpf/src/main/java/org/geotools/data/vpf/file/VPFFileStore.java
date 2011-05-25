/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.FeatureReader;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * A data store for VPF files. Any file can be retrieved from here.
 * If you want joins (for example features with their geometries), 
 * you will have to look elsewhere.
 * Hopefully some one will take advantage of this class to provide 
 * the full functionality.
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class VPFFileStore extends AbstractDataStore {
    /**
     * A collection of files which are available
     * Don't ask me how/when to close them!
     */
    private Map files;

    /**
     * Default constructor. Nothing special
     *
     */
    public VPFFileStore() {
        files = new HashMap();
    }

    /* (non-Javadoc)
     * @see org.geotools.data.AbstractDataStore#getTypeNames()
     */
    public String[] getTypeNames() {
        String[] result = new String[files.size()];
        int counter = 0;
        VPFFile currentFile;
        Iterator iter = files.keySet().iterator();

        while (iter.hasNext()) {
            currentFile = (VPFFile) iter.next();
            result[counter] = currentFile.getTypeName();
        }

        return result;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.AbstractDataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(String pathName) throws IOException {
        SimpleFeatureType result = null;

        if (files.containsKey(pathName)) {
            result = (SimpleFeatureType) files.get(pathName);
        } else {
            try {
                result = findFile(pathName);
            } catch (SchemaException exc) {
                throw new IOException("Schema error in path: " + pathName
                    + "\n" + exc.getMessage());
            }

            files.put(pathName, result);
        }

        return result;
    }

    // How on earth does one get from the query to this method?
    /* (non-Javadoc)
     * @see org.geotools.data.AbstractDataStore#getFeatureReader(java.lang.String)
     */
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String pathName)
        throws IOException {
        return new VPFFileFeatureReader((VPFFile) getSchema(pathName));
    }
    /**
     * Closes all of the opoen files and removes them from the collection of
     * open files.
     *
     */
    public void reset(){
        Iterator iter = files.values().iterator();
        VPFFile file = null;
        while(iter.hasNext()){
            try {
                file = (VPFFile)iter.next();
                file.close();
            } catch (Exception exc) {
                // No idea why this might happen
                exc.printStackTrace();
            }
        }
        files.clear();
    }

    /**
     * This does basically a case independent file search
     * through the pathName to try to find the file.
     * This is necessary due to many problems seen with VPF
     * data disks where the file names specified in the VPF files
     * do not match the case of the files on disk.  
     **/
    private VPFFile findFile(String pathName)
        throws IOException, SchemaException {

        if (new File(pathName).exists())
            return new VPFFile(pathName);

        ArrayList matches = new ArrayList();
        matches.add("");  // Need to start with something in the list
        StringTokenizer st = new StringTokenizer(pathName, File.separator);
        while (st.hasMoreTokens() && !matches.isEmpty()) {
            String curr = st.nextToken();
            String currUpper = curr.toUpperCase();
            String currLower = curr.toLowerCase();
            boolean useUpper = !curr.equals(currUpper);
            boolean useLower = !curr.equals(currLower);
            ArrayList newMatches = new ArrayList();
            
            for(Iterator it = matches.iterator(); it.hasNext(); ) {
                String match = (String)it.next();
                String tmp = match + File.separator + curr;

                if (new File(tmp).exists())
                    newMatches.add(tmp);
                else {
                    // For performance reasons only do the upper and lower case checks
                    // if the "as-is" check fails.  
                    if (useUpper) {
                        tmp = match + File.separator + currUpper;
                        if (new File(tmp).exists())
                            newMatches.add(tmp);
                    }
                
                    if (useLower) {
                        tmp = match + File.separator + currLower;
                        if (new File(tmp).exists())
                            newMatches.add(tmp);
                    }
                }
            }
            matches = newMatches;
        }

        if (matches.isEmpty()) {
            throw new FileNotFoundException("Could not find file: " + pathName);
        }
        
        return new VPFFile((String)matches.get(0));
    }
}
