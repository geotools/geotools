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

package org.geotools.swing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
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
 * @source $URL$
 * @version $Id$
 */
public class StatusBar extends JPanel {
    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");

    private JMapPane mapPane;
    private MapContext context;
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
     * Default constructor.
     * {@linkplain #setMapPane} must be
     * called subsequently for the status bar to receive mouse events.
     */
    public StatusBar() {
        this(null);
    }

    /**
     * Constructor. Links the status bar to the specified map pane.
     *
     * @param pane the map pane that will send mouse events to this
     * status bar
     */
    public StatusBar(JMapPane pane) {
        createListeners();
        initComponents();

        if (pane != null) {
            setMapPane(pane);
        }
    }

    /**
     * Register this status bar to receive mouse events from
     * the given map pane
     *
     * @param newPane the map pane
     * @throws IllegalArgumentException if pane is null
     */
    public void setMapPane(final JMapPane newPane) {
        if (newPane == null) {
            throw new IllegalArgumentException(stringRes.getString("arg_null_error"));
        }

        if (mapPane != newPane) {
            if (mapPane != null) {
                mapPane.removeMouseListener(mouseListener);
            }

            newPane.addMouseListener(mouseListener);
            newPane.addMapPaneListener(mapPaneListener);
            context = newPane.getMapContext();
            mapPane = newPane;

            crsMenu.setMapPane(mapPane);
        }
    }

    /**
     * Clear the map coordinate display
     */
    public void clearCoords() {
        coordsLabel.setText("");
    }

    /**
     * Clear the map bounds display
     */
    public void clearBounds() {
        boundsLabel.setText("");
    }

    /**
     * Format and display the coordinates of the given position
     *
     * @param mapPos mouse cursor position (world coords)
     */
    public void displayCoords(DirectPosition2D mapPos) {
        if (mapPos != null) {
            coordsLabel.setText(String.format("  %.2f %.2f", mapPos.x, mapPos.y));
        }
    }

    /**
     * Display the bounding coordinates of the given envelope
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
     * Display the name of the coordinate reference system
     * @param crs the CRS to display
     */
    public void displayCRS(CoordinateReferenceSystem crs) {
        if (crs == null) {
            crsBtn.setText(stringRes.getString("crs_undefined"));
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

        crsBtn = new JButton(stringRes.getString("crs_undefined"));
        crsBtn.setFont(font);

        rect = getFontMetrics(font).getStringBounds("X", graphics);

        constraint = String.format("height %d!", (int)rect.getHeight() + 6);

        crsBtn.setToolTipText(stringRes.getString("tool_tip_statusbar_crs"));
        crsMenu = new CRSPopupMenu();
        crsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crsMenu.show(crsBtn, 0, 0);
            }
        });

        add(crsBtn, constraint);
    }

    /**
     * Initialize the mouse and map bounds listeners
     */
    private void createListeners() {
        mouseListener = new MapMouseAdapter() {

            @Override
            public void onMouseMoved(MapMouseEvent ev) {
                displayCoords(ev.getMapPosition());
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
            public void onResized(MapPaneEvent ev) {
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

            @Override
            public void onRenderingProgress(MapPaneEvent ev) {
                float progress = ((Number) ev.getData()).floatValue();
                System.out.println("render progress: " + progress);
            }

        };
    }

}
