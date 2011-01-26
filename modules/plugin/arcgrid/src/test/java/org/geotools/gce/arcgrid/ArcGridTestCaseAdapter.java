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
 *
 */
package org.geotools.gce.arcgrid;

import java.io.File;

/**
 * {@link ArcGridBaseTestCase} adapter.
 * @author Simone Giannecchini
 * @since 2.3.x
 *
 *
 * @source $URL$
 */
public class ArcGridTestCaseAdapter extends ArcGridBaseTestCase {

	/**
	 * @param name
	 */
	public ArcGridTestCaseAdapter(String name) {
		super(name);
	
	}

	/* (non-Javadoc)
	 * @see org.geotools.gce.arcgrid.ArcGridBaseTestCase#runMe(java.io.File)
	 */
	public void runMe(File file) throws Exception {
		

	}

}
