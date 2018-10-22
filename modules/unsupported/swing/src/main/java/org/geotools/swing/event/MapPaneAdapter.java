/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.event;

/**
 * An adapter class that implements all of the methods defined by {@code MapPaneListener} as empty
 * methods, allowing sub-classes to just override the methods they need.
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $Id$
 */
public class MapPaneAdapter implements MapPaneListener {

    /** {@inheritDoc} */
    @Override
    public void onNewMapContent(MapPaneEvent ev) {}

    /** {@inheritDoc} */
    @Override
    public void onDisplayAreaChanged(MapPaneEvent ev) {}

    /** {@inheritDoc} */
    @Override
    public void onRenderingStarted(MapPaneEvent ev) {}

    /** {@inheritDoc} */
    @Override
    public void onRenderingStopped(MapPaneEvent ev) {}
}
