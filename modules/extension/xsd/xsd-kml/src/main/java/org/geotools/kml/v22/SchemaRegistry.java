package org.geotools.kml.v22;

import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.simple.SimpleFeatureType;

public class SchemaRegistry {

    private Map<String, SimpleFeatureType> featureTypes;

    public SchemaRegistry() {
        this.featureTypes = new HashMap<String, SimpleFeatureType>();
    }

    public void add(String featureTypeName, SimpleFeatureType featureType) {
        featureTypes.put(featureTypeName, featureType);
    }

    public SimpleFeatureType get(String featureTypeName) {
        return featureTypes.get(featureTypeName);
    }

    public SimpleFeatureType get(SimpleFeatureType featureType) {
        return get(featureType.getName().getLocalPart());
    }

}
