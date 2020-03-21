/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import org.geotools.appschema.util.InterpolationProperties;
import org.geotools.data.FeatureSource;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * A registry that stores all app schema data access instances per application. This allows mappings
 * from different data accesses to be accessed globally.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaDataAccessRegistry extends DataAccessRegistry {

    // NC - this class is only kept for backward compatibility, all the work is done in
    // DataAccessRegistry

    private static final long serialVersionUID = -1517768637801603351L;

    // -------------------------------------------------------------------------------------
    // Static short-cut methods for convenience and backward compatibility
    // -----------------------------------------------------------------------------------

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses. If
     * the type mapping has mappingName, then it will be the key that is matched in the search. If
     * it doesn't, then it will match the targetElementName.
     *
     * @param featureTypeName Feature type name
     */
    public static boolean hasName(Name featureTypeName) throws IOException {
        return getInstance().hasAppSchemaAccessName(featureTypeName);
    }

    /**
     * Get a feature type mapping from a registered app-schema data access. Please note that this is
     * only possible for app-schema data access instances.
     *
     * @return feature type mapping
     */
    public static FeatureTypeMapping getMappingByName(Name featureTypeName) throws IOException {
        return getInstance().mappingByName(featureTypeName);
    }

    public static FeatureTypeMapping getMappingByElement(Name featureTypeName) throws IOException {
        return getInstance().mappingByElement(featureTypeName);
    }

    /**
     * Get a feature source for simple features with supplied feature type name.
     *
     * @return feature source
     */
    @SuppressWarnings("unchecked")
    public static FeatureSource<FeatureType, Feature> getSimpleFeatureSource(Name featureTypeName)
            throws IOException {
        return getMappingByElement(featureTypeName).getSource();
    }

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses as
     * targetElementName, regardless whether or not mappingName exists.
     */
    public static boolean hasTargetElement(Name featureTypeName) throws IOException {
        return getInstance().hasAppSchemaTargetElement(featureTypeName);
    }

    /**
     * Get App-schema properties
     *
     * @return app-schema properties
     */
    public static InterpolationProperties getAppSchemaProperties() {
        return getInstance().getProperties();
    }

    /** Clean-up properties, mainly used for cleaning up after tests */
    public static void clearAppSchemaProperties() {
        getInstance().clearProperties();
    }
}
