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

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.filter.text.commons.ICompiler;
import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.io.ParseException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeType;

public class CQLJsonCompiler implements ICompiler {

    static final Logger LOGGER = Logging.getLogger(CQLJsonCompiler.class);

    /** cql expression to compile */
    private final String source;

    private final ObjectMapper mapper = new ObjectMapper();

    private CQLJsonFilterBuilder builder;

    private Filter filter;

    /** new instance of CQL Compiler */
    public CQLJsonCompiler(final String cqlSource, final FilterFactory filterFactory) {

        assert filterFactory != null : "filterFactory cannot be null";

        this.source = cqlSource;
        this.builder = new CQLJsonFilterBuilder(filterFactory);
    }

    @Override
    public String getSource() {
        return source;
    }

    /**
     * Compiles Filter from predicates source json
     *
     * @throws CQLException If there is an issue Parsing the predicates
     */
    @Override
    public void compileFilter() throws CQLException {
        try {
            JsonNode cql2Expression = mapper.readTree(source);
            filter = convertToFilter(cql2Expression);
        } catch (IOException | ParseException e) {
            throw new CQLException(e.getMessage());
        }
    }

    /**
     * Get compiled filter
     *
     * @return Filter
     * @throws CQLException
     */
    @Override
    public Filter getFilter() throws CQLException {
        return filter;
    }

    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public void compileExpression() throws CQLException {
        // Is this really necessary for CQL-JSON??
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public Expression getExpression() throws CQLException {
        return null;
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public void compileFilterList() throws CQLException {
        // Is this really necessary for CQL-JSON??
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public List<Filter> getFilterList() throws CQLException {
        return null;
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public IToken getTokenInPosition(int position) {
        return null;
    }

    /**
     * Converts JSON Node into GT Filter
     *
     * @param cql2Expression JSON Node parsed from source text
     * @return GeoTools Filter
     * @throws CQLException Typically messages about unsupported CQL-JSON features
     * @throws IOException IO Issues
     * @throws ParseException JSON Parsing Issues
     */
    public Filter convertToFilter(JsonNode cql2Expression) throws CQLException, IOException, ParseException {
        Filter out = null;
        if (isCql2Expression(cql2Expression)) {
            String op = cql2Expression.get("op").asString();
            switch (op) {
                case "like":
                    out = builder.convertLike((ArrayNode) cql2Expression.get("args"));
                    break;
                case "=":
                    out = builder.convertEquals((ArrayNode) cql2Expression.get("args"));
                    break;
                case "<>":
                    out = builder.convertNotEquals((ArrayNode) cql2Expression.get("args"));
                    break;
                case ">":
                    out = builder.convertGreaterThan((ArrayNode) cql2Expression.get("args"));
                    break;
                case "<":
                    out = builder.convertLessThan((ArrayNode) cql2Expression.get("args"));
                    break;
                case ">=":
                    out = builder.convertGreaterThanOrEq((ArrayNode) cql2Expression.get("args"));
                    break;
                case "<=":
                    out = builder.convertLessThanOrEq((ArrayNode) cql2Expression.get("args"));
                    break;
                case "between":
                    out = builder.convertBetween((ArrayNode) cql2Expression.get("args"));
                    break;
                case "in":
                    out = builder.convertIn((ArrayNode) cql2Expression.get("args"));
                    break;
                case "isNull":
                    out = builder.convertIsNull((ArrayNode) cql2Expression.get("args"));
                    break;
                case "or":
                    out = builder.convertOr(this, (ArrayNode) cql2Expression.get("args"));
                    break;
                case "and":
                    out = builder.convertAnd(this, (ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_contains":
                    out = builder.convertContains((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_crosses":
                    out = builder.convertCrosses((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_disjoint":
                    out = builder.convertDisjoint((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_equals":
                    out = builder.convertSEquals((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_intersects":
                    out = builder.convertIntersects((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_overlaps":
                    out = builder.convertOverlaps((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_touches":
                    out = builder.convertTouches((ArrayNode) cql2Expression.get("args"));
                    break;
                case "s_within":
                    out = builder.convertWithin((ArrayNode) cql2Expression.get("args"));
                    break;
                case "not":
                    out = builder.convertNot(this, (ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_after":
                    out = builder.convertAfter((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_before":
                    out = builder.convertBefore((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_disjoint":
                    out = builder.convertTDisjoint((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_during":
                    out = builder.convertDuring((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_equals":
                    out = builder.convertEquals((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_finishedBy":
                    out = builder.convertFinishedBy((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_finishing":
                    out = builder.convertFinishing((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_intersects":
                    out = builder.convertTIntersects((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_meets":
                    out = builder.convertMeets((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_metBy":
                    out = builder.convertMetBy((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_overlappedBy":
                    out = builder.convertOverlappedBy((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_overlaps":
                    out = builder.convertTOverlaps((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_startedBy":
                    out = builder.convertStartedBy((ArrayNode) cql2Expression.get("args"));
                    break;
                case "t_starts":
                    out = builder.convertStarts((ArrayNode) cql2Expression.get("args"));
                    break;
                case "a_containedBy":
                    out = builder.convertAContainedBy((ArrayNode) cql2Expression.get("args"));
                    break;
                case "a_contains":
                    out = builder.convertAContaining((ArrayNode) cql2Expression.get("args"));
                    break;
                case "a_equals":
                    out = builder.convertArrayEquals((ArrayNode) cql2Expression.get("args"));
                    break;
                case "a_overlaps":
                    out = builder.convertAOverlaps((ArrayNode) cql2Expression.get("args"));
                    break;
            }
        }

        return out;
    }

    private boolean isCql2Expression(JsonNode node) {
        boolean out = false;
        if (node.getNodeType() == JsonNodeType.OBJECT
                && node.get("op") != null
                && node.get("op").getNodeType() == JsonNodeType.STRING
                && node.get("args") != null
                && node.get("args").getNodeType() == JsonNodeType.ARRAY) {
            return true;
        }
        return out;
    }
}
