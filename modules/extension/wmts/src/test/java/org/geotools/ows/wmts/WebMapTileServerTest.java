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
package org.geotools.ows.wmts;

import static junit.framework.TestCase.*;
import static org.geotools.ows.wmts.WMTSTestUtils.createCapabilities;

import java.util.Set;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author Emanuele Tajariol (etj at geo-solutions dot it) */
public class WebMapTileServerTest {

    @Test
    public void nasaGetEnvelopeTest() throws Exception {

        WMTSCapabilities caps = createCapabilities("nasa.getcapa.xml");
        WebMapTileServer wmts = new WebMapTileServer(caps);

        Layer layer = (Layer) caps.getLayer("AMSRE_Surface_Rain_Rate_Night");
        // urn:ogc:def:crs:OGC:1.3:CRS84

        // <ows:WGS84BoundingBox crs="urn:ogc:def:crs:OGC:2:84">
        // <ows:LowerCorner>-180 -90</ows:LowerCorner>
        // <ows:UpperCorner>180 90</ows:UpperCorner>
        // </ows:WGS84BoundingBox>

        assertNotNull(wmts.getEnvelope(layer, CRS.decode("urn:ogc:def:crs:OGC:1.3:CRS84")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("CRS:84")));

        assertNull(wmts.getEnvelope(layer, CRS.decode("EPSG:4326")));

        CoordinateReferenceSystem crs = CRS.decode("CRS:84");

        GeneralEnvelope envelope = wmts.getEnvelope(layer, crs);
        assertNotNull(envelope);
        assertEquals(-90.0, envelope.getMinimum(1), 0.0001);
        assertEquals(-180.0, envelope.getMinimum(0), 0.0001);
        assertEquals(90.0, envelope.getMaximum(1), 0.0001);
        assertEquals(180.0, envelope.getMaximum(0), 0.0001);
    }

    @Test
    public void chGetEnvelopeTest() throws Exception {
        WMTSCapabilities caps = createCapabilities("admin_ch.getcapa.xml");
        WebMapTileServer wmts = new WebMapTileServer(caps);

        Layer layer = (Layer) caps.getLayer("ch.are.alpenkonvention");
        // <ows:SupportedCRS>urn:ogc:def:crs:EPSG:2056</ows:SupportedCRS>
        // <ows:WGS84BoundingBox>
        // <ows:LowerCorner>5.140242 45.398181</ows:LowerCorner>
        // <ows:UpperCorner>11.47757 48.230651</ows:UpperCorner>
        // </ows:WGS84BoundingBox>

        assertNotNull(wmts.getEnvelope(layer, CRS.decode("urn:ogc:def:crs:OGC:1.3:CRS84")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("CRS:84")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("EPSG:2056")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("urn:ogc:def:crs:EPSG:2056")));

        assertNull(wmts.getEnvelope(layer, CRS.decode("EPSG:4326")));

        CoordinateReferenceSystem crs = CRS.decode("CRS:84");

        GeneralEnvelope envelope = wmts.getEnvelope(layer, crs);
        assertNotNull(envelope);
        assertEquals(45.398181, envelope.getMinimum(1), 0.0001);
        assertEquals(5.140242, envelope.getMinimum(0), 0.0001);
        assertEquals(48.230651, envelope.getMaximum(1), 0.0001);
        assertEquals(11.47757, envelope.getMaximum(0), 0.0001);
    }

    @Test
    public void testMapRequestCRS() throws Exception {

        WebMapTileServer server = createServer("geodata.nationaalgeoregister.nl.xml");
        WMTSLayer layer = server.getCapabilities().getLayer("brtachtergrondkaartwater");
        ReferencedEnvelope bbox = new ReferencedEnvelope(51, 53, 4, 6, CRS.decode("EPSG:4326"));

        // Try with a CRS that doesn't exist in the layer and expect the first CRS supported by the
        // layer
        GetTileRequest tileRequest1 = server.createGetTileRequest();
        tileRequest1.setLayer(layer);
        tileRequest1.setCRS(CRS.decode("EPSG:4326"));
        tileRequest1.setRequestedBBox(bbox);
        tileRequest1.setRequestedHeight(768);
        tileRequest1.setRequestedWidth(1024);
        Set<Tile> receivedTiles = tileRequest1.getTiles();
        assertTrue(receivedTiles.size() > 0);
        for (Tile t : receivedTiles) {

            String recvdTileCRS =
                    t.getExtent()
                            .getCoordinateReferenceSystem()
                            .getIdentifiers()
                            .toArray()[0]
                            .toString();

            assertEquals("EPSG:25831", recvdTileCRS);
        }

        // Now try with a specific  CRS that isn't the first one declared for the layer and expect
        // that one
        GetTileRequest tileRequest2 = server.createGetTileRequest();
        tileRequest2.setLayer(layer);
        tileRequest2.setCRS(CRS.decode("EPSG:3857"));
        tileRequest2.setRequestedBBox(bbox);
        tileRequest2.setRequestedHeight(768);
        tileRequest2.setRequestedWidth(1024);
        Set<Tile> receivedTiles2 = tileRequest2.getTiles();
        assertTrue(receivedTiles2.size() > 0);
        for (Tile t : receivedTiles2) {
            String recvdTileCRS =
                    t.getExtent()
                            .getCoordinateReferenceSystem()
                            .getIdentifiers()
                            .toArray()[0]
                            .toString();

            assertEquals("EPSG:3857", recvdTileCRS);
        }

        // Now try with a CRS that exists but is in longitude first format
        GetTileRequest tileRequest3 = server.createGetTileRequest();
        tileRequest3.setLayer(layer);
        tileRequest3.setCRS(CRS.decode("EPSG:3857", true));
        tileRequest3.setRequestedBBox(bbox);
        tileRequest3.setRequestedHeight(768);
        tileRequest3.setRequestedWidth(1024);
        Set<Tile> receivedTiles3 = tileRequest3.getTiles();
        assertTrue(receivedTiles3.size() > 0);
        for (Tile t : receivedTiles3) {
            String recvdTileCRS =
                    t.getExtent()
                            .getCoordinateReferenceSystem()
                            .getIdentifiers()
                            .toArray()[0]
                            .toString();

            assertEquals("EPSG:3857", recvdTileCRS);
        }
    }

    private WebMapTileServer createServer(String resourceName) throws Exception {

        WMTSCapabilities capa = createCapabilities(resourceName);
        return new WebMapTileServer(capa);
    }
}
