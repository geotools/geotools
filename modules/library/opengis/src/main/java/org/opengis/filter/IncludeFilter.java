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
 * Indicating "no filtering", evaluates to {@code true}.
 * This is a placeholder filter intended to be used in data structuring definition.
 * <p>
 * <ul>
 *   <li>INCLUDE or  Filter ==> INCLUDE</li>
 *   <li>INCLUDE and Filter ==> Filter</li>
 *   <li>not INCLUDE ==> EXCLUDE</li>
 * </ul>
 * <p>
 * The above does imply that the OR opperator can short circuit on encountering NONE.
 *
 * @author Jody Garnett (Refractions Research, Inc.)
 * @author Martin Desruisseaux (Geomatys)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/IncludeFilter.java $
 */
public final class IncludeFilter implements Filter, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -8429407144421087160L;

    /**
     * Not extensible.
     */
    IncludeFilter() {
    }

    /**
     * Accepts a visitor.
     */
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit( this, extraData );
    }

    /**
     * Returns {@code true}, content is included.
     */
    public boolean evaluate(Object object) {
        return true;
    }

    /**
     * Returns a string representation of this filter.
     */
    @Override
    public String toString() {
        return "Filter.INCLUDE";
    }

    /**
     * Returns the canonical instance on deserialization.
     */
    private Object readResolve() throws ObjectStreamException {
        return Filter.INCLUDE;
    }
}
