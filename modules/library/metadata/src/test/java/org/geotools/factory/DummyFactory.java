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

import java.util.Map;
import java.util.Collections;
import java.awt.RenderingHints;
import static org.junit.Assert.*;


/**
 * An internal dummy factory for testing factory dependencies.
 * It doesn't matter if this factory is registered or not. We
 * just need a {@code InternalFactory.class} value different
 * than {@code DummyFactory.class}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
interface InternalFactory extends Factory {
}

/**
 * Dummy factory interface for {@link FactoryRegistryTest}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public interface DummyFactory extends InternalFactory {
    /**
     * A hint key for a {@code DummyFactory} instance.
     */
    Hints.Key DUMMY_FACTORY = new Hints.ClassKey(DummyFactory.class);

    /**
     * A hint key for a {@code DummyFactory2} instance.
     */
    Hints.Key INTERNAL_FACTORY = new Hints.ClassKey(InternalFactory.class);

    /**
     * Dummy factory implementation #1.
     * This factory doesn't use any other factory.
     */
    final class Example1 implements DummyFactory {
        @Override
        public String toString() {
            return "#1";
        }

        public Map<RenderingHints.Key, ?> getImplementationHints() {
            return Collections.singletonMap(Hints.KEY_INTERPOLATION,
                                            Hints.VALUE_INTERPOLATION_BILINEAR);
        }
    }

    /**
     * Dummy factory implementation #2.
     * This factory uses factory #1.
     */
    final class Example2 implements DummyFactory {
        @Override
        public String toString() {
            return "#2";
        }

        public Map<RenderingHints.Key, ?> getImplementationHints() {
            final RenderingHints.Key key = INTERNAL_FACTORY;
            return Collections.singletonMap(key, new Example1());
        }
    }

    /**
     * Dummy factory implementation #3.
     * This factory uses factory #2, which uses itself factory #1.
     */
    final class Example3 implements DummyFactory {
        @Override
        public String toString() {
            return "#3";
        }

        public Map<RenderingHints.Key, ?> getImplementationHints() {
            final RenderingHints.Key key = INTERNAL_FACTORY;
            return Collections.singletonMap(key, new Example2());
        }
    }

    /**
     * Dummy factory implementation #4.
     * {@link FactoryRegistryTest} will not register this factory in same time than other ones.
     */
    final class Example4 implements DummyFactory {
        @Override
        public String toString() {
            return "#4";
        }

        public Map<RenderingHints.Key, ?> getImplementationHints() {
            return Collections.singletonMap(Hints.KEY_INTERPOLATION,
                                            Hints.VALUE_INTERPOLATION_BICUBIC);
        }
    }

    /**
     * Dummy factory implementation #5.
     * {@link FactoryRegistryTest} will not register this factory in same time than other ones.
     * This factory is the only one to accept hints.
     */
    final class Example5 implements DummyFactory {
        private Object value = Hints.VALUE_INTERPOLATION_BILINEAR;

        public Example5() {
            fail("The constructor with Hints argument should have been used.");
        }

        public Example5(Hints hints) {
            if (hints!=null && hints.containsKey(Hints.KEY_INTERPOLATION)) {
                value = hints.get(Hints.KEY_INTERPOLATION);
            }
        }

        @Override
        public String toString() {
            return "#5";
        }

        public Map<RenderingHints.Key, ?> getImplementationHints() {
            return Collections.singletonMap(Hints.KEY_INTERPOLATION, value);
        }
    }
}
