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
package org.geotools.resources.coverage;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageReadParam;
import javax.measure.unit.Unit;
import javax.media.jai.ImageFunction;
import javax.media.jai.operator.ImageFunctionDescriptor;

import junit.framework.Assert;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.factory.FactoryFinder;
import org.geotools.util.NumberRange;
import org.hsqldb.Database;
import org.junit.Test;

import sun.management.counter.Units;

import com.vividsolutions.jts.noding.NodableSegmentString;


/**
 * Tests {@link CoverageUtilities}.
 *
 * @source $URL$
 */
public final class CoverageUtilitiesTest extends Assert {
	
	private final static class MyImageFunction implements ImageFunction{

		private final Random rand= new Random();
		
		@Override
	    public void getElements(float startX, float startY,float deltaX, float deltaY,int countX, int countY,int element, float[] real, float[] imag) {

	        int index = 0;
	        for (int row = 0; row < countY; row++) {
	            for (int col = 0; col < countX; col++, index++) {
	                real[index] = (float) rand.nextDouble();
	            }
	        }
	    }

		@Override
	    public void getElements(double startX, double startY,double deltaX, double deltaY,int countX, int countY,int element, double[] real, double[] imag) {
	        int index = 0;
	        for (int row = 0; row < countY; row++) {
	            for (int col = 0; col < countX; col++, index++) {
	                real[index] = rand.nextDouble();
	            }
	        }
		}

		@Override
		public int getNumElements() {
			return 1;
		}

		@Override
		public boolean isComplex() {
			return false;
		}
		
	}
    /**
     * Tests the checkEmptySourceRegion method.
     */
    @Test
    public void testCheckEmptySourceRegion() {
        final ImageReadParam params = new ImageReadParam();
        Rectangle sourceRegion = new Rectangle(300, 300, 700, 700);
        params.setSourceRegion(sourceRegion);
        Assert.assertEquals(sourceRegion.x, 300);
        Assert.assertEquals(sourceRegion.y, 300);
        Assert.assertEquals(sourceRegion.height, 700);
        Assert.assertEquals(sourceRegion.width, 700);
        
        final Rectangle intersecting = new Rectangle(400, 400, 900, 900);
        boolean isEmpty = CoverageUtilities.checkEmptySourceRegion(params, intersecting);
        
        Assert.assertFalse(isEmpty);
        final Rectangle intersection = params.getSourceRegion();
        Assert.assertEquals(intersection.x, 400);
        Assert.assertEquals(intersection.y, 400);
        Assert.assertEquals(intersection.height, 600);
        Assert.assertEquals(intersection.width, 600);
       
        final Rectangle intersecting2 = new Rectangle(0, 0, 300, 300);
        isEmpty = CoverageUtilities.checkEmptySourceRegion(params, intersecting2);
        Assert.assertTrue(isEmpty);
    }

    /**
     * Tests the {@link CoverageUtilities#getBackgroundValues(GridCoverage2D)} method.
     */
    @Test
    public void testNodata() {

    	// test coverage no data property
    	final HashMap properties= new HashMap();
    	properties.put("GC_NODATA", Double.valueOf(-9999.0));
    	final GridGeometry2D gg2D = new GridGeometry2D(
    			new Rectangle(0,0,800,600),
    			new Rectangle(-180,90,360,180));
    	
    	GridCoverage2D gc= CoverageFactoryFinder.getGridCoverageFactory(null).
    	create(
    			"test", 
    			ImageFunctionDescriptor.create(new MyImageFunction(), Integer.valueOf(800), Integer.valueOf(600), Float.valueOf(1.0f),  Float.valueOf(1.0f),  Float.valueOf(0.0f),  Float.valueOf(0.0f), null), 
    			gg2D, 
    			null, 
    			null, 
    			properties);
    	
    	double[] bkg=CoverageUtilities.getBackgroundValues(gc);
    	assertEquals(1, bkg.length);
    	assertEquals(Double.valueOf(-9999.0), bkg[0]);
    	
    	// test grid sampledimension no data property
    	final Category noDataCategory= new Category(CoverageUtilities.NODATA,new Color[]{Color.black},NumberRange.create(Double.valueOf(-9999.0),Double.valueOf(-9999.0)),NumberRange.create(Double.valueOf(-9999.0),Double.valueOf(-9999.0)));
    	final GridSampleDimension gsd = new GridSampleDimension("test", new Category[]{noDataCategory},Unit.ONE);
    	gc= CoverageFactoryFinder.getGridCoverageFactory(null).
    	create(
    			"test", 
    			ImageFunctionDescriptor.create(new MyImageFunction(), Integer.valueOf(800), Integer.valueOf(600), Float.valueOf(1.0f),  Float.valueOf(1.0f),  Float.valueOf(0.0f),  Float.valueOf(0.0f), null), 
    			gg2D, 
    			new GridSampleDimension[]{gsd}, 
    			null, 
    			null);
    	
    	bkg=CoverageUtilities.getBackgroundValues(gc);
    	assertEquals(1, bkg.length);
    	assertEquals(Double.valueOf(-9999.0), bkg[0]);
    	
    	// test getting NaN in case we do not have any no data
    	gc= CoverageFactoryFinder.getGridCoverageFactory(null).
    	create(
    			"test", 
    			ImageFunctionDescriptor.create(new MyImageFunction(), Integer.valueOf(800), Integer.valueOf(600), Float.valueOf(1.0f),  Float.valueOf(1.0f),  Float.valueOf(0.0f),  Float.valueOf(0.0f), null), 
    			gg2D, 
    			null, 
    			null, 
    			null);
    	
    	bkg=CoverageUtilities.getBackgroundValues(gc);
    	assertEquals(1, bkg.length);
    	assertTrue(Double.isNaN(bkg[0]));
    	
    }
    
    @Test
    public void testSuggestNodata(){
    	assertEquals(Byte.valueOf((byte)0), CoverageUtilities.suggestNoDataValue(DataBuffer.TYPE_BYTE));
    	assertTrue(Double.isNaN(CoverageUtilities.suggestNoDataValue(DataBuffer.TYPE_DOUBLE).doubleValue()));
    	assertTrue(Float.isNaN(CoverageUtilities.suggestNoDataValue(DataBuffer.TYPE_FLOAT).floatValue()));
    	assertEquals(Integer.valueOf(Integer.MIN_VALUE), CoverageUtilities.suggestNoDataValue(DataBuffer.TYPE_INT));
    	assertEquals(Short.valueOf((short)0), CoverageUtilities.suggestNoDataValue(DataBuffer.TYPE_USHORT));
    	assertEquals(Short.valueOf(Short.MIN_VALUE), CoverageUtilities.suggestNoDataValue(DataBuffer.TYPE_SHORT));
    }
}
