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
package org.geotools.utils;

import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.event.IIOWriteWarningListener;

/**
 * Simple adapter for {@link IIOWriteProgressListener} and
 * {@link IIOWriteWarningListener}.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @see IIOWriteProgressListener
 * @see IIOWriteWarningListener
 * 
 *
 * @source $URL$
 */
public class WriteProgressListenerAdapter implements IIOWriteProgressListener,
		IIOWriteWarningListener {

	public void imageStarted(ImageWriter source, int imageIndex) {
	}

	public void imageProgress(ImageWriter source, float percentageDone) {
	}

	public void imageComplete(ImageWriter source) {
	}

	public void thumbnailStarted(ImageWriter source, int imageIndex,
			int thumbnailIndex) {
	}

	public void thumbnailProgress(ImageWriter source, float percentageDone) {
	}

	public void thumbnailComplete(ImageWriter source) {
	}

	public void writeAborted(ImageWriter source) {
	}

	public void warningOccurred(ImageWriter source, int imageIndex,
			String warning) {
	}

}
