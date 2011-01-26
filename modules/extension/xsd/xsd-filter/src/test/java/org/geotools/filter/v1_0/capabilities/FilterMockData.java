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
package org.geotools.filter.v1_0.capabilities;

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
import org.opengis.filter.capability.Operator;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.geotools.factory.CommonFactoryFinder;


public class FilterMockData {
    static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    static Element functionName(Document document, Node parent) {
        return functionName(document, parent, "foo", 2);
    }

    static Element functionName(Document document, Node parent, String name, int nargs) {
        Element function = element(document, parent, new QName(OGC.NAMESPACE, "Function_Name"));
        function.setAttributeNS("", "nArgs", nargs + "");
        function.appendChild(document.createTextNode(name));

        return function;
    }

    static Element functionNames(Document document, Node parent) {
        Element functionNames = element(document, parent, new QName(OGC.NAMESPACE, "Function_Names"));
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
                new QName(OGC.NAMESPACE, "Arithmetic_Operators"));

        if (simple) {
            element(document, arithmetic, OGC.Simple_Arithmetic);
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

    static Element comparison(Document document, Node parent) {
        return comparison(document, parent, true);
    }

    static Element comparison(Document document, Node parent, boolean simple) {
        Element comparison = element(document, parent,
                new QName(OGC.NAMESPACE, "Comparison_Operators"));

        if (simple) {
            element(document, comparison, OGC.Simple_Comparisons);
        }

        element(document, comparison, OGC.Like);
        element(document, comparison, OGC.Between);
        element(document, comparison, OGC.NullCheck);

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
            element(document, scalar, OGC.Logical_Operators);
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

    static Element spatial(Document document, Node parent) {
        Element spatial = element(document, parent, new QName(OGC.NAMESPACE, "Spatial_Operators"));
        element(document, spatial, OGC.BBOX);
        element(document, spatial, OGC.Equals);
        element(document, spatial, OGC.Disjoint);
        element(document, spatial, OGC.Intersect);
        element(document, spatial, OGC.Touches);
        element(document, spatial, OGC.Contains);
        element(document, spatial, OGC.Crosses);
        element(document, spatial, OGC.Within);
        element(document, spatial, OGC.Overlaps);
        element(document, spatial, OGC.Beyond);
        element(document, spatial, OGC.DWithin);

        return spatial;
    }

    static SpatialOperators spatial() {
        List o = new ArrayList();

        o.add(ff.spatialOperator("BBOX", null));
        o.add(ff.spatialOperator("Equals", null));
        o.add(ff.spatialOperator("Disjoint", null));
        o.add(ff.spatialOperator("Intersect", null));
        o.add(ff.spatialOperator("Touches", null));
        o.add(ff.spatialOperator("Crosses", null));
        o.add(ff.spatialOperator("Within", null));
        o.add(ff.spatialOperator("Contains", null));
        o.add(ff.spatialOperator("Overlaps", null));
        o.add(ff.spatialOperator("Beyond", null));
        o.add(ff.spatialOperator("DWithin", null));

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

    static FilterCapabilities capabilities() {
        return ff.capabilities(FilterCapabilities.VERSION_100, scalarCapabilities(),
            spatialCapabilities(), null);
    }

    static Element capabilities(Document document, Node parent) {
        Element capabilities = element(document, parent, OGC.Filter_Capabilities);
        capabilities.setAttributeNS("", "version", "1.0.0");

        scalarCapabilities(document, capabilities);
        spatialCapabilities(document, capabilities);

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
