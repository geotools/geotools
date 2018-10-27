/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.TestCoverageSourceDescriptor;
import org.geotools.coverage.io.impl.DefaultCoverageSource;
import org.geotools.coverage.io.impl.DefaultFileCoverageAccess;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.data.DataSourceException;
import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.util.factory.Hints;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

/**
 * @author Simone Giannecchini, GeoSolutions
 *     <p>A simple TestDriver supporting only connections to a single TEST_URL resource. No support
 *     on delete and create,
 */
public class TestDriver extends DefaultFileDriver implements Driver {

    public static final String TEST_URL = "file:/" + TestCoverageSourceDescriptor.TEST_COVERAGE;

    public static final String TEST_DRIVER = "test driver";

    private static TestDriver testDriver = new TestDriver();

    private static Map<String, Parameter<?>> emptyMap = Collections.emptyMap();

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(TestDriver.class);

    private static final String EXTENSION = ".EXT";

    @Override
    public boolean isAvailable() {
        return true;
    }

    public TestDriver() {
        super(
                TEST_DRIVER,
                TEST_DRIVER,
                TEST_DRIVER,
                new Hints(),
                Collections.singletonList(EXTENSION),
                EnumSet.of(
                        DriverCapabilities.CONNECT,
                        DriverCapabilities.CREATE,
                        DriverCapabilities.DELETE));
    }

    @Override
    protected boolean canConnect(Map<String, Serializable> params) {
        return params != null
                && params.containsKey(DefaultFileDriver.URL.key)
                && ((URL) params.get(DefaultFileDriver.URL.key))
                        .getPath()
                        .contains(TestCoverageSourceDescriptor.TEST_COVERAGE);
    }

    @Override
    protected boolean canDelete(Map<String, Serializable> params) {
        return false;
    }

    @Override
    protected boolean canCreate(Map<String, Serializable> params) {
        return false;
    }

    @Override
    protected CoverageAccess connect(
            Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        return new TestCoverageAccess(this, EnumSet.of(AccessType.READ_ONLY), emptyMap, params);
    }

    @Override
    protected CoverageAccess create(
            Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        return new TestCoverageAccess(this, EnumSet.of(AccessType.READ_WRITE), emptyMap, params);
    }

    @Override
    protected CoverageAccess delete(
            Map<String, Serializable> params, Hints hints, ProgressListener listener)
            throws IOException {
        // TODO Auto-generated method stub
        return super.delete(params, hints, listener);
    }

    static class TestCoverageSource extends DefaultCoverageSource implements CoverageSource {

        protected TestCoverageSource(Name name, CoverageSourceDescriptor descriptor) {
            super(name, descriptor);
        }

        @Override
        public CoverageResponse read(CoverageReadRequest request, ProgressListener listener)
                throws IOException {
            return null;
        }
    }

    static class TestCoverageAccess extends DefaultFileCoverageAccess implements CoverageAccess {

        @Override
        public CoverageSource access(
                Name name,
                Map<String, Serializable> params,
                AccessType accessType,
                Hints hints,
                ProgressListener listener)
                throws IOException {
            return new TestCoverageSource(
                    name,
                    new TestCoverageSourceDescriptor(TestCoverageSourceDescriptor.TEST_COVERAGE));
        }

        @Override
        public boolean canCreate(
                Name name, Map<String, Serializable> params, Hints hints, ProgressListener listener)
                throws IOException {
            return super.canCreate(name, params, hints, listener);
        }

        @Override
        public boolean canDelete(Name name, Map<String, Serializable> params, Hints hints)
                throws IOException {
            return false;
        }

        @Override
        public CoverageStore create(
                Name name, Map<String, Serializable> params, Hints hints, ProgressListener listener)
                throws IOException {
            return super.create(name, params, hints, listener);
        }

        @Override
        public boolean delete(Name name, Map<String, Serializable> params, Hints hints)
                throws IOException {
            throw new UnsupportedOperationException();
        }

        public TestCoverageAccess(
                Driver driver,
                EnumSet<AccessType> allowedAccessTypes,
                Map<String, Parameter<?>> accessParams,
                Map<String, Serializable> connectionParameters)
                throws DataSourceException {
            super(driver, allowedAccessTypes, accessParams, null, connectionParameters);
            names = new ArrayList<Name>();
            names.add(new NameImpl(TestCoverageSourceDescriptor.TEST_COVERAGE));
        }
    }
}
