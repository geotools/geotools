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

package org.geotools.data.vpf;

import static org.geotools.data.vpf.ifc.FileConstants.*;
import static org.geotools.data.vpf.ifc.VPFLibraryIfc.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/*
 * A data store for a VPF library. A library is identified by
 * an LHT file.
 * <pre>
 * VPF 101:
 * Product: a profile of VPF data
 * Examples include DNC, VMAP, and VITD.
 *
 * Database: the world is separated into different databases.
 * A database typically fits on one CD.
 *
 * Library: each database is subdivided into libraries.
 * Libraries may be organized into coverage groups
 * which have different scales.
 * The BROWSE library typically has information on the other libraries.
 *
 * Coverage: associated feature types are grouped in coverages.
 * Coverages share a common topological model.
 *
 * Feature class: Feature types are grouped into classes.
 * Feature types in a class share a common set of attributes
 * and are stored together on disk.
 *
 * Feature type: Feature types are denoted by a five-character FACC code.
 * Feature types and feature classes are stored
 * at the same directory level, inside their containing coverages.
 *
 * File: VPF data is stored on disk in a complex hierarchy of flat files.
 * The VPFFile ContentDataStore can be used to extract the contents of
 * an individual file, but this is typically useful only for testing.
 * </pre>
 *
 * @since April 2004, 14:53
 *
 * @author  <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @source $URL$
 */
/** @source $URL$ */
public class VPFLibrary extends ContentDataStore {

    /** Part of bounding box. */
    private final double xmin;
    /** Part of bounding box. */
    private final double ymin;
    /** Part of bounding box. */
    private final double xmax;
    /** Part of bounding box. */
    private final double ymax;
    /** The directory containing the library */
    private final File directory;
    /** The name of the library */
    private final String libraryName;
    /** The coverages that are in the library */
    private final List coverages = new Vector();
    /** The coordinate reference system used through this library */
    private CoordinateReferenceSystem crs;
    /** Signals if an error has already been logged for a CRS related exception */
    private boolean loggedCRSException = false;

    private final Map<String, VPFFeatureType> typeMap = new HashMap<>();

    static URI DEFAULT_NAMESPACE;

    static {
        try {
            DEFAULT_NAMESPACE = new URI("http://www.vpf.org/default");
        } catch (java.net.URISyntaxException urise) {
            throw new RuntimeException("programmer error making default uri");
        }
    }

    /**
     * The namespace to create FeatureTypes with. Set with a reasonable default of
     * http://vpf.org/default.
     */
    private URI namespace = DEFAULT_NAMESPACE;

    /**
     * Complete constructor
     *
     * @param libraryFeature a feature from the library attribute table
     * @param dir the containing directory
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    /*
    public VPFLibrary(SimpleFeature libraryFeature, File dir, URI namespace)
            throws IOException, SchemaException {
        xmin = ((Number) libraryFeature.getAttribute(FIELD_XMIN)).doubleValue();
        ymin = ((Number) libraryFeature.getAttribute(FIELD_YMIN)).doubleValue();
        xmax = ((Number) libraryFeature.getAttribute(FIELD_XMAX)).doubleValue();
        ymax = ((Number) libraryFeature.getAttribute(FIELD_YMAX)).doubleValue();
        libraryName = libraryFeature.getAttribute(FIELD_LIB_NAME).toString();
        directory = new File(dir, libraryName);
        this.namespace = namespace != null ? namespace : DEFAULT_NAMESPACE;
        setCoverages();
    }
    */

    /**
     * Constructor that adds a namespace to the File only constructor. If using another constructor
     * then use setNamespace(URI) ((javadocTODO: add the correct link to previous method.))
     *
     * @param dir the containing directory
     * @throws SchemaException for problems making a featureType.
     */
    /*
    public VPFLibrary(File dir) throws IOException, SchemaException {
        this(dir, DEFAULT_NAMESPACE);
    }
    */

