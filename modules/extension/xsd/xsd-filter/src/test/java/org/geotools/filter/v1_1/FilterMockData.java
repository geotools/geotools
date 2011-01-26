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
package org.geotools.filter.v1_1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.opengis.filter.And;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.identity.GmlObjectId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gml3.GML;


/**
 * Mock data class used for filter binding tests.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class FilterMockData {
    static FilterFactory2 f = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);

    static Element propertyName(Document document, Node parent) {
        return propertyName("foo", document, parent);
    }

    static Element propertyName(String property, Document document, Node parent) {
        Element propertyName = element(document, parent, OGC.PropertyName);
        propertyName.appendChild(document.createTextNode(property));

        return propertyName;
    }

    static PropertyName propertyName() {
        return propertyName("foo");
    }

    static PropertyName propertyName(String property) {
        return f.property(property);
    }

    static Element literal(Document document, Node parent) {
        return literal("foo", document, parent);
    }

    static Element literal(String value, Document document, Node parent) {
        Element literal = element(document, parent, OGC.Literal);
        literal.appendChild(document.createTextNode(value));

        return literal;
    }

    static Literal literal() {
        return literal("foo");
    }

    static Literal literal(Object value) {
        return f.literal(value);
    }

    static Element propertyIsEqualTo(Document document, Node parent) {
        return binaryComparisonOp(document, parent, OGC.PropertyIsEqualTo);
    }

    static PropertyIsEqualTo propertyIsEqualTo() {
        return f.equals(propertyName(), literal());
    }

    static Element propertyIsNotEqualTo(Document document, Node parent) {
        return binaryComparisonOp(document, parent, OGC.PropertyIsNotEqualTo);
    }

    static PropertyIsNotEqualTo propertyIsNotEqualTo() {
        return f.notEqual(propertyName(), literal(), false);
    }

    static Element propertyIsLessThan(Document document, Node parent) {
        return binaryComparisonOp(document, parent, OGC.PropertyIsLessThan);
    }

    static PropertyIsLessThan propertyIsLessThan() {
        return f.less(propertyName(), literal());
    }

    static Element propertyIsLessThanOrEqualTo(Document document, Node parent) {
        return binaryComparisonOp(document, parent, OGC.PropertyIsLessThanOrEqualTo);
    }

    static PropertyIsLessThanOrEqualTo propertyIsLessThanOrEqualTo() {
        return f.lessOrEqual(propertyName(), literal());
    }

    static Element propertyIsGreaterThan(Document document, Node parent) {
        return binaryComparisonOp(document, parent, OGC.PropertyIsGreaterThan);
    }

    static PropertyIsGreaterThan propertyIsGreaterThan() {
        return f.greater(propertyName(), literal());
    }

    static Element propertyIsGreaterThanOrEqualTo(Document document, Node parent) {
        return binaryComparisonOp(document, parent, OGC.PropertyIsGreaterThanOrEqualTo);
    }

    static PropertyIsGreaterThanOrEqualTo propertyIsGreaterThanOrEqualTo() {
        return f.greaterOrEqual(propertyName(), literal());
    }

    static Element binaryComparisonOp(Document document, Node parent, QName name) {
        Element binaryComparisonOp = element(document, parent, name);

        propertyName(document, binaryComparisonOp);
        literal(document, binaryComparisonOp);

        return binaryComparisonOp;
    }

    static Element and(Document document, Node parent) {
        Element and = element(document, parent, OGC.And);

        propertyIsEqualTo(document, and);
        propertyIsNotEqualTo(document, and);

        return and;
    }

    static And and() {
        return f.and(propertyIsEqualTo(), propertyIsNotEqualTo());
    }

    static Element or(Document document, Node parent) {
        Element or = element(document, parent, OGC.Or);

        propertyIsEqualTo(document, or);
        propertyIsNotEqualTo(document, or);

        return or;
    }

    static Or or() {
        return f.or(propertyIsEqualTo(), propertyIsNotEqualTo());
    }

    static Not not() {
        return f.not(propertyIsEqualTo());
    }

    static Element not(Document document, Node parent) {
        Element not = element(document, parent, OGC.Not);
        propertyIsEqualTo(document, not);

        return not;
    }

    static Beyond beyond() {
        return f.beyond(f.property("the_geom"), f.literal(geometry()), 1.0d, "m");
    }

    static DWithin dwithin() {
        return f.dwithin(f.property("the_geom"), f.literal(geometry()), 1.0d, "m");
    }

    static Element beyond(Document document, Node parent) {
        return distanceBufferOperator(document, parent, OGC.Beyond);
    }

    static Element dwithin(Document document, Node parent) {
        return distanceBufferOperator(document, parent, OGC.DWithin);
    }

    static Element distanceBufferOperator(Document document, Node parent, QName name) {
        Element doperator = binarySpatialOperator(document, parent, name);
        Element distance = element(document, doperator, new QName(OGC.NAMESPACE, "Distance"));
        distance.appendChild(document.createTextNode("1.0"));
        distance.setAttribute("units", "m");

        return doperator;
    }

    static Element contains(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Contains);
    }

    static Contains contains() {
        return f.contains(f.property("the_geom"), f.literal(geometry()));
    }

    static Element crosses(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Crosses);
    }

    static Crosses crosses() {
        return f.crosses(f.property("the_geom"), f.literal(geometry()));
    }

    static Element disjoint(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Disjoint);
    }

    static Disjoint disjoint() {
        return f.disjoint(f.property("the_geom"), f.literal(geometry()));
    }

    static Element equals(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Equals);
    }

    static Equals equals() {
        return f.equal(f.property("the_geom"), f.literal(geometry()));
    }

    static Element intersects(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Intersects);
    }

    static Intersects intersects() {
        return f.intersects(f.property("the_geom"), f.literal(geometry()));
    }

    static Element overlaps(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Overlaps);
    }

    static Overlaps overlaps() {
        return f.overlaps(f.property("the_geom"), f.literal(geometry()));
    }

    static Element touches(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Touches);
    }

    static Touches touches() {
        return f.touches(f.property("the_geom"), f.literal(geometry()));
    }

    static Element within(Document document, Node parent) {
        return binarySpatialOperator(document, parent, OGC.Within);
    }

    static Within within() {
        return f.within(f.property("the_geom"), f.literal(geometry()));
    }

    static Element binarySpatialOperator(Document document, Node parent, QName name) {
        Element spatial = element(document, parent, name);

        propertyName(document, spatial);
        geometry(document, spatial);

        return spatial;
    }

    static Geometry geometry() {
        return new GeometryFactory().createPoint(new Coordinate(1, 1));
    }

    static Element geometry(Document document, Node parent) {
        Element geometry = element(document, parent, GML.Point);

        Element pos = element(document, geometry, GML.pos);
        pos.appendChild(document.createTextNode("1 1"));

        return geometry;
    }

    static Element envelope(Document document, Node parent) {
        Element envelope = element(document, parent, GML.Envelope);

        Element lower = element(document, envelope, new QName(GML.NAMESPACE, "lowerCorner"));
        lower.appendChild(document.createTextNode("0 0"));

        Element upper = element(document, envelope, new QName(GML.NAMESPACE, "upperCorner"));
        upper.appendChild(document.createTextNode("1 1"));

        return envelope;
    }

    static Element add(Document document, Node parent) {
        return binaryExpression(document, parent, OGC.Add);
    }

    static Add add() {
        return f.add(f.literal(1), f.literal(2));
    }

    static Element sub(Document document, Node parent) {
        return binaryExpression(document, parent, OGC.Sub);
    }

    static Subtract sub() {
        return f.subtract(f.literal(1), f.literal(2));
    }

    static Element mul(Document document, Node parent) {
        return binaryExpression(document, parent, OGC.Mul);
    }

    static Multiply mul() {
        return f.multiply(f.literal(1), f.literal(2));
    }

    static Element div(Document document, Node parent) {
        return binaryExpression(document, parent, OGC.Div);
    }

    static Divide div() {
        return f.divide(f.literal(1), f.literal(2));
    }

    static Element binaryExpression(Document document, Node parent, QName name) {
        Element binaryExpression = element(document, parent, name);
        literal(document, binaryExpression);
        literal(document, binaryExpression);

        return binaryExpression;
    }

    // Identifiers
    static Element gmlObjectId(Document document, Node parent) {
        Element gmlObjectId = element(document, parent, OGC.GmlObjectId);
        gmlObjectId.setAttributeNS(GML.NAMESPACE, "id", "foo");

        return gmlObjectId;
    }

    static GmlObjectId gmlObjectId() {
        return f.gmlObjectId("foo");
    }

    //sorting
    static Element sortBy(Document document, Node parent) {
        Element sortBy = element(document, parent, OGC.SortBy);
        sortProperty(document, sortBy);
        sortProperty(document, sortBy);

        return sortBy;
    }

    static SortBy[] sortBy() {
        return new SortBy[] { sortProperty(), sortProperty() };
    }

    static Element sortProperty(Document document, Node parent) {
        Element sortProperty = element(document, parent, new QName(OGC.NAMESPACE, "SortProperty"));
        propertyName(document, sortProperty);
        sortOrder(document, sortProperty);

        return sortProperty;
    }

    static SortBy sortProperty() {
        return f.sort("foo", SortOrder.ASCENDING);
    }

    static Element sortOrder(Document document, Node parent) {
        Element sortOrder = element(document, parent, new QName(OGC.NAMESPACE, "SortOrder"));
        sortOrder.appendChild(document.createTextNode("ASC"));

        return sortOrder;
    }

    static Element element(Document document, Node parent, QName name) {
        Element element = document.createElementNS(name.getNamespaceURI(), name.getLocalPart());

        if (parent != null) {
            parent.appendChild(element);
        }

        return element;
    }
}
