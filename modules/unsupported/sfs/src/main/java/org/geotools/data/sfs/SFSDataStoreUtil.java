/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 *  A Generic Utility class for misc. methods
 *  @author
 */
class SFSDataStoreUtil {

    static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Does the flipping of geometry object incase the axis is YX
     * We used CoordinateArraySequence to get coordinate Array
     * As per its documentation "In this implementation, Coordinates returned
     * by #toArray and #getCoordinate are live -- modifications to them are
     * actually changing the CoordinateSequence's underlying data."
     * @param fnG
     */
    public static void flipFeatureYX(Geometry fnG) {
        fnG.apply(new CoordinateSequenceFilter() {
            
            public boolean isGeometryChanged() {
                return true;
            }
            
            public boolean isDone() {
                return false;
            }
            
            public void filter(CoordinateSequence seq, int i) {
                double x = seq.getX(i);
                double y = seq.getY(i);
                seq.setOrdinate(i, 0, y);
                seq.setOrdinate(i, 1, x);
            }
        });
    }

    /**
     * Bounding Box has the form [ymin,xmin, ymax, xmax] and wre want to transform
     * it in [xmin,ymin,xmax,ymax]
     * 
     * @param fnAL
     */
    public static void flipYXInsideTheBoundingBox(ArrayList fnAL) {

        double ymin = Double.parseDouble(fnAL.get(0).toString());
        double xmin = Double.parseDouble(fnAL.get(1).toString());
        double ymax = Double.parseDouble(fnAL.get(2).toString());
        double xmax = Double.parseDouble(fnAL.get(3).toString());

        /*Clear the contents of the arraylist*/
        fnAL.clear();

        fnAL.add(0, xmin);
        fnAL.add(1, ymin);
        fnAL.add(2, xmax);
        fnAL.add(3, ymax);
    }

    /**
     * This method parses the query and creates a string which can be appended to
     * the baseURL and then execute it to get response
     * Pulled from GeoRest DataStore
     * @param fnQuery
     * @return String
     */
    public static String encodeQuery(Query fnQuery, SimpleFeatureType targetType) throws UnsupportedEncodingException {

        boolean firstChar = false;

        // Transform query into URL parameters:
        StringBuilder urlBuilder = new StringBuilder();

        // Feature number limitation:
        if (!fnQuery.isMaxFeaturesUnlimited()) {
            urlBuilder.append(getGlueChar(firstChar));
            firstChar = false;
            urlBuilder.append("limit=");
            urlBuilder.append(fnQuery.getMaxFeatures());
        }

        boolean no_geom = true;
        // Should we retrieve all properties? and also check for no_geom
        if (!fnQuery.retrieveAllProperties() && fnQuery.getPropertyNames().length > 0) {
            urlBuilder.append(getGlueChar(firstChar));
            firstChar = false;
            urlBuilder.append("attrs=");
            for (int i = 0; i < fnQuery.getPropertyNames().length; i++) {
                if (targetType.getDescriptor(fnQuery.getPropertyNames()[i]) instanceof GeometryDescriptor) {
                    no_geom = false;
                }

                urlBuilder.append(URLEncoder.encode(fnQuery.getPropertyNames()[i], DEFAULT_ENCODING));

                if (i < fnQuery.getPropertyNames().length - 1) {
                    urlBuilder.append(",");
                }
            }
            /*This is to add no_geom*/
            if(no_geom) {
                urlBuilder.append(getGlueChar(firstChar));
                urlBuilder.append("no_geom=");
                urlBuilder.append(no_geom);
            }
        }

        // Perhaps use an offset to start counting from:
        if (fnQuery.getStartIndex() != null) {
            urlBuilder.append(getGlueChar(firstChar));
            firstChar = false;

            urlBuilder.append("offset=");
            urlBuilder.append(fnQuery.getStartIndex());
        }

        // Is there a certain order required?
        if (fnQuery.getSortBy() != null && fnQuery.getSortBy().length > 0) {

            urlBuilder.append(getGlueChar(firstChar));
            firstChar = false;
            
            urlBuilder.append("order_by=");
            urlBuilder.append(URLEncoder.encode(fnQuery.getSortBy()[0].getPropertyName().getPropertyName(), DEFAULT_ENCODING));

            urlBuilder.append(getGlueChar(firstChar));
            urlBuilder.append("dir=");
            SortOrder order = fnQuery.getSortBy()[0].getSortOrder();
            urlBuilder.append(URLEncoder.encode(order.name(), DEFAULT_ENCODING));
        }

        // Is there a filter present?
        if (fnQuery.getFilter() != null) {
            SFSFilterVisitor visitor = new SFSFilterVisitor(false);
            fnQuery.getFilter().accept(visitor, null);
            visitor.finish(urlBuilder, !firstChar);
            firstChar = false;
        }
        
        // handle the query hints
        if (fnQuery.getHints() != null
                && fnQuery.getHints().get(Hints.VIRTUAL_TABLE_PARAMETERS) != null) {
            Map<String, String> params = (Map<String, String>) fnQuery.getHints().get(
                    Hints.VIRTUAL_TABLE_PARAMETERS);

            urlBuilder.append(getGlueChar(firstChar));
            firstChar = false;

            for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();

                urlBuilder.append(URLEncoder.encode(pair.getKey().toString(), DEFAULT_ENCODING));
                urlBuilder.append(":");
                urlBuilder.append(URLEncoder.encode(pair.getValue().toString(), DEFAULT_ENCODING));
                if (it.hasNext()) {
                    urlBuilder.append(";");
                }
            }
        }

