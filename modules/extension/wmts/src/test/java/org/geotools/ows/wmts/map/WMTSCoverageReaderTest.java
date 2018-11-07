/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.map;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.apache.commons.io.IOUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** @author ian */
public class WMTSCoverageReaderTest {

    private static final String KVP_CAPA_RESOURCENAME = "test-data/getcapa_kvp.xml";

    private static final String REST_CAPA_RESOURCENAME = "test-data/admin_ch.getcapa.xml";

    @Test
    public void testRESTInitMapRequest() throws Exception {
        WebMapTileServer server = createServer(REST_CAPA_RESOURCENAME);
        WMTSLayer layer =
                (WMTSLayer)
                        server.getCapabilities()
                                .getLayer("ch.are.agglomerationen_isolierte_staedte");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox = new ReferencedEnvelope(5, 12, 45, 49, CRS.decode("EPSG:4326"));
        testInitMapRequest(wcr, bbox);
    }

    @Test
    public void testKVPInitMapRequest() throws Exception {
        WebMapTileServer server = createServer(KVP_CAPA_RESOURCENAME);
        WMTSLayer layer = (WMTSLayer) server.getCapabilities().getLayer("topp:states");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        testInitMapRequest(wcr, bbox);
    }

    public void testInitMapRequest(WMTSCoverageReader wcr, ReferencedEnvelope bbox)
            throws Exception {

        int width = 400;
        int height = 200;
        ReferencedEnvelope grid = wcr.initTileRequest(bbox, width, height, null);
        assertNotNull(grid);
        GetTileRequest mapRequest = wcr.getTileRequest();
        mapRequest.setCRS(grid.getCoordinateReferenceSystem());
        Set<Tile> responses = wcr.wmts.issueRequest(mapRequest);
        for (Tile t : responses) {
            /*System.out.println(t);
            // System.out.println(t.getTileIdentifier() + " " + t.getExtent());*/
            assertNotNull(t);
        }
    }

    private WebMapTileServer createServer(String resourceName) throws Exception {

        File capaFile = getRESTgetcapaFile(resourceName);
        WMTSCapabilities capa = createCapabilities(capaFile);
        return new WebMapTileServer(capa);
    }

    private WMTSCapabilities createCapabilities(File capa) throws ServiceException {
        Object object;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(capa);
            Parser parser = new Parser(new WMTSConfiguration());

            object = parser.parse(new InputSource(inputStream));

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw (ServiceException) new ServiceException("Error while parsing XML.").initCause(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        if (object instanceof ServiceException) {
            throw (ServiceException) object;
        }

        return new WMTSCapabilities((CapabilitiesType) object);
    }

    private File getRESTgetcapaFile(String resourceName) {
        try {
            URL capaResource = getClass().getClassLoader().getResource(resourceName);
            assertNotNull("Can't find getCapa resource " + resourceName, capaResource);
            File capaFile = new File(capaResource.toURI());
            assertTrue("Can't find getCapa file", capaFile.exists());

            return capaFile;
        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }
}
