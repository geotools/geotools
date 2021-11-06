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
package org.geotools.feature.collection;

import java.io.IOException;
import org.easymock.EasyMock;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.visitor.UniqueVisitor;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;

public class DecoratingFeatureCollectionTest {

    @Test
    public void testDelegateVisitor() throws IOException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        UniqueVisitor visitor = new UniqueVisitor(ff.property("test"));

        FeatureCollection<FeatureType, Feature> mock = EasyMock.createMock(FeatureCollection.class);
        mock.accepts(visitor, null);
        EasyMock.expectLastCall();
        EasyMock.replay(mock);

        DecoratingFeatureCollection decorator =
                new DecoratingFeatureCollection<FeatureType, Feature>(mock) {
                    @Override
                    protected boolean canDelegate(FeatureVisitor visitor) {
                        return true;
                    }
                };
        decorator.accepts(visitor, null);
        EasyMock.verify(mock);
    }

    @Test
    public void testDontDelegateVisitor() throws IOException {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        UniqueVisitor visitor = new UniqueVisitor(ff.property("test"));

        FeatureCollection<FeatureType, Feature> mock = EasyMock.createMock(FeatureCollection.class);
        @SuppressWarnings("PMD.CloseResource")
        FeatureIterator<Feature> iterator = EasyMock.createNiceMock(FeatureIterator.class);
        EasyMock.expect(mock.features()).andReturn(iterator);
        EasyMock.replay(mock, iterator);

        DecoratingFeatureCollection decorator =
                new DecoratingFeatureCollection<FeatureType, Feature>(mock) {
                    @Override
                    protected boolean canDelegate(FeatureVisitor visitor) {
                        return false;
                    }
                };
        decorator.accepts(visitor, null);
        EasyMock.verify(mock);
    }
}
