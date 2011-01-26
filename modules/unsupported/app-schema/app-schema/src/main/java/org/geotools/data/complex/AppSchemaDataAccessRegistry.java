/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Arrays;

import org.geotools.data.DataAccess;
import org.geotools.data.FeatureSource;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

public class AppSchemaDataAccessRegistry extends DataAccessRegistry {
    /**
     * A registry that stores all app schema data access instances per application. This allows
     * mappings from different data accesses to be accessed globally.
     * 
     * @author Rini Angreani, Curtin University of Technology
     */
    private static final long serialVersionUID = -373404928035022963L;

    /**
     * Get a feature type mapping from a registered app-schema data access. Please note that this is
     * only possible for app-schema data access instances.
     * 
     * @param featureTypeName
     * @return feature type mapping
     * @throws IOException
     */
    public static FeatureTypeMapping getMappingByName(Name featureTypeName) throws IOException {
        if (registry == null) {
            throw new UnsupportedOperationException("No registered data access found for: "
                    + featureTypeName.toString());
        }
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess) {
                if (((AppSchemaDataAccess) dataAccess).hasName(featureTypeName)) {
                    return ((AppSchemaDataAccess) dataAccess).getMappingByName(featureTypeName);
                }
            }
        }
        throwDataSourceException(featureTypeName);

        return null;
    }

    public static FeatureTypeMapping getMappingByElement(Name featureTypeName) throws IOException {
        if (registry == null) {
            throw new UnsupportedOperationException("No registered data access found for: "
                    + featureTypeName.toString());
        }
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess) {
                if (((AppSchemaDataAccess) dataAccess).hasElement(featureTypeName)) {
                    return ((AppSchemaDataAccess) dataAccess).getMappingByElement(featureTypeName);
                }
            }
        }
        throwDataSourceException(featureTypeName);

        return null;
    }

    /**
     * Get a feature source for simple features with supplied feature type name.
     * 
     * @param featureTypeName
     * @return feature source
     * @throws IOException
     */
    public static FeatureSource<FeatureType, Feature> getSimpleFeatureSource(Name featureTypeName)
            throws IOException {
        return getMappingByElement(featureTypeName).getSource();
    }

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses. If
     * the type mapping has mappingName, then it will be the key that is matched in the search. If
     * it doesn't, then it will match the targetElementName.
     * 
     * @param featureTypeName
     *            Feature type name
     * @return
     * @throws IOException
     */
    public static boolean hasName(Name featureTypeName) throws IOException {
        if (registry == null) {
            // nothing's been registered, but it's OK, return false
            return false;
        }
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess
                    && (((AppSchemaDataAccess) dataAccess).hasName(featureTypeName) || ((AppSchemaDataAccess) dataAccess)
                            .hasElement(featureTypeName))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if a type name is mapped in one of the registered app-schema data accesses as
     * targetElementName, regardless whether or not mappingName exists.
     * 
     * @param featureTypeName
     * @return
     * @throws IOException
     */
    public static boolean hasTargetElement(Name featureTypeName) throws IOException {
        if (registry == null) {
            // nothing's been registered, but it's OK, return false
            return false;
        }
        for (DataAccess<FeatureType, Feature> dataAccess : registry) {
            if (dataAccess instanceof AppSchemaDataAccess
                    && Arrays.asList(((AppSchemaDataAccess) dataAccess).getTypeNames()).contains(
                            featureTypeName)) {
                return true;
            }
        }
        return false;
    }

}
