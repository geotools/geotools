/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_x;

import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;

/** @author ian */
public class ArcGisStrategy_1_X extends StrictWFS_1_x_Strategy {
    // this fixes the issue where ArcGis returns the full count when asked for a filtered and
    // limited HITs count
    @Override
    public boolean supports(final ResultType resultType) {
        switch (resultType) {
            case RESULTS:
                return true;
            case HITS:
            default:
                return false;
        }
    }

    @Override
    public boolean canLimit() {
        return false;
    }
}
