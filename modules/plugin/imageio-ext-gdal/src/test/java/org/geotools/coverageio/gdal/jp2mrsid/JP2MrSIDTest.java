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
package org.geotools.coverageio.gdal.jp2mrsid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import javax.media.jai.PlanarImage;

import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverageio.gdal.GDALTestCase;
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
 * Testing {@link JP2MrSIDReader}
 *
 *
 * @source $URL$
 */
public final class JP2MrSIDTest extends GDALTestCase {
    /**
     * file name of a valid JP2K sample data to be used for tests.
     */
    private final static String fileName = "sample.jp2";

    static {
	    try {
	        gdal.AllRegister();
	        final Driver driverkak = gdal.GetDriverByName("JP2KAK");
	        final Driver driverecw = gdal.GetDriverByName("JP2ECW");
	        if (driverkak != null || driverecw != null) {
	            final StringBuffer skipDriver = new StringBuffer("");
	            if (driverkak != null)
	                skipDriver.append("JP2KAK ");
	            if (driverecw != null)
	                skipDriver.append("JP2ECW");
	            gdal.SetConfigOption("GDAL_SKIP", skipDriver.toString());
	            gdal.AllRegister();
	        }
	    } catch (UnsatisfiedLinkError e) {
	        if (LOGGER.isLoggable(Level.WARNING))
	            LOGGER.warning("GDAL library unavailable.");
	    }
	}

	/**
     * Creates a new instance of JP2MrSIDTest
     *
     * @param name
     */
    public JP2MrSIDTest() {
        super("JP2MrSID", new JP2MrSIDFormatFactory());
    }

    @Test
    public void test() throws Exception {
        if (!testingEnabled()) {
            return;
        }
        
        File file =null;
        try {
            file = TestData.file(this, fileName);
        }catch (FileNotFoundException fnfe){
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        } catch (IOException ioe) {
            LOGGER.warning("test-data not found: " + fileName + "\nTests are skipped");
            return;
        }

        final JP2MrSIDReader reader = new JP2MrSIDReader(file);
        final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader.getFormat()).READ_GRIDGEOMETRY2D
            .createValue();
        final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
        gg.setValue(new GridGeometry2D(reader.getOriginalGridRange(), oldEnvelope));

        final GridCoverage2D gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg });

        Assert.assertNotNull(gc);

        if (TestData.isInteractiveTest()) {
            gc.show();
        } else {
            PlanarImage.wrapRenderedImage(gc.getRenderedImage()).getTiles();
        }

        if (TestData.isInteractiveTest()) {
            // printing CRS information
            LOGGER.info(gc.getCoordinateReferenceSystem().toWKT());
            LOGGER.info(gc.getEnvelope().toString());
        }
    }

    @Test
	public void testIsAvailable() throws NoSuchAuthorityCodeException, FactoryException {
	    if (!testingEnabled()) {
	        return;
	    }
	
	    GridFormatFinder.scanForPlugins();
	
	    Iterator list = GridFormatFinder.getAvailableFormats().iterator();
	    boolean found = false;
	    GridFormatFactorySpi fac = null;
	
	    while (list.hasNext()) {
	        fac = (GridFormatFactorySpi) list.next();
	
	        if (fac instanceof JP2MrSIDFormatFactory) {
	            found = true;
	            break;
	        }
	    }
	
	    Assert.assertTrue("JP2MrSIDFormatFactory not registered", found);
	    Assert.assertTrue("JP2MrSIDFormatFactory not available", fac.isAvailable());
	    Assert.assertNotNull(new JP2MrSIDFormatFactory().createFormat());
	}
}
