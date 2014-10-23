package org.geotools.ysld.parse;

import com.google.common.base.Optional;
import com.sun.istack.internal.Nullable;

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
    public ScaleRange getRange(Optional<Integer> min, Optional<Integer> max);
}
