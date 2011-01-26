/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.quality;

import java.util.List;
import java.util.Arrays;
import javax.measure.unit.Unit;

import org.opengis.metadata.quality.QuantitativeResult;
import org.opengis.util.InternationalString;
import org.opengis.util.Record;
import org.opengis.util.RecordType;


/**
 * Information about the value (or set of values) obtained from applying a data quality measure.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class QuantitativeResultImpl extends ResultImpl implements QuantitativeResult {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 1230713599561236060L;

    /**
     * Quantitative value or values, content determined by the evaluation procedure used.
     */
    private List<Record> values;

    /**
     * Value type for reporting a data quality result, or {@code null} if none.
     */
    private RecordType valueType;

    /**
     * Value unit for reporting a data quality result, or {@code null} if none.
     */
    private Unit valueUnit;

    /**
     * Statistical method used to determine the value, or {@code null} if none.
     */
    private InternationalString errorStatistic;

    /**
     * Constructs an initially empty quantitative result.
     */
    public QuantitativeResultImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public QuantitativeResultImpl(final QuantitativeResult source) {
        super(source);
    }

    /**
     * Constructs a quantitative result initialized to the specified value.
     */
    public QuantitativeResultImpl(final double[] values) {
        setValues(values);
    }

    /**
     * Quantitative value or values, content determined by the evaluation procedure used.
     */
    public synchronized List<Record> getValues() {
        return values = nonNullList(values, Record.class);
    }

    /**
     * Set the quantitative value or values, content determined by the evaluation procedure used.
     *
     * @since 2.4
     */
    public synchronized void setValues(final List<Record> newValues) {
        values = copyList(newValues, values, Record.class);
    }

    /**
     * Set the quantitative value or values, content determined by the evaluation procedure used.
     */
    public synchronized void setValues(final double[] newValues) {
        final List<Record> records;
        if (newValues == null) {
            records = null;
        } else {
            final Record[] data = new Record[newValues.length];
            for (int i=0; i<newValues.length; i++) {
                data[i] = new SimpleRecord(newValues[i]);
            }
            records = Arrays.asList(data);
        }
        setValues(records);
    }

    /**
     * Temporary record implementation will we wait for a real one.
     *
     * @deprecated To be replaced by a better implementation as soon as we can.
     */
    private static final class SimpleRecord implements Record, java.io.Serializable {
        private final java.util.Map<org.opengis.util.MemberName, Object> map;

        public SimpleRecord(final double value) {
            map = java.util.Collections.singletonMap((org.opengis.util.MemberName) null, (Object) value);
        }
        public RecordType getRecordType() {
            throw new UnsupportedOperationException();
        }

        public java.util.Map<org.opengis.util.MemberName, Object> getAttributes() {
            return map;
        }

        public Object locate(org.opengis.util.MemberName name) {
            throw new UnsupportedOperationException();
        }

        public void set(org.opengis.util.MemberName name, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(final Object other) {
            if (other instanceof SimpleRecord) {
                return map.equals(((SimpleRecord) other).map);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return map.hashCode();
        }
    }

    /**
     * Value type for reporting a data quality result, or {@code null} if none.
     */
    public RecordType getValueType()  {
        return valueType;
    }

    /**
     * Set the value type for reporting a data quality result, or {@code null} if none.
     */
    public synchronized void setValueType(final RecordType newValue) {
        checkWritePermission();
        valueType = newValue;
    }

    /**
     * Value unit for reporting a data quality result, or {@code null} if none.
     */
    public Unit getValueUnit()  {
        return valueUnit;
    }

    /**
     * Set the value unit for reporting a data quality result, or {@code null} if none.
     */
    public synchronized void setValueUnit(final Unit newValue) {
        checkWritePermission();
        valueUnit = newValue;
    }

    /**
     * Statistical method used to determine the value, or {@code null} if none.
     */
    public InternationalString getErrorStatistic()  {
        return errorStatistic;
    }

    /**
     * Set the statistical method used to determine the value, or {@code null} if none.
     */
    public synchronized void setErrorStatistic(final InternationalString newValue) {
        checkWritePermission();
        errorStatistic = newValue;
    }
}
