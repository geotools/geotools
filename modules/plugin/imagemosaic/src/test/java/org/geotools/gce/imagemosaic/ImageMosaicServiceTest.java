/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.util.Iterator;

import junit.framework.Assert;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.junit.Test;

/**
 * @author Simone Giannecchini,GeoSolutions
 * 
 *
 * @source $URL$
 */
public class ImageMosaicServiceTest  {
	@Test
	public void isAvailable() {
		final Iterator<GridFormatFactorySpi> list = GridFormatFinder.getAvailableFormats().iterator();
		boolean found = false;
		while (list.hasNext()) {
			final GridFormatFactorySpi fac = (GridFormatFactorySpi) list.next();

			if (fac instanceof ImageMosaicFormatFactory) {
				found = true;

				break;
			}
		}

		Assert.assertTrue("ImageMosaicFormatFactorySpi not registered", found);
	}
}
