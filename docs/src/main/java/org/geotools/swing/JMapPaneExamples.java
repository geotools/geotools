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
 */ package org.geotools.swing;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;

/** @author michael */
public class JMapPaneExamples {

    void listenForClicks(JMapPane myMapPane) {
        // mouselistener start
        myMapPane.addMouseListener(
                new MapMouseAdapter() {

                    @Override
                    public void onMouseClicked(MapMouseEvent ev) {
                        // print the screen and world position of the mouse
                        System.out.println("mouse click at");
                        System.out.printf("  screen: x=%d y=%d \n", ev.getX(), ev.getY());

                        DirectPosition2D pos = ev.getWorldPos();
                        System.out.printf("  world: x=%.2f y=%.2f \n", pos.x, pos.y);
                    }

                    @Override
                    public void onMouseEntered(MapMouseEvent ev) {
                        System.out.println("mouse entered map pane");
                    }

                    @Override
                    public void onMouseExited(MapMouseEvent ev) {
                        System.out.println("mouse left map pane");
                    }
                });
        // mouselistener end
    }
}
