package org.geotools.geojson.feature;

/**
 * Parses and encoded feature attributes.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/geojson/src/main/java/org/geotools/geojson/feature/AttributeIO.java $
 */
public interface AttributeIO {

    Object parse(String att, String value);

    String encode(String att, Object value);

}
