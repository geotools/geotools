/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.grib;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import org.geotools.coverage.io.netcdf.NetCDFFormat;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.imageio.unidata.utilities.UnidataUtilities;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.ParameterGroup;
import org.geotools.util.logging.Logging;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;

public class GRIBFormat extends NetCDFFormat{

    public static final ParameterDescriptor<Filter> FILTER = new DefaultParameterDescriptor<Filter>("Filter", Filter.class, null, null);

    private final static Logger LOGGER = Logging
            .getLogger("org.geotools.coverage.io.netcdf.NetCDFFormat");

    /**
     * Creates an instance and sets the metadata.
     */
    public GRIBFormat() {
        setInfo();
    }

    /**
     * Sets the metadata information.
     */
    private void setInfo() {
        final HashMap<String,String> info = new HashMap<String,String> ();
        info.put("name", "GRIB");
        info.put("description", "GRIB store plugin");
        info.put("vendor", "Geotools");
        info.put("docURL", "");
        info.put("version", "1.0");
        mInfo = info;

        // reading parameters
        readParameters = new ParameterGroup(new DefaultParameterDescriptorGroup(mInfo,
                new GeneralParameterDescriptor[]{
                        READ_GRIDGEOMETRY2D,
                TIME,
                ELEVATION,
                FILTER,
        }));

        // reading parameters
        writeParameters = null;
    }

    @Override
    public boolean accepts(Object source, Hints hints) {
        File file = null;
        if (source instanceof URL) {
            file = DataUtilities.urlToFile((URL) source);
        } else if (source instanceof File ){
            file = (File) source;
        }
        if (file != null) {
            if (file.isDirectory()) {
                return false;
            }
            String fileName = file.getName();
            
            // Check if it is a GRIB data and if the GRIB library is available
            boolean gribExtension = UnidataUtilities.isGribAvailable() && (fileName.contains("grb") || fileName.contains("grib"));
            
            if (fileName.endsWith("ncml") || gribExtension){
                return true;
            }
        }
        return false;
    }
}
