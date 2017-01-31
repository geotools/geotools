package org.geotools.mapbox;

/**
 * Thrown if the MapBox JSON used by {@link MBStyle} does not conform to the MapBox specification
 *
 * @author tbarsballe
 */
public class MBFormatException extends Exception {
    public MBFormatException(String message) {
        super(message);
    }
}
