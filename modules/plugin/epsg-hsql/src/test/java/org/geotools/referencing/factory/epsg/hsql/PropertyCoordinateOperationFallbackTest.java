/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.AbstractCoordinateOperation;
import org.geotools.referencing.operation.PropertyCoordinateOperationFactory;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Checks the operation resolution order of {@link PropertyCoordinateOperationFactory}: the property file first, then
 * the EPSG predefined operations, and only then the dynamic synthesis inherited from the default factory.
 *
 * <p>Before the EPSG step existed, a CRS pair missing from the property file lost its EPSG operation, because this
 * factory replaces the default one instead of augmenting the EPSG authority. Coverage reprojection then silently used a
 * synthesized datum shift, instead of the EPSG database defined one.
 *
 * <p>The CRS are decoded in authority axis order: the property file is keyed by CRS identifier, and forcing longitude
 * first drops the identifiers, so no definition would ever match.
 */
public class PropertyCoordinateOperationFallbackTest {

    /** Defined in the property file, so the definition there must win over EPSG:15929. */
    private static final String DEFINED_SOURCE = "EPSG:4313";

    /** Absent from the property file, and EPSG has a predefined operation for it. */
    private static final String UNDEFINED_SOURCE = "EPSG:4312";

    private static final String MGI_TO_WGS84 = "EPSG:1194";

    private static FactoryIteratorProvider factoryProvider;

    private CoordinateOperationFactory factory;

    /** Registers the factory the way GeoServer does, at maximum priority in the operation factory SPI. */
    @BeforeClass
    public static void registerFactory() {
        factoryProvider = new FactoryIteratorProvider() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> Iterator<T> iterator(Class<T> category) {
                if (CoordinateOperationFactory.class == category) {
                    return List.of((T) new TestFactory()).iterator();
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(factoryProvider);
        CRS.reset("all");
    }

    @AfterClass
    public static void deregisterFactory() {
        GeoTools.removeFactoryIteratorProvider(factoryProvider);
        CRS.reset("all");
    }

    @Before
    public void lookupFactory() {
        factory = ReferencingFactoryFinder.getCoordinateOperationFactory(null);
    }

    /**
     * The precondition that makes the rest matter: at maximum priority this factory replaces the default one for every
     * caller resolving a single operation factory -> a property file miss must not lose the EPSG operations.
     */
    @Test
    public void testReplacesTheDefaultOperationFactory() {
        assertThat(factory, instanceOf(TestFactory.class));
    }

    @Test
    public void testPropertyDefinitionWins() throws Exception {
        CoordinateReferenceSystem source = CRS.decode(DEFINED_SOURCE);
        CoordinateOperation operation = toWGS84(source);

        assertNull("should not come from the EPSG authority", identifier(operation));
        Position shifted = operation.getMathTransform().transform(new Position2D(source, 50.5, 4.45), null);
        assertEquals("latitude", 50.5, shifted.getOrdinate(0), 1e-9);
        assertEquals("longitude, rotated by the definition", 5.45, shifted.getOrdinate(1), 1e-9);
    }

    /** The whole point of the fallback: do not synthesize a datum shift for what EPSG already defines. */
    @Test
    public void testUndefinedPairUsesAuthorityOperation() throws Exception {
        CoordinateOperation operation = toWGS84(CRS.decode(UNDEFINED_SOURCE));

        assertEquals(MGI_TO_WGS84, identifier(operation));
        assertEquals(0.5, AbstractCoordinateOperation.getAccuracy(operation), 0);
    }

    /** Resolves the operation the way a caller does, rather than through the property file hook. */
    private CoordinateOperation toWGS84(CoordinateReferenceSystem source) throws Exception {
        return factory.createOperation(source, CRS.decode("EPSG:4326"));
    }

    /** The single EPSG identifier of the operation, or {@code null} for a locally defined one. */
    private static String identifier(CoordinateOperation operation) {
        Set<ReferenceIdentifier> identifiers = operation.getIdentifiers();
        return identifiers.isEmpty() ? null : identifiers.iterator().next().toString();
    }

    private static class TestFactory extends PropertyCoordinateOperationFactory {

        TestFactory() {
            super(null, MAXIMUM_PRIORITY);
        }

        @Override
        protected URL getDefinitionsURL() {
            return TestFactory.class.getResource("epsg_operations.properties");
        }
    }
}
