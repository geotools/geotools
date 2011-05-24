/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.expression;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * Abstract base class for the various filter expressions that compute some
 * value from two input values.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/expression/BinaryExpression.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("BinaryOperatorType")
public interface BinaryExpression extends Expression {
    /**
     * Returns the expression that represents the first (left) value that will
     * be used in the computation of another value.
     */
    @XmlElement("expression")
    Expression getExpression1();

    /**
     * Returns the expression that represents the second (right) value that will
     * be used in the computation of another value.
     */
    @XmlElement("expression")
    Expression getExpression2();

}
