/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import org.geotools.api.coverage.Coverage;
import org.geotools.api.coverage.grid.GridCoverage;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.coverage.io.CoverageSource.VerticalDomain;
import org.geotools.coverage.io.Driver.DriverCapabilities;
import org.geotools.coverage.io.driver.TestDriver;
import org.geotools.coverage.io.impl.DefaultCoverageSource;
import org.geotools.coverage.io.impl.DefaultFileCoverageAccess;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.coverage.io.metadata.MetadataAttribute;
import org.geotools.coverage.io.metadata.MetadataNode;
import org.geotools.coverage.io.util.DateRangeTreeSet;
import org.geotools.coverage.io.util.DoubleRangeTreeSet;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.Hints;
import org.junit.Test;

/** @author Nicola Lagomarsini Geosolutions */
public class CoverageTest {

    private static final TestDriverNew driver = new TestDriverNew();

    private static final Name TEST_NAME = new NameImpl("New Test Coverage");

    public static final String TEST_URL = "file:/" + TEST_NAME.getLocalPart();

    @Test
    public void testDomains() throws IOException, MismatchedDimensionException, TransformException {
        Map<String, Serializable> connectionParams = new HashMap<>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL(TEST_URL));

        CoverageAccess access =
                driver.access(DriverCapabilities.CONNECT, connectionParams, null, null);
        assertSame(driver, access.getDriver());

        // Checking proper coverage name
        final List<Name> names = access.getNames(null);
        final Name coverageName = names.get(0);
        assertEquals(1, names.size());
        assertEquals(TEST_NAME, coverageName);

        final CoverageSource source =
                access.access(TEST_NAME, null, AccessType.READ_ONLY, null, null);
        assertEquals(TEST_NAME, source.getName(null));

