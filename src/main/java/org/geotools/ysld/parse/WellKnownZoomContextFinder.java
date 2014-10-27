package org.geotools.ysld.parse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nullable;

/**
 * A collection of commonly used ZoomContexts
 * 
 * @author Kevin Smith, Boundless
 *
 */
public class WellKnownZoomContextFinder implements ZoomContextFinder {
    
    static private WellKnownZoomContextFinder INSTANCE = new WellKnownZoomContextFinder();
    
    public static WellKnownZoomContextFinder getInstance(){
        return INSTANCE;
    }
    
    private WellKnownZoomContextFinder() {
        contexts = new HashMap<>();
        canonicalNames = new TreeSet<>();
        
        ZoomContext googleMercatorExtended = new RatioZoomContext(559082263.9508929, 2);
        contexts.put("WebMercator".toUpperCase(), googleMercatorExtended);
        contexts.put("SphericalMercator".toUpperCase(), googleMercatorExtended);
        contexts.put("GoogleMercator".toUpperCase(), googleMercatorExtended);
        contexts.put("EPSG:3587".toUpperCase(), googleMercatorExtended);
        contexts.put("EPSG:900913".toUpperCase(), googleMercatorExtended);
        contexts.put("EPSG:3857".toUpperCase(), googleMercatorExtended);
        contexts.put("EPSG:3785".toUpperCase(), googleMercatorExtended);
        contexts.put("OSGEO:41001".toUpperCase(), googleMercatorExtended);
        canonicalNames.add("EPSG:3857");
        
        ZoomContext plateCarree = null;  // TODO decide on a definition
        contexts.put("PlateCarree".toUpperCase(), plateCarree);
        contexts.put("EPSG:4326".toUpperCase(), plateCarree);
        canonicalNames.add("EPSG:4326");
    }
    
    final Set<String> canonicalNames;
    final Map<String, ZoomContext> contexts;
    
    @Override
    public @Nullable ZoomContext get(String name) {
        return contexts.get(name.toUpperCase());
    }

    @Override
    public Set<String> getNames() {
        return contexts.keySet();
    }

    @Override
    public Set<String> getCanonicalNames() {
        return Collections.unmodifiableSet(canonicalNames);
    }
    
}
