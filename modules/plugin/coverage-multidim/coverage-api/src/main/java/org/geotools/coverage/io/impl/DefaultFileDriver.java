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
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.FileDriver;
import org.geotools.data.Parameter;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.util.ProgressListener;

/**
 * Base class extending {@link DefaultDriver} leveraging on URLs.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class DefaultFileDriver extends DefaultDriver implements FileDriver {

    private static final Logger LOGGER = Logging.getLogger(DefaultFileDriver.class);

    /**
     * Parameter "url" used to indicate to a local file or remote resource being accessed as a
     * coverage.
     */
    public static final Parameter<URL> URL =
            new Parameter<URL>(
                    "url",
                    java.net.URL.class,
                    new SimpleInternationalString("URL"),
                    new SimpleInternationalString("Url to a local file or remote location"));

    private final List<String> fileExtensions;

    protected DefaultFileDriver(
            final String name,
            final String description,
            final String title,
            final Hints implementationHints,
            final List<String> fileExtensions,
            final EnumSet<DriverCapabilities> driverCapabilities) {
        super(name, description, title, driverCapabilities, implementationHints);

        Utilities.ensureNonNull("fileExtensions", fileExtensions);
        this.fileExtensions = new ArrayList<String>(fileExtensions);
    }

    public List<String> getFileExtensions() {
        return new ArrayList<String>(fileExtensions);
    }

    @Override
    protected boolean canConnect(Map<String, Serializable> params) {

        // check for URL
        if (!params.containsKey(URL.key)) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(
                        Level.INFO,
                        "Unable to find parameter URL in parameters " + params.toString());
            return false;
        }

        // get the URL
        final URL url = (URL) params.get(URL.key);
        return canConnect(url, params);
    }

    @Override
    protected boolean canCreate(Map<String, Serializable> params) {
        // check for URL
        if (!params.containsKey(URL.key)) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(
                        Level.INFO,
                        "Unable to find parameter URL in parameters " + params.toString());
            return false;
        }
        // get the URL
        final URL url = (URL) params.get(URL.key);
        return canCreate(url, params);
    }

    @Override
    protected boolean canDelete(Map<String, Serializable> params) {
        // check for URL
        if (!params.containsKey(URL.key)) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(
                        Level.INFO,
                        "Unable to find parameter URL in parameters " + params.toString());
            return false;
        }

        // get the URL
        final URL url = (URL) params.get(URL.key);
        return canDelete(url, params);
    }

    @Override
    protected CoverageAccess connect(
            Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        // check for URL
        if (params == null) throw new IllegalArgumentException("Invalid or no input provided.");
        if (!params.containsKey(URL.key))
            throw new IllegalArgumentException(
                    "Unable to find parameter URL in parameters " + params.toString());

        // get the URL
        final URL url = (URL) params.get(URL.key);
        return connect(url, params, hints, listener);
    }

    @Override
    protected CoverageAccess create(
            Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        // check for URL
        if (params == null) throw new IllegalArgumentException("Invalid or no input provided.");
        if (!params.containsKey(URL.key))
            throw new IllegalArgumentException(
                    "Unable to find parameter URL in parameters " + params.toString());

        // get the URL
        final URL url = (URL) params.get(URL.key);
        return create(url, params, hints, listener);
    }

    @Override
    protected CoverageAccess delete(
            Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        // check for URL
        if (params == null) throw new IllegalArgumentException("Invalid or no input provided.");
        if (!params.containsKey(URL.key))
            throw new IllegalArgumentException(
                    "Unable to find parameter URL in parameters " + params.toString());

        // get the URL
        final URL url = (URL) params.get(URL.key);
        return delete(url, params, hints, listener);
    }

    public boolean canProcess(
            DriverCapabilities operation, URL url, Map<String, Serializable> params) {

        if (!getDriverCapabilities().contains(operation))
            throw new UnsupportedOperationException(
                    "Operation " + operation + " is not supported by this driver");

        // check input URL
        if (url == null) {
            // check for URL
            if (!params.containsKey(URL.key))
                throw new IllegalArgumentException(
                        "Unable to find parameter URL in parameters " + params.toString());

            // get the URL
            url = (URL) params.get(URL.key);
        }

        // check the operation
        switch (operation) {
            case CONNECT:
                return canConnect(url, params);
            case DELETE:
                return canDelete(url, params);
            case CREATE:
                return canCreate(url, params);
            default:
                throw new IllegalArgumentException("Unrecognized operation " + operation);
        }
    }

    public CoverageAccess process(
            DriverCapabilities operation,
            URL url,
            Map<String, Serializable> params,
            Hints hints,
            ProgressListener listener)
            throws IOException {

        if (!getDriverCapabilities().contains(operation)) {
            throw new UnsupportedOperationException(
                    "Operation " + operation + " is not supported by this driver");
        }

        // check input URL
        if (url == null) {
            // check for URL
            if (!params.containsKey(URL.key))
                throw new IllegalArgumentException(
                        "Unable to find parameter URL in parameters " + params.toString());

            // get the URL
            url = (URL) params.get(URL.key);
        }

        // check the operation
        switch (operation) {
            case CONNECT:
                return connect(url, params, hints, listener);
            case DELETE:
                return delete(url, params, hints, listener);
            case CREATE:
                return create(url, params, hints, listener);
            default:
                throw new IllegalArgumentException("Unrecognized operation " + operation);
        }
    }

    protected boolean canConnect(java.net.URL url, Map<String, Serializable> params) {
        return false;
    }

    protected boolean canCreate(java.net.URL url, Map<String, Serializable> params) {
        return false;
    }

    protected boolean canDelete(java.net.URL url, Map<String, Serializable> params) {
        return false;
    }

    protected CoverageAccess connect(
            java.net.URL url,
            Map<String, Serializable> params,
            Hints hints,
            ProgressListener listener)
            throws IOException {
        throw new UnsupportedOperationException("Operation not currently implemented");
    }

    protected CoverageAccess create(
            java.net.URL url,
            Map<String, Serializable> params,
            Hints hints,
            ProgressListener listener)
            throws IOException {
        throw new UnsupportedOperationException("Operation not currently implemented");
    }

    protected CoverageAccess delete(
            java.net.URL url,
            Map<String, Serializable> params,
            Hints hints,
            ProgressListener listener)
            throws IOException {
        throw new UnsupportedOperationException("Operation not currently implemented");
    }

    @Override
    protected Map<String, Parameter<?>> defineConnectParameterInfo() {
        final Map<String, Parameter<?>> params = new HashMap<String, Parameter<?>>();
        params.put(URL.key, URL);
        return params;
    }

    @Override
    protected Map<String, Parameter<?>> defineCreateParameterInfo() {
        final Map<String, Parameter<?>> params = new HashMap<String, Parameter<?>>();
        params.put(URL.key, URL);
        return params;
    }

    @Override
    protected Map<String, Parameter<?>> defineDeleteParameterInfo() {
        final Map<String, Parameter<?>> params = new HashMap<String, Parameter<?>>();
        params.put(URL.key, URL);
        return params;
    }
}
