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
package org.geotools.filter.v1_1.capabilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.capability.Functions;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.Operator;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v1_1.OGC;


public class FilterMockData {
    static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    static Element functionName(Document document, Node parent) {
        return functionName(document, parent, "foo", 2);
    }

    static Element functionName(Document document, Node parent, String name, int nargs) {
        Element function = element(document, parent, new QName(OGC.NAMESPACE, "FunctionName"));
        function.setAttributeNS("", "nArgs", nargs + "");
        function.appendChild(document.createTextNode(name));

        return function;
    }

    static Element functionNames(Document document, Node parent) {
        Element functionNames = element(document, parent, new QName(OGC.NAMESPACE, "FunctionNames"));
        functionName(document, functionNames, "foo", 2);
        functionName(document, functionNames, "bar", 3);

        return functionNames;
    }

    static Element functions(Document document, Node parent) {
        Element functions = element(document, parent, new QName(OGC.NAMESPACE, "Functions"));
        functionNames(document, functions);

        return functions;
    }

    static FunctionName functionName() {
        return functionName("foo", 2);
    }

    static FunctionName functionName(String name, int args) {
        return ff.functionName(name, args);
    }

    static FunctionName[] functionNames() {
        return new FunctionName[] { functionName("foo", 2), functionName("bar", 3) };
    }

    static Functions functions() {
        return ff.functions(functionNames());
    }

    static Element arithmetic(Document document, Node parent, boolean simple) {
        Element arithmetic = element(document, parent,
                new QName(OGC.NAMESPACE, "ArithmeticOperators"));

        if (simple) {
            element(document, arithmetic, OGC.SimpleArithmetic);
        }

        functions(document, arithmetic);

        return arithmetic;
    }

    static Element arithmetic(Document document, Node parent) {
        return arithmetic(document, parent, true);
    }

    static ArithmeticOperators arithmetic() {
        return arithmetic(true);
    }

    static ArithmeticOperators arithmetic(boolean simple) {
        return ff.arithmeticOperators(simple, functions());
    }

    static Element comparisonOperator(Document document, Node parent, String name) {
        Element operator = element(document, parent, new QName(OGC.NAMESPACE, "ComparisonOperator"));
        operator.appendChild(document.createTextNode(name));

        return operator;
    }

    static Operator comparisonOperator(String name) {
        return ff.operator(name);
    }

    static Element comparison(Document document, Node parent) {
        return comparison(document, parent, true);
    }

    static Element comparison(Document document, Node parent, boolean simple) {
        Element comparison = element(document, parent,
                new QName(OGC.NAMESPACE, "ComparisonOperators"));

        if (simple) {
            comparisonOperator(document, comparison, "LessThan");
            comparisonOperator(document, comparison, "GreaterThan");
            comparisonOperator(document, comparison, "LessThanOrEqualTo");
            comparisonOperator(document, comparison, "GreaterThanOrEqualTo");
            comparisonOperator(document, comparison, "EqualTo");
            comparisonOperator(document, comparison, "NotEqualTo");
        }

        comparisonOperator(document, comparison, "Like");
        comparisonOperator(document, comparison, "Between");
        comparisonOperator(document, comparison, "NullCheck");

        return comparison;
    }

    static ComparisonOperators comparison() {
        return comparison(true);
    }

    static ComparisonOperators comparison(boolean simple) {
        List o = new ArrayList();

        if (simple) {
            o.add(ff.operator("LessThan"));
            o.add(ff.operator("LessThanOrEqualTo"));
            o.add(ff.operator("GreaterThan"));
            o.add(ff.operator("GreaterThanOrEqualTo"));
            o.add(ff.operator("EqualTo"));
            o.add(ff.operator("NotEqualTo"));
        }

        o.add(ff.operator("Like"));
        o.add(ff.operator("Between"));
        o.add(ff.operator("NullCheck"));

        return ff.comparisonOperators((Operator[]) o.toArray(new Operator[o.size()]));
    }

    static Element scalarCapabilities(Document document, Node parent) {
        return scalarCapabilities(document, parent, true);
    }

    static Element scalarCapabilities(Document document, Node parent, boolean logical) {
        Element scalar = element(document, parent, new QName(OGC.NAMESPACE, "Scalar_Capabilities"));

        if (logical) {
            element(document, scalar, OGC.LogicalOperators);
        }

        comparison(document, scalar);
        arithmetic(document, scalar);

        return scalar;
    }

