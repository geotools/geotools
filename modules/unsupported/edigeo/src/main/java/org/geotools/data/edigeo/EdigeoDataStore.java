/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;


public class EdigeoDataStore extends AbstractDataStore {

    // File System constant
    private static String FS = System.getProperty("file.separator", "/");
    
    // Data type (reseau/network is not supported) 
    private static final String SPAGHETTI = "spaghetti";
    private static final String TOPOLOGIC = "topologique";
    
    // Geometry types of the objects
    private static final String KND_PCTOBJ = "Point";
    private static final String KND_LINOBJ = "LineString";
    private static final String KND_AREOBJ = "Polygon";
    private static final String KND_MULOBJ = "MultiPolygon";
    
    private static final HashMap<String, String> edigeoType = 
            new HashMap<String, String>();
    
    public static HashMap<String, HashMap<String, String>> scdObj = 
            new HashMap<String, HashMap<String, String>>();
    public static HashMap<String, HashMap<String, String>> ftAtt = 
            new HashMap<String, HashMap<String, String>>();
    
    private static File edigeoDir = null;
    
    private SimpleFeatureType featureType = null;
    private String pciObj;
    private HashMap<String, String> thfmap;
    

    /**
     * Static bloc to define static Edigeo Object map content
     */
    static {

        /**
         * Metadata for object BATIMENT
         * @param id string type, unique object
         * @param type string type, Nature de l'objet (CPX, PCT, LIN, ou ARE)
         * @param structure string type, 3 values : spaghetti, reseau, topologique
         */
        final HashMap<String, String> mdBat = new HashMap<String, String>();
        mdBat.put("id", "BATIMENT_id");
        mdBat.put("type", KND_AREOBJ);
        mdBat.put("structure", SPAGHETTI);

        final HashMap<String, String> mdBorne = new HashMap<String, String>();
        mdBorne.put("id", "BORNE_id");
        mdBorne.put("type", KND_PCTOBJ);
        mdBorne.put("structure", SPAGHETTI);

        final HashMap<String, String> mdCharge = new HashMap<String, String>();
        mdCharge.put("id", "CHARGE_id");
        mdCharge.put("type", KND_AREOBJ);
        mdCharge.put("structure", SPAGHETTI);

        final HashMap<String, String> mdCommune = new HashMap<String, String>();
        mdCommune.put("id", "COMMUNE_id");
        mdCommune.put("type", KND_AREOBJ);
        mdCommune.put("structure", SPAGHETTI);

        final HashMap<String, String> mdLieu = new HashMap<String, String>();
        mdLieu.put("id", "LIEUDIT_id");
        mdLieu.put("type", KND_AREOBJ);
        mdLieu.put("structure", SPAGHETTI);

        final HashMap<String, String> mdNumvoie = new HashMap<String, String>();
        mdNumvoie.put("id", "NUMVOIE_id");
        mdNumvoie.put("type", KND_PCTOBJ);
        mdNumvoie.put("structure", SPAGHETTI);

        final HashMap<String, String> mdParcelle = new HashMap<String, String>();
        mdParcelle.put("id", "PARCELLE_id");
        mdParcelle.put("type", KND_AREOBJ);
        mdParcelle.put("structure", TOPOLOGIC);

        final HashMap<String, String> mdPointcst = new HashMap<String, String>();
        mdPointcst.put("id", "POINTCST_id");
        mdPointcst.put("type", KND_PCTOBJ);
        mdPointcst.put("structure", SPAGHETTI);

        final HashMap<String, String> mdPtcanv = new HashMap<String, String>();
        mdPtcanv.put("id", "PTCANV_id");
        mdPtcanv.put("type", KND_PCTOBJ);
        mdPtcanv.put("structure", SPAGHETTI);

        final HashMap<String, String> mdSection = new HashMap<String, String>();
        mdSection.put("id", "SECTION_id");
        mdSection.put("type", KND_MULOBJ);
        mdSection.put("structure", TOPOLOGIC);

        final HashMap<String, String> mdSubdfisc = new HashMap<String, String>();
        mdSubdfisc.put("id", "SUBDFISC_id");
        mdSubdfisc.put("type", KND_AREOBJ);
        mdSubdfisc.put("structure", SPAGHETTI);

        final HashMap<String, String> mdSubdsect = new HashMap<String, String>();
        mdSubdsect.put("id", "SUBDSECT_id");
        mdSubdsect.put("type", KND_MULOBJ);
        mdSubdsect.put("structure", TOPOLOGIC);

        final HashMap<String, String> mdSymblim = new HashMap<String, String>();
        mdSymblim.put("id", "SYMBLIM_id");
        mdSymblim.put("type", KND_PCTOBJ);
        mdSymblim.put("structure", SPAGHETTI);

        final HashMap<String, String> mdTline = new HashMap<String, String>();
        mdTline.put("id", "TLINE_id");
        mdTline.put("type", KND_LINOBJ);
        mdTline.put("structure", SPAGHETTI);

        final HashMap<String, String> mdTpoint = new HashMap<String, String>();
        mdTpoint.put("id", "TPOINT_id");
        mdTpoint.put("type", KND_PCTOBJ);
        mdTpoint.put("structure", SPAGHETTI);

        final HashMap<String, String> mdTronfluv = new HashMap<String, String>();
        mdTronfluv.put("id", "TRONFLUV_id");
        mdTronfluv.put("type", KND_AREOBJ);
        mdTronfluv.put("structure", SPAGHETTI);

        final HashMap<String, String> mdTronroute = new HashMap<String, String>();
        mdTronroute.put("id", "TRONROUTE_id");
        mdTronroute.put("type", KND_AREOBJ);
        mdTronroute.put("structure", SPAGHETTI);

        final HashMap<String, String> mdTsurf = new HashMap<String, String>();
        mdTsurf.put("id", "TSURF_id");
        mdTsurf.put("type", KND_AREOBJ);
        mdTsurf.put("structure", SPAGHETTI);

        final HashMap<String, String> mdVoiep = new HashMap<String, String>();
        mdVoiep.put("id", "VOIEP_id");
        mdVoiep.put("type", KND_PCTOBJ);
        mdVoiep.put("structure", SPAGHETTI);

        final HashMap<String, String> mdZonecom = new HashMap<String, String>();
        mdZonecom.put("id", "ZONCOMMUNI_id");
        mdZonecom.put("type", KND_LINOBJ);
        mdZonecom.put("structure", SPAGHETTI);

        // Do I need to define an object type to get writing label point?? 
        scdObj.put("BATIMENT_id", mdBat);
        scdObj.put("E_2_1_0", mdBat);

        scdObj.put("BORNE_id", mdBorne);
        scdObj.put("I_2_4_0", mdBorne);

        scdObj.put("CHARGE_id", mdCharge);
        scdObj.put("H_11_6_0", mdCharge);

        scdObj.put("COMMUNE_id", mdCommune);
        scdObj.put("H_1_6_0", mdCommune);

        scdObj.put("LIEUDIT_id", mdLieu);
        scdObj.put("H_1_7_0", mdLieu);

        scdObj.put("NUMVOIE_id", mdNumvoie);
        scdObj.put("H_11_8_0", mdNumvoie);

        scdObj.put("PARCELLE_id", mdParcelle);
        scdObj.put("H_11_4_0", mdParcelle);

        scdObj.put("POINTCST_id", mdPointcst);
        scdObj.put("I_3_0_0", mdPointcst);

        scdObj.put("PTCANV_id", mdPtcanv);
        scdObj.put("I_1_0_0", mdPtcanv);

        scdObj.put("SECTION_id", mdSection);
        scdObj.put("H_11_1_0", mdSection);

        scdObj.put("SUBDFISC_id", mdSubdfisc);
        scdObj.put("H_11_5_0", mdSubdfisc);

        scdObj.put("SUBDSECT_id", mdSubdsect);
        scdObj.put("H_11_2_0", mdSubdsect);

        scdObj.put("SYMBLIM_id", mdSymblim);
        scdObj.put("Z_1_0_1", mdSymblim);

        scdObj.put("TLINE_id", mdTline);
        scdObj.put("Z_1_0_2", mdTline);

        scdObj.put("TPOINT_id", mdTpoint);
        scdObj.put("Z_1_0_1", mdTpoint);

        scdObj.put("TRONFLUV_id", mdTronfluv);
        scdObj.put("D_1_0_8", mdTronfluv);

        scdObj.put("TRONROUTE_id", mdTronroute);
        scdObj.put("A_1_0_0", mdTronroute);

        scdObj.put("TSURF_id", mdTsurf);
        scdObj.put("Z_1_0_3", mdTsurf);

        scdObj.put("VOIEP_id", mdVoiep);
        scdObj.put("H_11_7_0", mdVoiep);

        scdObj.put("ZONCOMMUNI_id", mdZonecom);
        scdObj.put("A_1_0_5", mdZonecom);


        /*
         * Defines Edigeo Attribut Types
         */
        edigeoType.put("A", "String");
        edigeoType.put("D", "Date");
        edigeoType.put("E", "Double");
        edigeoType.put("I", "Integer");
        edigeoType.put("N", "Integer");
        edigeoType.put("R", "Double");
        edigeoType.put("T", "String");
    }

