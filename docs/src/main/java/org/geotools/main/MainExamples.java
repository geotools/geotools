/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
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
        SimpleFeatureType modified = DataUtilities.createSubType(origional, "centerline");

        SimpleFeature feature = DataUtilities.template(origional);

        SimpleFeature changed = DataUtilities.reType(modified, feature);
    }
    // exampleRetype end

    @SuppressFBWarnings
    void exampleIterator() throws Exception {
        SimpleFeatureCollection featureCollection = null;
        // exampleIterator start
        try (SimpleFeatureIterator iterator = featureCollection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // process feature
            }
        }
        // exampleIterator end
    }
}
