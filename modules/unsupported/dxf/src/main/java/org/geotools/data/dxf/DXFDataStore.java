/*
 * $Id: DXFDataStore.java Matthijs $
 */
package org.geotools.data.dxf;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.AbstractFileDataStore;
import org.geotools.data.FeatureReader;
import java.util.ArrayList;
import org.geotools.data.ServiceInfo;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * DataStore for reading a DXF file produced by Autodesk.
 * 
 * The attributes are always the same:
 * key: String
 * name: String
 * urlLink: String
 * entryLineNumber: Integer
 * parseError: Boolean
 * error: String
 *  * 
 * @author Chris van Lith B3Partners
 */
public class DXFDataStore extends AbstractFileDataStore {

    private static final Log log = LogFactory.getLog(DXFDataStore.class);
    private URL url;
    private FeatureReader featureReader;
    private String srs;
    private String strippedFileName;
    private String typeName;
    private ArrayList dxfInsertsFilter = new ArrayList();

    public DXFDataStore(URL url, String srs) throws IOException {
        this.url = url;
        this.strippedFileName = getURLTypeName(url);
        this.srs = srs;
    }

    public String[] getTypeNames() throws IOException {
        //return GeometryType.getTypeNames(strippedFileName, GeometryType.LINE, GeometryType.POINT, GeometryType.POLYGON);
        return GeometryType.getTypeNames(strippedFileName, GeometryType.ALL);
    }

    static String getURLTypeName(URL url) throws IOException {
        String file = url.getFile();
        if (file.length() == 0) {
            return "unknown_dxf";
        } else {
            int i = file.lastIndexOf('/');
            if (i != -1) {
                file = file.substring(i + 1);
            }
            if (file.toLowerCase().endsWith(".dxf")) {
                file = file.substring(0, file.length() - 4);
            }
            /* replace to make valid table names */
            file = file.replaceAll(" ", "_");
            return file;
        }
    }

    public void addDXFInsertFilter(String[] filteredNames) {
        dxfInsertsFilter.addAll(java.util.Arrays.asList(filteredNames));
    }

    public void addDXFInsertFilter(String filteredName) {
        dxfInsertsFilter.add(filteredName);
    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        // Update featureReader with typename and return SimpleFeatureType
        return (SimpleFeatureType) getFeatureReader(typeName).getFeatureType();
    }

    public SimpleFeatureType getSchema() throws IOException {
        if (typeName == null) {
            log.warn("Typename is null, probably because of using getFeatureSource().\n" +
                    "\tPlease use getFeatureSource(typename)");
        }
        return getSchema(typeName);
    }

    public FeatureReader getFeatureReader(String typeName) throws IOException {
        // Update featureReader for this typename
        resetFeatureReader(typeName);
        return featureReader;
    }

    public FeatureReader getFeatureReader() throws IOException {
        if (featureReader == null) {
            resetFeatureReader(typeName);
        }
        return featureReader;
    }

    public void resetFeatureReader(String typeName) throws IOException {
        if (typeName == null) {
            log.error("No typeName given for featureReader");
        } else {
            this.typeName = typeName;

            // Get geometryType from typeName (GeometryType)(typeName - fileName)
            String extension = typeName.replaceFirst(strippedFileName, "");
            GeometryType geometryType = GeometryType.getTypeByExtension(extension);

            if (featureReader == null) {
                try {
                    featureReader = new DXFFeatureReader(url, typeName, srs, geometryType, dxfInsertsFilter);
                } catch (DXFParseException e) {
                    throw new IOException("DXF parse exception" + e.getLocalizedMessage());
                }
            } else {
                ((DXFFeatureReader) featureReader).updateTypeFilter(typeName, geometryType, srs);
            }
        }
    }

    @Override
    public ServiceInfo getInfo() {
        try {
            return ((DXFFeatureReader) getFeatureReader()).getInfo();
        } catch (IOException ex) {
            return null;
        }
    }
}
