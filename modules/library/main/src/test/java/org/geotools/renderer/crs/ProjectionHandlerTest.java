/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.SingleCRS;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.PolarStereographic;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.util.Base64;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;

public class ProjectionHandlerTest {

    static final double EPS = 1e-5;

    static CoordinateReferenceSystem WGS84;

    static CoordinateReferenceSystem ED50_LATLON;

    static CoordinateReferenceSystem UTM32N;

    static CoordinateReferenceSystem MERCATOR;

    static CoordinateReferenceSystem MERCATOR_SHIFTED;

    static CoordinateReferenceSystem ED50;

    static CoordinateReferenceSystem OSM;

    static GeometryFactory gf = new GeometryFactory();

    @BeforeClass
    public static void setup() throws Exception {
        WGS84 = DefaultGeographicCRS.WGS84;
        UTM32N = CRS.decode("EPSG:32632", true);
        MERCATOR_SHIFTED = CRS.decode("EPSG:3349", true);
        MERCATOR = CRS.decode("EPSG:3395", true);
        OSM = CRS.decode("EPSG:3857", true);
        ED50 = CRS.decode("EPSG:4230", true);
        ED50_LATLON = CRS.decode("urn:x-ogc:def:crs:EPSG:4230", false);
        MapProjection.SKIP_SANITY_CHECKS = true;
    }

    @AfterClass
    public static void teardown() throws Exception {
        MapProjection.SKIP_SANITY_CHECKS = false;
    }

    @Test
    public void testWrappingOn3DCRS() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4939", true);
        SingleCRS hcrs = CRS.getHorizontalCRS(crs);
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(-190, 60, -90, 45, hcrs);
        Map<String, Object> params = new HashMap<>();
        params.put(ProjectionHandler.ADVANCED_PROJECTION_DENSIFY, 1.0);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(wgs84Envelope, crs, true, params);

