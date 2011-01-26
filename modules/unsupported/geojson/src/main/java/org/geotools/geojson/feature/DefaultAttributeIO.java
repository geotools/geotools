package org.geotools.geojson.feature;

public class DefaultAttributeIO implements AttributeIO {

    public Object parse(String att, String value) {
        return value;
    }
    
    public String encode(String att, Object value) {
        return value != null ? value.toString() : null;
    }
    
}
