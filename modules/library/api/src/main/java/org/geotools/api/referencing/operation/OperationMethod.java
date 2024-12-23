/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.operation;

import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.util.InternationalString;

/**
 * Definition of an algorithm used to perform a coordinate operation. Most operation methods use a number of operation
 * parameters, although some coordinate conversions use none. Each coordinate operation using the method assigns values
 * to these parameters.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see Operation
 */
public interface OperationMethod extends IdentifiedObject {
    /**
     * Key for the <code>{@value}</code> property. This is used for setting the value to be returned by
     * {@link #getFormula}.
     *
     * @see #getFormula
     */
    String FORMULA_KEY = "formula";

    /**
     * Formula(s) or procedure used by this operation method. This may be a reference to a publication. Note that the
     * operation method may not be analytic, in which case this attribute references or contains the procedure, not an
     * analytic formula.
     *
     * @return The formula used by this method.
     */
    InternationalString getFormula();

    /**
     * Number of dimensions in the source CRS of this operation method.
     *
     * @return The dimension of source CRS.
     */
    int getSourceDimensions();

    /**
     * Number of dimensions in the target CRS of this operation method.
     *
     * @return The dimension of target CRS.
     */
    int getTargetDimensions();

    /**
     * The set of parameters.
     *
     * @return The parameters, or an empty group if none.
     */
    ParameterDescriptorGroup getParameters();
}