    /**
     * Constructor which defaults the containing database to null and looks up the first (and
     * presumably only) entry in the library attribute table
     *
     * @param dir the containing directory
     * @param namespace the namespace to create features with.
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFLibrary(SimpleFeature libraryFeature, File dir, URI namespace)
            throws IOException, SchemaException {
        // read libraries info
        String vpfTableName = new File(dir, LIBRARY_HEADER_TABLE).toString();
        VPFFile lhtFile = VPFFileFactory.getInstance().getFile(vpfTableName);
        lhtFile.reset();
        this.namespace = namespace != null ? namespace : DEFAULT_NAMESPACE;
        try {
            lhtFile.readFeature(); // check for errors
        } catch (IllegalAttributeException exc) {
            exc.printStackTrace();
            throw new IOException("Illegal values in library attribute table");
        }
        if (libraryFeature != null) {
            xmin = ((Number) libraryFeature.getAttribute(FIELD_XMIN)).doubleValue();
            ymin = ((Number) libraryFeature.getAttribute(FIELD_YMIN)).doubleValue();
            xmax = ((Number) libraryFeature.getAttribute(FIELD_XMAX)).doubleValue();
            ymax = ((Number) libraryFeature.getAttribute(FIELD_YMAX)).doubleValue();
        } else {
            xmin = -180;
            ymin = -90;
            xmax = 180;
            ymax = 90;
        }
        directory = dir;
        // What about looking up the LAT from the previous directory? Or, can you get what you need
        // from the LHT?
        String directoryName = directory.getPath();
        libraryName = directoryName.substring(directoryName.lastIndexOf(File.separator) + 1);
        setCoverages();
    }
    /**
     * Determines the coverages contained by this library
     *
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    private void setCoverages() throws IOException, SchemaException {
        VPFCoverage coverage;
        SimpleFeature feature;
        String directoryName;

        boolean debug = VPFLogger.isLoggable(Level.FINEST);

        if (debug) {
            VPFLogger.log("+++++++++++++ setCoverages: " + directory);
        }

        // I'd like to know why this if is here...
        if (!directory.getName().equals("RFERENCE")) {
            String vpfTableName = new File(directory, COVERAGE_ATTRIBUTE_TABLE).toString();
            VPFFile vpfFile = VPFFileFactory.getInstance().getFile(vpfTableName);
            if (debug) {
                VPFLogger.log("vpfTableName: " + vpfTableName);
                VPFLogger.log("vpfRootPathName: " + vpfFile.getPathName());
            }

            //            TableInputStream vpfTable = new TableInputStream(vpfTableName);
            Iterator iter = vpfFile.readAllRows().iterator();
            while (iter.hasNext()) {
                feature = (SimpleFeature) iter.next();
                directoryName = directory.getPath();
                coverage = new VPFCoverage(this, feature, directoryName, namespace);
                coverages.add(coverage);
                String coverageName = coverage.getName();
                // Find the Tileref coverage, if any
                if (coverageName.equalsIgnoreCase("TILEREF")) {
                    createTilingSchema(coverage);
                }

                if (debug) {
                    VPFLogger.log("---------- coverageName: " + coverageName);
                    VPFLogger.log(coverage.getPathName());
                    VPFLogger.log(coverage.getDescription());
                }

                List featureTypes = coverage.getFeatureTypes();

                for (int ift = 0; ift < featureTypes.size(); ift++) {
                    VPFFeatureType featureType = (VPFFeatureType) featureTypes.get(ift);
                    VPFFeatureClass featureClass = featureType.getFeatureClass();
                    String featureTypeName = featureType.getTypeName();
                    if (debug) {
                        VPFLogger.log(">>>>>featureType: " + featureTypeName);
                        VPFLogger.log("     directory:   " + featureType.getDirectoryName());
                        VPFLogger.log("     fc type  :   " + featureClass.getFCTypeName());
                    }

                    typeMap.put(featureTypeName, featureType);

                    List fileList = featureClass.getFileList();

                    if (debug) {
                        VPFLogger.log("   file count :   " + fileList.size());
                    }

                    for (int ifl = 0; ifl < fileList.size(); ifl++) {
                        VPFFile vpfClassFile = (VPFFile) fileList.get(ifl);
                        if (debug) {
                            if (vpfClassFile == null) {
                                VPFLogger.log("null");
                            } else {
                                VPFLogger.log(vpfClassFile.getPathName());
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * Returns the coverages contained by the library
     *
     * @return a <code>List</code> value which contains VPFCoverage objects
     */
    public List getCoverages() {
        return coverages;
    }

    /**
     * Getter for property xmax.
     *
     * @return Value of property xmax.
     */
    public double getXmax() {
        return xmax;
    }

    /**
     * Getter for property xmin.
     *
     * @return Value of property xmin.
     */
    public double getXmin() {
        return xmin;
    }

    /**
     * Getter for property ymax.
     *
     * @return Value of property ymax.
     */
    public double getYmax() {
        return ymax;
    }

