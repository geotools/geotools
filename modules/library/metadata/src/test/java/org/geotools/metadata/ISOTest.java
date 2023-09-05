/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.geotools.api.metadata.citation.CitationFactory;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.metadata.extent.VerticalExtent;
import org.geotools.api.util.CodeList;
import org.geotools.util.CheckedCollection;
import org.geotools.util.Classes;
import org.junit.Test;

/**
 * Tests every implementation in the {@link org.geotools.metadata.iso} package.
 *
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public final class ISOTest {

    /** Root package for interfaces, with trailing dot. */
    private static final String INTERFACE_PACKAGE = "org.geotools.api.metadata.";

    /** Root package for implementations, with trailing dot. */
    private static final String IMPLEMENTATION_PACKAGE = "org.geotools.metadata.iso.";

    /** Suffix for implementation classes. */
    private static final String IMPLEMENTATION_SUFFIX = "Impl";

    /**
     * List of GeoAPI interfaces to test. This list is not exclusive, since this test suite will
     * automatically scans for dependencies even if an interface do not appears in this list. This
     * list should not contains any {@link CodeList}.
     */
    private static final Class<?>[] TEST = {
        org.geotools.api.metadata.Identifier.class,
        org.geotools.api.metadata.citation.Address.class,
        org.geotools.api.metadata.citation.Citation.class,
        org.geotools.api.metadata.citation.CitationFactory.class,
        org.geotools.api.metadata.citation.Contact.class,
        org.geotools.api.metadata.citation.OnLineResource.class,
        org.geotools.api.metadata.citation.ResponsibleParty.class,
        org.geotools.api.metadata.citation.Telephone.class,
        org.geotools.api.metadata.extent.Extent.class,
        org.geotools.api.metadata.extent.GeographicBoundingBox.class,
        org.geotools.api.metadata.extent.GeographicExtent.class,
        org.geotools.api.metadata.extent.TemporalExtent.class,
        org.geotools.api.metadata.extent.VerticalExtent.class,
        org.geotools.api.metadata.quality.AbsoluteExternalPositionalAccuracy.class,
        org.geotools.api.metadata.quality.ConformanceResult.class,
        org.geotools.api.metadata.quality.Element.class,
        org.geotools.api.metadata.quality.PositionalAccuracy.class,
        org.geotools.api.metadata.quality.QuantitativeResult.class,
        org.geotools.api.metadata.quality.Result.class
    };

    /** GeoAPI interfaces that are know to be unimplemented at this stage. */
    private static final Class<?>[] UNIMPLEMENTED = {
        CitationFactory.class, // SHOULD THIS INTERFACE REALLY EXISTS IN GEOAPI?
        VerticalExtent.class, // Inconsistent 'verticalCRS' type in GeoAPI interface.
        OnLineResource.class // No 'setProtocol' method.
    };

    /** Ensures that the {@link #TEST} array do not contains code list. */
    @Test
    public void testNoCodeList() {
        for (final Class type : TEST) {
            assertFalse(type.getName(), CodeList.class.isAssignableFrom(type));
        }
    }

    /** Tests all dependencies. */
    @Test
    public void testDependencies() {
        assertNull(getImplementation(Number.class));
        final Set<Class<?>> done = new HashSet<>();
        for (final Class<?> type : TEST) {
            final Class<?> impl = getImplementation(type);
            if (impl == null) {
                if (isImplemented(type)) {
                    fail(type.getName() + " is not implemented.");
                }
                continue;
            }
            assertSetters(new PropertyAccessor(impl, type), done);
        }
    }

    /**
     * Recursively ensures that the specified metadata implementation has setters for every methods.
     */
    private static void assertSetters(final PropertyAccessor accessor, final Set<Class<?>> done) {
        if (done.add(accessor.type)) {
            /*
             * Tries to instantiate the implementation. Every implementation should have a
             * no-args constructor, and their instantiation should never fail. Note that
             * this dummy will also be of some help later in this test.
             */
            final Object dummyInstance;
            final boolean isImplemented = isImplemented(accessor.type);
            if (isImplemented)
                try {
                    dummyInstance =
                            accessor.implementation
                                    .getConstructor((Class[]) null)
                                    .newInstance((Object[]) null);
                } catch (Exception e) {
                    fail(e.toString());
                    return;
                }
            else {
                dummyInstance = null;
            }
            /*
             * Iterates over all properties defined in the interface,
             * and checks for the existences of a setter method.
             */
            final String classname = Classes.getShortName(accessor.type) + '.';
            final int count = accessor.count();
            for (int i = 0; i < count; i++) {
                final String name = accessor.name(i);
                assertNotNull(String.valueOf(i), name);
                final String fullname = classname + name;
                assertEquals(fullname, i, accessor.indexOf(name));
                if (!isImplemented) {
                    continue;
                }
                // We can not continue below this point for
                // implementations that are only partial.
                assertTrue(fullname, accessor.isWritable(i));
                /*
                 * Get the property type. In the special case where the property type
                 * is a collection, get an empty collection from the implementation.
                 * This is needed in order to get the element type in the collection.
                 */
                Class<?> type = accessor.type(i);
                if (Collection.class.isAssignableFrom(type)) {
                    final Object example = accessor.get(i, dummyInstance);
                    if (example instanceof CheckedCollection) {
                        type = ((CheckedCollection) example).getElementType();
                    }
                }
                final Class<?> impl = getImplementation(type);
                if (impl != null) {
                    assertSetters(new PropertyAccessor(impl, type), done);
                }
            }
        }
    }

    /**
     * Returns the implementation class for the specified interface class, or {@code null} if none.
     */
    private static Class<?> getImplementation(final Class<?> type) {
        if (!CodeList.class.isAssignableFrom(type)) {
            String name = type.getName();
            if (name.startsWith(INTERFACE_PACKAGE)) {
                name =
                        IMPLEMENTATION_PACKAGE
                                + name.substring(INTERFACE_PACKAGE.length())
                                + IMPLEMENTATION_SUFFIX;
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    /*
                     * Found a class which is not implemented. Before to report an error,
                     * check if it is part of the list of known unimplemented interfaces.
                     */
                    if (isImplemented(type)) {
                        fail(e.toString());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified type is not in the list of known unimplemented types.
     */
    private static boolean isImplemented(final Class<?> type) {
        for (Class<?> aClass : UNIMPLEMENTED) {
            if (type.equals(aClass)) {
                return false;
            }
        }
        return true;
    }
}
