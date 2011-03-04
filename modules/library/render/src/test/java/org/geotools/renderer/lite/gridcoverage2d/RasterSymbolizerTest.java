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

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.transform.TransformerException;

import junit.framework.Assert;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.image.ComponentColorModelJAI;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ChannelSelectionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.style.ContrastMethod;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * @author  Simone Giannecchini, GeoSolutions.
 *
 * @source $URL$
 */
public class RasterSymbolizerTest  {

	private final static StyleFactory sf = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

	/**
	 * Creates a simple 500x500 {@link RenderedImage} for testing purposes.
	 * 
	 * <p>
	 * Values are randomly set to the provided noDataValue.
	 * 
	 * @param noDataValue
	 * @return
	 */
	public static RenderedImage getSynthetic(final double noDataValue) {
		final int width = 500;
		final int height = 500;
		final WritableRaster raster = RasterFactory.createBandedRaster(
				DataBuffer.TYPE_DOUBLE, width, height, 1, null);
		final Random random = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (random.nextFloat() > 0.9)
					raster.setSample(x, y, 0, noDataValue);
				else
					raster.setSample(x, y, 0, (x + y));
			}
		}
		final ColorModel cm = new ComponentColorModelJAI(ColorSpace
				.getInstance(ColorSpace.CS_GRAY), false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_DOUBLE);
		final BufferedImage image = new BufferedImage(cm, raster, false, null);
		return image;
	}

	@org.junit.Test
	public void contrastEnhancementMethods() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "hs.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "histogram.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh_SLD = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh_SLD.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh_SLD);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "hs.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);

		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

		cntEnh.setMethod(ContrastMethod.HISTOGRAM);;
		//cntEnh.setType(type);
		//cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeGray.setChannelName("1");
		chTypeGray.setContrastEnhancement(cntEnh);
		chSel.setGrayChannel(chTypeGray);
		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));
		
		// visit the RasterSymbolizer
		rsh_StyleBuilder.visit(rsb_1);
		testRasterSymbolizerHelper(rsh_StyleBuilder);
		
		// ////////////////////////////////////////////////////////////////////
		//
		// Test #3: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Logarithmic}
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "hs.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);

		surl = TestData.url(this, "logarithmic.sld");
		stylereader = new SLDParser(sf, surl);
		sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		rsh_SLD = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_2 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh_SLD.visit(rs_2);
		
		testRasterSymbolizerHelper(rsh_SLD);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #4: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Logarithmic}
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "hs.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }));

		// the RasterSymbolizer Helper
		rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_2 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel_2 = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray_2 = new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh_2 = new ContrastEnhancementImpl();

		cntEnh_2.setLogarithmic();
		//cntEnh.setType(type);
		//cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeGray_2.setChannelName("1");
		chTypeGray_2.setContrastEnhancement(cntEnh_2);
		chSel_2.setGrayChannel(chTypeGray_2);
		rsb_2.setChannelSelection(chSel_2);
		rsb_2.setOpacity(sldBuilder.literalExpression(1.0));
		
		// visit the RasterSymbolizer
		rsh_StyleBuilder.visit(rsb_2);
		testRasterSymbolizerHelper(rsh_StyleBuilder);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #5: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Hyperbolic}
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "hs.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);

		surl = TestData.url(this, "exponential.sld");
		stylereader = new SLDParser(sf, surl);
		sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		rsh_SLD = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_3 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh_SLD.visit(rs_3);
		
		testRasterSymbolizerHelper(rsh_SLD);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #6: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Hyperbolic}
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "hs.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);

		// the RasterSymbolizer Helper
		rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh_StyleBuilder = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_3 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel_3 = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray_3 = new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh_3 = new ContrastEnhancementImpl();

		cntEnh_3.setExponential();
		//cntEnh.setType(type);
		//cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeGray_3.setChannelName("1");
		chTypeGray_3.setContrastEnhancement(cntEnh_3);
		chSel_3.setGrayChannel(chTypeGray_3);
		rsb_3.setChannelSelection(chSel_3);
		rsb_3.setOpacity(sldBuilder.literalExpression(1.0));
		rsb_3.setOverlap(sldBuilder.literalExpression("AVERAGE"));
		
		// visit the RasterSymbolizer
		rsh_StyleBuilder.visit(rsb_3);
		testRasterSymbolizerHelper(rsh_StyleBuilder);

	}
	
	@org.junit.Test
	public void bandFloat32_SLD() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Float32.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);
		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "1band_Float32_test1.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh);
		
		


		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//
		// ////////////////////////////////////////////////////////////////////
		// the GridCoverage
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Float32.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }));
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();
		cntEnh.setLogarithmic();
		chTypeGray.setChannelName("1");
		chTypeGray.setContrastEnhancement(cntEnh);
		chSel.setGrayChannel(chTypeGray);
		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		
		testRasterSymbolizerHelper(rsh);

	}
 
	@Test
	public void test1BandFloat32_ColorMap_SLD() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Float32.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//    - ColorMap
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "1band_Float32_test2.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final UserLayer nl = (UserLayer) sld.getStyledLayers()[0];
		final Style style = nl.getUserStyles()[0];
		final FeatureTypeStyle fts = style.getFeatureTypeStyles()[0];
		final Rule rule = fts.getRules()[0];
		final RasterSymbolizer rs_1 = (RasterSymbolizer) rule.getSymbolizers()[0];

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh);
		


		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//    - ColorMap
		//
		// ////////////////////////////////////////////////////////////////////
		// the GridCoverage
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Float32.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }));
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

		cntEnh.setMethod(ContrastMethod.HISTOGRAM);;
		//cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeGray.setChannelName("1");
		chTypeGray.setContrastEnhancement(cntEnh);
		
		chSel.setGrayChannel(chTypeGray);

		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		
		rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));
		
		final ColorMap cm = sldBuilder.createColorMap(
				new String[] { // labels
					"category",
					"category",
					"category"
				},
				new double[] { // quantities
					0.1,
					50.0,
					200.0
				},
				new Color[] { // colors with alpha
					new Color(255,0,0,255),
					new Color(0,255,0,40),
					new Color(0,0,255,125)
				},
				ColorMap.TYPE_RAMP);
		
		rsb_1.setColorMap(cm);
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		
		testRasterSymbolizerHelper(rsh);

	}
 
	@org.junit.Test
	public void bandsUInt16_SLD() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		final GridSampleDimension[] gsd={
				new GridSampleDimension("test1BandByte_SLD1"),
		};
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "test.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },new double[] { 90, 180 }),
						gsd,
						null,
						null);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: RGB
		//    - Contrast Enh: Histogram
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "4bands_UInt16_test1.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		testRasterSymbolizerHelper(rsh);
		
		
		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: RGB
		//	  - Contrast Enh: Histogram
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "small_4bands_UInt16.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },new double[] { 90, 180 }));
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeRed  	= new SelectedChannelTypeImpl();
		final SelectedChannelType chTypeBlue   	= new SelectedChannelTypeImpl();
		final SelectedChannelType chTypeGreen 	= new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

		cntEnh.setMethod(ContrastMethod.HISTOGRAM);;
		//cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeRed.setChannelName("1");
		chTypeBlue.setChannelName("2");
		chTypeGreen.setChannelName("3");
		
		chSel.setRGBChannels(chTypeRed, chTypeBlue, chTypeGreen);

		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		rsb_1.setContrastEnhancement(cntEnh);
		rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		
		testRasterSymbolizerHelper(rsh);

	}

	private static RasterSymbolizer extractRasterSymbolizer(StyledLayerDescriptor sld) {
		final UserLayer nl = (UserLayer) sld.getStyledLayers()[0];
		final Style style = nl.getUserStyles()[0];
		final FeatureTypeStyle fts = style.featureTypeStyles().get(0);
		final Rule rule = fts.rules().get(0);
		final RasterSymbolizer rs_1 = (RasterSymbolizer) rule.getSymbolizers()[0];
		return rs_1;
	}

	@org.junit.Test
	public void bandsByte_SLD() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		final GridSampleDimension[] gsd={
				new GridSampleDimension("test1BandByte_SLD1"),
				new GridSampleDimension("test1BandByte_SLD2"),
				new GridSampleDimension("test1BandByte_SLD3")
		};
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_3bands_Byte.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),gsd,null,null);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: RGB
		//    - Contrast Enh: Histogram
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "3bands_Byte_test1.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh);
		

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: RGB
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "small_3bands_Byte.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }));
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeRed  	= new SelectedChannelTypeImpl();
		final SelectedChannelType chTypeBlue   	= new SelectedChannelTypeImpl();
		final SelectedChannelType chTypeGreen 	= new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

		cntEnh.setMethod(ContrastMethod.HISTOGRAM);;
		cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeRed.setChannelName("1");
		chTypeBlue.setChannelName("2");
		chTypeGreen.setChannelName("3");
		chSel.setRGBChannels(chTypeRed, chTypeBlue, chTypeGreen);
		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		rsb_1.setContrastEnhancement(cntEnh);
		rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		
		testRasterSymbolizerHelper(rsh);

	}
	@org.junit.Test
	public void bandsByte_ColorMap_SLD() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		final GridSampleDimension[] gsd={
				new GridSampleDimension("test1BandByte_SLD1"),
				new GridSampleDimension("test1BandByte_SLD2"),
				new GridSampleDimension("test1BandByte_SLD3")
		};
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_3bands_Byte.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),gsd,null,null);

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: RGB
		//    - Contrast Enh: Histogram
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "3bands_Byte_test2.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		final RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh);
		


		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: RGB
		//	  - Contrast Enh: Histogram
		//
		// ////////////////////////////////////////////////////////////////////
		// the GridCoverage
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_3bands_Byte.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }));
		
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray  	= new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

		cntEnh.setMethod(ContrastMethod.HISTOGRAM);;
		//cntEnh.setGammaValue(sldBuilder.literalExpression(0.50));
		
		chTypeGray.setChannelName("1");		
		chSel.setGrayChannel(chTypeGray);

		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		rsb_1.setContrastEnhancement(cntEnh);
		rsb_1.setOverlap(sldBuilder.literalExpression("AVERAGE"));

		final ColorMap cm = sldBuilder.createColorMap(
				new String[] { // labels
					"category",
					"category",
					"category"
				},
				new double[] { // quantities
					0.1,
					50.0,
					200.0
				},
				new Color[] { // colors with alpha
					new Color(255,0,0,255),
					new Color(0,255,0,40),
					new Color(0,0,255,125)
				},
				ColorMap.TYPE_RAMP);
		
		rsb_1.setColorMap(cm);
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		
		testRasterSymbolizerHelper(rsh);

	}

	@org.junit.Test
	public void BandByte_SLD() throws IOException, TransformerException, FactoryRegistryException, IllegalArgumentException, URISyntaxException {
		// the GridCoverage
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Byte.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("test1BandByte_SLD")},null,null);
		

		// ////////////////////////////////////////////////////////////////////
		//
		// Test #1: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//
		// ////////////////////////////////////////////////////////////////////
		java.net.URL surl = TestData.url(this, "1band_Float32_test1.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);

		RasterSymbolizer rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh);


		// ////////////////////////////////////////////////////////////////////
		//
		// Test #2: [StyleBuilder]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//
		// ////////////////////////////////////////////////////////////////////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Byte.tif").toURI())),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }));
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);
		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
		final ContrastEnhancement cntEnh = new ContrastEnhancementImpl();

		//this will convert to byte!!!
		cntEnh.setMethod(ContrastMethod.HISTOGRAM);
		chTypeGray.setChannelName("1");
		chTypeGray.setContrastEnhancement(cntEnh);
		chSel.setGrayChannel(chTypeGray);
		rsb_1.setChannelSelection(chSel);
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		final RenderedImage im=((GridCoverage2D) rsh.getOutput()).getRenderedImage();
		Assert.assertTrue(im.getSampleModel().getDataType()==0);
		
		testRasterSymbolizerHelper(rsh);



		// ////////////////////////////////////////////////////////////////////
		//
		// Test #3: [SLD]
		//    - Opacity: 1.0
		//    - ChannelSelection: Gray {Contrast Enh: Histogram}
		//    - ColorMap
		//
		// ////////////////////////////////////////////////////////////////////
		// the GridCoverage
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", new File(TestData.url(this, "small_1band_Byte.tif").toURI())),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }));
		
		surl = TestData.url(this, "1band_Float32_test2.sld");
		stylereader = new SLDParser(sf, surl);
		sld = stylereader.parseSLD();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		// build the RasterSymbolizer
		rs_1 = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs_1);
		
		testRasterSymbolizerHelper(rsh);
	}
 

 
	@org.junit.Test
	public void colorMap() throws IOException, TransformerException {
		////
		//
		// Test using an SLD file
		//
		////
		final URL sldURL = TestData.url(this, "colormap.sld");
		final SLDParser stylereader = new SLDParser(sf, sldURL);
		final StyledLayerDescriptor sld = stylereader.parseSLD();

		// get a coverage
		GridCoverage2D gc = CoverageFactoryFinder
				.getGridCoverageFactory(null)
				.create(
						"name",
						PlanarImage.wrapRenderedImage(getSynthetic(Double.NaN)),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),
						new GridSampleDimension[] { new GridSampleDimension(
								"sd", new Category[] { new Category("",
										Color.BLACK, 0) }, null) }, null, null);

	
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
		RasterSymbolizer rs = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs);
		IndexColorModel icm1 = (IndexColorModel) ((GridCoverage2D)rsh.getOutput()).getRenderedImage().getColorModel();
		testRasterSymbolizerHelper(rsh);
		
		
		////
		//
		// Test using StyleBuilder
		//
		////
		// get a coverage
		gc = CoverageFactoryFinder
				.getGridCoverageFactory(null)
				.create(
						"name",
						PlanarImage.wrapRenderedImage(getSynthetic(Double.NaN)),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),
						new GridSampleDimension[] { new GridSampleDimension(
								"sd", new Category[] { new Category("",
										Color.BLACK, 0) }, null) }, null, null);

		// build the RasterSymbolizer
		StyleBuilder sldBuilder = new StyleBuilder();
		rsh = new RasterSymbolizerHelper(gc, null);
		rs = sldBuilder.createRasterSymbolizer();
		final ColorMap cm = sldBuilder.createColorMap(
				new String[] { // labels
					"category0",
					"category1",
					"category2"
				},
				new double[] { // quantities
					100.0,
					500.0,
					900.0
				},
				new Color[] { // colors
					new Color(255,0,0,255),
					new Color(0,255,0,(int) (255*0.8)),
					new Color(0,0,255,(int) (255*0.2))
				},
				ColorMap.TYPE_RAMP);
		cm.setExtendedColors(true);
		rs.setColorMap(cm);

		// visit the RasterSymbolizer
		rsh.visit(rs);
		IndexColorModel icm2 = (IndexColorModel) ((GridCoverage2D)rsh.getOutput()).getRenderedImage().getColorModel();
		testRasterSymbolizerHelper(rsh);
		//check that the two color models are equals!
		Assert.assertTrue(icm1.equals(icm2));

	}
	@org.junit.Test
	public void rgb() throws IOException, TransformerException {
		java.net.URL surl = TestData.url(this, "testrgb.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();

		final GridSampleDimension[] gsd={
				new GridSampleDimension("test1BandByte_SLD1"),
				new GridSampleDimension("test1BandByte_SLD2"),
				new GridSampleDimension("test1BandByte_SLD3"),
		};
		// get a coverage
		final GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", 
								TestData.file(this,"bahamas_hires.jpg")),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),gsd,null,null);

		// build the RasterSymbolizer
		final SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
		final RasterSymbolizer rs = extractRasterSymbolizer(sld);

		// visit the RasterSymbolizer
		rsh.visit(rs);

		testRasterSymbolizerHelper(rsh);

	}

	/**
	 * Found out that we were creating a classification transform that was incorrect
	 * since the gaps index was set to the max index + 1 in the colormap, which 
	 * resulted in {@link IndexOutOfBoundsException}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGapsColor() throws IOException{
            java.net.URL surl = TestData.url(this, "Topsoil-Organic-Carbon.sld");
            SLDParser stylereader = new SLDParser(sf, surl);
            StyledLayerDescriptor sld = stylereader.parseSLD();
    
            final GridSampleDimension[] gsd = { new GridSampleDimension("test1BandByte_SLD1")};
            // get a coverage
            final GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null).create("name",
                    JAI.create("ImageRead", TestData.file(this, "toc.tif")),
                    new GeneralEnvelope(new double[] { -90, -180 }, new double[] { 90, 180 }), gsd,
                    null, null);
    
            // build the RasterSymbolizer
            final SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc,
                    null);
            final RasterSymbolizer rs = extractRasterSymbolizer(sld);
    
            // visit the RasterSymbolizer
            rsh.visit(rs);
    
            testRasterSymbolizerHelper(rsh);
	}
	
	@org.junit.Test
	public void dem() throws IOException, TransformerException {
		
		////
		//
		// Test using an SLD file
		//
		////
		java.net.URL surl = TestData.url(this, "raster_dem.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();

		// get a coverage
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", TestData.file(this,"smalldem.tif")),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("dem")},null,null);
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
		final RasterSymbolizer rs = extractRasterSymbolizer(sld);
		rsh.visit(rs);
		testRasterSymbolizerHelper(rsh);
		
		
		////
		//
		// Test using stylebuilder
		//
		////
		gc = CoverageFactoryFinder.getGridCoverageFactory(null)
		.create(
				"name",
				JAI.create("ImageRead", TestData.file(this,"smalldem.tif")),
				new GeneralEnvelope(new double[] { -90, -180 },
						new double[] { 90, 180 }));
		StyleBuilder sldBuilder = new StyleBuilder();
		// the RasterSymbolizer Helper
		rsh = new RasterSymbolizerHelper(gc, null);

		final RasterSymbolizer rsb_1 = sldBuilder.createRasterSymbolizer();
		final ChannelSelection chSel = new ChannelSelectionImpl();
		final SelectedChannelType chTypeGray = new SelectedChannelTypeImpl();
		chTypeGray.setChannelName("1");
		chSel.setGrayChannel(chTypeGray);
		rsb_1.setChannelSelection(chSel);
		rsb_1.setOpacity(sldBuilder.literalExpression(1.0));
		final ColorMap cm = sldBuilder.createColorMap(
				new String[] { // labels
					"category",
					"category",
					"category"
				},
				new double[] { // quantities
					0.1,
					50.0,
					200.0
				},
				new Color[] { // colors with alpha
					new Color(255,0,0,255),
					new Color(0,255,0,40),
					new Color(0,0,255,125)
				},
				ColorMap.TYPE_RAMP);
		
		rsb_1.setColorMap(cm);
		
		// visit the RasterSymbolizer
		rsh.visit(rsb_1);
		
		testRasterSymbolizerHelper(rsh);

	}
	@org.junit.Test
	public void landsat() throws IOException, TransformerException {
		java.net.URL surl = TestData.url(this, "landsat.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();

		final GridSampleDimension[] gsd={
				new GridSampleDimension("test1BandByte_SLD1"),
				new GridSampleDimension("test1BandByte_SLD2"),
				new GridSampleDimension("test1BandByte_SLD3"),
				new GridSampleDimension("test1BandByte_SLD4"),
				new GridSampleDimension("test1BandByte_SLD5"),
				new GridSampleDimension("test1BandByte_SLD6"),
			new GridSampleDimension("test1BandByte_SLD")
		};
		// get a coverage
		final GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", TestData.file(this,"landsat.tiff")),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),gsd,null,null);
		final SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
		final RasterSymbolizer rs = extractRasterSymbolizer(sld);
		rsh.visit(rs);
		
		final RenderedImage ri = ((GridCoverage2D)rsh.getOutput()).getRenderedImage();
		Assert.assertTrue(ri.getColorModel() instanceof ComponentColorModel);
		Assert.assertTrue(ri.getColorModel().getNumComponents()==3);
		testRasterSymbolizerHelper(rsh);

	}

	private static void testRasterSymbolizerHelper(final SubchainStyleVisitorCoverageProcessingAdapter rsh) {
		if (TestData.isInteractiveTest()) {
			visualize(((GridCoverage2D)rsh.getOutput()).getRenderedImage(), rsh.getName()
					.toString());

		} else {
			PlanarImage.wrapRenderedImage(((GridCoverage2D)rsh.getOutput()).getRenderedImage())
					.getTiles();
			rsh.dispose(new Random().nextBoolean() ? true : false);
		}
	}

	/**
	 * @param rsh
	 * @throws HeadlessException
	 */
	public static void visualize(
			RenderedImage ri, 
			final String name)
			throws HeadlessException {
		
		//actually shows this image.
		final JFrame jf= new JFrame(name);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(new DisplayJAI(ri));
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				jf.pack();
				jf.setVisible(true);
				
			}

		});


	}

	@Test
	public void twoColorsTest() throws IOException{
		java.net.URL surl = TestData.url(this, "raster_discretecolors.sld");
		SLDParser stylereader = new SLDParser(sf, surl);
		StyledLayerDescriptor sld = stylereader.parseSLD();

		// get a coverage
		GridCoverage2D gc = CoverageFactoryFinder.getGridCoverageFactory(null)
				.create(
						"name",
						JAI.create("ImageRead", TestData.file(this,"smalldem.tif")),
						new GeneralEnvelope(new double[] { -90, -180 },
								new double[] { 90, 180 }),new GridSampleDimension[]{new GridSampleDimension("dem")},null,null);
		SubchainStyleVisitorCoverageProcessingAdapter rsh = new RasterSymbolizerHelper(gc, null);
		final RasterSymbolizer rs = extractRasterSymbolizer(sld);
		rsh.visit(rs);
		
		//test
		final RenderedImage ri = ((GridCoverage2D)rsh.getOutput()).getRenderedImage();
		Assert.assertTrue(ri.getColorModel() instanceof IndexColorModel);
		final IndexColorModel icm= (IndexColorModel) ri.getColorModel();
		final int mapSize=icm.getMapSize();
		Assert.assertEquals(3, mapSize);
		
		//get colors
		final int rgb[]= new int[mapSize];		
		rgb[0]=0xFFFFFF&icm.getRGB(0);
		rgb[1]=0xFFFFFF&icm.getRGB(1);
		rgb[2]=0xFFFFFF&icm.getRGB(2);
		
		int found=0;
		for(int i =0;i<mapSize;i++){
			switch(rgb[i]){
			case 0x008000:case 0x663333:case 0:
				found++;
				break;
			default:
				throw new IllegalStateException("Found unexpected colors:"+rgb[i]);
			}
		}
			
		
		testRasterSymbolizerHelper(rsh);
		
	}

}
