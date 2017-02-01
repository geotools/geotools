package org.geotools.mbstyle;

/**
 * Thrown if the MapBox JSON used by {@link MBStyle} does not conform to the MapBox specification
 *
 * @author Torben Barsballe (Boundless)
 */
public class MBFormatException extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = 8328125000220917830L;

    public MBFormatException(String message) {
        super(message);
    }
}
