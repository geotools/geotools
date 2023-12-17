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
package org.geotools.gce.geotiff;

import it.geosolutions.imageio.pam.PAMDataset;
import java.util.List;
import org.geotools.coverage.grid.io.PAMResourceInfo;
import org.geotools.data.DefaultFileResourceInfo;

/** ResouceInfo for GeoTIFF files */
class GeoTIFFResourceInfo extends DefaultFileResourceInfo implements PAMResourceInfo {

    private final PAMDataset pam;

    public GeoTIFFResourceInfo(List<FileGroup> files, PAMDataset pam) {
        super(files);
        this.pam = pam;
    }

    @Override
    public PAMDataset getPAMDataset() {
        return pam;
    }
}
