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

package org.geotools.swing.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.swing.ExceptionMonitor;
import org.geotools.swing.locale.LocaleUtils;
import org.geotools.swing.dialog.JCRSChooser;
import org.geotools.swing.dialog.JTextReporter;
import org.geotools.swing.MapPane;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A pop-up menu that can be used with a {@code MapPane} for coordinate
 * reference system operations. It has the following items:
 * <ul>
 * <li> Set the CRS for the map pane
 * <li> Show the CRS definition
 * </ol>
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class CRSPopupMenu extends JPopupMenu {
    
    private static final String SET_CRS_STRING = LocaleUtils.getValue("Menu", "CRS_Set");
    private static final String SHOW_CRS_STRING = LocaleUtils.getValue("Menu", "CRS_Show");

    private MapPane mapPane;

    /**
     * Creates a CRS pop-up menu.
     * Use {@linkplain #setMapPane(MapPane) later to
     * associate this menu with a map pane.
     */
    public CRSPopupMenu() {
        this(null);
    }

    /**
     * Creates a CRS pop-up menu to work with the given map pane.
     *
     * @param mapPane an instance of MapPane, or {@code null}
     */
    public CRSPopupMenu(MapPane mapPane) {
        super("CRS options");

        this.mapPane = mapPane;

        JMenuItem setCRSItem = new JMenuItem(SET_CRS_STRING);
                
        setCRSItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCRS();
            }
        });
        add(setCRSItem);

        JMenuItem showCRSItem = new JMenuItem(SHOW_CRS_STRING);
        showCRSItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCRS();
            }
        });
        add(showCRSItem);
    }

    /**
     * Sets the map pane.
     *
     * @param mapPane the map pane
     */
    public void setMapPane(MapPane mapPane) {
        this.mapPane = mapPane;
    }

    /**
     * {@inheritDoc}
     * The menu items will only be enabled when both the {@code MapPane} associated with
     * this menu, and its {@code MapContent}, are set.
     *
     */
    @Override
    public void show(Component invoker, int x, int y) {
        boolean enabled = (mapPane != null && mapPane.getMapContent() != null);
        for (Component c : getComponents()) {
            if (c instanceof JMenuItem) {
                c.setEnabled(enabled);
            }
        }
        super.show(invoker, x, y);
    }

    /**
     * Sets the CRS.
     */
    private void setCRS() {
        if (mapPane != null && mapPane.getMapContent() != null) {
            String initial = null;
            CoordinateReferenceSystem crs = mapPane.getMapContent().getCoordinateReferenceSystem();
            if (crs != null) {
                try {
                    initial = CRS.lookupIdentifier(Citations.EPSG, crs, false);
                } catch (Exception ex) {
                    // do nothing
                }
            }
            
            CoordinateReferenceSystem newCRS = JCRSChooser.showDialog(null, null, initial);

            if (newCRS != null && (crs == null || !CRS.equalsIgnoreMetadata(crs, newCRS))) {
                try {
                    mapPane.getMapContent().getViewport().setCoordinateReferenceSystem(newCRS);

                } catch (Exception ex) {
                    ExceptionMonitor.show(this, ex, "Failed to set the display CRS");
                }
            }
        }
    }

    /**
     * Displays the CRS definition in a dialog.
     */
    private void showCRS() {
        if (mapPane != null && mapPane.getMapContent() != null) {
            CoordinateReferenceSystem crs = mapPane.getMapContent().getCoordinateReferenceSystem();
            String wkt = crs.toWKT();
            JTextReporter reporter = new JTextReporter("Coordinate reference system");
            reporter.append(wkt);
            reporter.setModal(true);
            reporter.setVisible(true);
        }
    }
}
