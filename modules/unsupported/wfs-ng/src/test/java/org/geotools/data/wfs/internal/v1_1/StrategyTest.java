/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_1;

import static org.geotools.data.wfs.WFSTestData.GEOS_STATES_11;
import static org.geotools.data.wfs.WFSTestData.createTestProtocol;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.wfs.WFSTestData.TestWFSClient;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.ows.ServiceException;
import org.junit.Test;
import org.opengis.filter.Filter;

public class StrategyTest {

    @Test
    public void testResultTypeHits() throws ServiceException, IOException {
        TestWFSClient wfsClient = createTestProtocol(GEOS_STATES_11.CAPABILITIES);
        wfsClient.setProtocol(false);

        GetFeatureRequest request = wfsClient.createGetFeatureRequest();
        request.setTypeName(GEOS_STATES_11.TYPENAME);
        request.setFilter(Filter.INCLUDE);
        request.setResultType(ResultType.HITS);
        URL url = request.getFinalURL();

        // System.out.println(url);
        assertTrue(url.toString().contains("RESULTTYPE=HITS"));
    }
}