        assertNull(handler.validAreaBounds);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());

        ReferencedEnvelope expected = new ReferencedEnvelope(170, 180, -90, 45, hcrs);
        assertTrue(envelopes.remove(wgs84Envelope));
        assertEquals(expected, envelopes.get(0));
    }

    @Test
    public void testWrappingDisabledHeuristic() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline (measures are still accurate for something
        // crossing the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g = new WKTReader().read("LINESTRING(-40 20, 190 20)");
        Map<String, Object> params = new HashMap<>();
        params.put(WrappingProjectionHandler.DATELINE_WRAPPING_CHECK_ENABLED, false);

        MathTransform mt = CRS.findMathTransform(WGS84, MERCATOR, true);
        Geometry reprojected = JTS.transform(g, mt);

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true, params);
        Geometry processed = handler.postProcess(mt, reprojected);

        assertEquals(processed.getGeometryN(0), reprojected);

        params.put(WrappingProjectionHandler.DATELINE_WRAPPING_CHECK_ENABLED, true);
        handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true, params);

        processed = handler.postProcess(mt, reprojected);

        assertNotEquals(processed.getGeometryN(0), reprojected);
    }

    @Test
    public void testDensification() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(-190, 60, -90, 45, WGS84);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(wgs84Envelope, WGS84, true);
        Geometry line = gf.createLineString(new Coordinate[] {new Coordinate(40, 45), new Coordinate(40, 88)});
        LineString notDensified = (LineString) handler.preProcess(line);
        assertEquals(2, notDensified.getCoordinates().length);

        Map<String, Object> params = new HashMap<>();
        params.put(ProjectionHandler.ADVANCED_PROJECTION_DENSIFY, 1.0);
        handler = ProjectionHandlerFinder.getHandler(wgs84Envelope, WGS84, true, params);
        LineString densified = (LineString) handler.preProcess(line);
        assertEquals(44, densified.getCoordinates().length);
    }

    /**
     * Test that the densification can defend itself against OOMs
     *
     * @throws Exception
     */
    @Test
    public void testDensificationOOM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(-190, 60, -90, 45, WGS84);

        // this setup would result in 40 million densified points
        Map<String, Object> params = new HashMap<>();
        params.put(ProjectionHandler.ADVANCED_PROJECTION_DENSIFY, 1e-9);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(wgs84Envelope, WGS84, true, params);
        Geometry line = gf.createLineString(new Coordinate[] {new Coordinate(40, 45), new Coordinate(40, 85)});
        LineString densified = (LineString) handler.preProcess(line);
        // the value has been reduced below the max threshold
        assertEquals(152589, densified.getCoordinates().length);
    }

    @Test
    public void testQueryWrappingWGS84() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(-190, 60, -90, 45, WGS84);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(wgs84Envelope, WGS84, true);

        assertNull(handler.validAreaBounds);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());

        ReferencedEnvelope expected = new ReferencedEnvelope(170, 180, -90, 45, WGS84);
        assertTrue(envelopes.remove(wgs84Envelope));
        assertEquals(expected, envelopes.get(0));
    }

    @Test
    public void testQueryWrappingED50LatLon() throws Exception {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-90, 45, -190, 60, ED50_LATLON);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(envelope, ED50_LATLON, true);

        assertNull(handler.validAreaBounds);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());

        ReferencedEnvelope expected = new ReferencedEnvelope(-90, 45, 170, 180, ED50_LATLON);
        assertTrue(envelopes.remove(envelope));
        assertEquals(expected, envelopes.get(0));
    }

    @Test
    public void testValidAreaMercator() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -89.9999, 89.9999, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR_SHIFTED, true);

        // check valid area
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        Envelope va = handler.validAreaBounds;
        assertNotNull(va);
        assertTrue(va.getMinX() <= -180.0);
        assertTrue(va.getMaxX() >= 180.0);
        assertTrue(-90 < va.getMinY());
        assertTrue(90.0 > va.getMaxY());
    }

    @Test
    public void testValidAreaLambertAzimuthalEqualArea() throws Exception {
        // check valid area for the north case
        ReferencedEnvelope wgs84north = new ReferencedEnvelope(-120, 0, 45, 90, WGS84);
        ReferencedEnvelope laeNorth = wgs84north.transform(CRS.decode("EPSG:3408"), true);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(laeNorth, WGS84, true);
        ReferencedEnvelope va = handler.validAreaBounds;
        assertNotNull(va);
        assertEquals(va.getCoordinateReferenceSystem(), WGS84);
        assertEquals(-180.0, va.getMinX(), 0d);
        assertEquals(180.0, va.getMaxX(), 0d);
        assertEquals(0, va.getMinY(), 0d);
        assertEquals(90, va.getMaxY(), 0d);

        // check the south case
        ReferencedEnvelope wgs84South = new ReferencedEnvelope(-120, 0, -90, -45, WGS84);
        ReferencedEnvelope laeSouth = wgs84South.transform(CRS.decode("EPSG:3409"), true);
        handler = ProjectionHandlerFinder.getHandler(laeSouth, WGS84, true);
        va = handler.validAreaBounds;
        assertNotNull(va);
        assertEquals(-180.0, va.getMinX(), 0d);
        assertEquals(180.0, va.getMaxX(), 0d);
        assertEquals(-90, va.getMinY(), 0d);
        assertEquals(0, va.getMaxY(), 0d);
    }

    @Test
    public void testValidAreaWorldVanDerGrinten() throws Exception {
        String wkt = "PROJCS[\"World_Van_der_Grinten_I\", \n"
                + "  GEOGCS[\"GCS_WGS_1984\", \n"
                + "    DATUM[\"D_WGS_1984\", \n"
                + "      SPHEROID[\"WGS_1984\", 6378137.0, 298.257223563]], \n"
                + "    PRIMEM[\"Greenwich\", 0.0], \n"
                + "    UNIT[\"degree\", 0.017453292519943295], \n"
                + "    AXIS[\"Longitude\", EAST], \n"
                + "    AXIS[\"Latitude\", NORTH]], \n"
                + "  PROJECTION[\"World_Van_der_Grinten_I\"], \n"
                + "  PARAMETER[\"central_meridian\", 0.0], \n"
                + "  PARAMETER[\"false_easting\", 0.0], \n"
                + "  PARAMETER[\"false_northing\", 0.0], \n"
                + "  UNIT[\"m\", 1.0], \n"
                + "  AXIS[\"x\", EAST], \n"
                + "  AXIS[\"y\", NORTH], \n"
                + "  AUTHORITY[\"EPSG\",\"54029\"]]";

        // check valid area for the north case
        ReferencedEnvelope envelopeWgs84 = new ReferencedEnvelope(-180, 180, -90, 90, WGS84);
        ReferencedEnvelope envelope = envelopeWgs84.transform(CRS.parseWKT(wkt), true);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(envelope, WGS84, true);
        ReferencedEnvelope va = handler.validAreaBounds;
        assertNotNull(va);
        assertEquals(va.getCoordinateReferenceSystem(), WGS84);
        assertEquals(-Integer.MAX_VALUE, va.getMinX(), 0d);
        assertEquals(Integer.MAX_VALUE, va.getMaxX(), 0d);
        assertEquals(-90, va.getMinY(), 0d);
        assertEquals(90, va.getMaxY(), 0d);
    }

    @Test
    public void testValidAreaLambertConformal() throws Exception {
        // check valid area for the north case
        ReferencedEnvelope wgs84north = new ReferencedEnvelope(-120, 0, 45, 90, WGS84);
        ReferencedEnvelope laeNorth = wgs84north.transform(CRS.decode("EPSG:2062"), true);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(laeNorth, WGS84, true);
        Envelope va = handler.validAreaBounds;
        assertNotNull(va);
        assertEquals(-179.9, va.getMinX(), 0d);
        assertEquals(179.9, va.getMaxX(), 0d);
        assertEquals(-4, va.getMinY(), 0d);
        assertEquals(90, va.getMaxY(), 0d);

        // check the south case
        ReferencedEnvelope wgs84South = new ReferencedEnvelope(-180, -90, -40, 0, WGS84);
        ReferencedEnvelope laeSouth = wgs84South.transform(CRS.decode("EPSG:2194"), true);
        handler = ProjectionHandlerFinder.getHandler(laeSouth, WGS84, true);
        va = handler.validAreaBounds;
        assertNotNull(va);
        assertEquals(-180, va.getMinX(), 0d);
        assertEquals(180, va.getMaxX(), 0d);
        assertEquals(-90, va.getMinY(), 0d);
        assertEquals(29.73, va.getMaxY(), 0.01d);
    }

    @Test
    public void testCutGeometryLambertConformal() throws Exception {
        // get a lambert conformal conic with
        ReferencedEnvelope wgs84South = new ReferencedEnvelope(-180, -90, -40, 0, WGS84);
        ReferencedEnvelope laeSouth = wgs84South.transform(CRS.decode("EPSG:2194"), true);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(laeSouth, WGS84, true);
        // a Geometry that crosses the opposite of the central meridian
        Polygon geometry = JTS.toGeometry(new Envelope(5, 15, 0, 10));
        Geometry preProcessed = handler.preProcess(geometry);
        assertTrue("Should have sliced the geometry in two parts", preProcessed instanceof MultiPolygon);
    }

    @Test
    public void testRobustCutting() throws Exception {
        String wkt = "PROJCS[\"Asia_South_Lambert_Conformal_Conic\", \n"
                + "  GEOGCS[\"GCS_WGS_1984\", \n"
                + "    DATUM[\"WGS_1984\", \n"
                + "      SPHEROID[\"WGS_1984\", 6378137.0, 298.257223563]], \n"
                + "    PRIMEM[\"Greenwich\", 0.0], \n"
                + "    UNIT[\"degree\", 0.017453292519943295], \n"
                + "    AXIS[\"Longitude\", EAST], \n"
                + "    AXIS[\"Latitude\", NORTH]], \n"
                + "  PROJECTION[\"Lambert_Conformal_Conic_2SP\"], \n"
                + "  PARAMETER[\"central_meridian\", 125.0], \n"
                + "  PARAMETER[\"latitude_of_origin\", -15.0], \n"
                + "  PARAMETER[\"standard_parallel_1\", 7.0], \n"
                + "  PARAMETER[\"false_easting\", 0.0], \n"
                + "  PARAMETER[\"false_northing\", 0.0], \n"
                + "  PARAMETER[\"scale_factor\", 1.0], \n"
                + "  PARAMETER[\"standard_parallel_2\", -32.0], \n"
                + "  UNIT[\"m\", 1.0], \n"
                + "  AXIS[\"x\", EAST], \n"
                + "  AXIS[\"y\", NORTH], \n"
                + "  AUTHORITY[\"EPSG\",\"102030\"]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        Geometry geom;
        try (Reader reader = new InputStreamReader(
                new GZIPInputStream(ProjectionHandlerTest.class.getResourceAsStream("para.wkt.gz")),
                StandardCharsets.UTF_8)) {
            geom = new WKTReader().read(reader);
        }

        ReferencedEnvelope re = new ReferencedEnvelope(
                1.2248782489837505E7, 2.0320948299686E7, -4848266.752703998, 3223899.0571445003, crs);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(re, WGS84, true);
        // hard intersection, not even enhanced precision ops was able to make it
        Geometry preprocessed = handler.preProcess(geom);
        assertNotEquals(preprocessed, geom);
    }

    @Test
    public void testQueryWrappingMercatorWorld() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-200, 200, -89, 89, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR_SHIFTED, true);

        // get query area, we expect just one envelope spanning the whole world
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());

        ReferencedEnvelope env = envelopes.get(0);
        assertEquals(-180.0, env.getMinX(), EPS);
        assertEquals(180.0, env.getMaxX(), EPS);
        assertEquals(-85, env.getMinY(), 0.1);
        assertEquals(85.0, env.getMaxY(), 0.1);
    }

    @Test
    public void testQueryOutsideValidArea() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-200, 200, -89, -86, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR_SHIFTED, true);

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(0, envelopes.size());
    }

    @Test
    public void testQueryWrappingMercatorSeparate() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(160, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // get query area, we expect two separate query envelopes
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());

        ReferencedEnvelope reOrig = envelopes.get(0); // original
        assertEquals(170.0, reOrig.getMinX(), EPS);
        assertEquals(180.0, reOrig.getMaxX(), EPS);

        ReferencedEnvelope reAdded = envelopes.get(1); // added
        assertEquals(-180.0, reAdded.getMinX(), EPS);
        assertEquals(-170.0, reAdded.getMaxX(), EPS);
    }

    @Test
    public void testQueryWrappingPacificMercator() throws Exception {
        // <wcs:DimensionTrim>
        // <wcs:Dimension>N</wcs:Dimension>
        // <wcs:TrimLow>0</wcs:TrimLow>
        // <wcs:TrimHigh>4838471</wcs:TrimHigh>
        // </wcs:DimensionTrim>
        // <wcs:DimensionTrim>
        // <wcs:Dimension>E</wcs:Dimension>
        // <wcs:TrimLow>1113195</wcs:TrimLow>
        // <wcs:TrimHigh>5565975</wcs:TrimHigh>
        // </wcs:DimensionTrim>
        // <wcs:format>image/tiff</wcs:format>
        // <wcs:Extension>
        // <!-- Mercator centered on 150°, request is roughly for long(160,-160),lat(0, 40)-->
        // <wcscrs:subsettingCrs>http://www.opengis.net/def/crs/EPSG/0/3832</wcscrs:subsettingCrs>
        //

        CoordinateReferenceSystem crs = CRS.decode("EPSG:3832");
        ReferencedEnvelope mercatorEnvelope = new ReferencedEnvelope(1113195, 5565975, 0, 4838471, crs);

        // get query area, we expect two separate query envelopes across the dateline
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());

        ReferencedEnvelope reOrig = envelopes.get(0);
        assertEquals(160.0, reOrig.getMinX(), EPS);
        assertEquals(180.0, reOrig.getMaxX(), EPS);

        ReferencedEnvelope reAdded = envelopes.get(1);
        assertEquals(-180.0, reAdded.getMinX(), EPS);
        assertEquals(-160.0, reAdded.getMaxX(), EPS);
    }

    @Test
    public void testValidAreaUTM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(8, 10, 40, 45, WGS84);
        ReferencedEnvelope utmEnvelope = wgs84Envelope.transform(UTM32N, true);

        // check valid area
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(utmEnvelope, WGS84, true);
        Envelope va = handler.validAreaBounds;
        assertNotNull(va);
        assertTrue(9 - 90 < va.getMinX() && va.getMinX() <= 9 - 3);
        assertTrue(9 + 3 <= va.getMaxX() && va.getMaxX() < 9 + 90);
        assertEquals(-85, va.getMinY(), EPS);
        assertEquals(85.0, va.getMaxY(), EPS);
    }

    @Test
    public void testQueryUTM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(8, 10, 40, 45, WGS84);
        ReferencedEnvelope utmEnvelope = wgs84Envelope.transform(UTM32N, true);

        // get query area, we expect just one envelope, the original one
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(utmEnvelope, WGS84, true);
        ReferencedEnvelope expected = utmEnvelope.transform(WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        assertEquals(expected, envelopes.get(0));
    }

    @Test
    public void testWrapGeometryMercator() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(160, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline (measures are still accurate for something
        // crossing the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g = new WKTReader().read("LINESTRING(170 -40, 190 40)");

        // make sure the geometry is not wrapped
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(g, preProcessed);
        // transform and post process
        MathTransform mt = CRS.findMathTransform(WGS84, MERCATOR, true);
        Geometry transformed = JTS.transform(g, mt);
        Geometry postProcessed = handler.postProcess(mt.inverse(), transformed);
        Envelope env = postProcessed.getEnvelopeInternal();
        // check the geometry is in the same area as the rendering envelope
        assertEquals(mercatorEnvelope.getMinX(), env.getMinX(), EPS);
        assertEquals(mercatorEnvelope.getMaxX(), env.getMaxX(), EPS);
    }

    /** Checks that the measures of XYM geometries are not transformed. */
    @Test
    public void testXymGeometriesMeasuresArePreserved() throws Exception {
        // build a XYM geometry and reproject it
        Geometry geometry = new WKTReader().read("LINESTRINGM(170 -40 2, 190 40 7)");
        MathTransform transform = CRS.findMathTransform(WGS84, MERCATOR, true);
        Geometry transformed = JTS.transform(geometry, transform);
        // check that coordinates where transformed but measures preserved
        assertThat(transformed, instanceOf(LineString.class));
        LineString line = (LineString) transformed;
        assertThat(line.getCoordinateSequence().getDimension(), is(3));
        assertThat(line.getCoordinateSequence().getMeasures(), is(1));
        // check the first coordinate
        assertThat(line.getCoordinateSequence().getX(0), closeTo(1.8924313434856504E7, EPS));
        assertThat(line.getCoordinateSequence().getY(0), closeTo(-4838471.398061137, EPS));
        assertThat(line.getCoordinateSequence().getZ(0), is(Double.NaN));
        assertThat(line.getCoordinateSequence().getM(0), is(2.0));
        // check the second coordinate
        assertThat(line.getCoordinateSequence().getX(1), closeTo(2.115070325072198E7, EPS));
        assertThat(line.getCoordinateSequence().getY(1), closeTo(4838471.398061137, EPS));
        assertThat(line.getCoordinateSequence().getZ(1), is(Double.NaN));
        assertThat(line.getCoordinateSequence().getM(1), is(7.0));
    }

    /** Checks that the measures of XYZM geometries are not transformed. */
    @Test
    public void testXyzmGeometriesMeasuresArePreserved() throws Exception {
        // build a XYM geometry and reproject it
        Geometry geometry = new WKTReader().read("LINESTRINGZM(170 -40 10 2, 190 40 15 7)");
        MathTransform transform = CRS.findMathTransform(WGS84, MERCATOR, true);
        Geometry transformed = JTS.transform(geometry, transform);
        // check that coordinates where transformed but measures preserved
        assertThat(transformed, instanceOf(LineString.class));
        LineString line = (LineString) transformed;
        assertThat(line.getCoordinateSequence().getDimension(), is(4));
        assertThat(line.getCoordinateSequence().getMeasures(), is(1));
        // check the first coordinate
        assertThat(line.getCoordinateSequence().getX(0), closeTo(1.8924313434856504E7, EPS));
        assertThat(line.getCoordinateSequence().getY(0), closeTo(-4838471.398061137, EPS));
        assertThat(line.getCoordinateSequence().getZ(0), is(10.0));
        assertThat(line.getCoordinateSequence().getM(0), is(2.0));
        // check the second coordinate
        assertThat(line.getCoordinateSequence().getX(1), closeTo(2.115070325072198E7, EPS));
        assertThat(line.getCoordinateSequence().getY(1), closeTo(4838471.398061137, EPS));
        assertThat(line.getCoordinateSequence().getZ(1), is(15.0));
        assertThat(line.getCoordinateSequence().getM(1), is(7.0));
    }

    @Test
    public void testWrapGeometrySmall() throws Exception {
        // projected dateline CRS
        CoordinateReferenceSystem FIJI = CRS.decode("EPSG:3460", true);
        // a small geometry that will cross the dateline
        Geometry g =
                new WKTReader().read("POLYGON ((2139122 5880020, 2139122 5880030, 2139922 5880030, 2139122 5880020))");
        Geometry original = g.copy();

        // rendering bounds only slightly bigger than geometry
        ReferencedEnvelope world = new ReferencedEnvelope(178, 181, -1, 1, WGS84);

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, FIJI, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process
        MathTransform mt = CRS.findMathTransform(FIJI, WGS84);
        Geometry transformed = JTS.transform(g, mt);
        Geometry postProcessed = handler.postProcess(mt.inverse(), transformed);
        // check the geometry is in the same area as the rendering envelope
        assertTrue(world.contains(postProcessed.getEnvelopeInternal()));
    }

    @Test
    public void testWorldLargeGeometry() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -90, 90, WGS84);

        // a geometry close to the dateline
        Geometry g = new WKTReader().read("POLYGON((-178 -90, -178 90, 178 90, 178 -90, -178 -90))");
        Geometry original = new WKTReader().read("POLYGON((-178 -90, -178 90, 178 90, 178 -90, -178 -90))");

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process (provide identity transform to force wrap heuristic)
        Geometry postProcessed = handler.postProcess(CRS.findMathTransform(WGS84, WGS84), g);
        // check the geometry is in the same area as the rendering envelope
        assertEquals(original, postProcessed);
    }

    @Test
    public void testWrapGeometryLatLonMultipleTimes() throws Exception {
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(-90, 90, -580, 540, ED50_LATLON);

        // a geometry close to the dateline
        Geometry g = new WKTReader().read("POLYGON((-74 -33, -29 -33, -29 5, -74 5, -74 -33))");

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(renderingEnvelope, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        MathTransform mt = handler.getRenderingTransform(CRS.findMathTransform(WGS84, ED50_LATLON));
        Geometry transformed = JTS.transform(preProcessed, mt);
        // post process (provide identity transform to force wrap heuristic)
        Geometry postProcessed = handler.postProcess(mt, transformed);
        assertTrue(postProcessed.isValid());
        // should have been replicated three times
        assertEquals(3, postProcessed.getNumGeometries());
    }

    @Test
    public void testWrapGeometryReprojectToLatLonED50() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-80, 80, -180, 180, ED50_LATLON);

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope (with wgs84 lon/latcoordinates)
        String wkt = "POLYGON((178 -80, 178 80, 182 80, 182 80, 178 -80))";
        Geometry g = new WKTReader().read(wkt);
        Geometry original = new WKTReader().read(wkt);
        MathTransform mt = CRS.findMathTransform(WGS84, ED50_LATLON);
        MathTransform prepared = handler.getRenderingTransform(mt);
        Geometry reprojected = JTS.transform(original, prepared);

        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process, this should wrap the geometry and clone it
        Geometry postProcessed = handler.postProcess(prepared, reprojected);
        assertTrue(postProcessed instanceof MultiPolygon);
    }

    @Test
    public void testWrapAnctartica() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-80, 80, -180, 180, ED50_LATLON);

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope (with wgs84 lon/latcoordinates)
        String wkt = "POLYGON((180 -90, 180 90, -180 90, -180 -90, 180 -90))";
        Geometry g = new WKTReader().read(wkt);
        MathTransform mt = CRS.findMathTransform(WGS84, ED50_LATLON);
        MathTransform prepared = handler.getRenderingTransform(mt);

        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        Geometry reprojected = JTS.transform(preProcessed, prepared);
        assertTrue(reprojected.isValid());
        reprojected.apply((CoordinateFilter) coord -> {
            assertEquals(90.0, Math.abs(coord.getOrdinate(0)), 0.1);
            assertEquals(180.0, Math.abs(coord.getOrdinate(1)), 5);
        });
        // post process, this should wrap the geometry, make sure it's valid, and avoid large jumps
        // in its border
        Geometry postProcessed = handler.postProcess(prepared, reprojected);
        assertThat(postProcessed, CoreMatchers.instanceOf(MultiPolygon.class));
        assertEquals(2, postProcessed.getNumGeometries());
    }

    @Test
    public void testWrapGeometryReprojectToED50() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-80, 80, -180, 180, ED50);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope (with wgs84 lon/latcoordinates)
        String wkt = "POLYGON((178 -80, 178 80, 182 80, 182 80, 178 -80))";
        Geometry g = new WKTReader().read(wkt);
        Geometry original = new WKTReader().read(wkt);
        MathTransform mt = CRS.findMathTransform(WGS84, ED50);
        mt = handler.getRenderingTransform(mt);
        Geometry reprojected = JTS.transform(original, mt);

        // make sure the geometry is not wrapped, but it is preserved

        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process, this should wrap the geometry and clone it
        Geometry postProcessed = handler.postProcess(mt, reprojected);
        assertTrue(postProcessed instanceof MultiPolygon);
    }

    @Test
    public void testWrapJumpLast() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -90, 90, WGS84);
        Geometry g = new WKTReader().read("POLYGON((-131 -73.5,0 -90,163 -60,174 -60,-131 -73.5))");
        Geometry original = new WKTReader().read("POLYGON((-131 -73.5,0 -90,163 -60,174 -60,-131 -73.5))");
        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process (provide identity transform to force wrap heuristic)
        Geometry postProcessed = handler.postProcess(CRS.findMathTransform(WGS84, WGS84), g);
        // check the geometry is in the same area as the rendering envelope
        assertEquals(original, postProcessed);
    }

    @Test
    public void testWrapGeometryWGS84Duplicate() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-200, 200, -90, 90, WGS84);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g = new WKTReader().read("POLYGON((-178 -90, -178 90, 178 90, 178 -90, -178 -90))");
        Geometry original = new WKTReader().read("POLYGON((-178 -90, -178 90, 178 90, 178 -90, -178 -90))");

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process
        Geometry postProcessed = handler.postProcess(null, g);
        // check we have replicated the geometry in both directions
        Envelope ppEnvelope = postProcessed.getEnvelopeInternal();
        Envelope expected = new Envelope(-538, 538, -90, 90);
        assertEquals(expected, ppEnvelope);
    }

    @Test
    public void testDuplicateGeometryMercator() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -50, 50, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g = new WKTReader().read("LINESTRING(170 -50, 190 50)");

        // make sure the geometry is not wrapped
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(g, preProcessed);
        // transform and post process
        MathTransform mt = CRS.findMathTransform(WGS84, MERCATOR, true);
        Geometry transformed = JTS.transform(g, mt);
        Geometry postProcessed = handler.postProcess(mt, transformed);
        // should have been duplicated in two parts
        assertTrue(postProcessed instanceof MultiLineString);
        MultiLineString mls = (MultiLineString) postProcessed;
        assertEquals(2, mls.getNumGeometries());
        // the two geometries width should be the same as 20°
        double twentyDegWidth = mercatorEnvelope.getWidth() / 18;
        assertEquals(twentyDegWidth, mls.getGeometryN(0).getEnvelopeInternal().getWidth(), EPS);
        assertEquals(twentyDegWidth, mls.getGeometryN(1).getEnvelopeInternal().getWidth(), EPS);
    }

    @Test
    public void testLimitExcessiveDuplication() throws Exception {
        // a veeeery large rendering envelope, enough to trigger whatever are the default
        // protection limits (yes, it's in degrees!)
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(-1800000, 1800000, -50, 50, WGS84);

        // the geometry that will be wrapped
        Geometry g = new WKTReader().read("LINESTRING(-179 -89, 179 89)");

        // make sure the geometry is not pre-processed
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(renderingEnvelope, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        assertEquals(g, preProcessed);
        Geometry postProcessed = handler.postProcess(IdentityTransform.create(2), g);
        // should have been copied several times, but not above the limit
        assertTrue(postProcessed instanceof MultiLineString);
        MultiLineString mls = (MultiLineString) postProcessed;
        assertEquals(ProjectionHandlerFinder.WRAP_LIMIT * 2 + 1, mls.getNumGeometries());
    }

    @Test
    public void testCutGeometryUTM() throws Exception {
        ReferencedEnvelope wgs84Envelope = new ReferencedEnvelope(8, 10, 40, 45, WGS84);
        ReferencedEnvelope utmEnvelope = wgs84Envelope.transform(UTM32N, true);

        // a geometry that will definitely go outside of the UTM32N valid area
        Geometry g = new WKTReader().read("LINESTRING(-170 -40, 170 40)");

        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(utmEnvelope, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        assertFalse(preProcessed.equalsTopo(g));
        assertTrue(handler.validAreaBounds.contains(preProcessed.getEnvelopeInternal()));
    }

    @Test
    public void testPolarStereographic() throws Exception {
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(-10700000, 14700000, -10700000, 14700000, CRS.decode("EPSG:5041", true));
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(envelope, WGS84, true);
        assertNotNull(handler);
        assertEquals(envelope, handler.getRenderingEnvelope());
        assertTrue(CRS.getMapProjection(envelope.getCoordinateReferenceSystem()) instanceof PolarStereographic);
    }

    @Test
    public void testSkipInvalidGeometries() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(160, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline (measures are still accurate for something
        // crossing the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g1 = new WKTReader()
                .read("POLYGON((150 40, 150 -90, 190 -90, 190 40, 175 40, 175 -87, 165 -87, 165 40, 150 40))");
        Geometry g2 = new WKTReader().read("POLYGON((-178 -90, -178 90, 178 90, 178 -90, -178 -90))");
        // MultiPolygon containing both geometries
        Geometry collection = new MultiPolygon(new Polygon[] {(Polygon) g1, (Polygon) g2}, g1.getFactory());

        // make sure the geometry is not wrapped
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(mercatorEnvelope, WGS84, true);
        assertTrue(handler.requiresProcessing(collection));
        Geometry preProcessed = handler.preProcess(collection);
        // Ensure something has changed
        assertNotEquals(collection, preProcessed);
        // Ensure the result is a Geometry collection which does not accept Geometries of the same
        // type of the Projection Handler one.
        assertNotNull(preProcessed);
        assertTrue(preProcessed instanceof GeometryCollection);
        int numGeometries = preProcessed.getNumGeometries();
        assertEquals(numGeometries, 3);
        for (int i = 0; i < numGeometries; i++) {
            assertTrue(preProcessed.getGeometryN(i) instanceof Polygon);
        }
    }

    @Test
    public void testSkipEmptyGeometryCollections() throws Exception {
        ReferencedEnvelope world = new ReferencedEnvelope(160, 180, -40, 40, WGS84);
        ReferencedEnvelope mercatorEnvelope = world.transform(MERCATOR, true);
        // move it so that it crosses the dateline (measures are still accurate for something
        // crossing the dateline
        mercatorEnvelope.translate(mercatorEnvelope.getWidth() / 2, 0);

        // a geometry that will cross the dateline and sitting in the same area as the
        // rendering envelope
        Geometry g1 = new WKTReader()
                .read("POLYGON((150 40, 150 -90, 190 -90, 190 40, 175 40, 175 -87, 165 -87, 165 40, 150 40))");
        // Empty geometry collection
        Geometry collection = new GeometryCollection(null, g1.getFactory());

        // make sure the geometry is not wrapped
        ProjectionHandler handler =
                new ProjectionHandler(WGS84, new Envelope(-0.5d, 2.0d, -0.5d, 2.0d), mercatorEnvelope);
        assertTrue(handler.requiresProcessing(collection));
        Geometry preProcessed = handler.preProcess(collection);
        // Ensure something has changed
        assertNotEquals(collection, preProcessed);
        // Ensure the result is null
        assertNull(preProcessed);
    }

    @Test
    public void testQueryEnvelopesNonWrappingWGS84() throws Exception {
        // dateline crossing request
        ReferencedEnvelope request = new ReferencedEnvelope(170, 190, -40, 40, WGS84);

        // grab a non wrapping handler
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(request, WGS84, false);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        assertEquals(request, envelopes.get(0));
    }

    @Test
    public void testQueryEnvelopesNonWrapping3857() throws Exception {
        // dateline crossing request
        ReferencedEnvelope requestWgs84 = new ReferencedEnvelope(170, 190, -40, 40, WGS84);
        ReferencedEnvelope request = requestWgs84.transform(OSM, true);

        // grab a non wrapping handler
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(request, WGS84, false);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        assertEnvelopesEqual(requestWgs84, envelopes.get(0), EPS);
    }

    private void assertEnvelopesEqual(ReferencedEnvelope re1, ReferencedEnvelope re2, double eps) {
        assertEquals(re1.getCoordinateReferenceSystem(), re2.getCoordinateReferenceSystem());
        assertEquals(re1.getMinX(), re2.getMinX(), eps);
        assertEquals(re1.getMinY(), re2.getMinY(), eps);
        assertEquals(re1.getMaxX(), re2.getMaxX(), eps);
        assertEquals(re1.getMaxY(), re2.getMaxY(), eps);
    }

    @Test
    public void testQueryEnvelopesWrappingWGS84() throws Exception {
        // dateline crossing request
        ReferencedEnvelope request = new ReferencedEnvelope(170, 190, -40, 40, WGS84);

        // grab a non wrapping handler
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(request, WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());
        assertTrue(envelopes.contains(new ReferencedEnvelope(170, 180, -40, 40, WGS84)));
        assertTrue(envelopes.contains(new ReferencedEnvelope(-180, -170, -40, 40, WGS84)));
    }

    @Test
    public void testQueryEnvelopesWrapping3857() throws Exception {
        // dateline crossing request
        ReferencedEnvelope requestWgs84 = new ReferencedEnvelope(170, 190, -40, 40, WGS84);
        ReferencedEnvelope request = requestWgs84.transform(OSM, true);

        // grab a non wrapping handler
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(request, WGS84, true);
        List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
        assertEquals(2, envelopes.size());
        assertEnvelopesEqual(new ReferencedEnvelope(170, 180, -40, 40, WGS84), envelopes.get(0), EPS);
        assertEnvelopesEqual(new ReferencedEnvelope(-180, -170, -40, 40, WGS84), envelopes.get(1), EPS);
    }

    @Test
    public void testWorldMeridian() throws Exception {
        ReferencedEnvelope requestWgs84 = new ReferencedEnvelope(-180, 180, -85, 85, WGS84);
        ReferencedEnvelope requestWebMercator = requestWgs84.transform(OSM, true);

        // a geometry close to the dateline
        Geometry g = new WKTReader().read("LINESTRING(0 -90, 0 90)");
        Geometry expected = new WKTReader().read("LINESTRING(0 -85, 0 85)");

        // make sure the geometry is not wrapped, but it is preserved
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(requestWebMercator, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // should have cut at 85 degrees, the web mercator breaks at the poles
        assertEquals(expected, preProcessed);
        // post process (provide identity transform to force wrap heuristic)
        Geometry postProcessed = handler.postProcess(CRS.findMathTransform(WGS84, WGS84), expected);
        // check the geometry is in the same area as the rendering envelope
        assertEquals(expected, postProcessed);
    }

    @Test
    public void testWrapPDCMercator() throws Exception {
        CoordinateReferenceSystem pdc = CRS.decode("EPSG:3832", true);
        ReferencedEnvelope world = new ReferencedEnvelope(-20000000, 20000000, -20000000, 20000000, pdc);
        Geometry g = new WKTReader()
                .read(
                        "MULTIPOLYGON(((-73 60, -73 83, -11 83, -11 60, -73 60)),((-10 60, -10 61, -11 61, -11 60, -10 60)))");
        Geometry original = g.copy();
        //
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(world, WGS84, true);
        assertTrue(handler.requiresProcessing(g));
        Geometry preProcessed = handler.preProcess(g);
        // no cutting expected
        assertEquals(original, preProcessed);
        // post process (provide identity transform to force wrap heuristic)
        MathTransform mt = CRS.findMathTransform(WGS84, pdc, true);
        Geometry transformed = JTS.transform(g, mt);
        final Geometry postProcessed = handler.postProcess(mt, transformed);
        // make sure we got the geometry unwrapped and replicated
        assertEquals(3, postProcessed.getNumGeometries());
        postProcessed.apply((GeometryComponentFilter) geom -> {
            if (geom != postProcessed && geom.getEnvelopeInternal().getWidth() > 40000000) {
                fail("The geometry did not get rewrapped properly");
            }
        });
    }

    @Test
    public void testReprojectBackwardsTo900913() throws Exception {
        // use a WKT in order to miss the EPSG database support
        String wkt = "PROJCS[\"WGS84 / Google Mercator\", GEOGCS[\"WGS 84\", DATUM[\"World Geodetic System 1984\", "
                + "SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], AUTHORITY[\"EPSG\",\"6326\"]], "
                + "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], UNIT[\"degree\", 0.017453292519943295], AUTHORITY[\"EPSG\",\"4326\"]], "
                + "PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]], PARAMETER[\"semi_major\", 6378137.0], PARAMETER[\"semi_minor\", 6378137.0], "
                + "PARAMETER[\"latitude_of_origin\", 0.0], PARAMETER[\"central_meridian\", 0.0], PARAMETER[\"scale_factor\", 1.0], "
                + "PARAMETER[\"false_easting\", 0.0], PARAMETER[\"false_northing\", 0.0], UNIT[\"m\", 1.0],  AUTHORITY[\"EPSG\",\"900913\"]]";
        CoordinateReferenceSystem epsg900913 = CRS.parseWKT(wkt);

        // assume we are rendering in WGS84
        ReferencedEnvelope renderingArea = new ReferencedEnvelope(-180, 0, 0, 90, DefaultGeographicCRS.WGS84);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(renderingArea, epsg900913, true);
        List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
        assertEquals(1, queryEnvelopes.size());

        // the bisection transformation provides a query envelope, close to the pole
        ReferencedEnvelope qa = queryEnvelopes.get(0);
        assertEquals(-2.0037508342789244E7, qa.getMinX(), 1);
        assertEquals(0, qa.getMaxX(), 1);
        assertEquals(0, qa.getMinY(), 1);
        assertEquals(2.8066337200331915E7, qa.getMaxY(), 1);
    }

    @Test
    public void testQueryEnvelopeOnInvalidArea() throws Exception {
        // override test defaults, this one really needs the checks
        MapProjection.SKIP_SANITY_CHECKS = false;
        try {
            // and Envelope that does not make sense for EPSG:3003, too far away from central
            // meridian
            ReferencedEnvelope re = new ReferencedEnvelope(-130, -120, -40, 30, DefaultGeographicCRS.WGS84);
            ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, CRS.decode("EPSG:3003", true), true);
            List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
            assertEquals(0, queryEnvelopes.size());
        } finally {
            MapProjection.SKIP_SANITY_CHECKS = true;
        }
    }

    @Test
    public void testQueryEnvelopeOnInvalidArea2() throws Exception {
        // override test defaults, this one really needs the checks
        MapProjection.SKIP_SANITY_CHECKS = false;
        try {
            // and Envelope that does not make sense for EPSG:3003, too far away from central
            // meridian
            ReferencedEnvelope re = new ReferencedEnvelope(-130, -120, -40, 30, DefaultGeographicCRS.WGS84);
            ReferencedEnvelope re3857 = re.transform(CRS.decode("EPSG:3857", true), true);
            ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re3857, CRS.decode("EPSG:3003", true), true);
            List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
            assertEquals(0, queryEnvelopes.size());
        } finally {
            MapProjection.SKIP_SANITY_CHECKS = true;
        }
    }

    @Test
    public void testCutGeometryTouchingValidArea() throws Exception {
        // for some reason JTS won't cut correctly this geometry with an zero height, but not empty,
        // reference cutting
        // envelope (result of intersecting the valid area with the geometry own bbox, which results
        // in a "line" polygon
        // Used a BASE64 encoded WKB as WKT is not precise enough to make it happen
        String wkb = "AAAAAAMAAAACAAAAJsBX4dme0ZBrwBBHY3zBphTAV9wm/XQjVMAN5aZ25n+AwFfRnB4QLcDACkbY\n"
                + "u6mfgMBX0IDk68FEwAn9i4xuQADAV9LyxRH1/MAKtRmwO4KAwFfFCYjFNfzABzwNbZWeAMBXro+u\n"
                + "Q3TIwAMozHt2zADAV5nQ8kYDGMAABfJy8QWAwFdz2+lf1Xy/9dkUVMuzAMBXK24mVt9kv9u1veRJ\n"
                + "CADAVxApDvwLFD0wAAAAAAAAwFlKt6hdgbi+jC+LAAAAAMBZS9W86QecwAP6Pg0jSwDAWOUBPzQM\n"
                + "OMAD1MCXZ+6AwFiD2a7LN6jABO4o20CCgMBYcM4alj+AwAVxI3rh+4DAWGFB3LdZJMAGMd3VXb+A\n"
                + "wFhJc+py+vDACDw/gWQHAMBYO+vyzRGUwAnbp1i4wgDAWDEaWTENgMALgYeOOnuAwFgpGBGENtjA\n"
                + "DRepG9PXAMBYIP4dLlt8wA+klG+5IADAWBwX+BP4WMARKUKeEtiAwFgXoZhXNYDAExH5Zal0QMBY\n"
                + "GEP/ANogwBbbF0xESIDAWBN/U/McQMAYXuP5jyKAwFgKqiyQ+gTAGix578FUgMBYA1s+gKpwwBsT\n"
                + "o7wYhYDAV/qZDD5aYMAbwu+JuyoAwFfqj6KbhxTAHIrvGplNAMBX4hOw7Jx0wBy+1+x/XkDAV9vV\n"
                + "ZESR7MAca5H4ZYHAwFfWkK/y7sDAG3TJ1EFXgMBX1+eqod08wBrqABEuC8DAV+jS2XIy3MAYBQgf\n"
                + "RHnAwFfslYpsZkDAFphJ8XbhQMBX6mVp0arUwBOSV/ye1QDAV+HZntGQa8AQR2N8waYUAAAABMBX\n"
                + "4dme0ZBrwBBHY3zBphTAV+GPbx9YxMAQMbM4fB1AwFfivDEmxxzAEIAAI+c6wMBX4dme0ZBrwBBH\n"
                + "Y3zBphQ=";
        Geometry geometry = new WKBReader().read(Base64.decode(wkb));

        CoordinateReferenceSystem lambertPolar = getLambertPolar();
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(
                -14542204.652543461, 15480411.404320458, -18705497.11355389, 11278026.995319324, lambertPolar);
        ReferencedEnvelope validAreaBounds = new ReferencedEnvelope(-180, 180, 0, 90, DefaultGeographicCRS.WGS84);
        ProjectionHandler ph = new ProjectionHandler(DefaultGeographicCRS.WGS84, validAreaBounds, renderingEnvelope);

        // should not return anything
        assertNull(ph.preProcess(geometry));
    }

    @Test
    public void testCutGeometryCrossingValidArea() throws Exception {
        // same as above, but with a different geometry and a different cause
        String wkb = "AAAAAAMAAAABAAAAIMBX4dme0ZBrwBBHY3zBphTAV9GcHhAtwMAKRti7qZ+AwFfQgOTrwUTACf2L\n"
                + "jG5AAMBX0vLFEfX8wAq1GbA7goDAV8UJiMU1/MAHPA1tlZ4AwFeuj65DdMjAAyjMe3bMAMBXc9vp\n"
                + "X9V8v/XZFFTLswDAVytuJlbfZL/btb3kSQgAwFcQKQ78CxQ9MAAAAAAAAMBZSreoXYG4vowviwAA\n"
                + "AADAWUvVvOkHnMAD+j4NI0sAwFjlAT80DDjAA9TAl2fugMBYg9muyzeowATuKNtAgoDAWGFB3LdZ\n"
                + "JMAGMd3VXb+AwFhJc+py+vDACDw/gWQHAMBYMRpZMQ2AwAuBh446e4DAWCkYEYQ22MANF6kb09cA\n"
                + "wFgg/h0uW3zAD6SUb7kgAMBYF6GYVzWAwBMR+WWpdEDAWBhD/wDaIMAW2xdMREiAwFgKqiyQ+gTA\n"
                + "Gix578FUgMBYA1s+gKpwwBsTo7wYhYDAV/qZDD5aYMAbwu+JuyoAwFfqj6KbhxTAHIrvGplNAMBX\n"
                + "4hOw7Jx0wBy+1+x/XkDAV9vVZESR7MAca5H4ZYHAwFfWkK/y7sDAG3TJ1EFXgMBX1+eqod08wBrq\n"
                + "ABEuC8DAV+jS2XIy3MAYBQgfRHnAwFfslYpsZkDAFphJ8XbhQMBX6mVp0arUwBOSV/ye1QDAV+HZ\n"
                + "ntGQa8AQR2N8waYU";
        Geometry geometry = new WKBReader().read(Base64.decode(wkb));

        CoordinateReferenceSystem lambertPolar = getLambertPolar();
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(
                -14542204.652543461, 15480411.404320458, -18705497.11355389, 11278026.995319324, lambertPolar);
        ReferencedEnvelope validAreaBounds = new ReferencedEnvelope(-180, 180, 0, 90, DefaultGeographicCRS.WGS84);
        ProjectionHandler ph = new ProjectionHandler(DefaultGeographicCRS.WGS84, validAreaBounds, renderingEnvelope);

        // should returns something, but not the original geometry
        Geometry preProcessed = ph.preProcess(geometry);
        assertNull(preProcessed);
    }

    private CoordinateReferenceSystem getLambertPolar() throws FactoryException {
        String crsWKT =
                "PROJCS[\"North_Pole_Lambert_Azimuthal_Equal_Area\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\","
                        + "SPHEROID[\"WGS_1984\",6378137,298.257223563]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],"
                        + "PROJECTION[\"Lambert_Azimuthal_Equal_Area\"],PARAMETER[\"False_Easting\",0],PARAMETER[\"False_Northing\",0],"
                        + "PARAMETER[\"Central_Meridian\",0],PARAMETER[\"Latitude_Of_Origin\",90],UNIT[\"Meter\",1]]";
        return CRS.parseWKT(crsWKT);
    }

    @Test
    public void testUTMDatelineWrapping() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32601", true);
        ReferencedEnvelope re = new ReferencedEnvelope(300000, 409800, 5890200, 6000000, crs);

        MathTransform mt = CRS.findMathTransform(crs, WGS84);
        Geometry geom = JTS.toGeometry(re);

        ReferencedEnvelope targetReferenceEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, WGS84);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(targetReferenceEnvelope, crs, true);

        Geometry preProcessed = ph.preProcess(geom);
        Geometry transformed = JTS.transform(preProcessed, mt);
        Geometry postProcessed = ph.postProcess(mt.inverse(), transformed);

        // sits across the dateline and it's "small" (used to cover the entire planet)
        Envelope ppEnvelope = postProcessed.getGeometryN(0).getEnvelopeInternal();
        assertTrue(ppEnvelope.contains(180, 54));
        // the original width is 109km, at this latitude one degree of longitude is only 65km
        assertEquals(1.7, ppEnvelope.getWidth(), 0.1);
    }

    @Test
    public void testMercatorBug() throws NoSuchAuthorityCodeException, FactoryException, TransformException {
        // see GEOT-6141
        CoordinateReferenceSystem sourceCrs = CRS.decode("EPSG:3857", true);
        CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:31370", true);

        ReferencedEnvelope sourceEnv = new ReferencedEnvelope(
                381033.2707188717, 381046.4083331082, 6583847.177786637, 6583860.315400874, sourceCrs);

        ProjectionHandler projectionHandler = ProjectionHandlerFinder.getHandler(sourceEnv, targetCrs, true);

        ReferencedEnvelope targetEnv = projectionHandler.getQueryEnvelopes().get(0);

        assertEquals(83304.59570855058, targetEnv.getMinX(), 0.1);
        assertEquals(83313.02253560493, targetEnv.getMaxX(), 0.1);
        assertEquals(164573.9584101988, targetEnv.getMinY(), 0.1);
        assertEquals(164582.36316849105, targetEnv.getMaxY(), 0.1);
    }

    @Test
    public void testWGS84BackToWebMercator() throws Exception {
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(135, 180, -90, -45, WGS84);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(renderingEnvelope, OSM, true);
        assertNotNull(handler);

        List<ReferencedEnvelope> queryEnvelopes = handler.getQueryEnvelopes();
        assertEquals(1, queryEnvelopes.size());

        ReferencedEnvelope expected = new ReferencedEnvelope(
                1.5028131257091932E7, 2.0037508342789244E7, -3.2487565023661762E7, -5621521.486192067, OSM);
        assertEnvelopesEqual(expected, queryEnvelopes.get(0), EPS);
    }

    @Test
    public void testE50LatLonBackToWebMercator() throws Exception {
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(-80, -45, 135, 180, ED50_LATLON);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(renderingEnvelope, OSM, true);
        assertNotNull(handler);

        List<ReferencedEnvelope> queryEnvelopes = handler.getQueryEnvelopes();
        assertEquals(1, queryEnvelopes.size());

        // the ED50 to WGS84 switch causes dateline switch, making it read a larger area...
        // to be fixed in another commit (a different Jira)
        ReferencedEnvelope expected = new ReferencedEnvelope(
                -2.003748375258002E7, 1.958231203373337E7, -1.5538175797794182E7, -5621345.809658899, OSM);
        assertEnvelopesEqual(expected, queryEnvelopes.get(0), EPS);
    }

    @Test
    public void testE50BackToWebMercator() throws Exception {
        ReferencedEnvelope renderingEnvelope = new ReferencedEnvelope(135, 180, -80, -45, ED50);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(renderingEnvelope, OSM, true);
        assertNotNull(handler);

        List<ReferencedEnvelope> queryEnvelopes = handler.getQueryEnvelopes();
        assertEquals(1, queryEnvelopes.size());

        // the ED50 to WGS84 switch causes dateline switch, making it read a larger area...
        // to be fixed in another commit (a different Jira)
        ReferencedEnvelope expected = new ReferencedEnvelope(
                -2.003748375258002E7, 1.958231203373337E7, -1.5538175797794182E7, -5621345.809658899, OSM);
        assertEnvelopesEqual(expected, queryEnvelopes.get(0), EPS);
    }

    @Test
    public void testOutsideValidArea() throws Exception {
        Geometry g = new WKTReader().read("POLYGON((0 87, 10 87, 10 89, 0 89, 0 87))");
        Geometry original = g.copy();
        ReferencedEnvelope re = new ReferencedEnvelope(-20000000, 20000000, -20000000, 20000000, OSM);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, WGS84, true);
        Geometry preProcessed = ph.preProcess(original);
        assertNull(preProcessed);
    }

    @Test
    public void testLargeObject() throws Exception {
        Geometry g = new WKTReader().read("POLYGON((-96 -2, -96 67, 133 67, 133 -2, -96 -2))");
        Geometry original = g.copy();
        // area large enough to wrap 3 times
        ReferencedEnvelope re = new ReferencedEnvelope(-40000000, 40000000, -20000000, 20000000, OSM);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, WGS84, true);
        Geometry preProcessed = ph.preProcess(original);
        MathTransform mt = CRS.findMathTransform(WGS84, OSM, true);
        Geometry transformed = JTS.transform(preProcessed, mt);
        Geometry postProcessed = ph.postProcess(mt.inverse(), transformed);
        assertThat(postProcessed, CoreMatchers.instanceOf(MultiPolygon.class));
        assertEquals(3, postProcessed.getNumGeometries());
        // and still large objects
        assertEquals(2.54e7, postProcessed.getGeometryN(0).getEnvelopeInternal().getWidth(), 1e5);
        assertEquals(2.54e7, postProcessed.getGeometryN(1).getEnvelopeInternal().getWidth(), 1e5);
        assertEquals(2.54e7, postProcessed.getGeometryN(2).getEnvelopeInternal().getWidth(), 1e5);
    }

    @Test
    public void testLargeObjectSourceInFeet() throws Exception {
        String worldMercatorFeet = "PROJCS[\"World_Mercator\",\n"
                + "    GEOGCS[\"GCS_WGS_1984\",\n"
                + "        DATUM[\"WGS_1984\",\n"
                + "            SPHEROID[\"WGS_1984\",6378137,298.257223563]],\n"
                + "        PRIMEM[\"Greenwich\",0],\n"
                + "        UNIT[\"Degree\",0.017453292519943295]],\n"
                + "    PROJECTION[\"Mercator_1SP\"],\n"
                + "    PARAMETER[\"False_Easting\",0],\n"
                + "    PARAMETER[\"False_Northing\",0],\n"
                + "    PARAMETER[\"Central_Meridian\",0],\n"
                + "    UNIT[\"Foot_US\",0.3048006096012192],\n"
                + "    AUTHORITY[\"EPSG\",\"54004\"]]";
        CoordinateReferenceSystem WOLD_MERCATOR_FEET = CRS.parseWKT(worldMercatorFeet);

        Geometry g = new WKTReader()
                .read(
                        "POLYGON ((-35061186 -725700, -35061186 33191143, 48574352 33191143, 48574352 -725700, -35061186 -725700))");
        Geometry original = g.copy();
        // area large enough to wrap 3 times
        ReferencedEnvelope re = new ReferencedEnvelope(-540, 540, -90, 90, WGS84);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, WOLD_MERCATOR_FEET, true);
        Geometry preProcessed = ph.preProcess(original);
        MathTransform mt = CRS.findMathTransform(WOLD_MERCATOR_FEET, WGS84, true);
        Geometry transformed = JTS.transform(preProcessed, mt);
        Geometry postProcessed = ph.postProcess(mt.inverse(), transformed);
        assertThat(postProcessed, CoreMatchers.instanceOf(MultiPolygon.class));
        assertEquals(3, postProcessed.getNumGeometries());
        // and still large objects
        assertEquals(228, postProcessed.getGeometryN(0).getEnvelopeInternal().getWidth(), 1);
        assertEquals(228, postProcessed.getGeometryN(1).getEnvelopeInternal().getWidth(), 1);
        assertEquals(228, postProcessed.getGeometryN(2).getEnvelopeInternal().getWidth(), 1);
    }

    /** Simulates a client zooming out too much, asking for an area larger than the full world in AZEQ */
    @Test
    public void testAzEqBeyondLimitsCentered() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,0,0", true);
        double beyond = 27000000;
        ReferencedEnvelope re = new ReferencedEnvelope(-beyond, beyond, -beyond, beyond, crs);

        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        assertNotNull(ph);
        List<ReferencedEnvelope> envelopes = ph.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope qe = envelopes.get(0);
        assertEquals(-180, qe.getMinX(), 1e-3);
        assertEquals(-90, qe.getMinY(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
        assertEquals(90, qe.getMaxY(), 1e-3);
    }

    @Test
    public void testAzEqBeyondLimits150_60() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,150,60", true);
        double beyond = 27000000;
        ReferencedEnvelope re = new ReferencedEnvelope(-beyond, beyond, -beyond, beyond, crs);

        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        assertNotNull(ph);
        List<ReferencedEnvelope> envelopes = ph.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope qe = envelopes.get(0);
        assertEquals(-180, qe.getMinX(), 1e-3);
        assertEquals(-90, qe.getMinY(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
        assertEquals(90, qe.getMaxY(), 1e-3);
    }

    @Test
    public void testAzEqBeyondLimitsHalfWorld() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,0,0", true);
        double beyond = 27000000;
        ReferencedEnvelope re = new ReferencedEnvelope(0, beyond, -beyond, beyond, crs);

        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        assertNotNull(ph);
        List<ReferencedEnvelope> envelopes = ph.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope qe = envelopes.get(0);
        assertEquals(0, qe.getMinX(), 1e-3);
        assertEquals(-90, qe.getMinY(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
        assertEquals(90, qe.getMaxY(), 1e-3);
    }

    @Test
    public void testAzEqBeyondLimitsQuarterWorld() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("AUTO:97003,9001,0,0", true);
        double beyond = 27000000;
        ReferencedEnvelope re = new ReferencedEnvelope(0, beyond, 0, beyond, crs);

        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        assertNotNull(ph);
        List<ReferencedEnvelope> envelopes = ph.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope qe = envelopes.get(0);
        assertEquals(0, qe.getMinX(), 2e-1);
        assertEquals(0, qe.getMinY(), 2e-1);
        assertEquals(180, qe.getMaxX(), 1e-3);
        assertEquals(90, qe.getMaxY(), 1e-3);
    }

    @Test
    public void testQueryEnvelopeAcrossDateLine() throws Exception {
        final double minX = 140;
        final double maxX = 200;
        // [140, 200] can be split into
        // [140, 180] AND [180, 200] which is the same as
        // [140, 180] AND [-180, -160]

        ReferencedEnvelope re = new ReferencedEnvelope(minX, maxX, -40, 30, WGS84);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, WGS84, true);
        List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
        assertEquals(2, queryEnvelopes.size());
        ReferencedEnvelope qe = queryEnvelopes.get(1);

        final double wrappedMinX = minX % 360;
        final double wrappedMaxX = maxX % 360;
        assertEquals(-180, qe.getMinX(), 1e-3);
        assertEquals(wrappedMaxX - 360, qe.getMaxX(), 1e-3);

        qe = queryEnvelopes.get(0);
        assertEquals(wrappedMinX, qe.getMinX(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
    }

    @Test
    public void testQueryEnvelopeFarAway() throws Exception {
        final double minX = 2170;
        final double maxX = 2220;
        // [2170, 2220] is the same as [10, 60]

        ReferencedEnvelope re = new ReferencedEnvelope(minX, maxX, -40, 30, WGS84);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, WGS84, true);
        List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
        ReferencedEnvelope qe = queryEnvelopes.get(queryEnvelopes.size() - 1);

        final double wrappedMinX = minX % 360;
        final double wrappedMaxX = maxX % 360;
        assertEquals(wrappedMinX, qe.getMinX(), 1e-3);
        assertEquals(wrappedMaxX, qe.getMaxX(), 1e-3);
    }

    @Test
    public void testQueryEnvelopeAcrossDateLineFarAway() throws Exception {
        final double positiveMinX = 2170;
        final double positiveMaxX = 2380;
        // [2170, 2380] is the same as [10, 220] which can be split
        // into [10, 180] AND [180, 220] which is the same as
        // [10 , 180] AND [-180, -140]

        final double negativeMinX = -2380;
        final double negativeMaxX = -2170;
        final double[] negatives = {negativeMinX, negativeMaxX};
        final double[] positives = {positiveMinX, positiveMaxX};
        final double[][] sets = {negatives, positives};

        boolean negative = true;
        for (double[] set : sets) {
            final double minX = set[0];
            final double maxX = set[1];

            ReferencedEnvelope re = new ReferencedEnvelope(minX, maxX, -40, 30, WGS84);
            ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, WGS84, true);
            List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
            assertEquals(3, queryEnvelopes.size());
            ReferencedEnvelope qe = queryEnvelopes.get(1);

            final double wrappedMinX = minX % 360;
            final double wrappedMaxX = maxX % 360;
            assertEquals(-180, qe.getMinX(), 1e-3);
            assertEquals(wrappedMaxX - (negative ? 0 : 360), qe.getMaxX(), 1e-3);

            qe = queryEnvelopes.get(2);
            assertEquals(wrappedMinX + (negative ? 360 : 0), qe.getMinX(), 1e-3);
            assertEquals(180, qe.getMaxX(), 1e-3);
            negative = false;
        }
    }

    @Test
    public void testQueryEnvelopeOnExtentGreaterThanWholeWorld() throws Exception {
        ReferencedEnvelope re = new ReferencedEnvelope(350, 1000, -40, 30, WGS84);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, OSM, true);
        List<ReferencedEnvelope> queryEnvelopes = ph.getQueryEnvelopes();
        ReferencedEnvelope qe = queryEnvelopes.get(queryEnvelopes.size() - 1);

        // We should get back the whole 3857 domain of validity
        // since we add at least a full whole world span
        MathTransform transform = CRS.findMathTransform(WGS84, OSM);
        Coordinate minX = JTS.transform(new Coordinate(-180, -85), null, transform);
        Coordinate maxX = JTS.transform(new Coordinate(180, -85), null, transform);
        assertEquals(minX.x, qe.getMinX(), 1e-3);
        assertEquals(maxX.x, qe.getMaxX(), 1e-3);

        ph = ProjectionHandlerFinder.getHandler(re, WGS84, true);
        queryEnvelopes = ph.getQueryEnvelopes();
        qe = queryEnvelopes.get(queryEnvelopes.size() - 1);
        assertEquals(-180, qe.getMinX(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
    }

    @Test
    public void testCutGeometryHomolosine() throws Exception {
        String wkt = "PROJCS[\"Homolosine\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\","
                + "6378137,298.257223563 ] ], PRIMEM[\"Greenwich\",0.0], UNIT[\"degree\","
                + "0.01745329251994328 ]],PROJECTION[\"Goode_Homolosine\"],UNIT[\"m\",1.0] ]";
        CoordinateReferenceSystem homolosine = CRS.parseWKT(wkt);
        // get a lambert conformal conic with
        Envelope worldEnvelope = new Envelope(-180, 180, -90, 90);
        ReferencedEnvelope worldWGS84 = new ReferencedEnvelope(worldEnvelope, WGS84);
        ReferencedEnvelope worldHomolosine = worldWGS84.transform(homolosine, true);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(worldHomolosine, WGS84, true);
        // a Geometry that spans the whole world
        Polygon geometry = JTS.toGeometry(worldEnvelope);
        // cut it
        Geometry preProcessed = handler.preProcess(geometry);
        assertEquals(worldEnvelope, preProcessed.getEnvelopeInternal());
        // ensure the breaklines are not part of the geometry
        GeometryFactory gf = geometry.getFactory();
        assertFalse(preProcessed.intersects(lineString(gf, new double[] {-40, 0.1, -40, 90})));
        assertFalse(preProcessed.intersects(lineString(gf, new double[] {-100, -0.1, -100, -90})));
        assertFalse(preProcessed.intersects(lineString(gf, new double[] {-20, -0.1, -20, -90})));
        assertFalse(preProcessed.intersects(lineString(gf, new double[] {80, -0.1, 80, -90})));
    }

    public LineString lineString(GeometryFactory gf, double[] coords) {
        return gf.createLineString(new LiteCoordinateSequence(coords));
    }

    @Test
    public void testRotatedPolarSource() throws Exception {
        CoordinateReferenceSystem rotatedPolar =
                CRS.parseWKT("FITTED_CS[\"rotated_latitude_longitude\", INVERSE_MT[PARAM_MT[\"Rotated_Pole\", "
                        + " PARAMETER[\"semi_major\", 6371229.0],  PARAMETER[\"semi_minor\", "
                        + "6371229.0],  PARAMETER[\"central_meridian\", -106.0],  "
                        + "PARAMETER[\"latitude_of_origin\", 54.0],  PARAMETER[\"scale_factor\", "
                        + "1.0],  PARAMETER[\"false_easting\", 0.0],  "
                        + "PARAMETER[\"false_northing\", 0.0]]],  GEOGCS[\"unknown\", "
                        + "DATUM[\"unknown\",  SPHEROID[\"unknown\", 6371229.0, 0.0]],  "
                        + "PRIMEM[\"Greenwich\", 0.0],  UNIT[\"degree\", 0.017453292519943295],  "
                        + "AXIS[\"Geodetic longitude\", EAST],  AXIS[\"Geodetic latitude\", NORTH]]]");
        Envelope worldEnvelope = new Envelope(-180, 180, -90, 90);
        ReferencedEnvelope worldWGS84 = new ReferencedEnvelope(worldEnvelope, WGS84);
        ProjectionHandler handler = ProjectionHandlerFinder.getHandler(worldWGS84, rotatedPolar, true);
        assertThat(handler, instanceOf(WrappingProjectionHandler.class));
    }

    @Test
    public void testAzEqFalseOrigins() throws Exception {
        String wkt =
                "PROJCS[\"equi7_antarctica\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Azimuthal_Equidistant\"],PARAMETER[\"false_easting\",3714266.97719],PARAMETER[\"false_northing\",3402016.50625],PARAMETER[\"central_meridian\",0.0],PARAMETER[\"latitude_of_origin\",-90.0],UNIT[\"Meter\",1.0]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        double beyond = 27000000;
        double cx = 3714266.97719; // false easting
        double cy = 3402016.50625; // false northing
        ReferencedEnvelope re = new ReferencedEnvelope(cx - beyond, cx + beyond, cy - beyond, cy + beyond, crs);

        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        assertNotNull(ph);
        List<ReferencedEnvelope> envelopes = ph.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope qe = envelopes.get(0);
        assertEquals(-180, qe.getMinX(), 1e-3);
        assertEquals(-90, qe.getMinY(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
        assertEquals(90, qe.getMaxY(), 1e-3);
    }

    @Test
    public void testAzEqPositiveLatOrigin() throws Exception {
        String wkt =
                "PROJCS[\"equi7_asia_nofalseXY\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Azimuthal_Equidistant\"],PARAMETER[\"false_easting\",4340913.84808],PARAMETER[\"false_northing\",4812712.92347],PARAMETER[\"central_meridian\",94.0],PARAMETER[\"latitude_of_origin\",47.0],UNIT[\"Meter\",1.0]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        ReferencedEnvelope re = new ReferencedEnvelope(-12000000, 12000000, -12000000, 12000000, crs);
        ProjectionHandler ph = ProjectionHandlerFinder.getHandler(re, DefaultGeographicCRS.WGS84, false);
        assertNotNull(ph);
        List<ReferencedEnvelope> envelopes = ph.getQueryEnvelopes();
        assertEquals(1, envelopes.size());
        ReferencedEnvelope qe = envelopes.get(0);
        assertEquals(-180, qe.getMinX(), 1e-3);
        // miny used to be a higher number, making the reprojection miss necessary data
        assertEquals(-90, qe.getMinY(), 1e-3);
        assertEquals(180, qe.getMaxX(), 1e-3);
        assertEquals(90, qe.getMaxY(), 1e-3);
    }
}
