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
package org.geotools.data.wfs.internal.v2_0;

import java.util.logging.Logger;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.util.logging.Logging;

/** @author ian */
public class ArcGisStrategy_2_0 extends StrictWFS_2_0_Strategy {
    private static final Logger LOGGER = Logging.getLogger(StrictWFS_2_0_Strategy.class);
    /** */
    public ArcGisStrategy_2_0() {
        super();
        LOGGER.warning(
                "This ArcGIS server WFS strategy is unsupported - consider using the better tested (and supported) version 1.0.0.");
    }
    /**
     * Pull Parser doesn't manage to parse feature count from response, so asking for HITS is a
     * waste of time currently.
     */
    @Override
    public boolean supports(final ResultType resultType) {
        return false;
    }
}
