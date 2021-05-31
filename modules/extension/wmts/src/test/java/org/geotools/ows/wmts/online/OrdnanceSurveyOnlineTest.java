/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.online;

import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests to check the WMTS capabilities of Ordnance Survey services
 *
 * @author Roar Brænden
 */
public class OrdnanceSurveyOnlineTest extends OnlineTestCase {

    private String wmtsUrl;

    private String key;

    private static Logger LOGGER = Logging.getLogger(OrdnanceSurveyOnlineTest.class);

    @Override
    protected String getFixtureId() {
        return "os-uk-wmts";
    }

    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put(
                "key", "<< You should sign up for a API key at https://osdatahub.os.uk/plans >>");
        example.put("wmts_url", "https://api.os.uk/maps/raster/v1/wmts");

        return example;
    }

    @Override
    protected void setUpInternal() throws Exception {
        wmtsUrl = fixture.getProperty("wmts_url");
        key = fixture.getProperty("key");
    }

    @Test
    public void testWMTSCapabilities() throws Exception {
        URL lowerCaseUrl = new URL(wmtsUrl + "?key=" + key);
        Assert.assertEquals(
                "Get capabilities with lowercase key",
                WMTSServiceType.KVP,
                new WebMapTileServer(lowerCaseUrl).getType());

        URL upperCaseUrl = new URL(wmtsUrl + "?KEY=" + key);
        Assert.assertEquals(
                "Get capabilities with uppercase key",
                WMTSServiceType.KVP,
                new WebMapTileServer(upperCaseUrl).getType());
    }

    @Test
    public void testWMTSTileRequestShouldKeepKey() throws Exception {
        WebMapTileServer tileServer = new WebMapTileServer(new URL(wmtsUrl + "?key=" + key));
        GetTileRequest tileRequest = tileServer.createGetTileRequest();
        URL finalUrl = tileRequest.getFinalURL();
        LOGGER.fine("GetTile finalUrl: " + finalUrl.toString());
        String query = finalUrl.getQuery();

        Assert.assertTrue("Query contains key=", query.contains("key="));
    }
}
