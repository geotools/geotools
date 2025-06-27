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
package org.geotools.xsd.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFinder;
import org.geotools.xsd.Node;

/**
 * Convenience class for filter parsing.
 *
 * <p>The primary function of this class is to share code among the different versions of filter parsing.
 *
 * @author Justin Deoliveira, OpenGEO
 */
public class FilterParsingUtils {

    public static Object Filter_getProperty(Object object, QName qName) {
        Filter filter = (Filter) object;

        // use the local name to handle oth the OGC and FES namespaces
        String name = qName.getLocalPart();

        // &lt;xsd:element ref="ogc:spatialOps"/&gt;
        if ("spatialOps".equals(name) && filter instanceof BinarySpatialOperator) {
            return filter;
        }

        // &lt;xsd:element ref="ogc:comparisonOps"/&gt;
        if ("comparisonOps".equals(name)) {
            // JD: extra check here because many of our spatial implementations
            // extend both
            if (filter instanceof BinaryComparisonOperator && !(filter instanceof BinarySpatialOperator)) {
                return filter;
            } else {
                // filters that don't extend BinaryComparisonOperator but are still
                // comparisonOps
                if (filter instanceof PropertyIsLike
                        || filter instanceof PropertyIsNull
                        || filter instanceof PropertyIsBetween) {
                    return filter;
                }
            }
        }

        // &lt;xsd:element ref="ogc:logicOps"/&gt;
        if ("logicOps".equals(name) && (filter instanceof BinaryLogicOperator || filter instanceof Not)) {
            return filter;
        }
        // &lt;xsd:element ref="ogc:temporalOps"/&gt;
        if ("temporalOps".equals(name) && filter instanceof BinaryTemporalOperator) {
            return filter;
        }

        // &lt;xsd:element maxOccurs="unbounded" ref="ogc:_Id"/&gt;
        if (filter instanceof Id && ("_Id".equals(name) /*1.1/2.0*/ || "FeatureId".equals(name) /*1.0*/)) {
            // unwrap
            Id id = (Id) filter;

            return id.getIdentifiers();
        }

        return null;
    }

    public static List<Filter> BinaryLogicOperator_getChildFilters(
            Node node, org.geotools.api.filter.FilterFactory factory) {
        List<Filter> filters = node.getChildValues(Filter.class);
        if (filters.size() < 2) {
            // look for Id elements and turn them into fid filters
            // note: this is not spec compliant and is a bit lax
            List<Identifier> ids = node.getChildValues(Identifier.class);
            for (Identifier id : ids) {
                filters.add(factory.id(Collections.singleton(id)));
            }

            // look for extended operator(s)
            filters.addAll(parseExtendedOperators(node, factory));
        }

        // TODO: this parsing returns the children out of order...
        return filters;
    }

    public static List<Filter> parseExtendedOperators(Node node, org.geotools.api.filter.FilterFactory factory) {
        List<Filter> extOps = new ArrayList<>();

        // TODO: this doesn't actually handle the case of an extended operator that does not take
        // any arguments
        if (node.hasChild(Expression.class)) {
            // case of a single operator containing a single expression
            Node n = node.getChild(Expression.class);
            Name opName = new NameImpl(
                    n.getComponent().getNamespace(), n.getComponent().getName());

            Filter extOp = lookupExtendedOperator(opName, Arrays.asList((Expression) n.getValue()), factory);
            if (extOp != null) {
                extOps.add(extOp);
            }
        } else if (node.hasChild(Map.class)) {
            List<Node> children = node.getChildren(Map.class);
            for (Node n : children) {
                Name opName = new NameImpl(
                        n.getComponent().getNamespace(), n.getComponent().getName());
                Map map = (Map) n.getValue();

                List<Expression> expressions = new ArrayList<>();
                for (Object o : map.values()) {
                    if (o instanceof Expression) {
                        expressions.add((Expression) o);
                    }
                }

                Filter extOp = lookupExtendedOperator(opName, expressions, factory);
                if (extOp != null) {
                    extOps.add(extOp);
                }
            }
        }

        return extOps;
    }

    static Filter lookupExtendedOperator(
            Name opName, List<Expression> expressions, org.geotools.api.filter.FilterFactory factory) {
        FunctionFinder finder = new FunctionFinder(null);
        Function f = finder.findFunction(opName.getLocalPart(), expressions);
        return factory.equal(f, factory.literal(true), true);
    }
}
