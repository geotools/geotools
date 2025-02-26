/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.graph;

import org.geotools.swing.JMapFrame;

/**
 * This is a quick visual example of how to use the graph module using the TIGER roads data.
 *
 * <p>The gt-graph module builds ontop of core GeoTools concepts (so you should be familiar with DataStore,
 * FeatureCollection, Query, Geometry, MapContent prior to starting this tutorial).
 *
 * <p>This example consists of a *simple* JMapFrame with a number of actions allowing you to load a shapefile; convert
 * it to an internal graph; select "waypoints"; and use the way points to calculate a route; use the route to create a
 * FeatureCollection; and display that FeatureCollection as a new layer.
 *
 * @author Jody Garnett
 */
public class Tiger extends JMapFrame {

    public Tiger() {}

    public static void main(String[] args) {
        Tiger tiger = new Tiger();

        tiger.setVisible(true);
    }
}
