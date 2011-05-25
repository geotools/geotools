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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.quality.Element;
import org.opengis.metadata.quality.EvaluationMethodType;
import org.opengis.metadata.quality.Result;
import org.opengis.util.InternationalString;

import org.geotools.metadata.iso.MetadataEntity;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Type of test applied to the data specified by a data quality scope.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ElementImpl extends MetadataEntity implements Element {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -3542504624077298894L;

    /**
     * Name of the test applied to the data.
     */
    private Collection<InternationalString> namesOfMeasure;

    /**
     * Code identifying a registered standard procedure, or {@code null} if none.
     */
    private Identifier measureIdentification;

    /**
     * Description of the measure being determined.
     */
    private InternationalString measureDescription;

    /**
     * Type of method used to evaluate quality of the dataset, or {@code null} if unspecified.
     */
    private EvaluationMethodType evaluationMethodType;

    /**
     * Description of the evaluation method.
     */
    private InternationalString evaluationMethodDescription;

    /**
     * Reference to the procedure information, or {@code null} if none.
     */
    private Citation evaluationProcedure;

    /**
     * Date or range of dates on which a data quality measure was applied.
     * The array length is 1 for a single date, or 2 for a range. Returns
     * {@code null} if this information is not available.
     */
    private long date1 = Long.MIN_VALUE, date2 = Long.MIN_VALUE;

    /**
     * Value (or set of values) obtained from applying a data quality measure or the out
     * come of evaluating the obtained value (or set of values) against a specified
     * acceptable conformance quality level.
     */
    private Collection<Result> results;

    /**
     * Constructs an initially empty element.
     */
    public ElementImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ElementImpl(final Element source) {
        super(source);
    }

    /**
     * Creates an element initialized to the given result.
     */
    public ElementImpl(final Result result) {
        setResults(Collections.singleton(result));
    }

    /**
     * Returns the name of the test applied to the data.
     */
    public synchronized Collection<InternationalString> getNamesOfMeasure() {
        return namesOfMeasure = nonNullCollection(namesOfMeasure, InternationalString.class);
    }

    /**
     * Set the name of the test applied to the data.
     */
    public synchronized void setNamesOfMeasure(
            final Collection<? extends InternationalString> newValues)
    {
        namesOfMeasure = copyCollection(newValues, namesOfMeasure, InternationalString.class);
    }

    /**
     * Returns the code identifying a registered standard procedure, or {@code null} if none.
     */
    public Identifier getMeasureIdentification() {
        return measureIdentification;
    }

    /**
     * Set the code identifying a registered standard procedure.
     */
    public synchronized void setMeasureIdentification(final Identifier newValue)  {
        checkWritePermission();
        measureIdentification = newValue;
    }

    /**
     * Returns the description of the measure being determined.
     */
    public InternationalString getMeasureDescription() {
        return measureDescription;
    }

    /**
     * Set the description of the measure being determined.
     */
    public synchronized void setMeasureDescription(final InternationalString newValue)  {
        checkWritePermission();
        measureDescription = newValue;
    }

    /**
     * Returns the type of method used to evaluate quality of the dataset,
     * or {@code null} if unspecified.
     */
    public EvaluationMethodType getEvaluationMethodType() {
        return evaluationMethodType;
    }

    /**
     * Set the ype of method used to evaluate quality of the dataset.
     */
    public synchronized void setEvaluationMethodType(final EvaluationMethodType newValue)  {
        checkWritePermission();
        evaluationMethodType = newValue;
    }

    /**
     * Returns the description of the evaluation method.
     */
    public InternationalString getEvaluationMethodDescription() {
        return evaluationMethodDescription;
    }

    /**
     * Set the description of the evaluation method.
     */
    public synchronized void setEvaluationMethodDescription(final InternationalString newValue)  {
        checkWritePermission();
        evaluationMethodDescription = newValue;
    }

    /**
     * Returns the reference to the procedure information, or {@code null} if none.
     */
    public Citation getEvaluationProcedure() {
        return evaluationProcedure;
    }

    /**
     * Set the reference to the procedure information.
     */
    public synchronized void setEvaluationProcedure(final Citation newValue) {
        checkWritePermission();
        evaluationProcedure = newValue;
    }

    /**
     * Returns the date or range of dates on which a data quality measure was applied.
     * The array length is 1 for a single date, or 2 for a range. Returns
     * an empty list if this information is not available.
     *
     * @since 2.4
     */
    public synchronized Collection<Date> getDates() {
        if (date1 == Long.MIN_VALUE) {
            return Collections.emptyList();
        }
        if (date2 == Long.MIN_VALUE) {
            return Collections.singleton(new Date(date1));
        }
        return Arrays.asList(
            new Date[] {new Date(date1), new Date(date2)}
        );
    }

    /**
     * Set the date or range of dates on which a data quality measure was applied.
     * The collection size is 1 for a single date, or 2 for a range.
     *
     * @since 2.4
     */
    public void setDates(final Collection<Date> newValues) {
        checkWritePermission();
        date1 = date2 = Long.MIN_VALUE;
        final Iterator<Date> it = newValues.iterator();
        if (it.hasNext()) {
            date1 = it.next().getTime();
            if (it.hasNext()) {
                date2 = it.next().getTime();
                if (it.hasNext()) {
                    throw new IllegalArgumentException(
                            Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH));
                }
            }
        }
    }

    /**
     * Returns the value (or set of values) obtained from applying a data quality measure or
     * the out come of evaluating the obtained value (or set of values) against a specified
     * acceptable conformance quality level.
     *
     * @since 2.4
     */
    public synchronized Collection<Result> getResults() {
        return results = nonNullCollection(results, Result.class);
    }

    /**
     * Set the value (or set of values) obtained from applying a data quality measure or
     * the out come of evaluating the obtained value (or set of values) against a specified
     * acceptable conformance quality level.
     *
     * @since 2.4
     */
    public synchronized void setResults(final Collection<? extends Result> newValues) {
        results = copyCollection(newValues, results, Result.class);
    }
}
