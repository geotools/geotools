/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.filter.spatial;

import org.geotools.api.filter.expression.Expression;

/**
 * Abstract superclass for filter operators that perform some sort of spatial comparison on two
 * geometric objects.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
public interface BinarySpatialOperator extends SpatialOperator {
    /**
     * Returns an expression that will be evaluated to determine the first operand to the spatial
     * predicate represented by this operator. The result of evaluating this expression must be a
     * geometry object.
     */
    Expression getExpression1();

    /**
     * Returns an expression that will be evaluated to determine the second operand to the spatial
     * predicate represented by this operator. The result of evaluating this expression must be a
     * geometry object.
     */
    Expression getExpression2();
}
