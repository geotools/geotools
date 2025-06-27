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
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.swing.MapPane;
import org.geotools.swing.dialog.JCRSChooser;
import org.geotools.swing.dialog.JExceptionReporter;
import org.geotools.swing.dialog.JTextReporter;
import org.geotools.swing.locale.LocaleUtils;

/**
 * A pop-up menu that can be used with a {@code MapPane} for coordinate reference system operations. It has the
 * following items:
 *
 * <ul>
 *   <li>Set the CRS for the map pane
 *   <li>Show the CRS definition
 * </ol>
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $Id$
 */
public class JCRSPopupMenu extends JPopupMenu {

    private static final String SET_CRS_STRING = LocaleUtils.getValue("Menu", "CRS_Set");
    private static final String SHOW_CRS_STRING = LocaleUtils.getValue("Menu", "CRS_Show");

    private MapPane mapPane;

    /**
     * Creates a CRS pop-up menu.
     * Use {@linkplain #setMapPane(MapPane) later to
     * associate this menu with a map pane.
     */
    public JCRSPopupMenu() {
        this(null);
    }

    /**
     * Creates a CRS pop-up menu to work with the given map pane.
     *
     * @param mapPane an instance of MapPane, or {@code null}
     */
    public JCRSPopupMenu(MapPane mapPane) {
        super("CRS options");

        this.mapPane = mapPane;

        JMenuItem setCRSItem = new JMenuItem(SET_CRS_STRING);

        setCRSItem.addActionListener(e -> setCRS());
        add(setCRSItem);

        JMenuItem showCRSItem = new JMenuItem(SHOW_CRS_STRING);
        showCRSItem.addActionListener(e -> showCRS());
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
     * {@inheritDoc} The menu items will only be enabled when both the {@code MapPane} associated with this menu, and
     * its {@code MapContent}, are set.
     */
    @Override
    public void show(Component invoker, int x, int y) {
        boolean enabled = mapPane != null && mapPane.getMapContent() != null;
        for (Component c : getComponents()) {
            if (c instanceof JMenuItem) {
                c.setEnabled(enabled);
            }
        }
        super.show(invoker, x, y);
    }

    /** Sets the CRS. */
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

            CoordinateReferenceSystem newCRS = JCRSChooser.showDialog(null, initial, "EPSG");

            if (newCRS != null && (crs == null || !CRS.equalsIgnoreMetadata(crs, newCRS))) {
                try {
                    mapPane.getMapContent().getViewport().setCoordinateReferenceSystem(newCRS);

                } catch (Exception ex) {
                    JExceptionReporter.showDialog(ex, "Failed to set the requested CRS");
                }
            }
        }
    }

    /** Displays the CRS definition in a dialog. */
    private void showCRS() {
        if (mapPane != null && mapPane.getMapContent() != null) {
            CoordinateReferenceSystem crs = mapPane.getMapContent().getCoordinateReferenceSystem();
            JTextReporter.showDialog("Coordinate reference system", crs.toWKT(), JTextReporter.FLAG_MODAL);
        }
    }
}
