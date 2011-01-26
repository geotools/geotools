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
 */
package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageReader;
import it.geosolutions.imageio.plugins.arcgrid.spi.AsciiGridsImageReaderSpi;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.resources.image.ComponentColorModelJAI;
import org.geotools.util.NumberRange;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL$
 */
public class TestLinearClassifier extends TestCase {

	@Before
	protected void setUp() throws Exception {

		// check that it exisits
		File file = TestData.copy(this, "arcgrid/arcgrid.zip");
		assertTrue(file.exists());

		// unzip it
		TestData.unzipFile(this, "arcgrid/arcgrid.zip");

	}

	private static final int TEST_NUM = 1;

	static {
		RasterClassifier.register(JAI.getDefaultInstance());
	}

//
//	public static TestSuite suite() {
//		TestSuite suite = new TestSuite();
////		suite.addTest(new TestLinearClassifier("testSWAN"));
////		suite.addTest(new TestLinearClassifier("testSWANGAP"));
////		suite.addTest(new TestLinearClassifier("testSynthetic_Double"));
////		suite.addTest(new TestLinearClassifier("testSynthetic_Float"));
//		suite.addTest(new TestLinearClassifier("testSpearfish"));
////		suite.addTest(new TestLinearClassifier("testNoDataOnly"));
//
//		return suite;
//	}


//
//	/**
//	 * Testing Piecewise operation.
//	 * 
//	 * @throws IOException
//	 * @throws TransformException
//	 */
//	public void testPiecewise() throws IOException, TransformException {
//		// /////////////////////////////////////////////////////////////////////
//		//
//		// This test is quite standard since the NoData category specified
//		// is for NoData values since the input file is a GRASS ascii file
//		// where the missing values are represented by * which are substituted
//		// with NaN during reads. The only strange thing that we try here is
//		// that we map two different classes to the same color with the same
//		// index.
//		//
//		// /////////////////////////////////////////////////////////////////////
//		final RenderedImage image = getSpearfhisDemo();
//
//		// for (int i = 0; i < TEST_NUM; i++) {
//		final LinearColorMapElement c0 = LinearColorMapElement
//				.create("c0", Color.yellow, new NumberRange(
//						Double.NEGATIVE_INFINITY, false, 1100, true), 5);
//
//		final LinearColorMapElement c1 = LinearColorMapElement
//				.create("c2", Color.blue, new NumberRange(1100.0, false,
//						1200.0, true), 1);
//
//		final LinearColorMapElement c3 = LinearColorMapElement
//				.create("c3", Color.green, new NumberRange(1200.0, false,
//						1400.0, true), 7);
//
//		final LinearColorMapElement c4 = LinearColorMapElement
//				.create("c4", Color.blue, new NumberRange(1400.0, false, 1600,
//						true), 1);
//
//		final LinearColorMapElement c5 = LinearColorMapElement
//				.create("c4", Color.CYAN, new NumberRange(1600.0, false,
//						Double.POSITIVE_INFINITY, true), 11);
//
//		final LinearColorMapElement c6 = LinearColorMapElement
//				.create("nodata", new Color(0, 0, 0, 0), new NumberRange(
//						Double.NaN, Double.NaN), 0);
//
//		final LinearColorMap list = new LinearColorMap(
//				new LinearColorMapElement[] { c0, c1, c3, c4, c5 },
//				new LinearColorMapElement[] { c6 });
//
//		// assertFalse(10f < 10f + 3 * Float.MIN_VALUE);
//		// final int actualNumber = Float.floatToRawIntBits(10f);
//		// final int comparisonNumber = Float.floatToRawIntBits(10f)
//		// + Float.floatToRawIntBits(3 * Float.MIN_VALUE);
//		// assertTrue(-actualNumber + comparisonNumber > 0);
//
//		final ParameterBlockJAI pbj = new ParameterBlockJAI("PieceWise");
//		pbj.addSource(image);
//		final float breakp[][][] = new float[1][2][];
//		breakp[0][0] = new float[] { 0f, 10f, 10f + 3f * Float.MIN_VALUE, 20f,
//				20f + 3f * Float.MIN_VALUE, 30f, 30f + 3f * Float.MIN_VALUE,
//				40f, 256f };
//		breakp[0][1] = new float[] { 0f, 0f, 1f, 1f, 2f, 2f, 3f, 3f, 4f };
//		pbj.setParameter("breakPoints", breakp);
//		final RenderedOp d = JAI.create("PieceWise", pbj);
//		d.getTiles();
//		d.dispose();
//
//	}

