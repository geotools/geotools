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
        assertTrue("orig: rules must not be null", original.rules() != null);
        assertTrue("orig: one rule expected", original.rules().size() == 1);
        original.featureTypeNames().add(new NameImpl("MyFeatureType1"));
        original.featureTypeNames().add(new NameImpl("MyFeatureType2"));
        assertTrue("orig: featureTypeNames must not be null", original.featureTypeNames() != null);
        assertTrue("orig: two featureTypeNames expected", original.featureTypeNames().size() == 2);
        original.semanticTypeIdentifiers().add(SemanticType.POINT);
        original.semanticTypeIdentifiers().add(SemanticType.LINE);
        original.semanticTypeIdentifiers().add(SemanticType.POLYGON);
        assertTrue(
                "orig: semanticTypeIdentifiers must not be null",
                original.semanticTypeIdentifiers() != null);
        assertTrue(
                "orig: three semanticTypeIdentifiers expected",
                original.semanticTypeIdentifiers().size() == 3);

        final FeatureTypeStyle clone = (FeatureTypeStyle) original.clone();
        assertTrue("clone: rules must not be null", clone.rules() != null);
        assertTrue("clone: one rule expected", clone.rules().size() == 1);
        assertTrue(
                "clone: rules collection should have been cloned",
                clone.rules() != original.rules());
        assertTrue("clone: featureTypeNames must not be null", clone.featureTypeNames() != null);
        assertTrue("clone: two featureTypeNames expected", clone.featureTypeNames().size() == 2);
        assertTrue(
                "clone: semanticTypeIdentifiers collection should have been cloned",
                clone.featureTypeNames() != original.featureTypeNames());
        assertTrue(
                "clone: semanticTypeIdentifiers must not be null",
                clone.semanticTypeIdentifiers() != null);
        assertTrue(
                "clone: three semanticTypeIdentifiers expected",
                clone.semanticTypeIdentifiers().size() == 3);
        assertTrue(
                "clone: semanticTypeIdentifiers collection should have been cloned",
                clone.semanticTypeIdentifiers() != original.semanticTypeIdentifiers());
    }
}
