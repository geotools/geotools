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
package org.geotools.coverageio.gdal;

import it.geosolutions.imageio.pam.PAMDataset;
import org.geotools.api.data.ResourceInfo;
import org.geotools.coverage.grid.io.PAMResourceInfo;
import org.geotools.data.DefaultResourceInfo;

public class GDALResourceInfo extends DefaultResourceInfo implements PAMResourceInfo {

    PAMDataset pamDataset;

    public GDALResourceInfo(ResourceInfo resourceInfo) {
        super(resourceInfo);
        if (resourceInfo instanceof GDALResourceInfo info) {
            this.pamDataset = info.getPAMDataset();
        }
    }

    public GDALResourceInfo() {
        super();
    }

    public void setPAMDataset(PAMDataset pamDataset) {
        this.pamDataset = pamDataset;
    }

    @Override
    public PAMDataset getPAMDataset() {
        return pamDataset;
    }
}
