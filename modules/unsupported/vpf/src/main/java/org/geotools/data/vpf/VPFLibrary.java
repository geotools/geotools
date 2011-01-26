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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.FileConstants;
import org.geotools.data.vpf.ifc.VPFLibraryIfc;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/*
 * A data store for a VPF library. A library is identified by
 * an LHT file.
 * 
 * VPF 101: 
 * Product: a profile of VPF data
 * Examples include DNC, VMAP, and VITD.<b>
 * Database: the world is separated into different databases. 
 * A database typically fits on one CD.<b>
 * Library: each database is subdivided into libraries. 
 * Libraries may be organized into coverage groups 
 * which have different scales.
 * The BROWSE library typically has information on the other libraries.<b>
 * Coverage: associated feature types are grouped in coverages.
 * Coverages share a common topological model.<b>
 * Feature class: Feature types are grouped into classes.
 * Feature types in a class share a common set of attributes
 * and are stored together on disk. <b>
 * Feature type: Feature types are denoted by a five-character FACC code.
 * Feature types and feature classes are stored 
 * at the same directory level, inside their containing coverages.<b>
 * File: VPF data is stored on disk in a complex hierarchy of flat files.
 * The VPFFile AbstractDataStore can be used to extract the contents of
 * an individual file, but this is typically useful only for testing.<b>
 *
 * Created on 19. april 2004, 14:53
 *
 * @author  <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @source $URL$
 */
public class VPFLibrary extends AbstractDataStore implements FileConstants, VPFLibraryIfc {

    /**
     * Part of bounding box.
     */
    private final double xmin;
    /**
     * Part of bounding box.
     */
    private final double ymin;
    /**
     * Part of bounding box.
     */
    private final double xmax;
    /**
     * Part of bounding box.
     */
    private final double ymax;
    /**
     * The directory containing the library
     */
    private final File directory;
    /**
     * The name of the library
     */
    private final String libraryName;
    /**
     * The coverages that are in the library
     */
    private final List coverages = new Vector();
    /**
     * The coordinate reference system used through this library
     */
    private CoordinateReferenceSystem crs;
    /**
     * Signals if an error has already been logged for a CRS related exception
     */
    private boolean loggedCRSException = false;

    static URI DEFAULT_NAMESPACE;

    static {
        try {
            DEFAULT_NAMESPACE = new URI("http://www.vpf.org/default");
        } catch (java.net.URISyntaxException urise) {
	    throw new RuntimeException("programmer error making default uri");
        }
    }
  
    /**
     * The namespace to create FeatureTypes with.  Set with a reasonable 
     * default of http://vpf.org/default.
     */
    private URI namespace = DEFAULT_NAMESPACE;
    
    /**
     * Complete constructor
     * 
     * @param libraryFeature a feature from the library attribute table
     * @param dir the containing directory
     * 
     * @throws IOException
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFLibrary(SimpleFeature libraryFeature, File dir) throws IOException, SchemaException {
        xmin = ((Number)libraryFeature.getAttribute(FIELD_XMIN)).doubleValue();
        ymin = ((Number)libraryFeature.getAttribute(FIELD_YMIN)).doubleValue();
        xmax = ((Number)libraryFeature.getAttribute(FIELD_XMAX)).doubleValue();
        ymax = ((Number)libraryFeature.getAttribute(FIELD_YMAX)).doubleValue();
        libraryName = libraryFeature.getAttribute(FIELD_LIB_NAME).toString();
        directory = new File(dir, libraryName);
        setCoverages();
    }

    /**
     * Constructor that adds a namespace to the File only constructor.  If
     * using another constructor then use setNamespace(URI) 
     * ((javadocTODO: add the correct link to previous method.))
     * 
     * @param dir the containing directory
     * @throws IOException
     * @throws SchemaException for problems making a featureType.
     */
    public VPFLibrary(File dir) throws IOException, SchemaException{
	this(dir, DEFAULT_NAMESPACE);
    }

