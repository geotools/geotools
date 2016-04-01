package org.geotools.ysld.parse;

import javax.annotation.Nullable;

/**
 * Represents a mapping between zoom level and scale.
 * 
 * @author Kevin Smith, Boundless
 *
 */
public interface ZoomContext {
    
    public static final String HINT_ID = "ZoomContext";
    
    /**
     * Find the reciprocal of the scale at a specified zoom level in this context.
     * @param level The level
     * @return The scale denominator
     */
    public double getScaleDenominator(int level);
    
    /**
     * Return a scale range covering the specified zoom level but no others.
     * @param min Minimum zoom level.  Absent for open ended.
     * @param max Maximum zoom level.  Absent for open ended.
     * @return
     */
    public ScaleRange getRange(@Nullable Integer min, @Nullable Integer max);
    
    /**
     * Is the given level within the range for which the context has clearly defined scales.
     * @param level
     * @return
     */
    public boolean isInRange(int level);
}
