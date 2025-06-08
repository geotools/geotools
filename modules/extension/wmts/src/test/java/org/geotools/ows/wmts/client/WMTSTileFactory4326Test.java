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
package org.geotools.ows.wmts.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.Tile;
import org.geotools.tile.Tile.RenderState;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WMTSTileFactory4326Test {

    protected WMTSTileFactory factory;

    @Before
    public void setUp() {
        this.factory = createFactory();
    }

    static class TestPoint {
        double lat;

        double lon;

        int zoomlevel;

        int expectedMRow;

        int expectedMCol;

        int expectedLRow;

        int expectedLCol;

        public TestPoint(
                double lat,
                double lon,
                int zoomlevel,
                int expectedMatrixCol,
                int expectedMatrixRow,
                int expectedLimitedCol,
                int expectedLimitedRow) {
            super();
            this.lat = lat;
            this.lon = lon;
            this.zoomlevel = zoomlevel;
            this.expectedMRow = expectedMatrixRow;
            this.expectedMCol = expectedMatrixCol;

            this.expectedLRow = expectedLimitedRow;
            this.expectedLCol = expectedLimitedCol;
        }
    }

    @Test
    public void testCreateTileFromXYZoom() throws Exception {
        Tile kvpTile = createTileFromXYZoom(createKVPService());
        Assert.assertEquals(
                new URL(
                        "http://demo.geo-solutions.it/geoserver/gwc/service/wmts?REQUEST=getcapabilities&tilematrixset=EPSG%3A4326&TileRow=15&service=WMTS&format=image%2Fpng&TileCol=20&version=1.0.0&layer=unesco%3AUnesco_point&TileMatrix=EPSG%3A4326%3A5"),
                kvpTile.getUrl());
    }

    private Tile createTileFromXYZoom(TileService service) throws Exception {
        TileIdentifier identifier = new WMTSTileIdentifier(20, 15, new WebMercatorZoomLevel(5), service.getName());
        Tile tile = factory.create(identifier, service);

        Assert.assertEquals(RenderState.NEW, tile.getRenderState());

        return tile;
    }

    @Test
    public void testGetTileFromCoordinate() throws Exception {

        WMTSTileService service = createKVPService(); // TODO: create a testpoint array for REST too

        // unesco_points has this bbox:
        //  <ows:WGS84BoundingBox>
        //    <ows:LowerCorner>7.466999156080053 36.67491984727179</ows:LowerCorner>
        //    <ows:UpperCorner>18.033902263137904 46.65640699246296</ows:UpperCorner>
        //   </ows:WGS84BoundingBox>

        TestPoint[] tests = { //
            new TestPoint(90, -180, 0, 0, 0, 1, 0), //
            new TestPoint(90, -180, 1, 0, 0, 2, 0), //
            new TestPoint(90, -180, 2, 0, 0, 4, 0), //
            new TestPoint(90, -180, 8, 0, 0, 266, 61), //
            new TestPoint(10, 40, 0, 1, 0, 1, 0), //
            new TestPoint(10, 40, 1, 2, 0, 2, 0), //
            new TestPoint(10, 40, 2, 4, 1, 4, 0), //
            new TestPoint(8, 37, 3, 9, 3, 8, 1), //
            new TestPoint(8, 46, 3, 10, 3, 8, 1), //
        };

        for (TestPoint tp : tests) {
            int offset = 0;

            WMTSZoomLevel zoomLevel = service.getZoomLevel(tp.zoomlevel + offset);
            TileIdentifier mtile = service.identifyTileAtCoordinate(tp.lon, tp.lat, zoomLevel);
            TileIdentifier ltile = service.constrainToUpperLeftTile(mtile, zoomLevel);

            /*System.out.println(
            tp.lat
                    + ","
                    + tp.lon
                    + " z:"
                    + tp.zoomlevel
                    + " in matrix["
                    + mtile.getTileIdentifier().getX()
                    + ","
                    + mtile.getTileIdentifier().getY()
                    + "]"
                    + " limited["
                    + ltile.getTileIdentifier().getX()
                    + ","
                    + ltile.getTileIdentifier().getY()
                    + "]"
                    + " expectedM: ["
                    + tp.expectedMCol
                    + ","
                    + tp.expectedMRow
                    + "]"
                    + " expectedL: ["
                    + tp.expectedLCol
                    + ","
                    + tp.expectedLRow
                    + "]");*/

            Assert.assertEquals("Bad mX", tp.expectedMCol, mtile.getX());
            Assert.assertEquals("Bad mY", tp.expectedMRow, mtile.getY());
            Assert.assertEquals("Bad lX", tp.expectedLCol, ltile.getX());
            Assert.assertEquals("Bad lY", tp.expectedLRow, ltile.getY());
        }
    }

    @Test
    public void testFindRightNeighbour() throws Exception {
        for (WMTSServiceType t : WMTSServiceType.values()) {
            WMTSTileService service = (WMTSTileService) createService(t);
            WMTSZoomLevel zoomLevel = service.getZoomLevel(5);
            WMTSTile tile = new WMTSTile(20, 15, zoomLevel, service);

            Tile neighbour = factory.findRightNeighbour(tile, service);
            Assert.assertNotNull(neighbour);
            // assertTrue(neighbour.getContextState().equals(ContextState.OKAY));
            WMTSTile expectedNeighbour = new WMTSTile(21, 15, zoomLevel, service);

            Assert.assertEquals(expectedNeighbour.getTileIdentifier(), neighbour.getTileIdentifier());
        }
    }

    @Test
    public void testFindLowerNeighbour() throws Exception {
        for (WMTSServiceType t : WMTSServiceType.values()) {
            TileService service = createService(t);
            WMTSTile tile = new WMTSTile(10, 5, new WMTSZoomLevel(5, (WMTSTileService) service), service);

            Tile neighbour = factory.findLowerNeighbour(tile, service);

            WMTSTile expectedNeighbour = new WMTSTile(10, 6, new WMTSZoomLevel(5, (WMTSTileService) service), service);

            Assert.assertEquals(expectedNeighbour.getTileIdentifier(), neighbour.getTileIdentifier());
        }
    }

    @Ignore
    @Test
    public void testGetExtentFromTileName() throws Exception {

        WMTSTileService[] services = {createRESTService(), createKVPService()};

        ReferencedEnvelope[] expectedEnv = {
            new ReferencedEnvelope(1102848.0, 2151424.0, -951424.0, 97152.0, CRS.decode("EPSG:31287")),
            new ReferencedEnvelope(-90, 0.00, -90.0, 0.0, DefaultGeographicCRS.WGS84) // This doesn't look right
        };

        for (int i = 0; i < 2; i++) {
            TileService service = services[i];
            // For some reason map proxy has an extra level compared to
            // GeoServer!
            int offset = i == 0 ? 1 : 0; // REST has 1 in offset
            WMTSZoomLevel zoomLevel = ((WMTSTileService) service).getZoomLevel(1 + offset);
            WMTSTileIdentifier tileId = new WMTSTileIdentifier(1, 1, zoomLevel, "SomeName");
            WMTSTile tile = new WMTSTile(tileId, service);

            ReferencedEnvelope env = WMTSTileFactory.getExtentFromTileName(tileId, service);

            Assert.assertEquals(tile.getExtent(), env);

            Assert.assertEquals(service.getName(), expectedEnv[i].getMinX(), env.getMinX(), 0.001);
            Assert.assertEquals(service.getName(), expectedEnv[i].getMinY(), env.getMinY(), 0.001);
            Assert.assertEquals(service.getName(), expectedEnv[i].getMaxX(), env.getMaxX(), 0.001);
            Assert.assertEquals(service.getName(), expectedEnv[i].getMaxY(), env.getMaxY(), 0.001);
        }
    }

    private TileService createService(WMTSServiceType type) throws Exception {

        if (WMTSServiceType.REST.equals(type)) {
            return createRESTService();
        } else {
            return createKVPService();
        }
    }

    private WMTSTileService createRESTService() throws Exception {
        try {
            URL capaResource = getClass().getClassLoader().getResource("test-data/zamg.getcapa.xml");
            assertNotNull("Can't find REST getCapa resource", capaResource);
            File capaFile = new File(capaResource.toURI());
            assertTrue("Can't find REST getCapa file", capaFile.exists());
            WMTSCapabilities capa = createCapabilities(capaFile);

            String baseURL = "XXXhttp://wmsx.zamg.ac.at/mapcacheStatmap/wmts/1.0.0/WMTSCapabilities.xml";
            return new WMTSTileService(
                    baseURL, WMTSServiceType.REST, capa.getLayer("grey"), null, capa.getMatrixSet("statmap"));

        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    private WMTSTileService createKVPService() throws Exception {
        try {
            URL capaKvp = getClass().getClassLoader().getResource("test-data/geosolutions_getcapa_kvp.xml");
            assertNotNull(capaKvp);
            File capaFile = new File(capaKvp.toURI());
            WMTSCapabilities capa = createCapabilities(capaFile);

            String baseURL = "http://demo.geo-solutions.it/geoserver/gwc/service/wmts?REQUEST=getcapabilities";

            WMTSLayer layer = capa.getLayer("unesco:Unesco_point");
            TileMatrixSet matrixSet = capa.getMatrixSet("EPSG:4326");
            assertNotNull(layer);
            assertNotNull(matrixSet);

            return new WMTSTileService(baseURL, WMTSServiceType.KVP, layer, null, matrixSet);

        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    protected WMTSTileFactory createFactory() {
        return new WMTSTileFactory();
    }

    public static WMTSCapabilities createCapabilities(File capFile) throws Exception {
        Parser parser = new Parser(new WMTSConfiguration());

        Object object = parser.parse(new FileReader(capFile));
        assertTrue("Capabilities failed to parse " + object.getClass(), object instanceof CapabilitiesType);

        return new WMTSCapabilities((CapabilitiesType) object);
    }

    @Test
    public void testWMTSTileWithStyleInCapabilities() throws Exception {
        // capabilities file with resource URL having an
        // {style} placeholder
        URL capaResource = getClass().getClassLoader().getResource("test-data/zamg.getcapa.xml");

        File capaFile = new File(capaResource.toURI());

        WMTSCapabilities capa = createCapabilities(capaFile);

        String baseURL =
                "http://wmsx.zamg.ac.at/mapcacheStatmap/wmts/1.0.0/overlay-all/default/{style}/{TileMatrixSet}/{TileMatrix}/{TileRow}/{TileCol}.png";
        WMTSTileService service = new WMTSTileService(
                baseURL, WMTSServiceType.REST, capa.getLayer("grey"), "default", capa.getMatrixSet("statmap"));

        WMTSZoomLevel zoomLevel = service.getZoomLevel(1);
        WMTSTileIdentifier tileId = new WMTSTileIdentifier(1, 1, zoomLevel, "SomeName");
        WMTSTile tile = new WMTSTile(tileId, service);
        String url = tile.getUrl().toString();
        // check that url contains style instead of {style}
        assertTrue(url.contains("/overlay-all/default/default/"));
        assertFalse(url.contains("{style}"));
    }

    @Test
    public void testWMTSTileWithStyleInCapabilities2() throws Exception {
        // capabilities file with resource URL having an
        // {Style} placeholder
        URL capaResource = getClass().getClassLoader().getResource("test-data/basemapGetCapa.xml");

        WMTSCapabilities capa = createCapabilities(new File(capaResource.toURI()));

        String baseURL =
                "https://maps1.wien.gv.at/basemap/bmapoberflaeche/{Style}/{TileMatrixSet}/{TileMatrix}/{TileRow}/{TileCol}.jpeg";
        WMTSTileService service = new WMTSTileService(
                baseURL, WMTSServiceType.REST, capa.getLayer("bmaphidpi"), "normal", capa.getMatrixSet("EPSG:4326"));

        WMTSTileIdentifier tileId = new WMTSTileIdentifier(1, 1, service.getZoomLevel(1), "SomeName");
        WMTSTile tile = new WMTSTile(tileId, service);
        String url = tile.getUrl().toString();
        // check that url contains style name instead of {Style}
        assertTrue(url.contains("/normal/"));
        assertFalse(url.contains("{Style}"));
    }
}
