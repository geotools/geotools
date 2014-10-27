package org.geotools.ysld.parse;

import javax.annotation.Nullable;

/**
 * A zoom context that can find half way points between zoom levels.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public abstract class MedialZoomContext implements ZoomContext{
    
    public MedialZoomContext() {
        super();
    }
    
    /** 
     * Get a scale between the given zoom level and the next 
     */
    protected abstract double getMedialScale(int level);
    
    @Override
    public ScaleRange getRange(@Nullable Integer min, @Nullable Integer max) {
        double minDenom = 0;
        double maxDenom = Double.POSITIVE_INFINITY;
        // Note that scale denominator is inverse to zoom so the maximum denominator is controlled
        // by the minimum zoom and vis versa
        if(min!=null) {
            maxDenom=getMedialScale(min-1);
        }
        if(max!=null) {
            minDenom=getMedialScale(max);
        }
        return new ScaleRange(minDenom, maxDenom);
    }
    
}