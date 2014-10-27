package org.geotools.ysld.parse;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Test;

public class RatioZoomContextTest {
    
    static final double EPSILON = 0.0000001;
    
    @Test
    public void testLevels() {
        
        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2); 
        
        assertThat(ctxt.getScaleDenominator(0), Matchers.closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), Matchers.closeTo(5000000/2, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), Matchers.closeTo(5000000/4, EPSILON));
    }
    
    @Test
    public void testNonIntegerRatio() {
        
        RatioZoomContext ctxt = new RatioZoomContext(5000000, 1.5); 
        
        assertThat(ctxt.getScaleDenominator(0), Matchers.closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), Matchers.closeTo(5000000/1.5, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), Matchers.closeTo(5000000/(1.5*1.5), EPSILON));
    }
    
    @Test
    public void testNonZeroInitial() {
        
        RatioZoomContext ctxt = new RatioZoomContext(2, 5000000, 2); 
        
        assertThat(ctxt.getScaleDenominator(0), Matchers.closeTo(5000000*4, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), Matchers.closeTo(5000000*2, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), Matchers.closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(3), Matchers.closeTo(5000000/2, EPSILON));
        assertThat(ctxt.getScaleDenominator(4), Matchers.closeTo(5000000/4, EPSILON));
    }
    
    @Test
    public void testSingletonRangeInitial() {
        
        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2); 
        
        ScaleRange result = ctxt.getRange(0, 0);
        
        assertThat(result.maxDenom, Matchers.greaterThan(5000000d));
        assertThat(result.minDenom, Matchers.lessThan(5000000d));
        assertThat(result.maxDenom, Matchers.lessThan(5000000d*2));
        assertThat(result.minDenom, Matchers.greaterThan(5000000d/2));
    }
    
    @Test
    public void testSingletonRange() {
        
        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2); 
        
        ScaleRange result = ctxt.getRange(2, 2);
        
        assertThat(result.maxDenom, Matchers.greaterThan(5000000d/4));
        assertThat(result.minDenom, Matchers.lessThan(5000000d/4));
        assertThat(result.maxDenom, Matchers.lessThan(5000000d*2/4));
        assertThat(result.minDenom, Matchers.greaterThan(5000000d/2/4));
    }
   
    @Test
    public void testRange() {
        
        RatioZoomContext ctxt = new RatioZoomContext(5000000, 2); 
        
        ScaleRange result = ctxt.getRange(0, 2);
        
        assertThat(result.maxDenom, Matchers.greaterThan(5000000d));
        assertThat(result.maxDenom, Matchers.lessThan(5000000d*2));

        assertThat(result.minDenom, Matchers.lessThan(5000000d/4));
        assertThat(result.minDenom, Matchers.greaterThan(5000000d/2/4));
    }
   
}
