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
package org.geotools.data.transform;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.FeatureCollectionWrapperTestSupport;
import org.geotools.feature.NameImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;

public class TransformFeatureCollectionMemoryTest extends FeatureCollectionWrapperTestSupport {

    public SimpleFeatureCollection transformWithExpressions() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("att", ECQL.toExpression("someAtt")));
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(defaultGeom, 1)")));

        SimpleFeatureSource source = DataUtilities.source(delegate);
        TransformFeatureSource transformedSource =
                new TransformFeatureSource(source, new NameImpl("Transformed"), definitions);
        return transformedSource.getFeatures();
    }

    public void testPreserveUserData() throws Exception {
        SimpleFeatureCollection transformedCollection = transformWithExpressions();
        SimpleFeature first = DataUtilities.first(transformedCollection);
        assertEquals(TEST_VALUE, first.getUserData().get(TEST_KEY));
    }
}
