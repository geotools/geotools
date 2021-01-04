/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import junit.framework.TestCase;
import org.geotools.feature.NameImpl;
import org.opengis.style.SemanticType;

/**
 * This test case captures the FeatureTypeStyleImpl.clone() issue GEOT-5397
 *
 * @author Burkhard Strauss
 */
public class FeatureTypeStyleImplTest extends TestCase {

    /** Checks if clone method works. */
    public void testCloneMethod() throws Exception {

        final StyleBuilder sb = new StyleBuilder();
        final Symbolizer symbolizer = sb.createPointSymbolizer();
        final Rule rule = sb.createRule(symbolizer);

        final FeatureTypeStyleImpl original = new FeatureTypeStyleImpl(new Rule[] {rule});
        assertNotNull("orig: rules must not be null", original.rules());
        assertEquals("orig: one rule expected", 1, original.rules().size());
        original.featureTypeNames().add(new NameImpl("MyFeatureType1"));
        original.featureTypeNames().add(new NameImpl("MyFeatureType2"));
        assertNotNull("orig: featureTypeNames must not be null", original.featureTypeNames());
        assertEquals("orig: two featureTypeNames expected", 2, original.featureTypeNames().size());
        original.semanticTypeIdentifiers().add(SemanticType.POINT);
        original.semanticTypeIdentifiers().add(SemanticType.LINE);
        original.semanticTypeIdentifiers().add(SemanticType.POLYGON);
        assertNotNull(
                "orig: semanticTypeIdentifiers must not be null",
                original.semanticTypeIdentifiers());
        assertEquals(
                "orig: three semanticTypeIdentifiers expected",
                3,
                original.semanticTypeIdentifiers().size());

        final FeatureTypeStyle clone = (FeatureTypeStyle) original.clone();
        assertNotNull("clone: rules must not be null", clone.rules());
        assertEquals("clone: one rule expected", 1, clone.rules().size());
        assertNotSame(
                "clone: rules collection should have been cloned", clone.rules(), original.rules());
        assertNotNull("clone: featureTypeNames must not be null", clone.featureTypeNames());
        assertEquals("clone: two featureTypeNames expected", 2, clone.featureTypeNames().size());
        assertNotSame(
                "clone: semanticTypeIdentifiers collection should have been cloned",
                clone.featureTypeNames(),
                original.featureTypeNames());
        assertNotNull(
                "clone: semanticTypeIdentifiers must not be null", clone.semanticTypeIdentifiers());
        assertEquals(
                "clone: three semanticTypeIdentifiers expected",
                3,
                clone.semanticTypeIdentifiers().size());
        assertNotSame(
                "clone: semanticTypeIdentifiers collection should have been cloned",
                clone.semanticTypeIdentifiers(),
                original.semanticTypeIdentifiers());
    }
}
