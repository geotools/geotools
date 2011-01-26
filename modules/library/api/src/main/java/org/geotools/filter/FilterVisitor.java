/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;


/**
 * An interface for classes that want to perform operations on a Filter
 * hierarchy. It forms part of a GoF Visitor Patern implementation. A call to
 * filter.accept(FilterVisitor) will result in a call to one of the methods in
 * this interface. The responsibility for traversing sub filters is intended
 * to lie with the visitor (this is unusual, but permited under the Visitor
 * pattern). A typical use would be to transcribe a filter into a specific
 * format, e.g. XML or SQL.  Alternativly it may be to extract specific
 * infomration from the Filter strucure, for example a list of all bboxes.
 *
 * @author James Macgill
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: These need to throw some sort of checked exception as the
 *       implementing classes are swallowing exceptions or throwing runtime
 *       ones.
 *
 * @deprecated use {@link org.opengis.filter.FilterVisitor}
 */
public interface FilterVisitor {
    /**
     * Called when accept is called on an AbstractFilter. As it is imposible to
     * create an instance of AbstractFilter this should never happen.  If it
     * does it means that a subclass of AbstractFilter has failed to implement
     * accept(FilterVisitor) correctly. Implementers of this method should
     * probaly log a warning.
     *
     * @param filter The filter to visit
     */
    void visit(Filter filter);

    /**
     * Called when accept is called on a BetweenFilter. Implementers will want
     * to access the left, middle and right expresions.
     *
     * @param filter The filter to visit
     */
    void visit(BetweenFilter filter);

    /**
     * Called when accept is called on a Compare Filter. Implementers will want
     * to access the left and right expresions.
     *
     * @param filter The filter to visit
     */
    void visit(CompareFilter filter);

    /**
     * Called when accept is called on a Geometry Filter. Implementers will
     * want to access the left and right geometries.
     *
     * @param filter The filter to visit
     */
    void visit(GeometryFilter filter);

    /**
     * Called when accept is called on a Like Filter. Implementers will want to
     * access the pattern and value.
     *
     * @param filter The filter to visit.
     */
    void visit(LikeFilter filter);

    /**
     * Called when accept is called on a Logic Filter. Implementers will want
     * to access the sub filters.
     *
     * @param filter The filter to visit.
     */
    void visit(LogicFilter filter);

    /**
     * Called when accept is called on a Null Filter. Implementers will want to
     * access the null check.
     *
     * @param filter The filter to visit.
     */
    void visit(NullFilter filter);

    /**
     * Called when accept is called on a Fid Filter. Implementers will want to
     * access the fids.
     *
     * @param filter The filter to visit.
     */
    void visit(FidFilter filter);

    /**
     * Called when accept is called on an attribute expression. Implementors
     * will want to access the attribute.
     *
     * @param expression The expression to visit.
     */
    void visit(AttributeExpression expression);

    /**
     * This should never be called.  This can only happen if a subclass of
     * DefaultExpression fails to implement its own version of
     * accept(FilterVisitor);
     *
     * @param expression the expression to visit.
     */
    void visit(Expression expression);

    /**
     * Called when accept is called on a literal expression. Implementors will
     * want to access the literal.
     *
     * @param expression The expression to visit.
     */
    void visit(LiteralExpression expression);

    /**
     * Called when accept is called on an math expression.
     *
     * @param expression The expression to visit.
     */
    void visit(MathExpression expression);

    /**
     * Called when accept is called on an function expression.
     *
     * @param expression The expression to visit.
     */
    void visit(FunctionExpression expression);
}
