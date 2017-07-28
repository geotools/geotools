/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wmts.model;

import java.util.ArrayList;
import org.geotools.data.ows.CRSEnvelope;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class TileMatrixSet {
    private static CoordinateReferenceSystem WEB_MERCATOR_CRS;

    static {
        CoordinateReferenceSystem tmpCrs = null;

        try {
            tmpCrs = CRS.decode("EPSG:3857");
        } catch (FactoryException e) {
            /*LOGGER.log(Level.SEVERE,
                    "Failed to create Web Mercator CRS EPSG:3857", e);*/
            throw new RuntimeException(e);
        }
        WEB_MERCATOR_CRS = tmpCrs;

    }
    final public static String OWS = "http://www.opengis.net/ows/1.1";

    private String identifier;

    private String crs;

    private CRSEnvelope bbox;

    private ArrayList<TileMatrix> matrices = new ArrayList<>();

    public void setIdentifier(String textContent) {
        this.identifier = textContent;

    }

    public void setCRS(String textContent) {
        this.crs = textContent;

    }

    public void addMatrix(TileMatrix processTM) {
        matrices.add(processTM);

    }

    /**
     * @return the crs
     */
    public String getCrs() {
        return crs;
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem()
            throws NoSuchAuthorityCodeException, FactoryException {
        
        //TODO: Kill who ever is still using this hack!
        if(crs.equalsIgnoreCase("epsg:900913")||crs.equalsIgnoreCase("urn:ogc:def:crs:EPSG::900913")) {
            return WEB_MERCATOR_CRS;
        }

        return CRS.decode(crs);
    }

    /**
     * @return the matrices
     */
    public ArrayList<TileMatrix> getMatrices() {
        return matrices;
    }

    /**
     * @param matrices the matrices to set
     */
    public void setMatrices(ArrayList<TileMatrix> matrices) {
        this.matrices = matrices;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    public CRSEnvelope getBbox() {
        return bbox;
    }

    public void setBbox(CRSEnvelope bbox) {
        this.bbox = bbox;
    }

//    /**
//     * @param e
//     */
//    @Deprecated
//    public static TileMatrixSet parseTileMatrixSet(Element e) {
//        TileMatrixSet ret = new TileMatrixSet();
//        NodeList crs = e.getElementsByTagNameNS(OWS, "SupportedCRS");
//        ret.setCRS(crs.item(0).getTextContent());
//        NodeList id = e.getElementsByTagNameNS(OWS, "Identifier");
//        ret.setIdentifier(id.item(0).getTextContent());
//        NodeList matrices = e.getElementsByTagName("TileMatrix");
//
//        for (int i = 0; i < matrices.getLength(); i++) {
//            TileMatrix tm = TileMatrix.parse((Element) matrices.item(i));
//            tm.setParent(ret);
//            ret.addMatrix(tm);
//        }
//
//        return ret;
//    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIdentifier()).append("\t").append(getCrs()).append("\n");
        for (TileMatrix m : matrices) {
            sb.append("\t").append(m.toString());
        }
        return sb.toString();
    }

    /**
     * @return
     */
    public int size() {
        return matrices.size();
    }
}
