package org.geotools.geojson.feature;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.geotools.geojson.GeoJSONUtil;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class FeatureTypeAttributeIO implements AttributeIO {

    HashMap<String, AttributeIO> ios = new HashMap();
    
    public FeatureTypeAttributeIO(SimpleFeatureType featureType) {
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            AttributeIO io = null;
            if (Date.class.isAssignableFrom(ad.getType().getBinding())) {
                io = new DateAttributeIO();
            }
            else {
                io = new DefaultAttributeIO();
            }
            ios.put(ad.getLocalName(), io);
        }
    }
    
    public String encode(String att, Object value) {
        return ios.get(att).encode(att, value);
        
    }

    public Object parse(String att, String value) {
        return ios.get(att).parse(att, value);
    }
    
    static class DateAttributeIO implements AttributeIO {

        public String encode(String att, Object value) {
            return GeoJSONUtil.DATE_FORMAT.format((Date)value);
        }

        public Object parse(String att, String value) {
            try {
                return GeoJSONUtil.DATE_FORMAT.parse(value);
            } 
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        
    }

}
