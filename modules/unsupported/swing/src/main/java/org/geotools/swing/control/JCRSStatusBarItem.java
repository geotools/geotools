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
import org.geotools.map.MapContent;
import org.geotools.swing.locale.LocaleUtils;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.menu.JCRSPopupMenu;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A status bar item that displays the coordinate reference system name
 * and provides a pop-up menu to inspect or change the CRS.
 *
 * @see JMapStatusBar
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class JCRSStatusBarItem extends StatusBarItem {
    private static final String COMPONENT_NAME =
            LocaleUtils.getValue("StatusBar", "CRSItemName");
    
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
    public JCRSStatusBarItem(MapPane mapPane) {
        super(COMPONENT_NAME);

        if (mapPane == null) {
            throw new IllegalArgumentException("mapPane must not be null");
        }
        
        btn = new JButton(NO_CRS);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFont(JMapStatusBar.DEFAULT_FONT);
        btn.setToolTipText(TOOL_TIP);
        add(btn);

        CoordinateReferenceSystem crs = null;
        MapContent mapContent = mapPane.getMapContent();
        if (mapContent != null) {
            crs = mapContent.getCoordinateReferenceSystem();
        }
        displayCRS(crs);

        mapPane.addMapPaneListener(new MapPaneAdapter() {
            @Override
            public void onDisplayAreaChanged(MapPaneEvent ev) {
                ReferencedEnvelope env = (ReferencedEnvelope) ev.getData();
                displayCRS( env.getCoordinateReferenceSystem() );
            }
        });
        
        final JPopupMenu menu = new JCRSPopupMenu(mapPane);
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
