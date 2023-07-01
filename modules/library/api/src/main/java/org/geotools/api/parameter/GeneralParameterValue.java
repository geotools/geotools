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

import static org.geotools.api.annotation.Specification.ISO_19111;

import org.geotools.api.annotation.UML;
import org.geotools.api.util.Cloneable;

/**
 * Abstract parameter value or group of parameter values.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @author Jody Garnett (Refractions Research)
 * @since GeoAPI 1.0
 * @see GeneralParameterDescriptor
 */
@UML(identifier = "CC_GeneralParameterValue", specification = ISO_19111)
public interface GeneralParameterValue extends Cloneable {
    /**
     * Returns the abstract definition of this parameter or group of parameters.
     *
     * @return The abstract definition of this parameter or group of parameters.
     */
    GeneralParameterDescriptor getDescriptor();

    /**
     * Returns a copy of this parameter value or group.
     *
     * @return A copy of this parameter value or group.
     */
    @Override
    GeneralParameterValue clone();
}
