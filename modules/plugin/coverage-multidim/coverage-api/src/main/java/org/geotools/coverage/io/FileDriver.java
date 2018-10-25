/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.geotools.util.factory.Hints;
import org.opengis.util.ProgressListener;

/** @author Simone Giannecchini, GeoSolutions */
public interface FileDriver extends Driver {

    /**
     * The list of filename extensions handled by this driver.
     *
     * <p>This List may be empty if the Driver is not file based.
     *
     * <p>
     *
     * @return List of file extensions which can be read by this dataStore.
     */
    public List<String> getFileExtensions();

    public boolean canProcess(
            DriverCapabilities operation, URL url, Map<String, Serializable> params);

    public CoverageAccess process(
            DriverCapabilities operation,
            URL url,
            Map<String, Serializable> params,
            Hints hints,
            final ProgressListener listener)
            throws IOException;
}
