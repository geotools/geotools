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
package org.geotools.gce.arcgrid;

import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Test reading and writing arcgrid grid coverages.
 * 
 * @author Daniele Romagnoli
 * @author Simone Giannecchini (simboss)
 *
 * @source $URL$
 */
@SuppressWarnings("deprecation")
public class ArcGridReadWriteTest extends ArcGridBaseTestCase {
	private final Random generator = new Random();

	/**
	 * Creates a new instance of ArcGridReadWriteTest
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public ArcGridReadWriteTest(String name) {
		super(name);
	}

	/**
	 * 
	 * @param testParam
	 * @throws Exception
	 */
	public void runMe(final File testFile) throws Exception {
		

		// create a temporary output file
		// temporary file to use
		File tmpFile = TestData.temp(this, Long.toString(Math
				.round(100000 * generator.nextDouble()))
				+ testFile.getName());
		tmpFile.deleteOnExit();

		// ESRI
		LOGGER.info(testFile.getName() + " ESRI");
		writeEsriUnCompressed(testFile, tmpFile);

		tmpFile = TestData.temp(this, Long.toString(Math
				.round(100000 * generator.nextDouble()))
				+ testFile.getName());
		tmpFile.deleteOnExit();
		// GRASS
		LOGGER.info(testFile.getName() + " GRASS");
		writeGrassUnCompressed(testFile, tmpFile);

	}

	/**
	 * Compares 2 grid covareages, throws an exception if they are not the same.
	 * 
	 * @param gc1
	 *            First Grid Coverage
	 * @param gc2
	 *            Second Grid Coverage
	 * 
	 * @throws Exception
	 *             If Coverages are not equal
	 */
	static void compare(GridCoverage2D gc1, GridCoverage2D gc2) throws Exception {
		final GeneralEnvelope e1 = (GeneralEnvelope) gc1.getEnvelope();
		final GeneralEnvelope e2 = (GeneralEnvelope) gc2.getEnvelope();

		/** Checking Envelopes */
		if ((e1.getLowerCorner().getOrdinate(0) != e2.getLowerCorner()
				.getOrdinate(0))
				|| (e1.getLowerCorner().getOrdinate(1) != e2.getLowerCorner()
						.getOrdinate(1))
				|| (e1.getUpperCorner().getOrdinate(0) != e2.getUpperCorner()
						.getOrdinate(0))
				|| (e1.getUpperCorner().getOrdinate(1) != e2.getUpperCorner()
						.getOrdinate(1))) {
			throw new Exception("GridCoverage Envelopes are not equal"
					+ e1.toString() + e2.toString());
		}

		/** Checking CRS */
		if (e1.getCoordinateReferenceSystem().toWKT().compareToIgnoreCase(
				e2.getCoordinateReferenceSystem().toWKT()) != 0) {
			throw new Exception("GridCoverage CRS are not equal"
					+ e1.getCoordinateReferenceSystem().toWKT()
					+ e2.getCoordinateReferenceSystem().toWKT());
		}

		/** Checking values */
		final Double noData1 = new Double(ArcGridWriter.getCandidateNoData(gc1));
		final Double noData2 = new Double(ArcGridWriter.getCandidateNoData(gc2));
		final int minTileX1 = gc1.getRenderedImage().getMinTileX();
		final int minTileY1 = gc1.getRenderedImage().getMinTileY();
		final int width = gc1.getRenderedImage().getTileWidth();
		final int height = gc1.getRenderedImage().getTileHeight();
		final int maxTileX1 = minTileX1 + gc1.getRenderedImage().getNumXTiles();
		final int maxTileY1 = minTileY1 + gc1.getRenderedImage().getNumYTiles();
		double value1 = 0, value2 = 0;

		for (int tileIndexX = minTileX1; tileIndexX < maxTileX1; tileIndexX++)
			for (int tileIndexY = minTileY1; tileIndexY < maxTileY1; tileIndexY++) {

				final Raster r1 = gc1.getRenderedImage().getTile(tileIndexX,
						tileIndexY);
				final Raster r2 = gc2.getRenderedImage().getTile(tileIndexX,
						tileIndexY);

				for (int i = r1.getMinX(); i < width; i++) {
					for (int j = r1.getMinY(); j < height; j++) {
						value1 = r1.getSampleDouble(i, j, 0);
						value2 = r2.getSampleDouble(i, j, 0);

						if (!(noData1.compareTo(value1) == 0 && noData2.compareTo(value2) == 0)
								&& (value1 != value2)) {
							throw new Exception(
									"GridCoverage Values are not equal: "
											+ value1 + ", " + value2);
						}

					}
				}
			}

	}

