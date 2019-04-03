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
 *
 */

package org.geotools.main;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Main examples used for sphinx documentation.-+*
 *
 * @author Jody Garnett
 */
public class MainExamples {

    void exampleDataUtilities() throws Exception {
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;

        // exampleDataUtilities start
        SimpleFeatureCollection features = DataUtilities.simple(collection);
        // exampleDataUtilities end
    }

    // exampleRetype start
    void exampleRetype() throws Exception {
        SimpleFeatureType origional =
                DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");
        SimpleFeatureType modified =
                DataUtilities.createSubType(origional, new String[] {"centerline"});

        SimpleFeature feature = DataUtilities.template(origional);

        SimpleFeature changed = DataUtilities.reType(modified, feature);
    }
    // exampleRetype end

    @SuppressFBWarnings
    void exampleIterator() throws Exception {
        SimpleFeatureCollection featureCollection = null;
        // exampleIterator start
        SimpleFeatureIterator iterator = featureCollection.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // process feature
            }
        } finally {
            iterator.close();
        }
        // exampleIterator end
    }
}