    /**
     * Getter for property ymin.
     *
     * @return Value of property ymin.
     */
    public double getYmin() {
        return ymin;
    }
    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return String.format(
                "{"
                        + "library: \"%s\","
                        + "xmin: %f,"
                        + "xmax: %f,"
                        + "ymin: %f,"
                        + "ymax: %f"
                        + "}",
                libraryName, getXmin(), getXmax(), getYmin(), getYmax());
    }
    /** A map containing the tiles used by this library */
    private final Map tileMap = new HashMap();
    /**
     * Returns a map containing the tiles used by this library. The map has string keys and and
     * string values.
     *
     * @return a <code>Map</code> value
     */
    public Map getTileMap() {
        return tileMap;
    }
    /**
     * Generates the tile map for this coverage
     *
     * @param coverage a <code>VPFCoverage</code> which happens to be a TILEREF coverage
     */
    private void createTilingSchema(VPFCoverage coverage) throws IOException {
        //            File tilefile = new File(directory, "tilereft.tft");

        VPFFeatureType tileType = (VPFFeatureType) coverage.getFeatureTypes().get(0);

        // VPFFile tileFile = (VPFFile) tileType.getFeatureClass().getFileList().get(0);
        // Iterator rowsIter = tileFile.readAllRows().iterator();
        List<SimpleFeature> allFeatures = tileType.readAllRows();
        Iterator rowsIter = allFeatures.iterator();

        while (rowsIter.hasNext()) {
            SimpleFeature row = (SimpleFeature) rowsIter.next();
            Short rowId = Short.valueOf(Short.parseShort(row.getAttribute("id").toString()));
            String value = row.getAttribute(FIELD_TILE_NAME).toString();

            // Mangle tile directory from DOS style directory splits to a system
            // specific form
            String[] tmp = value.split("\\\\");
            value = tmp[0];
            for (int i = 1, ii = tmp.length; i < ii; i++) {
                value = value.concat(File.separator).concat(tmp[i]);
            }
            tileMap.put(rowId, value);
        }
    }
    //            HashMap hm = new HashMap();
    //
    //            TableInputStream testInput = new TableInputStream(
    //                                                 tilefile.getAbsolutePath());
    //            TableRow row = (TableRow) testInput.readRow();
    //            String tmp = null;
    //            StringBuffer buff = null;
    //
    //            while (row != null) {
    //                tmp = row.get().toString().trim();
    //
    //                if ((tmp != null) && (tmp.length() > 0)) {
    //                    tmp = tmp.toLowerCase();
    //                    buff = new StringBuffer();
    //                    buff.append(tmp.charAt(0));
    //
    //                    for (int i = 1; i < tmp.length(); i++) {
    //                        buff.append(File.separator);
    //                        buff.append(tmp.charAt(i));
    //                    }
    //
    //                    hm.put(row.get(FIELD_TILE_ID).toString().trim(),
    //                           buff.toString());
    //
    //
    //                    //VPFLogger.log( new File( coverage, tmp.charAt(0) + File.separator +
    // tmp.charAt(1) ).getAbsolutePath() );
    //                    row = (TableRow) testInput.readRow();
    //                }
    //            }
    //
    //            testInput.close();
    //            base.setTileMap(hm);
    //        }

    /* (non-Javadoc)
     * @see org.geotools.data.ContentDataStore#getNames()
     */
    public List<Name> getNames() {
        // Get the type names for each coverage
        ArrayList<Name> result = new ArrayList<Name>();
        int coveragesCount = coverages.size();
        int featureTypesCount = 0;
        // int index = 0;
        List[] coverageTypes = new List[coveragesCount];
        for (int inx = 0; inx < coveragesCount; inx++) {
            coverageTypes[inx] = ((VPFCoverage) coverages.get(inx)).getFeatureTypes();
            featureTypesCount += coverageTypes[inx].size();
        }
        // result = new String[featureTypesCount];
        for (int inx = 0; inx < coveragesCount; inx++) {
            // VPFCoverage coverage = (VPFCoverage) coverages.get(inx);
            for (int jnx = 0; jnx < coverageTypes[inx].size(); jnx++) {
                // result[index] = ((SimpleFeatureType) coverageTypes[inx].get(jnx)).getTypeName();
                SimpleFeatureType featureType = (SimpleFeatureType) coverageTypes[inx].get(jnx);
                // String featureTypeName = this.libraryName + "_" + featureType.getTypeName();
                String featureTypeName = featureType.getTypeName();
                NameImpl name = new NameImpl(featureTypeName);
                result.add(name);
                // index++;
            }
        }
        return result;
    }

    /*
    public String[] getTypeNames() {
        // Get the type names for each coverage
        String[] result = null;
        int coveragesCount = coverages.size();
        int featureTypesCount = 0;
        int index = 0;
        List[] coverageTypes = new List[coveragesCount];
        for (int inx = 0; inx < coveragesCount; inx++) {
            coverageTypes[inx] = ((VPFCoverage) coverages.get(inx)).getFeatureTypes();
            featureTypesCount += coverageTypes[inx].size();
        }
        result = new String[featureTypesCount];
        for (int inx = 0; inx < coveragesCount; inx++) {
            for (int jnx = 0; jnx < coverageTypes[inx].size(); jnx++) {
                result[index] = ((SimpleFeatureType) coverageTypes[inx].get(jnx)).getTypeName();
                NameImpl name = new NameImpl(((SimpleFeatureType) coverageTypes[inx].get(jnx)).getTypeName());
                result.add(name);
                index++;
            }
        }
        return result;
    }
    */

    public VPFFeatureType getFeatureType(ContentEntry entry) throws IOException {
        String typeName = entry.getTypeName();
        return (VPFFeatureType) this.getTypeSchema(typeName);
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
    public ContentFeatureSource getFeatureSource(Name typeName, Transaction tx) throws IOException {
        String localTypeName = typeName.getLocalPart();
        return super.getFeatureSource(new NameImpl(localTypeName), tx);
    }

    @Override
    public ContentFeatureSource getFeatureSource(String typeName) throws IOException {

        VPFFeatureSource featureSource = VPFFeatureSource.getFeatureSource(typeName);
        if (featureSource == null) {
            featureSource = VPFFeatureSource.getFeatureSource(typeName.toUpperCase());
        }

        if (featureSource == null) {
            VPFFeatureType featureType = typeMap.get(typeName);
            if (featureType == null) {
                featureType = typeMap.get(typeName.toUpperCase());
            }
            if (featureType != null) {
                String featureTypeName = featureType.getTypeName();
                Query query = new Query(Query.ALL);
                ContentEntry entry = this.entry(new NameImpl(featureTypeName));
                featureSource = new VPFCovFeatureSource(featureType, entry, query);
            }
        }

        if (featureSource == null) {
            if (VPFLogger.isLoggable(Level.FINEST)) {
                VPFLogger.log("VPFLibrary.getFeatureSource returned null");
                VPFLogger.log(typeName);
            }
            Query query = new Query(Query.ALL);
            ContentEntry entry = this.entry(new NameImpl(typeName));
            featureSource = new VPFLibFeatureSource(entry, query);
        }
        return featureSource;
    }

    public File getDirectory() {
        return this.directory;
    }

    public SimpleFeatureType getTypeSchema(String typeName) throws IOException {
        // Look through all of the coverages to find a matching feature type
        SimpleFeatureType result = null;
        Iterator coverageIter = coverages.iterator();
        Iterator featureTypesIter;
        SimpleFeatureType temp;
        boolean breakOut = false;
        while (coverageIter.hasNext() && !breakOut) {
            featureTypesIter = ((VPFCoverage) coverageIter.next()).getFeatureTypes().iterator();
            while (featureTypesIter.hasNext()) {
                temp = (SimpleFeatureType) featureTypesIter.next();
                if (temp.getTypeName().equals(typeName)) {
                    result = temp;
                    breakOut = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns the coordinate reference system appropriate for this library. If the coordinate
     * reference system cannot be determined null will be returned.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (crs == null) {
            try {
                // This is not really correct.  It does some basic sanity checks
                // (for GEO data type and WGE datum code), but basically assumes
                // WGS84 data ("EPSG:4326").
                String vpfTableName = new File(directory, GEOGRAPHIC_REFERENCE_TABLE).toString();
                VPFFile grtFile = VPFFileFactory.getInstance().getFile(vpfTableName);
                SimpleFeature grt = grtFile.getRowFromId("id", 1);
                String dataType = String.valueOf(grt.getAttribute("data_type"));

                if ("GEO".equalsIgnoreCase(dataType)) {
                    String geoDatumCode = String.valueOf(grt.getAttribute("geo_datum_code"));
                    if ("WGE".equalsIgnoreCase(geoDatumCode)) {
                        crs = DefaultGeographicCRS.WGS84;
                    }
                }
            } catch (Exception ex) {
                // Don't know what else can be done here, just dump it
                if (!loggedCRSException) {
                    ex.printStackTrace();
                    loggedCRSException = true;
                }
            }
        }
        return crs;
    }

    //    public VPFFeatureClass getFeatureClass(String typename) {
    //        VPFFeatureClass tmp = null;
    //
    //        if (coverages != null) {
    //            for (int i = 0; i < coverages.length; i++) {
    //                if (coverages[i] != null) {
    //                    tmp = coverages[i].getFeatureClass(typename);
    //
    //                    if (tmp != null) {
    //                        return tmp;
    //                    }
    //                }
    //            }
    //        }
    //
    //        return null;
    //    }
}