    /**
     * <p>
     * Builds a new EdigeoDataStore given a Edigeo file (THF) path.
     * </p>
     * 
     * @param path The full path of a single Edigeo file (THF). 
     * @param obj Edigeo object id (ie. COMMUNE_id)
     *
     * @throws IOException Path does not exists, or error accessing files
     *
     */
    public EdigeoDataStore(String path, String obj) throws IOException {
        super(false); // Does not allow writing

        pciObj = checkPciObj(obj);
        
        
        EdigeoTHF thf = new EdigeoTHF(path);
        edigeoDir = thf.thfFile;
        LOGGER.info("Reading Edigeo file : "+edigeoDir.getPath());
        thfmap = thf.readTHFile();

        EdigeoSCD scd = new EdigeoSCD(edigeoDir.getParentFile().getPath() + FS + thfmap.get("scdfname"));
        HashMap<String, String> scdAtt = scd.readSCDFile(pciObj);

        if (!scdAtt.isEmpty()) {
            EdigeoDIC dic = new EdigeoDIC(edigeoDir.getParentFile().getPath() + FS + thfmap.get("dicfname"));
            ftAtt = dic.readDICFile(scdAtt);
        }
    }

    /**
     * <p>
     * Checks supported Edigeo according to PCI object
     * </p>
     *  
     * @param obj String 
     * 
     * @return the Edigeo object (String) 
     */
    protected static String checkPciObj(String aObj) {
        if (scdObj.containsKey(aObj)) {
            aObj = scdObj.get(aObj).get("id");
        } else {
            LOGGER.severe("Unsupported Edigeo object : " + aObj);
            throw new IllegalArgumentException("Unsupported Edigeo object : " + aObj);
        }

        return aObj;
    }

