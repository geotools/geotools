/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.filter.expression;

import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
//
/**
 * Abstract superclass of these Expression implementations.
 * <p>
 * Contains additional support for "Expression chaining". This allows
 * Expressions to be constructed as a chain of Java commands similar to the use
 * of the java collections api.
 * </p>
 * <p>
 * Note: Expression chaining is a simple developer convience, it has no effect
 * on the data model exposed by the GeoAPI interfaces.
 * </p>
 * <p>
 * Idea: We may also be able to teach this implementation to make use of
 * JXPath to extract "attribute values" from Java Beans, DOM, JDOM in addition
 * to the geotools & geoapi FeatureType models. It is a cunning plan - any
 * implementation will make use of this abstract base class.
 * </p>
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public abstract class ExpressionAbstract implements Expression {

    /**
     * Subclass should override, default implementation returns null.
     * 
     * @return default implementation returns null
     */
    public Object evaluate(Object object) {
        return null;
    }
    /**
     * Default implementation delegates handling of context
     * conversion to Value utility class.
     * <p>
     * Subclasses are expected to make use of the Value utility class
     * (as the easiest way to provide value morphing in confirmance with
     *  the Filter specification).
     */
    public <T> T evaluate(Object object, Class<T> context) {
        return Converters.convert(evaluate(object), context);
    }

}
