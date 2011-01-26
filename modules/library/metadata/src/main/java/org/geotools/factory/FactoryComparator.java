/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Set;
import java.util.Map;
import java.awt.RenderingHints;
import org.geotools.util.Utilities;


/**
 * Compares two factories for equality.
 * Used internally for {@link AbstractFactory#equals} implementation only.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class FactoryComparator {
    /**
     * A pair of factory already compared.
     */
    private final Factory f1, f2;

    /**
     * Prepare a comparaison between the two specified factories.
     */
    FactoryComparator(final Factory f1, final Factory f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    /**
     * Returns {@code true} if {@code f1} and {@code f2} are equals.
     *
     * @param done An initially empty set. Used internally for preventing infinite recursivity.
     */
    boolean compare(final Set<FactoryComparator> done) {
        if (done.add(this)) {
            final Map<RenderingHints.Key, ?> m1 = f1.getImplementationHints();
            final Map<RenderingHints.Key, ?> m2 = f2.getImplementationHints();
            if (m1.size() != m2.size()) {
                return false;
            }
            for (final Map.Entry<RenderingHints.Key, ?> entry : m1.entrySet()) {
                final Object key = entry.getKey();
                final Object v1  = entry.getValue();
                final Object v2  = m2.get(key);
                if (v1 == v2) continue;
                if (v1 instanceof Factory) {
                    if (v2 == null || !v1.getClass().equals(v2.getClass()) ||
                       !new FactoryComparator((Factory) v1, (Factory) v2).compare(done))
                    {
                        return false;
                    }
                } else if (!Utilities.equals(v1, v2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * For internal use only. This implementation assumes that {@code f1.equals(f2)}
     * is symetric (i.e. equivalents to {@code f2.equals(f1)}).
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof FactoryComparator) {
            final FactoryComparator that = (FactoryComparator) object;
            return (this.f1 == that.f1 && this.f2 == that.f2) ||
                   (this.f1 == that.f2 && this.f2 == that.f1);
        }
        return false;
    }

    /**
     * For internal use only. Must be compatible with the symetry assumption made in
     * {@link #equals(Object)}: use a commutative operation (addition here) and do not
     * multiply a term by some factor like the usual 37.
     */
    @Override
    public int hashCode() {
        return System.identityHashCode(f1) + System.identityHashCode(f2);
    }
}
