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
package org.geotools.image.io.netcdf;

import java.awt.image.DataBuffer;

import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.VariableIF;
import ucar.nc2.dataset.VariableEnhanced;

import org.geotools.resources.XArray;
import org.geotools.image.io.metadata.Band;


/**
 * Parses the offset, scale factor, minimum, maximum and fill values from a variable. This class
 * duplicate UCAR's {@code EnhanceScaleMissingImpl} functionality, but we have to do that because:
 * <p>
 * <ul>
 *   <li>I have not been able to find any method giving me directly the offset and scale factor.
 *       We can use some trick with {@link VariableEnhanced#convertScaleOffsetMissing}, but
 *       they are subject to rounding errors and there is no efficient way I can see to take
 *       missing values in account.</li>
 *   <li>The {@link VariableEnhanced} methods are available only if the variable is enhanced.
 *       Our variable is not, because we want raw (packed) data.</li>
 *   <li>We want minimum, maximum and fill values in packed units (as opposed to the geophysics
 *       values provided by the UCAR's API), because we check for missing values before to
 *       convert them.</li>
 * </ul>
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class VariableMetadata {
    /**
     * Raw image type as one of {@link DataBuffer} constants.
     */
    private final int imageType;

    /**
     * The scale and and offset values, or {@link Double#NaN NaN} if none.
     */
    public final double scale, offset;

    /**
     * The minimal and maximal valid values in geophysics units, or infinity if none.
     * They are converted from the packed values if needed, as UCAR does.
     */
    public final double minimum, maximum;

    /**
     * The fill and missing values in <strong>packed</strong> units, or {@code null} if none.
     * Note that this is different from UCAR, who converts to geophysics values. We keep packed
     * values in order to avoir rounding error. This array contains both the fill value and the
     * missing values, without duplicated values.
     */
    public final double[] missingValues;

    /**
     * The widest type found in attributes scanned by the {@link #attribute} method
     * since the last time this field was set. This is a temporary variable used by
     * the constructor only.
     */
    private transient DataType widestType;

    /**
     * Extracts metadata from the specified variable using UCAR's API. This approach suffers
     * from rounding errors and is unable to get the missing values. Use this constructor
     * only for comparing our own results with the results from the UCAR's API.
     */
    public VariableMetadata(final VariableEnhanced variable) {
        imageType = getRawDataType(variable);
        offset    =  variable.convertScaleOffsetMissing(0.0);
        scale     =  variable.convertScaleOffsetMissing(1.0) - offset;
        minimum   = (variable.getValidMin() - offset) / scale;
        maximum   = (variable.getValidMax() - offset) / scale;
        missingValues = null; // No way to get this information.
    }

    /**
     * Extracts metadata from the specified variable using our own method.
     *
     * @param variable The variable to extract metadata from.
     * @param forceRangePacking {@code true} if the valid range is encoded in geophysics units
     *        (which is a violation of CF convention), or {@code false} in order to autodetect
     *        using the UCAR heuristic rule.
     */
    public VariableMetadata(final Variable variable, boolean forceRangePacking) {
        final DataType dataType, scaleType, rangeType;
        /*
         * Gets the scale factors, if present. Also remember its type
         * for the heuristic rule to be applied later on the valid range.
         */
        imageType  = getRawDataType(variable);
        dataType   = widestType = variable.getDataType();
        scale      = attribute(variable, "scale_factor");
        offset     = attribute(variable, "add_offset");
        scaleType  = widestType;
        widestType = dataType; // Reset before we scan the other attributes.
        /*
         * Gets minimum and maximum. If a "valid_range" attribute is presents, it as precedence
         * over "valid_min" and "valid_max" as specified in UCAR documentation.
         */
        double minimum = Double.NaN;
        double maximum = Double.NaN;
        Attribute attribute = variable.findAttribute("valid_range");
        if (attribute != null) {
            widestType = widest(attribute.getDataType(), widestType);
            Number value = attribute.getNumericValue(0);
            if (value != null) {
                minimum = value.doubleValue();
            }
            value = attribute.getNumericValue(1);
            if (value != null) {
                maximum = value.doubleValue();
            }
        }
        if (Double.isNaN(minimum)) {
            minimum = attribute(variable, "valid_min");
        }
        if (Double.isNaN(maximum)) {
            maximum = attribute(variable, "valid_max");
        }
        rangeType  = widestType;
        widestType = dataType; // Reset before we scan the other attributes.
        if (!forceRangePacking) {
            // Heuristic rule defined in UCAR documentation (see EnhanceScaleMissing interface)
            forceRangePacking = rangeType.equals(scaleType) &&
                                rangeType.equals(widest(rangeType, dataType));
        }
        if (forceRangePacking) {
            final double offset = Double.isNaN(this.offset) ? 0 : this.offset;
            final double scale  = Double.isNaN(this.scale ) ? 1 : this.scale;
            minimum = (minimum - offset) / scale;
            maximum = (maximum - offset) / scale;
            if (!isFloatingPoint(rangeType)) {
                if (!Double.isNaN(minimum) && !Double.isInfinite(minimum)) {
                    minimum = Math.round(minimum);
                }
                if (!Double.isNaN(maximum) && !Double.isInfinite(maximum)) {
                    maximum = Math.round(maximum);
                }
            }
        }
        if (Double.isNaN(minimum)) minimum = Double.NEGATIVE_INFINITY;
        if (Double.isNaN(maximum)) maximum = Double.POSITIVE_INFINITY;
        this.minimum = minimum;
        this.maximum = maximum;
        /*
         * Gets fill and missing values. According UCAR documentation, they are
         * always in packed units. We keep them "as-is" (as opposed to UCAR who
         * converts them to geophysics units), in order to avoid rounding errors.
         * Note that we merge missing and fill values in a single array, without
         * duplicated values.
         */
        widestType = dataType;
        attribute = variable.findAttribute("missing_value");
        final double fillValue    = attribute(variable, "_FillValue");
        final int    fillCount    = Double.isNaN(fillValue) ? 0 : 1;
        final int    missingCount = (attribute != null) ? attribute.getLength() : 0;
        final double[] missings   = new double[fillCount + missingCount];
        if (fillCount != 0) {
            missings[0] = fillValue;
        }
        int count = fillCount;
scan:   for (int i=0; i<missingCount; i++) {
            final Number number = attribute.getNumericValue(i);
            if (number != null) {
                final double value = number.doubleValue();
                if (!Double.isNaN(value)) {
                    for (int j=0; j<count; j++) {
                        if (value == missings[j]) {
                            // Current value duplicates a previous one.
                            continue scan;
                        }
                    }
                    missings[count++] = value;
                }
            }
        }
        missingValues = (count != 0) ? XArray.resize(missings, count) : null;
    }

    /**
     * Returns the attribute value as a {@code double}.
     */
    private double attribute(final Variable variable, final String name) {
        final Attribute attribute = variable.findAttribute(name);
        if (attribute != null) {
            widestType = widest(attribute.getDataType(), widestType);
            final Number value = attribute.getNumericValue();
            if (value != null) {
                return value.doubleValue();
            }
        }
        return Double.NaN;
    }

    /**
     * Returns the widest of two data types.
     */
    private static DataType widest(final DataType type1, final DataType type2) {
        if (type1 == null) return type2;
        if (type2 == null) return type1;
        final int size1 = type1.getSize();
        final int size2 = type2.getSize();
        if (size1 > size2) return type1;
        if (size1 < size2) return type2;
        return isFloatingPoint(type2) ? type2 : type1;
    }

    /**
     * Returns {@code true} if the specified type is a floating point type.
     */
    private static boolean isFloatingPoint(final DataType type) {
        return DataType.FLOAT.equals(type) || DataType.DOUBLE.equals(type);
    }

    /**
     * Returns the data type which most closely represents the "raw" internal data
     * of the variable. This is the value returned by the default implementation of
     * {@link NetcdfImageReader#getRawDataType}.
     *
     * @param  variable The variable.
     * @return The data type, or {@link DataBuffer#TYPE_UNDEFINED} if unknown.
     *
     * @see NetcdfImageReader#getRawDataType
     */
    static int getRawDataType(final VariableIF variable) {
        final DataType type = variable.getDataType();
        if (DataType.BOOLEAN.equals(type) || DataType.BYTE.equals(type)) {
            return DataBuffer.TYPE_BYTE;
        }
        if (DataType.CHAR.equals(type)) {
            return DataBuffer.TYPE_USHORT;
        }
        if (DataType.SHORT.equals(type)) {
            return variable.isUnsigned() ? DataBuffer.TYPE_USHORT : DataBuffer.TYPE_SHORT;
        }
        if (DataType.INT.equals(type)) {
            return DataBuffer.TYPE_INT;
        }
        if (DataType.FLOAT.equals(type)) {
            return DataBuffer.TYPE_FLOAT;
        }
        if (DataType.LONG.equals(type) || DataType.DOUBLE.equals(type)) {
            return DataBuffer.TYPE_DOUBLE;
        }
        return DataBuffer.TYPE_UNDEFINED;
    }

    /**
     * Copies the value in this variable metadata into the specified band.
     */
    public void copyTo(final Band band) {
        band.setScale(scale);
        band.setOffset(offset);
        band.setPackedValues(minimum, maximum, missingValues, imageType);
    }
}
