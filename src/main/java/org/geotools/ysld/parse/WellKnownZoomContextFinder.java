package org.geotools.ysld.parse;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class WellKnownZoomContextFinder implements ZoomContextFinder {
    
    static final Map<String, ZoomContext> wellKnownZoomContexts;
    static {
        wellKnownZoomContexts = new HashMap<>();
        
        ZoomContext googleMercatorExtended = new RatioZoomContext(559082263.9508929, 2);
        wellKnownZoomContexts.put("WebMercator".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("SphericalMercator".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("GoogleMercator".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:3587".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:900913".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:3857".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("EPSG:3785".toUpperCase(), googleMercatorExtended);
        wellKnownZoomContexts.put("OSGEO:41001".toUpperCase(), googleMercatorExtended);
    }
    
    @Override
    public @Nullable ZoomContext get(String name) {
        return wellKnownZoomContexts.get(name.toUpperCase());
    }
    
}
