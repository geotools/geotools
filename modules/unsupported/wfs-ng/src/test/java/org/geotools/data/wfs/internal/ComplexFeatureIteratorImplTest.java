/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.api.feature.Feature;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.junit.Test;
import org.mockito.Mockito;

/** Test class for {@link ComplexFeatureIteratorImpl}. */
public class ComplexFeatureIteratorImplTest {

    @Test
    public void hasNext_whenParserReturnsFeature_returnsTrue() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        Feature feature = mock(Feature.class);
        when(parser.parse()).thenReturn(feature);

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            assertTrue(iterator.hasNext());
        }
    }

    @Test
    public void hasNext_whenParserReturnsNull_returnsFalse() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        when(parser.parse()).thenReturn(null);

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    public void hasNext_whenParserThrowsIOException_returnsFalse() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        when(parser.parse()).thenThrow(new IOException("boom"));

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    public void hasNext_calledMultipleTimes_onlyParsesOnceUntilNextIsCalled() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        Feature feature = mock(Feature.class);
        when(parser.parse()).thenReturn(feature);

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            assertTrue(iterator.hasNext());
            assertTrue(iterator.hasNext());
            verify(parser, times(1)).parse();
        }
    }

    @Test
    public void next_returnsFeaturesInOrderAndAdvances() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        Feature first = mock(Feature.class);
        Feature second = mock(Feature.class);
        when(parser.parse()).thenReturn(first, second, null);

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            assertEquals(first, iterator.next());
            assertEquals(second, iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    public void next_whenNoMoreFeatures_throwsNoSuchElementException() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        when(parser.parse()).thenReturn(null);

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            assertThrows(NoSuchElementException.class, iterator::next);
        }
    }

    @Test
    public void close_closesParserAndClearsFeature() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        when(parser.parse()).thenReturn(mock(Feature.class));

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            iterator.hasNext();
            iterator.close();

            verify(parser, times(1)).close();
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    public void close_whenParserThrowsIOException_doesNotPropagate() throws IOException {
        XmlComplexFeatureParser parser = mock(XmlComplexFeatureParser.class);
        Mockito.doThrow(new IOException("boom")).when(parser).close();

        try (ComplexFeatureIteratorImpl iterator = new ComplexFeatureIteratorImpl(parser)) {
            iterator.close();
            verify(parser, never()).parse();
        }
    }
}
