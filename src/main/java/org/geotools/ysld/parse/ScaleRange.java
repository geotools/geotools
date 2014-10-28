package org.geotools.ysld.parse;

import org.geotools.styling.Rule;

public class ScaleRange {
    final double minDenom;
    final double maxDenom;
    
    public ScaleRange(double minDenom, double maxDenom) {
        if(!(minDenom>=0 && !Double.isNaN(minDenom))) {
            throw new IllegalArgumentException("minDenom must be non-negative");
        }
        if(!(maxDenom>=0 && !Double.isNaN(minDenom))) {
            throw new IllegalArgumentException("maxDenom must be non-negative");
        }
        if(!(minDenom<=maxDenom)) {
            throw new IllegalArgumentException("maxDenom must be greater than or equal to minDenom");
        }
        
        this.minDenom = minDenom;
        this.maxDenom = maxDenom;
    }
    
    public void applyTo(Rule r) {
        r.setMaxScaleDenominator(maxDenom);
        r.setMinScaleDenominator(minDenom);
    }
    
    public boolean contains(double denom) {
        return minDenom<=denom && maxDenom>denom;
    }
    
    @Override
    public String toString() {
        return String.format("[1:%f, 1:%f)", minDenom, maxDenom);
    }

    public double getMinDenom() {
        return minDenom;
    }

    public double getMaxDenom() {
        return maxDenom;
    }
}