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
package org.geotools.filter.v2_0.bindings;

import java.util.List;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.geotools.filter.LikeFilterFesImpl;
import org.geotools.filter.v1_0.OGCPropertyIsLikeTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Binding object for the type http://www.opengis.net/fes/2.0:PropertyIsLikeType.
 *
 * <p>
 *
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="PropertyIsLikeType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:ComparisonOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element maxOccurs="2" minOccurs="2" ref="fes:expression"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="wildCard" type="xsd:string" use="required"/&gt;
 *              &lt;xsd:attribute name="singleChar" type="xsd:string" use="required"/&gt;
 *              &lt;xsd:attribute name="escapeChar" type="xsd:string" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *        </code>
 *       </pre>
 *
 * @generated
 */
public class PropertyIsLikeTypeBinding extends OGCPropertyIsLikeTypeBinding {
    FilterFactory factory;

    public PropertyIsLikeTypeBinding(FilterFactory factory) {
        super(factory);
        this.factory = factory;
    }

    @Override
    public QName getTarget() {
        return FES.PropertyIsLikeType;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        PropertyIsLike isLike = (PropertyIsLike) object;

        if (FES.expression.equals(name) && isLike instanceof LikeFilterFesImpl) {
            LikeFilterFesImpl isLikeFes = (LikeFilterFesImpl) isLike;
            return new Object[] {isLikeFes.getExpression1(), isLikeFes.getExpression2()};
        }

        if (FES.expression.equals(name)) {
            return new Object[] {
                isLike.getExpression(),
                isLike.getLiteral() != null ? factory.literal(isLike.getLiteral()) : null
            };
        }

        return super.getProperty(object, name);
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        String wildcard = (String) node.getAttributeValue("wildCard");
        String single = (String) node.getAttributeValue("singleChar");
        String escape = (String) node.getAttributeValue("escape");
        boolean matchCase = true;

        if (node.getAttributeValue("matchCase") != null) {
            matchCase = (Boolean) node.getAttributeValue("matchCase");
        }

        if (escape == null) {
            // 1.1 uses "escapeChar", suppot that too
            escape = (String) node.getAttributeValue("escapeChar");
        }

        // With the 2.0 schema, must support "expression" on RHS
        List<?> childValues = node.getChildValues(Function.class);
        List<Function> functions =
                childValues
                        .stream()
                        .filter(Function.class::isInstance)
                        .map(Function.class::cast)
                        .collect(Collectors.toList());

        if (functions.size() == 2) {
            return createFesLike(
                    functions.get(0), functions.get(1), matchCase, wildcard, single, escape);
        }

        PropertyName name = (PropertyName) node.getChildValue(PropertyName.class);
        Literal literal = (Literal) node.getChildValue(Literal.class);

        if (name == null) {
            return createFesLike(functions.get(0), literal, matchCase, wildcard, single, escape);
        }

        if (literal == null) {
            return createFesLike(name, functions.get(0), matchCase, wildcard, single, escape);
        }

        return factory.like(name, literal.toString(), wildcard, single, escape, matchCase);
    }

    private LikeFilterFesImpl createFesLike(
            Expression propertyExpression,
            Expression valueExpression,
            boolean matchCase,
            String wildcard,
            String single,
            String escape) {
        LikeFilterFesImpl like = new LikeFilterFesImpl(valueExpression);
        like.setExpression(propertyExpression);
        like.setLiteral(valueExpression.evaluate(valueExpression).toString());
        like.setMatchCase(matchCase);
        like.setWildCard(wildcard);
        like.setSingleChar(single);
        like.setEscape(escape);
        return like;
    }
}
