/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.parameter;

import org.opengis.util.Cloneable;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.*;


/**
 * Abstract parameter value or group of parameter values.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Jody Garnett (Refractions Research)
 * @since   GeoAPI 1.0
 *
 * @see GeneralParameterDescriptor
 */
@UML(identifier="CC_GeneralParameterValue", specification=ISO_19111)
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
    GeneralParameterValue clone();
}
