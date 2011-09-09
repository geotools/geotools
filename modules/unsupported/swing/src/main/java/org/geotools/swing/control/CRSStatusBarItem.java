/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.LocaleUtils;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.menu.CRSPopupMenu;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A status bar item that displays the coordinate reference system name
 * and provides a pop-up menu to inspect or change the CRS.
 *
 * @see JMapStatusBar
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class CRSStatusBarItem extends StatusBarItem {
    private static final String NO_CRS = 
            LocaleUtils.getValue("StatusBar", "CRSUndefined");
    
    private static final String TOOL_TIP = 
            LocaleUtils.getValue("StatusBar", "CRSTooltip");
    
    private final JButton btn;

    /**
     * Creates a new item to display CRS details of the associated map pane.
     *
     * @param mapPane the map pane
     * @throws IllegalArgumentException if {@code mapPane} is {@code null}
     */
    public CRSStatusBarItem(MapPane mapPane) {
        super("CRS");

        if (mapPane == null) {
            throw new IllegalArgumentException("mapPane must not be null");
        }
        
        btn = new JButton(NO_CRS);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFont(JMapStatusBar.DEFAULT_FONT);
        btn.setToolTipText(TOOL_TIP);
        add(btn);

        displayCRS(mapPane.getMapContent().getCoordinateReferenceSystem());

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onDisplayAreaChanged(MapPaneEvent ev) {
                ReferencedEnvelope env = (ReferencedEnvelope) ev.getData();
                displayCRS( env.getCoordinateReferenceSystem() );
            }
        });
        
        final JPopupMenu menu = new CRSPopupMenu(mapPane);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.show(btn, 0, 0);
            }
        });
    }

    /**
     * Displays the CRS name as item text.
     *
     * @param crs the CRS
     */
    private void displayCRS(CoordinateReferenceSystem crs) {
        String name = NO_CRS;

        if (crs != null) {
            String crsName = crs.getName().toString();
            if (crsName != null && crsName.trim().length() > 0) {
                name = crsName;
            }
        }

        btn.setText(name);
    }
}