	/**
	 * A Simple Test Method which read an arcGrid and write it as a GRASS Ascii
	 * Grid
	 * 
	 * @param wf
	 * @param rf
	 */
	public void writeGrassUnCompressed(File rf, File wf) throws Exception {

            final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,DefaultGeographicCRS.WGS84);
		/** Step 1: Reading the coverage */
		GridCoverageReader reader = new ArcGridReader(rf,hints);
		final GridCoverage2D gc1 = (GridCoverage2D) reader.read(null);
		assertTrue(CoverageUtilities.hasRenderingCategories(gc1));

		/** Step 2: Write grid coverage out to temp file */
		final GridCoverageWriter writer = new ArcGridWriter(wf);

		// setting write parameters
		ParameterValueGroup params;
		params = writer.getFormat().getWriteParameters();
		params.parameter("GRASS").setValue(true);
		final ArcGridWriteParams wp = new ArcGridWriteParams();
		wp.setSourceBands(new int[] { 0 });
		params.parameter(
				AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
				.setValue(wp);
		// params.parameter("compressed").setValue(false);
		GeneralParameterValue[] gpv = {
				params.parameter("GRASS"),
				params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS
						.getName().toString()) };
		writer.write(gc1, gpv);
		writer.dispose();

		/** Step 3: Read the just written coverage */
		GridCoverageReader reader2 = new ArcGridReader(wf,hints);
		final GridCoverage2D gc2 = (GridCoverage2D) reader2.read(null);

		/** Step 4: Check if the 2 coverage are equals */
		compare(gc1, gc2);

		/** Step 5: Show the new coverage */
		if (TestData.isInteractiveTest()) {
			gc1.show();
			gc2.show();
		} else {
			gc1.getRenderedImage().getData();
			gc2.getRenderedImage().getData();
		}
	}

	/**
	 * A Simple Test Method which read an arcGrid and write it as an ArcGrid
	 * 
	 * @param rf
	 * @param wf
	 */
	public void writeEsriUnCompressed(File rf, File wf) throws Exception {

            final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,DefaultGeographicCRS.WGS84);
		/** Step 1: Reading the coverage */
		GridCoverageReader reader = new ArcGridReader(rf,hints);
		final GridCoverage2D gc1 = (GridCoverage2D) reader.read(null);
		assertTrue(CoverageUtilities.hasRenderingCategories(gc1));

		/** Step 2: Write grid coverage out to temp file */
		final GridCoverageWriter writer = new ArcGridWriter(wf);

		// setting write parameters
		ParameterValueGroup params;
		params = writer.getFormat().getWriteParameters();
		params.parameter("GRASS").setValue(false);
		final ArcGridWriteParams wp = new ArcGridWriteParams();
		wp.setSourceBands(new int[] { 0 });
		params.parameter(
				AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
				.setValue(wp);
		// params.parameter("compressed").setValue(false);
		GeneralParameterValue[] gpv = {
				params.parameter("GRASS"),
				params.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS
						.getName().toString()) };
		writer.write(gc1, gpv);
		writer.dispose();

		/** Step 3: Read the just written coverage */
		GridCoverageReader reader2 = new ArcGridReader(wf,hints);
		final GridCoverage2D gc2 = (GridCoverage2D) reader2.read(null);

		/** Step 4: Check if the 2 coverage are equals */
		compare(gc1, gc2);

		/** Step 5: Show the new coverage */
		if (TestData.isInteractiveTest()) {
			gc1.show();
			gc2.show();
		} else {
			gc1.getRenderedImage().getData();
			gc2.getRenderedImage().getData();
		}

	}

	public void testErrorConditions() throws IOException {

		// testing format and reader
		LOGGER.info("testErrorConditions");
		final File testFile = TestData.file(this, "arcgrid/precip30min.asc");
		final URL testURL = TestData.url(this, "arcgrid/precip30min.asc");
		final ArcGridFormat af = new ArcGridFormat();
		assertTrue("Unable to get an a reader for a file", af.accepts(testFile));
		assertTrue("Unable to get an a reader for a URL", af.accepts(testURL));

		assertNotNull("Unable to get an a reader for a file", af
				.getReader(testFile));
		assertNotNull("Unable to get an a reader for a URL", af
				.getReader(testURL));
		assertFalse("We should not get a reader for a non existing file", af
				.accepts(TestData.temp(this, "temp")));
		assertTrue(
				"Write params of incorrect type",
				af.getDefaultImageIOWriteParameters() instanceof ArcGridWriteParams);

		boolean caught = false;
		try {
			af.getReader(TestData
					.openStream(this, "arcgrid/precip30min.asc"));
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue("Streams are  supported now", !caught);

		// testing writer
		assertNotNull("Unable to get a writer for a file", af
				.getWriter(TestData.temp(this, "temp")));
		assertNotNull("Unable to get a writer for a url", af.getWriter(TestData
				.temp(this, "temp").toURI().toURL()));
		assertNotNull("We should be ablet to write on an http link", af
				.getWriter(new URL("http://www.geo-solutions.it")));

	}

	public static final void main(String[] args) throws Exception {
		junit.textui.TestRunner.run(ArcGridReadWriteTest.class);
	}

}
