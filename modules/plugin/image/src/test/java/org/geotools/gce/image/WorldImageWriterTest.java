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
 *
 */
package org.geotools.gce.image;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.logging.Logger;

import junit.textui.TestRunner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.test.TestData;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * Test class for WorldImageWriter. This test tries to read, writer and re-read
 * successive images checking for errors.
 * 
 * @author Simone Giannecchini
 * @author rgould
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/image/src/test/java/org/geotools/gce/image/WorldImageWriterTest.java $
 */
public class WorldImageWriterTest extends WorldImageBaseTestCase {
	private final static String[] supportedFormat = new String[] { "tiff",
			"gif", "png", "bmp", "jpeg" };

	private Logger logger = org.geotools.util.logging.Logging.getLogger(WorldImageWriterTest.class
			.toString());

	/** The format for the image e will write. */
	private String format;


	public WorldImageWriterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		File testData = TestData.file(this, ".");
		new File(testData,"write").mkdir();
	}
	/**
	 * This method simply read all the respecting a predefined pattern inside
	 * the testData directory and then it tries to read, write and re-read them
	 * back. All the possible errors are caught.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FactoryException
	 * @throws TransformException
	 * @throws ParseException
	 * @throws ParseException
	 */
	public void testWrite() throws MalformedURLException, IOException,
			IllegalArgumentException, FactoryException, TransformException,
			ParseException {

		// checking test data directory for all kind of inputs
		final File test_data_dir = TestData.file(this, null);
		final String[] fileList = test_data_dir.list(new MyFileFilter());
		final int length = fileList.length;
		final int numSupportedFormat = supportedFormat.length;
		for (int j = 0; j < numSupportedFormat; j++) {
			format = supportedFormat[j];
			final StringBuffer buff = new StringBuffer("Format is ").append(
					format).append("\n");
			for (int i = 0; i < length; i++) {
				buff.append(" file is ").append(fileList[i]).append("\n");
				// url
				final URL url = TestData.getResource(this, fileList[i]);
				assertTrue(url != null);
				this.write(url);

				// getting file
				final File file = TestData.file(this, fileList[i]);
				assertTrue(file != null);
				// starting write test
				this.write(file);

			}
			logger.info(buff.toString());
		}

	}

	/**
	 * This method is responsible for loading the provided source object as a
	 * cverage then for writing it on the temp directoy and finally for
	 * rereading the coverage back into memory in order to display it.
	 * 
	 * @param source
	 *            Object The object on disk representing the coverage to test.
	 * 
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FactoryException
	 * @throws TransformException
	 * @throws ParseException
	 */
	private void write(Object source) throws IOException,
			IllegalArgumentException, FactoryException, TransformException,
			ParseException {
		// instantiating a reader
		WorldImageReader wiReader = new WorldImageReader(source);

		// reading the original coverage
		GridCoverage2D coverage = (GridCoverage2D) wiReader.read(null);

		assertNotNull(coverage);
		assertNotNull(coverage.getRenderedImage());
		assertNotNull(coverage.getEnvelope());

		// remember to provide a valid name, it wil be mde unique by the helper
		// function temp
		final StringBuffer buff = new StringBuffer("./write/temp").append(".").append(format);
		final File tempFile =TestData.temp(this, buff.toString());


		// getting a writer
		final WorldImageWriter wiWriter = new WorldImageWriter(tempFile);

		// writing parameters for png
		final Format writerFormat = wiWriter.getFormat();
		

		// setting write parameters
		final ParameterValueGroup params = writerFormat.getWriteParameters();
		params.parameter(WorldImageFormat.FORMAT.getName().toString())
				.setValue(format);
		final GeneralParameterValue[] gpv = { params
				.parameter(WorldImageFormat.FORMAT.getName().toString()) };
		// writing
		wiWriter.write(coverage, gpv);
        wiWriter.dispose();

		// reading again
		assertTrue(tempFile.exists());
		wiReader = new WorldImageReader(tempFile);
		coverage = (GridCoverage2D) wiReader.read(null);

		// displaying the coverage
		if (TestData.isInteractiveTest())
			coverage.show();
		else
			coverage.getRenderedImage().getData();
		wiReader.dispose();
        coverage.dispose(true);
	}

    

	/**
	 * TestRunner for testing inside a java application. It gives us the ability
	 * to keep windows open to inspect what happened.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TestRunner.run(WorldImageWriterTest.class);
	}
}
