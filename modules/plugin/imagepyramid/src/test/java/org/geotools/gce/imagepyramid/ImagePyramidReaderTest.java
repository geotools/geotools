/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagepyramid;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Testing {@link ImagePyramidReader}.
 * 
 * @author Simone Giannecchini
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Test coverage for pyramids stored in JARs and referenced by URLs
 * @since 2.3
 * 
 *
 *
 * @source $URL$
 */
public class ImagePyramidReaderTest extends Assert {

	/**
	 * File to be used for testing purposes.
	 */
	private final static String TEST_FILE = "pyramid.properties";
	private final static String TEST_JAR_FILE = "pyramid.jar";
	
	  
    /**
     * Tests automatic building of all the mosaic and pyramid files
     * 
     * @throws IOException
     */
    @Test
    public void testAutomaticBuild() throws IOException {
        final URL testFile = TestData.getResource(this, TEST_FILE);
        File sourceDir = DataUtilities.urlToFile(testFile).getParentFile();
        File targetDir = File.createTempFile("pyramid", "tst", new File("./target"));
        targetDir.delete();
        targetDir.mkdir();
        try {
            prepareEmptyMosaic(sourceDir, targetDir);

            // now make sure we can actually rebuild the mosaic
            final AbstractGridFormat format = new ImagePyramidFormat();
            final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
            assertTrue(((ImagePyramidFormat)format).accepts(targetDir, hints));
            final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(targetDir, hints);
            assertNotNull(reader);
        } finally {
            // cleanup
            FileUtils.deleteQuietly(targetDir);
        }
    }

    /**
     * Tests automatic building of all the mosaic and pyramid files from a gdal_retile like
     * directory structure
     * 
     * @throws IOException
     */
    @Test
    public void testAutomaticBuildGdalRetile() throws IOException {
        final URL testFile = TestData.getResource(this, TEST_FILE);
        File sourceDir = DataUtilities.urlToFile(testFile).getParentFile();
        File targetDir = File.createTempFile("pyramid", "tst", new File("./target"));
        targetDir.delete();
        targetDir.mkdir();
        try {
            prepareEmptyMosaic(sourceDir, targetDir);

            // move the files so that it looks like a gdal_retile created directory
            File zeroDir = new File(targetDir, "0");
            assertTrue(zeroDir.isDirectory());
            FileUtils.copyDirectory(zeroDir, targetDir);
            FileUtils.deleteDirectory(zeroDir);

            // now make sure we can actually rebuild the mosaic
            final AbstractGridFormat format = new ImagePyramidFormat();
            final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
            assertTrue(((ImagePyramidFormat)format).accepts(targetDir, hints));
            final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(targetDir, hints);
            assertNotNull(reader);
        } finally {
            // cleanup
            FileUtils.deleteQuietly(targetDir);
        }
    }

   /**
    * Copies the mosaic from the source dir to the target dir and removes all
    * metadata files from it
    * @param sourceDir
    * @param targetDir
    * @throws IOException
    */
    void prepareEmptyMosaic(File sourceDir, File targetDir) throws IOException {
        FileUtils.copyDirectory(sourceDir, targetDir);
        
        // remove the files we want to recreate
        File[] dirs = new File[] {targetDir, new File(targetDir, "0"),
                new File(targetDir, "2"), new File(targetDir, "4"), new File(targetDir, "8")};
        FileFilter metadataFilter = FileFilterUtils.prefixFileFilter("pyramid.");
        for (File dir : dirs) {
            for (File file : dir.listFiles(metadataFilter)) {
                file.delete();
            }
        }
    }
	


