/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.api.data.BatchFeatureEvent;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureEvent;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.factory.GeoTools;

@SuppressWarnings("unused")
@SuppressFBWarnings("UWF_NULL_FIELD")
public class SimpleFeatureStoreExamples {
    @SuppressFBWarnings("NP_UNWRITTEN_FIELD")
    DataStore dataStore = null;

    String typeName;

    private void addFeaturesExample() throws Exception {
        // addExample start
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);

        SimpleFeatureType featureType = store.getSchema();

        SimpleFeatureBuilder build = new SimpleFeatureBuilder(featureType);
        GeometryBuilder geom = new GeometryBuilder();

        List<SimpleFeature> list = new ArrayList<>();
        list.add(build.buildFeature("fid1", geom.point(1, 1), "hello"));
        list.add(build.buildFeature("fid2", geom.point(2, 3), "martin"));
        SimpleFeatureCollection collection = new ListFeatureCollection(featureType, list);

        Transaction transaction = new DefaultTransaction("Add Example");
        store.setTransaction(transaction);
        try {
            store.addFeatures(collection);
            transaction.commit(); // actually writes out the features in one go
        } catch (Exception eek) {
            transaction.rollback();
        }
        // addExample end
    }

    private void addFeatureIdExample(SimpleFeatureCollection collection) throws Exception {
        // addFeatureIdExample start
        Transaction transaction = new DefaultTransaction("Add Example");
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
        store.setTransaction(transaction);
        try {
            List<FeatureId> added = store.addFeatures(collection);
            System.out.println(added); // prints out the temporary feature ids

            transaction.commit();
            System.out.println(added); // prints out the final feature ids

            Set<FeatureId> selection = new HashSet<>(added);
            FilterFactory ff = CommonFactoryFinder.getFilterFactory();
            Filter selected = ff.id(selection); // filter selecting all the features just added
        } catch (Exception problem) {
            transaction.rollback();
            throw problem;
        }
        // addFeatureIdExample end
    }

    private void addFeaturesEvents(SimpleFeatureCollection collection) throws Exception {
        // addEvents start
        Transaction transaction = new DefaultTransaction("Add Example");
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
        store.setTransaction(transaction);

        class CommitListener implements FeatureListener {
            @Override
            public void changed(FeatureEvent featureEvent) {
                if (featureEvent instanceof BatchFeatureEvent batchEvent) {

                    System.out.println("area changed:" + batchEvent.getBounds());
                    System.out.println("created fids:" + batchEvent.getCreatedFeatureIds());
                } else {
                    System.out.println("bounds:" + featureEvent.getBounds());
                    System.out.println("change:" + featureEvent.getFilter());
                }
            }
        }
        CommitListener listener = new CommitListener();
        store.addFeatureListener(listener);
        try {
            List<FeatureId> added = store.addFeatures(collection);
            transaction.commit();
        } catch (Exception problem) {
            transaction.rollback();
            throw problem;
        }
        // addEvents end
    }

    private void removeExample() throws Exception {
        // removeExample start
        Transaction transaction = new DefaultTransaction("removeExample");
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
        store.setTransaction(transaction);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Filter filter = ff.id(Collections.singleton(ff.featureId("fred")));
        try {
            store.removeFeatures(filter);
            transaction.commit();
        } catch (Exception eek) {
            transaction.rollback();
        }
        // removeExample end
    }

    private void removeExample2() throws Exception {
        // removeExample2 start
        Transaction transaction = new DefaultTransaction("removeExample");
        SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
        store.setTransaction(transaction);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Filter filter = ff.id(Collections.singleton(ff.featureId("fred")));
        try {
            final Set<FeatureId> removed = new HashSet<>();
            SimpleFeatureCollection collection = store.getFeatures(new Query(typeName, filter, Query.NO_NAMES));
            collection.accepts(
                    new FeatureVisitor() {
                        @Override
                        public void visit(Feature feature) {
                            removed.add(feature.getIdentifier());
                        }
                    },
                    null);
            store.removeFeatures(filter);
            transaction.commit();
        } catch (Exception eek) {
            transaction.rollback();
        }
        // removeExample2 end
    }
}
