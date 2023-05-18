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
import java.util.Iterator;
import java.util.Objects;
import java.util.logging.Logger;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import org.apache.commons.beanutils.BeanUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.SourceSPIProviderFactory;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;

/** Catalog configuration. */
public class CatalogConfigurationBean {

    private static final Logger LOGGER = Logging.getLogger(CatalogConfigurationBean.class);

    /** The typename to use for the mosaic index */
    private String typeName;

    private String locationAttribute = Utils.DEFAULT_LOCATION_ATTRIBUTE;

    /** Suggested SPI for the various tiles. May be null. * */
    private String suggestedSPI;

    /** Suggested Format for the various tiles. May be null. * */
    private String suggestedFormat;

    /** Suggested InputStreamSPI for the various tiles. May be null. * */
    private String suggestedIsSPI;

    /** we want to use caching for our index. */
    private boolean caching = Utils.DEFAULT_CONFIGURATION_CACHING;

    private boolean heterogeneous;

    private boolean heterogeneousCRS;

    private SourceSPIProviderFactory urlSourceSPIProvider;

    private boolean skipExternalOverviews;

    /**
     * Whether the specified store should be wrapped. Only PostGis stores support this parameter.
     * (Oracle stores are wrapped by default).
     */
    private boolean wrapStore = false;

    private PathType pathType;

    /** The coverage name */
    private String name;

    /** Caches for resolved formats/SPIs */
    private transient AbstractGridFormat resolvedFormat;

    private transient ImageReaderSpi resolvedSuggestedSPI;
    private transient ImageInputStreamSpi resolvedIsSPI;

    private boolean propertySelectionEnabled;

    public CatalogConfigurationBean() {}

    public CatalogConfigurationBean(final CatalogConfigurationBean that) {
        Utilities.ensureNonNull("CatalogConfigurationBean", that);
        try {
            BeanUtils.copyProperties(this, that);
        } catch (IllegalAccessException | InvocationTargetException e) {
            final IllegalArgumentException iae = new IllegalArgumentException(e);
            throw iae;
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

    /** @return the suggestedSPI */
    public String getSuggestedSPI() {
        return suggestedSPI;
    }

    public ImageReaderSpi suggestedSPI() {
        if (suggestedSPI == null) return null;
        if (resolvedSuggestedSPI == null) {
            resolvedSuggestedSPI = lookupImageReaderSpi(suggestedSPI);
            if (resolvedSuggestedSPI == null) {
                LOGGER.info(
                        "The suggested imageReaderSPI: "
                                + suggestedSPI
                                + " wasn't found at the classpath.");
            }
        }
        return resolvedSuggestedSPI;
    }

    static ImageReaderSpi lookupImageReaderSpi(String className) {
        Objects.requireNonNull(className);
        Iterator<ImageReaderSpi> serviceProviders =
                IIORegistry.lookupProviders(ImageReaderSpi.class);
        while (serviceProviders.hasNext()) {
            ImageReaderSpi serviceProvider = serviceProviders.next();
            if (className.equals(serviceProvider.getClass().getName())) {
                return serviceProvider;
            }
        }
        return null;
    }

    /** @param suggestedSPI the suggestedSPI to set */
    public void setSuggestedSPI(final String suggestedSPI) {
        this.suggestedSPI = suggestedSPI;
    }

    public boolean isHeterogeneous() {
        return heterogeneous;
    }

    public void setHeterogeneous(boolean heterogeneous) {
        this.heterogeneous = heterogeneous;
    }

    public boolean isHeterogeneousCRS() {
        return heterogeneousCRS;
    }

    public void setHeterogeneousCRS(boolean heterogeneousCRS) {
        this.heterogeneousCRS = heterogeneousCRS;
    }

    public PathType getPathType() {
        return pathType == null ? PathType.RELATIVE : pathType;
    }

    public void setPathType(PathType pathType) {
        this.pathType = pathType;
    }

    public boolean isWrapStore() {
        return wrapStore;
    }

    public void setWrapStore(boolean wrapStore) {
        this.wrapStore = wrapStore;
    }

    public String getSuggestedFormat() {
        return suggestedFormat;
    }

    public void setSuggestedFormat(String suggestedFormat) {
        this.suggestedFormat = suggestedFormat;
    }

    public AbstractGridFormat suggestedFormat() throws ReflectiveOperationException {
        if (suggestedFormat == null) return null;
        if (resolvedFormat == null) {
            resolvedFormat =
                    (AbstractGridFormat)
                            Class.forName(suggestedFormat).getDeclaredConstructor().newInstance();
        }
        return resolvedFormat;
    }

    public String getSuggestedIsSPI() {
        return suggestedIsSPI;
    }

    public ImageInputStreamSpi suggestedIsSPI() throws ReflectiveOperationException {
        if (suggestedIsSPI == null) return null;
        if (resolvedIsSPI == null) {
            resolvedIsSPI =
                    (ImageInputStreamSpi)
                            Class.forName(suggestedIsSPI).getDeclaredConstructor().newInstance();
        }
        return resolvedIsSPI;
    }

    public void setSuggestedIsSPI(String suggestedIsSPI) {
        this.suggestedIsSPI = suggestedIsSPI;
    }

    public SourceSPIProviderFactory getUrlSourceSPIProvider() {
        return urlSourceSPIProvider;
    }

    public void setUrlSourceSPIProvider(SourceSPIProviderFactory urlSourceSPIProvider) {
        this.urlSourceSPIProvider = urlSourceSPIProvider;
    }

    public boolean isSkipExternalOverviews() {
        return skipExternalOverviews;
    }

    public void setSkipExternalOverviews(boolean skipExternalOverviews) {
        this.skipExternalOverviews = skipExternalOverviews;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPropertySelectionEnabled() {
        return propertySelectionEnabled;
    }

    public void setPropertySelectionEnabled(boolean propertySelectionEnabled) {
        this.propertySelectionEnabled = propertySelectionEnabled;
    }
}
