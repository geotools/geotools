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

/**
 * @author Christian Mueller
 * 
 * This class holds info for one generalization
 * 
 * distance the distance used in generalization featureName the name of the feature geomPropertyName
 * the name of the geometry property in the feature dataSourceName the name of the datasource
 * dataSourceNameSpace the namespace of the datasource, may be null
 * 
 *
 * @source $URL$
 */
public class Generalization implements Comparable<Generalization> {
    private Double distance;

    private String featureName, geomPropertyName;

    private String dataSourceName, dataSourceNameSpace;

    private GeneralizationInfo parent;

    public Generalization(Double distance, String featureName, String geomPropertyName,
            GeneralizationInfo parent) {
        super();
        this.distance = distance;
        this.featureName = featureName;
        this.geomPropertyName = geomPropertyName;
        this.parent = parent;
    }

    public Double getDistance() {
        return distance;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getGeomPropertyName() {
        return geomPropertyName;
    }

    public int compareTo(Generalization other) {
        return getDistance().compareTo(other.getDistance());
    }

    public String getDataSourceName() {
        if (dataSourceName != null)
            return dataSourceName;
        return parent.getDataSourceName();

    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

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
     *             if data source, feature name or geometry property name is null
     */
    public void validate() throws IOException {
        if (getDataSourceName() == null)
            throw new IOException("Datasource name missing");
        if (getFeatureName() == null)
            throw new IOException("Feature name missing");
        if (getGeomPropertyName() == null)
            throw new IOException("Geometry property name missing");
    }

}
