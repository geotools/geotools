package org.geotools.coverage.io.range.impl;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.List;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.operator.BandMergeDescriptor;
import javax.media.jai.operator.ConstantDescriptor;

import org.geotools.coverage.io.range.RangeAxis;
import org.geotools.coverage.io.range.RangeAxisBin.StringAxisBin;
import org.junit.Ignore;
import org.junit.Test;
/**
 * Tests for the axis class and its related classes
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class SyntheticDataTest extends org.junit.Assert{
	
	public static RenderedImage band= ConstantDescriptor.create(512.0f, 512.0f, new Byte[]{0}, null);
	

	@Test
	@Ignore
	public void testAxis(){
		final RangeAxis rangeAxis = IMAGE_BAND_UTILITIES.PHOTOGRAPHIC_BANDS_AXIS;
		assertEquals(rangeAxis, IMAGE_BAND_UTILITIES.PHOTOGRAPHIC_BANDS_AXIS);
		final RangeAxis axis2= new RangeAxis(
				rangeAxis.getName(),
				rangeAxis.getDescription(),
				rangeAxis.getUnitOfMeasure());
		assertEquals(rangeAxis, axis2);
		
	}
	@Test
	@Ignore
	public void testAxisBin(){
		
		// get the bin for the single gray band
		List<StringAxisBin> bins = IMAGE_BAND_UTILITIES.getBinsFromRenderedImage(band);
		assertEquals(1, bins.size());
		// we should be able to get the same bin using the enum keys
		assertEquals(IMAGE_BAND_UTILITIES.GRAY.getAxisBin(), bins.get(0));
		
		// now two bands
		final RenderedImage twoBands= BandMergeDescriptor.create(band, band, null);
		bins = IMAGE_BAND_UTILITIES.getBinsFromRenderedImage(twoBands);
		assertEquals(2, bins.size());
		// we should be able to get the same bin using the enum keys
		assertEquals(IMAGE_BAND_UTILITIES.GRAY.getAxisBin(), bins.get(0));
		// we should be able to get the same bin using the enum keys
		assertEquals(IMAGE_BAND_UTILITIES.ALPHA.getAxisBin(), bins.get(1));
		
		//RGB
		final ImageLayout layout= new ImageLayout();
		layout.setColorModel(
				new ComponentColorModel(
						ColorSpace.getInstance(ColorSpace.CS_sRGB),
						false,
						false,
						Transparency.OPAQUE,
						DataBuffer.TYPE_BYTE
				)
			);
		final RenderingHints hints= new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout);
		final RenderedImage rgb= BandMergeDescriptor.create(band, twoBands, hints);
		bins = IMAGE_BAND_UTILITIES.getBinsFromRenderedImage(rgb);
		assertEquals(3, bins.size());
		// we should be able to get the same bin using the enum keys
		assertEquals(IMAGE_BAND_UTILITIES.RED.getAxisBin(), bins.get(0));
		// we should be able to get the same bin using the enum keys
		assertEquals(IMAGE_BAND_UTILITIES.GREEN.getAxisBin(), bins.get(1));
		// we should be able to get the same bin using the enum keys
		assertEquals(IMAGE_BAND_UTILITIES.BLUE.getAxisBin(), bins.get(2));		
		
	}


}
