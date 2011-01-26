/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import junit.framework.TestCase;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.XMath;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

public class RenderUtilitiesTest extends TestCase {

	public void testScaleOutsideCrsDefinition() throws Exception {
		CoordinateReferenceSystem utm1N = CRS.decode("EPSG:32601");
		ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(0, 0, 100,
				100), utm1N);
		try {
			RendererUtilities.calculateScale(re, 100, 100, 75);
			fail("Should have failed, envelope outside of the source crs validity area");
		} catch (IllegalArgumentException e) {
			// ok
		}
	}

	public void testScaleProjected() throws Exception {
		CoordinateReferenceSystem utm1N = CRS.decode("EPSG:32601");
		// valid coords for utm nord 1 start from (200000, 0)
		ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(200000, 200100,
				0, 100), utm1N);
		double scale = RendererUtilities.calculateScale(re, 100, 100, 2.54);
		assertEquals(100.0, scale, 0.1); // account for projection deformation
	}
	
	public void testScaleCartesian() throws Exception {
		ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(0, 10,
				0, 10), DefaultEngineeringCRS.CARTESIAN_2D);
		double scale = RendererUtilities.calculateScale(re, 10 * 100, 10 * 100, 2.54);
		assertEquals(1.0, scale, 0.00001); // no projection deformation here!
	}
	
	public void testScaleGeneric() throws Exception {
		ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(0, 10,
				0, 10), DefaultEngineeringCRS.GENERIC_2D);
		double scale = RendererUtilities.calculateScale(re, 10 * 100, 10 * 100, 2.54);
		assertEquals(1.0, scale, 0.00001); // no projection deformation here!
	}
    
    public void testOGCScaleProjected() throws Exception {
        ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(0, 10,
                0, 10), DefaultEngineeringCRS.CARTESIAN_2D);
        int tenMetersPixels = (int) Math.round(10 / 0.00028);
        double scale = RendererUtilities.calculateOGCScale(re, tenMetersPixels , new HashMap());
        assertEquals(1.0, scale, 0.0001);
    }
    
    public void testOGCScaleGeographic() throws Exception {
        // same example as page 29 in the SLD OGC spec, but with the expected scale corrected
        // since the OGC document contains a very imprecise one
        ReferencedEnvelope re = new ReferencedEnvelope(new Envelope(0, 2,
                0, 2), DefaultGeographicCRS.WGS84);
        double scale = RendererUtilities.calculateOGCScale(re, 600 , new HashMap());
        assertEquals(1325232.03, scale, 0.01);
    }
    
    public void testOGCScaleAffineProjected() throws Exception {
        // 1 pixel == 500 m  =>  0.00028 m [ screen] == 500 m [world]
        // => scaleDenominator = 500/0.00028
        final AffineTransform screenToWord = AffineTransform.getScaleInstance(500,500);
        final AffineTransform worldToScreen = screenToWord.createInverse();
        
        final CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        double scale;
        
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen,new HashMap());
        assertEquals(500/0.00028, scale, 0.0001);
        
        worldToScreen.rotate(1.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen,new HashMap());
        assertEquals(500/0.00028, scale, 0.0001);
        
        worldToScreen.translate(100.0,100.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen,new HashMap());
        assertEquals(500/0.00028, scale, 0.0001);
    }
    
    public void testOGCScaleAffineGeographic() throws Exception {
        // 1 pixel == 0.5 degree  =>  0.00028 m [ screen] == 0.5 degree * OGC_DEGREE_TO_METERS m [world]
        // => scaleDenominator = 0.5 * OGC_DEGREE_TO_METERS/0.00028
        final AffineTransform screenToWord = AffineTransform.getScaleInstance(0.5,0.5);
        final AffineTransform worldToScreen = screenToWord.createInverse();
        
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        double scale;
        
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen,new HashMap());
        assertEquals(0.5*RendererUtilities.OGC_DEGREE_TO_METERS/0.00028, scale, 0.0001);
        
        worldToScreen.rotate(1.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen,new HashMap());
        assertEquals(0.5*RendererUtilities.OGC_DEGREE_TO_METERS/0.00028, scale, 0.0001);
        
        worldToScreen.translate(100.0,100.0);
        scale = RendererUtilities.calculateOGCScaleAffine(crs, worldToScreen,new HashMap());
        assertEquals(0.5*RendererUtilities.OGC_DEGREE_TO_METERS/0.00028, scale, 0.0001);
      
    }
    
    public void testCreateMapEnvelope() throws Exception {
        final double offset = 10000;
        final Rectangle paintArea = new Rectangle(0,0,800,600);
      
        final AffineTransform worldToScreen = AffineTransform.getScaleInstance(0.5,0.5);
        worldToScreen.translate(-offset,-offset);
        AffineTransform at = new AffineTransform(worldToScreen);
        Envelope env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(new Envelope(offset,offset+1600,offset,offset+1200),env,0.001);
        // Test for negative world coordinates
        at.translate(2*offset,2*offset);
        env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(new Envelope(-offset,-offset+1600,-offset,-offset+1200),env,0.001);
        // Restore to standard offset
        at.translate(-2*offset,-2*offset);
        at.rotate(Math.PI/2.0,offset,offset);
        env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(new Envelope(offset,offset+1200,offset-1600,offset),env,0.0001);
        at = new AffineTransform(worldToScreen);
        at.rotate(Math.PI/4.0,offset,offset);
        env = RendererUtilities.createMapEnvelope(paintArea, at);
        assertEnvelopeEquals(new Envelope(
                                          offset,
                                          offset+Math.cos(Math.PI/4.0)*1600 + Math.sin(Math.PI/4.0)*1200,
                                          offset-Math.sin(Math.PI/4.0)*1600,
                                          offset+Math.cos(Math.PI/4.0)*1200),env,0.0001);
        
    }
    
    private void assertEnvelopeEquals(Envelope expected, Envelope actual, double delta) {
         if (expected.equals(actual)) {
             return;
         }
         boolean equals = true;
         equals &= Math.abs(expected.getMinX() - actual.getMinX()) <= delta;
         equals &= Math.abs(expected.getMaxX() - actual.getMaxX()) <= delta;
         equals &= Math.abs(expected.getMinY() - actual.getMinY()) <= delta;
         equals &= Math.abs(expected.getMaxY() - actual.getMaxY()) <= delta;
         if (!equals) {
             failNotEquals(null,expected,actual); 
         }
 
         
        
    }

    /**
     * The following test is from the tile module where the behavior
     * of RenderUtilities changed between 2.2. and 2.4.
     */
    public void testCenterTile() throws Exception {
        Envelope centerTile = new Envelope( 0, 36, -18, 18 );
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        double scale = RendererUtilities.calculateScale(centerTile, crs, 512, 512, 72.0 );
        double groundDistance = XMath.hypot( 36,  36) * (1852   * 60);
        double pixelDistance  = XMath.hypot(512, 512) * (0.0254 / 72);
        double expected = groundDistance / pixelDistance;
        assertEquals(expected, scale, expected * 0.05); // no projection deformation here!
    }
}
