/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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
