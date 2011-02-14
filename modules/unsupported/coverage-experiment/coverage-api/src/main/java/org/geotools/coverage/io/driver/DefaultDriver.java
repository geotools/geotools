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
package org.geotools.coverage.io.driver;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Base Implementation for the {@link Driver} interface.
 */
public class DefaultDriver implements Driver {

    private String name;

    private InternationalString description;

    private InternationalString title;

    private Map<Key, ?> implementationHints;

    private Map<String, Parameter<?>> connectParameterInfo;

    private Map<String, Parameter<?>> createParameterInfo;

    private Map<String, Parameter<?>> deleteParameterInfo;

    protected DefaultDriver(final String name, final String description, final String title,
            final Hints implementationHints) {
        this.name = name;
        this.description = new SimpleInternationalString(description);
        this.title = new SimpleInternationalString(title);
    }

    public String getName() {
        return this.name;
    }

    public InternationalString getTitle() {
        return this.title;
    }

    /**
     * Implementation hints provided during construction.
     * <p>
     * Often these hints are configuration and factory settings used to intergrate the driver with
     * application services.
     */
    public Map<Key, ?> getImplementationHints() {
        return this.implementationHints;
    }

    public InternationalString getDescription() {
        return this.description;
    }

    public boolean canProcess(final DriverOperation operation,
            final Map<String, Serializable> params) {

        if (!getDriverCapabilities().contains(operation))
            throw new UnsupportedOperationException("Operation " + operation
                    + " is not supported by this driver");
        switch (operation) {
        case CONNECT:
            return canConnect(params);
        case DELETE:
            return canDelete(params);
        case CREATE:
            return canCreate(params);
        default:
            throw new IllegalArgumentException("Operation " + operation + " uknknown ");
        }
    }

    public CoverageAccess process(final DriverOperation operation,
            final Map<String, Serializable> params, Hints hints, final ProgressListener listener)
            throws IOException {

        if (!getDriverCapabilities().contains(operation))
            throw new UnsupportedOperationException("Operation " + operation
                    + " is not supported by this driver");
        switch (operation) {
        case CONNECT:
            return connect(params, hints, listener);
        case DELETE:
            return delete(params, hints, listener);
        case CREATE:
            return create(params, hints, listener);
        default:
            throw new IllegalArgumentException("Operation " + operation + " uknknown ");
        }
    }

    protected synchronized Map<String, Parameter<?>> getConnectParameterInfo() {
        if (connectParameterInfo == null) {
            connectParameterInfo = defineConnectParameterInfo();
            if (connectParameterInfo == null) {
                connectParameterInfo = Collections.emptyMap();
            }
        }
        return connectParameterInfo;
    }

    protected synchronized Map<String, Parameter<?>> getDeleteParameterInfo() {
        if (deleteParameterInfo == null) {
            deleteParameterInfo = defineDeleteParameterInfo();
            if (deleteParameterInfo == null) {
                deleteParameterInfo = Collections.emptyMap();
            }
        }
        return deleteParameterInfo;
    }

    protected synchronized Map<String, Parameter<?>> getCreateParameterInfo() {
        if (createParameterInfo == null) {
            createParameterInfo = defineCreateParameterInfo();
            if (createParameterInfo == null) {
                createParameterInfo = Collections.emptyMap();
            }
        }
        return createParameterInfo;
    }

    public Map<String, Parameter<?>> getParameterInfo(DriverOperation operation) {
        switch (operation) {
        case CONNECT:
            return getConnectParameterInfo();
        case DELETE:
            return getDeleteParameterInfo();
        case CREATE:
            return getCreateParameterInfo();
        default:
            throw new IllegalArgumentException("Operation " + operation + " uknknown ");
        }
    }

    
    protected boolean canConnect(Map<String, Serializable> params) {
    	return false;
    }

    
    protected boolean canCreate(Map<String, Serializable> params) {
    	return false;
    }

    
    protected boolean canDelete(Map<String, Serializable> params) {
    	return false;
    }

    
    protected CoverageAccess connect(Map<String, Serializable> params, Hints hints,
    		ProgressListener listener) throws IOException {
    
    	throw new UnsupportedOperationException("Operation not currently implemented");
    }

    
    protected CoverageAccess create(Map<String, Serializable> params, Hints hints,
    		ProgressListener listener) throws IOException {
    	throw new UnsupportedOperationException("Operation not currently implemented");
    }

    
    protected Map<String, Parameter<?>> defineConnectParameterInfo() {
    	return Collections.emptyMap();
    }

    
    protected Map<String, Parameter<?>> defineCreateParameterInfo() {
    	return Collections.emptyMap();
    }

    
    protected Map<String, Parameter<?>> defineDeleteParameterInfo() {
    	return Collections.emptyMap();
    }

    
    protected CoverageAccess delete(Map<String, Serializable> params, Hints hints,
    		ProgressListener listener) throws IOException {
    	throw new UnsupportedOperationException("Operation not currently implemented");
    }

    public EnumSet<DriverOperation> getDriverCapabilities() {
    	return EnumSet.noneOf(DriverOperation.class);
    }

    public boolean isAvailable() {
    	return false;
    }

}
