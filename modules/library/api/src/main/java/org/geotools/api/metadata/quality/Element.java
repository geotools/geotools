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

import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import java.util.Date;
import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.util.InternationalString;

/**
 * Type of test applied to the data specified by a data quality scope.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface Element {
    /**
     * Name of the test applied to the data.
     *
     * @return Name of the test applied to the data.
     */
    Collection<? extends InternationalString> getNamesOfMeasure();

    /**
     * Code identifying a registered standard procedure, or {@code null} if none.
     *
     * @return Code identifying a registered standard procedure, or {@code null}.
     */
    Identifier getMeasureIdentification();

    /**
     * Description of the measure being determined.
     *
     * @return Description of the measure being determined, or {@code null}.
     */
    InternationalString getMeasureDescription();

    /**
     * Type of method used to evaluate quality of the dataset, or {@code null} if unspecified.
     *
     * @return Type of method used to evaluate quality, or {@code null}.
     */
    EvaluationMethodType getEvaluationMethodType();

    /**
     * Description of the evaluation method.
     *
     * @return Description of the evaluation method, or {@code null}.
     */
    @UML(identifier = "evaluationMethodDescription", obligation = OPTIONAL, specification = ISO_19115)
    InternationalString getEvaluationMethodDescription();

    /**
     * Reference to the procedure information, or {@code null} if none.
     *
     * @return Reference to the procedure information, or {@code null}.
     */
    Citation getEvaluationProcedure();

    /**
     * Date or range of dates on which a data quality measure was applied. The collection size is 1
     * for a single date, or 2 for a range. Returns an empty collection if this information is not
     * available.
     *
     * @return Date or range of dates on which a data quality measure was applied.
     * @since GeoAPI 2.1
     */
    Collection<? extends Date> getDates();

    /**
     * Value (or set of values) obtained from applying a data quality measure or the out come of
     * evaluating the obtained value (or set of values) against a specified acceptable conformance
     * quality level.
     *
     * @return Set of values obtained from applying a data quality measure.
     * @since GeoAPI 2.1
     */
    Collection<? extends Result> getResults();
}