    /**
     * <p>
     * Gets a FeatureReader from an Edigeo object
     * </p>
     * 
     * @param typeName name of the FeatureType
     * 
     * @return The FeatureReader
     * 
     * @throws IOException
     */
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName) throws IOException {
        String fileExt;
        if (pciObj.equals("PARCELLE_id")) {
            fileExt = "T1";
        } else if (pciObj.equals("SUBDSECT_id")) {
            fileExt = "T2";
        } else if (pciObj.equals("SECTION_id")) {
            fileExt = "T3";
        } else {
            fileExt = "S1";
        }
        try {
        	return new EdigeoFeatureReader(edigeoDir, thfmap.get("vecfname_"+fileExt), pciObj, getSchema(getTypeNames()[0]));
        } catch (IllegalAttributeException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * <p>
     *  Creates a Schema (FeatureType) given a typeName.
     * </p>
     * 
     * @param typeName
     * @return SimpleFeatureType
     * @throws java.io.IOException
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        if (featureType == null) {
            String typeSpec = property();
            try {
                String namespace = edigeoDir.getParentFile().getName();
                featureType = DataUtilities.createType(namespace + "." + typeName, typeSpec);
            } catch (SchemaException e) {
                e.printStackTrace();
                throw new DataSourceException(typeName + " schema not available", e);
            }
        }
        return featureType;
    }

    /**
     * <p>
     * Returns the list of type names (EDIGEO files).<BR/>EdigeoDataStore
     * will always return a single name.
     * </p>
     *
     * @return The list of type names
     *
     * @throws IOException Couldn't scan path for files
     */
    public String[] getTypeNames() throws IOException {
        return new String[]{getCurrentTypeName(),};
    }
    
    /**
     * Create the type name of the single FeatureType this DataStore
     * represents.<BR/> For example, if the Edigeo file path is
     * file:///home/foo/athfFile.thf, the type name will be athfFile.
     *
     * @return The name of the THF file without the extension.
     */
    protected String createFeatureTypeName() {
        String name = edigeoDir.getName();
        int dot = name.lastIndexOf(".");

        if (dot > 0) {
            name = name.substring(0, dot);
        }
        return name;
    }

    /**
     * 
     * @return Current Type Name
     */
    protected String getCurrentTypeName() {
        return (featureType == null) ? createFeatureTypeName() : featureType.getTypeName();
    }

    /**
     * 
     * @return
     */
    private String property() {
        String typeSpec = "";
        if (ftAtt != null) {
            Iterator<String> it = ftAtt.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String type = edigeoType.get(ftAtt.get(key).get("type"));
                typeSpec += key + ":" + type + ",";
            }
        }
        typeSpec += "the_geom:" + scdObj.get(pciObj).get("type");
        return typeSpec;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IllegalAttributeException {
        // TODO Auto-generated method stub
        try {
            EdigeoDataStore edDS = new EdigeoDataStore("/home/mcoudert/app/geotools/geotools-2.5-M3/modules/unsupported/edigeo/src/test/resources/org/geotools/data/edigeo/test-data/E000AB01.THF", "COMMUNE_id");
            // pb : EDI0000B.THF  EDI0000E.THF  EDI0000F.THF 
            //  /home/mcoudert/public_html/pvs/sample_data/pvs/edigeo2/job_test/com-195/EDIGEO01.THF
            // /home/mcoudert/public_html/pvs/sample_data/pvs/edigeo2/com-029/feuille-029000AA01/E000AA01.THF
            String typeName = edDS.getTypeNames()[0];
            //FeatureReader readerFR = edDS.getFeatureReader(typeName);
            //reader.
            FeatureSource<SimpleFeatureType, SimpleFeature> source = edDS.getFeatureSource(typeName);
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures();
            FeatureIterator<SimpleFeature> reader = features.features();
            while(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                System.out.println("feature  :" + feature.getDefaultGeometryProperty().getValue());
            }
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }
}
