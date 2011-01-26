/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen.info;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Container having a base feature and 0..n generalizations
 * 
 * The base feature is the original feature for which generalizations were build
 * 
 * The feature type of the base feature is the feature type of a PregeneralizedFeature, except
 * geometry properties holding generalized geometries
 * 
 * if a generalized geometry is stored in a different feature source, this feature source must
 * include all non geometry properties from the baseFeatureSource
 * 
 * 
 * @author Christian Mueller
 * 
 *
 * @source $URL$
 */
public class GeneralizationInfo {

    private String featureName, baseFeatureName, geomPropertyName;

    private SortedSet<Generalization> generalizations;

    private String dataSourceName, dataSourceNameSpace;

    private GeneralizationInfos parent;

    public GeneralizationInfo(String baseFeatureName, String featureName, String geomPropertyName,
            GeneralizationInfos parent) {
        super();
        this.baseFeatureName = baseFeatureName;
        this.featureName = featureName;
        this.geomPropertyName = geomPropertyName;
        this.generalizations = new TreeSet<Generalization>();
        this.parent = parent;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getBaseFeatureName() {
        return baseFeatureName;
    }

    public String getGeomPropertyName() {
        return geomPropertyName;
    }

    public SortedSet<Generalization> getGeneralizations() {
        return generalizations;
    }

    /**
     * @param requestedDistance
     * @return The proper Generalization for the requested distance, null if no proper distance
     *         found example: Given are generalizations for 10.0 and 20 0<= requestedDistance < 10
     *         ---> return null 10<= requestedDistance < 20 ---> return distance info for 10.0 20<=
     *         requestedDistance ---> return distance info for 20.0
     */
    public Generalization getGeneralizationForDistance(Double requestedDistance) {

        if (requestedDistance == null)
            return null;
        Generalization result = null;
        for (Generalization di : generalizations) {
            if (requestedDistance >= di.getDistance())
                result = di;
            else
                break;
        }
        return result;

    }

    /**
     * @return data source name for base feature.
     * 
     */
    public String getDataSourceName() {
        if (dataSourceName != null)
            return dataSourceName;
        return parent.getDataSourceName();
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * @return workspace name for base feature, my be null
     */
    public String getDataSourceNameSpace() {
        if (dataSourceNameSpace != null)
            return dataSourceNameSpace;

        return parent.getDataSourceNameSpace();
    }

    public void setDataSourceNameSpace(String namespace) {
        this.dataSourceNameSpace = namespace;
    }

    /**
     * Validates not null instance variables
     * 
     * @throws IOException
     *             1)if data source, feature name, base feature name or geometry property name is
     *             null 2) if the validation of generalizations fails
     * 
     */
    public void validate() throws IOException {
        if (getDataSourceName() == null)
            throw new IOException("Datasource name missing");
        if (getFeatureName() == null)
            throw new IOException("Feature name missing");
        if (getBaseFeatureName() == null)
            throw new IOException("Base feature name missing");
        if (getGeomPropertyName() == null)
            throw new IOException("Geometry property name missing");
        for (Generalization di : getGeneralizations()) {
            di.validate();
        }
    }

}