        // Test additional domains Setter and getter
        assertFalse(source.getAdditionalDomains().isEmpty());
        assertNotNull(source.getVerticalDomain());
    }

    @Test
    public void testAttributes()
            throws IOException, MismatchedDimensionException, TransformException {
        Map<String, Serializable> connectionParams = new HashMap<>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL(TEST_URL));

        CoverageAccess access =
                driver.access(DriverCapabilities.CONNECT, connectionParams, null, null);

        final CoverageSource source =
                access.access(TEST_NAME, null, AccessType.READ_ONLY, null, null);

        // Minor checks on the metadata node and attributes
        MetadataNode metadata = source.getMetadata(null, null);
        assertNotNull(metadata);
        Map<String, MetadataAttribute> attributes = metadata.getAttributes();
        assertNotNull(attributes);
        assertFalse(attributes.isEmpty());
        MetadataAttribute metadataAttribute = attributes.get("testAttribute");
        assertNotNull(metadataAttribute);
        assertNotNull(metadata.getNodes());
    }

    @Test
    public void testDomainTypes() {
        assertTrue(DomainType.DATE.toString().equalsIgnoreCase("DATE"));
        assertTrue(DomainType.DATERANGE.toString().equalsIgnoreCase("DATERANGE"));
        assertTrue(DomainType.NUMBER.toString().equalsIgnoreCase("NUMBER"));
        assertTrue(DomainType.NUMBERRANGE.toString().equalsIgnoreCase("NUMBERRANGE"));
        assertTrue(DomainType.STRING.toString().equalsIgnoreCase("STRING"));
    }

    @Test
    public void testRequestAndResponse()
            throws IOException, MismatchedDimensionException, TransformException {
        Map<String, Serializable> connectionParams = new HashMap<>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL(TEST_URL));

        CoverageAccess access =
                driver.access(DriverCapabilities.CONNECT, connectionParams, null, null);

        final CoverageSource source =
                access.access(TEST_NAME, null, AccessType.READ_ONLY, null, null);

        // Checking proper coverage name
        final List<Name> names = access.getNames(null);
        final Name coverageName = names.get(0);

        // Creation of a dummy Request
        CoverageReadRequest request = new CoverageReadRequest();
        // Definition of the parameters
        CoordinateReferenceSystem crs = source.getCoordinateReferenceSystem();
        Rectangle rasterArea = new Rectangle(0, 0, 10, 10);
        Hints hints = new Hints();
        String handle = "test_handle";
        DateRange range = new DateRange(new Date(10000), new Date(20000));
        DateRangeTreeSet set = new DateRangeTreeSet();
        set.add(range);
        Filter filter = Filter.INCLUDE;
        MathTransform2D gridToWorldTransform =
                new AffineTransform2D(AffineTransform.getTranslateInstance(0, 0));
        Set<NumberRange<Double>> verticalSubset = new HashSet<>();
        verticalSubset.add(new NumberRange<>(Double.class, 0.0d, 10000.0d));
        // Setting of the request parameters
        request.setName(coverageName);
        request.setHints(hints);
        request.setHandle(handle);
        request.setDomainSubset(rasterArea, gridToWorldTransform, crs);
        // Ensure that both geographic and raster area have been set
        assertNotNull(request.getRasterArea());
        assertNotNull(request.getGeographicArea());
        // Check that there is no Filter already set
        assertNull(request.getFilter());
        // Setting the filter
        request.setFilter(filter);
        assertNotNull(request.getFilter());
        // Check that there is no temporal subset already set
        assertTrue(request.getTemporalSubset().isEmpty());
        // Setting temporal subset
        request.setTemporalSubset(set);
        assertFalse(request.getTemporalSubset().isEmpty());
        // Check that there is no vertical subset already set
        assertTrue(request.getVerticalSubset().isEmpty());
        // Setting vertical subset
        request.setVerticalSubset(verticalSubset);
        assertFalse(request.getVerticalSubset().isEmpty());
        // Get the response
        CoverageResponse response = source.read(request, null);
        // Ensure the response status is success
        assertSame(response.getStatus(), Status.SUCCESS);
        // Ensure the request is the same
        assertEquals(response.getRequest(), request);
        // Ensure the Handle is the same
        assertTrue(response.getHandle().equalsIgnoreCase(handle));
        // Ensure the result is not null
        Collection<? extends Coverage> results = response.getResults(null);
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.iterator().next() instanceof GridCoverage2D);
    }

    @Test
    public void testUpdateRequestAndResponse()
            throws IOException, MismatchedDimensionException, TransformException {
        Map<String, Serializable> connectionParams = new HashMap<>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL(TEST_URL));

        CoverageAccess access =
                driver.access(DriverCapabilities.CONNECT, connectionParams, null, null);

        final CoverageStore store = access.create(TEST_NAME, null, new Hints(), null);

        // Creation of a dummy Request
        CoverageUpdateRequest request = new CoverageUpdateRequest();

        // Setting of the parameters
        Map<String, String> metadata = new HashMap<>();
        metadata.put("testKey", "testMetadata");
        List<GridCoverage2D> data = new ArrayList<>();
        GridCoverage2D cov =
                new GridCoverageFactory()
                        .create(
                                TEST_NAME.getLocalPart(),
                                new float[][] {{1.0f, 1.0f}},
                                new ReferencedEnvelope(0.0d, 1.0d, 0.0d, 1.0d, null));
        data.add(cov);
        request.setMetadata(metadata);
        request.setData(data);

        // Get the response
        CoverageResponse response = store.update(request, null);

        Collection<? extends Coverage> results = response.getResults(null);
        // Ensure the results are not null and it is not empty
        assertNotNull(results);
        assertFalse(results.isEmpty());
        // Get the first result from the collection and ensure it is the same coverage of the input
        GridCoverage2D covOutput = (GridCoverage2D) results.iterator().next();
        assertSame(cov, covOutput);

        // Ensure they have the same metadata
        CoverageUpdateRequest request2 = (CoverageUpdateRequest) response.getRequest();
        assertEquals(metadata.get("testKey"), request2.getMetadata().get("testKey"));
    }

    static class TestVerticalDomain extends VerticalDomain {

        static Set<NumberRange<Double>> verticalSubset = new HashSet<>();

        static {
            verticalSubset.add(new NumberRange<>(Double.class, 0.0d, 10000.0d));
        }

        @Override
        public SortedSet<? extends NumberRange<Double>> getVerticalElements(
                boolean overall, ProgressListener listener) throws IOException {
            return new DoubleRangeTreeSet(verticalSubset);
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return DefaultGeographicCRS.WGS84;
        }
    }

    static class TestAdditionalDomain extends AdditionalDomain {

        static Set<Object> test = new HashSet<>();

        static {
            test.add("test");
        }

        @Override
        public Set<Object> getElements(boolean overall, ProgressListener listener)
                throws IOException {
            return test;
        }

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public DomainType getType() {
            return DomainType.STRING;
        }
    }

    static class TestCoverageSourceDescriptorNew extends TestCoverageSourceDescriptor {

        public TestCoverageSourceDescriptorNew() {
            super(TEST_NAME.getLocalPart());
            setVerticalDomain(new TestVerticalDomain());
            setHasVerticalDomain(true);
            ArrayList<AdditionalDomain> additionalDomains = new ArrayList<>();
            additionalDomains.add(new TestAdditionalDomain());
            setAdditionalDomains(additionalDomains);
            setHasAdditionalDomains(true);
        }
    }

    public static class TestDriverNew extends DefaultFileDriver implements Driver {

        public TestDriverNew() {
            super(
                    TestDriver.TEST_DRIVER,
                    TestDriver.TEST_DRIVER,
                    TestDriver.TEST_DRIVER,
                    new Hints(),
                    Collections.singletonList(".EXT"),
                    EnumSet.of(
                            DriverCapabilities.CONNECT,
                            DriverCapabilities.CREATE,
                            DriverCapabilities.DELETE));
        }

        private static Map<String, Parameter<?>> emptyMap = Collections.emptyMap();

        @Override
        protected CoverageAccess connect(
                Map<String, Serializable> params, Hints hints, ProgressListener listener)
                throws IOException {
            return new TestCoverageAccessNew(
                    this, EnumSet.of(AccessType.READ_WRITE), emptyMap, params);
        }
    }

    static class TestCoverageAccessNew extends DefaultFileCoverageAccess {

        @Override
        public CoverageSource access(
                Name name,
                Map<String, Serializable> params,
                AccessType accessType,
                Hints hints,
                ProgressListener listener)
                throws IOException {
            return new TestCoverageSourceNew(name, new TestCoverageSourceDescriptorNew());
        }

        @Override
        public CoverageStore create(
                Name name, Map<String, Serializable> params, Hints hints, ProgressListener listener)
                throws IOException {
            return new TestCoverageSourceNew(name, new TestCoverageSourceDescriptorNew());
        }

        public TestCoverageAccessNew(
                Driver driver,
                EnumSet<AccessType> allowedAccessTypes,
                Map<String, Parameter<?>> accessParams,
                Map<String, Serializable> connectionParameters)
                throws DataSourceException {
            super(driver, allowedAccessTypes, accessParams, null, connectionParameters);
            names = new ArrayList<>();
            names.add(TEST_NAME);
        }
    }

    static class TestCoverageSourceNew extends DefaultCoverageSource implements CoverageStore {

        protected TestCoverageSourceNew(Name name, CoverageSourceDescriptor descriptor) {
            super(name, descriptor);
        }

        @Override
        public CoverageResponse read(CoverageReadRequest request, ProgressListener listener)
                throws IOException {
            // creating a simple response
            CoverageResponse response = new CoverageResponse();
            response.setRequest(request);
            response.setHandle(request.getHandle());
            response.setStatus(Status.SUCCESS);
            // Adding results
            response.addResult(
                    new GridCoverageFactory()
                            .create(
                                    TEST_NAME.getLocalPart(),
                                    new float[][] {{1.0f, 1.0f}},
                                    new ReferencedEnvelope(0.0d, 1.0d, 0.0d, 1.0d, null)));

            return response;
        }

        @Override
        public Map<String, Parameter<?>> getUpdateParameterInfo() {
            Map<String, Parameter<?>> parameterInfo = new HashMap<>();
            return parameterInfo;
        }

        @Override
        public CoverageResponse update(
                CoverageUpdateRequest writeRequest, ProgressListener progress) {
            CoverageResponse response = new CoverageResponse();
            response.setRequest(writeRequest);
            @SuppressWarnings("unchecked")
            Collection<GridCoverage> coverages = (Collection<GridCoverage>) writeRequest.getData();
            response.addResults(coverages);
            return response;
        }

        @Override
        public MetadataNode getMetadata(String metadataDomain, ProgressListener listener) {
            MetadataNode metadataNode = new MetadataNode();
            Map<String, MetadataAttribute> attributes = new HashMap<>();
            attributes.put("testAttribute", new MetadataAttribute());
            metadataNode.setAttributes(attributes);
            metadataNode.setNodes(new ArrayList<>());
            return metadataNode;
        }
    }
}
