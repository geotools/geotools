package org.geotools.ysld.parse;


/**
 * Zoom Context supporting non-integer levels.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public abstract class ContinuousZoomContext extends MedialZoomContext implements ZoomContext {
    
    public ContinuousZoomContext() {
        super();
    }
    
    /**
     * Find the reciprocal of the scale at a specified zoom level in this context.
     * @param level The level
     * @return The scale denominator
     */
    protected abstract double getScaleDenominator(double level);
    
    /**
     * Find the reciprocal of the scale at a specified zoom level in this context.
     * @param level The level
     * @return The scale denominator
     */
    @Override
    public double getScaleDenominator(int level) {
        return getScaleDenominator(level+0d);
    }
    
    /** 
     * Get a scale between the given zoom level and the next 
     */
    @Override
    protected double getMedialScale(int level) {
        return getScaleDenominator(level+0.5d);
    }
    
}