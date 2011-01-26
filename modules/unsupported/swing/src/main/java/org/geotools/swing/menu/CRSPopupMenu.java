/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.ResourceBundle;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.swing.ExceptionMonitor;
import org.geotools.swing.JCRSChooser;
import org.geotools.swing.JMapPane;
import org.geotools.swing.JTextReporter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A pop-up menu that can be used with {@code JMapPane} for coordinate
 * reference system operations. It has the following items:
 * <ul>
 * <li> Set the CRS for the map pane
 * <li> Show the CRS definition
 * </ol>
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class CRSPopupMenu extends JPopupMenu {

    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");
    private JMapPane mapPane;

    /**
     * Creates a CRS pop-up menu.
     * Use {@linkplain #setMapPane(org.geotools.swing.JMapPane) later to
     * associate this menu with a map pane.
     */
    public CRSPopupMenu() {
        this(null);
    }

    /**
     * Creates a CRS pop-up menu to wotk with the given map pane
     *
     * @param mapPane an instance of JMapPane, or {@code null}
     */
    public CRSPopupMenu(JMapPane mapPane) {
        super("CRS options");

        this.mapPane = mapPane;

        JMenuItem setCRSItem = new JMenuItem(stringRes.getString("crs_popupmenu_setcrs"));
        setCRSItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCRS();
            }
        });
        add(setCRSItem);

        JMenuItem showCRSItem = new JMenuItem(stringRes.getString("crs_popupmenu_showcrs"));
        showCRSItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCRS();
            }
        });
        add(showCRSItem);
    }

    /**
     * Set the map pane that this menu will service
     *
     * @param mapPane an instance of JMapPane, or {@code null}
     */
    public void setMapPane(JMapPane mapPane) {
        this.mapPane = mapPane;
    }

    /**
     * {@inheritDoc}
     * The menu items will only be enabled when both the {@code JMapPane} associated with
     * this menu, and its {@code MapContext}, are non-null.
     *
     */
    @Override
    public void show(Component invoker, int x, int y) {
        boolean enabled = (mapPane != null && mapPane.getMapContext() != null);
        for (Component c : getComponents()) {
            if (c instanceof JMenuItem) {
                c.setEnabled(enabled);
            }
        }
        super.show(invoker, x, y);
    }

    /**
     * Action method for the "Set CRS" item in the CRS label pop-up menu
     */
    private void setCRS() {
        if (mapPane != null && mapPane.getMapContext() != null) {
            CoordinateReferenceSystem crs = mapPane.getMapContext().getCoordinateReferenceSystem();
            String initialSelection = null;
            try {
                if (crs != null) {
                    initialSelection = CRS.lookupIdentifier(Citations.EPSG, crs, false);
                }

            } catch (Exception ex) {
                // do nothing
            }

            CoordinateReferenceSystem newCRS = JCRSChooser.showDialog(
                    mapPane, null, "Choose a projection for the map display", initialSelection);

            if (newCRS != null && (crs == null || !CRS.equalsIgnoreMetadata(crs, newCRS))) {
                try {
                    mapPane.getMapContext().setCoordinateReferenceSystem(newCRS);

                } catch (Exception ex) {
                    ExceptionMonitor.show(this, ex, "Failed to set the display CRS");
                }
            }
        }
    }

    /**
     * Action method for the "Set CRS" item in the CRS label pop-up menu
     */
    private void showCRS() {
        if (mapPane != null && mapPane.getMapContext() != null) {
            CoordinateReferenceSystem crs = mapPane.getMapContext().getCoordinateReferenceSystem();
            String wkt = crs.toWKT();
            JTextReporter reporter = new JTextReporter("Coordinate reference system");
            reporter.append(wkt);
            reporter.setModal(true);
            reporter.setVisible(true);
        }
    }
}
