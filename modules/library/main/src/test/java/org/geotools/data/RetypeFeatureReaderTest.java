/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class RetypeFeatureReaderTest {

    private static final String UD_KEY = "someKey";
    private static final String UD_VALUE = "someValue";

    @Test
    public void testRetypeUserData() throws Exception {
        // create base and derived feature type
        SimpleFeatureType featureType =
                DataUtilities.createType(
                        "feature", "id:string,name:String,geometry:Point:srid=4326");
        SimpleFeatureType targetType =
                SimpleFeatureTypeBuilder.retype(featureType, new String[] {"id", "geometry"});
        // create a feature collection wit a single feature
        SimpleFeature feature = DataUtilities.createFeature(featureType, "1|foobar|POINT(1 2)");
        feature.getUserData().put(UD_KEY, UD_VALUE);
        ListFeatureCollection features = new ListFeatureCollection(featureType);
        features.add(feature);
        // test
        int featuresCount = 0;
        FeatureReader<SimpleFeatureType, SimpleFeature> delegate =
                DataUtilities.reader((SimpleFeatureCollection) features);
        try (ReTypeFeatureReader retyped = new ReTypeFeatureReader(delegate, targetType)) {
            // check that the feature was correctly reprojected
            SimpleFeature retypedFeature = retyped.next();
            assertThat(retypedFeature, notNullValue());
            // check the schema is the desired one
            assertThat(retypedFeature.getFeatureType(), is(targetType));
            assertThat(retypedFeature.getAttribute("name"), nullValue());
            // check that the user data was preserved
            assertThat(retypedFeature.getUserData().get("someKey"), is("someValue"));
            // increment the features counter
            featuresCount++;
        }
        // check that we iterate over the feature
        assertThat(featuresCount, is(1));
    }
}
