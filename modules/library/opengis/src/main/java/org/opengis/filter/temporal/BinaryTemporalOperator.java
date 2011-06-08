/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.temporal;

import org.opengis.annotation.XmlElement;
import org.opengis.filter.MultiValuedFilter;
import org.opengis.filter.expression.Expression;

/**
 * Base interface for all temporal filter operators.
 *  
 * @author Justin Deoliveira, OpenGeo
 *
 * @since 8.0
 */
@XmlElement("BinaryTemporalOpType")
public interface BinaryTemporalOperator extends MultiValuedFilter {

    /**
     * Returns the first of the two expressions to be compared by this operator.
     */
    @XmlElement("expression")
    Expression getExpression1();

   /**
     * Returns the second of the two expressions to be compared by this operator.
     */
    @XmlElement("expression")
    Expression getExpression2();
}
