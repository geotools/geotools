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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;

/**
 * This class reads decoded tiles from the queue and performs the mosaicing and
 * scaling
 * 
 * @author mcr
 * 
 */
class ImageComposerThread extends AbstractThread {
	/** Logger. */
	protected final static Logger LOGGER = Logging
			.getLogger(ImageComposerThread.class.getPackage().getName());

	protected GridCoverageFactory coverageFactory;

	private GridCoverage2D gridCoverage2D;

	private Color outputTransparentColor;

	private boolean xAxisSwitch;

	ImageComposerThread(Color outputTransparentColor, Rectangle pixelDimension,
			GeneralEnvelope requestEnvelope, ImageLevelInfo levelInfo,
			LinkedBlockingQueue<TileQueueElement> tileQueue, Config config,
			boolean xAxisSwitch, GridCoverageFactory coverageFactory) {
		super(pixelDimension, requestEnvelope, levelInfo, tileQueue, config);
		this.outputTransparentColor = outputTransparentColor;
		this.xAxisSwitch = xAxisSwitch;
		this.coverageFactory = coverageFactory;
	}

	private Dimension getStartDimension() {
		double width;
		double height;

		width = pixelDimension.getWidth() / rescaleX;
		height = pixelDimension.getHeight() / rescaleY;

		return new Dimension((int) Math.round(width), (int) Math.round(height));
	}

	private BufferedImage getStartImage(BufferedImage copyFrom) {
		Dimension dim = getStartDimension();

		int imageType = copyFrom.getType();
		if (imageType == BufferedImage.TYPE_CUSTOM)
			imageType = ImageMosaicJDBCReader.DEFAULT_IMAGE_TYPE;

		BufferedImage image = null;
		if ((imageType == BufferedImage.TYPE_BYTE_BINARY || imageType == BufferedImage.TYPE_BYTE_INDEXED)
				&& (copyFrom.getColorModel() instanceof IndexColorModel))
			image = new BufferedImage((int) dim.getWidth(), (int) dim
					.getHeight(), imageType, (IndexColorModel) copyFrom
					.getColorModel());
		else
			image = new BufferedImage((int) dim.getWidth(), (int) dim
					.getHeight(), imageType);

		Graphics2D g2D = (Graphics2D) image.getGraphics();
		Color save = g2D.getColor();
		g2D.setColor(outputTransparentColor);
		g2D.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2D.setColor(save);

		return image;
	}

	private BufferedImage getStartImage(int imageType) {
		Dimension dim = getStartDimension();

		if (imageType == BufferedImage.TYPE_CUSTOM)
			imageType = ImageMosaicJDBCReader.DEFAULT_IMAGE_TYPE;

		BufferedImage image = new BufferedImage((int) dim.getWidth(), (int) dim
				.getHeight(), imageType);

		Graphics2D g2D = (Graphics2D) image.getGraphics();
		Color save = g2D.getColor();
		g2D.setColor(outputTransparentColor);
		g2D.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2D.setColor(save);

		return image;
	}

	@Override
	public void run() {
		BufferedImage image = null;
		Graphics2D g2D = null;

		TileQueueElement queueObject = null;

		try {
			while ((queueObject = tileQueue.take()).isEndElement() == false) {

				if (image == null) {
					image = getStartImage(queueObject.getTileImage());
					g2D = (Graphics2D) image.getGraphics();
				}

				int posx = (int) ((queueObject.getEnvelope().getMinimum(0) - requestEnvelope
						.getMinimum(0)) / levelInfo.getResX());
				int posy = (int) ((requestEnvelope.getMaximum(1) - queueObject
						.getEnvelope().getMaximum(1)) / levelInfo.getResY());
				g2D.drawImage(queueObject.getTileImage(), AffineTransform
						.getTranslateInstance(posx, posy), null);

			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (image == null) // no tiles ??
			image = getStartImage(ImageMosaicJDBCReader.DEFAULT_IMAGE_TYPE);

		GeneralEnvelope resultEnvelope = null;

		if (xAxisSwitch) {
			Rectangle2D tmp = new Rectangle2D.Double(requestEnvelope
					.getMinimum(1), requestEnvelope.getMinimum(0),
					requestEnvelope.getSpan(1), requestEnvelope.getSpan(0));
			resultEnvelope = new GeneralEnvelope(tmp);
			resultEnvelope.setCoordinateReferenceSystem(requestEnvelope
					.getCoordinateReferenceSystem());
		} else {
			resultEnvelope = requestEnvelope;
		}

		gridCoverage2D = coverageFactory.create(config.getCoverageName(),
				rescaleImage(image), resultEnvelope);
	}

	GridCoverage2D getGridCoverage2D() {
		return gridCoverage2D;
	}
}
