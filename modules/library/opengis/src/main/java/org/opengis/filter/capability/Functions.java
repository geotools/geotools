/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.capability;

import java.util.Collection;

/**
 * Supported functions in a capabilities document.
 *
 * <p>
 *
 * <pre>
 *  &lt;xsd:complexType name="FunctionsType"&gt;
 *    &lt;xsd:sequence&gt;
 *       &lt;xsd:element name="FunctionNames" type="ogc:FunctionNamesType"/&gt;
 *    &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface Functions {

    /**
     * Provided functions.
     *
     * <p>
     *
     * <pre>
     * &lt;xsd:element name="FunctionNames" type="ogc:FunctionNamesType"/&gt;
     * </pre>
     */
    Collection<FunctionName> getFunctionNames();

    /**
     * Looks up a function by name, returning null if no such function is found.
     *
     * @param name The name of the function.
     * @return A function, or null.
     */
    FunctionName getFunctionName(String name);
}
