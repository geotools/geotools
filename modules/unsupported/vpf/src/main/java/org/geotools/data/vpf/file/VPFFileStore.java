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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.vpf.VPFFeatureSource;
import org.geotools.data.vpf.VPFLogger;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * A data store for VPF files. Any file can be retrieved from here. If you want joins (for example
 * features with their geometries), you will have to look elsewhere. Hopefully some one will take
 * advantage of this class to provide the full functionality.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFFileStore extends ContentDataStore {
    /** A collection of files which are available Don't ask me how/when to close them! */
    private Map<String, SimpleFeatureType> files;

    private String vpfFilePath;

    /** Default constructor. Nothing special */
    public VPFFileStore(String vpfFilePath) throws IOException {
        files = new HashMap<String, SimpleFeatureType>();

        this.vpfFilePath = vpfFilePath;
        this.getTypeSchema(vpfFilePath);
    }

    public String getVpfFilePath() {
        return this.vpfFilePath;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.ContentDataStore#getNames()
     */
    public List<Name> getNames() {
        // String[] result = new String[files.size()];
        ArrayList<Name> result = new ArrayList<Name>();
        // int counter = 0;
        SimpleFeatureType currentFile;
        Iterator<SimpleFeatureType> iter = files.values().iterator();

        while (iter.hasNext()) {
            currentFile = iter.next();
            // result[counter] = currentFile.getTypeName();
            result.add(new NameImpl(currentFile.getTypeName()));
        }

        return result;
    }

    public SimpleFeatureType getFeatureType(ContentEntry entry) throws IOException {
        String typeName = entry.getTypeName();
        return this.getTypeSchema(typeName);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        // return Collections.singletonList(getTypeName());
        return this.getNames();
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        String typeName = entry.getTypeName();
        if (typeName == null) {
            return null;
        } else {
            return getFeatureSource(typeName);
        }
    }

    @Override
    public ContentFeatureSource getFeatureSource(String typeName) throws IOException {

        VPFFeatureSource featureSource = VPFFeatureSource.getFeatureSource(typeName);
        if (featureSource == null) {
            featureSource = VPFFeatureSource.getFeatureSource(typeName.toUpperCase());
        }

        if (featureSource == null) {
            if (VPFLogger.isLoggable(Level.FINEST)) {
                VPFLogger.log("VPFFileStore.getFeatureSource returned null: " + typeName);
            }
            Query query = new Query(typeName);
            ContentEntry entry = this.entry(new NameImpl(typeName));
            featureSource = new VPFFileFeatureSource(entry, query);
        }
        return featureSource;
    }

    public SimpleFeatureType getTypeSchema(String pathName) throws IOException {
        SimpleFeatureType result = null;

        if (files.containsKey(pathName)) {
            result = (SimpleFeatureType) files.get(pathName);
        } else {
            try {
                VPFFile file = findFile(pathName);
                result = file.getFeatureType();
            } catch (SchemaException exc) {
                throw new IOException(
                        "Schema error in path: " + pathName + "\n" + exc.getMessage());
            }

            files.put(pathName, result);
        }

        return result;
    }

    public VPFFile getFile(String pathName) throws IOException {
        SimpleFeatureType featureType = this.getTypeSchema(pathName);
        if (featureType == null) return null;
        VPFFile file = (VPFFile) featureType.getUserData().get(VPFFile.class);
        return file;
    }

    /** Closes all of the opoen files and removes them from the collection of open files. */
    public void reset() {
        Iterator<SimpleFeatureType> iter = files.values().iterator();
        VPFFile file = null;
        while (iter.hasNext()) {
            try {
                SimpleFeatureType schema = iter.next();
                file = (VPFFile) schema.getUserData().get(VPFFile.class);
                file.close();
            } catch (Exception exc) {
                // No idea why this might happen
                exc.printStackTrace();
            }
        }
        files.clear();
    }

    /**
     * This does basically a case independent file search through the pathName to try to find the
     * file. This is necessary due to many problems seen with VPF data disks where the file names
     * specified in the VPF files do not match the case of the files on disk.
     */
    private VPFFile findFile(String pathName) throws IOException, SchemaException {

        if (new File(pathName).exists()) return new VPFFile(pathName);

        ArrayList<String> matches = new ArrayList<String>();
        matches.add(""); // Need to start with something in the list
        StringTokenizer st = new StringTokenizer(pathName, File.separator);
        while (st.hasMoreTokens() && !matches.isEmpty()) {
            String curr = st.nextToken();
            String currUpper = curr.toUpperCase();
            String currLower = curr.toLowerCase();
            boolean useUpper = !curr.equals(currUpper);
            boolean useLower = !curr.equals(currLower);
            ArrayList<String> newMatches = new ArrayList<String>();

            for (Iterator<String> it = matches.iterator(); it.hasNext(); ) {
                String match = (String) it.next();
                String tmp = match + File.separator + curr;

                if (new File(tmp).exists()) newMatches.add(tmp);
                else {
                    // For performance reasons only do the upper and lower case checks
                    // if the "as-is" check fails.
                    if (useUpper) {
                        tmp = match + File.separator + currUpper;
                        if (new File(tmp).exists()) newMatches.add(tmp);
                    }

                    if (useLower) {
                        tmp = match + File.separator + currLower;
                        if (new File(tmp).exists()) newMatches.add(tmp);
                    }
                }
            }
            matches = newMatches;
        }

        if (matches.isEmpty()) {
            throw new FileNotFoundException("Could not find file: " + pathName);
        }

        return new VPFFile((String) matches.get(0));
    }
}
