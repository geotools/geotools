/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio;

import org.geotools.util.NumberRange;

/**
 * <p>
 * An internal class used to represent vertical levels. A vertical level is
 * usually identified by a numeric value (with a Unit of Measure). Some vertical
 * levels are instead represented by a symbolic value. As an instance, a GRIB1
 * record may contain data collected at the Sea Surface Level (without any
 * numeric/unit of measure knowledge). Moreover, a vertical level may be related
 * to a vertical range like, as an instance, the related 2D slice may be defined
 * for a Barometric pressure belonging 300 and 500 mb.
 * </p>
 * <p>
 * GeoAPI only provides specification to handle vertical extents representing
 * numerical quantities. Moreover they define a method returning CRS. The case
 * of symbolic vertical extent isn't handled by GeoAPI.
 * </p>
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public class VerticalExtent {

    public enum VerticalLevelType {
        SINGLE_NUMBER, NUMBER_RANGE, UNDEFINED
    };

    /**
     * Where:<BR>
     * SINGLE_NUMBER indicates a vertical level identified by a numeric value
     * with UoM.<BR>
     * NUMBER_RANGE indicates a vertical level identified by a numeric range
     * with UoM.<BR>
     * SYMBOLIC indicates a vertical level identified by a symbolic value.<BR>
     */

    /**
     * Specify the type of this vertical level, using one of the previously
     * defined constants
     */
    protected VerticalLevelType type = VerticalLevelType.UNDEFINED;

    /**
     * The real Object storing the vertical value: a {@code String}, a
     * {@code NumberRange}, a {@code Double}.
     */
    protected Object value;

    /**
     * The Unit of measure of this vertical level (if available), an empty
     * String otherwise
     */
    protected String uom;

    /** Return the unit of measure for this vertical level. */
    public String getUoM() {
        return uom;
    }

    /**
     * Return the type of Vertical level which may be one of
     * {@link VerticalLevelType#SINGLE_NUMBER}<BR>
     * {@link VerticalLevelType#NUMBER_RANGE}<BR>
     * 
     * @return the Vertical level type.
     */
    public VerticalLevelType getType() {
        return type;
    }

    public void setValue(Object value) {
        if (value instanceof Double) {
            type = VerticalLevelType.SINGLE_NUMBER;
        } else if (value instanceof NumberRange) {
            type = VerticalLevelType.NUMBER_RANGE;
        }
        this.value = value;
    }

    public String getValueAsString() {
        switch (type) {
        case SINGLE_NUMBER:
            return ((Double) value).toString();
        case NUMBER_RANGE:
            return ((NumberRange) value).toString();
        default:
            return "";
        }
    }

    /**
     * Return the maximum value in case of Numeric vertical extents. In case of
     * symbolic link a {@link Double#NaN} is returned.
     */
    public Double getMaximumValue() {
        if (type == VerticalLevelType.SINGLE_NUMBER)
            return (Double) value;
        else if (type == VerticalLevelType.NUMBER_RANGE)
            return Double.valueOf(((NumberRange) value).getMaximum());
        else
            return Double.NaN;
    }

    /**
     * Return the minimum value in case of Numeric vertical extents. In case of
     * symbolic link a {@link Double#NaN} is returned.
     */
    public Double getMinimumValue() {
        if (type == VerticalLevelType.SINGLE_NUMBER)
            return (Double) value;
        else if (type == VerticalLevelType.NUMBER_RANGE)
            return Double.valueOf(((NumberRange) value).getMinimum());
        else
            return Double.NaN;
    }

    // /**
    // * @TODO: Set this properly and handle Symbolic Vertical levels having no
    // * CRS.
    // */
    // public VerticalCRS getVerticalCRS() {
    // return null;
    // }

    /**
     * Returns the vertical level value.
     * 
     * @return value {@linkplain Object}
     */
    public Object getValue() {
        return value;
    }
}
