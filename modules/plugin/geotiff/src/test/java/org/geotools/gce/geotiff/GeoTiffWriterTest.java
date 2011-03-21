/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import it.geosolutions.imageio.plugins.tiff.BaselineTIFFTagSet;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import junit.framework.Assert;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder.TagSet;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @author Simone Giannecchini
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/geotiff/test/org/geotools/gce/geotiff/GeoTiffVisualizationTest.java $
 */
@SuppressWarnings("deprecation")
public class GeoTiffWriterTest extends Assert {
	private static final Logger logger = org.geotools.util.logging.Logging.getLogger(GeoTiffWriterTest.class.toString());



	/**
	 * Testing {@link GeoTiffWriter} capabilities to write a cropped coverage.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @throws ParseException
	 * @throws FactoryException
	 * @throws TransformException
	 */
	@Test
	public void testWriteCroppedCoverage() throws IllegalArgumentException,
			IOException, UnsupportedOperationException, ParseException,
			FactoryException, TransformException {


		// /////////////////////////////////////////////////////////////////////
		//
		// Look for the original coverage that we want to crop.
		//
		// /////////////////////////////////////////////////////////////////////
		final File readdir = TestData.file(GeoTiffWriterTest.class, "");
		final File writedir = new File(new StringBuilder(readdir.getAbsolutePath()).append("/testWriter/").toString());
		writedir.mkdir();
		final File tiff = new File(readdir, "latlon.tiff");
		assert tiff.exists() && tiff.canRead() && tiff.isFile();
		if (TestData.isInteractiveTest())
			logger.info(tiff.getAbsolutePath());

		// /////////////////////////////////////////////////////////////////////
		//
		// Create format and reader
		//
		// /////////////////////////////////////////////////////////////////////
		final GeoTiffFormat format = new GeoTiffFormat();
		// getting a reader
		GridCoverageReader reader = format.getReader(tiff);
		assertNotNull(reader);

		// /////////////////////////////////////////////////////////////////////
		//
		// Play with metadata
		//
		// /////////////////////////////////////////////////////////////////////
		IIOMetadataDumper metadataDumper = new IIOMetadataDumper(((GeoTiffReader) reader).getMetadata().getRootNode());
		if (TestData.isInteractiveTest()) {
			logger.info(metadataDumper.getMetadata());
		} else
			metadataDumper.getMetadata();

		// /////////////////////////////////////////////////////////////////////
		//
		// Read the original coverage.
		//
		// /////////////////////////////////////////////////////////////////////
		GridCoverage2D gc = (GridCoverage2D) reader.read(null);
		if (TestData.isInteractiveTest()) {
			logger.info(new StringBuilder("Coverage before: ").append("\n")
					.append(gc.getCoordinateReferenceSystem().toWKT()).append(
							gc.getEnvelope().toString()).toString());
		}
		final CoordinateReferenceSystem sourceCRS = gc.getCoordinateReferenceSystem2D();
		final GeneralEnvelope sourceEnvelope = (GeneralEnvelope) gc.getEnvelope();
		final GridGeometry2D sourcedGG = (GridGeometry2D) gc.getGridGeometry();
		final MathTransform sourceG2W = sourcedGG.getGridToCRS(PixelInCell.CELL_CENTER);

		// /////////////////////////////////////////////////////////////////////
		//
		//
		// CROP
		//
		//
		// /////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////
		//
		// Crop the original coverage.
		//
		// /////////////////////////////////////////////////////////////////////
		double xc = sourceEnvelope.getMedian(0);
		double yc = sourceEnvelope.getMedian(1);
		double xl = sourceEnvelope.getSpan(0);
		double yl = sourceEnvelope.getSpan(1);
		final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
				xc - xl / 4.0, yc - yl / 4.0 }, new double[] { xc + xl / 4.0,
				yc + yl / 4.0 });
		final CoverageProcessor processor = CoverageProcessor.getInstance();
		final ParameterValueGroup param = processor
				.getOperation("CoverageCrop").getParameters();
		param.parameter("Source").setValue(gc);
		param.parameter("Envelope").setValue(cropEnvelope);
		final GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);

		// /////////////////////////////////////////////////////////////////////
		//
		// Check that we got everything correctly after the crop.
		//
		// /////////////////////////////////////////////////////////////////////
		// checking the ranges of the output image.
		final GridGeometry2D croppedGG = (GridGeometry2D) cropped.getGridGeometry();
		final GridEnvelope croppedGR = croppedGG.getGridRange();
		final MathTransform croppedG2W = croppedGG.getGridToCRS(PixelInCell.CELL_CENTER);
		final GeneralEnvelope croppedEnvelope = (GeneralEnvelope) cropped.getEnvelope();
		assertTrue("min x do not match after crop", 29 == croppedGR.getLow(0));
		assertTrue("min y do not match after crop", 30 == croppedGR.getLow(1));
		assertTrue("max x do not match after crop", 90 == croppedGR.getHigh(0)+1);
		assertTrue("max y do not match after crop", 91 == croppedGR.getHigh(1)+1);
		// check that the affine transform are the same thing
		assertTrue(
				"The Grdi2World tranformations of the original and the cropped covearage do not match",
				sourceG2W.equals(croppedG2W));
		// check that the envelope is correct
		final GeneralEnvelope expectedEnvelope = new GeneralEnvelope(croppedGR,PixelInCell.CELL_CENTER, croppedG2W, cropped.getCoordinateReferenceSystem2D());
		assertTrue("Expected envelope is different from the computed one",expectedEnvelope.equals(croppedEnvelope, XAffineTransform.getScale((AffineTransform) croppedG2W) / 2.0, false));

		//release things
		cropped.dispose(true);
		gc.dispose(true);
		try{
			if(reader!=null)
				reader.dispose();
		}catch (Throwable e) {
		}
		// /////////////////////////////////////////////////////////////////////
		//
		//
		// WRITING AND TESTING
		//
		//
		// /////////////////////////////////////////////////////////////////////
		final File writeFile = new File(new StringBuilder(writedir.getAbsolutePath()).append(File.separatorChar).append(cropped.getName().toString()).append(".tiff").toString());
		final GridCoverageWriter writer = format.getWriter(writeFile);
		
		try{
			writer.write(cropped, null);
		}catch (IOException e) {
		}
		finally{
			try{
				writer.dispose();
			}catch (Throwable e) {
			}
		}
		try{
			reader = new GeoTiffReader(writeFile, null);
			assertNotNull(reader);
			gc = (GridCoverage2D) reader.read(null);
			assertNotNull(gc);
			final CoordinateReferenceSystem targetCRS = gc.getCoordinateReferenceSystem2D();
			assertTrue(
					"Source and Target coordinate reference systems do not match",
					CRS.equalsIgnoreMetadata(sourceCRS, targetCRS));
			assertEquals("Read-back and Cropped envelopes do not match", cropped
					.getEnvelope(), croppedEnvelope);
	
			if (TestData.isInteractiveTest()) {
				logger.info(new StringBuilder("Coverage after: ").append("\n")
						.append(gc.getCoordinateReferenceSystem().toWKT()).append(
								gc.getEnvelope().toString()).toString());
				if (TestData.isInteractiveTest())
					gc.show();
				else
					PlanarImage.wrapRenderedImage(gc.getRenderedImage()).getTiles();
	
			}
		}finally{
			try{
				if(reader!=null)
					reader.dispose();
			}catch (Throwable e) {
			}
			
			if(!TestData.isInteractiveTest())
				gc.dispose(true);
		}
	}

    @Test
    public void testWriteGoogleMercator() throws Exception {
        final String google= "PROJCS[\"WGS84 / Google Mercator\", GEOGCS[\"WGS 84\", DATUM[\"World Geodetic System 1984\", SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], AUTHORITY[\"EPSG\",\"6326\"]], PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], UNIT[\"degree\", 0.017453292519943295], AUTHORITY[\"EPSG\",\"4326\"]], PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]], PARAMETER[\"semi_major\", 6378137.0], PARAMETER[\"semi_minor\", 6378137.0], PARAMETER[\"latitude_of_origin\", 0.0], PARAMETER[\"central_meridian\", 0.0], PARAMETER[\"scale_factor\", 1.0], PARAMETER[\"false_easting\", 0.0], PARAMETER[\"false_northing\", 0.0], UNIT[\"m\", 1.0],  AUTHORITY[\"EPSG\",\"900913\"]]";
        final CoordinateReferenceSystem googleCRS= CRS.parseWKT(google);
        
        //
        // world geotiff
        //
        final File testFile = TestData.file(GeoTiffReaderTest.class, "latlon.tiff");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(testFile));
        
        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(testFile);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        assertEquals(CRS.lookupIdentifier(coverage.getCoordinateReferenceSystem(), true),
                "EPSG:4267");
        reader.dispose();
        
        // reproject
        coverage=(GridCoverage2D) Operations.DEFAULT.resample(coverage, googleCRS);
        
        
        
        // get a writer
        final File mercator = new File(TestData.file(GeoTiffReaderTest.class, "."),"wms_900913.tif");
        GeoTiffWriter writer = new GeoTiffWriter(mercator);
        
        writer.write(coverage,null );
        writer.dispose();
        
        
        // getting a reader
        reader = new GeoTiffReader(mercator);
        // reading the coverage
        GridCoverage2D coverageMercator = (GridCoverage2D) reader.read(null);
     // check coverage and crs
        assertNotNull(coverageMercator);
        assertNotNull(coverageMercator.getCoordinateReferenceSystem());
        assertTrue(CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(),googleCRS));
        assertTrue(coverage.getEnvelope2D().getFrame().equals(coverageMercator.getEnvelope2D().getFrame()));
        reader.dispose();
        coverage.dispose(true);
        coverage.dispose(true);
    }
    
   
	@Test
	public void testWriteTFW() throws Exception{

	        //
	        // no crs geotiff
	        //
	        final File noCrs = TestData.file(GeoTiffReaderTest.class, "no_crs.tif");
	        final AbstractGridFormat format = new GeoTiffFormat();
	        assertTrue(format.accepts(noCrs));

	        // hint for CRS
	        final CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
	        final Hints hint = new Hints();
	        hint.put(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs);

	        // getting a reader
	        GeoTiffReader reader = new GeoTiffReader(noCrs, hint);

	        // reading the coverage
	        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

	        // check coverage and crs
	        assertNotNull(coverage);
	        assertNotNull(coverage.getCoordinateReferenceSystem());
	        assertEquals(CRS.lookupIdentifier(coverage.getCoordinateReferenceSystem(), true),
	                "EPSG:32632");
	        reader.dispose();
	        
	        
	        // get a writer
	        final File noCrsTFW = new File(TestData.file(GeoTiffReaderTest.class, "."),"no_crs_tfw.tif");
	        GeoTiffWriter writer = new GeoTiffWriter(noCrsTFW);
	        
	        final ParameterValue<Boolean> tfw = GeoTiffFormat.WRITE_TFW.createValue();
	        tfw.setValue(true);
	        writer.write(coverage,new GeneralParameterValue[]{tfw} );
	        writer.dispose();
	        coverage.dispose(true);
	        
	        final File finalTFW=new File(noCrsTFW.getParent(),noCrsTFW.getName().replace("tif", "tfw"));
	        assertTrue(finalTFW.canRead());
	        

	}
	
    @Test
    public void testWriteWithMetadata() throws Exception {

        //
        // no crs geotiff
        //
        final File input = TestData.file(GeoTiffReaderTest.class, "geo.tiff");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(input));

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(input);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        reader.dispose();

        // get a writer
        final File output = new File(TestData.file(GeoTiffReaderTest.class, "."), "outMetadata.tif");
        GeoTiffWriter writer = new GeoTiffWriter(output);
        
        // Setting a COPYRIGHT metadata
        String copyrightInfo = "(C) GEOTOOLS sample writer";
        String software = "GeoTools Coverage Writer test";
        
        writer.setMetadataValue(Integer.toString(BaselineTIFFTagSet.TAG_COPYRIGHT), copyrightInfo);
        writer.setMetadataValue(TagSet.BASELINE + ":" + Integer.toString(BaselineTIFFTagSet.TAG_SOFTWARE), software);
        
        writer.write(coverage,null);
        writer.dispose();
        coverage.dispose(true);
        
        // getting a reader
        reader = new GeoTiffReader(output);

        GeoTiffIIOMetadataDecoder metadata = reader.getMetadata();
        String readSoftware = metadata.getAsciiTIFFTag(Integer.toString(BaselineTIFFTagSet.TAG_SOFTWARE));
        assertTrue(software.equalsIgnoreCase(readSoftware));
        String readCopyright = metadata.getAsciiTIFFTag(Integer.toString(BaselineTIFFTagSet.TAG_COPYRIGHT));
        assertTrue(copyrightInfo.equalsIgnoreCase(readCopyright));
        
        reader.dispose();

        
    }
}