    /**
     * Constructor which defaults the containing database to null and looks up the first
     * (and presumably only) entry in the library 
     * attribute table
     * @param dir the containing directory
     * @param namespace the namespace to create features with.
     * @throws IOException
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFLibrary(File dir, URI namespace) throws IOException, SchemaException {
        // read libraries info
        String vpfTableName = new File(dir, LIBRARY_HEADER_TABLE).toString();
        VPFFile lhtFile = VPFFileFactory.getInstance().getFile(vpfTableName);
        lhtFile.reset();
	this.namespace = namespace;
        try {
            lhtFile.readFeature(); // check for errors
        } catch (IllegalAttributeException exc) {
            exc.printStackTrace();
            throw new IOException("Illegal values in library attribute table");
        }
        xmin = -180;
        ymin = -90;
        xmax = 180;
        ymax = 90;
        directory = dir;
        // What about looking up the LAT from the previous directory? Or, can you get what you need from the LHT?
        String directoryName = directory.getPath();
        libraryName = directoryName.substring(directoryName.lastIndexOf(File.separator) + 1);
        setCoverages();
    }
    /**
     * Determines the coverages contained by this library 
     * @throws IOException
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    private void setCoverages() throws IOException, SchemaException {
        VPFCoverage coverage;
        SimpleFeature feature;
        String directoryName;

        // I'd like to know why this if is here...
        if (!directory.getName().equals("rference")) {
            String vpfTableName = new File(directory, COVERAGE_ATTRIBUTE_TABLE).toString();
            VPFFile vpfFile = VPFFileFactory.getInstance().getFile(vpfTableName);
            //            TableInputStream vpfTable = new TableInputStream(vpfTableName);
            Iterator iter = vpfFile.readAllRows().iterator();
            while (iter.hasNext()){
                feature = (SimpleFeature)iter.next();
                directoryName = directory.getPath();
                coverage = new VPFCoverage(this, feature, directoryName, namespace);
                coverages.add(coverage);
                // Find the Tileref coverage, if any
                if (coverage.getName().toLowerCase().equals("tileref")){
                    createTilingSchema(coverage);
                }
            }
        }
    }
    /**
     * Returns the coverages contained by the library
     * @return a <code>List</code> value which contains VPFCoverage objects
     */
    public List getCoverages() {
        return coverages;
    }

    /** Getter for property xmax.
     * @return Value of property xmax.
     *
     */
    public double getXmax() {
        return xmax;
    }

    /** Getter for property xmin.
     * @return Value of property xmin.
     *
     */
    public double getXmin() {
        return xmin;
    }

    /** Getter for property ymax.
     * @return Value of property ymax.
     *
     */
    public double getYmax() {
        return ymax;
    }

