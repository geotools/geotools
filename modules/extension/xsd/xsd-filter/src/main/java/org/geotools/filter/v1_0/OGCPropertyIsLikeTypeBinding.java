/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v1_0;

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Binding object for the type http://www.opengis.net/ogc:PropertyIsLikeType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="PropertyIsLikeType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:ComparisonOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *                  &lt;xsd:element ref="ogc:Literal"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="wildCard" type="xsd:string" use="required"/&gt;
 *              &lt;xsd:attribute name="singleChar" type="xsd:string" use="required"/&gt;
 *              &lt;xsd:attribute name="escape" type="xsd:string" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class OGCPropertyIsLikeTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCPropertyIsLikeTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return OGC.PropertyIsLikeType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return PropertyIsLike.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @SuppressWarnings("unchecked")
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Expression name = null;
        List<Expression> names = node.getChildValues(Expression.class);
        if (names.size() == 2) { // simple pair extract PropertyName
            name = (Expression) node.getChildValue(PropertyName.class);
        }
        if (name == null) { // search for a non literal Expression
            for (Expression n : names) {
                if (n instanceof Literal) {
                    continue;
                }
                name = n;
            }
        }
        Literal literal = (Literal) node.getChildValue(Literal.class);

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

        return factory.like(name, literal.toString(), wildcard, single, escape, matchCase);
    }

    public Object getProperty(Object object, QName name) throws Exception {
        PropertyIsLike isLike = (PropertyIsLike) object;

        if (OGC.expression.equals(name) && !(isLike.getExpression() instanceof Literal)) {
            return isLike.getExpression();
        }

        if (OGC.PropertyName.equals(name)) {
            return isLike.getExpression();
        }

        if (OGC.Literal.equals(name)) {
            return isLike.getLiteral() != null ? factory.literal(isLike.getLiteral()) : null;
        }

        if ("wildCard".equals(name.getLocalPart())) {
            return isLike.getWildCard();
        }

        if ("singleChar".equals(name.getLocalPart())) {
            return isLike.getSingleChar();
        }

        if ("escape".equals(name.getLocalPart()) || "escapeChar".equals(name.getLocalPart())) {
            return isLike.getEscape();
        }

        if ("matchCase".equals(name.getLocalPart())) {
            return isLike.isMatchingCase();
        }

        return null;
    }
}
