/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex;

import java.util.Arrays;
import java.util.List;
import org.geotools.appschema.util.IndexQueryUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/** @author Fernando Miño, Geosolutions */
public abstract class IndexesTest {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    protected final String attId = "st:Station";
    protected final String attName = "st:Station/st:name";
    protected final String attLocationName = "st:Station/st:location/st:name";

    protected Filter partialIndexedFilter() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.and(totallyIndexedFilter(), ff.like(ff.property(attLocationName), "*fer*"));
        return filter;
    }

    protected Filter totallyIndexedFilter() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.or(
                        ff.equals(ff.property(attId), ff.literal("st.1")),
                        ff.like(ff.property(attName), "*fer*"));
        return filter;
    }

    protected Filter totallyIndexedFilter2() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter filter =
                ff.or(
                        ff.equals(ff.property(attName), ff.literal("fer")),
                        ff.like(ff.property(attName), "*mariela*"));
        return filter;
    }

    protected Filter partialIndexedFilter_2idxfilterResults() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        List<Filter> filters =
                Arrays.asList(
                        totallyIndexedFilter(),
                        ff.like(ff.property(attLocationName), "*fer*"),
                        totallyIndexedFilter2());
        Filter filter = ff.and(filters);
        return filter;
    }

    protected Filter buildIdInExpression(List<String> idValues, FeatureTypeMapping mapping) {
        return IndexQueryUtils.buildIdInExpression(idValues, mapping);
    }
}
