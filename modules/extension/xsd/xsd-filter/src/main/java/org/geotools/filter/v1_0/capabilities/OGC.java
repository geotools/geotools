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

import java.util.Set;
import javax.xml.namespace.QName;
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
    /** singleton instance */
    private static final OGC instance = new OGC();

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/ogc";

    /* Type Definitions */
    /** @generated */
    public static final QName Arithmetic_OperatorsType = new QName("http://www.opengis.net/ogc",
            "Arithmetic_OperatorsType");

    /** @generated */
    public static final QName Comparison_OperatorsType = new QName("http://www.opengis.net/ogc",
            "Comparison_OperatorsType");

    /** @generated */
    public static final QName Function_NamesType = new QName("http://www.opengis.net/ogc",
            "Function_NamesType");

    /** @generated */
    public static final QName Function_NameType = new QName("http://www.opengis.net/ogc",
            "Function_NameType");

    /** @generated */
    public static final QName FunctionsType = new QName("http://www.opengis.net/ogc",
            "FunctionsType");

    /** @generated */
    public static final QName Scalar_CapabilitiesType = new QName("http://www.opengis.net/ogc",
            "Scalar_CapabilitiesType");

    /** @generated */
    public static final QName Spatial_CapabilitiesType = new QName("http://www.opengis.net/ogc",
            "Spatial_CapabilitiesType");

    /** @generated */
    public static final QName Spatial_OperatorsType = new QName("http://www.opengis.net/ogc",
            "Spatial_OperatorsType");

    /** @generated */
    public static final QName _BBOX = new QName("http://www.opengis.net/ogc", "_BBOX");

    /** @generated */
    public static final QName _Between = new QName("http://www.opengis.net/ogc", "_Between");

    /** @generated */
    public static final QName _Beyond = new QName("http://www.opengis.net/ogc", "_Beyond");

    /** @generated */
    public static final QName _Contains = new QName("http://www.opengis.net/ogc", "_Contains");

    /** @generated */
    public static final QName _Crosses = new QName("http://www.opengis.net/ogc", "_Crosses");

    /** @generated */
    public static final QName _Disjoint = new QName("http://www.opengis.net/ogc", "_Disjoint");

    /** @generated */
    public static final QName _DWithin = new QName("http://www.opengis.net/ogc", "_DWithin");

    /** @generated */
    public static final QName _Equals = new QName("http://www.opengis.net/ogc", "_Equals");

    /** @generated */
    public static final QName _Filter_Capabilities = new QName("http://www.opengis.net/ogc",
            "_Filter_Capabilities");

    /** @generated */
    public static final QName _Intersect = new QName("http://www.opengis.net/ogc", "_Intersect");

    /** @generated */
    public static final QName _Like = new QName("http://www.opengis.net/ogc", "_Like");

    /** @generated */
    public static final QName _Logical_Operators = new QName("http://www.opengis.net/ogc",
            "_Logical_Operators");

    /** @generated */
    public static final QName _NullCheck = new QName("http://www.opengis.net/ogc", "_NullCheck");

    /** @generated */
    public static final QName _Overlaps = new QName("http://www.opengis.net/ogc", "_Overlaps");

    /** @generated */
    public static final QName _Simple_Arithmetic = new QName("http://www.opengis.net/ogc",
            "_Simple_Arithmetic");

    /** @generated */
    public static final QName _Simple_Comparisons = new QName("http://www.opengis.net/ogc",
            "_Simple_Comparisons");

    /** @generated */
    public static final QName _Touches = new QName("http://www.opengis.net/ogc", "_Touches");

    /** @generated */
    public static final QName _Within = new QName("http://www.opengis.net/ogc", "_Within");

    /* Elements */
    /** @generated */
    public static final QName BBOX = new QName("http://www.opengis.net/ogc", "BBOX");

    /** @generated */
    public static final QName Between = new QName("http://www.opengis.net/ogc", "Between");

    /** @generated */
    public static final QName Beyond = new QName("http://www.opengis.net/ogc", "Beyond");

    /** @generated */
    public static final QName Contains = new QName("http://www.opengis.net/ogc", "Contains");

    /** @generated */
    public static final QName Crosses = new QName("http://www.opengis.net/ogc", "Crosses");

    /** @generated */
    public static final QName Disjoint = new QName("http://www.opengis.net/ogc", "Disjoint");

    /** @generated */
    public static final QName DWithin = new QName("http://www.opengis.net/ogc", "DWithin");

    /** @generated */
    public static final QName Equals = new QName("http://www.opengis.net/ogc", "Equals");

    /** @generated */
    public static final QName Filter_Capabilities = new QName("http://www.opengis.net/ogc",
            "Filter_Capabilities");

    /** @generated */
    public static final QName Intersect = new QName("http://www.opengis.net/ogc", "Intersect");

    /** @generated */
    public static final QName Like = new QName("http://www.opengis.net/ogc", "Like");

    /** @generated */
    public static final QName Logical_Operators = new QName("http://www.opengis.net/ogc",
            "Logical_Operators");

    /** @generated */
    public static final QName NullCheck = new QName("http://www.opengis.net/ogc", "NullCheck");

    /** @generated */
    public static final QName Overlaps = new QName("http://www.opengis.net/ogc", "Overlaps");

    /** @generated */
    public static final QName Simple_Arithmetic = new QName("http://www.opengis.net/ogc",
            "Simple_Arithmetic");

    /** @generated */
    public static final QName Simple_Comparisons = new QName("http://www.opengis.net/ogc",
            "Simple_Comparisons");

    /** @generated */
    public static final QName Touches = new QName("http://www.opengis.net/ogc", "Touches");

    /** @generated */
    public static final QName Within = new QName("http://www.opengis.net/ogc", "Within");

    /**
     * private constructor
     */
    private OGC() {
    }

    /**
     * Returns the singleton instance.
     */
    public static final OGC getInstance() {
        return instance;
    }

    protected void addDependencies(Set dependencies) {
        //TODO: add dependencies here
    }

    /**
     * Returns 'http://www.opengis.net/ogc'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'filterCapabilities.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("filterCapabilities.xsd").toString();
    }

    /* Attributes */
}
