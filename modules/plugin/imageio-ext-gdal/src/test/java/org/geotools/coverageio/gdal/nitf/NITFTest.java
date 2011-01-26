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
package org.geotools.coverageio.gdal.nitf;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.coverageio.gdal.GDALTestCase;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * 
 * Testing {@link NITFReader}
 *
 * @source $URL$
 */
public final class NITFTest extends GDALTestCase {
	/**
	 * file name of a valid NITF sample data to be used for tests.
	 * The sample data is available at <A HREF="http://dl.maptools.org/dl/gdal/data/nitf/cadrg/"> 
	 * cadrg sample data</A>
	 */
	private final static String fileName = "001zc013.on1";

	/**
	 * Creates a new instance of {@code NITFTest}
	 * 
	 * @param name
	 */
	public NITFTest() {
		super( "NITF", new NITFFormatFactory());
	}
	

	@Test
	public void test() throws Exception {
		if (!testingEnabled()) {
			return;
		}

		// get a reader
		File file =null;
		try{
			file = TestData.file(this, fileName);
		}
		catch (FileNotFoundException fnfe){
			LOGGER.warning("Test File not found, please download it at: http://dl.maptools.org/dl/gdal/data/nitf/cadrg/001zc013.on1");
			return;
		}
		catch (IOException ioe){
			LOGGER.warning("Test File not found, please download it at: http://dl.maptools.org/dl/gdal/data/nitf/cadrg/001zc013.on1");
			return;
		}
		
		// Preparing an useful layout in case the image is striped.
                final ImageLayout l = new ImageLayout();
                l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512)
                                .setTileWidth(512);

                Hints hints = new Hints();
                hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));
			 
		final BaseGDALGridCoverage2DReader reader = new NITFReader(file, hints);

		// /////////////////////////////////////////////////////////////////////
		//
		// read once
		//
		// /////////////////////////////////////////////////////////////////////
		GridCoverage2D gc = (GridCoverage2D) reader.read(null);
		forceDataLoading(gc);

		// /////////////////////////////////////////////////////////////////////
		//
		// read again with subsampling and crop
		//
		// /////////////////////////////////////////////////////////////////////
		final double cropFactor = 2.0;
		final int oldW = gc.getRenderedImage().getWidth();
		final int oldH = gc.getRenderedImage().getHeight();
		final Rectangle range = ((GridEnvelope2D)reader.getOriginalGridRange());
		final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				oldEnvelope.getLowerCorner().getOrdinate(0)
						+ (oldEnvelope.getLength(0) / cropFactor),

				oldEnvelope.getLowerCorner().getOrdinate(1)
						+ (oldEnvelope.getLength(1) / cropFactor) },
				new double[] { oldEnvelope.getUpperCorner().getOrdinate(0),
						oldEnvelope.getUpperCorner().getOrdinate(1) });
		cropEnvelope.setCoordinateReferenceSystem(reader.getCrs());

		final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader
				.getFormat()).READ_GRIDGEOMETRY2D.createValue();
		gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0,
				(int) (range.width / 2.0 / cropFactor),
				(int) (range.height / 2.0 / cropFactor))), cropEnvelope));
		gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg });
		forceDataLoading(gc);
	}

	@Test
	public void testIsAvailable() throws NoSuchAuthorityCodeException,
			FactoryException {
		if (!testingEnabled()) {
			return;
		}
	
		GridFormatFinder.scanForPlugins();
	
		Iterator list = GridFormatFinder.getAvailableFormats().iterator();
		boolean found = false;
		GridFormatFactorySpi fac = null;
	
		while (list.hasNext()) {
			fac = (GridFormatFactorySpi) list.next();
	
			if (fac instanceof NITFFormatFactory) {
				found = true;
	
				break;
			}
		}
	
		Assert.assertTrue("NITFFormatFactory not registered", found);
		Assert.assertTrue("NITFFormatFactory not available", fac.isAvailable());
		Assert.assertNotNull(new NITFFormatFactory().createFormat());
	}
}
