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
package org.geotools.gce.geotiff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.driver.Driver.DriverOperation;
import org.geotools.coverage.io.geotiff.GeoTiffDriver;
import org.geotools.coverage.io.impl.DefaultCoverageReadRequest;
import org.geotools.test.TestData;
import org.opengis.coverage.Coverage;
import org.opengis.feature.type.Name;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.util.ProgressListener;

/**
 * Testing {@link GeoTiffReader} as well as {@link IIOMetadataDumper}.
 * 
 * @author Simone Giannecchini
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/geotiff/test/org
 *         /geotools/gce/geotiff/GeoTiffReaderTest.java $
 */
public class GeoTiffReaderTest extends TestCase {
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(GeoTiffReaderTest.class.toString());

	private static GeoTiffDriver factory;

	/**
	 * Constructor for GeoTiffReaderTest.
	 * 
	 * @param arg0
	 */
	public GeoTiffReaderTest(String name) {
		super(name);
	}
	public void testSomething(){
		
	}
	public static Test suite() {
		factory = new GeoTiffDriver();

		TestSuite suite = new TestSuite("GeoTiffReader Tests");

		// Add one entry for each test class
		// or test suite.
		suite.addTestSuite(GeoTiffReaderTest.class);

		// For a master test suite, use this pattern.
		// (Note that here, it's recursive!)
		File testData;
		try {
			testData = TestData.file(GeoTiffReaderTest.class, "");
			TestSuite fileSuite = new TestSuite( testData.getAbsolutePath());
			final File files[] = testData.listFiles();
			for (File file : files) {
				final String path = file.getAbsolutePath().toLowerCase();
				if (!path.endsWith("tif") && !path.endsWith("tiff")) {
					continue;
				}
				fileSuite.addTest(createTestReader(file));
			}
			suite.addTest(fileSuite);			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return suite;
	}

	/**
	 * testReader
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws NoSuchAuthorityCodeException
	 */
	public static Test createTestReader(final File file) {
		Test test = new Test() {				
			public int countTestCases() {
				return 1;
			}
			public String toString() {
				return file.getName();
			}
			public void run(TestResult result) {
				result.startTest(this);
				try {
					ProgressListener watch;

					StringBuffer buffer = new StringBuffer();

					buffer.append(file.getAbsolutePath()).append("\n");
					final URL source = file.toURI().toURL();
					if (factory.canProcess(DriverOperation.CONNECT, source, null)) {
						buffer.append("ACCEPTED").append("\n");

						// getting access to the file
						watch = new TestProgress();
						final CoverageAccess access = factory.process(DriverOperation.CONNECT,source,null, null, watch);
						assertEquals("Connect progress", 1.0f, watch
								.getProgress());

						if (access == null)
							throw new IOException("Could not access " + file);

						// get the names
						watch = new TestProgress();
						final List<Name> names = access.getNames(watch);
						assertEquals("names progress", 1.0f, watch
								.getProgress());

						for (Name name : names) {
							// get a source
							watch = new TestProgress();
							final CoverageSource gridSource = access.access(
									name, null, AccessType.READ_ONLY, null,
									watch);
							assertEquals("access progress", 1.0f, watch
									.getProgress());

							if (gridSource == null)
								throw new IOException("");
							// create a request
							// reading the coverage
							watch = new TestProgress();
							CoverageResponse response = gridSource.read(
									new DefaultCoverageReadRequest(), watch);
							assertEquals("read progress", 1.0f, watch
									.getProgress());

							assertNotNull("response is null", response);
							assertEquals("response success", Status.SUCCESS,
									response.getStatus());
							assertTrue("response has no errors", response
									.getExceptions().isEmpty());

							watch = new TestProgress();
							final Collection<? extends Coverage> results = response
									.getResults(watch);
							assertEquals("request results progress", 1.0f,
									watch.getProgress());

							for (Coverage c : results) {
								GridCoverage2D coverage = (GridCoverage2D) c;
								// Crs and envelope
								if (TestData.isInteractiveTest()) {
									buffer
											.append("CRS: ")
											.append(
													coverage
															.getCoordinateReferenceSystem2D()
															.toWKT()).append(
													"\n");
									buffer.append("GG: ").append(
											coverage.getGridGeometry()
													.toString()).append("\n");
								}
								// // display metadata
								// if (org.geotools.TestData.isExtensiveTest())
								// {
								// IIOMetadataDumper iIOMetadataDumper = new
								// IIOMetadataDumper(
								// ((GeoTiffReader) reader).getMetadata()
								// .getRootNode());
								// buffer.append("TIFF metadata: ").append(
								//iIOMetadataDumper.getMetadata()).append("\n");
								// }
								// showing it
								if (TestData.isInteractiveTest())
									coverage.show();
								else
									coverage.getRenderedImage().getData();

							}
						}
					} else {
						buffer.append("NOT ACCEPTED").append("\n");
					}
					if (TestData.isInteractiveTest()) {
						LOGGER.info(buffer.toString());
					}
				} catch (AssertionFailedError failure) {
					result.addFailure(this, failure);
				} catch (Exception error) {
					result.addError(this, error);
				}
				finally {
					result.endTest(this);
				}
			}
		};
		return test;
	}
}
