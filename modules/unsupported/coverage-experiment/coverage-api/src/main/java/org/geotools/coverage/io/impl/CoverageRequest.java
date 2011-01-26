/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl;

import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.opengis.feature.type.Name;

/**
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 */
public abstract class CoverageRequest  {

    /**
     * @uml.property name="name"
     */
    private Name name;

    /**
     * @uml.property name="handle"
     */
    private String handle;

    /**
     * @uml.property name="hints"
     */
    private Hints hints;

    /**
     * @uml.property name="additionalParameters"
     */
    private Map<String, Parameter<?>> additionalParameters;

    /**
     * The handle attribute is included to allow a client to associate a mnemonic name to the Query
     * request. The purpose of the handle attribute is to provide an error handling mechanism for
     * locating a statement that might fail.
     * 
     * @return the mnemonic name of the query request.
     * @uml.property name="handle"
     */
    public String getHandle() {
        return handle;
    }

    /**
     * @see org.geotools.coverage.io.CoverageRequest#setHandle(java.lang.String)
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Specifies some hints to drive the query execution and results build-up.
     * <p>
     * Hints examples can be the GeometryFactory to be used, a generalization distance to be applied
     * right in the data store, to data store specific things such as the fetch size to be used in
     * JDBC queries. The set of hints supported can be fetched by calling {@links
     * FeatureSource#getSupportedHints()} . Depending on the actual values of the hints, the data
     * store is free to ignore them. No mechanism is in place, at the moment, to figure out which
     * hints where actually used during the query execution.
     * 
     * @return the Hints the data store should try to use when executing the query (eventually empty
     *         but never null).
     * @uml.property name="hints"
     */
    public Hints getHints() {
        return hints;
    }

    /**
     * @see org.geotools.coverage.io.CoverageRequest#setHints(Hints)
     */
    public void setHints(Hints hints) {
        this.hints = hints;
    }

    /**
     * @see org.geotools.coverage.io.CoverageRequest#setAdditionalParameters(Map)
     */
    public void setAdditionalParameters(Map<String, Parameter<?>> additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    /**
     * @see org.geotools.coverage.io.CoverageRequest#getAdditionalParameters()
     */
    public Map<String, Parameter<?>> getAdditionalParameters() {
        return additionalParameters;
    }

    /**
     * @see org.geotools.coverage.io.CoverageRequest#getName()
     */
    public Name getName() {
        return name;
    }

    /**
     * @see org.geotools.coverage.io.CoverageRequest#setName(org.opengis.feature.type.Name)
     */
    public void setName(Name name) {
        this.name = name;
    }

}
