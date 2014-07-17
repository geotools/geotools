/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.hdf4.aps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.geotools.imageio.netcdf.NetCDFUtilities.KeyValuePair;
import org.w3c.dom.Node;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class HDF4APSStreamMetadata extends IIOMetadata {
    public static final String nativeMetadataFormatName = "it_geosolutions_imageio_plugins_hdf4_aps_APSStreamMetadata_1.0";

    public static final String PFA_NODE = "ProductFilesAttributes";
    
    public static final String PFA_IPA_NODE = "InputParameterAttributes";
	
    public static final String DATUM = "Datum";

    public static final String PROJECTION = "Projection";

    public static final String SEMI_MAJOR_AXIS = "Param0";

    public static final String SEMI_MINOR_AXIS = "Param1";

    public static final String LONGITUDE_OF_CENTRAL_MERIDIAN = "LongitudeOfCentralMeridian";

    public static final String LATITUDE_OF_TRUE_SCALE = "LatitudeOfTrueScale";

    public static final String FALSE_EASTINGS = "FalseEastings";

    public static final String FALSE_NORTHINGS = "FalseNorthings";

    public static final String STD_NODE = "StandardAPSAttributes";

    public static final String PFA_NA_NODE = "NavigationAttributes";

    public static final String STD_FA_NODE = "FileAttributes";

    public static final String STD_TA_NODE = "TimeAttributes";

	public static final String STD_SA_NODE = "SensorAttributes";

	public static final String REFERENCING_NODE = "Referencing";

	public static final String PFA_IGCA_NODE = "InputGeographicalCoverageAttributes";

	public static final String GENERICS_NODE = "GenericAttributes";

	public static final String PROJECTION_NODE = "Projection";

	public static final String ZONE = "Zone";
	
	public static final String MAP_UPPER_RIGHT = "mapUpperRight";
	
	public static final String MAP_LOWER_LEFT = "mapLowerLeft";

    // TODO: Provides to build a proper structure to get CP_Pixels, CP_Lines,
    // CP_Latitudes, CP_Longitudes information

    /**
     * Build a node having name <code>nodeName</code> and attributes retrieved
     * from the provided <code>attribMap</code>.
     * 
     * @param attribMap
     *                A <code>Map</code> containing couples (attrib name,
     *                attrib value)
     * @param nodeName
     *                the name which need to be set for the node.
     * @return the built node.
     */
    private IIOMetadataNode buildAttributesNodeFromMap(final Map<String,String> attribMap,
            final String nodeName) {
        final IIOMetadataNode node = new IIOMetadataNode(nodeName);
        synchronized (attribMap) {
            if (attribMap != null) {
                final Set<String> set = attribMap.keySet();
                final Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    final String attribValue = attribMap.get(key);
                    key = key.replace('\\', '_');
                    node.setAttribute(key, attribValue);
                }
            }
        }
        return node;
    }

    private Map<String,String> stdFileAttribMap = new LinkedHashMap<String,String>(11);
    private Map<String,String> stdTimeAttribMap = new LinkedHashMap<String,String>(9);
    private Map<String,String> stdSensorAttribMap = new LinkedHashMap<String,String>(13);
    private Map<String,String> fileInputParamAttribMap = new LinkedHashMap<String,String>(6);
    private Map<String,String> fileNavAttribMap = new LinkedHashMap<String,String>(7);
    private Map<String,String> fileInGeoCovAttribMap = new LinkedHashMap<String,String>(8);
    private Map<String,String> genericAttribMap = new LinkedHashMap<String,String>(15);
    private Map<String,String> projectionMap = null;
    
    private Map<String,ArrayList<Map<String,String>>> productsMap = null;
    private String[] prodList = null;
    private int prodListNum;
    private String projectionDatasetName;

    public HDF4APSStreamMetadata() {
        super(false, nativeMetadataFormatName, null, null, null);
    }

    public HDF4APSStreamMetadata(HDF4APSImageReader reader) {
        this();
        buildMetadata(reader);
    }
    public Node getAsTree(String formatName) {
        if (formatName.equals(nativeMetadataFormatName)) {
            return getNativeTree();
        } else {
            throw new IllegalArgumentException("Not a recognized format!");
        }
    }

    private synchronized Node getNativeTree() {
        final IIOMetadataNode root = new IIOMetadataNode(nativeMetadataFormatName);

        // /////////////////////////////////////////////////////////////
        // 
        // Attributes Node
        //
        // /////////////////////////////////////////////////////////////
        final IIOMetadataNode attribNode = new IIOMetadataNode("Attributes");

        // /////////////////////////////////////////////////////////////
        // Standard APS Attributes
        // /////////////////////////////////////////////////////////////
        final IIOMetadataNode stdNode = new IIOMetadataNode(STD_NODE);

        // File Attributes
        final IIOMetadataNode stdFaNode = buildAttributesNodeFromMap(stdFileAttribMap, STD_FA_NODE);
        stdNode.appendChild(stdFaNode);

        // Time Attributes
        final IIOMetadataNode stdTaNode = buildAttributesNodeFromMap(stdTimeAttribMap, STD_TA_NODE);
        stdNode.appendChild(stdTaNode);

        // Sensor Attributes
        final IIOMetadataNode stdSaNode = buildAttributesNodeFromMap(stdSensorAttribMap, STD_SA_NODE);
        stdNode.appendChild(stdSaNode);

        attribNode.appendChild(stdNode);

        // ////////////////////////////////////////////////////////////////
        // File Products Attributes
        // ////////////////////////////////////////////////////////////////
        final IIOMetadataNode fpaNode = new IIOMetadataNode(PFA_NODE);

        // Input Parameter Attributes
        final IIOMetadataNode fpIpaNode = buildAttributesNodeFromMap(fileInputParamAttribMap, PFA_IPA_NODE);
        fpaNode.appendChild(fpIpaNode);

        // Navigation Attributes
        final IIOMetadataNode fpNaNode = buildAttributesNodeFromMap(fileNavAttribMap, PFA_NA_NODE);
        fpaNode.appendChild(fpNaNode);

        // Input Geographical Coverage Attributes
        final IIOMetadataNode fpIgcaNode = buildAttributesNodeFromMap(fileInGeoCovAttribMap, PFA_IGCA_NODE);
        fpaNode.appendChild(fpIgcaNode);

        attribNode.appendChild(fpaNode);

        // ////////////////////////////////////////////////////////////////
        // Generic Attributes
        // ////////////////////////////////////////////////////////////////
        final IIOMetadataNode genericNode = buildAttributesNodeFromMap(genericAttribMap, GENERICS_NODE);
        attribNode.appendChild(genericNode);
        root.appendChild(attribNode);

//        IIOMetadataNode productsNode = new IIOMetadataNode("Products");
//        productsNode.setAttribute("numberOfProducts", Integer
//                .toString(prodListNum));
//
//        final Set<String> set = productsMap.keySet();
//        final Iterator<String> productsIt = set.iterator();
//
//        while (productsIt.hasNext()) {
//            IIOMetadataNode productNode = new IIOMetadataNode("Product");
//            final String name = (String) productsIt.next();
//            productNode.setAttribute("name", name);
//            final ArrayList<Map<String,String>> attribs = productsMap.get(name);
//
//            final Map<String,String> pdsaAttribMap = (LinkedHashMap<String,String>) attribs.get(0);
//            IIOMetadataNode pdsaAttribNode = buildAttributesNodeFromMap(
//                    pdsaAttribMap, "ProductDatasetAttributes");
//            productNode.appendChild(pdsaAttribNode);
//
//            final Map<String,String> genericAttribMap = (LinkedHashMap<String,String>) attribs.get(1);
//            IIOMetadataNode genericPdsaNode = buildAttributesNodeFromMap(
//                    genericAttribMap, "ProductGenericAttributes");
//            productNode.appendChild(genericPdsaNode);
//
//            productsNode.appendChild(productNode);
//        }
//
//        root.appendChild(productsNode);
        IIOMetadataNode referencingNode = new IIOMetadataNode(REFERENCING_NODE);
        IIOMetadataNode projectionNode = buildAttributesNodeFromMap(projectionMap, PROJECTION_NODE);

        referencingNode.appendChild(projectionNode);
        root.appendChild(referencingNode);
        return root;
    }
    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void mergeTree(String formatName, Node root)
            throws IIOInvalidTreeException {
    	throw new UnsupportedOperationException("mergeTree operation is not allowed");
    }
    @Override
    public void reset() {
    	throw new UnsupportedOperationException("reset operation is not allowed");
    }

    synchronized void buildMetadata(HDF4APSImageReader reader) {
        try {
        	final int numAttributes = reader.getNumGlobalAttributes();

            // number of supported attributes
            final int nStdFileAttribMap = HDF4APSProperties.STD_FA_ATTRIB.length;
            final int nStdTimeAttribMap = HDF4APSProperties.STD_TA_ATTRIB.length;
            final int nStdSensorAttribMap = HDF4APSProperties.STD_SA_ATTRIB.length;
            final int nFileInputParamAttribMap = HDF4APSProperties.PFA_IPA_ATTRIB.length;
            final int nFileNavAttribMap = HDF4APSProperties.PFA_NA_ATTRIB.length;
            final int nFileInGeoCovAttribMap = HDF4APSProperties.PFA_IGCA_ATTRIB.length;

            for (int i = 0; i < numAttributes; i++) {
                // get Attributes
                final KeyValuePair keyValuePair = reader.getGlobalAttribute(i);
                final String attribName = keyValuePair.getKey();
                final String attribValue = keyValuePair.getValue();
                // get Attribute Name
                // checks if the attribute name matches one of the supported
                // attributes

                boolean found = false;
                for (int k = 0; k < nStdFileAttribMap && !found; k++) {
                    // if matched
                    if (attribName.equals(HDF4APSProperties.STD_FA_ATTRIB[k])) {
                        stdFileAttribMap.put(attribName, attribValue);
                        found = true;
                    }
                }

                for (int k = 0; k < nStdTimeAttribMap && !found; k++) {
                    // if matched
                    if (attribName.equals(HDF4APSProperties.STD_TA_ATTRIB[k])) {
                        stdTimeAttribMap.put(attribName, attribValue);
                        found = true;
                    }
                }

                for (int k = 0; k < nStdSensorAttribMap && !found; k++) {
                    // if matched
                    if (attribName.equals(HDF4APSProperties.STD_SA_ATTRIB[k])) {
                        stdSensorAttribMap.put(attribName, attribValue);
                        found = true;
                    }
                }

                for (int k = 0; k < nFileInputParamAttribMap && !found; k++) {
                    // if matched
                    if (attribName.equals(HDF4APSProperties.PFA_IPA_ATTRIB[k])) {
                        fileInputParamAttribMap.put(attribName,attribValue);
                        if (attribName.equals(HDF4APSProperties.PFA_IPA_PRODLIST)) {
                            String products[] = attribValue.split(",");
                            prodList = HDF4APSProperties.refineProductList(products);
                        }
                        found = true;
                    }
                }

                for (int k = 0; k < nFileNavAttribMap && !found; k++) {
                    // if matched
                    if (attribName.equals(HDF4APSProperties.PFA_NA_ATTRIB[k])) {
                        fileNavAttribMap.put(attribName, attribValue);
                        if (attribName .equals(HDF4APSProperties.PFA_NA_MAPPROJECTION))
                            projectionDatasetName = attribValue;
                        found = true;
                    }
                }

                for (int k = 0; k < nFileInGeoCovAttribMap && !found; k++) {
                    // if matched
                    if (attribName.equals(HDF4APSProperties.PFA_IGCA_ATTRIB[k])) {
                        fileInGeoCovAttribMap.put(attribName,attribValue);
                        found = true;
                    }
                }

                if (!found)
                    genericAttribMap.put(attribName, attribValue);
            }

            // //////////////////////////////////
            // Retrieving products metadata
            // //////////////////////////////////

//            prodListNum = prodList.length;
//            if (productsMap == null)
//                productsMap = new LinkedHashMap<String, ArrayList<Map<String,String>>>(prodListNum);

//            final int numSDS = root.size();
//            for (int i = 0; i < numSDS; i++) {
//                H4SDS sds = (H4SDS) root.get(i);
//                final String name = sds.getName();
//                boolean productFound = false;
//                for (int j = 0; j < prodListNum && !productFound; j++) {
//                    if (name.equals(prodList[j])) {
//                        productFound = true;
//
//                        final int sdsAttributes = sds.getNumAttributes();
//                        if (sdsAttributes != 0) {
//
//                            final int nPdsAttrib = HDF4APSProperties.PDSA_ATTRIB.length;
//                            final LinkedHashMap<String,String> pdsAttribMap = new LinkedHashMap<String,String>(
//                                    nPdsAttrib);
//                            final LinkedHashMap<String,String> pdsGenericAttribMap = new LinkedHashMap<String,String>(
//                                    10);
//
//                            for (int indexAttr = 0; indexAttr < sdsAttributes; indexAttr++) {
//
//                                // get Attributes
//                                final H4Attribute att = (H4Attribute) sds
//                                        .getAttribute(indexAttr);
//
//                                // get Attribute Name
//                                final String attribName = att.getName();
//
//                                // get Attribute Value
//                                final String attribValue = H4Utilities
//                                        .buildAttributeString(att);
//                                boolean attributeFound = false;
//                                for (int k = 0; k < nPdsAttrib
//                                        && !attributeFound; k++) {
//                                    // if matched
//                                    if (attribName
//                                            .equals(HDF4APSProperties.PDSA_ATTRIB[k])) {
//                                        pdsAttribMap.put(attribName,
//                                                attribValue);
//
//                                        attributeFound = true;
//                                    }
//                                }
//
//                                if (!attributeFound) {
//                                    pdsGenericAttribMap.put(attribName, attribValue);
//                                }
//                            }
//
//                            final ArrayList<Map<String,String>> productAttribs = new ArrayList<Map<String,String>>(2);
//                            // putting pdsaAttribMap and genericAttribMap
//                            // in the arrayList
//                            productAttribs.add(pdsAttribMap);
//                            productAttribs.add(pdsGenericAttribMap);
//                            productsMap.put(name, productAttribs);
//                        }
//                    }
//                }
//
//            
//                if (!productFound && name.equals(projectionDatasetName)) {
//                    Object data = sds.read();
//                    final int datatype = sds.getDatatype();
//                    if (projectionMap == null)
//                        projectionMap = buildProjectionAttributesMap(data,
//                                datatype);
//                }
            Map<String,String> originalMap = reader.projectionMap;
            projectionMap = new LinkedHashMap<String, String>(originalMap.size());
            for (String key : originalMap.keySet()){
            	String value = originalMap.get(key);
            	projectionMap.put(key, value);
            }
//            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Errors while getting access" +
            		" to HDF during StreamMetadata setting",e);
        }
    }
}
