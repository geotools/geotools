/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.crs;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A factory providing NetCDF/GRIB custom {@link CoordinateReferenceSystem} instances with the
 * related custom EPSG.
 *
 * @author Daniele Romagnoli - GeoSolutions
 */
public class NetCDFCRSAuthorityFactory extends FactoryUsingWKT implements CRSAuthorityFactory {
    public static final String SYSTEM_DEFAULT_USER_PROJ_FILE = "netcdf.projections.file";

    public NetCDFCRSAuthorityFactory() {
        this(null);
    }

    public NetCDFCRSAuthorityFactory(Hints userHints) {
        this(userHints, MAXIMUM_PRIORITY - 5);
    }

    public NetCDFCRSAuthorityFactory(Hints userHints, int priority) {
        super(userHints, priority);
    }

    /**
     * Returns the URL to the property file that contains CRS definitions.
     *
     * @return The URL, or {@code null} if none.
     */
    protected URL getDefinitionsURL() {
        String cust_proj_file = System.getProperty(SYSTEM_DEFAULT_USER_PROJ_FILE);

        // Attempt to load user-defined projections
        if (cust_proj_file != null) {
            File proj_file = new File(cust_proj_file);

            if (proj_file.exists()) {
                URL url = URLs.fileToUrl(proj_file);
                if (url != null) {
                    return url;
                } else {
                    LOGGER.log(
                            Level.SEVERE, "Had troubles converting " + cust_proj_file + " to URL");
                }
            }
        } else {
            // Use the built-in property definitions
            cust_proj_file = "netcdf.projections.properties";
            return NetCDFCRSAuthorityFactory.class.getResource(cust_proj_file);
        }

        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "\nDefinition URL = " + getDefinitionsURL();
    }
}
