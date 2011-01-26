/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.matfile5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.SoftValueHashMap;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

class UTMUtilities {

    private final static Logger LOGGER = org.geotools.util.logging.Logging
    .getLogger("org.geotools.coverageio.matfile5.sas");
    
    private UTMUtilities(){
        
    }

    public final static List<String> UTM_NORTHS = new ArrayList<String>(60);
    
    public final static List<String> UTM_SOUTHS = new ArrayList<String>(60);
    
    private final static String EPSG_N = "EPSG:326";
    private final static String EPSG_S = "EPSG:327";
    
    private final static Map<String, ReferencedEnvelope> AOI_MAP = new HashMap<String, ReferencedEnvelope>(120); 
    
    private final static SoftValueHashMap<String, CoordinateReferenceSystem> CRS_MAP = new SoftValueHashMap<String, CoordinateReferenceSystem>();
    
    private final static String DEFAULT_FIRST_TO_BE_SEEKED = "EPSG:32634";
    
    static{
        for (int i=1;i<61;i++){
            StringBuilder sbn = new StringBuilder(EPSG_N);
            StringBuilder sbs = new StringBuilder(EPSG_S);
            if (i<10){
                sbn.append("0");
                sbs.append("0");
            }
            sbn.append(i);
            sbs.append(i);
            String epsgcodeN = sbn.toString();
            String epsgcodeS = sbs.toString();
            UTM_NORTHS.add(epsgcodeN);
            UTM_SOUTHS.add(epsgcodeS);
            
            for (int j=0;j<2;j++){
                String epsgCode = j==0?epsgcodeN:epsgcodeS;
                CoordinateReferenceSystem crs=null;
                try {
                    crs = getCRS(epsgCode);
                } catch (NoSuchAuthorityCodeException e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage());
                } catch (FactoryException e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage());
                }
                if (crs!=null){
                    Extent extent = crs.getDomainOfValidity();
                    Collection<? extends GeographicExtent> geo = extent.getGeographicElements();
                    GeographicExtent geoext = geo.iterator().next();
                    GeographicBoundingBoxImpl impl = (GeographicBoundingBoxImpl) geoext;
                    ReferencedEnvelope env = getReferencedEnvelopeFromGeographicBoundingBox(impl);
                    AOI_MAP.put(epsgCode,env);
                }
            }
        }
    }
    
    
    /**
     * Builds a {@link ReferencedEnvelope} from a {@link GeographicBoundingBox}.
     * This is useful in order to have an implementation of {@link BoundingBox}
     * from a {@link GeographicBoundingBox} which strangely does implement
     * {@link GeographicBoundingBox}.
     * 
     * @param geographicBBox
     *                the {@link GeographicBoundingBox} to convert.
     * @return an instance of {@link ReferencedEnvelope}.
     */
    private static ReferencedEnvelope getReferencedEnvelopeFromGeographicBoundingBox(
            final GeographicBoundingBox geographicBBox) {
        if (geographicBBox==null)
            throw new IllegalArgumentException("Provided null bounding box");
        return new ReferencedEnvelope(geographicBBox.getEastBoundLongitude(),
                geographicBBox.getWestBoundLongitude(), geographicBBox
                        .getSouthBoundLatitude(), geographicBBox
                        .getNorthBoundLatitude(), DefaultGeographicCRS.WGS84);
    }
    
    
    /**
     * Returns a {@link SpatioTemporalMetadata} instance for the specified
     * imageIndex.
     * 
     * @param imageIndex
     *                the index of the specified 2D raster.
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     * @see SpatioTemporalImageReader#getSpatioTemporalMetadata(int)
     */
    private static CoordinateReferenceSystem getCRS(String epsgCode) throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = null;
        if (epsgCode!=null && epsgCode.trim().length()>1)
        synchronized (CRS_MAP) {
            if (!CRS_MAP.containsKey(epsgCode)) {
                crs = CRS.decode(epsgCode);
                CRS_MAP.put(epsgCode, crs);
            } else {
                crs = CRS_MAP.get(epsgCode);
                if (crs == null) {
                    crs = CRS.decode(epsgCode);
                    CRS_MAP.put(epsgCode, crs);
                }
            }
        }
        return crs;
    }
    
    public static CoordinateReferenceSystem getProperUTM(final double lon, final double lat){
        ReferencedEnvelope env;
        CoordinateReferenceSystem crs = null;
        try {
        if (AOI_MAP.containsKey(DEFAULT_FIRST_TO_BE_SEEKED)){
            env = AOI_MAP.get(DEFAULT_FIRST_TO_BE_SEEKED);
            if (env.contains(lon,lat)){
               
                    return getCRS(DEFAULT_FIRST_TO_BE_SEEKED);
            }
        }
        Iterator<String> keys = AOI_MAP.keySet().iterator();
        while (keys.hasNext()){
            String code = keys.next();
            if (AOI_MAP.containsKey(code)){
                env = AOI_MAP.get(code);
                if (env.contains(lon,lat)){
                    return getCRS(code);
                }
            }   
        }
        } catch (NoSuchAuthorityCodeException e) {
            crs = null;
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, e.getLocalizedMessage());
        } catch (FactoryException e) {
            crs = null;
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, e.getLocalizedMessage());
        }
        return crs;
    }
}
