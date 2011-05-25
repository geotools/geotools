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
package org.geotools.filter.v1_0;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.gml2.GML;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/ogc schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class OGC extends XSD {
    /**
     * singleton instance.
     */
    private static OGC instance = new OGC();

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/ogc";

    /* Type Definitions */
    /** @generated */
    public static final QName BBOXType = new QName("http://www.opengis.net/ogc", "BBOXType");

    /** @generated */
    public static final QName BinaryComparisonOpType = new QName("http://www.opengis.net/ogc",
            "BinaryComparisonOpType");

    /** @generated */
    public static final QName BinaryLogicOpType = new QName("http://www.opengis.net/ogc",
            "BinaryLogicOpType");

    /** @generated */
    public static final QName BinaryOperatorType = new QName("http://www.opengis.net/ogc",
            "BinaryOperatorType");

    /** @generated */
    public static final QName BinarySpatialOpType = new QName("http://www.opengis.net/ogc",
            "BinarySpatialOpType");

    /** @generated */
    public static final QName ComparisonOpsType = new QName("http://www.opengis.net/ogc",
            "ComparisonOpsType");

    /** @generated */
    public static final QName DistanceBufferType = new QName("http://www.opengis.net/ogc",
            "DistanceBufferType");

    /** @generated */
    public static final QName DistanceType = new QName("http://www.opengis.net/ogc", "DistanceType");

    /** @generated */
    public static final QName ExpressionType = new QName("http://www.opengis.net/ogc",
            "ExpressionType");

    /** @generated */
    public static final QName FeatureIdType = new QName("http://www.opengis.net/ogc",
            "FeatureIdType");

    /** @generated */
    public static final QName FilterType = new QName("http://www.opengis.net/ogc", "FilterType");

    /** @generated */
    public static final QName FunctionType = new QName("http://www.opengis.net/ogc", "FunctionType");

    /** @generated */
    public static final QName LiteralType = new QName("http://www.opengis.net/ogc", "LiteralType");

    /** @generated */
    public static final QName LogicOpsType = new QName("http://www.opengis.net/ogc", "LogicOpsType");

    /** @generated */
    public static final QName LowerBoundaryType = new QName("http://www.opengis.net/ogc",
            "LowerBoundaryType");

    /** @generated */
    public static final QName PropertyIsBetweenType = new QName("http://www.opengis.net/ogc",
            "PropertyIsBetweenType");

    /** @generated */
    public static final QName PropertyIsLikeType = new QName("http://www.opengis.net/ogc",
            "PropertyIsLikeType");

    /** @generated */
    public static final QName PropertyIsNullType = new QName("http://www.opengis.net/ogc",
            "PropertyIsNullType");

    /** @generated */
    public static final QName PropertyNameType = new QName("http://www.opengis.net/ogc",
            "PropertyNameType");

    /** @generated */
    public static final QName SpatialOpsType = new QName("http://www.opengis.net/ogc",
            "SpatialOpsType");

    /** @generated */
    public static final QName UnaryLogicOpType = new QName("http://www.opengis.net/ogc",
            "UnaryLogicOpType");

    /** @generated */
    public static final QName UpperBoundaryType = new QName("http://www.opengis.net/ogc",
            "UpperBoundaryType");

    /* Elements */
    /** @generated */
    public static final QName Add = new QName("http://www.opengis.net/ogc", "Add");

    /** @generated */
    public static final QName And = new QName("http://www.opengis.net/ogc", "And");

    /** @generated */
    public static final QName BBOX = new QName("http://www.opengis.net/ogc", "BBOX");

    /** @generated */
    public static final QName Beyond = new QName("http://www.opengis.net/ogc", "Beyond");

    /** @generated */
    public static final QName comparisonOps = new QName("http://www.opengis.net/ogc",
            "comparisonOps");

    /** @generated */
    public static final QName Contains = new QName("http://www.opengis.net/ogc", "Contains");

    /** @generated */
    public static final QName Crosses = new QName("http://www.opengis.net/ogc", "Crosses");

    /** @generated */
    public static final QName Disjoint = new QName("http://www.opengis.net/ogc", "Disjoint");

    /** @generated */
    public static final QName Div = new QName("http://www.opengis.net/ogc", "Div");

    /** @generated */
    public static final QName DWithin = new QName("http://www.opengis.net/ogc", "DWithin");

    /** @generated */
    public static final QName Equals = new QName("http://www.opengis.net/ogc", "Equals");

    /** @generated */
    public static final QName expression = new QName("http://www.opengis.net/ogc", "expression");

    /** @generated */
    public static final QName FeatureId = new QName("http://www.opengis.net/ogc", "FeatureId");

    /** @generated */
    public static final QName Filter = new QName("http://www.opengis.net/ogc", "Filter");

    /** @generated */
    public static final QName Function = new QName("http://www.opengis.net/ogc", "Function");

    /** @generated */
    public static final QName Intersects = new QName("http://www.opengis.net/ogc", "Intersects");

    /** @generated */
    public static final QName Literal = new QName("http://www.opengis.net/ogc", "Literal");

    /** @generated */
    public static final QName logicOps = new QName("http://www.opengis.net/ogc", "logicOps");

    /** @generated */
    public static final QName Mul = new QName("http://www.opengis.net/ogc", "Mul");

    /** @generated */
    public static final QName Not = new QName("http://www.opengis.net/ogc", "Not");

    /** @generated */
    public static final QName Or = new QName("http://www.opengis.net/ogc", "Or");

    /** @generated */
    public static final QName Overlaps = new QName("http://www.opengis.net/ogc", "Overlaps");

    /** @generated */
    public static final QName PropertyIsBetween = new QName("http://www.opengis.net/ogc",
            "PropertyIsBetween");

    /** @generated */
    public static final QName PropertyIsEqualTo = new QName("http://www.opengis.net/ogc",
            "PropertyIsEqualTo");

    /** @generated */
    public static final QName PropertyIsGreaterThan = new QName("http://www.opengis.net/ogc",
            "PropertyIsGreaterThan");

    /** @generated */
    public static final QName PropertyIsGreaterThanOrEqualTo = new QName("http://www.opengis.net/ogc",
            "PropertyIsGreaterThanOrEqualTo");

    /** @generated */
    public static final QName PropertyIsLessThan = new QName("http://www.opengis.net/ogc",
            "PropertyIsLessThan");

    /** @generated */
    public static final QName PropertyIsLessThanOrEqualTo = new QName("http://www.opengis.net/ogc",
            "PropertyIsLessThanOrEqualTo");

    /** @generated */
    public static final QName PropertyIsLike = new QName("http://www.opengis.net/ogc",
            "PropertyIsLike");

    /** @generated */
    public static final QName PropertyIsNotEqualTo = new QName("http://www.opengis.net/ogc",
            "PropertyIsNotEqualTo");

    /** @generated */
    public static final QName PropertyIsNull = new QName("http://www.opengis.net/ogc",
            "PropertyIsNull");

    /** @generated */
    public static final QName PropertyName = new QName("http://www.opengis.net/ogc", "PropertyName");

    /** @generated */
    public static final QName spatialOps = new QName("http://www.opengis.net/ogc", "spatialOps");

    /** @generated */
    public static final QName Sub = new QName("http://www.opengis.net/ogc", "Sub");

    /** @generated */
    public static final QName Touches = new QName("http://www.opengis.net/ogc", "Touches");

    /** @generated */
    public static final QName Within = new QName("http://www.opengis.net/ogc", "Within");

    /**
     * private constructor.
     */
    private OGC() {
    }

    /**
     * The singleton instance.
     */
    public static OGC getInstance() {
        return instance;
    }

    protected void addDependencies(Set dependencies) {
        dependencies.add(GML.getInstance());
    }

    /**
     * Returns 'http://www.opengis.net/ogc'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'filter.xsd'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("filter.xsd").toString();
    }
}
