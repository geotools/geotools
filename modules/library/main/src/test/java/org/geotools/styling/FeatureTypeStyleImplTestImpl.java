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

import org.geotools.api.style.SemanticType;
import org.geotools.api.style.Symbolizer;
import org.geotools.feature.NameImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * This test case captures the FeatureTypeStyleImpl.clone() issue GEOT-5397
 *
 * @author Burkhard Strauss
 */
public class FeatureTypeStyleImplTestImpl {

    /** Checks if clone method works. */
    @Test
    public void testCloneMethod() throws Exception {

        final StyleBuilder sb = new StyleBuilder();
        final Symbolizer symbolizer = sb.createPointSymbolizer();
        final RuleImpl rule = sb.createRule(symbolizer);

        final FeatureTypeStyleImpl original = new FeatureTypeStyleImpl(new RuleImpl[] {rule});
        Assert.assertNotNull("orig: rules must not be null", original.rules());
        Assert.assertEquals("orig: one rule expected", 1, original.rules().size());
        original.featureTypeNames().add(new NameImpl("MyFeatureType1"));
        original.featureTypeNames().add(new NameImpl("MyFeatureType2"));
        Assert.assertNotNull(
                "orig: featureTypeNames must not be null", original.featureTypeNames());
        Assert.assertEquals(
                "orig: two featureTypeNames expected", 2, original.featureTypeNames().size());
        original.semanticTypeIdentifiers().add(SemanticType.POINT);
        original.semanticTypeIdentifiers().add(SemanticType.LINE);
        original.semanticTypeIdentifiers().add(SemanticType.POLYGON);
        Assert.assertNotNull(
                "orig: semanticTypeIdentifiers must not be null",
                original.semanticTypeIdentifiers());
        Assert.assertEquals(
                "orig: three semanticTypeIdentifiers expected",
                3,
                original.semanticTypeIdentifiers().size());

        final FeatureTypeStyleImpl clone = (FeatureTypeStyleImpl) original.clone();
        Assert.assertNotNull("clone: rules must not be null", clone.rules());
        Assert.assertEquals("clone: one rule expected", 1, clone.rules().size());
        Assert.assertNotSame(
                "clone: rules collection should have been cloned", clone.rules(), original.rules());
        Assert.assertNotNull("clone: featureTypeNames must not be null", clone.featureTypeNames());
        Assert.assertEquals(
                "clone: two featureTypeNames expected", 2, clone.featureTypeNames().size());
        Assert.assertNotSame(
                "clone: semanticTypeIdentifiers collection should have been cloned",
                clone.featureTypeNames(),
                original.featureTypeNames());
        Assert.assertNotNull(
                "clone: semanticTypeIdentifiers must not be null", clone.semanticTypeIdentifiers());
        Assert.assertEquals(
                "clone: three semanticTypeIdentifiers expected",
                3,
                clone.semanticTypeIdentifiers().size());
        Assert.assertNotSame(
                "clone: semanticTypeIdentifiers collection should have been cloned",
                clone.semanticTypeIdentifiers(),
                original.semanticTypeIdentifiers());
    }
}
