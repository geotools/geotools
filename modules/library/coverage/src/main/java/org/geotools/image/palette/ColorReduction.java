/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PointOpImage;
import javax.media.jai.operator.BandMergeDescriptor;
import javax.media.jai.operator.BandSelectDescriptor;
import javax.media.jai.operator.MultiplyConstDescriptor;

/**
 * {@link PointOpImage} to perform color reduction on an image using the palette builder.
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
@SuppressWarnings("unchecked")
public class ColorReduction extends PointOpImage {

	private int numColors;

	private int alphaThreshold;

	private CustomPaletteBuilder paletteBuilder;
	
	private boolean initialized = false;

	private RenderingHints hints;

	public ColorReduction(RenderedImage image, RenderingHints hints,
			int numColors, int alpaThreshold, int subsx, int subsy) {
		super(image, new ImageLayout(image), null, false);
		this.numColors = numColors;
		this.alphaThreshold = alpaThreshold;
		if (image.getColorModel().hasAlpha()) {
			RenderedImage alpha = BandSelectDescriptor.create(image,
					new int[] { image.getSampleModel().getNumBands() - 1 },
					null);
			alpha = MultiplyConstDescriptor.create(alpha,
					new double[] { alphaThreshold }, null);
			image = BandSelectDescriptor.create(image, new int[] { 0, 1, 2 },
					null);

			final ImageLayout layout = new ImageLayout();
			layout.setColorModel(new ComponentColorModel(ColorSpace
					.getInstance(ColorSpace.CS_sRGB), true, false,
					Transparency.BITMASK, DataBuffer.TYPE_BYTE));
			image = BandMergeDescriptor.create(image, alpha,
					new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout))
					.getNewRendering();
			this.setSource(image, 0);
		}

		// force palette computation
		this.paletteBuilder = new CustomPaletteBuilder(image, this.numColors,subsx, subsy, 1);
		this.hints=hints;
		

	}

	private ImageLayout getImageLayout(RenderedImage image, RenderingHints hints) {
		ImageLayout layout = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
		if (layout == null)

			layout = new ImageLayout(image);

		layout.setColorModel(this.paletteBuilder.getIndexColorModel());
		layout.setSampleModel(paletteBuilder.getIndexColorModel().createCompatibleSampleModel(image.getWidth(),image.getHeight()));
		return layout;
	}

	@Override
	public Raster computeTile(int tx, int ty) {
		synchronized (this) {
			if(!initialized){
				this.paletteBuilder.buildPalette();
				this.setImageLayout(getImageLayout(getSourceImage(0), hints));
			}
		}
		final RenderedImage sourceImage = getSourceImage(0);
		final ColorModel sourceColorModel = sourceImage.getColorModel();
		final Raster sourceRaster = sourceImage.getTile(tx, ty);
		final int w = sourceRaster.getWidth();
		final int h = sourceRaster.getHeight();
		final int numBands = sourceRaster.getSampleModel().getNumBands();
		final int rgba[] = new int[numBands];
		final boolean sourceHasAlpha = sourceColorModel.hasAlpha();
		final int alphaBand = sourceHasAlpha ? numBands - 1 : -1;
		final int minx = sourceRaster.getMinX();
		final int maxx = minx + w;
		final int miny = sourceRaster.getMinY();
		final int maxy = miny + h;
		final WritableRaster destRaster = this.colorModel
				.createCompatibleWritableRaster(w, h)
				.createWritableTranslatedChild(minx, miny);
		
		// scan the provided tile and for each pixel assing the best color we have
		for (int i = minx; i < maxx; i++)
			for (int j = miny; j < maxy; j++) {
				sourceRaster.getPixel(i, j, rgba);
				destRaster.setSample(i, j, 0, paletteBuilder.findNearestColorIndex(rgba, alphaBand));
			}
		return destRaster;
	}

}
