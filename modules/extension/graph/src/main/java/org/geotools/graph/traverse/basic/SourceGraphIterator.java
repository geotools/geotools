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
package org.geotools.graph.traverse.basic;

import org.geotools.graph.structure.Graphable;

/**
 * A GraphIterator that starts iteration from a specefied point.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public abstract class SourceGraphIterator extends AbstractGraphIterator {

    /** source of the iteration * */
    private Graphable m_source;

    /**
     * Sets the source for the iteration.
     *
     * @param source The source of the iteration.
     */
    public void setSource(Graphable source) {
        m_source = source;
    }

    /**
     * Returns the source of the iteration.
     *
     * @return The source of the iteration.
     */
    public Graphable getSource() {
        return m_source;
    }
}
