/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import org.geotools.api.metadata.quality.ConformanceResult;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.quality.PositionalAccuracyImpl;
import org.junit.Test;

/**
 * Tests {@link Citations} and related constants.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class CitationsTest {

    @Test
    public void testErrors() {
        String message = ErrorKeys.BAD_ENTRY;
        assertNotNull(message);
    }
    /**
     * Tests the {@link AbstractMetadata#toString()} method first, since debugging will relying a lot on this method for
     * the remaining of the test suite.
     */
    @Test
    public void testToString() {
        final String text = Citations.EPSG.toString();
        /*
         * Reminder: (?s) allows .* to skip new line characters.
         *           (?m) enable the multi-lines mode for ^ and $.
         *           ^ and $ match the begining and end of a line respectively.
         */
        assertTrue(text.matches("(?s)(?m).*^\\s+Identifiers:\\s+Code:\\s+EPSG$.*"));
        assertTrue(text.matches("(?s)(?m).*^\\s+Linkage:\\s+http://www.epsg.org$.*"));
    }

    /** Makes sure that {@link Citations} constants are immutables. */
    @Test
    public void testCitation() {
        assertEquals("Identity comparaison", Citations.EPSG, Citations.EPSG);
        assertNotSame(Citations.EPSG, Citations.OGC);
        assertTrue(Citations.EPSG instanceof CitationImpl);
        try {
            ((CitationImpl) Citations.EPSG).setISBN("Dummy");
            fail("Pre-defined metadata should be unmodifiable.");
        } catch (UnmodifiableMetadataException e) {
            // This is the expected exception.
        }
        try {
            Citations.EPSG.getIdentifiers().add(null);
            fail("Pre-defined metadata should be unmodifiable.");
        } catch (UnsupportedOperationException e) {
            // This is the expected exception.
        }
    }

    /** Tests {@link PositionalAccuracyImpl} constants. */
    @Test
    @SuppressWarnings(
            "UndefinedEquals") // Subtypes of Collection are not guaranteed to implement a useful #equals method.
    public void testPositionalAccuracy() {
        assertEquals(
                "Identity comparaison",
                PositionalAccuracyImpl.DATUM_SHIFT_APPLIED,
                PositionalAccuracyImpl.DATUM_SHIFT_APPLIED);

        assertEquals(
                "Identity comparaison",
                PositionalAccuracyImpl.DATUM_SHIFT_OMITTED,
                PositionalAccuracyImpl.DATUM_SHIFT_OMITTED);

        assertNotSame(PositionalAccuracyImpl.DATUM_SHIFT_APPLIED, PositionalAccuracyImpl.DATUM_SHIFT_OMITTED);

        final Collection appliedResults = PositionalAccuracyImpl.DATUM_SHIFT_APPLIED.getResults();
        final Collection omittedResults = PositionalAccuracyImpl.DATUM_SHIFT_OMITTED.getResults();
        final ConformanceResult applied =
                (ConformanceResult) appliedResults.iterator().next();
        final ConformanceResult omitted =
                (ConformanceResult) omittedResults.iterator().next();
        assertNotSame(applied, omitted);
        assertTrue(applied.pass());
        assertFalse(omitted.pass());
        assertNotEquals(applied, omitted);
        assertNotEquals(appliedResults, omittedResults);
        assertNotEquals(PositionalAccuracyImpl.DATUM_SHIFT_APPLIED, PositionalAccuracyImpl.DATUM_SHIFT_OMITTED);
    }
}
