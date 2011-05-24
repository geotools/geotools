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

// Annotation
import org.opengis.annotation.Extension;


/**
 * Visitor with {@code visit} methods to be called by {@link Expression#accept Expression.accept(...)}.
 * <p>
 * Please note that a generic visit( Expression ) entry point has not been provided, although Expression
 * forms a heirarchy for your convience it is not an open heirarchy. If you need to extend this system
 * please make use of {code Function}, this will allow extention while remaining standards complient.
 * </p>
 * <p>
 * It is very common for a single instnace to implement both ExpressionVisitor and FilterVisitor.
 * </p>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/expression/ExpressionVisitor.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@Extension
public interface ExpressionVisitor {
    /**
     * Used to visit a Expression.NIL, also called for <code>null</code> where an
     * expression is expected.
     * <p>
     * This is particularly useful when doing data transformations, as an example when
     * using a StyleSymbolizer Expression.NIL can be used to represent the default
     * stroke color.
     * </p>
     * @param extraData
     * @return implementation specific
     */
    Object visit(NilExpression  expression, Object extraData);
    Object visit(Add            expression, Object extraData);
    Object visit(Divide         expression, Object extraData);
    Object visit(Function       expression, Object extraData);
    Object visit(Literal        expression, Object extraData);
    Object visit(Multiply       expression, Object extraData);
    Object visit(PropertyName   expression, Object extraData);
    Object visit(Subtract       expression, Object extraData);
}
