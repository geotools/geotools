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
package org.geotools.filter.text.commons;

import java.util.List;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * This interface presents the methods which will be implemented by the different compiles.
 *
 * <p>Warning: This component is not published. It is part of module implementation. Client module
 * should not use this feature.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public interface ICompiler {

    /** @return the compilation source */
    public String getSource();

    /**
     * Compiles the source string to produce a {@link Filter}. The filter result must be retrieved
     * with {@link #getFilter()}.
     */
    public void compileFilter() throws CQLException;

    /**
     * The resultant filter of the compilation
     *
     * @see #compileFilter()
     * @return Filter
     */
    public Filter getFilter() throws CQLException;

    /**
     * Compiles the source string to produce an {@link Expression}. The resultant expression must be
     * retrieved with {@link #getExpression()}.
     */
    public void compileExpression() throws CQLException;
    /**
     * The resultant {@link Expression} of the compilation.
     *
     * @see #compileExpression()
     * @return Expression
     */
    public Expression getExpression() throws CQLException;

    /**
     * Compiles the source string to produce a {@link List} of {@link Filter}. The result must be
     * retrieved with {@link #getFilterList()()}.
     */
    public void compileFilterList() throws CQLException;

    /**
     * Return the compilation result.
     *
     * @see #compileFilterList()
     * @return List<Filter>
     */
    public List<Filter> getFilterList() throws CQLException;

    /**
     * Return the token presents in the position specified.
     *
     * @return IToken
     */
    public IToken getTokenInPosition(int position);
}
