package org.geotools.feature;

import java.io.IOException;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FeatureReaderIteratorTest extends TestCase {

    public void testCloseOnException() {
        FeatureReaderIterator it = new FeatureReaderIterator(new BreakingFeatureReader());
        assertFalse(it.hasNext());
        // the reader is really closed or not?
        assertNull(it.reader);
    }

    class BreakingFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

        public void close() throws IOException {
            throw new IllegalStateException("The exception we saw in GEOT-2068");
        }

        public SimpleFeatureType getFeatureType() {
            return null;
        }

        public boolean hasNext() throws IOException {
            throw new IllegalStateException("The exception we saw in GEOT-2068");
        }

        public SimpleFeature next() throws IOException, IllegalArgumentException,
                NoSuchElementException {
            return null;
        }

    }
}
