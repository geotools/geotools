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
 */
package org.geotools.data.complex.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccess;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Utility class to help keep track of the DataAccess instances created while parsing App-Schema
 * configuration and thus avoid creating the same (i.e. with identical configuration parameters)
 * DataAccess twice.
 *
 * <p>DataAccess instances are indexed by the parameters map used to create them.
 *
 * @author Stefano Costa, GeoSolutions
 */
public class DataAccessMap
        extends HashMap<Map<String, Serializable>, DataAccess<FeatureType, Feature>> {

    /** serialVersionUID */
    private static final long serialVersionUID = 133019722648852790L;
}
