/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.referencing.IdentifiedObject;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Definition of an algorithm used to perform a coordinate operation. Most operation
 * methods use a number of operation parameters, although some coordinate conversions
 * use none. Each coordinate operation using the method assigns values to these parameters.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see Operation
 */
@UML(identifier="CC_OperationMethod", specification=ISO_19111)
public interface OperationMethod extends IdentifiedObject {
    /**
     * Key for the <code>{@value}</code> property.
     * This is used for setting the value to be returned by {@link #getFormula}.
     *
     * @see #getFormula
     */
    String FORMULA_KEY = "formula";

    /**
     * Formula(s) or procedure used by this operation method. This may be a reference to a
     * publication. Note that the operation method may not be analytic, in which case this
     * attribute references or contains the procedure, not an analytic formula.
     *
     * @return The formula used by this method.
     */
    @UML(identifier="formula", obligation=MANDATORY, specification=ISO_19111)
    InternationalString getFormula();

    /**
     * Number of dimensions in the source CRS of this operation method.
     *
     * @return The dimension of source CRS.
     */
    @UML(identifier="sourceDimensions", obligation=MANDATORY, specification=ISO_19111)
    int getSourceDimensions();

    /**
     * Number of dimensions in the target CRS of this operation method.
     *
     * @return The dimension of target CRS.
     */
    @UML(identifier="targetDimensions", obligation=MANDATORY, specification=ISO_19111)
    int getTargetDimensions();

    /**
     * The set of parameters.
     *
     * @return The parameters, or an empty group if none.
     */
    @UML(identifier="usesParameter", obligation=MANDATORY, specification=ISO_19111)
    ParameterDescriptorGroup getParameters();
}
