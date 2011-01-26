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
package org.geotools.data.wms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.referencing.CRS;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


/**
 * Provides miscellaneous utility methods for use with WMSs.
 *
 * @author Richard Gould
 * @source $URL$
 */
public class WMSUtils {
    /**
     * Utility method to return each layer that has a name. This method maintains no hierarchy at all.
     * 
     * @return An array of Layers, each value has a it's name property set or an empty array if there are none. It will return null if there is no capabilities document
     * 
     */
    public static Layer[] getNamedLayers(WMSCapabilities capabilities) {
    	
    	if (capabilities == null) {
    		return null;
    	}
    	
        List namedLayersList = new ArrayList();
        
        Layer[] layers = (Layer[]) capabilities.getLayerList().toArray(new Layer[capabilities.getLayerList().size()]);
    
        for( int i = 0; i < layers.length; i++ ) {
            if ((layers[i].getName() != null) && (layers[i].getName().length() != 0)) {
                namedLayersList.add(layers[i]);
            }
        }
    
        Layer[] namedLayers = new Layer[namedLayersList.size()];
        for (int i = 0; i < namedLayersList.size(); i++) {
            namedLayers[i] = (Layer) namedLayersList.get(i);
        }
        
        return namedLayers;
    }

    public static Set getQueryableLayers(WMSCapabilities capabilities) {
        Set layers = new TreeSet();
    
        Layer[] namedLayers = getNamedLayers(capabilities);
    
        for( int i = 0; i < namedLayers.length; i++ ) {
            Layer layer = namedLayers[i];
    
            if (layer.isQueryable()) {
                layers.add(layer);
            }
        }
    
        return layers;
    }

    public static Set getSRSs(WMSCapabilities capabilities) {
        Set srss = new TreeSet();
    
        Layer[] layers = (Layer[]) capabilities.getLayerList().toArray(new Layer[capabilities.getLayerList().size()]);
    
        for( int i = 0; i < layers.length; i++ ) {
            if (layers[i].getSrs() != null) {
                srss.addAll(layers[i].getSrs());
            }
        }
    
        return srss;
    }
    
    /**
     * Given a list of type Layer, return all EPSG codes that is supported
     * by all of the layers. This is an intersection of each layer's SRS set.
     *  
     * @param layers A List of type Layer
     * @return a Set of type String, containin EPSG codes, or empty if none found
     */
    public static Set findCommonEPSGs(List layers) {
    	TreeSet set = new TreeSet();
    	
    	Layer first = (Layer) layers.get(0);
    	
    	set.addAll(first.getSrs());
    	
    	for (int i = 1; i < layers.size(); i++ ) {
    		Layer layer = (Layer) layers.get(i);
    		set.retainAll(layer.getSrs());
    	}
    	
    	return set;
    }
    
    //Map<CoordinateReferenceSystem, TreeSet<String>>
    private static Map crsCache;
    
    static {
    	crsCache = new HashMap();
    }
    
    /**
     * Given a CRS and a Set of type String consisting of EPSG CRS codes
     * (such as "EPSG:4326"), it will check the transform that exists between
     * each EPSG code's CRS and the given CRS. If this is the identity
     * transform, meaning the CRS is equivalent to the EPSG code, 
     * the used EPSG code will be returned. The first valid EPSG code found
     * is returned, so it is possibly that multiple valid codes exist.
     * 
     * If no such identity transform can be found, null will be returned.
     * 
     * If this method is succesful, the result is stored in a cache, which is
     * called in subsequent calls. 
     * 
     * @param crs a CRS that is to be compared to each EPSG code in codes
     * @param codes a Set of type String containing EPSG codes
     * @return an EPSG code that correspondes to crs, or null if one can't be found
     */
    public static String matchEPSG(CoordinateReferenceSystem crs, Set codes) {
    	
    	TreeSet previous = (TreeSet) crsCache.get(crs);
    	if (previous != null) {
    		
    		Iterator iter = previous.iterator();
    		while (iter.hasNext()) {
    			String code = (String) iter.next();
    			
    			if (codes.contains(code)) {
    				return code;
    			}
    		}
    	}
    	
    	Iterator iter = crs.getIdentifiers().iterator();
    	while (iter.hasNext()) {
    		Identifier ident = (Identifier) iter.next();
    		String epsgCode = ident.toString();
    		if (codes.contains(epsgCode)) {
    			if (crsCache.get(crs) == null) {
    				crsCache.put(crs, new TreeSet());
    			}
    			TreeSet set = (TreeSet) crsCache.get(crs);
    			set.add(epsgCode);
    			
    			return epsgCode;
    		}
    	}
    	
    	iter = null;
    	
    	iter = codes.iterator();
    	while (iter.hasNext()) {
    		String epsgCode = (String) iter.next();
    		
    		CoordinateReferenceSystem epsgCRS;
			try {
				epsgCRS = CRS.decode(epsgCode);
			} catch (Exception e) {
//				e.printStackTrace();
				continue;
			}
    		
    		MathTransform transform;
			try {
				transform = CRS.findMathTransform(crs, epsgCRS, true);
			} catch (FactoryException e) {
//				e.printStackTrace();
				continue;
			}
    		
    		if (transform.isIdentity()) {
    			if (crsCache.get(crs) == null) {
    				crsCache.put(crs, new TreeSet());
    			}
    			TreeSet set = (TreeSet) crsCache.get(crs);
    			set.add(epsgCode);
    			
    			return epsgCode;
    		}
    	}
    	return null;
    }
}
