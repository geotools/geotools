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
 * An adapter class that implements all of the methods defined by
 * {@code MapPaneListener} as empty methods, allowing sub-classes
 * to just override the methods they need. 
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class MapPaneAdapter implements MapPaneListener {

    /**
     * {@inheritDoc}
     */
    public void onNewContent(MapPaneEvent ev) {}

    /**
     * {@inheritDoc}
     */
    public void onNewRenderer(MapPaneEvent ev) {}

    /**
     * {@inheritDoc}
     */
    public void onDisplayAreaChanged(MapPaneEvent ev) {}

    /**
     * {@inheritDoc}
     */
    public void onRenderingStarted(MapPaneEvent ev) {}

    /**
     * {@inheritDoc}
     */
    public void onRenderingStopped(MapPaneEvent ev) {}

}
