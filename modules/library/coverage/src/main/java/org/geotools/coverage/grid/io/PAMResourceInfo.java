/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import it.geosolutions.imageio.pam.PAMDataset;

/**
 * A {@link org.geotools.data.ResourceInfo} that can return a {@link
 * it.geosolutions.imageio.pam.PAMDataset}
 */
public interface PAMResourceInfo {

    /**
     * Returns a PAMDataset for the resource, if available, or null, otherwise
     *
     * @return
     */
    PAMDataset getPAMDataset();
}
