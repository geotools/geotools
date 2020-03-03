/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wfs.gtxml;

import java.io.IOException;
import javax.xml.namespace.QName;
import org.geotools.xsd.Configuration;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Operates as a front end to GTXML parser/encoder services.
 *
 * <p>This is a simple utility class; if you need more control please look at the implementation of
 * the provided methods.
 */
public class GTXML {
    /**
     * Parse a feature type; using the provided configuration.
     *
     * <p>Usually the configuration is based on org.geotools.wfs.v1_0.WFSConfiguration or
     * org.geotools.wfs.v1_1.WFSConfiguration; you need to indicate which name you want parsed out
     * as a FetureType.
     *
     * @param configuration wfs configuration to use
     * @param name name to parse out as a feature type
     * @param crs Optional coordinate reference system for generated feature type
     * @return FeatureType
     */
    public static FeatureType parseFeatureType(
            Configuration configuration, QName name, CoordinateReferenceSystem crs)
            throws IOException {
        return EmfAppSchemaParser.parse(configuration, name, crs);
    }
}
