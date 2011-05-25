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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

/**
 * This class provide an Image oriented interface for the
 * {@link EfficientInverseColorMapComputation}. Specifically, it is designed in
 * order to implement the {@link BufferedImage} for processing
 * {@link BufferedImage}s efficiently accessing the raster pixels directly but
 * it also provide a method to process general {@link RenderedImage}s
 * implementations.
 * 
 * @author Simone Giannecchini - GeoSolutions SAS
 * @see EfficientInverseColorMapComputation
 * 
 *
 * @source $URL$
 */
public final class InverseColorMapOp implements BufferedImageOp {

	protected final InverseColorMapRasterOp rasterOp;

	protected final IndexColorModel icm;

	protected final int alphaThreshold;

	protected final boolean hasAlpha;

	protected final int transparencyIndex;

	public InverseColorMapOp(final IndexColorModel destCM,
			final int quantizationColors, final int alphaThreshold) {
		this.rasterOp = new InverseColorMapRasterOp(destCM, quantizationColors,
				alphaThreshold);
		this.icm = destCM;
		this.alphaThreshold = alphaThreshold;
		hasAlpha = icm.hasAlpha();
		transparencyIndex = icm.getTransparentPixel();

	}

	public InverseColorMapOp(final IndexColorModel destCM) {
		this(destCM, InverseColorMapRasterOp.DEFAULT_QUANTIZATION_COLORS,
				InverseColorMapRasterOp.DEFAULT_ALPHA_TH);
	}

	public BufferedImage createCompatibleDestImage(BufferedImage src,
			ColorModel destCM) {
		if (!(destCM instanceof IndexColorModel)
				|| ((IndexColorModel) destCM).getTransparency() == Transparency.TRANSLUCENT)
			return null;
		return new BufferedImage(src.getWidth(), src.getHeight(),
				BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel) destCM);
	}

	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		if (dest == null)
			dest = new BufferedImage(src.getWidth(), src.getHeight(),
					BufferedImage.TYPE_BYTE_INDEXED, icm);
		else {
			if (!(dest.getColorModel() instanceof IndexColorModel)
					|| ((IndexColorModel) dest.getColorModel())
							.getTransparency() != this.transparencyIndex)
				throw new IllegalArgumentException();
			if (((IndexColorModel) dest.getColorModel()).getTransparentPixel() != this.transparencyIndex)
				throw new IllegalArgumentException();
		}
		final WritableRaster wr = dest.getRaster();
		final Raster ir = src.getRaster();
		this.rasterOp.filter(ir, wr);
		return dest;
	}

	public BufferedImage filterRenderedImage(RenderedImage src) {
		// //
		//
		// ShortCut for using bufferedimages and avoiding tiling
		//
		// //
		if (src instanceof BufferedImage)
			return filter((BufferedImage) src, null);

		// //
		//
		// Create the destination image
		//
		// //
		final BufferedImage dest = new BufferedImage(src.getWidth(), src
				.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, icm);
		final WritableRaster destWr = dest.getRaster();

		// //
		//
		// Filter the image out
		//
		// //

		// /
		//
		// Optimize the hell out of this code. We have a single tile, let's go
		// fast!
		//
		// //
		if (src.getNumXTiles() == 1 && src.getNumYTiles() == 1) {
			final int minTileX = src.getMinTileX();
			final int minTileY = src.getMinTileY();
			final Raster sourceR = src.getTile(minTileX, minTileY);
			rasterOp.filter(sourceR.createChild(src.getMinX(), src.getMinY(),
					src.getWidth(), src.getHeight(), 0, 0, null), destWr);
			return dest;
		}

		// //
		//
		// Collecting info about the source image
		//
		// //
		final int numBands = src.getSampleModel().getNumBands();
		final int rgba[] = new int[numBands];
		final boolean sourceHasAlpha = (numBands % 2 == 0);
		final int alphaBand = sourceHasAlpha ? numBands - 1 : -1;
		final EfficientInverseColorMapComputation invCM = rasterOp.getInvCM();
		final int minx_ = src.getMinX();
		final int miny_ = src.getMinY();
		final int srcW_ = src.getWidth();
		final int srcH_ = src.getHeight();
		final int maxx_ = minx_ + srcW_;
		final int maxy_ = miny_ + srcH_;
		final int minTileX = src.getMinTileX();
		final int minTileY = src.getMinTileY();
		final int tileW = src.getTileWidth();
		final int tileH = src.getTileHeight();
		final int maxTileX = minTileX + src.getNumXTiles();
		final int maxTileY = minTileY + src.getNumYTiles();
		int dstTempX = 0;
		int dstTempY = 0;
		for (int ty = minTileY; ty < maxTileY; ty++) {
			dstTempX = 0;
			int actualWidth = 0;
			int actualHeight = 0;
			for (int tx = minTileX; tx < maxTileX; tx++) {
				// get the source raster
				final Raster r = src.getTile(tx, ty);

				int minx = r.getMinX();
				int miny = r.getMinY();
				minx = minx < minx_ ? minx_ : minx;
				miny = miny < miny_ ? miny_ : miny;
				int maxx = minx + tileW;
				int maxy = miny + tileH;
				maxx = maxx > maxx_ ? maxx_ : maxx;
				maxy = maxy > maxy_ ? maxy_ : maxy;
				actualWidth = maxx - minx;
				actualHeight = maxy - miny;
				for (int j = miny, jj = dstTempY; j < maxy; j++, jj++) {
					for (int i = minx, ii = dstTempX; i < maxx; i++, ii++) {
						r.getPixel(i, j, rgba);

						// wr.setPixel(i, j, rgba);

						if (!sourceHasAlpha
								|| !hasAlpha
								|| (sourceHasAlpha && hasAlpha && rgba[alphaBand] >= this.alphaThreshold)) {
							int val = invCM.getIndexNearest(rgba[0] & 0xff,
									rgba[numBands == 1 ? 0 : 1] & 0xff, rgba[numBands == 1 ? 0 : 2]);
							if (hasAlpha && val >= transparencyIndex)
								val++;
							destWr.setSample(ii, jj, 0, (byte) (val & 0xff));
						} else
							destWr.setSample(ii, jj, 0, transparencyIndex);

					}
				}
				dstTempX += actualWidth;

			}
			dstTempY += actualHeight;
		}
		return dest;
	}

	public Rectangle2D getBounds2D(BufferedImage src) {
		return new Rectangle(src.getWidth(), src.getHeight());
	}

	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		if (dstPt == null)
			dstPt = new Point();
		dstPt.setLocation(srcPt);
		return dstPt;
	}

	public RenderingHints getRenderingHints() {
		return null;
	}

	public IndexColorModel getIcm() {
		return icm;
	}

}
