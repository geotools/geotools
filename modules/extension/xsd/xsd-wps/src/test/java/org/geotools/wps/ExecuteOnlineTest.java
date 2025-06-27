/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ExecuteOnlineTest {

    static final Logger LOGGER = Logging.getLogger(ExecuteOnlineTest.class);

    /*
     * Try doing an execute request from the example xsd and parse it
     */
    @Test
    public void testExecute() throws IOException, SAXException, ParserConfigurationException {
        URL url = new URL("http://schemas.opengis.net/wps/1.0.0/examples/51_wpsExecute_request_ResponseDocument.xml");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            Parser parser = new Parser(new WPSConfiguration());
            Object obj = parser.parse(in);
            Assert.assertNotNull(obj);
        } catch (UnknownHostException notFound) {
            LOGGER.warning(notFound.getClass().getName() + ":" + notFound.getMessage());
            LOGGER.warning("the server was not found - you may be running"
                    + "in offline mode - or behind a firewall?"
                    + "in anycase this represents a failure of network"
                    + "and not our software");
        }
    }
}
