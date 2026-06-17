/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.Properties;
import java.util.Set;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.AbstractFactory;
import org.jspecify.annotations.NonNull;
import org.junit.Before;
import org.junit.Test;

/**
 * This class defines a basic test suite for {@link PropertyCoordinateOperationFactory}.
 *
 * @author skalesse
 * @since 2026-06-17
 */
public class PropertyCoordinateOperationFactoryTest {

    private static final String RESOURCE = "epsg_operations.properties";
    private PropertyCoordinateOperationFactory factory;

    Position2D expectedPoint_EPSG4326 = new Position2D(2.0, 47.0);
    Position2D expectedPoint_EPSG1000001 = new Position2D(-651.6472, -4636.7108);

    @Before
    public void setUp() {
        factory = new TestPropertyCoordinateOperationFactory(RESOURCE);
    }

    /**
     * A basic test for forward lookup, i.e. the mapping for the requested operation is directly defined in
     * epsg_operations.
     */
    @Test
    public void testForwardLookup() {

        CoordinateReferenceSystem sourceCRS;
        CoordinateReferenceSystem targetCRS;
        try {
            sourceCRS = DefaultGeographicCRS.WGS84;
            targetCRS = CRS.decode("EPSG:1000001");
        } catch (FactoryException e) {
            // this should throw an exception
            throw new RuntimeException(e);
        }
        // core tests
        ///////////////
        Set<CoordinateOperation> coordinateOperations = factory.findFromDatabase(sourceCRS, targetCRS, 1);
        assertThat(
                "Set of coordinate operations should not be empty",
                coordinateOperations,
                not(emptyCollectionOf(CoordinateOperation.class)));
        assertThat("Exactly one operation should be found", coordinateOperations, hasSize(1));

        // CRS tests
        ////////////////
        CoordinateOperation coordinateOperation =
                coordinateOperations.iterator().next();
        assertThat(
                "The operation should have the correct source CRS set",
                coordinateOperation.getSourceCRS(),
                is(sourceCRS));
        assertThat(
                "The operation should have the correct target CRS set",
                coordinateOperation.getTargetCRS(),
                is(targetCRS));

        // math transform tests
        ///////////////////////
        MathTransform mathTransform = coordinateOperation.getMathTransform();
        assertThat(
                "The transformation should not be an inverse transformation",
                mathTransform.toWKT(),
                not(containsString("INVERSE_MT")));

        // transformation tests
        ///////////////////////
        try {
            Position2D dstPoint_EPSG1000001 = new Position2D();
            mathTransform.transform(expectedPoint_EPSG4326, dstPoint_EPSG1000001);
            assertPositions(expectedPoint_EPSG1000001, dstPoint_EPSG1000001);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A basic test for backward lookup, i.e. the mapping for the requested operation is not defined in epsg_operations,
     * but its inverse is defined.
     */
    @Test
    public void testBackwardLookup() {
        CoordinateReferenceSystem sourceCRS;
        CoordinateReferenceSystem targetCRS;
        try {
            sourceCRS = CRS.decode("EPSG:1000001");
            targetCRS = DefaultGeographicCRS.WGS84;
        } catch (FactoryException e) {
            // this should throw an exception
            throw new RuntimeException(e);
        }

        // core tests
        ///////////////
        Set<CoordinateOperation> coordinateOperations = factory.findFromDatabase(sourceCRS, targetCRS, 1);
        assertThat(
                "Set of coordinate operations should not be empty",
                coordinateOperations,
                not(emptyCollectionOf(CoordinateOperation.class)));
        assertThat("Exactly one operation should be found", coordinateOperations, hasSize(1));

        // CRS tests
        ////////////////
        CoordinateOperation coordinateOperation =
                coordinateOperations.iterator().next();
        assertThat(
                "The operation should have the correct source CRS set",
                coordinateOperation.getSourceCRS(),
                is(sourceCRS));
        assertThat(
                "The operation should have the correct target CRS set",
                coordinateOperation.getTargetCRS(),
                is(targetCRS));

        // math transform tests
        ////////////////
        MathTransform mathTransform = coordinateOperation.getMathTransform();
        String mathTransformWKT = mathTransform.toWKT();
        assertThat(
                "The transformation should not be an inverse transformation",
                mathTransformWKT,
                containsString("INVERSE_MT"));

        Position2D dstPoint_EPSG_4326 = new Position2D();
        try {
            mathTransform.transform(expectedPoint_EPSG1000001, dstPoint_EPSG_4326);
            assertPositions(expectedPoint_EPSG4326, dstPoint_EPSG_4326);
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The ultimate test that creating the inverse operation from an indirect mapping is really returning the inverse
     * transform. This test verifies the fix for <a
     * href="https://osgeo-org.atlassian.net/browse/GEOT-7917">GEOT-7917</a>
     *
     * @throws NoninvertibleTransformException if the backward transform is not invertible
     */
    @Test
    public void test_inverseOperation() throws NoninvertibleTransformException {
        CoordinateReferenceSystem sourceCRS;
        CoordinateReferenceSystem targetCRS;
        try {
            sourceCRS = DefaultGeographicCRS.WGS84;
            targetCRS = CRS.decode("EPSG:1000001");
        } catch (FactoryException e) {
            // this should throw an exception
            throw new RuntimeException(e);
        }

        // core tests
        ///////////////
        CoordinateOperation forwardOperation =
                factory.findFromDatabase(sourceCRS, targetCRS, 1).iterator().next();
        MathTransform forwardTransform = forwardOperation.getMathTransform();
        CoordinateOperation backwardOperation =
                factory.findFromDatabase(targetCRS, sourceCRS, 1).iterator().next();
        MathTransform backwardTransform = backwardOperation.getMathTransform();

        assertThat(
                "Forward transform should be inverse of backward transform",
                forwardTransform.equals(backwardTransform.inverse()),
                is(true));
    }

    /**
     * assert two positions by a delta of '1e-03'.
     *
     * @param position1 position one
     * @param position2 position one
     */
    private void assertPositions(Position2D position1, Position2D position2) {
        assertEquals(position1.x, position2.x, 1e-3);
        assertEquals(position1.y, position2.y, 1e-3);
    }

    /**
     * An implementation of the {@link PropertyCoordinateOperationFactory} that implements {@link #getDefinitionsURL()}
     * for loading the 'epsg_operations.properties' from the system resources passed into the constructor.
     */
    public static class TestPropertyCoordinateOperationFactory extends PropertyCoordinateOperationFactory {

        private final String resource;
        private Properties processedDefinitions;

        public TestPropertyCoordinateOperationFactory(String resource) {
            super(null, AbstractFactory.MAXIMUM_PRIORITY);
            this.resource = resource;
        }

        /**
         * This method overrides the super() method, so to imitate org.vfny.geoserver.crs.GeoserverWKTOperationFactory's
         * behavior of qualifying the operations with an 'EPSG:' entry if not already present.
         *
         * @return a 'qualified' version of the definitions.
         */
        @Override
        protected Properties getDefinitions() {
            if (processedDefinitions == null) {
                Properties definitions = super.getDefinitions();
                if (definitions == null) return null;

                // post-process the definitions for backwards compatibility,
                // if the key contains just numbers assume it's an EPSG code
                Properties processed = new Properties();
                for (String key : definitions.stringPropertyNames()) {
                    String[] split = key.split("\\s*,\\s*");
                    if (split.length == 2) {
                        String source = split[0];
                        String target = split[1];
                        source = qualifyCode(source);
                        target = qualifyCode(target);
                        String newKey = source + "," + target;
                        processed.put(newKey, definitions.getProperty(key));
                    } else {
                        processed.put(key, definitions.getProperty(key));
                    }
                }
                this.processedDefinitions = processed;
            }
            return processedDefinitions;
        }

        private static @NonNull String qualifyCode(String code) {
            if (!code.contains(":")) {
                code = "EPSG:" + code;
            }
            return code;
        }

        @Override
        protected URL getDefinitionsURL() {
            return getClass().getResource(resource);
        }
    }
}
