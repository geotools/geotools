/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.FileCoverageAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.Parameter;

/** @author Simone Giannecchini, GeoSolutions */
public class DefaultFileCoverageAccess extends DefaultCoverageAccess implements FileCoverageAccess {

    protected final URL source;

    /** */
    public DefaultFileCoverageAccess(
            Driver driver,
            EnumSet<AccessType> allowedAccessTypes,
            Map<String, Parameter<?>> accessParams,
            URL source,
            Map<String, Serializable> connectionParameters)
            throws DataSourceException {
        super(driver, allowedAccessTypes, accessParams, connectionParameters);
        // checks
        if (source == null && connectionParameters == null) {
            throw new DataSourceException("Provided source and connection parameters are null!");
        }
        // check source
        if (source == null) {
            if (connectionParameters.containsKey(DefaultFileDriver.URL.key)) {
                source = (URL) connectionParameters.get(DefaultFileDriver.URL.key);
            }
        }
        if (source == null) {
            throw new DataSourceException("Provided source is null!");
        }
        this.source = source;

        // augment connection params with this one in case it is not there already
        super.connectionParameters.put(DefaultFileDriver.URL.key, source);
    }

    @Override
    public Set<URL> getFileSet() {
        return Collections.singleton(source);
    }
}
