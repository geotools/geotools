/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.dggs;

import static org.junit.Assert.assertEquals;

import com.uber.h3core.H3Core;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.data.DataUtilities;
import org.geotools.dggs.gstore.DGGSResolutionCalculator;
import org.geotools.dggs.h3.H3DGGSInstance;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class DGGSFilterTransformerTest {

    FilterFactory FF = CommonFactoryFinder.getFilterFactory();
    DGGSFilterTransformer transformer;
    private H3DGGSInstance dggs;

    @Before
    public void setupTransformer() throws Exception {
        dggs = new H3DGGSInstance(H3Core.newInstance());
        DGGSResolutionCalculator resolutionCalculator = new DGGSResolutionCalculator(dggs, null);
        AttributeDescriptor zoneIdDescriptor =
                DataUtilities.createType("test", "zoneId:String").getDescriptor("zoneId");
        transformer = new DGGSFilterTransformer(dggs, resolutionCalculator, 3, zoneIdDescriptor);
    }

    @Test
    public void testPolygon() throws Exception {
        Intersects intersects = FF.intersects("geom", read("POLYGON((12 45, 12 46, 13 46, 13 45, 12 45))"));
        Filter transformed = (Filter) intersects.accept(transformer, null);
        PropertyIsEqualTo inZone = FF.equal(
                FF.function("in", FF.property("zoneId"), FF.literal("831ea5fffffffff")), FF.literal(true), true);
        PropertyIsEqualTo matchResolution = FF.equal(FF.property("resolution"), FF.literal(3), true);
        Filter expected = FF.and(inZone, matchResolution);
        assertEquals(expected, transformed);
    }

    @Test
    public void testMultiPolygon() throws Exception {
        Intersects intersects = FF.intersects(
                "geom",
                read("MULTIPOLYGON(((11 45, 11 46, 12 46, 12 45, 11 45)), ((12 45, 12 46, 13 46, 13 45, 12 45)))"));
        Filter transformed = (Filter) intersects.accept(transformer, null);
        PropertyIsEqualTo inZone = FF.equal(
                FF.function("in", FF.property("zoneId"), FF.literal("831ea4fffffffff"), FF.literal("831ea5fffffffff")),
                FF.literal(true),
                true);
        PropertyIsEqualTo matchResolution = FF.equal(FF.property("resolution"), FF.literal(3), true);
        Filter expected = FF.and(inZone, matchResolution);
        assertEquals(transformed, expected);
    }

    @Test
    public void testFixedResolution() throws Exception {
        DGGSResolutionCalculator resolutionCalculator = new DGGSResolutionCalculator(dggs, 3);
        AttributeDescriptor zoneIdDescriptor =
                DataUtilities.createType("test", "zoneId:String").getDescriptor("zoneId");
        DGGSFilterTransformer transformer = new DGGSFilterTransformer(dggs, resolutionCalculator, 3, zoneIdDescriptor);

        Intersects intersects = FF.intersects(
                "geom",
                read("MULTIPOLYGON(((11 45, 11 46, 12 46, 12 45, 11 45)), ((12 45, 12 46, 13 46, 13 45, 12 45)))"));
        Filter transformed = (Filter) intersects.accept(transformer, null);
        PropertyIsEqualTo inZone = FF.equal(
                FF.function("in", FF.property("zoneId"), FF.literal("831ea4fffffffff"), FF.literal("831ea5fffffffff")),
                FF.literal(true),
                true);
        Filter fixedResolutionFilter = Filter.INCLUDE;
        Filter expected = FF.and(inZone, fixedResolutionFilter);
        assertEquals(transformed, expected);
    }

    @Test
    public void testFixedResolutionOnIntegerType() throws Exception {
        DGGSResolutionCalculator resolutionCalculator = new DGGSResolutionCalculator(dggs, 3);
        AttributeDescriptor zoneIdDescriptor =
                DataUtilities.createType("test", "zoneId:Integer").getDescriptor("zoneId");
        DGGSFilterTransformer transformer = new DGGSFilterTransformer(dggs, resolutionCalculator, 3, zoneIdDescriptor);

        Intersects intersects = FF.intersects(
                "geom",
                read("MULTIPOLYGON(((11 45, 11 46, 12 46, 12 45, 11 45)), ((12 45, 12 46, 13 46, 13 45, 12 45)))"));
        Filter transformed = (Filter) intersects.accept(transformer, null);
        PropertyIsEqualTo inZone = FF.equal(
                FF.function(
                        "in",
                        FF.property("zoneId"),
                        FF.literal(Long.parseUnsignedLong("831ea4fffffffff", 16)),
                        FF.literal(Long.parseUnsignedLong("831ea5fffffffff", 16))),
                FF.literal(true),
                true);
        Filter fixedResolutionFilter = Filter.INCLUDE;
        Filter expected = FF.and(inZone, fixedResolutionFilter);
        assertEquals(transformed, expected);
    }

    private static Geometry read(String wellKnownText) throws ParseException {
        return new WKTReader().read(wellKnownText);
    }

    @After
    public void tearDown() {
        if (dggs != null) {
            dggs.close();
        }
    }
}
