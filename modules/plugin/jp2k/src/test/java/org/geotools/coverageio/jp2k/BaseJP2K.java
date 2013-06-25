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
 */
package org.geotools.coverageio.jp2k;

import java.io.IOException;
import java.util.logging.Logger;

import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.test.TestData;
import org.junit.Assert;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * Base testing class initializing JAI properties to be used during tests.
 *
 *
 * @source $URL$
 */
@SuppressWarnings("deprecation")
public class BaseJP2K extends Assert{

	protected final static double DELTA = 1.0E-6;
	
	/**
	 * The {@code GridFormatFactorySpi} provided by the specific subclass to
	 * handle a specific format.
	 */
	protected final static JP2KFormatFactory factorySpi  = new JP2KFormatFactory();
	
    protected final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BaseJP2K.class);

	protected static void forceDataLoading(final GridCoverage2D gc) {
    	Assert.assertNotNull(gc);

        if (TestData.isInteractiveTest()) {
           final JFrame frame= new JFrame();
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.getContentPane().add(new ScrollingImagePanel(gc.getRenderedImage(),800,800));
           frame.pack();
           SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				frame.setVisible(true);
				
			}});
        } else {
            PlanarImage.wrapRenderedImage(gc.getRenderedImage()).getTiles();
        }
    }

    protected boolean testingEnabled() {
        boolean available = factorySpi.isAvailable();

        if (!available) {
            LOGGER.warning("Kakadu libraries are not available, skipping tests!");
        }

        return available;
    }
    
    /**
     * @param reader
     * @throws IOException 
     */
    protected void checkReader(GridCoverage2DReader reader) throws IOException {
        Assert.assertNotNull(reader);
        

        // layout checks
        final ImageLayout layout = reader.getImageLayout();
        Assert.assertNotNull(layout);
        Assert.assertNotNull(layout.getColorModel(null));
        Assert.assertNotNull(layout.getSampleModel(null));
        Assert.assertEquals(0,layout.getMinX(null));
        Assert.assertEquals(0,layout.getMinY(null));
        Assert.assertTrue(layout.getWidth(null) > 0);
        Assert.assertTrue(layout.getHeight(null) > 0);
        Assert.assertEquals(0,layout.getTileGridXOffset(null));
        Assert.assertEquals(0,layout.getTileGridYOffset(null));
        Assert.assertTrue(layout.getTileHeight(null) > 0);
        Assert.assertTrue(layout.getTileWidth(null) > 0);
        
    }
}