	@Test
	public void testDefaultParameterValue() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testFile = TestData.getResource(this, TEST_FILE);//
		assertNotNull(testFile);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		assertTrue(format.accepts(testFile));
		final ImagePyramidReader reader = (ImagePyramidReader) format
				.getReader(testFile);
		assertNotNull(reader);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
		assertEquals("pyramid", coverage.getName().toString());
		assertNotNull("Null value returned instead of a coverage", coverage);
		assertTrue("coverage dimensions different from what we expected",
				coverage.getGridGeometry().getGridRange().getSpan(0) == 250
						&& coverage.getGridGeometry().getGridRange().getSpan(
								1) == 250);
		if (TestData.isInteractiveTest())
			coverage.show("testDefaultParameterValue");
		else
			PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage()).getTiles();

	}
	@Test
	public void testDefaultParameterValueFile() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final File testFile = TestData.file(this, TEST_FILE);//
		assertNotNull(testFile);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		assertTrue(format.accepts(testFile));
		final ImagePyramidReader reader = (ImagePyramidReader) format
				.getReader(testFile);
		assertNotNull(reader);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
		assertNotNull("Null value returned instead of a coverage", coverage);
		assertTrue("coverage dimensions different from what we expected",
				coverage.getGridGeometry().getGridRange().getSpan(0) == 250
						&& coverage.getGridGeometry().getGridRange().getSpan(
								1) == 250);
		if (TestData.isInteractiveTest())
			coverage.show("testDefaultParameterValueFile");
		else
			((GridCoverage2D) coverage).getRenderedImage().getData();

	}
	@Test
	public void testDefaultParameterValueString() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final String testFile = TestData.file(this, TEST_FILE)
				.getCanonicalPath();//
		assertNotNull(testFile);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		assertTrue(format.accepts(testFile));
		final ImagePyramidReader reader = (ImagePyramidReader) format
				.getReader(testFile);
		assertNotNull(reader);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
		assertNotNull("Null value returned instead of a coverage", coverage);
		assertTrue("coverage dimensions different from what we expected",
				coverage.getGridGeometry().getGridRange().getSpan(0) == 250
						&& coverage.getGridGeometry().getGridRange().getSpan(
								1) == 250);
		if (TestData.isInteractiveTest())
			coverage.show("testDefaultParameterValueString");
		else
			((GridCoverage2D) coverage).getRenderedImage().getData();

	}
	@Test
	public void testForErrors() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final File testFile = TestData.file(this, TEST_FILE);//
		assertNotNull(testFile);

		// /////////////////////////////////////////////////////////////////
		//
		// Null argument
		//
		//
		// /////////////////////////////////////////////////////////////////
		ImagePyramidReader reader = null;
		try {
			reader = new ImagePyramidReader(null, new Hints(
					Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
		} catch (DataSourceException e) {

		}
		assertNull(reader);

		// /////////////////////////////////////////////////////////////////
		//
		// Illegal arguments
		//
		//
		// /////////////////////////////////////////////////////////////////
		try {
			reader = new ImagePyramidReader(new FileInputStream(testFile),
					new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,
							Boolean.FALSE));
		} catch (IllegalArgumentException e) {

		}
		assertNull(reader);
		try {
			reader = new ImagePyramidReader(ImageIO
					.createImageInputStream(testFile), new Hints(
					Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
		} catch (IllegalArgumentException e) {

		}
		assertNull(reader);

		// /////////////////////////////////////////////////////////////////
		//
		// Unsopported operations
		//
		//
		// /////////////////////////////////////////////////////////////////
		reader = new ImagePyramidReader(testFile, new Hints(
				Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));

		try {
			reader.getCurrentSubname();

		} catch (UnsupportedOperationException e) {
			try {
				String[] names = reader.getMetadataNames();
				assertNull(names);

			} catch (UnsupportedOperationException e1) {
				
			}
			
			try {
				String value = reader.getMetadataValue("");
				assertNull(value);
				return;
			} catch (UnsupportedOperationException e2) {
				return;
			}
		}
		assertTrue("Some of the unsopported method did not send an exception",false);

	}
	
	@Test
	public void testComplete() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testFile = TestData.getResource(this, TEST_FILE);
		assertNotNull(testFile);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final ImagePyramidReader reader = new ImagePyramidReader(
				testFile,
				new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
		assertNotNull(reader);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// alpha on output
		//
		//
		// /////////////////////////////////////////////////////////////////
		final ParameterValue<Color> transp = ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
		transp.setValue(Color.black);

		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		GridCoverage2D coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[] {  transp });
		assertNotNull(coverage);
		assertTrue("coverage dimensions different from what we expected",coverage.getGridGeometry().getGridRange().getSpan(0) == 250&& coverage.getGridGeometry().getGridRange().getSpan(1) == 250);
		if (TestData.isInteractiveTest())
			coverage.show("testComplete");
		else
			PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage()).getTiles();

	}
	
	/**
	 * This is related to http://jira.codehaus.org/browse/GEOS-4081 and happens only if the requested
	 * envelope is overlapping with the pyramid envelope for way less than a pixel
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
    @Test
    public void testRequestOutsideBounds() throws IOException, MismatchedDimensionException,
            NoSuchAuthorityCodeException {
        // grab the reader
        final URL testFile = TestData.getResource(this, TEST_FILE);
        assertNotNull(testFile);
        final ImagePyramidReader reader = new ImagePyramidReader(testFile, new Hints(
                Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        assertNotNull(reader);
        
        // prepare a request that crosses the bounds for a really minimal part
        GeneralEnvelope ge = reader.getOriginalEnvelope();
        ReferencedEnvelope requestedEnvelope = new ReferencedEnvelope(ge.getMinimum(0) - 5, ge.getMinimum(0), 
                ge.getMinimum(1), ge.getMaximum(1), ge.getCoordinateReferenceSystem());
        final Parameter<GridGeometry2D> readGG = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D);
        readGG.setValue(new GridGeometry2D(new GridEnvelope2D(0, 0, 400, 400), requestedEnvelope));
        
        // make sure we get back a null, not an exception
        assertNull(reader.read(new GeneralParameterValue[] {readGG}));
    }

	/**
	 * Testing {@link ImagePyramidReader} by cropping requesting a the best
	 * possible dimension.
	 * 
	 * <p>
	 * The underlying pyramid i made by 4 levels on the same area, more or less
	 * italy, with resolution decreasing as a power of 2.
	 * 
	 * <p>
	 * Size of the original mosaic is 250,250.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	public void testCropHighestLevel() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {

		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testFile = TestData.getResource(this, TEST_FILE);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
		assertNotNull(reader);

		// /////////////////////////////////////////////////////////////////
		//
		// crop
		//
		//
		// /////////////////////////////////////////////////////////////////
		final ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0),
				oldEnvelop.getLowerCorner().getOrdinate(1) }, new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0)
						+ oldEnvelop.getSpan(0) / 2,
				oldEnvelop.getLowerCorner().getOrdinate(1)
						+ oldEnvelop.getSpan(1) / 2 });
		cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
		gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0,
				250, 250)), cropEnvelope));

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		GridCoverage2D coverage = ((GridCoverage2D) reader
				.read(new GeneralParameterValue[] { gg }));
		assertNotNull("Null value returned instead of a coverage", coverage);
		assertTrue("coverage dimensions different from what we expected",
				coverage.getGridGeometry().getGridRange().getSpan(0) == 254
						&& coverage.getGridGeometry().getGridRange().getSpan(
								1) == 254);
		if (TestData.isInteractiveTest())
			coverage.show("testCropHighestLevel");
		else
			PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();

	}

	/**
	 * Testing {@link ImagePyramidReader} by cropping requesting a the second
	 * better avialble resolution.
	 * 
	 * <p>
	 * The underlying pyramid i made by 4 levels on the same area, more or less
	 * italy, with resolution decreasing as a power of 2.
	 * 
	 * <p>
	 * Size of the original mosaic is 250,250.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	public void testCropLevel1() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {

		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testFile = TestData.getResource(this, TEST_FILE);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
		assertNotNull(reader);

		// /////////////////////////////////////////////////////////////////
		//
		// crop
		//
		//
		// /////////////////////////////////////////////////////////////////
		final ParameterValue<GridGeometry2D> gg = ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0),
				oldEnvelop.getLowerCorner().getOrdinate(1) }, new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0)
						+ oldEnvelop.getSpan(0) / 2,
				oldEnvelop.getLowerCorner().getOrdinate(1)
						+ oldEnvelop.getSpan(1) / 2 });
		cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
		gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0,
				125, 125)), cropEnvelope));

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		GridCoverage2D coverage = ((GridCoverage2D) reader
				.read(new GeneralParameterValue[] { gg }));
		assertNotNull("Null value returned instead of a coverage", coverage);
		// assertTrue("coverage dimensions different from what we expected",
		// coverage.getGridGeometry().getGridRange().getSpan(0) == 63
		// && coverage.getGridGeometry().getGridRange().getSpan(
		// 1) == 62);
		if (TestData.isInteractiveTest())
			coverage.show("testCropLevel1");
		else
			coverage.getRenderedImage().getData();

	}

	/**
	 * Testing {@link ImagePyramidReader} by cropping requesting a the third
	 * better avialble resolution.
	 * 
	 * <p>
	 * The underlying pyramid i made by 4 levels on the same area, more or less
	 * italy, with resolution decreasing as a power of 2.
	 * 
	 * <p>
	 * Size of the original mosaic is 250,250.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	public void testCropLevel2() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {

		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testFile = TestData.getResource(this, TEST_FILE);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
		assertNotNull(reader);

		// /////////////////////////////////////////////////////////////////
		//
		// crop
		//
		//
		// /////////////////////////////////////////////////////////////////
		final ParameterValue<GridGeometry2D> gg =  ImageMosaicFormat.READ_GRIDGEOMETRY2D
				.createValue();
		final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0),
				oldEnvelop.getLowerCorner().getOrdinate(1) }, new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0)
						+ oldEnvelop.getSpan(0) / 2,
				oldEnvelop.getLowerCorner().getOrdinate(1)
						+ oldEnvelop.getSpan(1) / 2 });
		cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
		gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0,
				62, 62)), cropEnvelope));

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		GridCoverage2D coverage = ((GridCoverage2D) reader
				.read(new GeneralParameterValue[] { gg }));
		assertNotNull("Null value returned instead of a coverage", coverage);
		// assertTrue("coverage dimensions different from what we expected",
		// coverage.getGridGeometry().getGridRange().getSpan(0) == 31
		// && coverage.getGridGeometry().getGridRange().getSpan(
		// 1) == 31);
		if (TestData.isInteractiveTest())
			coverage.show("testCropLevel1");
		else
			coverage.getRenderedImage().getData();

	}

	/**
	 * Testing {@link ImagePyramidReader} by cropping requesting a the worst
	 * availaible resolution.
	 * 
	 * <p>
	 * The underlying pyramid i made by 4 levels on the same area, more or less
	 * italy, with resolution decreasing as a power of 2.
	 * 
	 * <p>
	 * Size of the original mosaic is 250,250.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws NoSuchAuthorityCodeException
	 */
	@Test
	public void testCropLevel3() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {

		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testFile = TestData.getResource(this, TEST_FILE);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
		assertNotNull(reader);
		
		// /////////////////////////////////////////////////////////////////
		//
		// crop
		//
		//
		// /////////////////////////////////////////////////////////////////
		final ParameterValue<GridGeometry2D> gg = ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
		final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0),
				oldEnvelop.getLowerCorner().getOrdinate(1) }, new double[] {
				oldEnvelop.getLowerCorner().getOrdinate(0)
						+ oldEnvelop.getSpan(0) / 2,
				oldEnvelop.getLowerCorner().getOrdinate(1)
						+ oldEnvelop.getSpan(1) / 2 });
		cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
		gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0,
				25, 25)), cropEnvelope));

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		GridCoverage2D coverage = ((GridCoverage2D) reader
				.read(new GeneralParameterValue[] { gg }));
		assertNotNull("Null value returned instead of a coverage", coverage);
		// assertTrue("coverage dimensions different from what we expected",
		// coverage.getGridGeometry().getGridRange().getSpan(0) == 15
		// && coverage.getGridGeometry().getGridRange().getSpan(
		// 1) == 15);
		if (TestData.isInteractiveTest())
			coverage.show("testCropLevel1");
		else
			coverage.getRenderedImage().getData();

	}
	

	/**
	 * Tests to read a pyramid from inside a JAR. The source is passed as an {@link URL}
	 */
	@Test
	@Ignore
	public void testDefaultParameterValueURLtoJAR() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testJarFile = TestData.getResource(this, TEST_JAR_FILE);
		assertNotNull(testJarFile);
		
		final String spec = "jar:"+testJarFile.toExternalForm()+"!/"+TEST_FILE;
		final URL testFile = new URL(spec); 
		
		assertNotNull(testFile);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		assertTrue(format.accepts(testFile));
		final ImagePyramidReader reader = (ImagePyramidReader) format
				.getReader(testFile);
		assertNotNull(reader);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
		assertNotNull("Null value returned instead of a coverage", coverage);
		assertTrue("coverage dimensions different from what we expected",
				coverage.getGridGeometry().getGridRange().getSpan(0) == 250
						&& coverage.getGridGeometry().getGridRange().getSpan(
								1) == 250);
		if (TestData.isInteractiveTest())
			coverage.show("testDefaultParameterValue");
		else
			((GridCoverage2D) coverage).getRenderedImage().getData();

	}
	

	/**
	 * Tests to read a pyramid from inside a JAR. The source is passed as a {@link String}
	 */
	@Test
	@Ignore
	public void testDefaultParameterValueStringtoURLtoJAR() throws IOException,
			MismatchedDimensionException, NoSuchAuthorityCodeException {
		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get the resource.
		//
		//
		// /////////////////////////////////////////////////////////////////
		final URL testJarFile = TestData.getResource(this, TEST_JAR_FILE);
		assertNotNull(testJarFile);
		
		final String spec = "jar:"+testJarFile.toExternalForm()+"!/"+TEST_FILE;
		
		assertNotNull(spec);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Get a reader
		//
		//
		// /////////////////////////////////////////////////////////////////
		final AbstractGridFormat format = new ImagePyramidFormat();
		assertTrue(format.accepts(spec));
		final ImagePyramidReader reader = (ImagePyramidReader) format
				.getReader(spec);
		assertNotNull(reader);

		//
		// /////////////////////////////////////////////////////////////////
		//
		// Show the coverage
		//
		//
		// /////////////////////////////////////////////////////////////////
		final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
		assertNotNull("Null value returned instead of a coverage", coverage);
		assertTrue("coverage dimensions different from what we expected",
				coverage.getGridGeometry().getGridRange().getSpan(0) == 250
						&& coverage.getGridGeometry().getGridRange().getSpan(
								1) == 250);
		if (TestData.isInteractiveTest())
			coverage.show("testDefaultParameterValue");
		else
			((GridCoverage2D) coverage).getRenderedImage().getData();

	}
}
