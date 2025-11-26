/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.operation.cross;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.GeocentricTranslation;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CrossAuthorityTest {

    public static final double EPS = 1e-6;
    private static FactoryIteratorProvider FACTORIES_PROVIDER;

    /** Programmatically register a factory provider for the test cases. */
    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setUpClass() {
        FACTORIES_PROVIDER = new FactoryIteratorProvider() {

            @Override
            public <T> Iterator<T> iterator(Class<T> category) {

                if (CoordinateOperationFactory.class == category) {
                    // two factories to check that fallback is working
                    return List.of((T) new TestCoordinateOperationFactory1(), (T) new TestCoordinateOperationFactory2())
                            .iterator();
                } else if (CRSAuthorityFactory.class == category) {
                    return List.of((T) new TestAuthorityFactory()).iterator();
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(FACTORIES_PROVIDER);
        CRS.reset("all");
    }

    @AfterClass
    public static void tearDownClass() {
        GeoTools.removeFactoryIteratorProvider(FACTORIES_PROVIDER);
        CRS.reset("all");
    }

    @Test
    public void testAuthorities() {
        Set<String> authorities = CRS.getSupportedAuthorities(false);
        assertTrue("Expected TEST authority to be registered", authorities.contains("TEST"));
        assertTrue("Expected TEST authority to be registered", authorities.contains("EPSG"));
    }

    @Test
    public void testCRSs() throws Exception {
        testTestIdentifier("TEST:1234");
        testTestIdentifier("TEST:1235");
        testTestIdentifier("TEST:1236");
    }

    /**
     * Tests that the given code can be decoded and that the identifier can be looked up again.
     *
     * @param code
     * @throws FactoryException
     */
    private static void testTestIdentifier(String code) throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode(code);
        assertNotNull(crs);
        assertEquals(code, CRS.lookupIdentifier(crs, false));
    }

    /** Tests a custom operation found in {@link TestCoordinateOperationFactory1} */
    @Test
    public void testCrossTransformation1() throws Exception {
        CoordinateReferenceSystem sourceCRS = CRS.decode("TEST:1234");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");

        CoordinateOperation forward = CRS.getCoordinateOperationFactory(true).createOperation(sourceCRS, targetCRS);
        checkConcatenatedGeocentricTranslation(forward, 1, 1, 2, 3);

        CoordinateOperation backwards = CRS.getCoordinateOperationFactory(true).createOperation(targetCRS, sourceCRS);
        checkConcatenatedGeocentricTranslation(backwards, 1, -1, -2, -3);
    }

    /** Tests a custom operation found in {@link TestCoordinateOperationFactory2} */
    @Test
    public void testCrossTransformation2() throws Exception {
        CoordinateReferenceSystem sourceCRS = CRS.decode("TEST:1235");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");

        CoordinateOperation forward = CRS.getCoordinateOperationFactory(true).createOperation(sourceCRS, targetCRS);
        checkConcatenatedGeocentricTranslation(forward, 1, 4, 5, 6);

        CoordinateOperation backwards = CRS.getCoordinateOperationFactory(true).createOperation(targetCRS, sourceCRS);
        checkConcatenatedGeocentricTranslation(backwards, 1, -4, -5, -6);
    }

    /**
     * Tests a custom operation found in {@link TestCoordinateOperationFactory1} is used when looking for a
     * transformation involving it as a step in a larger transformation.
     */
    @Test
    public void testNestedTransformation() throws Exception {
        CoordinateReferenceSystem sourceCRS = CRS.decode("TEST:1236");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");

        CoordinateOperation forward = CRS.getCoordinateOperationFactory(true).createOperation(sourceCRS, targetCRS);
        checkConcatenatedGeocentricTranslation(forward, 2, 1, 2, 3);

        CoordinateOperation backwards = CRS.getCoordinateOperationFactory(true).createOperation(targetCRS, sourceCRS);
        checkConcatenatedGeocentricTranslation(backwards, 1, -1, -2, -3);
    }

    private void checkConcatenatedGeocentricTranslation(
            CoordinateOperation operation, int translationIndex, int tx, int ty, int tz) {
        ConcatenatedTransform transform = (ConcatenatedTransform) operation.getMathTransform();
        List<MathTransform> transforms = unpackTransform(transform);
        assertThat(transforms.size(), Matchers.greaterThanOrEqualTo(translationIndex - 1));
        assertThat(transforms.get(translationIndex), instanceOf(GeocentricTranslation.class));
        GeocentricTranslation gt = (GeocentricTranslation) transforms.get(translationIndex);
        Matrix matrix = gt.getMatrix();
        assertEquals(tx, matrix.getElement(0, 3), EPS);
        assertEquals(ty, matrix.getElement(1, 3), EPS);
        assertEquals(tz, matrix.getElement(2, 3), EPS);
    }

    List<MathTransform> unpackTransform(ConcatenatedTransform concat) {
        return unpackTransform(concat, new ArrayList<>());
    }

    List<MathTransform> unpackTransform(ConcatenatedTransform concat, List<MathTransform> transforms) {
        if (concat.transform1 instanceof ConcatenatedTransform) {
            unpackTransform((ConcatenatedTransform) concat.transform1, transforms);
        } else {
            transforms.add(concat.transform1);
        }
        if (concat.transform2 instanceof ConcatenatedTransform) {
            unpackTransform((ConcatenatedTransform) concat.transform2, transforms);
        } else {
            transforms.add(concat.transform2);
        }
        return transforms;
    }
}
