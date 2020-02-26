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

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;
import org.geotools.data.Query;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.visitor.UniqueVisitor;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Iterator for result ids from index datasource
 *
 * @author Fernando Miño, Geosolutions
 */
public interface IndexIdIterator extends Iterator<String>, Closeable {

    /**
     * Index iterator for to work with a FeatureIterator delegate
     *
     * @author Fernando Miño, Geosolutions
     */
    public static class IndexFeatureIdIterator implements IndexIdIterator {

        private FeatureIterator<? extends Feature> indexIterator;

        public IndexFeatureIdIterator(FeatureIterator<? extends Feature> indexIterator) {
            this.indexIterator = indexIterator;
        }

        @Override
        public boolean hasNext() {
            return indexIterator.hasNext();
        }

        @Override
        public String next() {
            return simplifyIndentifier(indexIterator.next());
        }

        @Override
        public void close() {
            indexIterator.close();
        }

        /** Simplifies id value, cutting "typename." part if exists */
        protected String simplifyIndentifier(Feature feature) {
            String schemaPart = feature.getType().getName().getLocalPart() + ".";
            String fid = feature.getIdentifier().getID();
            if (fid.startsWith(schemaPart)) {
                return fid.substring(schemaPart.length());
            }
            return fid;
        }
    }

    /**
     * Index iterator for to work with UniqueVisitor over id field.
     *
     * @author Fernando Miño, Geosolutions
     */
    public static class IndexUniqueVisitorIterator implements IndexIdIterator {

        protected static final int STEP_LOAD = 1000;

        public static Consumer<UniqueVisitor> uniqueVisitorBuildHook;

        private FeatureCollection<SimpleFeatureType, SimpleFeature> fc;
        private Query idQuery;
        private String idFieldName;
        private int start;
        private int max;
        // visitor vars
        private UniqueVisitor visitor;
        private Iterator visitorIterator;
        private int currentVisitorStart;
        private int currentMax;
        private String nextValue = null;
        private int counter = 0;

        public IndexUniqueVisitorIterator(
                FeatureCollection<SimpleFeatureType, SimpleFeature> fc,
                Query idQuery,
                String idFieldName) {
            super();
            this.fc = fc;
            this.idQuery = idQuery;
            this.idFieldName = idFieldName;
            this.start = idQuery.getStartIndex() != null ? idQuery.getStartIndex() : 0;
            this.max = idQuery.getMaxFeatures();
            this.currentVisitorStart = this.start;
            this.currentMax = this.max;
            initVisitor();
        }

        private void initVisitor() {
            visitor = new UniqueVisitor(this.idFieldName);
            visitor.setStartIndex(currentVisitorStart);
            visitor.setMaxFeatures(Math.min(STEP_LOAD, currentMax));
            // execute hook if exists
            if (uniqueVisitorBuildHook != null) {
                uniqueVisitorBuildHook.accept(visitor);
            }
            try {
                fc.accepts(visitor, new NullProgressListener());
                visitorIterator = visitor.getUnique().iterator();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private String getNextFromVisitor() {
            // if max features reached, no more items.
            if (counter >= idQuery.getMaxFeatures()) return null;
            if (visitorIterator.hasNext()) {
                counter++;
                return (String) visitorIterator.next();
            } else {
                // if next current visitor start is into bounds
                int nextStart = currentVisitorStart + STEP_LOAD;
                if (nextStart <= (start + max - 1)) {
                    // init new visitor, next bounds
                    currentVisitorStart = nextStart;
                    currentMax = currentMax - STEP_LOAD;
                    initVisitor();
                    // if don't have next value yet, no more data -> return null
                    if (!visitorIterator.hasNext()) return null;
                    // has next value, return it
                    counter++;
                    return (String) visitorIterator.next();
                }
            }

            return null;
        }

        @Override
        public boolean hasNext() {
            // if placeholder has next value, return true
            if (nextValue != null) return true;
            // else get next value
            nextValue = getNextFromVisitor();
            // if no value yet, finish him!
            if (nextValue == null) return false;
            // there is new value, return true
            return true;
        }

        @Override
        public String next() {
            if (hasNext()) {
                // take value, make placeholder null
                String value = nextValue;
                nextValue = null;
                return value;
            } else return null;
        }

        @Override
        public void close() {}
    }
}
