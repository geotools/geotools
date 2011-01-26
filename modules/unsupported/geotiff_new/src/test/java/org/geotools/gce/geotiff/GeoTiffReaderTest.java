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
import java.io.IOException;
import java.util.logging.Logger;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.factory.Hints;
import org.geotools.test.TestData;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Testing {@link GeoTiffReader} as well as {@link IIOMetadataDumper}.
 * 
 * @author Simone Giannecchini
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/geotiff/test/org/geotools/gce/geotiff/GeoTiffReaderTest.java $
 */
public class GeoTiffReaderTest extends TestCase {
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(GeoTiffReaderTest.class.toString());

	/**
	 * Constructor for GeoTiffReaderTest.
	 * 
	 * @param arg0
	 */
	public GeoTiffReaderTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		TestRunner.run(GeoTiffReaderTest.class);

	}

	/**
	 * testReader
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws NoSuchAuthorityCodeException
	 */
	public void testReader() throws IllegalArgumentException, IOException,
			NoSuchAuthorityCodeException {

		final File file = TestData.file(GeoTiffReaderTest.class, "");
		final File files[] = file.listFiles();
		final int numFiles = files.length;
		final AbstractGridFormat format = new GeoTiffFormat();
		for (int i = 0; i < numFiles; i++) {
			StringBuffer buffer = new StringBuffer();
			final String path = files[i].getAbsolutePath().toLowerCase();
			if (!path.endsWith("tif") && !path.endsWith("tiff"))
				continue;

			buffer.append(files[i].getAbsolutePath()).append("\n");
			Object o;
			if (i % 2 == 0)
				// testing file
				o = files[i];
			else
				// testing url
				o = files[i].toURI().toURL();
			if (format.accepts(o)) {
				buffer.append("ACCEPTED").append("\n");

				// getting a reader
				GeoTiffReader reader = new GeoTiffReader(o, new Hints(
						Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));

				if (reader != null) {

					// reading the coverage
					GridCoverage2D coverage = (GridCoverage2D) reader
							.read(null);

					// Crs and envelope
					if (TestData.isInteractiveTest()) {
						buffer.append("CRS: ").append(
								coverage.getCoordinateReferenceSystem2D()
										.toWKT()).append("\n");
						buffer.append("GG: ").append(
								coverage.getGridGeometry().toString()).append("\n");
					}
					// display metadata
					if (org.geotools.TestData.isExtensiveTest()) {
						IIOMetadataDumper iIOMetadataDumper = new IIOMetadataDumper(
								((GeoTiffReader) reader).getMetadata()
										.getRootNode());
						buffer.append("TIFF metadata: ").append(
								iIOMetadataDumper.getMetadata()).append("\n");
					}
					// showing it
					if (TestData.isInteractiveTest())
						coverage.show();
					else
						coverage.getRenderedImage().getData();

				}

			} else
				buffer.append("NOT ACCEPTED").append("\n");
			if (TestData.isInteractiveTest())
				LOGGER.info(buffer.toString());

		}

	}
}
