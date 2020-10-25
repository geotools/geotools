/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geotools.filter.text.commons.ICompiler;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cqljson.model.Predicates;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;

public class CQLJson {
    private CQLJson() {}

    /**
     * Parses the input string in OGC CQL format into a Filter, using the systems default
     * FilterFactory implementation.
     *
     * @param cqlJson a string containing a query predicate in OGC CQL format.
     * @return a {@link Filter} equivalent to the constraint specified in <code>cqlPredicate</code>.
     */
    public static Filter toFilter(final String cqlJson) throws CQLException {
        Filter filter = CQLJson.toFilter(cqlJson, null);

        return filter;
    }

    /**
     * Converts JSON string into CQL Predicates
     *
     * @param cqlJson CQL as JSON String
     * @return CQL Predicates
     * @throws CQLException Thrown if issue with parsing
     */
    public static Predicates jsonToPredicates(String cqlJson) throws CQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(cqlJson, Predicates.class);
        } catch (JsonProcessingException e) {
            throw new CQLException(e.getMessage());
        }
    }

    /**
     * Parses the input string in OGC CQL format into a Filter, using the provided FilterFactory.
     *
     * @param cqlPredicate a string containing a query predicate in OGC CQL format.
     * @param filterFactory the {@link FilterFactory} to use for the creation of the Filter. If it
     *     is null the method finds the default implementation.
     * @return a {@link Filter} equivalent to the constraint specified in <code>Predicate</code>.
     */
    public static Filter toFilter(final String cqlPredicate, final FilterFactory2 filterFactory)
            throws CQLException {

        CQLJsonCompilerFactory compilerFactory = new CQLJsonCompilerFactory();
        ICompiler compiler = compilerFactory.createCompiler(cqlPredicate, filterFactory);
        compiler.compileFilter();
        return compiler.getFilter();
    }
}
