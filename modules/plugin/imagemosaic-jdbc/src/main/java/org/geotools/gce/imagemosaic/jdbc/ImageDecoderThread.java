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
package org.geotools.gce.imagemosaic.jdbc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

/**
 * This class is used for multithreaded decoding of the tiles read from the
 * database.
 * <p>
 * Decoding errors result in a log message and the result is <code>null</code>.
 * <p>
 * Creating an ImageDocoderThread object with a null or empty bytearray result
 * in a <code>null</code> value for <code>g {@link #getBufferedImage()}</code>
 * <p>
 * 
 * @author mcr
 * 
 */
/**
 * @author christian
 * 
 */
class ImageDecoderThread extends AbstractThread {
	/** Logger. */
	protected final static Logger LOGGER = Logging
			.getLogger(ImageDecoderThread.class.getPackage().getName());

	private byte[] imageBytes;

	private String location;

	private GeneralEnvelope tileEnvelope;

	/**
	 * @param bytes
	 *            the image bytes
	 * @param location
	 *            the tile name
	 * @param tileEnvelope
	 *            the georeferencing information for the tile
	 * @param pixelDimension
	 *            the pixel dimension required
	 * @param requestEnvelope
	 *            the requested envelope
	 * @param levelInfo
	 *            the proper levelInfo
	 * @param tileQueue
	 *            the queue where to put the result
	 * @param config
	 *            the reader config
	 */

	ImageDecoderThread(byte[] bytes, String location,
			GeneralEnvelope tileEnvelope, Rectangle pixelDimension,
			GeneralEnvelope requestEnvelope, ImageLevelInfo levelInfo,
			LinkedBlockingQueue<TileQueueElement> tileQueue, Config config) {
		super(pixelDimension, requestEnvelope, levelInfo, tileQueue, config);

		this.imageBytes = bytes;
		this.location = location;
		this.tileEnvelope = tileEnvelope;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if ((imageBytes == null) || (imageBytes.length == 0)) { // nothing to do
			return;
		}

		try {

			BufferedImage bufferedImage = null;

			boolean triedFromStream = false;
			if (levelInfo.getCanImageIOReadFromInputStream()) {
				bufferedImage = ImageIO.read(new ByteArrayInputStream(
						imageBytes));
				triedFromStream = true;
			}
			if (bufferedImage == null) {
				if (triedFromStream)
					LOGGER.warning("Could not read " + location
							+ " from stream, switch to JAI");
				bufferedImage = readImage2(imageBytes);
			}

			if (requestEnvelope.contains(tileEnvelope, true) == false) {
				GeneralEnvelope savedTileEnvelope = new GeneralEnvelope(
						tileEnvelope);
				tileEnvelope.intersect(requestEnvelope);

				double scaleX = savedTileEnvelope.getSpan(0)
						/ bufferedImage.getWidth();
				double scaleY = savedTileEnvelope.getSpan(1)
						/ bufferedImage.getHeight();
				int x = (int) (Math
						.round((tileEnvelope.getMinimum(0) - savedTileEnvelope
								.getMinimum(0))
								/ scaleX));
				int y = (int) (Math
						.round((savedTileEnvelope.getMaximum(1) - tileEnvelope
								.getMaximum(1))
								/ scaleY));
				int width = (int) (Math.round(bufferedImage.getWidth()
						/ savedTileEnvelope.getSpan(0)
						* tileEnvelope.getSpan(0)));
				int height = (int) (Math.round(bufferedImage.getHeight()
						/ savedTileEnvelope.getSpan(1)
						* tileEnvelope.getSpan(1)));

				if ((width > 0) && (height > 0)) {

					BufferedImage clippedImage = bufferedImage.getSubimage(x,
							y, width, height);

					tileQueue.add(new TileQueueElement(location, clippedImage,
							tileEnvelope));

				}
			} else {
				tileQueue.add(new TileQueueElement(location, bufferedImage,
						tileEnvelope));
			}
		} catch (IOException ex) {
			LOGGER.severe("Decorde error for tile " + location);
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * Fallback Method, in some jre implementations, ImageIO.read(InputStream
	 * in) returns null. If this happens, this method is called, which is not so
	 * efficient but it works
	 * 
	 * @param imageBytes
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImage2(byte[] imageBytes) throws IOException {

		SeekableStream stream = new ByteArraySeekableStream(imageBytes);
		String decoderName = null;

		for (String dn : ImageCodec.getDecoderNames(stream)) {
			decoderName = dn;
			break;
		}

		ImageDecoder decoder = ImageCodec.createImageDecoder(decoderName,
				stream, null);
		PlanarImage img = PlanarImage.wrapRenderedImage(decoder
				.decodeAsRenderedImage());
		return img.getAsBufferedImage();
	}

}
