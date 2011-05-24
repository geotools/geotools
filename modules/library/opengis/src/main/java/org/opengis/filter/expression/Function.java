/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */package org.opengis.filter.expression;

import java.util.List;

import org.opengis.annotation.XmlElement;


/**
 * Instances of this class represent a function call into some implementation-specific
 * function.
 * <p>
 * Each execution environment should provide a list of supported functions
 * (and the number of arguments they expect) as part of a FilterCapabilities
 * data structure.
 * <p>
 * This is included for completeness with respect to the
 * OGC Filter specification.  However, no functions are required to be supported
 * by that specification.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/expression/Function.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("Function")
public interface Function extends Expression {
    /**
     * Returns the name of the function to be called.  For example, this might
     * be "{@code cos}" or "{@code atan2}".
     * <p>
     * You can use this name to look up the number of required parameters
     * in a FilterCapabilities data structure. For the specific meaning of
     * the required parameters you will need to consult the documentation.
     */
    String getName();

   /**
     * Returns the list subexpressions that will be evaluated to provide the
     * parameters to the function.
     */
    List<Expression> getParameters();

    /**
     * The value of the fallbackValue attribute is used as a default value, if the SE
     * implementation does not support the function. If the implementation supports the
     * function, then the result value is determined by executing the function.
     *
     * @return Optional literal to use if an implementation for this function is not available.
     *
     * @since GeoAPI 2.2
     */
    @XmlElement("fallbackValue")
    Literal getFallbackValue();
}
