/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Envelope;

public class GeoHashGridProcessTest {

    private SimpleFeatureCollection features;

    private double fineDelta;

    private GeoHashGridProcess process;

    private FilterFactory ff;

    @Before
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        features = TestUtil.createAggregationFeatures(ImmutableList.of(
                ImmutableMap.of("_aggregation", mapper.writeValueAsBytes(ImmutableMap.of("key",GeoHash.encodeHash(new LatLong(-89.9,-179.9),1),"doc_count",10))),
                ImmutableMap.of("_aggregation", mapper.writeValueAsBytes(ImmutableMap.of("key",GeoHash.encodeHash(new LatLong(0.1,0.1),1),"doc_count",10))),
                ImmutableMap.of("_aggregation", mapper.writeValueAsBytes(ImmutableMap.of("key",GeoHash.encodeHash(new LatLong(89.9,179.9),1),"doc_count",10)))
                ));
        fineDelta = 0.45;
        ff = CommonFactoryFinder.getFilterFactory(null);
        process = new GeoHashGridProcess();
    }

    @Test
    public void testBasic() throws NoSuchAuthorityCodeException, TransformException, FactoryException {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180,180,-90,90,DefaultGeographicCRS.WGS84);
        int width = 8;
        int height = 4;
        int pixelsPerCell = 1;
        String strategy = "Basic";
        List<String> strategyArgs = null;
        Float emptyCellValue = null;
        Float scaleMin = 0f;
        Float scaleMax = null;
        boolean useLog = false;

        GridCoverage2D coverage = process.execute(features, pixelsPerCell, strategy, strategyArgs, emptyCellValue, scaleMin, scaleMax, useLog, envelope, width, height, null);
        checkInternal(coverage, fineDelta);
        checkEdge(coverage, envelope, fineDelta);
    }

    @Test
    public void testScaled() throws NoSuchAuthorityCodeException, TransformException, FactoryException {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180,180,-90,90,DefaultGeographicCRS.WGS84);
        int width = 16;
        int height = 8;
        int pixelsPerCell = 1;
        String strategy = "Basic";
        List<String> strategyArgs = null;
        Float emptyCellValue = null;
        Float scaleMin = 0f;
        Float scaleMax = null;
        boolean useLog = false;

        GridCoverage2D coverage = process.execute(features, pixelsPerCell, strategy, strategyArgs, emptyCellValue, scaleMin, scaleMax, useLog, envelope, width, height, null);
        checkInternal(coverage, fineDelta);
        checkEdge(coverage, envelope, fineDelta);
    }

    @Test
    public void testSubCellCrop() throws NoSuchAuthorityCodeException, TransformException, FactoryException {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-168.75,168.75,-78.75,78.75,DefaultGeographicCRS.WGS84);
        int width = 16;
        int height = 8;
        int pixelsPerCell = 1;
        String strategy = "Basic";
        List<String> strategyArgs = null;
        Float emptyCellValue = null;
        Float scaleMin = 0f;
        Float scaleMax = null;
        boolean useLog = false;

        GridCoverage2D coverage = process.execute(features, pixelsPerCell, strategy, strategyArgs, emptyCellValue, scaleMin, scaleMax, useLog, envelope, width, height, null);
        checkInternal(coverage, fineDelta);
        checkEdge(coverage, envelope, fineDelta);
    }

    @Test
    public void testSubCellCropWithSheer() throws NoSuchAuthorityCodeException, TransformException, FactoryException {
        ReferencedEnvelope envelope = new ReferencedEnvelope(-168.75,168.75,-78.75,78.75,DefaultGeographicCRS.WGS84);
        int width = 900;
        int height = 600;
        int pixelsPerCell = 1;
        String strategy = "Basic";
        List<String> strategyArgs = null;
        Float emptyCellValue = null;
        Float scaleMin = 0f;
        Float scaleMax = null;
        boolean useLog = false;

        GridCoverage2D coverage = process.execute(features, pixelsPerCell, strategy, strategyArgs, emptyCellValue, scaleMin, scaleMax, useLog, envelope, width, height, null);
        checkInternal(coverage, fineDelta);
    }

    @Test
    public void testInvertQuery() throws Exception {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(0,1,2,3,DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, query, null);
        assertEquals(ff.bbox("geom", 0, 2, 1, 3, "EPSG:4326"), queryOut.getFilter());
    }

    @Test
    public void testInvertQueryAcrossDateline() throws Exception {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(-179,179,2,3,DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, query, null);
        assertEquals(ff.bbox("geom", -179, 2, 179, 3, "EPSG:4326"), queryOut.getFilter());
    }

    @Test
    public void testInvertQueryNorthEastAxisOrder() throws Exception {
        Filter filter = ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326");
        CRSAuthorityFactory   factory = CRS.getAuthorityFactory(false);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
        ReferencedEnvelope env = new ReferencedEnvelope(2,3,0,1,crs);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, query, null);
        assertEquals(ff.bbox("geom", 0, 2, 1, 3, "EPSG:4326"), queryOut.getFilter());
    }

    @Test
    public void testInvertQueryWithOtherFilterElement() {
        Filter filter = ff.and(ff.equals(ff.property("key"), ff.literal("value")), ff.bbox("geom", 0, 0, 0, 0, "EPSG:4326"));
        ReferencedEnvelope env = new ReferencedEnvelope(0,1,2,3,DefaultGeographicCRS.WGS84);
        Query query = new Query();
        query.setFilter(filter);
        Query queryOut = process.invertQuery(env, query, null);
        assertEquals(ff.and(ff.equals(ff.property("key"), ff.literal("value")), ff.bbox("geom", 0, 2, 1, 3, "EPSG:4326")), queryOut.getFilter());
    }

    private void checkInternal(GridCoverage2D coverage, double delta) {
        assertEquals(10, coverage.evaluate(new Point2D.Double(-135-delta, -45-delta), new float[1])[0],1e-10);
        assertEquals(0, coverage.evaluate(new Point2D.Double(-135+delta, -45+delta), new float[1])[0],1e-10);

        assertEquals(0, coverage.evaluate(new Point2D.Double(-delta, -delta), new float[1])[0],1e-10);
        assertEquals(10, coverage.evaluate(new Point2D.Double(delta, delta), new float[1])[0],1e-10);
        assertEquals(10, coverage.evaluate(new Point2D.Double(45-delta, 45-delta), new float[1])[0],1e-10);
        assertEquals(0, coverage.evaluate(new Point2D.Double(45+delta, 45+delta), new float[1])[0],1e-10);

        assertEquals(10, coverage.evaluate(new Point2D.Double(135+delta, 45+delta), new float[1])[0],1e-10);
        assertEquals(0, coverage.evaluate(new Point2D.Double(135-delta, 45-delta), new float[1])[0],1e-10);
    }

    private void checkEdge(GridCoverage2D coverage, Envelope env, double delta) {
        assertEquals(10, coverage.evaluate(new Point2D.Double(env.getMinX()+delta, env.getMinY()+delta), new float[1])[0],1e-10);
        assertEquals(10, coverage.evaluate(new Point2D.Double(env.getMaxX()-delta, env.getMaxY()-delta), new float[1])[0],1e-10);
    }

}
