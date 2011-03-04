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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;

public final class InverseColorMapRasterOp implements RasterOp {

	/**
	 * Default number of quantization colors used to build the index for the
	 * inverse color map.
	 */
	public static final int DEFAULT_QUANTIZATION_COLORS = 5;

	/**
	 * Default value for the threshold to decide whether a pixel is opaque (>=)
	 * or transparent (<).
	 */
	public static final int DEFAULT_ALPHA_TH = 1;

	private final IndexColorModel icm;

	private int alphaThreshold;

	private boolean hasAlpha;

	private int transparencyIndex;

	private EfficientInverseColorMapComputation invCM;

	public InverseColorMapRasterOp(final IndexColorModel destCM,
			final int quantizationColors, final int alphaThreshold) {
		this.icm = destCM;
		this.alphaThreshold = alphaThreshold;
		hasAlpha = icm.hasAlpha();
		transparencyIndex = icm.getTransparentPixel();
		final int mapSize = icm.getMapSize();
		final byte[][] colorMap = new byte[3][hasAlpha ? (mapSize - 1)
				: mapSize];

		if (hasAlpha) {
			final byte[] r = new byte[mapSize];
			final byte[] g = new byte[mapSize];
			final byte[] b = new byte[mapSize];
			icm.getReds(r);
			icm.getGreens(g);
			icm.getBlues(b);
			final int reducedMapSize = mapSize - 1;
			if (transparencyIndex == 0) {
				System.arraycopy(r, 1, colorMap[0], 0, reducedMapSize);
				System.arraycopy(g, 1, colorMap[1], 0, reducedMapSize);
				System.arraycopy(b, 1, colorMap[2], 0, reducedMapSize);
			} else if (transparencyIndex == mapSize - 1) {
				System.arraycopy(r, 0, colorMap[0], 0, reducedMapSize);
				System.arraycopy(g, 0, colorMap[1], 0, reducedMapSize);
				System.arraycopy(b, 0, colorMap[2], 0, reducedMapSize);
			} else {
				System.arraycopy(r, 0, colorMap[0], 0, transparencyIndex);
				System.arraycopy(g, 0, colorMap[1], 0, transparencyIndex);
				System.arraycopy(b, 0, colorMap[2], 0, transparencyIndex);

				System.arraycopy(r, transparencyIndex + 1, colorMap[0],
						transparencyIndex, reducedMapSize - transparencyIndex);
				System.arraycopy(g, transparencyIndex + 1, colorMap[1],
						transparencyIndex, reducedMapSize - transparencyIndex);
				System.arraycopy(b, transparencyIndex + 1, colorMap[2],
						transparencyIndex, reducedMapSize - transparencyIndex);
			}
		} else {
			icm.getReds(colorMap[0]);
			icm.getGreens(colorMap[1]);
			icm.getBlues(colorMap[2]);
		}
		invCM = new EfficientInverseColorMapComputation(colorMap,quantizationColors);

	}

	public InverseColorMapRasterOp(final IndexColorModel destCM) {
		this(destCM, DEFAULT_QUANTIZATION_COLORS, DEFAULT_ALPHA_TH);
	}

	public WritableRaster createCompatibleDestRaster(Raster src) {
		return icm.createCompatibleWritableRaster(src
				.getWidth(), src.getHeight()).createWritableTranslatedChild(src.getMinX(), src.getMinY());
	}

	public WritableRaster filter(Raster src, WritableRaster dest) {
		if (dest == null)
			dest = createCompatibleDestRaster(src);
		else {

			if (dest.getSampleModel().getNumBands() != 1)
				throw new IllegalArgumentException(
						"The destination raster for the IverseColorMapRasterOp must one one bad.");
		}
		final int w = dest.getWidth();
		final int h = dest.getHeight();
		final int srcMinX = src.getMinX();
		final int srcMinY = src.getMinY();
		final int srcMaxX = srcMinX + w;
		final int srcMaxY = srcMinY + h;
		final int dstMinX = src.getMinX();
		final int dstMinY = src.getMinY();
		final int numBands = src.getSampleModel().getNumBands();
		final boolean sourceHasAlpha = (numBands % 2 == 0);
		final int alphaBand = sourceHasAlpha ? numBands - 1 : -1;
		final int rgba[] = new int[numBands];
		for (int y = srcMinY, y_ = dstMinY; y < srcMaxY; y++, y_++) {
			for (int x = srcMinX, x_ = dstMinX; x < srcMaxX; x++, x_++) {
				src.getPixel(x, y, rgba);
				if (!sourceHasAlpha
						|| !hasAlpha
						|| (sourceHasAlpha && hasAlpha && rgba[alphaBand] >= this.alphaThreshold)) {
					int val = invCM.getIndexNearest(rgba[0] & 0xff,
							rgba[numBands == 1 ? 0 : 1] & 0xff, rgba[numBands == 1 ? 0 : 2]);
					if (hasAlpha && val >= transparencyIndex)
						val++;
					dest.setSample(x_, y_, 0, (byte) (val & 0xff));
				} else
					dest.setSample(x_, y_, 0, transparencyIndex);

			}
		}
		return dest;

	}

	public Rectangle2D getBounds2D(Raster src) {
		return (Rectangle) src.getBounds().clone();
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

	EfficientInverseColorMapComputation getInvCM() {
		return invCM;
	}

}
