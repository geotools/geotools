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
package org.geotools.coverage.grid.io.imageio;

import java.awt.Point;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.media.jai.CachedTile;
import javax.media.jai.TileCache;
import javax.media.jai.TileFactory;
import javax.media.jai.TileRecycler;

import com.sun.media.jai.util.DataBufferUtils;

/**
 * A simple implementation of <code>TileFactory</code> wherein the tiles
 * returned from <code>createTile()</code> attempt to re-use primitive arrays
 * provided by the <code>TileRecycler</code> method <code>recycleTile()</code>.
 * 
 * <p>
 * A simple example of the use of this class is as follows wherein image files
 * are read, each image is filtered, and each output written to a file:
 * 
 * <pre>
 * String[] sourceFiles; // source file paths
 * KernelJAI kernel; // filtering kernel
 * 
 * // Create a RenderingHints object and set hints.
 * RenderingHints rh = new RenderingHints(null);
 * RecyclingTileFactory rtf = new RecyclingTileFactory();
 * rh.put(JAI.KEY_TILE_RECYCLER, rtf);
 * rh.put(JAI.KEY_TILE_FACTORY, rtf);
 * rh.put(JAI.KEY_IMAGE_LAYOUT, new ImageLayout().setTileWidth(32).setTileHeight(
 * 		32));
 * 
 * int counter = 0;
 * 
 * // Read each image, filter it, and save the output to a file.
 * for (int i = 0; i &lt; sourceFiles.length; i++) {
 * 	PlanarImage source = JAI.create(&quot;fileload&quot;, sourceFiles[i]);
 * 	ParameterBlock pb = (new ParameterBlock()).addSource(source).add(kernel);
 * 
 * 	// The TileFactory hint will cause tiles to be created by 'rtf'.
 * 	RenderedOp dest = JAI.create(&quot;convolve&quot;, pb, rh);
 * 	String fileName = &quot;image_&quot; + (++counter) + &quot;.tif&quot;;
 * 	JAI.create(&quot;filestore&quot;, dest, fileName);
 * 
 * 	// The TileRecycler hint will cause arrays to be reused by 'rtf'.
 * 	dest.dispose();
 * }
 * </pre>
 * 
 * In the above code, if the <code>SampleModel</code> of all source images is
 * identical, then data arrays should only be created in the first iteration.
 * </p>
 * 
 * @since JAI 1.1.2
 *
 * @source $URL$
 */
