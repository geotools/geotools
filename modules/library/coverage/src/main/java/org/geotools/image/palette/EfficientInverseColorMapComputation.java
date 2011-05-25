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


/**
 * This class is responsible for computing efficiently an inverse color map for
 * a given color map.
 * 
 * <p>
 * This algorithm is adapted from the algorithm found in Graphics Gems volume 2
 * by Spencer W. Thomas "Efficient Inverse Color Map Computation".
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 *
 * @source $URL$
 */
public final class EfficientInverseColorMapComputation {
	/**
	 * Number of most significant bits we are going to use from the input color
	 * in order to quantize them.
	 */
	protected final int bits;

	protected final int truncationBits;

	protected final int blueQuantizationMask;

	protected final int greenQuantizationMask;

	protected final int redQuantizationMask;

	/**
	 * Forward color map. Is a 3*numcolors array.
	 */
	protected final byte[][] colorMap;

	/**
	 * inverse rgb color map
	 */
	protected final byte[] mapBuf;

	/**
	 * {@link EfficientInverseColorMapComputation} that allows us to specify the
	 * number of bits we are going to save from the quantization.
	 * 
	 * @param rgbColorMap
	 * @param quantizationBits
	 */
	public EfficientInverseColorMapComputation(byte[][] rgbColorMap,
			final int quantizationBits) {
		colorMap = rgbColorMap;
		bits = quantizationBits;
		truncationBits = 8 - bits;
		blueQuantizationMask = (1 << bits) - 1;
		greenQuantizationMask = (blueQuantizationMask << bits);
		redQuantizationMask = (greenQuantizationMask << bits);
		final int maximumQuantizationValue = 1 << bits;
		final int numberOfColors = colorMap[0].length;
		mapBuf = new byte[maximumQuantizationValue * maximumQuantizationValue* maximumQuantizationValue];
		if(mapBuf.length<=0)
			throw new IllegalArgumentException("Illegal number of quantization colors");
		final int[] distBuf = new int[maximumQuantizationValue* maximumQuantizationValue * maximumQuantizationValue];

		final int x = (1 << truncationBits);
		final int xsqr = x * x;
		final int txsqr = xsqr + xsqr;

		// /////////////////////////////////////////////////////////////////////
		//
		// This code visits every cell in the inverse color map for each
		// representative color.
		//
		// /////////////////////////////////////////////////////////////////////
		for (int i = 0; i < numberOfColors; ++i) {

			// //
			//
			// Get the representative color components
			//
			// //
			final int red = colorMap[0][i] & 0xFF;
			final int green = colorMap[1][i] & 0xFF;
			final int blue = colorMap[2][i] & 0xFF;

			// //
			//
			// Distances are measured to the center of each quantized cell, so
			// x/2 is used as the starting point. This is due to the fact that
			// we quantize by performing right shift.
			//
			// //
			final int x_ = x / 2;
			int rdist = red - x_;
			int gdist = green - x_;
			int bdist = blue - x_;
			// distance
			rdist = rdist * rdist + gdist * gdist + bdist * bdist;

			final int rinc = 2 * (xsqr - (red << truncationBits));
			final int ginc = 2 * (xsqr - (green << truncationBits));
			final int binc = 2 * (xsqr - (blue << truncationBits));

			// //
			//
			// Going to check for all the quantized space
			//
			// //
			for (int r = 0, rxx = rinc, rgbI = 0; r < maximumQuantizationValue; rdist += rxx, ++r, rxx += txsqr) {
				gdist = rdist;
				for (int g = 0, gxx = ginc; g < maximumQuantizationValue; gdist += gxx, ++g, gxx += txsqr) {
					bdist = gdist;
					for (int b = 0, bxx = binc; b < maximumQuantizationValue; bdist += bxx, ++b, ++rgbI, bxx += txsqr) {
						if (i == 0 || distBuf[rgbI] > bdist) {
							distBuf[rgbI] = bdist;
							mapBuf[rgbI] = (byte) i;
						}
					}
				}
			}
		}
	}

	/**
	 * This method is responsible for doing the actual lookup that given an rgb
	 * triple returns the best, taking into account quantization, index in the
	 * forward color map.
	 * 
	 * @param red
	 *            component.
	 * @param green
	 *            component.
	 * @param blue
	 *            component.
	 * @return the best, taking into account quantization, index in the forward
	 *         color map for the provided triple.
	 */
	public int getIndexNearest(int red, int green, int blue) {
		return mapBuf[((red << (2 * bits - truncationBits)) & redQuantizationMask)
				+ ((green << (1 * bits - truncationBits)) & greenQuantizationMask)
				+ ((blue >> (truncationBits)) & blueQuantizationMask)] & 0xFF;
	}

}
