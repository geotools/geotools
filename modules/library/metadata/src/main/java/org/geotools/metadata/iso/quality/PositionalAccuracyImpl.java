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

import org.opengis.metadata.quality.Result;
import org.opengis.metadata.quality.EvaluationMethodType;
import org.opengis.metadata.quality.PositionalAccuracy;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.SimpleInternationalString;


/**
 * Accuracy of the position of features.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class PositionalAccuracyImpl extends ElementImpl implements PositionalAccuracy {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 6043381860937480828L;

    /**
     * Indicates that a {@linkplain org.opengis.referencing.operation.Transformation transformation}
     * requires a datum shift and some method has been applied. Datum shift methods often use
     * {@linkplain org.geotools.referencing.datum.BursaWolfParameters Bursa Wolf parameters},
     * but other kind of method may have been applied as well.
     *
     * @see org.opengis.referencing.operation.Transformation#getPositionalAccuracy
     * @see org.geotools.referencing.operation.AbstractCoordinateOperationFactory#DATUM_SHIFT
     */
    public static final PositionalAccuracy DATUM_SHIFT_APPLIED;

    /**
     * Indicates that a {@linkplain org.opengis.referencing.operation.Transformation transformation}
     * requires a datum shift, but no method has been found applicable. This usually means that no
     * {@linkplain org.geotools.referencing.datum.BursaWolfParameters Bursa Wolf parameters} have
     * been found. Such datum shifts are approximative and may have 1 kilometer error. This
     * pseudo-transformation is allowed by
     * {@linkplain org.geotools.referencing.operation.DefaultCoordinateOperationFactory coordinate
     * operation factory} only if it was created with
     * {@link org.geotools.factory.Hints#LENIENT_DATUM_SHIFT} set to {@link Boolean#TRUE}.
     *
     * @see org.opengis.referencing.operation.Transformation#getPositionalAccuracy
     * @see org.geotools.referencing.operation.AbstractCoordinateOperationFactory#ELLIPSOID_SHIFT
     */
    public static final PositionalAccuracy DATUM_SHIFT_OMITTED;
    static {
        // TODO: localize.
        final InternationalString   desc = new SimpleInternationalString("Transformation accuracy");
        final InternationalString   eval = new SimpleInternationalString("Is a datum shift method applied?");
        final ConformanceResultImpl pass = new ConformanceResultImpl(Citations.GEOTOOLS, eval, true );
        final ConformanceResultImpl fail = new ConformanceResultImpl(Citations.GEOTOOLS, eval, false);
        pass.freeze();
        fail.freeze();
        final PositionalAccuracyImpl APPLIED, OMITTED;
        DATUM_SHIFT_APPLIED = APPLIED = new AbsoluteExternalPositionalAccuracyImpl(pass);
        DATUM_SHIFT_OMITTED = OMITTED = new AbsoluteExternalPositionalAccuracyImpl(fail);
        APPLIED.setMeasureDescription(desc);
        OMITTED.setMeasureDescription(desc);
        APPLIED.setEvaluationMethodDescription(eval);
        OMITTED.setEvaluationMethodDescription(eval);
        APPLIED.setEvaluationMethodType(EvaluationMethodType.DIRECT_INTERNAL);
        OMITTED.setEvaluationMethodType(EvaluationMethodType.DIRECT_INTERNAL);
        APPLIED.freeze();
        OMITTED.freeze();
    }

    /**
     * Constructs an initially empty positional accuracy.
     */
    public PositionalAccuracyImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public PositionalAccuracyImpl(final PositionalAccuracy source) {
        super(source);
    }

    /**
     * Creates an positional accuracy initialized to the given result.
     */
    public PositionalAccuracyImpl(final Result result) {
        super(result);
    }
}
