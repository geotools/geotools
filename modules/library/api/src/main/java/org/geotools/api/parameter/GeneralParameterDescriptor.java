/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.parameter;

import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19111;

import org.geotools.api.annotation.UML;
import org.geotools.api.referencing.IdentifiedObject;

/**
 * Abstract definition of a parameter or group of parameters used by an operation method.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @author Jody Garnett (Refractions Research)
 * @since GeoAPI 2.0
 * @see GeneralParameterValue
 */
public interface GeneralParameterDescriptor extends IdentifiedObject {
    /**
     * Creates a new instance of {@linkplain GeneralParameterValue parameter value or group} initialized with the
     * {@linkplain ParameterDescriptor#getDefaultValue default value(s)}. The
     * {@linkplain GeneralParameterValue#getDescriptor parameter value descriptor} for the created parameter value(s)
     * will be {@code this} object.
     *
     * @return A new parameter initialized to its default value.
     */
    GeneralParameterValue createValue();

    /**
     * The minimum number of times that values for this parameter group or parameter are required. The default value is
     * one. A value of 0 means an optional parameter.
     *
     * @return The minimum occurence.
     * @see #getMaximumOccurs
     */
    int getMinimumOccurs();

    /**
     * The maximum number of times that values for this parameter group or parameter can be included. For a
     * {@linkplain ParameterDescriptor single parameter}, the value is always 1. For a
     * {@linkplain ParameterDescriptorGroup parameter group}, it may vary. The default value is one.
     *
     * @return The maximum occurence.
     * @see #getMinimumOccurs
     */
    @UML(identifier = "CC_OperationParameterGroup.maximumOccurs", obligation = OPTIONAL, specification = ISO_19111)
    int getMaximumOccurs();
}
