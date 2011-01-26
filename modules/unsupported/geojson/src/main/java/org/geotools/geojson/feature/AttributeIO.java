package org.geotools.geojson.feature;

/**
 * Parses and encoded feature attributes.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public interface AttributeIO {

    Object parse(String att, String value);

    String encode(String att, Object value);

}
