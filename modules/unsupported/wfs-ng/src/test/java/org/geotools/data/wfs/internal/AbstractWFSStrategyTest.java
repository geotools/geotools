/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2020, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.WFSTestData.url;

import java.io.IOException;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.internal.v2_0.StrictWFS_2_0_Strategy;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.xml.sax.EntityResolver;

/** @author Matthias Schulze - Landesamt für Digitalisierung, Breitband und Vermessung */
public class AbstractWFSStrategyTest {

    /**
     * Test method for {@link
     * org.geotools.data.wfs.internal.AbstractWFSStrategy#splitFilters(javax.xml.namespace.QName,
     * org.opengis.filter.Filter)}.
     */
    @Test
    public void testSplitFilters() throws IOException, ServiceException {
        String baseDirectory = "GeoServer_2.2.x/2.0.0/";
        URL urlCap = url(baseDirectory + "/GetCapabilities.xml");
        HTTPResponse httpResp = new TestHttpResponse("text/xml", "UTF-8", urlCap);
        EntityResolver resolver = null;
        WFSGetCapabilities capabilities =
                new GetCapabilitiesResponse(httpResp, resolver).getCapabilities();
        WFSStrategy strategy = new StrictWFS_2_0_Strategy();
        strategy.setCapabilities(capabilities);

        WFSConfig config = new WFSConfig();
        GetFeatureRequest request = new GetFeatureRequest(config, strategy);

        final QName typeName = request.getTypeName();
        // String resource = "GetFeature_" + typeName.getLocalPart() + ".xml";

        // Filter filter = request.getFilter();
        org.opengis.filter.expression.Expression expr =
                new LiteralExpressionImpl("searchedproperty");
        String pattern = "value*";
        String wildcardMulti = "*";
        String wildcardSingle = "?";
        String escape = "\\";
        Filter filter = new LikeFilterImpl(expr, pattern, wildcardMulti, wildcardSingle, escape);
        Filter[] filters = strategy.splitFilters(typeName, filter);

        Assert.assertNotNull(filters);
        Assert.assertEquals(2, filters.length);
        Assert.assertTrue(filters[0].evaluate(filter));
    }
}
