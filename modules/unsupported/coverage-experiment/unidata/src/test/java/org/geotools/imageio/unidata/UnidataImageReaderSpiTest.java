package org.geotools.imageio.unidata;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.imageio.ImageReader;

import org.geotools.imageio.unidata.reader.DummyUnidataImageReaderSpi;
import org.geotools.test.TestData;
import org.junit.Test;

public class UnidataImageReaderSpiTest {

	private final static Logger LOGGER = Logger.getLogger(UnidataImageReaderSpiTest.class.toString());
	@Test
	public void testReadRegularNetCDF() throws IOException {
		DummyUnidataImageReaderSpi readerSpi = new DummyUnidataImageReaderSpi();
		 File file=null;
	        try{
	            file = TestData.file(this, "2DLatLonCoverage.nc");
	        } catch (IOException e) {
	            LOGGER.warning("Unable to find file 2DLatLonCoverage.nc");
	            return;
	        }
		assertTrue(readerSpi.canDecodeInput(file));
	}

	@Test
	public void testReadNcML() throws IOException {
		DummyUnidataImageReaderSpi readerSpi = new DummyUnidataImageReaderSpi();
		 File file=null;
	        try{
	            file = TestData.file(this, "2DLatLonCoverage.ncml");
	        } catch (IOException e) {
	            LOGGER.warning("Unable to find file 2DLatLonCoverage.ncml");
	            return;
	        }
		assertTrue(readerSpi.canDecodeInput(file));
	}

}
