package org.geotools.ysld.parse;

import org.geotools.styling.Rule;

public class ScaleRange {
    final double minDenom;
    final double maxDenom;
    
    public ScaleRange(double minDenom, double maxDenom) {
        if(!(minDenom>=0 && minDenom<Double.POSITIVE_INFINITY && !Double.isNaN(minDenom))) {
            throw new IllegalArgumentException("minDenom must be finite and non negative");
        }
        if(!(maxDenom>0 && !Double.isNaN(minDenom))) {
            throw new IllegalArgumentException("maxDenom must be positive");
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
}