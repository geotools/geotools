/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb;

import java.util.Collections;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.spatial.DWithinImpl;
import org.geotools.filter.spatial.IntersectsImpl;
import org.junit.Assert;
import org.junit.Test;

public class MongoFilterSplitterTest {

    private static final DWithinImpl D_WITHIN_POINT =
            new DWithinImpl(
                    new AttributeExpressionImpl("geometry"),
                    new LiteralExpressionImpl("POINT (5.006253 60.701807)"));

    private static final DWithinImpl D_WITHIN_LINE =
            new DWithinImpl(
                    new AttributeExpressionImpl("geometry"),
                    new LiteralExpressionImpl(
                            "LINESTRING (1.669922 42.617791, 9.667969 47.100045, 8.085938 52.160455)"));

    private static final IntersectsImpl INTERSECTS =
            new IntersectsImpl(
                    new AttributeExpressionImpl("geometry"),
                    new LiteralExpressionImpl(
                            "POLYGON ((10.567627244276403 60.81420914025842, 10.787353806776403 60.81420914025842, 10.787353806776403 61.03393570275842, 10.567627244276403 61.03393570275842, 10.567627244276403 60.81420914025842))"));

    private static final FilterCapabilities FCS = new FilterCapabilities(DWithin.class);

    @Test
    public void testDWithinSplitWithCorrectIndex() {
        MongoFilterSplitter splitter =
                new MongoFilterSplitter(
                        FCS,
                        null,
                        null,
                        new MongoCollectionMeta(Collections.singletonMap("geometry", "2dsphere")));
        splitter.visit(D_WITHIN_POINT, null);
        Assert.assertEquals(D_WITHIN_POINT, splitter.getFilterPre());
    }

    @Test
    public void testDWithinSplitIncorrectIndex() {
        MongoFilterSplitter splitter =
                new MongoFilterSplitter(
                        FCS,
                        null,
                        null,
                        new MongoCollectionMeta(Collections.singletonMap("_id", "1")));
        splitter.visit(D_WITHIN_POINT, null);
        Assert.assertEquals(D_WITHIN_POINT, splitter.getFilterPost());
    }

    @Test
    public void testDWithinSplitWithoutIndex() {
        MongoFilterSplitter splitter = new MongoFilterSplitter(FCS, null, null, null);
        splitter.visit(D_WITHIN_POINT, null);
        Assert.assertEquals(D_WITHIN_POINT, splitter.getFilterPost());
    }

    @Test
    public void testDWithinSplitLinestring() {
        MongoFilterSplitter splitter = new MongoFilterSplitter(FCS, null, null, null);
        splitter.visit(D_WITHIN_LINE, null);
        Assert.assertEquals(D_WITHIN_LINE, splitter.getFilterPre());
    }

    @Test
    public void testIntersects() {
        MongoFilterSplitter splitter = new MongoFilterSplitter(FCS, null, null, null);
        splitter.visit(INTERSECTS, null);
        Assert.assertEquals(INTERSECTS, splitter.getFilterPost());
    }
}
