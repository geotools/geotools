package org.geotools.coverage.io.netcdf.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.AdditionalDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.CompositeDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ExactDimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.ListFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter.RangeFilter;
import org.geotools.imageio.netcdf.slice.filter.PreFilterDimensionExtractor;
import org.geotools.imageio.netcdf.slice.filter.SplitFilterResult;
import org.junit.Before;
import org.junit.Test;

public class PreFilterDimensionExtractorTest {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    private DimensionBindingResolver resolver;

    @Before
    public void setup() {
        VariableAdapter adapter = mock(VariableAdapter.class);

        when(adapter.getNDimensionIndex(VariableAdapter.T)).thenReturn(0);
        when(adapter.getNDimensionIndex(VariableAdapter.Z)).thenReturn(1);
        when(adapter.getNDimensionIndex(2)).thenReturn(2);

        List<NetCDFSliceProvider.AdditionalDomainBinding> additionalBindings =
                List.of(new NetCDFSliceProvider.AdditionalDomainBinding("runtime", "runtime", 2, DomainType.NUMBER));

        resolver = new NetCDFDimensionBindingResolver(adapter, "time", "elevation", additionalBindings);
    }

    @Test
    public void testExtractNullPrefilterReturnsDefaults() {
        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);

        SplitFilterResult result = extractor.extract(null, null);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.INCLUDE, result.postFilter());
    }

    @Test
    public void testExtractIncludePrefilterReturnsDefaults() {
        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);

        SplitFilterResult result = extractor.extract(Filter.INCLUDE, Filter.EXCLUDE);

        assertSame(DimensionFilter.ALL, result.time());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
        assertSame(Filter.EXCLUDE, result.postFilter());
    }

    @Test
    public void testTimeEqualityBecomesExactFilter() {
        Date time = new Date(1000L);
        Filter pre = FF.equals(FF.property("time"), FF.literal(time));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.time() instanceof ExactDimensionFilter);
        ExactDimensionFilter exact = (ExactDimensionFilter) result.time();
        assertEquals(time, exact.expected());
        assertSame(DimensionFilter.ALL, result.elevation());
        assertTrue(result.additional().isEmpty());
    }

    @Test
    public void testBetweenBecomesInclusiveRangeFilter() {
        Filter pre = FF.between(FF.property("elevation"), FF.literal(10.0), FF.literal(20.0));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.elevation() instanceof RangeFilter);
        RangeFilter range = (RangeFilter) result.elevation();
        assertEquals(10.0, ((Number) range.getLower()).doubleValue(), 0d);
        assertEquals(20.0, ((Number) range.getUpper()).doubleValue(), 0d);
        assertTrue(range.isLowerInclusive());
        assertTrue(range.isUpperInclusive());
    }

    @Test
    public void testLessThanBecomesUpperExclusiveRange() {
        Filter pre = FF.less(FF.property("elevation"), FF.literal(20.0));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.elevation() instanceof RangeFilter);
        RangeFilter range = (RangeFilter) result.elevation();
        assertNull(range.getLower());
        assertEquals(20.0, ((Number) range.getUpper()).doubleValue(), 0d);
        assertTrue(range.isLowerInclusive());
        assertFalse(range.isUpperInclusive());
    }

    @Test
    public void testGreaterOrEqualBecomesLowerInclusiveRange() {
        Filter pre = FF.greaterOrEqual(FF.property("elevation"), FF.literal(5.0));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.elevation() instanceof RangeFilter);
        RangeFilter range = (RangeFilter) result.elevation();
        assertEquals(5.0, ((Number) range.getLower()).doubleValue(), 0d);
        assertNull(range.getUpper());
        assertTrue(range.isLowerInclusive());
        assertTrue(range.isUpperInclusive());
    }

    @Test
    public void testOrEqualitiesBecomeListFilter() {
        Date t1 = new Date(1000L);
        Date t2 = new Date(2000L);
        Filter pre =
                FF.or(FF.equals(FF.property("time"), FF.literal(t1)), FF.equals(FF.property("time"), FF.literal(t2)));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.time() instanceof ListFilter);
        ListFilter list = (ListFilter) result.time();
        assertEquals(2, list.getValues().size());
        assertTrue(list.getValues().contains(t1));
        assertTrue(list.getValues().contains(t2));
    }

    @Test
    public void testAdditionalDimensionEqualityIsCollected() {
        Filter pre = FF.equals(FF.property("runtime"), FF.literal(6.0));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertEquals(1, result.additional().size());
        AdditionalDimensionFilter adf = result.additional().get(0);
        assertEquals(2, adf.dimensionIndex());
        assertTrue(adf.filter() instanceof ExactDimensionFilter);
    }

    @Test
    public void testAndAcrossDimensionsPopulatesAllBuckets() {
        Date time = new Date(1000L);

        Filter pre = FF.and(List.of(
                FF.equals(FF.property("time"), FF.literal(time)),
                FF.between(FF.property("elevation"), FF.literal(10.0), FF.literal(20.0)),
                FF.equals(FF.property("runtime"), FF.literal(6.0))));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.time() instanceof ExactDimensionFilter);
        assertTrue(result.elevation() instanceof RangeFilter);
        assertEquals(1, result.additional().size());
    }

    @Test
    public void testAndOnSameDimensionMergesFilters() {
        Filter pre = FF.and(
                FF.greaterOrEqual(FF.property("elevation"), FF.literal(10.0)),
                FF.less(FF.property("elevation"), FF.literal(20.0)));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertTrue(result.elevation() instanceof CompositeDimensionFilter);
    }

    @Test
    public void testAdditionalFiltersOnSameDimensionAreMerged() {
        Filter pre = FF.and(
                FF.greaterOrEqual(FF.property("runtime"), FF.literal(2.0)),
                FF.lessOrEqual(FF.property("runtime"), FF.literal(8.0)));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        SplitFilterResult result = extractor.extract(pre, Filter.INCLUDE);

        assertEquals(1, result.additional().size());
        assertEquals(2, result.additional().get(0).dimensionIndex());
        assertTrue(result.additional().get(0).filter() instanceof CompositeDimensionFilter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithNonEqualityChildThrows() {
        Filter pre = FF.or(
                FF.equals(FF.property("time"), FF.literal(new Date(1000L))),
                FF.less(FF.property("time"), FF.literal(new Date(2000L))));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        extractor.extract(pre, Filter.INCLUDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithMixedAttributesThrows() {
        Filter pre = FF.or(
                FF.equals(FF.property("time"), FF.literal(new Date(1000L))),
                FF.equals(FF.property("runtime"), FF.literal(6.0)));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        extractor.extract(pre, Filter.INCLUDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownAttributeThrows() {
        Filter pre = FF.equals(FF.property("station"), FF.literal("abc"));

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        extractor.extract(pre, Filter.INCLUDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedPrefilterStructureThrows() {
        Filter pre = Filter.EXCLUDE;

        PreFilterDimensionExtractor extractor = new PreFilterDimensionExtractor(resolver);
        extractor.extract(pre, Filter.INCLUDE);
    }
}
