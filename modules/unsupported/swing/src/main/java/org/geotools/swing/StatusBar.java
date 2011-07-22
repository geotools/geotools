/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.event.MapMouseAdapter;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.menu.CRSPopupMenu;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A status bar to work with JMapPane. It displays the following:
 * <ul>
 * <li> Mouse cursor position (world coordinates)
 * <li> Current display area (world coordinates)
 * <li> Coordinate reference system name
 * <li> Rendering busy indicator
 * </ul>
 *
 * The CRS name is displayed on a JButton which displays a pop-up menu
 * of CRS options for the map.
 *
 * @author Michael Bedward
 * @since 2.6
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class StatusBar extends JPanel {
    private static final String NO_CRS = "CRS undefined";
    
    private MapPane mapPane;
    private MapMouseListener mouseListener;
    private MapPaneAdapter mapPaneListener;

    private JLabel renderLabel;
    private JLabel coordsLabel;
    private JLabel boundsLabel;
    private JButton crsBtn;
    private CRSPopupMenu crsMenu;

    private ImageIcon busyIcon;
    private static final String BUSY_ICON_IMAGE = "/org/geotools/swing/icons/busy_16.gif";


    /**
     * Creates a new instance. {@linkplain #setMapPane} must be called 
     * subsequently.
     */
    public StatusBar() {
        this(null);
    }

    /**
     * Creates a new instance linked to the specified MapPane.
     *
     * @param pane the map pane
     */
    public StatusBar(MapPane pane) {
        createListeners();
        initComponents();

        if (pane != null) {
            doSetMapPane(pane);
        }
    }

    /**
     * Associates this status bar with a map pane.
     *
     * @param newPane the map pane
     * @throws IllegalArgumentException if pane is {@code null}
     */
    public void setMapPane(MapPane newPane) {
        doSetMapPane(newPane);
    }

    /**
     * Helper for {@link #setMapPane(MapPane)} which can be called from
     * the constructor without raising a compiler warning.
     * 
     * @param newPane the map pane
     */
    private void doSetMapPane(final MapPane newPane) {
        if (newPane == null) {
            throw new IllegalArgumentException("newPane must not be null");
        }

        if (mapPane != newPane) {
            if (mapPane != null) {
                mapPane.removeMouseListener(mouseListener);
            }

            newPane.addMouseListener(mouseListener);
            newPane.addMapPaneListener(mapPaneListener);
            mapPane = newPane;

            crsMenu.setMapPane(mapPane);
        }
    }

    /**
     * Clears the map coordinate display.
     */
    public void clearCoords() {
        coordsLabel.setText("");
    }

    /**
     * Clears the map bounds display.
     */
    public void clearBounds() {
        boundsLabel.setText("");
    }

    /**
     * Displays position coordinates.
     *
     * @param mapPos mouse cursor position in world coordinates
     */
    public void displayCoords(DirectPosition2D mapPos) {
        if (mapPos != null) {
            coordsLabel.setText(String.format("  %.2f %.2f", mapPos.x, mapPos.y));
        }
    }

    /**
     * Displays the bounding coordinates.
     * 
     * @param bounds the bounds to display
     */
    public void displayBounds(Envelope bounds) {
        if (bounds != null) {
            boundsLabel.setText(String.format("Min:%.2f %.2f Span:%.2f %.2f",
                    bounds.getMinimum(0),
                    bounds.getMinimum(1),
                    bounds.getSpan(0),
                    bounds.getSpan(1)));
        }
    }

    /**
     * Displays the name of the coordinate reference system.
     * 
     * @param crs the CRS to display
     */
    public void displayCRS(CoordinateReferenceSystem crs) {
        if (crs == null) {
            crsBtn.setText(NO_CRS);
        } else {
            crsBtn.setText(crs.getName().toString());
        }
    }

    /**
     * Creates and sets the layout of components
     */
    private void initComponents() {
        Rectangle2D rect;
        String constraint;

        LayoutManager lm = new MigLayout("insets 0");
        this.setLayout(lm);

        Font font = Font.decode("Courier-12");

        busyIcon = new ImageIcon(StatusBar.class.getResource(BUSY_ICON_IMAGE));
        renderLabel = new JLabel();
        renderLabel.setHorizontalTextPosition(JLabel.LEADING);
        rect = getFontMetrics(font).getStringBounds(
                "rendering", renderLabel.getGraphics());

        constraint = String.format("gapx 5, width %d!, height %d!",
                (int)rect.getWidth() + busyIcon.getIconWidth() + renderLabel.getIconTextGap(),
                (int)Math.max(rect.getHeight(), busyIcon.getIconHeight()) + 6);

        add(renderLabel, constraint);

        coordsLabel = new JLabel();
        Graphics graphics = coordsLabel.getGraphics();
        coordsLabel.setFont(font);

        rect = getFontMetrics(font).getStringBounds(
                "  00000000.000 00000000.000", graphics);

        constraint = String.format("width %d!, height %d!",
                (int)rect.getWidth() + 10, (int)rect.getHeight() + 6);

        add(coordsLabel, constraint);

        boundsLabel = new JLabel();
        boundsLabel.setFont(font);

        rect = getFontMetrics(font).getStringBounds(
                "Min: 00000000.000 00000000.000 Span: 00000000.000 00000000.000", graphics);

        constraint = String.format("width %d!, height %d!",
                (int)rect.getWidth() + 10, (int)rect.getHeight() + 6);

        add(boundsLabel, constraint);

        crsBtn = new JButton(NO_CRS);
        crsBtn.setFont(font);

        rect = getFontMetrics(font).getStringBounds("X", graphics);

        constraint = String.format("height %d!", (int)rect.getHeight() + 6);

        crsBtn.setToolTipText("Set or display the map projection");
        crsMenu = new CRSPopupMenu();
        crsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crsMenu.show(crsBtn, 0, 0);
            }
        });

        add(crsBtn, constraint);
    }

    /**
     * Initializes the mouse and map bounds listeners.
     */
    private void createListeners() {
        mouseListener = new MapMouseAdapter() {
            @Override
            public void onMouseMoved(MapMouseEvent ev) {
                displayCoords(ev.getWorldPos());
            }

            @Override
            public void onMouseExited(MapMouseEvent ev) {
                clearCoords();
            }
        };

        mapPaneListener = new MapPaneAdapter() {
            @Override
            public void onDisplayAreaChanged(MapPaneEvent ev) {
                ReferencedEnvelope env = mapPane.getDisplayArea();
                if (env != null) {
                    displayBounds(env);
                    displayCRS(env.getCoordinateReferenceSystem());
                }
            }

            @Override
            public void onRenderingStarted(MapPaneEvent ev) {
                renderLabel.setText("rendering");
                renderLabel.setIcon(busyIcon);
            }

            @Override
            public void onRenderingStopped(MapPaneEvent ev) {
                renderLabel.setText("");
                renderLabel.setIcon(null);
            }
        };
    }

}
