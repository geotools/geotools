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

import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;

import javax.media.jai.ImageLayout;
import javax.media.jai.PointOpImage;

/**
 * {@link PointOpImage} to perform an efficient color inversion given a certain palette.
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 */
@SuppressWarnings("unchecked")
public class ColorInversion extends PointOpImage {

	
	private InverseColorMapRasterOp op;

	public ColorInversion(RenderedImage image,IndexColorModel icm,
			int quantizationColors, int alpaThreshold) {
		super(image, new ImageLayout(image), null, false);
		this.setSource(image, 0);
		this.op=new InverseColorMapRasterOp(icm,quantizationColors,alpaThreshold);
		
		// setlayout;
		setImageLayout(
				new ImageLayout(image).setColorModel(icm).setSampleModel(icm.createCompatibleSampleModel(image.getWidth(), image.getHeight())));

	}

	@Override
	public Raster computeTile(int tx, int ty) {
		final RenderedImage sourceImage = getSourceImage(0);
		final Raster sourceRaster = sourceImage.getTile(tx, ty);
		return op.filter(sourceRaster, null);
	}

}
