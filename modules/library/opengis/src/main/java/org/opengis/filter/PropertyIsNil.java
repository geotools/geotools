/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

// OpenGIS direct dependencies
import org.opengis.annotation.XmlElement;
import org.opengis.filter.expression.Expression;


/**
 * Filter operator that checks if an expression's value is nil.
 * <p>
 * From the specification:
 * <i><pre>
 * The PropertyIsNil operator tests the content of the specified property and evaluates if it is nil. 
 * The operator can also evaluate the nil reason using the nilReason parameter. The implied operator 
 * for evaluating the nil reason is "equals".
 * </pre></i>
 * </p>
 *
 * @author Justin Deoliveira, OpenGeo
 * @see http://portal.opengeospatial.org/files/?artifact_id=39968
 * @since GeoAPI 2.0
 */
@XmlElement("PropertyIsNil")
public interface PropertyIsNil extends Filter {
    /** Operator name used to check FilterCapabilities */
    public static String NAME = "Nil";

    /**
     * Returns the expression whose value will be checked for {@code null}.
     */
    @XmlElement("expression")
    Expression getExpression();

    /**
     * The nil reason.
     */
    @XmlElement("nilReason")
    Object getNilReason();
}
