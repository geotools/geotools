/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.util.CheckedArrayList;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 * @since 2.4
 */
public class TypeMapping implements Serializable {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(TypeMapping.class);

    private static final long serialVersionUID = 1444252634598922057L;

    private String sourceDataStore;

    private String sourceTypeName;

    private String indexDataStore;
    private String indexTypeName;

    private String itemXpath;

    /**
     * True if we don't want to create a new feature, but want to add attributes to the feature
     * returned from the backend Data access.
     */
    private boolean isXmlDataStore;

    /**
     * True if data is multiple rows represent 1 feature. The safe default is to assume it's true.
     */
    private boolean isDenormalised = true;
    /** True if isDenormalised has been set in config. */
    private boolean isDenormalisedSet = false;

    private String targetElementName;

    private String defaultGeometryXPath;

    private List attributeMappings = Collections.EMPTY_LIST;

    /**
     * Optional unique identifier for a FeatureTypeMapping, useful for multiple mappings of the same
     * type.
     */
    private String mappingName;

    public TypeMapping() {
        // no-op
    }

    public List getAttributeMappings() {
        return new ArrayList(attributeMappings);
    }

    public void setAttributeMappings(List attributeMappings) {
        this.attributeMappings = new CheckedArrayList(AttributeMapping.class);
        if (attributeMappings != null) {
            this.attributeMappings.addAll(attributeMappings);
        }
    }

    public String getSourceDataStore() {
        return sourceDataStore;
    }

    public void setSourceDataStore(String sourceDataStore) {
        this.sourceDataStore = sourceDataStore;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }

    public String getTargetElementName() {
        return targetElementName;
    }

    public void setTargetElementName(String targetElementName) {
        this.targetElementName = targetElementName;
    }

    public String getDefaultGeometryXPath() {
        return defaultGeometryXPath;
    }

    public void setDefaultGeometryXPath(String defaultGeometryXPath) {
        this.defaultGeometryXPath = defaultGeometryXPath;
    }

    public String getItemXpath() {
        return itemXpath;
    }

    public void setItemXpath(String itemXpath) {
        this.itemXpath = itemXpath;
    }

    public void setXmlDataStore(String isXmlDataStore) {
        this.isXmlDataStore = Boolean.valueOf(isXmlDataStore).booleanValue();
    }

    public boolean isXmlDataStore() {
        return isXmlDataStore;
    }

    public boolean isDenormalised() {
        if (!isDenormalisedSet) {
            LOGGER.info(
                    "isDenormalised is not set in app-schema mapping file for: "
                            + (mappingName == null ? targetElementName : mappingName)
                            + ".\n"
                            + "Setting isDenormalised can result in more efficient SQL queries.");
        }
        return isDenormalised;
    }

    public void setIsDenormalised(String isDenormalised) {
        this.isDenormalisedSet = true;
        this.isDenormalised = Boolean.valueOf(isDenormalised).booleanValue();
    }

    public void setMappingName(final String mappingName) {
        this.mappingName = mappingName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public String getIndexDataStore() {
        return indexDataStore;
    }

    public void setIndexDataStore(String indexDataStore) {
        this.indexDataStore = indexDataStore;
    }

    public String getIndexTypeName() {
        return indexTypeName;
    }

    public void setIndexTypeName(String indexTypeName) {
        this.indexTypeName = indexTypeName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TypeMappingDTO[");
        if (mappingName != null) {
            sb.append("mappingName=").append(mappingName).append(",\n ");
        }
        sb.append("sourceDataStore=")
                .append(sourceDataStore)
                .append(",\n sourceTypeName=")
                .append(sourceTypeName)
                .append(",\n targetElementName=")
                .append(targetElementName)
                .append(",\n attributeMappings=")
                .append(attributeMappings)
                .append("]");
        return sb.toString();
    }
}
