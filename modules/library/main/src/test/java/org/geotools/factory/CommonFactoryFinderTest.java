/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

import org.geotools.feature.AbstractFeatureFactoryImpl;
import org.geotools.feature.LenientFeatureFactoryImpl;
import org.geotools.feature.ValidatingFeatureFactoryImpl;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.feature.FeatureFactory;

public class CommonFactoryFinderTest {

    @Test
    public void testGetFeatureFactory() {
        FeatureFactory featureFactory = CommonFactoryFinder.getFeatureFactory(null);
        Assert.assertNotNull(featureFactory);
        Assert.assertTrue(featureFactory instanceof LenientFeatureFactoryImpl);
    }

    @Test
    public void testGetStyleFactory() {
        Assert.assertNotNull(CommonFactoryFinder.getStyleFactories(GeoTools.getDefaultHints()));
    }

    @Test
    public void testGetFilterFactory() {
        Assert.assertNotNull(CommonFactoryFinder.getFilterFactory(null));
    }

    @Test
    public void testGetDefaultFeatureFactory() {
        FeatureFactory featureFactory = CommonFactoryFinder.getFeatureFactory(null);
        Assert.assertNotNull(featureFactory);
        Assert.assertTrue(featureFactory instanceof AbstractFeatureFactoryImpl);
    }

    @Test
    public void testGetValidatingFeatureFactory() {
        Hints hints = new Hints(Hints.FEATURE_FACTORY, ValidatingFeatureFactoryImpl.class);
        FeatureFactory featureFactory = CommonFactoryFinder.getFeatureFactory(hints);
        Assert.assertNotNull(featureFactory);
        Assert.assertTrue(featureFactory instanceof ValidatingFeatureFactoryImpl);
    }
}
