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
package org.geotools.util.factory;

import java.awt.RenderingHints;
import java.util.Map;

/**
 * Hints which should not be merged with global hints, usually because the global hints have already
 * been merged.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 */
class StrictHints extends Hints {
    /** Creates a set of strict hints which is a copy of the specified hints. */
    public StrictHints(final Hints hints) {
        super(hints);
    }

    /** Creates a set of strict hints which is a copy of the specified hints. */
    public StrictHints(final Map<RenderingHints.Key, Object> hints) {
        super(hints);
    }

    /** An immutable set of empty hints. */
    static final class Empty extends StrictHints {
        /** Creates an empty instance. */
        Empty() {
            super((Hints) null);
        }

        /** Unsupported operation. */
        @Override
        public void add(RenderingHints hints) {
            throw new UnsupportedOperationException();
        }

        /** Unsupported operation. */
        @Override
        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        /** Unsupported operation. */
        @Override
        public void putAll(Map<?, ?> map) {
            throw new UnsupportedOperationException();
        }

        /** Returns a modifiable copy. */
        @Override
        public StrictHints clone() {
            return new StrictHints((Hints) null);
        }
    }
}
