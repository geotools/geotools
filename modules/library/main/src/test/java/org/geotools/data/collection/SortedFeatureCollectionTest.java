/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.util.Comparator;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.FeatureCollectionWrapperTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.SortedSimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class SortedFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    public void testNaturalSort() throws Exception {
        SortedSimpleFeatureCollection sorted = new SortedSimpleFeatureCollection(delegate,
                new SortBy[] { SortBy.NATURAL_ORDER });
        checkSorted(sorted, DataUtilities.sortComparator(SortBy.NATURAL_ORDER));
    }

    public void testReverseSort() throws Exception {
        SortedSimpleFeatureCollection sorted = new SortedSimpleFeatureCollection(delegate,
                new SortBy[] { SortBy.REVERSE_ORDER });
        checkSorted(sorted, DataUtilities.sortComparator(SortBy.REVERSE_ORDER));
    }

    public void testSortAttribute() throws Exception {
        SortBy sort = ff.sort("someAtt", SortOrder.ASCENDING);
        SortedSimpleFeatureCollection sorted = new SortedSimpleFeatureCollection(delegate,
                new SortBy[] { sort });
        checkSorted(sorted, DataUtilities.sortComparator(sort));
    }

    private void checkSorted(SortedSimpleFeatureCollection sorted,
            Comparator<SimpleFeature> comparator) {
        SimpleFeatureIterator fi = sorted.features();
        SimpleFeature prev = null;
        while (fi.hasNext()) {
            SimpleFeature curr = fi.next();
            if (prev != null) {
                assertTrue("Failed on " + prev + " / " + curr, comparator.compare(prev, curr) <= 0);
            }
            prev = curr;
        }
        fi.close();
    }

}