    /** Getter for property ymin.
     * @return Value of property ymin.
     *
     */
    public double getYmin() {
        return ymin;
    }
    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Dette er library : " + libraryName + " with extensions:\n" + 
        getXmin() + " " + getYmin() + " - " + getXmax() + " " + 
        getYmax() + "\n";
    }
    /**
     * A map containing the tiles used by this library
     */
    private final Map tileMap = new HashMap();
    /**
     * Returns a map containing the tiles used by this library.
     * The map has string keys and and string values. 
     * @return a <code>Map</code> value
     */
    public Map getTileMap() {
        return tileMap;
    }
    /**
     * Generates the tile map for this coverage
     * @param coverage a <code>VPFCoverage</code> which happens to be a TILEREF 
     * coverage
     */
    private void createTilingSchema(VPFCoverage coverage) throws IOException {
        //            File tilefile = new File(directory, "tilereft.tft");
        
        VPFFeatureType tileType = (VPFFeatureType)coverage.getFeatureTypes().get(0);
        VPFFile tileFile = (VPFFile)tileType.getFeatureClass().getFileList().get(0);
        Iterator rowsIter = tileFile.readAllRows().iterator();
        while (rowsIter.hasNext())
        {
            SimpleFeature row = (SimpleFeature)rowsIter.next();
            Short rowId = new Short(Short.parseShort(row.getAttribute("id").toString()));
            String value = row.getAttribute(FIELD_TILE_NAME).toString();

            // Mangle tile directory from DOS style directory splits to a system
            // specific form
            String[] tmp = value.split("\\\\");
            value = tmp[0];
            for(int i = 1, ii = tmp.length; i < ii; i++) {
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
    //                    //System.out.println( new File( coverage, tmp.charAt(0) + File.separator + tmp.charAt(1) ).getAbsolutePath() );
    //                    row = (TableRow) testInput.readRow();
    //                }
    //            }
    //        
    //            testInput.close();
    //            base.setTileMap(hm);
    //        }

    /* (non-Javadoc)
     * @see org.geotools.data.AbstractDataStore#getTypeNames()
     */
    public String[] getTypeNames() {
        // Get the type names for each coverage
        String[] result = null;
        int coveragesCount = coverages.size();
        int featureTypesCount = 0;
        int index = 0;
        List[] coverageTypes = new List[coveragesCount];
        for(int inx = 0; inx < coveragesCount; inx++){
            coverageTypes[inx] = ((VPFCoverage)coverages.get(inx)).getFeatureTypes();
            featureTypesCount += coverageTypes[inx].size();
        }
        result = new String[featureTypesCount];
        for(int inx = 0; inx < coveragesCount; inx++){
            for(int jnx = 0; jnx < coverageTypes[inx].size(); jnx++){
                result[index] = ((SimpleFeatureType)coverageTypes[inx].get(jnx)).getTypeName();
                index++;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.AbstractDataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(String typeName){
        // Look through all of the coverages to find a matching feature type
        SimpleFeatureType result = null;
        Iterator coverageIter = coverages.iterator();
        Iterator featureTypesIter;
        SimpleFeatureType temp;
        boolean breakOut = false;
        while(coverageIter.hasNext() && !breakOut){
            featureTypesIter = ((VPFCoverage)coverageIter.next()).getFeatureTypes().iterator();
            while(featureTypesIter.hasNext()){
                temp = (SimpleFeatureType)featureTypesIter.next();
                if(temp.getTypeName().equals(typeName)){
                    result = temp;
                    breakOut = true;
                    break;
                }
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.AbstractDataStore#getFeatureReader(java.lang.String)
     */
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName){
        // Find the appropriate feature type, make a reader for it, and reset its stream
         FeatureReader<SimpleFeatureType, SimpleFeature> result = null;
        VPFFeatureType featureType = (VPFFeatureType)getSchema(typeName);
        ((VPFFile)featureType.getFileList().get(0)).reset();
        result = new VPFFeatureReader(featureType);
        return result;
    }

    /**
     * Returns the coordinate reference system appropriate for this library.
     * If the coordinate reference system cannot be determined null will be returned.  
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem()
    {
        if(crs == null) {
            try{
                // This is not really correct.  It does some basic sanity checks
                // (for GEO data type and WGE datum code), but basically assumes
                // WGS84 data ("EPSG:4326").
                String vpfTableName = new File(directory, GEOGRAPHIC_REFERENCE_TABLE).toString();
                VPFFile grtFile = VPFFileFactory.getInstance().getFile(vpfTableName);
                SimpleFeature grt = grtFile.getRowFromId("id", 1);
                String dataType = String.valueOf(grt.getAttribute("data_type"));

                if("GEO".equalsIgnoreCase(dataType)){
                    String geoDatumCode = String.valueOf(grt.getAttribute("geo_datum_code"));
                    if ("WGE".equalsIgnoreCase(geoDatumCode)){
                        crs = DefaultGeographicCRS.WGS84;
                    }
                }
            } catch(Exception ex){
                // Don't know what else can be done here, just dump it
                if(!loggedCRSException) {
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
