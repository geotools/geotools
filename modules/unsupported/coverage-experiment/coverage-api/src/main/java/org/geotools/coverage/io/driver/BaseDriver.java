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
import java.util.Map;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Base Implementation for the {@link Driver} interface.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-api/src/main/java/org/geotools/coverage/io/driver/BaseDriver.java $
 */
public abstract class BaseDriver implements Driver {

    private String name;

    private InternationalString description;

    private InternationalString title;

    private Map<Key, ?> implementationHints;

    private Map<String, Parameter<?>> connectParameterInfo;

    private Map<String, Parameter<?>> createParameterInfo;

    private Map<String, Parameter<?>> deleteParameterInfo;

    protected BaseDriver(final String name, final String description, final String title,
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

    protected abstract boolean canConnect(Map<String, Serializable> params);

    /**
     * Subclass can override to support create operations
     * 
     * @return false - subclass can override when create is implemented
     */
    protected abstract boolean canCreate(Map<String, Serializable> params);

    /**
     * Subclass can override to support delete operations.
     * 
     * @return false - subclass can override when delete is implemented
     */
    protected abstract boolean canDelete(Map<String, Serializable> params);

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

    /**
     * Subclass can override to support delete operations.
     * 
     * @return false - subclass can override when delete is implemented
     */
    protected abstract CoverageAccess delete(Map<String, Serializable> params, Hints hints,
            ProgressListener listener) throws IOException;

    protected abstract CoverageAccess connect(Map<String, Serializable> params, Hints hints,
            ProgressListener listener) throws IOException;

    protected abstract CoverageAccess create(Map<String, Serializable> params, Hints hints,
            ProgressListener listener) throws IOException;

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

    protected abstract Map<String, Parameter<?>> defineDeleteParameterInfo();

    /**
     * Called to define the value returned by getConnectionParameterInfo().
     * <p>
     * Subclasses should provide an implementation of this method indicating the parameters they
     * require.
     * </p>
     */
    protected abstract Map<String, Parameter<?>> defineConnectParameterInfo();

    protected synchronized Map<String, Parameter<?>> getCreateParameterInfo() {
        if (createParameterInfo == null) {
            createParameterInfo = defineCreateParameterInfo();
            if (createParameterInfo == null) {
                createParameterInfo = Collections.emptyMap();
            }
        }
        return createParameterInfo;
    }

    /**
     * Define the parameters required for creation.
     * <p>
     * Subclasses should override this method when changing isCreatedSupported to true.
     * </p>
     * 
     * @return The default implementation returns an empty map.
     */
    protected abstract Map<String, Parameter<?>> defineCreateParameterInfo();

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

}
