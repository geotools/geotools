/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.Utilities;

/**
 * Catalog configuration.
 */
public class CatalogConfigurationBean {

    /** The typename to use for the mosaic index */
    private String typeName;

    private String locationAttribute = Utils.DEFAULT_LOCATION_ATTRIBUTE;

    /** Suggested SPI for the various tiles. May be null. **/
    private String suggestedSPI;

    /** we want to use caching for our index. */
    private boolean caching = Utils.DEFAULT_CONFIGURATION_CACHING;

    private boolean heterogeneous;

    /**
     * Whether the specified store should be wrapped.
     * Only PostGis stores support this parameter.
     * (Oracle stores are wrapped by default).  
     */
    private boolean wrapStore = false;

    /**
     * <code>true</code> it tells us if the mosaic points to absolute paths or to relative ones. (in case of <code>false</code>).
     */
    private boolean absolutePath = Utils.DEFAULT_PATH_BEHAVIOR;

    public CatalogConfigurationBean() {

    }

    public CatalogConfigurationBean(final CatalogConfigurationBean that) {
        Utilities.ensureNonNull("CatalogConfigurationBean", that);
        try {
            BeanUtils.copyProperties(this, that);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /** location attribute name */

    public String getLocationAttribute() {
        return locationAttribute;
    }

    public void setLocationAttribute(final String locationAttribute) {
        this.locationAttribute = locationAttribute;
    }

    public boolean isCaching() {
        return caching;
    }

    public void setCaching(final boolean caching) {
        this.caching = caching;
    }

    /**
     * @return the suggestedSPI
     */
    public String getSuggestedSPI() {
        return suggestedSPI;
    }

    /**
     * @param suggestedSPI the suggestedSPI to set
     */
    public void setSuggestedSPI(final String suggestedSPI) {
        this.suggestedSPI = suggestedSPI;
    }

    public boolean isHeterogeneous() {
        return heterogeneous;
    }

    public void setHeterogeneous(boolean heterogeneous) {
        this.heterogeneous = heterogeneous;
    }

    public boolean isAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(final boolean absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isWrapStore() {
        return wrapStore;
    }

    public void setWrapStore(boolean wrapStore) {
        this.wrapStore = wrapStore;
    }
}
