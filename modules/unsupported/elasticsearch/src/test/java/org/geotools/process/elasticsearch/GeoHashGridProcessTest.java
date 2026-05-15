/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import static org.junit.Assert.assertEquals;

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.awt.geom.Point2D;
import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.elasticsearch.GeohashUtil;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class GeoHashGridProcessTest {

    private SimpleFeatureCollection features;

    private double fineDelta;

    private GeoHashGridProcess process;

    private FilterFactory ff;

    @Before
    public void setup() throws JacksonException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] aggregation1 = mapper.writeValueAsBytes(
                ImmutableMap.of("key", GeoHash.encodeHash(new LatLong(-89.9, -179.9), 1), "doc_count", 10));
        byte[] aggregation2 = mapper.writeValueAsBytes(
                ImmutableMap.of("key", GeoHash.encodeHash(new LatLong(0.1, 0.1), 1), "doc_count", 10));
        byte[] aggregation3 = mapper.writeValueAsBytes(
                ImmutableMap.of("key", GeoHash.encodeHash(new LatLong(89.9, 179.9), 1), "doc_count", 10));
        features = TestUtil.createAggregationFeatures(ImmutableList.of(
                ImmutableMap.of("_aggregation", aggregation1),
                ImmutableMap.of("_aggregation", aggregation2),
                ImmutableMap.of("_aggregation", aggregation3)));
        fineDelta = 0.45;
        ff = CommonFactoryFinder.getFilterFactory(null);
        process = new GeoHashGridProcess();
    }

    @Test
    public void testBasic() {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
        int width = 8;
        int height = 4;
        String strategy = "Basic";
        Float scaleMin = 0f;

        GridCoverage2D coverage = process.execute(
                features, strategy, null, null, scaleMin, null, false, envelope, width, height, null, "", null);
        checkInternal(coverage, fineDelta);
        checkEdge(coverage, envelope, fineDelta);
    }

    @Test
    public void testScaled() {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
        int width = 16;
        int height = 8;
        String strategy = "Basic";
        Float scaleMin = 0f;

        GridCoverage2D coverage = process.execute(
                features, strategy, null, null, scaleMin, null, false, envelope, width, height, null, "", null);
        checkInternal(coverage, fineDelta);
        checkEdge(coverage, envelope, fineDelta);
    }

    @Test
    public void testSubCellCrop() {
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(-168.75, 168.75, -78.75, 78.75, DefaultGeographicCRS.WGS84);
        int width = 16;
        int height = 8;
        String strategy = "Basic";
        Float scaleMin = 0f;

        GridCoverage2D coverage = process.execute(
                features, strategy, null, null, scaleMin, null, false, envelope, width, height, null, "", null);
        checkInternal(coverage, fineDelta);
        checkEdge(coverage, envelope, fineDelta);
    }

    @Test
    public void testSubCellCropWithSheer() {
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(-168.75, 168.75, -78.75, 78.75, DefaultGeographicCRS.WGS84);
        int width = 900;
        int height = 600;
        String strategy = "Basic";
        Float scaleMin = 0f;

        GridCoverage2D coverage = process.execute(
                features, strategy, null, null, scaleMin, null, false, envelope, width, height, null, "", null);
        checkInternal(coverage, fineDelta);
    }

    @Test
    public void testAutomaticDefinition() throws FactoryException, TransformException {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);

        assertEquals(1, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 8)));
        assertEquals(1, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 16)));
        assertEquals(1, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 32)));
        assertEquals(2, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 128)));
        assertEquals(3, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 256)));
        assertEquals(3, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 720)));
    }

    @Test
    public void testAutomaticDefinitionWebMercator() throws FactoryException, TransformException {
        CoordinateReferenceSystem webMercator = CRS.decode("EPSG:3857", true);
        double wb = 20000000; // world bounds in web mercator
        ReferencedEnvelope envelope = new ReferencedEnvelope(-wb, wb, -wb, wb, webMercator);

        assertEquals(1, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 8)));
        assertEquals(1, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 16)));
        assertEquals(1, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 32)));
        assertEquals(2, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 128)));
        assertEquals(3, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 256)));
        assertEquals(3, (int) GeohashUtil.getPrecision(process.defaultAggregation(envelope, 720)));
    }

    /**
     * StreamingRenderer does not necessarily include a BBOX filter in the query, as it uses the layer definition query.
     * The area of interest is provided as part of the invertQuery parameters instead.
     */
    @Test
    public void testInvertQueryNoBBOX() {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(0, 1, 2, 3, DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, 320, null, query, null);
        assertEquals(ff.bbox("geom", 0, 2, 1, 3, "EPSG:4326"), queryOut.getFilter());
    }

    /** A NPE occurred when the filter did not contain a BBOX filter */
    @Test
    public void testInvertQueryNPE() {
        ReferencedEnvelope env = new ReferencedEnvelope(0, 1, 2, 3, DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(Filter.INCLUDE);
        Query queryOut = process.invertQuery(env, 320, null, query, null);
        assertEquals(ff.bbox("", 0, 2, 1, 3, "EPSG:4326"), queryOut.getFilter());
    }

    @Test
    public void testInvertQuery() {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(0, 1, 2, 3, DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, 320, null, query, null);
        assertEquals(ff.bbox("geom", 0, 2, 1, 3, "EPSG:4326"), queryOut.getFilter());
    }

    @Test
    public void testInvertQueryAcrossDateline() {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(-179, 179, 2, 3, DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, 320, null, query, null);
        assertEquals(ff.bbox("geom", -179, 2, 179, 3, "EPSG:4326"), queryOut.getFilter());
    }

    @Test
    public void testInvertQueryNorthEastAxisOrder() throws Exception {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        CRSAuthorityFactory factory = CRS.getAuthorityFactory(false);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(2, 3, 0, 1, crs);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, 320, null, query, null);
        // potential reprojection detected, padding occurs
        BBOX bbox = (BBOX) queryOut.getFilter();
        assertEquals("geom", ((PropertyName) bbox.getExpression1()).getPropertyName());
        assertEquals(-0.01, bbox.getBounds().getMinX(), 1e-3);
        assertEquals(1.01, bbox.getBounds().getMaxX(), 1e-3);
        assertEquals(1.99, bbox.getBounds().getMinY(), 1e-3);
        assertEquals(3.01, bbox.getBounds().getMaxY(), 1e-3);
    }

    @Test
    public void testInvertQueryWithOtherFilterElement() {
        Filter filter =
                ff.and(ff.equals(ff.property("key"), ff.literal("value")), ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326"));
        ReferencedEnvelope env = new ReferencedEnvelope(0, 1, 2, 3, DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, 320, null, query, null);
        assertEquals(
                ff.and(ff.equals(ff.property("key"), ff.literal("value")), ff.bbox("geom", 0, 2, 1, 3, "EPSG:4326")),
                queryOut.getFilter());
    }

    private void checkInternal(GridCoverage2D coverage, double delta) {
        assertEquals(10, coverage.evaluate(new Point2D.Double(-135 - delta, -45 - delta), new float[1])[0], 1e-10);
        assertEquals(0, coverage.evaluate(new Point2D.Double(-135 + delta, -45 + delta), new float[1])[0], 1e-10);

        assertEquals(0, coverage.evaluate(new Point2D.Double(-delta, -delta), new float[1])[0], 1e-10);
        assertEquals(10, coverage.evaluate(new Point2D.Double(delta, delta), new float[1])[0], 1e-10);
        assertEquals(10, coverage.evaluate(new Point2D.Double(45 - delta, 45 - delta), new float[1])[0], 1e-10);
        assertEquals(0, coverage.evaluate(new Point2D.Double(45 + delta, 45 + delta), new float[1])[0], 1e-10);

        assertEquals(10, coverage.evaluate(new Point2D.Double(135 + delta, 45 + delta), new float[1])[0], 1e-10);
        assertEquals(0, coverage.evaluate(new Point2D.Double(135 - delta, 45 - delta), new float[1])[0], 1e-10);
    }

    private void checkEdge(GridCoverage2D coverage, Envelope env, double delta) {
        assertEquals(
                10,
                coverage.evaluate(new Point2D.Double(env.getMinX() + delta, env.getMinY() + delta), new float[1])[0],
                1e-10);
        assertEquals(
                10,
                coverage.evaluate(new Point2D.Double(env.getMaxX() - delta, env.getMaxY() - delta), new float[1])[0],
                1e-10);
    }
}
