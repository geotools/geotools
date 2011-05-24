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

import org.opengis.annotation.Extension;
import org.opengis.annotation.XmlElement;
import org.opengis.feature.Feature;


/**
 * Interface for all the OGC Filter elements that compute values.
 * <p>
 * The most common use is with potentially using {@linkplain Feature feature} and
 * metadata.  The ability to access "attributes" based on the provided content is
 * defined based on XPath queries currently.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/expression/Expression.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @author Justin Deoliveira (The Open Planning Project)
 * @since GeoAPI 2.0
 */
@XmlElement("expression")
public interface Expression {
    /**
     * Constant expression that always evaulates to {@code null}.
     * <p>
     * This constant is a "NullObject" that can represent the absense of
     * expression in a data structures. As example it can be used to represent
     * the default stroke color in a LineSymbolizer Stroke structure.
     */
    Expression NIL = new NilExpression();

    /**
     * Evaluates the given expression based on the content of the given object.
     */
    @Extension
    Object evaluate(Object object);

    /**
     * Evaluates the given expressoin based on the content of the given object
     * and the context type.
     * <p>
     * The {@code context} parameter is used to control the type of the
     * result of the expression. A particular expression may not be able to evaluate
     * to an instance of {@code context}. Therefore to be safe calling code
     * should do a null check on the return value of this method, and call {@link #evaluate(Object)}
     * if neccessary. Example:
     * <pre>
     *  Object input = ...;
     *  String result = expression.evaluate( input, String.class );
     *  if ( result == null ) {
     *     result = expression.evalute( input ).toString();
     *  }
     *  ...
     * </pre>
     * </p>
     * <p>
     * Implementations that can not return a result as an instance of {@code context}
     * should return {@code null}.
     * </p>
     * @param <T> The type of the returned object.
     * @param object The object to evaluate the expression against.
     * @param context The type of the resulting value of the expression.
     *
     * @return Evaluates the given expression based on the content of the given object an
     *         an instance of {@code context}.
     */
    @Extension
    <T> T evaluate( Object object, Class<T> context );

    /**
     * Accepts a visitor. Subclasses must implement with a method whose content is the following:
     * <pre>return visitor.{@linkplain ExpressionVisitor#visit visit}(this, extraData);</pre>
     */
    @Extension
    Object accept(ExpressionVisitor visitor, Object extraData);
}
