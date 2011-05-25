/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.factory;

import java.util.*;
import org.geotools.resources.LazySet;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link FactoryRegistry} implementation.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class FactoryRegistryTest {
    /**
     * Ensures that class {@link Hints} is loaded before {@link DummyFactory}.
     * It is not needed for normal execution, but Maven seems to mess with class loaders.
     */
    @Before
    public void ensureHintsLoaded() {
        assertNotNull(Hints.DATUM_FACTORY.toString());
    }

    /**
     * Creates the factory registry to test. The tests performed in this method are more
     * J2SE tests than Geotools implementation tests. We basically just ensure that we
     * have setup the service registry properly.
     * <p>
     * Factories are specified in arguments as {@link Factory} objects in order to avoid
     * the {@link DummyClass} to be initialized before {@link Hints}. This is not a problem
     * for normal execution, but Maven seems to mess with class loaders.
     *
     * @param creator {@code true} if the registry should be an instance of {@link FactoryCreator}.
     */
    private FactoryRegistry getRegistry(final boolean creator,
                                        final Factory factory1,
                                        final Factory factory2,
                                        final Factory factory3)
    {
        @SuppressWarnings("unchecked")
        final Set<Class<?>> categories = (Set) Collections.singleton(DummyFactory.class);
        // The above line fails without the cast, I don't know why...
        final FactoryRegistry registry;
        if (creator) {
            registry = new FactoryCreator(categories);
        } else {
            registry = new FactoryRegistry(categories);
        }
        registry.registerServiceProvider(factory1);
        registry.registerServiceProvider(factory2);
        registry.registerServiceProvider(factory3);
        assertTrue(registry.setOrdering(DummyFactory.class, (DummyFactory)factory1, (DummyFactory)factory2));
        assertTrue(registry.setOrdering(DummyFactory.class, (DummyFactory)factory2, (DummyFactory)factory3));
        assertTrue(registry.setOrdering(DummyFactory.class, (DummyFactory)factory1, (DummyFactory)factory3));

        final List<?> factories = new ArrayList<Object>(new LazySet<Object>(
                registry.getServiceProviders(DummyFactory.class, null, null)));
        assertTrue(factories.contains(factory1));
        assertTrue(factories.contains(factory2));
        assertTrue(factories.contains(factory3));
        assertTrue(factories.indexOf(factory1) < factories.indexOf(factory2));
        assertTrue(factories.indexOf(factory2) < factories.indexOf(factory3));
        return registry;
    }

    /**
     * Tests the {@link FactoryRegistry#getProvider} method.
     * Note that the tested method do not create any new factory.
     * If no registered factory matching the hints is found, an exception is expected.
     * <br><br>
     * Three factories are initially registered: factory #1, #2 and #3.
     *
     * Factory #1 has no dependency.
     * Factory #2 uses factory #1.
     * Factory #3 uses factory #2, which implies an indirect dependency to factory #1.
     *
     * Additionnaly, factory #1 uses a KEY_INTERPOLATION hint.
     */
    @Test
    public void testGetProvider() {
        final Hints.Key    key      = DummyFactory.DUMMY_FACTORY;
        final DummyFactory factory1 = new DummyFactory.Example1();
        final DummyFactory factory2 = new DummyFactory.Example2();
        final DummyFactory factory3 = new DummyFactory.Example3();
        final FactoryRegistry registry = getRegistry(false, factory1, factory2, factory3);
        Hints hints;
        DummyFactory factory;
        // ------------------------------------------------
        //     PART 1: SIMPLE HINT (not a Factory hint)
        // ------------------------------------------------
        /*
         * No hints. The fist factory should be selected.
         */
        hints   = null;
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("No preferences; should select the first factory. ", factory1, factory);
        /*
         * A hint compatible with one of our factories. Factory #1 declares explicitly that it uses
         * a bilinear interpolation, which is compatible with user's hints. All other factories are
         * indifferent. Since factory #1 is the first one in the list, it should be selected.
         */
        hints   = new Hints(Hints.KEY_INTERPOLATION, Hints.VALUE_INTERPOLATION_BILINEAR);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("First factory matches; it should be selected. ", factory1, factory);
        /*
         * A hint incompatible with all our factories. Factory #1 is the only one to defines
         * explicitly a KEY_INTERPOLATION hint, but all other factories depend on factory #1
         * either directly (factory #2) or indirectly (factory #3, which depends on #2).
         */
        hints = new Hints(Hints.KEY_INTERPOLATION, Hints.VALUE_INTERPOLATION_BICUBIC);
        try {
            factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
            fail("Found factory "+factory+", while the hint should have been rejected.");
        } catch (FactoryNotFoundException exception) {
            // This is the expected exception. Continue...
        }
        /*
         * Add a new factory implementation, and try again with exactly the same hints
         * than the previous test. This time, the new factory should be selected since
         * this one doesn't have any dependency toward factory #1.
         */
        final DummyFactory factory4 = new DummyFactory.Example4();
        registry.registerServiceProvider(factory4);
        assertTrue(registry.setOrdering(DummyFactory.class, factory1, factory4));
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("The new factory should be selected. ", factory4, factory);

        // ----------------------------
        //     PART 2: FACTORY HINT
        // ----------------------------
        /*
         * Trivial case: user gives explicitly a factory instance.
         */
        DummyFactory explicit = new DummyFactory.Example3();
        hints   = new Hints(DummyFactory.DUMMY_FACTORY, explicit);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("The user-specified factory should have been selected. ", explicit, factory);
        /*
         * User specifies the expected implementation class rather than an instance.
         */
        hints   = new Hints(DummyFactory.DUMMY_FACTORY, DummyFactory.Example2.class);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("Factory of class #2 were requested. ", factory2, factory);
        /*
         * Same as above, but with classes specified in an array.
         */
        hints = new Hints(DummyFactory.DUMMY_FACTORY, new Class<?>[] {
            DummyFactory.Example3.class,
            DummyFactory.Example2.class
        });
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("Factory of class #3 were requested. ", factory3, factory);
        /*
         * The following hint should be ignored by factory #1, since this factory doesn't have
         * any dependency to the INTERNAL_FACTORY hint. Since factory #1 is first in the ordering,
         * it should be selected.
         */
        hints   = new Hints(DummyFactory.INTERNAL_FACTORY, DummyFactory.Example2.class);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("Expected factory #1. ", factory1, factory);
        /*
         * If the user really wants some factory that do have a dependency to factory #2, he should
         * specifies in a DUMMY_FACTORY hint the implementation classes (or a common super-class or
         * interface) that do care about the INTERNAL_FACTORY hint. Note that this extra step should
         * not be a big deal in most real application, because:
         *
         *  1) Either all implementations have this dependency (for example it would be
         *     unusual to see a DatumAuthorityFactory without a DatumFactory dependency);
         *
         *  2) or the user really know the implementation he wants (for example if he specifies a
         *     JTS CoordinateSequenceFactory, he probably wants to use the JTS GeometryFactory).
         *
         * In the particular case of this test suite, this extra step would not be needed
         * neither if factory #1 was last in the ordering rather than first.
         */
        final Hints implementations = new Hints(DummyFactory.DUMMY_FACTORY, new Class[]
                                      {DummyFactory.Example2.class, DummyFactory.Example3.class});
        /*
         * Now search NOT for factory #1, but rather for a factory using #1 internally.
         * This is the case of factory #2.
         */
        hints = new Hints(DummyFactory.INTERNAL_FACTORY, DummyFactory.Example1.class);
        hints.add(implementations);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("Expected a factory using #1 internally. ", factory2, factory);
    }

    /**
     * Tests the {@link FactoryCreator#getProvider} method.
     * This test tries again the cases that was expected to throws an exception in
     * {@link #testGetProvider}. But now, those cases are expected to creates automatically
     * new factory instances instead of throwing an exception.
     */
    @Test
    public void testCreateProvider() {
        final Hints.Key    key      = DummyFactory.DUMMY_FACTORY;
        final DummyFactory factory1 = new DummyFactory.Example1();
        final DummyFactory factory2 = new DummyFactory.Example2();
        final DummyFactory factory3 = new DummyFactory.Example3();
        final FactoryRegistry registry = getRegistry(true, factory1, factory2, factory3);
        Hints hints;
        DummyFactory factory;
        /*
         * Same tests than above (at least some of them).
         * See comments in 'testGetProvider()' for explanation.
         */
        hints   = new Hints(Hints.KEY_INTERPOLATION, Hints.VALUE_INTERPOLATION_BILINEAR);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("First factory matches; it should be selected. ", factory1, factory);

        hints   = new Hints(DummyFactory.DUMMY_FACTORY, DummyFactory.Example2.class);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame("Factory of class #2 were requested. ", factory2, factory);
        /*
         * The following case was throwing an exception in testGetProvider(). It should fails again
         * here, but for a different reason. FactoryCreator is unable to creates automatically a new
         * factory instance, since we gave no implementation hint and no registered factory have a
         * constructor expecting a Hints argument.
         */
        hints = new Hints(Hints.KEY_INTERPOLATION, Hints.VALUE_INTERPOLATION_BICUBIC);
        try {
            factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
            fail("Found or created factory "+factory+", while it should not have been allowed.");
        } catch (FactoryNotFoundException exception) {
            // This is the expected exception. Continue...
        }
        /*
         * Register a DummyFactory with a constructor expecting a Hints argument, and try again
         * with the same hints. Now it should creates a new factory instance, because we are using
         * FactoryCreator instead of FactoryRegistry and an appropriate constructor is found.
         * Note that an AssertionFailedError should be thrown if the no-argument constructor of
         * Example5 is invoked, since the constructor with a Hints argument should have priority.
         */
        final DummyFactory factory5 = new DummyFactory.Example5(null);
        registry.registerServiceProvider(factory5);
        assertTrue(registry.setOrdering(DummyFactory.class, factory1, factory5));
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertSame   ("An instance of Factory #5 should have been created.", factory5.getClass(), factory.getClass());
        assertNotSame("A NEW instance of Factory #5 should have been created", factory5, factory);
        /*
         * Tries again with a class explicitly specified as an implementation hint.
         * It doesn't matter if this class is registered or not.
         */
        hints.put(DummyFactory.DUMMY_FACTORY, DummyFactory.Example4.class);
        factory = registry.getServiceProvider(DummyFactory.class, null, hints, key);
        assertEquals("An instance of Factory #4 should have been created.", DummyFactory.Example4.class, factory.getClass());
    }
}
