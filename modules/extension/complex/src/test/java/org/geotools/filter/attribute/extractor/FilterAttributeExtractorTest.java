/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.attribute.extractor;

import java.util.HashSet;
import java.util.Set;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterAttributeExtractor;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FilterAttributeExtractorTest {

    @Test
    public void testNonExistingAttributeEvaluatesToAttributeDescriptor() throws Exception {

        SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
        sftb.setName("twoDoublesFeatureType");
        sftb.add("pointOne", Double.class);
        sftb.add("pointTwo", Double.class);
        SimpleFeatureType type = sftb.buildFeatureType();

        Feature feature = SimpleFeatureBuilder.build(type, new Object[] {5.0, 2.5}, null);
        FilterAttributeExtractor fae = new FilterAttributeExtractor(type);

        // the next call to fae.visit used to throw a ClassCastException, here it is ensured is not
        // anymore thrown
        Assert.assertNotNull(fae.visit(new AttributeExpressionImpl("type"), feature));

        Set<String> attributeTypesDescriptorsNames = new HashSet<>();
        attributeTypesDescriptorsNames.add("type");
        // checking that there is the there is a "type" AttributeDescriptor
        Assert.assertEquals(
                attributeTypesDescriptorsNames,
                fae.visit(new AttributeExpressionImpl("type"), feature));
    }
}
