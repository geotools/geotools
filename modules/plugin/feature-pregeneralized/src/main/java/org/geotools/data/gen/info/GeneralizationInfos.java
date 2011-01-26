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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Christian Mueller
 * 
 * Container for for GeneralizationInfo objects
 * 
 *
 * @source $URL$
 */
public class GeneralizationInfos {
    private Map<String, GeneralizationInfo> infoMap;

    private String dataSourceName, dataSourceNameSpace;

    public GeneralizationInfos() {
        infoMap = new HashMap<String, GeneralizationInfo>();
    }

    /**
     * add a GeneralizationInfo object
     * 
     * @param info
     * 
     */
    public void addGeneralizationInfo(GeneralizationInfo info) {
        infoMap.put(info.getBaseFeatureName(), info);
    }

    /**
     * remove a GeneralizationInfo object
     * 
     * @param info
     */
    public void removeGeneralizationInfo(GeneralizationInfo info) {
        infoMap.remove(info.getBaseFeatureName());
    }

    /**
     * get GeneralizationInfo for baseFeatureName
     * 
     * @see GeneralizationInfo for info about baseFeatureName
     * 
     * @param baseFeatureName
     * @return GeneralizationInfo or null
     */
    public GeneralizationInfo getGeneralizationInfoForBaseFeatureName(String baseFeatureName) {
        return infoMap.get(baseFeatureName);
    }

    /**
     * get GeneralizationInfo for featureName
     * 
     * @see GeneralizationInfo for info about featureName
     * 
     * @param featureName
     * @return GeneralizationInfo or null
     */
    public GeneralizationInfo getGeneralizationInfoForFeatureName(String featureName) {
        for (GeneralizationInfo info : infoMap.values())
            if (info.getFeatureName().equals(featureName))
                return info;
        return null;
    }

    /**
     * @see GeneralizationInfo for info about basefeatureName
     * 
     * @return list of base feature names
     */
    public Collection<String> getBaseFeatureNames() {
        TreeSet<String> names = new TreeSet<String>();
        names.addAll(infoMap.keySet());
        return names;
    }

    /**
     * @see GeneralizationInfo for info about featureName
     * 
     * @return list of feature names
     */

    public Collection<String> getFeatureNames() {
        TreeSet<String> names = new TreeSet<String>();
        for (GeneralizationInfo info : infoMap.values())
            names.add(info.getFeatureName());
        return names;
    }

    /**
     * This data source is the default data source for all GeneraliziationInfo objects in this
     * container
     * 
     * @return the data source name or null
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * This workspace is the default workspace for all GeneraliziationInfo objects in this container
     * 
     * @return the namespace name or null
     */

    public String getDataSourceNameSpace() {
        return dataSourceNameSpace;
    }

    public void setDataSourceNameSpace(String namespace) {
        this.dataSourceNameSpace = namespace;
    }

    public Collection<GeneralizationInfo> getGeneralizationInfoCollection() {
        return infoMap.values();
    }

    /**
     * 
     * @throws IOException
     *             if the validation of the generalization info objects fails
     * 
     */
    public void validate() throws IOException {
        for (GeneralizationInfo gi : getGeneralizationInfoCollection()) {
            gi.validate();
        }
    }

}
