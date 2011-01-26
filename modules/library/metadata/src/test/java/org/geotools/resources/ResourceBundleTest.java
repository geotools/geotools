/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.resources;

import java.util.Locale;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link ResourceBundle} class, especially {@link Vocabulary}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class ResourceBundleTest {
    /**
     * Tests some simple vocabulary words.
     */
    @Test
    public void testVocabulary() {
        Vocabulary resources;

        resources = Vocabulary.getResources(Locale.ENGLISH);
        assertSame(resources, Vocabulary.getResources(Locale.US));
        assertSame(resources, Vocabulary.getResources(Locale.UK));
        assertSame(resources, Vocabulary.getResources(Locale.CANADA));
        assertEquals("North", resources.getString(VocabularyKeys.NORTH));

        resources = Vocabulary.getResources(Locale.FRENCH);
        assertSame(resources, Vocabulary.getResources(Locale.FRANCE));
        assertSame(resources, Vocabulary.getResources(Locale.CANADA_FRENCH));
        assertEquals("Nord", resources.getString(VocabularyKeys.NORTH));
    }
}
