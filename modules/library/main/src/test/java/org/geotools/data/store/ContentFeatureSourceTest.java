/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class ContentFeatureSourceTest {

    /** Mock feature type name. */
    protected static final Name TYPENAME = new NameImpl("http://www.geotools.org", "Mock");

    /** Mock feature type. */
    protected static final SimpleFeatureType TYPE = buildType();

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /** Build the test type. */
    protected static SimpleFeatureType buildType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(TYPENAME);
        builder.add("geom", LineString.class);
        builder.add("name", String.class);
        builder.add("z", Integer.class);
        builder.add("cat", String.class);
        return builder.buildFeatureType();
    }

    @Test
    public void testRetypeCannotSortAll() throws Exception {
        checkRetypeCannotSort(Query.ALL, Query.ALL);
    }

    @Test
    public void testRetypeCannotSortCovered() throws Exception {
        Query q = new Query();
        q.setPropertyNames(new String[] {"name", "z"});
        q.setSortBy(new SortBy[] {ff.sort("z", SortOrder.ASCENDING)});
        checkRetypeCannotSort(q, q);
    }

    @Test
    public void testRetypeCannotSortPartiallyCovered() throws Exception {
        Query q = new Query();
        q.setPropertyNames(
                new String[] {
                    "name",
                });
        q.setSortBy(
                new SortBy[] {
                    ff.sort("name", SortOrder.ASCENDING), ff.sort("z", SortOrder.ASCENDING)
                });
        Query expected = new Query(q);
        expected.setPropertyNames(new String[] {"name", "z"});
        checkRetypeCannotSort(q, expected);
    }

    @Test
    public void testRetypeCannotSortFullyCovered() throws Exception {
        Query q = new Query();
        q.setPropertyNames(
                new String[] {
                    "name",
                });
        q.setSortBy(new SortBy[] {ff.sort("z", SortOrder.ASCENDING)});
        Query expected = new Query(q);
        expected.setPropertyNames(new String[] {"name", "z"});
        checkRetypeCannotSort(q, expected);
    }

    public void checkRetypeCannotSort(Query query, final Query expected) throws IOException {
        DataStore store =
                new ContentDataStore() {

                    {
                        namespaceURI = TYPE.getName().getNamespaceURI();
                    }

                    @SuppressWarnings("serial")
                    @Override
                    protected List<Name> createTypeNames() throws IOException {
                        return new ArrayList<Name>() {
                            {
                                add(TYPENAME);
                            }
                        };
                    }

                    @Override
                    protected ContentFeatureSource createFeatureSource(ContentEntry entry)
                            throws IOException {
                        return new ContentFeatureSource(entry, null) {

                            @Override
                            protected ReferencedEnvelope getBoundsInternal(Query query)
                                    throws IOException {
                                throw new RuntimeException("Unexpected call");
                            }

                            @Override
                            protected int getCountInternal(Query query) throws IOException {
                                throw new RuntimeException("Unexpected call");
                            }

                            @Override
                            protected FeatureReader<SimpleFeatureType, SimpleFeature>
                                    getReaderInternal(Query query) throws IOException {
                                assertEquals(expected, query);
                                return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(
                                        TYPE);
                            }

                            @Override
                            protected SimpleFeatureType buildFeatureType() throws IOException {
                                return TYPE;
                            }

                            @Override
                            protected boolean canRetype() {
                                return true;
                            }

                            @Override
                            protected boolean canSort() {
                                return false;
                            }
                        };
                    }
                };

        SimpleFeatureSource fs = store.getFeatureSource(TYPE.getName());
        SimpleFeatureCollection features = fs.getFeatures(query);
        try (SimpleFeatureIterator fi = features.features()) {
            assertFalse(fi.hasNext());
        }
    }
}
