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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.namespace.QName;
import org.apache.commons.io.IOUtils;
import org.geotools.api.filter.Filter;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.internal.v1_x.StrictWFS_1_x_Strategy;
import org.geotools.data.wfs.internal.v2_0.StrictWFS_2_0_Strategy;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.EntityResolver;

/** @author Matthias Schulze - Landesamt für Digitalisierung, Breitband und Vermessung */
public class AbstractWFSStrategyTest {

    /**
     * Test method for {@link org.geotools.data.wfs.internal.AbstractWFSStrategy#splitFilters(javax.xml.namespace.QName,
     * org.geotools.api.filter.Filter)}.
     */
    @Test
    public void testSplitFilters() throws IOException, ServiceException {
        String baseDirectory = "GeoServer_2.2.x/2.0.0/";
        URL urlCap = url(baseDirectory + "/GetCapabilities.xml");
        HTTPResponse httpResp = new TestHttpResponse("text/xml", "UTF-8", urlCap);
        EntityResolver resolver = null;
        WFSGetCapabilities capabilities = new GetCapabilitiesResponse(httpResp, resolver).getCapabilities();
        WFSStrategy strategy = new StrictWFS_2_0_Strategy();
        strategy.setCapabilities(capabilities);

        WFSConfig config = new WFSConfig();
        GetFeatureRequest request = new GetFeatureRequest(config, strategy);

        final QName typeName = request.getTypeName();
        // String resource = "GetFeature_" + typeName.getLocalPart() + ".xml";

        // Filter filter = request.getFilter();
        org.geotools.api.filter.expression.Expression expr = new LiteralExpressionImpl("searchedproperty");
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

    /**
     * Test method for {@link org.geotools.data.wfs.internal.AbstractWFSStrategy#buildUrlGET(WFSRequest)}, to check if
     * it produces the <code>STARTINDEX</code> query parameter also for a WFS 1.X request.
     */
    @Test
    public void testWfs1_XUrlBuildingWithStartIndexParameter() throws Exception {

        WFSStrategy strategy = new StrictWFS_1_x_Strategy();

        HTTPResponse httpResponse =
                new TestHttpResponse("text/xml", "UTF-8", url("GeoServer_1.7.x/1.1.0/GetCapabilities.xml"));
        WFSGetCapabilities capabilities = new GetCapabilitiesResponse(httpResponse, null).getCapabilities();
        strategy.setCapabilities(capabilities);

        GetFeatureRequest request = new GetFeatureRequest(new WFSConfig(), strategy);

        request.setTypeName(new QName("example"));
        request.setFilter(Filter.INCLUDE);
        request.setStartIndex(100);

        URL url = strategy.buildUrlGET(request);

        Assert.assertFalse(url.getQuery().contains("STARTINDEX"));
    }

    /**
     * Test method for {@link org.geotools.data.wfs.internal.AbstractWFSStrategy#buildUrlGET(WFSRequest)}, to check if
     * it produces the <code>STARTINDEX</code> query parameter for a WFS 2.0.0 request.
     */
    @Test
    public void testWfs2_0UrlBuildingWithStartIndexParameter() throws Exception {

        WFSStrategy strategy = new StrictWFS_2_0_Strategy();

        HTTPResponse httpResponse =
                new TestHttpResponse("text/xml", "UTF-8", url("GeoServer_2.2.x/2.0.0/GetCapabilities.xml"));
        WFSGetCapabilities capabilities = new GetCapabilitiesResponse(httpResponse, null).getCapabilities();
        strategy.setCapabilities(capabilities);

        GetFeatureRequest request = new GetFeatureRequest(new WFSConfig(), strategy);

        request.setTypeName(new QName("example"));
        request.setFilter(Filter.INCLUDE);
        request.setStartIndex(100);

        URL url = strategy.buildUrlGET(request);

        Assert.assertTrue(url.getQuery().contains("STARTINDEX=100"));
    }

    /**
     * Test method for {@link org.geotools.data.wfs.internal.AbstractWFSStrategy#getPostContents(WFSRequest)}, to check
     * if it does not produce the <code>startIndex</code> parameter for a WFS 1.X request, since it is not supported.
     */
    @Test
    public void testWfs1_XPostContentCreationWithStartIndexParameter() throws Exception {

        WFSStrategy strategy = new StrictWFS_1_x_Strategy();

        HTTPResponse httpResponse =
                new TestHttpResponse("text/xml", "UTF-8", url("GeoServer_1.7.x/1.1.0/GetCapabilities.xml"));
        WFSGetCapabilities capabilities = new GetCapabilitiesResponse(httpResponse, null).getCapabilities();
        strategy.setCapabilities(capabilities);

        GetFeatureRequest request = new GetFeatureRequest(new WFSConfig(), strategy);

        request.setTypeName(new QName("http://www.openplans.org/topp", "states", "topp"));
        request.setFilter(Filter.INCLUDE);
        request.setStartIndex(100);
        request.setMaxFeatures(222222);

        try (InputStream postContents = strategy.getPostContents(request)) {
            String postContentsString =
                    String.join("\n", IOUtils.readLines(new InputStreamReader(postContents, StandardCharsets.UTF_8)));
            Assert.assertFalse(postContentsString.contains("startIndex"));
        }
    }

    /**
     * Test method for {@link org.geotools.data.wfs.internal.AbstractWFSStrategy#getPostContents(WFSRequest)}, to check
     * if it produces the <code>startIndex</code> parameter for a WFS 2.0.0 request.
     */
    @Test
    public void testWfs2_0PostContentCreationWithStartIndexParameter() throws Exception {

        WFSStrategy strategy = new StrictWFS_2_0_Strategy();

        HTTPResponse httpResponse =
                new TestHttpResponse("text/xml", "UTF-8", url("GeoServer_2.2.x/2.0.0/GetCapabilities.xml"));
        WFSGetCapabilities capabilities = new GetCapabilitiesResponse(httpResponse, null).getCapabilities();
        strategy.setCapabilities(capabilities);

        GetFeatureRequest request = new GetFeatureRequest(new WFSConfig(), strategy);

        request.setTypeName(new QName("http://www.openplans.org/topp", "states", "topp"));
        request.setFilter(Filter.INCLUDE);
        request.setStartIndex(100);
        request.setMaxFeatures(222222);

        try (InputStream postContents = strategy.getPostContents(request)) {
            String postContentsString =
                    String.join("\n", IOUtils.readLines(new InputStreamReader(postContents, StandardCharsets.UTF_8)));
            Assert.assertTrue(postContentsString.contains("startIndex=\"100\""));
        }
    }
}
