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
package org.geotools.xml;

import java.util.ArrayList;
import java.util.List;
import org.geotools.gml.GMLHandlerFeature;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.helpers.XMLFilterImpl;

// yourcode start
public class YourCode extends XMLFilterImpl implements GMLHandlerFeature {

    private List<SimpleFeature> features = new ArrayList<>();

    public void feature(SimpleFeature feature) {
        features.add(feature);
    }

    public List<SimpleFeature> getFeatures() {
        return features;
    }
}
// yourcode end
