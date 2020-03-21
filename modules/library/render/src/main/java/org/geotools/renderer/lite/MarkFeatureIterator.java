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
package org.geotools.renderer.lite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.data.sort.SimpleFeatureIO;
import org.geotools.data.util.DefaultProgressListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.ProgressListener;

/**
 * A FeatureIterator that can have a position marked, and can be reset to it
 *
 * @author Andrea Aime - GeoSolutions
 */
abstract class MarkFeatureIterator implements FeatureIterator<Feature> {

    static final Logger LOGGER = Logging.getLogger(MarkFeatureIterator.class);

    /**
     * Builds a new {@link MarkFeatureIterator} making sure no too many features are kept in memory.
     * The listener won't receive any notification, but will be used to check if the data loading
     * should be stopped using {@link ProgressListener#isCanceled()}
     */
    public static MarkFeatureIterator create(
            FeatureCollection fc, int maxFeaturesInMemory, ProgressListener listener)
            throws IOException {
        List<Feature> features = new ArrayList<>();
        int count = 0;
        if (listener == null) {
            listener = new DefaultProgressListener();
        }
        try (FeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                if (listener.isCanceled()) {
                    return null;
                }
                Feature f = fi.next();
                features.add(f);
                count++;
                if (count >= maxFeaturesInMemory) {
                    if (fc.getSchema() instanceof SimpleFeatureType) {
                        return new DiskMarkFeatureIterator(
                                features, fi, (SimpleFeatureType) fc.getSchema(), listener);
                    } else {
                        throw new IllegalArgumentException(
                                "Cannot offload to disk complex features "
                                        + "and reached the max number of feature in memory: "
                                        + maxFeaturesInMemory);
                    }
                }
            }

            return new MemoryMarkFeatureIterator(features);
        }
    }

    /** Marks the current position of the feature iterator */
    public abstract void mark() throws IOException;

    public abstract void reset() throws IOException;

    /** Simple in memory implementation of the mark iterator */
    static class MemoryMarkFeatureIterator extends MarkFeatureIterator {

        List<Feature> features;

        int curr;

        int mark;

        public MemoryMarkFeatureIterator(List<Feature> features) {
            this.features = features;
            curr = mark = 0;
        }

        @Override
        public boolean hasNext() {
            return curr < features.size();
        }

        @Override
        public Feature next() throws NoSuchElementException {
            return features.get(curr++);
        }

        @Override
        public void close() {
            // nothing to do
        }

        @Override
        public void mark() throws IOException {
            mark = curr;
        }

        @Override
        public void reset() throws IOException {
            curr = mark;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "MemoryMarkFeatureIterator [curr=" + curr + ", mark=" + mark + "]";
        }
    }

    /**
     * Implementation offloading on disk the features
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class DiskMarkFeatureIterator extends MarkFeatureIterator {

        int mark;

        long markOffset;

        SimpleFeatureIO io;

        int curr;

        int featureCount;

        public DiskMarkFeatureIterator(
                List<Feature> features,
                FeatureIterator fi,
                SimpleFeatureType schema,
                ProgressListener listener)
                throws IOException {
            File file = File.createTempFile("z-ordered-", ".features");
            this.io = new SimpleFeatureIO(file, schema);

            // dump all the features in memory
            for (Feature feature : features) {
                if (listener.isCanceled()) {
                    break;
                }
                io.write((SimpleFeature) feature);
                featureCount++;
            }

            // dump all the subsequent ones
            while (fi.hasNext() && !listener.isCanceled()) {
                Feature feature = fi.next();
                io.write((SimpleFeature) feature);
                featureCount++;
            }
            // do not close the iterator, the caller does that

            // reset to the beginning of the file
            io.seek(0);
        }

        @Override
        public boolean hasNext() {
            return curr < featureCount;
        }

        @Override
        public Feature next() throws NoSuchElementException {
            try {
                curr++;
                return io.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
            if (io != null) {
                try {
                    io.close(true);
                    io = null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void mark() throws IOException {
            mark = curr;
            markOffset = io.getOffset();
        }

        @Override
        public void reset() throws IOException {
            curr = mark;
            io.seek(markOffset);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "DiskMarkFeatureIterator [mark="
                    + mark
                    + ", markOffset="
                    + markOffset
                    + ", io="
                    + io
                    + ", curr="
                    + curr
                    + ", featureCount="
                    + featureCount
                    + "]";
        }

        @Override
        @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
        protected void finalize() throws Throwable {
            if (io != null) {
                LOGGER.warning(
                        "There is code leaving DiskMarkFeatureIterator open, "
                                + "this is leaking temporary files!");
                close();
            }
        }
    }
}
