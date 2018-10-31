/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.data.Parameter;
import org.geotools.data.ServiceInfo;
import org.geotools.data.util.NullProgressListener;
import org.geotools.util.factory.Hints;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

/**
 * Default implementation of {@link CoverageAccess}.
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class DefaultCoverageAccess implements CoverageAccess {

    /** Driver used to create this CoverageAccess. */
    protected final Driver driver;

    protected final EnumSet<AccessType> allowedAccessTypes;

    protected final Map<String, Parameter<?>> accessParameters =
            new HashMap<String, Parameter<?>>();

    protected final Map<String, Serializable> connectionParameters =
            new HashMap<String, Serializable>();

    protected List<Name> names = null;

    public DefaultCoverageAccess(
            Driver driver,
            EnumSet<AccessType> allowedAccessTypes,
            Map<String, Parameter<?>> accessParams,
            Map<String, Serializable> connectionParameters) {
        this.driver = driver;
        this.allowedAccessTypes = allowedAccessTypes.clone();
        this.accessParameters.putAll(accessParams);
        if (connectionParameters != null) {
            this.connectionParameters.putAll(connectionParameters);
        }
    }

    @Override
    public CoverageSource access(
            Name name,
            Map<String, Serializable> params,
            AccessType accessType,
            Hints hints,
            ProgressListener listener)
            throws IOException {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public boolean canCreate(
            Name name, Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        return false;
    }

    public boolean canDelete(Name name, Map<String, Serializable> params, Hints hints)
            throws IOException {
        return false;
    }

    @Override
    public CoverageStore create(
            Name name, Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public boolean delete(Name name, Map<String, Serializable> params, Hints hints)
            throws IOException {
        return false;
    }

    @Override
    public Map<String, Parameter<?>> getAccessParameterInfo(AccessType accessType) {
        return Collections.unmodifiableMap(accessParameters);
    }

    @Override
    public Map<String, Serializable> getConnectParameters() {
        return Collections.unmodifiableMap(connectionParameters);
    }

    @Override
    public int getCoveragesNumber(ProgressListener listener) {
        if (listener == null) listener = new NullProgressListener();
        listener.started();
        try {
            return names.size();
        } finally {
            listener.complete();
        }
    }

    @Override
    public ServiceInfo getInfo(ProgressListener listener) {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public List<Name> getNames(ProgressListener listener) {
        if (listener == null) {
            listener = new NullProgressListener();
        }
        listener.started();
        try {
            return Collections.unmodifiableList(names);
        } finally {
            listener.complete();
        }
    }

    @Override
    public MetadataNode getStorageMetadata(String metadataDomain) {
        throw new UnsupportedOperationException("Operation not implemented");
    }

    @Override
    public Set<String> getStorageMetadataDomains() {
        return Collections.emptySet();
    }

    @Override
    public Set<AccessType> getSupportedAccessTypes() {
        return allowedAccessTypes;
    }

    @Override
    public boolean isCreateSupported() {
        return false;
    }

    @Override
    public boolean isDeleteSupported() {
        return false;
    }

    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public void dispose() {}
}
