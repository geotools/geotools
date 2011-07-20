/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.testutils;

import com.vividsolutions.jts.geom.Polygon;
import java.util.Arrays;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A class with static methods to create test data.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class TestData {
    private static SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

    /**
     * Creates a feature collection containing a single feature with a polygon
     * of the specified bounds.
     * 
     * @param env bounds for the polygon
     * @return the feature collection
     */
    public static SimpleFeatureCollection singlePolygonFeatureCollection(ReferencedEnvelope env) {
        if (env == null || env.isEmpty()) {
            throw new IllegalArgumentException("env must not be null or empty");
        }
        
        typeBuilder.setName("rectangle");
        typeBuilder.add("shape", Polygon.class, env.getCoordinateReferenceSystem());
        typeBuilder.add("label", String.class);
        final SimpleFeatureType TYPE = typeBuilder.buildFeatureType();
        
        SimpleFeature feature = SimpleFeatureBuilder.build(
                TYPE, new Object[]{JTS.toGeometry(env), "a rectangle"}, null);

        return new ListFeatureCollection(TYPE, Arrays.asList(feature));
    }
}
