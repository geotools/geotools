/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.tutorial.graph;

import org.geotools.swing.JMapFrame;

/**
 * This is a quick visual example of how to use the graph module using the TIGER roads data.
 *
 * <p>The gt-graph module builds ontop of core GeoTools concepts (so you should be familiar with
 * DataStore, FeatureCollection, Query, Geometry, MapContent prior to starting this tutorial).
 *
 * <p>This example consists of a *simple* JMapFrame with a number of actions allowing you to load a
 * shapefile; convert it to an internal graph; select "waypoints"; and use the way points to
 * calculate a route; use the route to create a FeatureCollection; and display that
 * FeatureCollection as a new layer.
 *
 * @author Jody Garnett
 */
public class Tiger extends JMapFrame {

    public Tiger() {}

    public static void main(String args[]) {
        Tiger tiger = new Tiger();

        tiger.setVisible(true);
    }
}
