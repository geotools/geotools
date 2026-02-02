/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.io.ParseException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * <b>OGC API CQL2-Json</b>. This class presents the operations available to convert filters into the CQL2-Json language
 *
 * <p>
 */
public class CQL2Json {

    private CQL2Json() {
        // do nothing, private constructor
        // to indicate it is a pure utility class
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Generates the CQL2-Json predicate associated to the {@link Filter} object.
     *
     * @return CQL2-Json predicate
     */
    public static JsonNode toCQL2Json(Filter filter) {
        FilterToCQL2Json toCQL2Json = new FilterToCQL2Json(objectMapper);
        return (JsonNode) filter.accept(toCQL2Json, objectMapper.createObjectNode());
    }

    /**
     * Generates the CQL2-Json predicate as a string associated to the {@link Filter} object.
     *
     * @param filter the filter to convert
     * @return the CQL2-Json predicate as a string
     */
    public static String toCQL2(Filter filter) {
        return toCQL2Json(filter).toString();
    }

    /**
     * Converts CQL2-Json to a {@link Filter} object.
     *
     * @param cql2Json CQL2-Json predicate
     * @return {@link Filter} object
     * @throws CQLException if the CQL2-Json predicate is not valid
     */
    public static Filter toFilter(String cql2Json) throws CQLException {
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(cql2Json, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        return cqlJsonCompiler.getFilter();
    }

    /**
     * Converts {@link Expression} object to JSON String.
     *
     * @param expression the expression to convert
     * @return the JSON String
     */
    public static String toCQL2(Expression expression) {
        return toCQL2Json(expression).toString();
    }

    /**
     * Converts {@link Expression} object to JsonNode.
     *
     * @param expression the expression to convert
     * @return the JsonNode
     */
    public static JsonNode toCQL2Json(Expression expression) {
        ExpressionToCQL2Json expVisitor = new ExpressionToCQL2Json(objectMapper);
        return (JsonNode) expression.accept(expVisitor, objectMapper.createArrayNode());
    }

    /**
     * Converts JSON String to {@link Expression} object.
     *
     * @param cql2Json the JSON String
     * @return the {@link Expression} object
     * @throws CQLException if the JSON String is not valid
     * @throws IOException if an error occurs while parsing the JSON String
     * @throws ParseException if an error occurs while parsing the JSON String
     */
    public static Expression toExpression(String cql2Json) throws CQLException, IOException, ParseException {
        CQLJsonFilterBuilder cqlJsonFilterBuilder = new CQLJsonFilterBuilder(new FilterFactoryImpl());
        return cqlJsonFilterBuilder.getExpression(objectMapper.readTree(cql2Json));
    }
}