	/**
	 * Synthetic with Double Sample Model!
	 * 
	 * @throws IOException
	 */
	@Test
	public void Synthetic_Double() throws IOException {

		// /////////////////////////////////////////////////////////////////////
		//
		// This test is interesting since it can be used to force the
		// creation of a sample model that uses a USHORT datatype since the
		// number of requested colors is pretty high. We are also using some
		// synthetic data where there is no NoData.
		//
		// /////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////
		//
		// Set the pixel values. Because we use only one tile with one band,
		// the
		// code below is pretty similar to the code we would have if we were
		// just setting the values in a matrix.
		//
		// /////////////////////////////////////////////////////////////////////
		final BufferedImage image = getSynthetic_Double();
		for (int i = 0; i < TEST_NUM; i++) {
			// /////////////////////////////////////////////////////////////////////
			//
			// Build the categories
			//
			// /////////////////////////////////////////////////////////////////////
			final LinearColorMapElement c0 = LinearColorMapElement
					.create("c0", Color.BLACK, NumberRange.create(
							Double.NEGATIVE_INFINITY, false, 10, true), 0);

			final LinearColorMapElement c1 = LinearColorMapElement
					.create("c2", Color.blue, NumberRange.create(10.0, false,
							100.0, true), 1);

			final LinearColorMapElement c3 = LinearColorMapElement
					.create("c3", Color.green, NumberRange.create(100.0, false,
							300.0, true), 2);

			final LinearColorMapElement c4 = LinearColorMapElement
					.create("c4", new Color[] { Color.green, Color.red },
							NumberRange.create(300.0, false, 400, true),
							NumberRange.create(3, 1000));

			final LinearColorMapElement c5 = LinearColorMapElement
					.create("c5", new Color[] { Color.red, Color.white },
							NumberRange.create(400.0, false, 1000, true),
							NumberRange.create(1001, 2000));

			final LinearColorMapElement c6 = LinearColorMapElement
					.create("c6", Color.red, 1001.0, 2001);

			final LinearColorMapElement c7 = LinearColorMapElement
					.create("nodata", new Color(0, 0, 0, 0), NumberRange.create(
							Double.NaN, Double.NaN), 2201);

			final LinearColorMap list = new LinearColorMap("",
					new LinearColorMapElement[] { c0, c1, c3, c4, c5, c6 },
					new LinearColorMapElement[] { c7 });

			final ParameterBlockJAI pbj = new ParameterBlockJAI(
					RasterClassifier.OPERATION_NAME);
			pbj.addSource(image);
			pbj.setParameter("Domain1D", list);
			final RenderedOp finalimage = JAI.create(
					RasterClassifier.OPERATION_NAME, pbj);

			if (TestData.isInteractiveTest())
				RasterSymbolizerTest.visualize(finalimage, "synthetic");
			else
				finalimage.getTiles();
			finalimage.dispose();
		}

	}

