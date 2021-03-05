package org.geotools.feature;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FeatureReaderIteratorTest {

    @Test
    public void testCloseOnException() {
        try (FeatureReaderIterator<SimpleFeature> it =
                new FeatureReaderIterator<>(new BreakingFeatureReader())) {
            Assert.assertFalse(it.hasNext());
            // the reader is really closed or not?
            Assert.assertNull(it.reader);
        }
    }

    class BreakingFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

        @Override
        public void close() throws IOException {
            throw new IllegalStateException("The exception we saw in GEOT-2068");
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return null;
        }

        @Override
        public boolean hasNext() throws IOException {
            throw new IllegalStateException("The exception we saw in GEOT-2068");
        }

        @Override
        public SimpleFeature next()
                throws IOException, IllegalArgumentException, NoSuchElementException {
            return null;
        }
    }
}
