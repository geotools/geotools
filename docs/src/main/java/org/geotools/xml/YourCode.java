package org.geotools.xml;

import java.util.ArrayList;
import java.util.List;

import org.geotools.gml.GMLHandlerFeature;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.helpers.XMLFilterImpl;
// yourcode start
public class YourCode extends XMLFilterImpl implements GMLHandlerFeature {

    private List<SimpleFeature> features = new ArrayList<SimpleFeature>();

    public void feature(SimpleFeature feature) {
        features.add(feature);
    }

    public List<SimpleFeature> getFeatures() {
        return features;
    }
}
// yourcode end