        if(urlBuilder.length() > 1) {
            return urlBuilder.substring(1).toString();
        } else {
            return "";
        }
    }

    /**
     * Method used to construct the URL from query
     * @param fcFlag
     * @return String
     */
    private static String getGlueChar(boolean fcFlag) {
        if (fcFlag) {
            return "";
        }
        return "&";
    }

    /**
     * 
     * @param json
     * @return
     */
    public static String strip(String json) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == ' ' || c == '\n') {
                continue;
            }
            if (c == '\'') {
                sb.append("\"");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * This method returns the class, which is required to build the feature type
     * if input string type in not among String, int, double, boolean,
     * timestamp and geometry objects then default string is returned
     * @param strObj
     * @return Class
     */
    public static Class getClass(String strObj) {

        if (strObj == null) {
            return String.class;
        } else {
            if (strObj.equalsIgnoreCase("String")) {
                return String.class;
            } else if (strObj.equalsIgnoreCase("int")) {
                return Integer.class;
            } else if (strObj.equalsIgnoreCase("double")) {
                return Double.class;
            } else if (strObj.equalsIgnoreCase("boolean")) {
                return Boolean.class;
            } else if (strObj.equalsIgnoreCase("geometry")) {
                return Geometry.class;
            } else if (strObj.equalsIgnoreCase("point")) {
                return Point.class;
            } else if (strObj.equalsIgnoreCase("multipoint")) {
                return MultiPoint.class;
            } else if (strObj.equalsIgnoreCase("linestring")) {
                return LineString.class;
            } else if (strObj.equalsIgnoreCase("multilinestring")) {
                return MultiLineString.class;
            } else if (strObj.equalsIgnoreCase("polygon")) {
                return Polygon.class;
            } else if (strObj.equalsIgnoreCase("multipolygon")) {
                return MultiPolygon.class;
            } else if (strObj.equalsIgnoreCase("geometrycollection")) {
                return GeometryCollection.class;
            } else if (strObj.equalsIgnoreCase("timestamp")) {
                return Date.class;
            } else if (strObj.equalsIgnoreCase("number")) {
                return Double.class;
            } else {
                return String.class;
            }
        }
    }
    
    /**
     * Decodes a CRS ensuring the axis order is X/Y no matter what syntaxhas been used
     * @param srsName
     * @return
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     */
    public static CoordinateReferenceSystem decodeXY(String srsName) throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = CRS.decode(srsName, true);
        if(!isXYOriented(crs)) {
            return CRS.decode("EPSG:" + CRS.lookupEpsgCode(crs, false), true);
        } else {
            return crs;
        }
    }
    
    /**
     * Returns the axis order of the provided {@link CoordinateReferenceSystem} object.
     * TODO: this utility has been taken from GML2Utils, move it to CRS so that it can be shared
     */
    static boolean isXYOriented(CoordinateReferenceSystem crs) {
        CoordinateSystem cs = null;

        if (crs instanceof ProjectedCRS) {
            ProjectedCRS pcrs = (ProjectedCRS) crs;
            cs = pcrs.getBaseCRS().getCoordinateSystem();
        } else if (crs instanceof GeographicCRS) {
            cs = crs.getCoordinateSystem();
        } else {
            return true;
        }

        int dimension = cs.getDimension();
        int longitudeDim = -1;
        int latitudeDim = -1;

        for (int i = 0; i < dimension; i++) {
            AxisDirection dir = cs.getAxis(i).getDirection().absolute();

            if (dir.equals(AxisDirection.EAST)) {
                longitudeDim = i;
            }

            if (dir.equals(AxisDirection.NORTH)) {
                latitudeDim = i;
            }
        }

        if ((longitudeDim >= 0) && (latitudeDim >= 0)) {
            if (longitudeDim < latitudeDim) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
