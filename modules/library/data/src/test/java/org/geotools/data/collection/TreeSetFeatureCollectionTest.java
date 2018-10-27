/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on July 21, 2003, 5:58 PM
 */

package org.geotools.data.collection;

import java.util.List;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Verify TreeSetFeatureCollection is conforming to the FeatureCollection api contract.
 *
 * @author Jody
 */
public class TreeSetFeatureCollectionTest
        extends org.geotools.data.collection.FeatureCollectionTest {
    public TreeSetFeatureCollectionTest(String testName) {
        super(testName);
    }

    protected TreeSetFeatureCollection newCollection(
            SimpleFeatureType schema, List<SimpleFeature> list) {
        TreeSetFeatureCollection treeset = new TreeSetFeatureCollection(null, null);
        treeset.addAll(list);
        return treeset;
    }
}