public class RecyclingTileFactory extends Observable implements TileFactory,
		TileRecycler, Observer {

	private static final boolean DEBUG = false;

	/**
	 * Cache of recycled arrays. The key in this mapping is a <code>Long</code>
	 * which is formed for a given two-dimensional array as
	 * 
	 * <pre>
	 * long type; // DataBuffer.TYPE_*
	 * 
	 * long numBanks; // Number of banks
	 * 
	 * long size; // Size of each bank
	 * 
	 * Long key = new Long((type &lt;&lt; 56) | (numBanks &lt;&lt; 32) | size);
	 * </pre>
	 * 
	 * where the value of <code>type</code> is one of the constants
	 * <code>DataBuffer.TYPE_*</code>. The value corresponding to each key is
	 * an <code>ArrayList</code> of <code>SoftReferences</code> to the
	 * internal data banks of <code>DataBuffer</code>s of tiles wherein the
	 * data bank array has the type and dimensions implied by the key.
	 */
	private HashMap recycledArrays = new HashMap(32);

	/**
	 * The amount of memory currrently used for array storage.
	 */
	private long memoryUsed = 0L;

	// XXX Inline this method or make it public?
	private static long getBufferSizeCSM(ComponentSampleModel csm) {
		int[] bandOffsets = csm.getBandOffsets();
		int maxBandOff = bandOffsets[0];
		for (int i = 1; i < bandOffsets.length; i++)
			maxBandOff = Math.max(maxBandOff, bandOffsets[i]);

		long size = 0;
		if (maxBandOff >= 0)
			size += maxBandOff + 1;
		int pixelStride = csm.getPixelStride();
		if (pixelStride > 0)
			size += pixelStride * (csm.getWidth() - 1);
		int scanlineStride = csm.getScanlineStride();
		if (scanlineStride > 0)
			size += scanlineStride * (csm.getHeight() - 1);
		return size;
	}

	// XXX Inline this method or make it public?
	private static long getNumBanksCSM(ComponentSampleModel csm) {
		int[] bankIndices = csm.getBankIndices();
		int maxIndex = bankIndices[0];
		for (int i = 1; i < bankIndices.length; i++) {
			int bankIndex = bankIndices[i];
			if (bankIndex > maxIndex) {
				maxIndex = bankIndex;
			}
		}
		return maxIndex + 1;
	}

	/** Tile cache I observe. */
	private final Observable tileCache;

	/**
	 * Returns a <code>SoftReference</code> to the internal bank data of the
	 * <code>DataBuffer</code>.
	 */
	private static Object getBankReference(DataBuffer db) {
		Object array = null;

		switch (db.getDataType()) {
		case DataBuffer.TYPE_BYTE:
			array = ((DataBufferByte) db).getBankData();
			break;
		case DataBuffer.TYPE_USHORT:
			array = ((DataBufferUShort) db).getBankData();
			break;
		case DataBuffer.TYPE_SHORT:
			array = ((DataBufferShort) db).getBankData();
			break;
		case DataBuffer.TYPE_INT:
			array = ((DataBufferInt) db).getBankData();
			break;
		case DataBuffer.TYPE_FLOAT:
			array = DataBufferUtils.getBankDataFloat(db);
			break;
		case DataBuffer.TYPE_DOUBLE:
			array = DataBufferUtils.getBankDataDouble(db);
			break;
		default:
			throw new UnsupportedOperationException("");

		}

		return array;
	}

	/**
	 * Returns the amount of memory (in bytes) used by the supplied data bank
	 * array.
	 */
	private static long getDataBankSize(int dataType, int numBanks, int size) {
		int bytesPerElement = 0;
		switch (dataType) {
		case DataBuffer.TYPE_BYTE:
			bytesPerElement = 1;
			break;
		case DataBuffer.TYPE_USHORT:
		case DataBuffer.TYPE_SHORT:
			bytesPerElement = 2;
			break;
		case DataBuffer.TYPE_INT:
		case DataBuffer.TYPE_FLOAT:
			bytesPerElement = 4;
			break;
		case DataBuffer.TYPE_DOUBLE:
			bytesPerElement = 8;
			break;
		default:
			throw new UnsupportedOperationException("");

		}

		return numBanks * size * bytesPerElement;
	}

	/**
	 * Constructs a <code>RecyclingTileFactory</code>.
	 */
	public RecyclingTileFactory(Observable tileCache) {
		if (tileCache instanceof TileCache)
			this.tileCache = tileCache;
		else
			throw new IllegalArgumentException(
					"Provided argument is not of type TileCache");
		tileCache.addObserver(this);
	}

	/**
	 * Returns <code>true</code>.
	 */
	public boolean canReclaimMemory() {
		return true;
	}

	/**
	 * Returns <code>true</code>.
	 */
	public boolean isMemoryCache() {
		return true;
	}

	public long getMemoryUsed() {
		return memoryUsed;
	}

	public void flush() {
		synchronized (recycledArrays) {
			recycledArrays.clear();
			memoryUsed = 0L;
		}
	}

	public WritableRaster createTile(SampleModel sampleModel, Point location) {

		if (sampleModel == null) {
			throw new IllegalArgumentException("sampleModel == null!");
		}

		if (location == null) {
			location = new Point(0, 0);
		}

		DataBuffer db = null;

		int type = sampleModel.getTransferType();
		long numBanks = 0;
		long size = 0;

		if (sampleModel instanceof ComponentSampleModel) {
			ComponentSampleModel csm = (ComponentSampleModel) sampleModel;
			numBanks = getNumBanksCSM(csm);
			size = getBufferSizeCSM(csm);
		} else if (sampleModel instanceof MultiPixelPackedSampleModel) {
			MultiPixelPackedSampleModel mppsm = (MultiPixelPackedSampleModel) sampleModel;
			numBanks = 1;
			int dataTypeSize = DataBuffer.getDataTypeSize(type);
			size = mppsm.getScanlineStride() * mppsm.getHeight()
					+ (mppsm.getDataBitOffset() + dataTypeSize - 1)
					/ dataTypeSize;
		} else if (sampleModel instanceof SinglePixelPackedSampleModel) {
			SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel) sampleModel;
			numBanks = 1;
			size = sppsm.getScanlineStride() * (sppsm.getHeight() - 1)
					+ sppsm.getWidth();
		}

		if (size != 0) {
			Object array = getRecycledArray(type, numBanks, size);
			if (array != null) {
				switch (type) {
				case DataBuffer.TYPE_BYTE: {
					byte[][] bankData = (byte[][]) array;
					for (int i = 0; i < numBanks; i++) {
						Arrays.fill(bankData[i], (byte) 0);
					}
					db = new DataBufferByte(bankData, (int) size);
				}
					break;
				case DataBuffer.TYPE_USHORT: {
					short[][] bankData = (short[][]) array;
					for (int i = 0; i < numBanks; i++) {
						Arrays.fill(bankData[i], (short) 0);
					}
					db = new DataBufferUShort(bankData, (int) size);
				}
					break;
				case DataBuffer.TYPE_SHORT: {
					short[][] bankData = (short[][]) array;
					for (int i = 0; i < numBanks; i++) {
						Arrays.fill(bankData[i], (short) 0);
					}
					db = new DataBufferShort(bankData, (int) size);
				}
					break;
				case DataBuffer.TYPE_INT: {
					int[][] bankData = (int[][]) array;
					for (int i = 0; i < numBanks; i++) {
						Arrays.fill(bankData[i], 0);
					}
					db = new DataBufferInt(bankData, (int) size);
				}
					break;
				case DataBuffer.TYPE_FLOAT: {
					float[][] bankData = (float[][]) array;
					for (int i = 0; i < numBanks; i++) {
						Arrays.fill(bankData[i], 0.0F);
					}
					db = DataBufferUtils.createDataBufferFloat(bankData,
							(int) size);
				}
					break;
				case DataBuffer.TYPE_DOUBLE: {
					double[][] bankData = (double[][]) array;
					for (int i = 0; i < numBanks; i++) {
						Arrays.fill(bankData[i], 0.0);
					}
					db = DataBufferUtils.createDataBufferDouble(bankData,
							(int) size);
				}
					break;
				default:
					throw new IllegalArgumentException("");
				}

				if (DEBUG) {
					System.out.println(getClass().getName()
							+ " Using a recycled array");// XXX
					// (new Throwable()).printStackTrace(); // XXX
				}
			} else if (DEBUG) {
				System.out.println(getClass().getName() + " No type " + type
						+ " array[" + numBanks + "][" + size + "] available");
			}
		} else if (DEBUG) {
			System.out.println(getClass().getName() + " Size is zero");
		}

		if (db == null) {
			if (DEBUG) {
				System.out.println(getClass().getName()
						+ " Creating new DataBuffer");// XXX
			}
			// (new Throwable()).printStackTrace(); // XXX
			db = sampleModel.createDataBuffer();
		}

		return Raster.createWritableRaster(sampleModel, db, location);
	}

	/**
	 * Recycle the given tile.
	 */
	public void recycleTile(Raster tile) {
		DataBuffer db = tile.getDataBuffer();

		Long key = new Long(((long) db.getDataType() << 56)
				| ((long) db.getNumBanks() << 32) | (long) db.getSize());

		if (DEBUG) {
			System.out.println("Recycling array for: " + db.getDataType() + " "
					+ db.getNumBanks() + " " + db.getSize());
			// System.out.println("recycleTile(); key = "+key);
		}

		synchronized (recycledArrays) {
			Object value = recycledArrays.get(key);
			ArrayList arrays = null;
			if (value != null) {
				arrays = (ArrayList) value;
			} else {
				arrays = new ArrayList(10);
			}

			memoryUsed += getDataBankSize(db.getDataType(), db.getNumBanks(),
					db.getSize());

			arrays.add(getBankReference(db));

			if (value == null) {
				recycledArrays.put(key, arrays);
			}
		}
	}

	/**
	 * Retrieve an array of the specified type and length.
	 */
	private Object getRecycledArray(int arrayType, long numBanks,
			long arrayLength) {
		Long key = new Long(((long) arrayType << 56) | numBanks << 32
				| arrayLength);

		if (DEBUG) {
			System.out.println("Attempting to get array for: " + arrayType
					+ " " + numBanks + " " + arrayLength);
			// System.out.println("Attempting to get array for key "+key);
		}

		synchronized (recycledArrays) {
			Object value = recycledArrays.get(key);

			if (value != null) {
				ArrayList arrays = (ArrayList) value;
				for (int idx = arrays.size() - 1; idx >= 0; idx--) {
					Object array = arrays.remove(idx);
					memoryUsed -= getDataBankSize(arrayType, (int) numBanks,
							(int) arrayLength);
					if (idx == 0) {
						recycledArrays.remove(key);
					}

					if (array != null) {
						if (DEBUG)
							System.out.println("good reference");
						return array;
					}

					if (DEBUG)
						System.out.println("null reference");
				}
			}
		}

		// if(DEBUG) System.out.println("getRecycledArray() returning "+array);

		return null;
	}

	public void update(Observable o, Object arg) {
		if (o.equals(tileCache)) {
			if (arg instanceof CachedTile) {
				CachedTile tile = (CachedTile) arg;
				switch (tile.getAction()) {
				case 1:
				case 2:// REMOVE,REMOVE_FROM_FLUSH
					this.recycleTile(tile.getTile());
				}
			}
		}
	}
}
