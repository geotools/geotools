/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.Arrays;
import java.util.HashSet;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.FilterFunction_geometryType;
import org.junit.Assert;
import org.junit.Before;
import org.locationtech.jts.geom.Envelope;

public class AbstractPostPreProcessFilterSplittingVisitorTests {
    public static class TestAccessor implements ClientTransactionAccessor {

        private Filter updateFilter;
        private String attribute;

        @Override
        public Filter getDeleteFilter() {
            return null;
        }

        @Override
        public Filter getUpdateFilter(String attributePath) {
            if (attributePath.equals(attribute)) return updateFilter;
            else return null;
        }

        public void setUpdate(String attribute, Filter updateFilter) {
            this.attribute = attribute;
            this.updateFilter = updateFilter;
        }
    }

    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    protected TestAccessor accessor;
    protected static final String typeName = "test";
    protected static final String geomAtt = "geom";
    protected static final String nameAtt = "name";
    protected static final String numAtt = "num";

    @Before
    public void setUp() throws Exception {}

    protected PostPreProcessFilterSplittingVisitor newVisitor(FilterCapabilities supportedCaps) throws SchemaException {
        return new PostPreProcessFilterSplittingVisitor(
                supportedCaps,
                DataUtilities.createType(typeName, geomAtt + ":Point," + nameAtt + ":String," + numAtt + ":int"),
                accessor);
    }

    protected PropertyIsEqualTo createPropertyIsEqualToFilter(String attr, String value) throws IllegalFilterException {
        return ff.equals(ff.property(attr), ff.literal(value));
    }

    protected Envelope createTestEnvelope() {
        return new Envelope(10, 20, 10, 20);
    }

    /**
     * Runs 3 tests. 1 with out filtercapabilities containing filter type. 1 with filter caps containing filter type 1
     * with an edit to the attribute being queried by filter.
     *
     * @param filter filter to process
     * @param filterTypeMask the constant in {@link FilterCapabilities} that is equivalent to the FilterType used in
     *     filter
     * @param attToEdit the attribute in filter that is queried. If null then edit test is not ran.
     */
    protected void runTest(Filter filter, FilterCapabilities supportedCaps, String attToEdit) throws SchemaException {
        // initialize fields that might be previously modified in current test
        PostPreProcessFilterSplittingVisitor visitor = newVisitor(new FilterCapabilities());
        if (accessor != null) accessor.setUpdate("", null);

        // Testing when FilterCapabilites indicate that filter type is not supported
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilterPost());
        Assert.assertEquals(Filter.INCLUDE, visitor.getFilterPre());

        // now filter type is supported
        visitor = newVisitor(supportedCaps);

        filter.accept(visitor, null);

        Assert.assertEquals(Filter.INCLUDE, visitor.getFilterPost());
        Assert.assertEquals(filter, visitor.getFilterPre());

        if (attToEdit != null && accessor != null) {
            // Test when the an update exists that affects the attribute of a
            // feature
            HashSet<FeatureId> idSet = new HashSet<>();
            idSet.add(ff.featureId("fid"));
            Id updateFilter = ff.id(idSet);

            accessor.setUpdate(attToEdit, updateFilter);

            visitor = newVisitor(supportedCaps);

            filter.accept(visitor, null);

            Assert.assertEquals(filter, visitor.getFilterPost());
            Assert.assertEquals(ff.or(filter, updateFilter), visitor.getFilterPre());
        }
    }

    protected PropertyIsEqualTo createFunctionFilter() throws Exception {

        FilterFunction_geometryType geomTypeExpr = new FilterFunction_geometryType();
        geomTypeExpr.setParameters(Arrays.asList(new Expression[] {ff.property("geom")}));

        PropertyIsEqualTo filter = ff.equals(geomTypeExpr, ff.literal("Polygon"));
        return filter;
    }
}