	/**
	 * Synthetic with Float Sample Model!
	 * 
	 * @return {@linkplain BufferedImage}
	 */
	private BufferedImage getSynthetic_Double() {
		final int width = 500;
		final int height = 500;
		final WritableRaster raster = RasterFactory.createBandedRaster(
				DataBuffer.TYPE_DOUBLE, width, height, 1, null);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster.setSample(x, y, 0, (x + y));
			}
		}
		final ColorModel cm = new ComponentColorModelJAI(ColorSpace
				.getInstance(ColorSpace.CS_GRAY), false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_DOUBLE);
		final BufferedImage image = new BufferedImage(cm, raster, false, null);
		return image;
	}

	/**
	 * Building a synthetic image upon a DOUBLE sample-model.
	 * 
	 * @return {@linkplain BufferedImage}
	 * @throws IOException
	 */
	@Test
	public void Synthetic_Float() throws IOException {

		// /////////////////////////////////////////////////////////////////////
		//
		// This test is interesting since it can be used to force the
		// creation of a sample model that uses a USHORT datatype since the
		// number of requested colors is pretty high. We are also using some
		// synthetic data where there is no NoData.
		//
		// /////////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////
		//
		// Set the pixel values. Because we use only one tile with one band,
		// the
		// code below is pretty similar to the code we would have if we were
		// just setting the values in a matrix.
		//
		// /////////////////////////////////////////////////////////////////////
		final BufferedImage image = getSynthetic_Float();
		for (int i = 0; i < TEST_NUM; i++) {
			// /////////////////////////////////////////////////////////////////////
			//
			// Build the categories
			//
			// /////////////////////////////////////////////////////////////////////
			final LinearColorMapElement c0 = LinearColorMapElement
					.create("c0", Color.BLACK, NumberRange.create(
							Double.NEGATIVE_INFINITY, false, 10, true), 0);

			final LinearColorMapElement c1 = LinearColorMapElement
					.create("c2", Color.blue, NumberRange.create(10.0f, false,
							100.0f, true), 1);

			final LinearColorMapElement c3 = LinearColorMapElement
					.create("c3", Color.green, NumberRange.create(100.0f, false,
							300.0f, true), 2);

			final LinearColorMapElement c4 = LinearColorMapElement
					.create("c4", new Color[] { Color.green, Color.red },
							NumberRange.create(300.0f, false, 400.0f, true),
							NumberRange.create(3, 1000));

			final LinearColorMapElement c5 = LinearColorMapElement
					.create("c5", new Color[] { Color.red, Color.white },
							NumberRange.create(400.0f, false, 1000.0f, true),
							NumberRange.create(1001, 2000));

			final LinearColorMapElement c6 = LinearColorMapElement
					.create("c6", Color.red, 1001.0f, 2001);

			final LinearColorMapElement c7 = LinearColorMapElement
					.create("nodata", new Color(0, 0, 0, 0), NumberRange.create(
							Double.NaN, Double.NaN), 2201);

			final LinearColorMap list = new LinearColorMap("",
					new LinearColorMapElement[] { c0, c1, c3, c4, c5, c6 },
					new LinearColorMapElement[] { c7 });

			final ParameterBlockJAI pbj = new ParameterBlockJAI(
					RasterClassifier.OPERATION_NAME);
			pbj.addSource(image);
			pbj.setParameter("Domain1D", list);
			final RenderedOp finalimage = JAI.create(
					RasterClassifier.OPERATION_NAME, pbj);

			if (TestData.isInteractiveTest())
				RasterSymbolizerTest.visualize(finalimage, "synthetic");
			else
				finalimage.getTiles();
			finalimage.dispose();
		}

	}

	/**
	 * Building a synthetic image upon a FLOAT sample-model.
	 * 
	 * @return {@linkplain BufferedImage}
	 */
	private BufferedImage getSynthetic_Float() {
		final int width = 500;
		final int height = 500;
		final WritableRaster raster = RasterFactory.createBandedRaster(
				DataBuffer.TYPE_FLOAT, width, height, 1, null);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster.setSample(x, y, 0, (x + y));
			}
		}
		final ColorModel cm = new ComponentColorModelJAI(ColorSpace
				.getInstance(ColorSpace.CS_GRAY), false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_FLOAT);
		final BufferedImage image = new BufferedImage(cm, raster, false, null);
		return image;
	}

	/**
	 * Spearfish test-case.
	 * 
	 * @throws IOException
	 */
	@Test
	public void spearfish() throws IOException {

		// /////////////////////////////////////////////////////////////////////
		//
		// This test is quite standard since the NoData category specified
		// is for NoData values since the input file is a GRASS ascii file
		// where the missing values are represented by * which are substituted
		// with NaN during reads. The only strange thing that we try here is
		// that we map two different classes to the same color with the same
		// index.
		//
		// /////////////////////////////////////////////////////////////////////
		final RenderedImage image = getSpearfhisDemo();

		for (int i = 0; i < TEST_NUM; i++) {
			final LinearColorMapElement c0 = LinearColorMapElement
					.create("c0", Color.yellow, NumberRange.create(
							Double.NEGATIVE_INFINITY, false, 1100, true), 5);

			final LinearColorMapElement c1 = LinearColorMapElement
					.create("c2", Color.blue, NumberRange.create(1100.0, false,
							1200.0, true), 1);

			final LinearColorMapElement c3 = LinearColorMapElement
					.create("c3", Color.green, NumberRange.create(1200.0, false,
							1400.0, true), 7);

			final LinearColorMapElement c4 = LinearColorMapElement
					.create("c4", Color.blue, NumberRange.create(1400.0, false,
							1600, true), 1);

			final LinearColorMapElement c5 = LinearColorMapElement
					.create("c4", Color.CYAN, NumberRange.create(1600.0, false,
							Double.POSITIVE_INFINITY, true), 11);

			final LinearColorMapElement c6 = LinearColorMapElement
					.create("nodata", new Color(0, 0, 0, 0), NumberRange.create(
							Double.NaN, Double.NaN), 0);

			final LinearColorMap list = new LinearColorMap("",
					new LinearColorMapElement[] { c0, c1, c3, c4, c5 },
					new LinearColorMapElement[] { c6 });

			final ParameterBlockJAI pbj = new ParameterBlockJAI(
					RasterClassifier.OPERATION_NAME);
			pbj.addSource(image);
			pbj.setParameter("Domain1D", list);
			final RenderedOp finalimage = JAI.create(
					RasterClassifier.OPERATION_NAME, pbj);

			if (TestData.isInteractiveTest())
				RasterSymbolizerTest.visualize(finalimage, "spearfish");
			else
				finalimage.getTiles();
			finalimage.dispose();
		}

	}

	/**
	 * Building an image based on Spearfish data.
	 * 
	 * @return {@linkplain BufferedImage}
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private RenderedImage getSpearfhisDemo() throws IOException,
			FileNotFoundException {
		final AsciiGridsImageReader reader = (AsciiGridsImageReader) new AsciiGridsImageReaderSpi()
				.createReaderInstance();
		reader.setInput(new FileImageInputStream(TestData.file(this,
				"arcgrid/spearfish_dem.arx")));
		final RenderedImage image = reader.readAsRenderedImage(0, null);
		return image;
	}

	/**
	 * SWAN test-case.
	 * 
	 * @throws IOException
	 */
	@Test
	public void SWAN() throws IOException {
		// /////////////////////////////////////////////////////////////////////
		//
		// This test is interesting since it can be used to simulate the
		// case where someone specifies a ColorMap that overlaps with the native
		// NoData value. For this SWAN data the NoData value is -9999.0 and no
		// NaN which falls right into the first category.
		//
		// We overcome this problem by simply giving higher priority to the
		// NoData category over the other categories when doing a search for
		// the right category given a certain value. This force us to
		// first evaluate the no data category and then evaluate a possible
		// provided overlapping value.
		//
		// This test is also interesting since we create a color map by
		// providing output indexes that are not ordered and also that are not
		// all contained in a closed natural interval. As you can notice by
		// inspecting the different classes below there is an index, 51, which
		// is way outside the range of the others.
		//
		// /////////////////////////////////////////////////////////////////////
		final RenderedImage image = getSWAN();

		for (int i = 0; i < TEST_NUM; i++) {
			final LinearColorMapElement c0 = LinearColorMapElement
					.create("c0", Color.green, NumberRange.create(
							Double.NEGATIVE_INFINITY, 0.3), 51);

			final LinearColorMapElement c1 = LinearColorMapElement
					.create("c2", Color.yellow, NumberRange.create(0.3, false,
							0.6, true), 1);
			
			final LinearColorMapElement c1b = LinearColorMapElement
			.create("c2", Color.BLACK, NumberRange.create(0.3, false,
					0.6, true), 1);
			final LinearColorMapElement c1c = LinearColorMapElement
			.create("c2", Color.yellow, NumberRange.create(0.3, false,
					0.6, true), 1);
			assertFalse(c1.equals(c1b));
			assertTrue(c1.equals(c1c));

			final LinearColorMapElement c3 = LinearColorMapElement
					.create("c3", Color.red, NumberRange.create(0.60, false, 0.90,
							true), 2);

			final LinearColorMapElement c4 = LinearColorMapElement
					.create("c4", Color.BLUE, NumberRange.create(0.9, false,
							Double.POSITIVE_INFINITY, true), 3);

			final LinearColorMapElement nodata = LinearColorMapElement
					.create("nodata", new Color(0, 0, 0, 0), NumberRange.create(
							-9.0, -9.0), 4);

			final LinearColorMap list = new LinearColorMap("testSWAN",
					new LinearColorMapElement[] { c0, c1, c3, c4 },
					new LinearColorMapElement[] { nodata }, new Color(0,0,0));
			

			assertEquals(list.getSourceDimensions(), 1);
			assertEquals(list.getTargetDimensions(), 1);
			assertEquals(list.getName().toString(), "testSWAN");
			assertNotNull(c0.toString());
			
			final ParameterBlockJAI pbj = new ParameterBlockJAI(
					RasterClassifier.OPERATION_NAME);
			pbj.addSource(image);
			pbj.setParameter("Domain1D", list);

			try {
				// //
				// forcing a bad band selection ...
				// //
				pbj.setParameter("bandIndex", new Integer(2));
				final RenderedOp d = JAI.create(
						RasterClassifier.OPERATION_NAME, pbj);
				d.getTiles();
				// we should not be here!
				assertTrue(false);
			} catch (Exception e) {
				// //
				// ... ok, Exception wanted!
				// //
			}

			pbj.setParameter("bandIndex", new Integer(0));
			final RenderedOp finalImage = JAI.create(
					RasterClassifier.OPERATION_NAME, pbj);
			if (TestData.isInteractiveTest())
				RasterSymbolizerTest.visualize(finalImage, "testSWAN1");
			else
				finalImage.getTiles();
			finalImage.dispose();
		}

	}
	
	/**
	 * SWAN test-case.
	 * 
	 * @throws IOException
	 */
	@Test
	public void SWANGAP() throws IOException {

		// /////////////////////////////////////////////////////////////////////
		//
		// This test is interesting since it can be used to simulate the
		// case where someone specifies a ColorMap that overlaps with the native
		// NoData value. For this SWAN data the NoData value is -9999.0 and no
		// NaN which falls right into the first category.
		//
		// We overcome this problem by simply giving higher priority to the
		// NoData category over the other categories when doing a search for
		// the right category given a certain value. This force us to
		// first evaluate the no data category and then evaluate a possible
		// provided overlapping value.
		//
		// This test is also interesting since we create a color map by
		// providing output indexes that are not ordered and also that are not
		// all contained in a closed natural interval. As you can notice by
		// inspecting the different classes below there is an index, 51, which
		// is way outside the range of the others.
		//
		// /////////////////////////////////////////////////////////////////////
		final RenderedImage image = getSWAN();

		for (int i = 0; i < TEST_NUM; i++) {
			final LinearColorMapElement c0 = LinearColorMapElement
					.create("c0", Color.green, NumberRange.create(
							Double.NEGATIVE_INFINITY, 0.3), 51);

			final LinearColorMapElement c1 = LinearColorMapElement
					.create("c2", Color.yellow, NumberRange.create(0.3, false,
							0.6, true), 1);

			final LinearColorMapElement c3 = LinearColorMapElement
					.create("c3", Color.red, NumberRange.create(0.70, false, 0.90,
							true), 2);

			final LinearColorMapElement c4 = LinearColorMapElement
					.create("c4", Color.BLUE, NumberRange.create(0.9, false,
							Double.POSITIVE_INFINITY, true), 3);

			final LinearColorMapElement nodata = LinearColorMapElement
					.create("nodata", Color.red, NumberRange.create(
							-9.0, -9.0), 4);

			final LinearColorMap list = new LinearColorMap("testSWAN",
					new LinearColorMapElement[] { c0, c1, c3, c4 },
					new LinearColorMapElement[] { nodata }, new Color(0,0,0,0));
		

			final ParameterBlockJAI pbj = new ParameterBlockJAI(
					RasterClassifier.OPERATION_NAME);
			pbj.addSource(image);
			pbj.setParameter("Domain1D", list);

			try {
				// //
				// forcing a bad band selection ...
				// //
				pbj.setParameter("bandIndex", new Integer(2));
				final RenderedOp d = JAI.create(
						RasterClassifier.OPERATION_NAME, pbj);
				d.getTiles();
				// we should not be here!
				assertTrue(false);
			} catch (Exception e) {
				// //
				// ... ok, Exception wanted!
				// //
			}

			pbj.setParameter("bandIndex", new Integer(0));
			final RenderedOp finalImage = JAI.create(
					RasterClassifier.OPERATION_NAME, pbj);
			final IndexColorModel icm=(IndexColorModel) finalImage.getColorModel();
			assertEquals(icm.getRed(4),255);
			assertEquals(icm.getRed(2),255);
			
			if (TestData.isInteractiveTest())
				RasterSymbolizerTest.visualize(finalImage, "testSWANGAP");
			else
				finalImage.getTiles();
			finalImage.dispose();
		}

	}
	/**
	 * Building an image based on SWAN data.
	 * 
	 * @return {@linkplain BufferedImage}
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private RenderedImage getSWAN() throws IOException, FileNotFoundException {
		final AsciiGridsImageReader reader = (AsciiGridsImageReader) new AsciiGridsImageReaderSpi()
				.createReaderInstance();
		reader.setInput(new FileImageInputStream(TestData.file(this,
				"arcgrid/SWAN_NURC_LigurianSeaL07_HSIGN.asc")));
		final RenderedImage image = reader.readAsRenderedImage(0, null);
		return image;
	}

	/**
	 * NoData only test-case.
	 * 
	 * @throws IOException
	 * @throws TransformException
	 */
	@Test
	public void noDataOnly() throws IOException, TransformException {
	
		// /////////////////////////////////////////////////////////////////////
		//
		// We are covering here a case that can often be verified, i.e. the case
		// when only NoData values are known and thus explicitly mapped by the
		// user to a defined nodata DomainElement, but not the same for the
		// others.
		// In such case we want CatrgoryLists automatically map unknown data to
		// a Passthrough DomainElement, which identically maps raster data to
		// category
		// data.
		//
		// /////////////////////////////////////////////////////////////////////
	
		for (int i = 0; i < TEST_NUM; i++) {
	
			final LinearColorMapElement n0 = LinearColorMapElement
					.create("nodata", new Color(0, 0, 0, 0), NumberRange.create(
							Double.NaN, Double.NaN), 9999);
	
			final LinearColorMap list = new LinearColorMap("",
					new LinearColorMapElement[] { n0 });
	
			double testNum = Math.random();
			try{
				assertEquals(list.transform(testNum), testNum, 0.0);
				assertTrue(false);
			}catch (Exception e) {
				// TODO: handle exception
			}
			assertEquals(list.transform(Double.NaN), 9999, 0.0);
	
	
		}
	}
}
