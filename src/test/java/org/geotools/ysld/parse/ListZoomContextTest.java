package org.geotools.ysld.parse;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.base.Optional;

public class ListZoomContextTest {

static final double EPSILON = 0.0000001;
    
    @Test
    public void testLevels() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d)); 
        
        assertThat(ctxt.getScaleDenominator(0), closeTo(5000000, EPSILON));
    }
    
    @Test
    public void testNonZeroInitial() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d), 2); 
        
        assertThat(ctxt.getScaleDenominator(2), closeTo(5000000, EPSILON));
    }
    
    @Test
    public void testBeforeList() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d)); 
        
        assertThat(ctxt.getScaleDenominator(-1), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testAfterList() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d)); 
        
        assertThat(ctxt.getScaleDenominator(1), is(0d));
    }
    @Test
    public void testMultiple() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d)); 
        
        assertThat(ctxt.getScaleDenominator(-1), is(Double.POSITIVE_INFINITY));
        assertThat(ctxt.getScaleDenominator(0), closeTo(5000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(1), closeTo(2000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(2), closeTo(1000000, EPSILON));
        assertThat(ctxt.getScaleDenominator(3), is(0d));
    }
   

    
    
    @Test
    public void testSingletonRange() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(1), Optional.of(1));
        
        assertThat(result.maxDenom, greaterThan(2000000d));
        assertThat(result.maxDenom, lessThan(5000000d));
        
        assertThat(result.minDenom, greaterThan(1000000d));
        assertThat(result.minDenom, lessThan(2000000d));
    }
    
    @Test
    public void testSingletonRangeFirst() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(0), Optional.of(0));
        
        assertThat(result.maxDenom, greaterThan(5000000d));
        assertThat(result.minDenom, lessThan(5000000d));
        
        assertThat(result.minDenom, greaterThan(2000000d));
    }
    
    @Test
    public void testSingletonRangeLast() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(2), Optional.of(2));
        
        assertThat(result.maxDenom, greaterThan(1000000d));
        assertThat(result.minDenom, lessThan(1000000d));
        
        assertThat(result.maxDenom, lessThan(2000000d));
    }
    
    @Test
    public void testRange() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(1), Optional.of(3));
        
        assertThat(result.maxDenom, greaterThan(2000000d));
        assertThat(result.maxDenom, lessThan(5000000d));
        
        assertThat(result.minDenom, lessThan(500000d));
        assertThat(result.minDenom, greaterThan(200000d));
    }
    
    @Test
    public void testRangeFirst() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(0), Optional.of(3));
        
        assertThat(result.maxDenom, greaterThan(5000000d));
        
        assertThat(result.minDenom, lessThan(500000d));
        assertThat(result.minDenom, greaterThan(200000d));
    }
    
    @Test
    public void testRangeLast() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(3), Optional.of(5));
        
        assertThat(result.maxDenom, greaterThan(500000d));
        assertThat(result.maxDenom, lessThan(1000000d));
        
        assertThat(result.minDenom, lessThan(100000d));
    }
    
    @Test
    public void testRangeOpenStart() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.<Integer>absent(), Optional.of(3));
        
        assertThat(result.maxDenom, is(Double.POSITIVE_INFINITY));
        
        assertThat(result.minDenom, lessThan(500000d));
        assertThat(result.minDenom, greaterThan(200000d));
    }
    
    @Test
    public void testRangeOpenEnd() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(3), Optional.<Integer>absent());
        
        assertThat(result.maxDenom, greaterThan(500000d));
        assertThat(result.maxDenom, lessThan(1000000d));
        
        assertThat(result.minDenom, is(0d));
    }
    
    @Test
    public void testRangePastStart() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(-1), Optional.of(3));
        
        assertThat(result.maxDenom, is(Double.POSITIVE_INFINITY));
        
        assertThat(result.minDenom, lessThan(500000d));
        assertThat(result.minDenom, greaterThan(200000d));
    }
    
    @Test
    public void testRangePastEnd() {
        
        ListZoomContext ctxt = new ListZoomContext(Arrays.asList(5000000d, 2000000d, 1000000d, 500000d, 200000d, 100000d)); 
        
        ScaleRange result = ctxt.getRange(Optional.of(3), Optional.of(6));
        
        assertThat(result.maxDenom, greaterThan(500000d));
        assertThat(result.maxDenom, lessThan(1000000d));
        
        assertThat(result.minDenom, is(0d));
    }
}
