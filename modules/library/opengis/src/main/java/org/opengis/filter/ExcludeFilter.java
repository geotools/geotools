/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

import java.io.ObjectStreamException;
import java.io.Serializable;


/**
 * Indicating "filter all", evaluates to {@code false}.
 * This is a placeholder filter intended to be used in data structuring definition.
 * <p>
 * <ul>
 *   <li>EXCLUDE or Filter ==> Filter</li>
 *   <li>EXCLUDE and Filter ==> EXCLUDE</li>
 *   <li>EXCLUDE ==> INCLUDE</li>
 * </ul>
 * <p>
 * The above does imply that the AND opperator can short circuit on encountering ALL.
 *
 * @author Jody Garnett (Refractions Research, Inc.)
 * @author Martin Desruisseaux (Geomatys)
 */
public final class ExcludeFilter implements Filter, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -716705962006999508L;

    /**
     * Not extensible.
     */
    ExcludeFilter() {
    }

    /**
     * Accepts a visitor.
     */
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    /**
     * Returns {@code false}, content is excluded.
     */
    public boolean evaluate(Object object) {
        return false;
    }

    /**
     * Returns a string representation of this filter.
     */
    @Override
    public String toString() {
        return "Filter.EXCLUDE";
    }

    /**
     * Returns the canonical instance on deserialization.
     */
    private Object readResolve() throws ObjectStreamException {
        return Filter.EXCLUDE;
    }
}
