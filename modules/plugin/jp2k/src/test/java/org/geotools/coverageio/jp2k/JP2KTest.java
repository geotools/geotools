/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverageio.jp2k;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverageio.jp2k.JP2KFormat;
import org.geotools.coverageio.jp2k.JP2KReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * Testing {@link JP2KReader}
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/jp2k/src/test/java/org/geotools/coverageio/jp2k/JP2KTest.java $
 */
public final class JP2KTest extends BaseJP2K {
	
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JP2KTest.class);
	
    /**
     * Creates a new instance of JP2KTest
     *
     * @param name
     */
    public JP2KTest() {
    }

    @Test
    public void testTiledImageReadMT() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        File file = null;
        try{
            file = TestData.file(this, "sample.jp2");
        }catch (FileNotFoundException fnfe){
            LOGGER.warning("test-data not found: sample.jp2 \nTests are skipped");
            return;
        }

        final JP2KFormat format= factorySpi.createFormat();
        assertTrue(format.accepts(file));
        
        
        // //
        //
        // Setting several parameters
        //
        // //
        final Hints hints = new Hints(Hints.OVERVIEW_POLICY,OverviewPolicy.getDefaultPolicy());
        final JP2KReader reader = new JP2KReader(file,hints);
        final ParameterValue<GridGeometry2D> gg = JP2KFormat.READ_GRIDGEOMETRY2D.createValue();
        final ParameterValue<Boolean> useMT = JP2KFormat.USE_MULTITHREADING.createValue();
        final ParameterValue<Boolean> useJAI = JP2KFormat.USE_JAI_IMAGEREAD.createValue();
        final ParameterValue<String> tileSize= JP2KFormat.SUGGESTED_TILE_SIZE.createValue();
        final ParameterValue<Color> transparentColor = JP2KFormat.INPUT_TRANSPARENT_COLOR.createValue();
        transparentColor.setValue(new Color(0, 0, 0));
        tileSize.setValue("128,128");
        
        useMT.setValue(false);
        useJAI.setValue(true);
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), oldEnvelope));
        
        // //
        //
        // Reading
        //
        // //
        final GridCoverage2D gc = (GridCoverage2D) reader.read(
        		new GeneralParameterValue[] { gg, useJAI, useMT, tileSize, transparentColor});
        assertNotNull(gc);
        forceDataLoading(gc);
        
        final MathTransform g2w = reader.getRaster2Model();
        final AffineTransform at = (AffineTransform)g2w;
        assertEquals(at.getScaleX(), 0.9, DELTA);
        assertEquals(at.getScaleY(), -0.9, DELTA);
        assertEquals(at.getTranslateX(), -179.55, DELTA);
        assertEquals(at.getTranslateY(), 89.55, DELTA);
        assertEquals(gc.getRenderedImage().getWidth(), 400);
        assertEquals(gc.getRenderedImage().getHeight(), 200);
    }
    
    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }

        File file = null;
        try{
        	file = TestData.file(this, "bogota.jp2");
        }catch (FileNotFoundException fnfe){
        	LOGGER.warning("test-data not found: bogota.jp2 \nTests are skipped");
        	return;
        }
        final JP2KFormat format= factorySpi.createFormat();
        assertTrue(format.accepts(file));
        
        // //
        //
        // Testing Direct read
        //
        // //

        final AbstractGridCoverage2DReader reader = new JP2KReader(file);
        final int nCov = reader.getGridCoverageCount();
        assertEquals(nCov, 1);
        
        final ParameterValue<GridGeometry2D> gg = JP2KFormat.READ_GRIDGEOMETRY2D.createValue();
        final ParameterValue<Boolean> useJAI = JP2KFormat.USE_JAI_IMAGEREAD.createValue();
        useJAI.setValue(false);
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), oldEnvelope));
        GridCoverage2D gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg, useJAI});
        assertNotNull(gc);
        forceDataLoading(gc);
        
        // //
        //
        // Testing simple imageRead
        //
        // //

        useJAI.setValue(true);
        final Envelope wgs84Envelope = CRS.transform(oldEnvelope, DefaultGeographicCRS.WGS84);
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), wgs84Envelope));
        gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] {gg, useJAI});
        assertNotNull(gc);
        forceDataLoading(gc);
    }
    
    @Before
	public void setUp() throws Exception {
	    ImageIO.setUseCache(false);
	    JAI.getDefaultInstance().getTileCache().setMemoryCapacity(
	            10 * 1024 * 1024);
	    JAI.getDefaultInstance().getTileCache().setMemoryThreshold(1.0f);
	    JAI.getDefaultInstance().getTileScheduler().setParallelism(2);
	    JAI.getDefaultInstance().getTileScheduler().setPrefetchParallelism(2);
	    JAI.getDefaultInstance().getTileScheduler().setPrefetchPriority(5);
	    JAI.getDefaultInstance().getTileScheduler().setPriority(5);
	}
}
