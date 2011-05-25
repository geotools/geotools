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

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

import javax.imageio.ImageTypeSpecifier;

/**
 * This class implements the octree quantization method as it is described in
 * the "Graphics Gems" (ISBN 0-12-286166-3, Chapter 4, pages 297-293)
 * 
 * @author Simone Giannecchini - GeoSolutions
 *
 * @source $URL$
 */
public final class CustomPaletteBuilder {
	/**
	 * Default value for the threshold to decide whether a pixel is opaque (>=)
	 * or transparent (<). Default is 1 to try to preserve antialising
	 */
	public static final int DEFAULT_ALPHA_TH = 1;

	/**
	 * maximum of tree depth
	 */
	protected int maxLevel;

	protected RenderedImage src;

	protected ColorModel srcColorModel;

	protected int requiredSize;

	protected ColorNode root;

	protected int numNodes;

	protected int maxNodes;

	protected int currLevel;

	protected int currSize;

	protected ColorNode[] reduceList;

	protected ColorNode[] palette;

	protected int transparency;

	protected ColorNode transColor;

	protected int subsampleX;

	protected int subsampley;

	protected int numBands;

	protected int alphaThreshold;

	/**
	 * Returns <code>true</code> if PaletteBuilder is able to create palette
	 * for given image type.
	 * 
	 * @param type
	 *            an instance of <code>ImageTypeSpecifier</code> to be
	 *            indexed.
	 * 
	 * @return <code>true</code> if the <code>PaletteBuilder</code> is
	 *         likely to be able to create palette for this image type.
	 * 
	 * @exception IllegalArgumentException
	 *                if <code>type</code> is <code>null</code>.
	 */
	public static boolean canCreatePalette(ImageTypeSpecifier type) {
		if (type == null) {
			throw new IllegalArgumentException("type == null");
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if PaletteBuilder is able to create palette
	 * for given rendered image.
	 * 
	 * @param image
	 *            an instance of <code>RenderedImage</code> to be indexed.
	 * 
	 * @return <code>true</code> if the <code>PaletteBuilder</code> is
	 *         likely to be able to create palette for this image type.
	 * 
	 * @exception IllegalArgumentException
	 *                if <code>image</code> is <code>null</code>.
	 */
	public static boolean canCreatePalette(RenderedImage image) {
		if (image == null) {
			throw new IllegalArgumentException("image == null");
		}

		ImageTypeSpecifier type = new ImageTypeSpecifier(image);

		return canCreatePalette(type);
	}

	public RenderedImage getIndexedImage() {
		// //
		//
		// Create the destination image
		//
		// //
		final IndexColorModel icm = getIndexColorModel();
		final WritableRaster destWr = icm.createCompatibleWritableRaster(src
				.getWidth(), src.getHeight());
		final BufferedImage dst = new BufferedImage(icm, destWr, false, null);

		// //
		//
		// Filter the image out
		//
		// //

		// //
		//
		// Collecting info about the source image
		//
		// //
		final int numBands = src.getSampleModel().getNumBands();
		final int rgba[] = new int[numBands];
		final boolean sourceHasAlpha = (numBands % 2 == 0);
		final int alphaBand = sourceHasAlpha ? numBands - 1 : -1;
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

						destWr.setSample(ii, jj, 0, findColorIndex(root, rgba,
								alphaBand));

					}
				}
				dstTempX += actualWidth;

			}
			dstTempY += actualHeight;
		}
		return dst;
	}

	public CustomPaletteBuilder(RenderedImage src) {
		this(src, 256, 1, 1, DEFAULT_ALPHA_TH);
	}

	public CustomPaletteBuilder(RenderedImage src, int size, int subsx,
			int subsy, int alpha_th) {
		if ((subsx <= 0) || (subsx >= src.getWidth())) {
			throw new IllegalArgumentException("Invalid subsample x size");
		}

		if ((subsy <= 0) || (subsy >= src.getWidth())) {
			throw new IllegalArgumentException("Invalid subsample y size");
		}

		this.alphaThreshold = alpha_th;
		this.src = src;
		this.srcColorModel = src.getColorModel();
		this.numBands = srcColorModel.getNumComponents();
		this.subsampleX = subsx;
		this.subsampley = subsy;
		this.transparency = srcColorModel.getTransparency();
		if (transparency != Transparency.OPAQUE) {
			transparency = Transparency.BITMASK;
			// make room for the transparent color
			this.requiredSize = size - 1;
			transColor = new ColorNode();
			transColor.isLeaf = true;
		} else {
			this.requiredSize = size;
		}

		if (this.requiredSize > 256) {
			throw new IllegalArgumentException(
					"Unvalid number of colors require.");
		}

		this.maxLevel = (int) Math.ceil(Math.log(requiredSize) / Math.log(2));
	}

	protected int findColorIndex(ColorNode aNode, int[] rgba, int transpBand) {
		if ((transparency != Transparency.OPAQUE)
				&& (rgba[transpBand] < alphaThreshold)) {
			return 0; // default transparent pixel
		}

		try {
			if (aNode.isLeaf) {
				return aNode.paletteIndex;
			} else {
				int childIndex = getBranchIndex(rgba, aNode.level);

				if (aNode.children[childIndex] == null) {
					int i = 1;
					for (; i < 8; i++) {
						if (((childIndex + i) < 8)
								&& (aNode.children[childIndex + i] != null)) {
							childIndex += i;

							break;
						}

						if (((childIndex - i) >= 0)
								&& (aNode.children[childIndex - i] != null)) {
							childIndex -= i;

							break;
						}
					}
				}
				return findColorIndex(aNode.children[childIndex], rgba,
						transpBand);
			}
		} catch (Exception e) {
		}
		return 0;
	}

	public CustomPaletteBuilder buildPalette() {
		reduceList = new ColorNode[maxLevel + 1];

		for (int i = 0; i < reduceList.length; i++) {
			reduceList[i] = null;
		}

		numNodes = 0;
		maxNodes = 0;
		root = null;
		currSize = 0;
		currLevel = maxLevel;

		// //
		//
		// Collecting info about the source image
		//
		// //
		final int numBands = src.getSampleModel().getNumBands();
		final int rgba[] = new int[numBands];
		final boolean discriminantTransparency = transparency != Transparency.OPAQUE;
		final int transpBand = numBands - 1;
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
		for (int ty = minTileY; ty < maxTileY; ty++) {
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
				for (int j = miny; j < maxy; j++) {
					if ((subsampley > 1) && ((j % subsampley) != 0)) {
						continue;
					}

					for (int i = minx; i < maxx; i++) {
						if ((subsampleX > 1) && ((i % subsampleX) != 0)) {
							continue;
						}
						r.getPixel(i, j, rgba);
						/*
						 * If transparency of given image is not opaque we
						 * assume all colors with alpha less than 1.0 as fully
						 * transparent.
						 */
						if (discriminantTransparency
								&& (rgba[transpBand] < alphaThreshold)) {
							transColor = insertNode(transColor, rgba, 0);
						} else {

							root = insertNode(root, rgba, 0);
						}

						if (currSize > requiredSize) {
							reduceTree();
						}

					}
				}

			}
		}
		return this;
	}

	protected ColorNode insertNode(ColorNode aNode, int[] rgba, int aLevel) {
		if (aNode == null) {
			aNode = new ColorNode();
			numNodes++;

			if (numNodes > maxNodes) {
				maxNodes = numNodes;
			}

			aNode.level = aLevel;
			aNode.isLeaf = (aLevel > maxLevel);

			if (aNode.isLeaf) {
				currSize++;
			}
		}
		final int numBands = rgba.length;
		aNode.colorCount++;
		aNode.red += rgba[0];
		aNode.green += rgba[numBands == 1 ? 0 : 1];
		aNode.blue += rgba[numBands == 1 ? 0 : 2];

		if (!aNode.isLeaf) {
			int branchIndex = getBranchIndex(rgba, aLevel);

			if (aNode.children[branchIndex] == null) {
				aNode.childCount++;

				if (aNode.childCount == 2) {
					aNode.nextReducible = reduceList[aLevel];
					reduceList[aLevel] = aNode;
				}
			}

			aNode.children[branchIndex] = insertNode(
					aNode.children[branchIndex], rgba, aLevel + 1);
		}

		return aNode;
	}

	public IndexColorModel getIndexColorModel() {
		int size = currSize;

		if (transparency == Transparency.BITMASK) {
			size++; // we need place for transparent color;
		}
		
		// if the palette size happens to be just one (happens with a fully transparent image)
		// then increase the size to two, otherwise the png encoders will go mad
		if(size < 2)
		    size = 2;


		final byte[] red = new byte[size];
		final byte[] green = new byte[size];
		final byte[] blue = new byte[size];

		int index = 0;
		palette = new ColorNode[size];

		if (transparency == Transparency.BITMASK) {
			index++;
		}
		findPaletteEntry(root, index, red, green, blue);
		if (transparency == Transparency.BITMASK) {
			return new IndexColorModel(8, size, red, green, blue, 0);
		}
		return new IndexColorModel(8, currSize, red, green, blue);
	}

	protected int findPaletteEntry(ColorNode aNode, int index, byte[] red,
			byte[] green, byte[] blue) {
		if (aNode == null) {
			return index;
		}

		if (aNode.isLeaf) {
			red[index] = (byte) (aNode.red / aNode.colorCount);
			green[index] = (byte) (aNode.green / aNode.colorCount);
			blue[index] = (byte) (aNode.blue / aNode.colorCount);
			aNode.paletteIndex = index;

			palette[index] = aNode;

			index++;
		} else {
			for (int i = 0; i < 8; i++) {
				if (aNode.children[i] != null) {
					index = findPaletteEntry(aNode.children[i], index, red,
							green, blue);
				}
			}
		}

		return index;
	}

	protected int getBranchIndex(int[] rgba, int aLevel) {
		if ((aLevel > maxLevel) || (aLevel < 0)) {
			throw new IllegalArgumentException("Invalid octree node depth: "
					+ aLevel);
		}
		final int numBands = rgba.length;
		int shift = maxLevel - aLevel;
		int red_index = 0x1 & ((0xff & rgba[0]) >> shift);
		int green_index = 0x1 & ((0xff & rgba[numBands == 1 ? 0 : 1]) >> shift);
		int blue_index = 0x1 & ((0xff & rgba[numBands == 1 ? 0 : 2]) >> shift);
		int index = (red_index << 2) | (green_index << 1) | blue_index;

		return index;
	}

	protected void reduceTree() {
		int level = reduceList.length - 1;
		while ((reduceList[level] == null) && (level >= 0)) {
			level--;
		}

		ColorNode thisNode = reduceList[level];

		if (thisNode == null) {
			// nothing to reduce
			return;
		}

		// look for element with lower color count
		ColorNode pList = thisNode;
		int minColorCount = pList.colorCount;

		int cnt = 1;

		while (pList.nextReducible != null) {
			if (minColorCount > pList.nextReducible.colorCount) {
				thisNode = pList;
				minColorCount = pList.colorCount;
			}

			pList = pList.nextReducible;
			cnt++;
		}

		// save pointer to first reducible node
		// NB: current color count for node could be changed in future
		if (thisNode == reduceList[level]) {
			reduceList[level] = thisNode.nextReducible;
		} else {
			pList = thisNode.nextReducible; // we need to process it
			thisNode.nextReducible = pList.nextReducible;
			thisNode = pList;
		}

		if (thisNode.isLeaf) {
			return;
		}

		// reduce node
		int leafChildCount = thisNode.getLeafChildCount();
		thisNode.isLeaf = true;
		currSize -= (leafChildCount - 1);

		final int aDepth = thisNode.level;
		for (int i = 0; i < 8; i++) {
			thisNode.children[i] = freeTree(thisNode.children[i]);
		}
		thisNode.childCount = 0;
	}

	protected ColorNode freeTree(ColorNode aNode) {
		if (aNode == null) {
			return null;
		}

		for (int i = 0; i < 8; i++) {
			aNode.children[i] = freeTree(aNode.children[i]);
		}

		numNodes--;

		return null;
	}

	/**
	 * The node of color tree.
	 */
	protected class ColorNode {
		public boolean isLeaf;

		public int childCount;

		public ColorNode[] children;

		public int colorCount;

		public long red;

		public long blue;

		public long green;

		public int paletteIndex;

		public int level;

		public ColorNode nextReducible;

		public ColorNode() {
			isLeaf = false;
			level = 0;
			childCount = 0;
			children = new ColorNode[8];

			for (int i = 0; i < 8; i++) {
				children[i] = null;
			}

			colorCount = 0;
			red = green = blue = 0;

			paletteIndex = 0;
		}

		public int getLeafChildCount() {
			if (isLeaf) {
				return 0;
			}

			int cnt = 0;

			for (int i = 0; i < children.length; i++) {
				if (children[i] != null) {
					if (children[i].isLeaf) {
						cnt++;
					} else {
						cnt += children[i].getLeafChildCount();
					}
				}
			}

			return cnt;
		}

		public int getRGB() {
			int r = (int) red / colorCount;
			int g = (int) green / colorCount;
			int b = (int) blue / colorCount;

			int c = (0xff << 24) | ((0xff & r) << 16) | ((0xff & g) << 8)
					| (0xff & b);

			return c;
		}
	}

	public int findNearestColorIndex(int[] rgba, int transparentBand) {
		return findColorIndex(root, rgba, transparentBand);
	}

}
