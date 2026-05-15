/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.slice.DimensionBindingResolver;
import org.geotools.imageio.netcdf.slice.NetCDFDimensionBindingResolver;
import org.geotools.imageio.netcdf.slice.NetCDFSliceProvider;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.CompositeDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ExactDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ListFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.RangeFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter;
import org.geotools.imageio.netcdf.slice.filter.SplitFilterResult;
import org.junit.Before;
import org.junit.Test;

public class DimensionFilterSplitterTest {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    private VariableAdapter adapter;
    private List<NetCDFSliceProvider.AdditionalDomainBinding> additionalBindings;
    private DimensionBindingResolver resolver;

    @Before
    public void setup() {
        adapter = mock(VariableAdapter.class);

        when(adapter.getNDimensionIndex(VariableAdapter.T)).thenReturn(0);
        when(adapter.getNDimensionIndex(VariableAdapter.Z)).thenReturn(1);
        when(adapter.getNDimensionIndex(2)).thenReturn(2);

        additionalBindings =
                List.of(new NetCDFSliceProvider.AdditionalDomainBinding("runtime", "runtime", 2, DomainType.NUMBER));
        resolver = new NetCDFDimensionBindingResolver(adapter, "time", "elevation", additionalBindings);
    }

    @Test
    public void testSplitNullReturnsEmptyResult() {
        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(null);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testSplitIncludeReturnsEmptyResult() {
        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(Filter.INCLUDE);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testSplitExcludeReturnsPostExclude() {
        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(Filter.EXCLUDE);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.EXCLUDE, result.postFilter());
    }

    @Test
    public void testTimeEqualityBecomesExactDimensionFilter() {
        Date time = new Date(1000L);
        Filter filter = FF.equals(FF.property("time"), FF.literal(time));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.time() instanceof ExactDimensionFilter);
        ExactDimensionFilter exact = (ExactDimensionFilter) result.time();
        assertEquals(time, exact.expected());

        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testElevationBetweenBecomesRangeFilter() {
        Filter filter = FF.between(FF.property("elevation"), FF.literal(100.0), FF.literal(200.0));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.elevation() instanceof RangeFilter);
        RangeFilter range = (RangeFilter) result.elevation();

        assertEquals(100.0, ((Number) range.getLower()).doubleValue(), 0d);
        assertEquals(200.0, ((Number) range.getUpper()).doubleValue(), 0d);
        assertTrue(range.isLowerInclusive());
        assertTrue(range.isUpperInclusive());

        assertSame(DimensionFilter.ALL, result.time());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testGreaterAndLessOnSameDimensionAreMerged() {
        Filter filter = FF.and(
                FF.greaterOrEqual(FF.property("elevation"), FF.literal(100.0)),
                FF.less(FF.property("elevation"), FF.literal(200.0)));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.elevation() instanceof CompositeDimensionFilter);
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testAdditionalDimensionEqualityIsRecognized() {
        Filter filter = FF.equals(FF.property("runtime"), FF.literal(6.0));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(filter);

        assertEquals(1, result.additional().size());
        DimensionFilter.AdditionalDimensionFilter adf = result.additional().get(0);
        assertEquals(2, adf.dimensionIndex());
        assertTrue(adf.filter() instanceof ExactDimensionFilter);

        ExactDimensionFilter exact = (ExactDimensionFilter) adf.filter();
        assertEquals(6.0, ((Number) exact.expected()).doubleValue(), 0d);

        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testSupportedOrOnSameAttributeBecomesListFilter() {
        Date t1 = new Date(1000L);
        Date t2 = new Date(2000L);

        Filter filter =
                FF.or(FF.equals(FF.property("time"), FF.literal(t1)), FF.equals(FF.property("time"), FF.literal(t2)));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.time() instanceof ListFilter);
        ListFilter list = (ListFilter) result.time();

        assertEquals(2, list.getValues().size());
        assertTrue(list.getValues().contains(t1));
        assertTrue(list.getValues().contains(t2));
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testCaseInsensitiveAttributeMatching() {
        Date time = new Date(1000L);
        Filter filter = FF.equals(FF.property("  TiMe "), FF.literal(time));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.time() instanceof ExactDimensionFilter);
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testUnknownAttributeStaysInPostFilter() {
        Filter filter = FF.equals(FF.property("station"), FF.literal("abc"));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());

        assertNotSame(Filter.INCLUDE, result.postFilter());
        assertEquals(filter, result.postFilter());
    }

    @Test
    public void testKnownAndUnknownPredicatesAreSplit() {
        Date time = new Date(1000L);
        Filter known = FF.equals(FF.property("time"), FF.literal(time));
        Filter unknown = FF.equals(FF.property("station"), FF.literal("abc"));
        Filter filter = FF.and(known, unknown);

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);

        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.time() instanceof ExactDimensionFilter);
        assertNotSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testOrAcrossDifferentAttributesFallsBackToPostFilter() {
        Date time = new Date(1000L);
        Filter filter = FF.or(
                FF.equals(FF.property("time"), FF.literal(time)), FF.equals(FF.property("runtime"), FF.literal(6.0)));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertNotSame(Filter.INCLUDE, result.postFilter());
        assertEquals(filter, result.postFilter());
    }

    @Test
    public void testMissingTimeDimensionDoesNotRecognizeTimeAttribute() {
        when(adapter.getNDimensionIndex(VariableAdapter.T)).thenReturn(-1);
        NetCDFDimensionBindingResolver resolver =
                new NetCDFDimensionBindingResolver(adapter, "time", "elevation", additionalBindings);

        Filter filter = FF.equals(FF.property("time"), FF.literal(new Date(1000L)));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertSame(DimensionFilter.ALL, result.time());
        assertNotSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testNotFilterIsNotExtractedAsPrefilter() {
        Date time = new Date(1000L);
        Filter inner = FF.equals(FF.property("time"), FF.literal(time));
        Filter filter = FF.not(inner);

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertEquals(filter, result.postFilter());
    }

    @Test
    public void testLikeFilterIsNotExtractedAsPrefilter() {
        Filter filter = FF.like(FF.property("runtime"), "6*");

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertEquals(filter, result.postFilter());
    }

    @Test
    public void testUnsupportedFunctionExpressionIsNotExtractedAsPrefilter() {
        Filter filter =
                FF.equals(FF.function("strConcat", FF.property("time"), FF.literal("_x")), FF.literal("anything"));

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertEquals(filter, result.postFilter());
    }

    @Test
    public void testUnsupportedFilterInsideAndKeepsSupportedPartOnlyAsPrefilter() {
        Date time = new Date(1000L);
        Filter supported = FF.equals(FF.property("time"), FF.literal(time));
        Filter unsupported = FF.not(FF.equals(FF.property("runtime"), FF.literal(6.0)));
        Filter filter = FF.and(supported, unsupported);

        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        SplitFilterResult result = splitter.split(filter);

        assertTrue(result.time() instanceof ExactDimensionFilter);
        ExactDimensionFilter exact = (ExactDimensionFilter) result.time();
        assertEquals(time, exact.expected());

        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertEquals(unsupported, result.postFilter());
    }
}
