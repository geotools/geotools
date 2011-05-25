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

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverageio.jp2k.JP2KFormat;
import org.geotools.coverageio.jp2k.JP2KReader;
import org.geotools.coverageio.jp2k.RasterLayerRequest;
import org.geotools.coverageio.jp2k.RasterManager;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * Testing {@link RasterLayerRequest}
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/jp2k/src/test/java/org/geotools/coverageio/jp2k/RasterLayerRequesTest.java $
 */
public final class RasterLayerRequesTest extends BaseJP2K {
	
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerRequesTest.class);
    /**
     * Creates a new instance of GranuleTest
     *
     * @param name
     */
    public RasterLayerRequesTest() {
    }

    @Test
    public void testRequest() throws Exception {
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
        
        final JP2KReader reader = new JP2KReader(file);
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final RasterManager manager = new RasterManager(reader);
        final ParameterValue<GridGeometry2D> gg = JP2KFormat.READ_GRIDGEOMETRY2D.createValue();
        final ParameterValue<Boolean> useJAI = JP2KFormat.USE_JAI_IMAGEREAD.createValue();
        useJAI.setValue(false);
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), envelope));
        final GeneralParameterValue[] params = new GeneralParameterValue[] { gg, useJAI};
        final RasterLayerRequest request = new RasterLayerRequest(params, manager);
        final Rectangle area = request.getDestinationRasterArea();
        assertEquals(area.width, 400);
        assertEquals(area.height, 200);
        final AffineTransform g2w = request.getRequestedGridToWorld();
        assertEquals(g2w.getScaleX(), 0.9, DELTA);
        assertEquals(g2w.getScaleY(), -0.9, DELTA);
        assertEquals(g2w.getTranslateX(), -179.55, DELTA);
        assertEquals(g2w.getTranslateY(), 89.55, DELTA);
        
        final String requestS = request.toString();
        if (TestData.isInteractiveTest()){
        	if (LOGGER.isLoggable(Level.INFO))
        		LOGGER.info(requestS);
        }
        
   }

}
