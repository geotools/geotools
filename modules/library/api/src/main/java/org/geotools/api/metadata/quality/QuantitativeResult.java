/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.quality;

import java.util.Collection;
import javax.measure.Unit;
import org.geotools.api.util.InternationalString;
import org.geotools.api.util.Record;
import org.geotools.api.util.RecordType;

/**
 * Information about the value (or set of values) obtained from applying a data quality measure.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface QuantitativeResult extends Result {
    /**
     * Quantitative value or values, content determined by the evaluation procedure used.
     *
     * @return Quantitative value or values.
     */
    Collection<? extends Record> getValues();

    /**
     * Value type for reporting a data quality result, or {@code null} if none.
     *
     * @return Value type for reporting a data quality result, or {@code null}.
     */
    RecordType getValueType();

    /**
     * Value unit for reporting a data quality result, or {@code null} if none.
     *
     * @return Value unit for reporting a data quality result, or {@code null}.
     */
    Unit<?> getValueUnit();

    /**
     * Statistical method used to determine the value, or {@code null} if none.
     *
     * @return Statistical method used to determine the value, or {@code null}.
     */
    InternationalString getErrorStatistic();
}