    static ScalarCapabilities scalarCapabilities() {
        return scalarCapabilities(true);
    }

    static ScalarCapabilities scalarCapabilities(boolean logical) {
        return ff.scalarCapabilities(comparison(), arithmetic(), logical);
    }

    static Element geometryOperand(Document document, Node parent, String name) {
        Element operand = element(document, parent, new QName(OGC.NAMESPACE, "GeometryOperand"));
        operand.appendChild(document.createTextNode("gml:" + name));

        return operand;
    }

    static Element geometryOperands(Document document, Node parent) {
        Element operands = element(document, parent, new QName(OGC.NAMESPACE, "GeometryOperands"));
        geometryOperand(document, operands, "Envelope");
        geometryOperand(document, operands, "Point");

        return operands;
    }

    static GeometryOperand[] geometryOperands() {
        return new GeometryOperand[] { GeometryOperand.Envelope, GeometryOperand.Point };
    }

    static Element spatialOperator(Document document, Node parent, String name) {
        Element operator = element(document, parent, new QName(OGC.NAMESPACE, "SpatialOperator"));
        operator.setAttributeNS("", "name", name);

        return operator;
    }

    static SpatialOperator spatialOperator(String name) {
        return ff.spatialOperator(name, null);
    }

    static Element spatial(Document document, Node parent) {
        Element spatial = element(document, parent, new QName(OGC.NAMESPACE, "SpatialOperators"));

        spatialOperator(document, spatial, "BBOX");
        spatialOperator(document, spatial, "Equals");
        spatialOperator(document, spatial, "Disjoint");
        spatialOperator(document, spatial, "Intersect");
        spatialOperator(document, spatial, "Touches");
        spatialOperator(document, spatial, "Contains");
        spatialOperator(document, spatial, "Crosses");
        spatialOperator(document, spatial, "Within");
        spatialOperator(document, spatial, "Overlaps");
        spatialOperator(document, spatial, "Beyond");
        spatialOperator(document, spatial, "DWithin");

        return spatial;
    }

    static SpatialOperators spatial() {
        List o = new ArrayList();

        o.add(spatialOperator("BBOX"));
        o.add(spatialOperator("Equals"));
        o.add(spatialOperator("Disjoint"));
        o.add(spatialOperator("Intersect"));
        o.add(spatialOperator("Touches"));
        o.add(spatialOperator("Crosses"));
        o.add(spatialOperator("Within"));
        o.add(spatialOperator("Contains"));
        o.add(spatialOperator("Overlaps"));
        o.add(spatialOperator("Beyond"));
        o.add(spatialOperator("DWithin"));

        return ff.spatialOperators((SpatialOperator[]) o.toArray(new SpatialOperator[o.size()]));
    }

    static SpatialCapabilities spatialCapabilities() {
        return ff.spatialCapabilities(null, spatial());
    }

    static Element spatialCapabilities(Document document, Node parent) {
        Element spatial = element(document, parent, new QName(OGC.NAMESPACE, "Spatial_Capabilities"));
        spatial(document, spatial);

        return spatial;
    }

    static Element idCapabilities(Document document, Node parent) {
        Element id = element(document, parent, new QName(OGC.NAMESPACE, "Id_Capabilities"));
        element(document, id, OGC.EID);
        element(document, id, OGC.FID);

        return id;
    }

    static IdCapabilities idCapabilities() {
        return ff.idCapabilities(true, true);
    }

    static FilterCapabilities capabilities() {
        return ff.capabilities(FilterCapabilities.VERSION_100, scalarCapabilities(),
            spatialCapabilities(), idCapabilities());
    }

    static Element capabilities(Document document, Node parent) {
        Element capabilities = element(document, parent, OGC.Filter_Capabilities);
        capabilities.setAttributeNS("", "version", "1.0.0");

        scalarCapabilities(document, capabilities);
        spatialCapabilities(document, capabilities);
        idCapabilities(document, capabilities);

        return capabilities;
    }

    static Element element(Document document, Node parent, QName name) {
        Element element = document.createElementNS(name.getNamespaceURI(), name.getLocalPart());

        if (parent != null) {
            parent.appendChild(element);
        }

        return element;
    }
}
