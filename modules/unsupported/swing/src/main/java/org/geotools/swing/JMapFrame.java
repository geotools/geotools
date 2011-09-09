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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.geotools.map.MapContent;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.NoToolAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.control.JMapStatusBar;

/**
 * A Swing frame containing a map display pane and (optionally) a toolbar,
 * status bar and map layer table.
 * <p>
 * Simplest use is with the static {@link #showMap(MapContent)} method:
 * <pre>{@code \u0000
 * MapContent content = new MapContent();
 * content.setTitle("My beautiful map");
 *
 * // add some layers to the MapContent...
 *
 * JMapFrame.showMap(content);
 * }</pre>
 *
 * @see MapLayerTable
 * @see StatusBar
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class JMapFrame extends JFrame {

    /**
     * Constants for available toolbar buttons used with the
     * {@link #enableTool} method.
     */
    public enum Tool {
        /**
         * Simple mouse cursor, used to unselect previous cursor tool.
         */
        POINTER,

        /**
         * The feature info cursor tool
         */
        INFO,

        /**
         * The panning cursor tool.
         */
        PAN,

        /**
         * The reset map extent cursor tool.
         */
        RESET,

        /**
         * The zoom display cursor tools.
         */
        ZOOM;
    }

    private boolean showToolBar;
    private Set<Tool> toolSet;

    /*
     * UI elements
     */
    private JMapPane mapPane;
    private MapLayerTable mapLayerTable;
    private JToolBar toolBar;

    private boolean showStatusBar;
    private boolean showLayerTable;
    private boolean uiSet;

    /**
     * Creates a new map frame with a toolbar, map pane and status
     * bar; sets the supplied {@code MapContent}; and displays the frame.
     * If {@linkplain MapContent#getTitle()} returns a non-empty string,
     * this is used as the frame's title.
     * <p>
     * This method can be called safely from any thread.
     *
     * @param content the map content
     */
    public static void showMap(final MapContent content) {
        if (SwingUtilities.isEventDispatchThread()) {
            doShowMap(content);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    doShowMap(content);
                }
            });
        }
    }
    
    private static void doShowMap(MapContent content) {
        final JMapFrame frame = new JMapFrame(content);
        frame.enableStatusBar(true);
        frame.enableToolBar(true);
        frame.initComponents();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    /**
     * Default constructor. Creates a {@code JMapFrame} with
     * no map content or renderer set
     */
    public JMapFrame() {
        this(null);
    }

    /**
     * Constructs a new {@code JMapFrame} object with specified map content.
     *
     * @param content the map content
     */
    public JMapFrame(MapContent content) {
        super(content == null ? "" : content.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showLayerTable = false;
        showStatusBar = false;
        showToolBar = false;
        toolSet = EnumSet.noneOf(Tool.class);

        // the map pane is the one element that is always displayed
        mapPane = new JMapPane(content);
        mapPane.setBackground(Color.WHITE);
        mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // give keyboard focus to the map pane
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                mapPane.requestFocusInWindow();
            }
        });
        
        mapPane.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            @Override
            public void focusLost(FocusEvent e) {
                mapPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        });
        
        mapPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mapPane.requestFocusInWindow();
            }
        });
    }

    /**
     * Sets whether to display the default toolbar (default is false). 
     * Calling this with state == true is equivalent to
     * calling {@link #enableTool} with all {@link JMapFrame.Tool}
     * constants.
     * 
     * @param enabled whether the toolbar is required
     */
    public void enableToolBar(boolean enabled) {
        if (enabled) {
            toolSet = EnumSet.allOf(Tool.class);
        } else {
            toolSet.clear();
        }
        showToolBar = enabled;
    }

    /**
     * This method is an alternative to {@link #enableToolBar(boolean)}.
     * It requests that a tool bar be created with specific tools, identified
     * by {@link JMapFrame.Tool} constants.
     * 
     * <code><pre>
     * myMapFrame.enableTool(Tool.PAN, Tool.ZOOM);
     * </pre></code>
     * 
     * Calling this method with no arguments or {@code null} is equivalent
     * to {@code enableToolBar(false)}.
     *
     * @param tool tools to display on the toolbar
     */
    public void enableTool(Tool ...tool) {
        if (tool == null || tool.length == 0) {
            enableToolBar(false);
        } else {
            toolSet = EnumSet.copyOf(Arrays.asList(tool));
            showToolBar = true;
        }
    }

    /**
     * Set whether a status bar will be displayed to display cursor position
     * and map bounds.
     *
     * @param enabled whether the status bar is required.
     */
    public void enableStatusBar(boolean enabled) {
        showStatusBar = enabled;
    }

    /**
     * Set whether a map layer table will be displayed to show the list
     * of layers in the map content and set their order, visibility and
     * selected status.
     *
     * @param enabled whether the map layer table is required.
     */
    public void enableLayerTable(boolean enabled) {
        showLayerTable = enabled;
    }

    /**
     * Calls {@link #initComponents()} if it has not already been called explicitly
     * to construct the frame's components before showing the frame.
     *
     * @param state true to show the frame; false to hide.
     */
    @Override
    public void setVisible(boolean state) {
        if (state && !uiSet) {
            initComponents();
        }

        super.setVisible(state);
    }

    /**
     * Creates and lays out the frame's components that have been
     * specified with the enable methods (e.g. {@link #enableToolBar(boolean)} ).
     * If not called explicitly by the client this method will be invoked by
     * {@link #setVisible(boolean) } when the frame is first shown.
     */
    public void initComponents() {
        if (uiSet) {
            // @todo log a warning ?
            return;
        }

        /*
         * We use the MigLayout manager to make it easy to manually code
         * our UI design
         */
        StringBuilder sb = new StringBuilder();
        if (!toolSet.isEmpty()) {
            sb.append("[]"); // fixed size
        }
        sb.append("[grow]"); // map pane and optionally layer table fill space
        if (showStatusBar) {
            sb.append("[min!]"); // status bar height
        }

        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, insets 0", // layout constrains: 1 component per row, no insets

                "[grow]", // column constraints: col grows when frame is resized

                sb.toString() ));

        /*
         * A toolbar with buttons for zooming in, zooming out,
         * panning, and resetting the map to its full extent.
         * The cursor tool buttons (zooming and panning) are put
         * in a ButtonGroup.
         *
         * Note the use of the XXXAction objects which makes constructing
         * the tool bar buttons very simple.
         */
        if (showToolBar) {
            toolBar = new JToolBar();
            toolBar.setOrientation(JToolBar.HORIZONTAL);
            toolBar.setFloatable(false);

            JButton btn;
            ButtonGroup cursorToolGrp = new ButtonGroup();
            
            if (toolSet.contains(Tool.POINTER)) {
                btn = new JButton(new NoToolAction(mapPane));
                toolBar.add(btn);
                cursorToolGrp.add(btn);
            }

            if (toolSet.contains(Tool.ZOOM)) {
                btn = new JButton(new ZoomInAction(mapPane));
                toolBar.add(btn);
                cursorToolGrp.add(btn);

                btn = new JButton(new ZoomOutAction(mapPane));
                toolBar.add(btn);
                cursorToolGrp.add(btn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.PAN)) {
                btn = new JButton(new PanAction(mapPane));
                toolBar.add(btn);
                cursorToolGrp.add(btn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.INFO)) {
                btn = new JButton(new InfoAction(mapPane));
                toolBar.add(btn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.RESET)) {
                btn = new JButton(new ResetAction(mapPane));
                toolBar.add(btn);
            }

            panel.add(toolBar, "grow");
        }

        if (showLayerTable) {
            mapLayerTable = new MapLayerTable(mapPane);

            /*
             * We put the map layer panel and the map pane into a JSplitPane
             * so that the user can adjust their relative sizes as needed
             * during a session. The call to setPreferredSize for the layer
             * panel has the effect of setting the initial position of the
             * JSplitPane divider
             */
            mapLayerTable.setPreferredSize(new Dimension(200, -1));
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                    false, 
                    mapLayerTable, 
                    mapPane);
            panel.add(splitPane, "grow");

        } else {
            /*
             * No layer table, just the map pane
             */
            panel.add(mapPane, "grow");
        }

        if (showStatusBar) {
            panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");
        }

        this.getContentPane().add(panel);
        uiSet = true;
    }

    /**
     * Get the map content associated with this frame.
     * Returns {@code null} if no map content has been set explicitly with the
     * constructor or {@link #setMapContent}.
     *
     * @return the current {@code MapContent} object
     */
    public MapContent getMapContent() {
        return mapPane.getMapContent();
    }

    /**
     * Set the MapContent object used by this frame.
     *
     * @param content the map content
     * @throws IllegalArgumentException if content is null
     */
    public void setMapContent(MapContent content) {
        if (content == null) {
            throw new IllegalArgumentException("map content must not be null");
        }

        mapPane.setMapContent(content);
    }

    /**
     * Provides access to the instance of {@code JMapPane} being used
     * by this frame.
     *
     * @return the {@code JMapPane} object
     */
    public JMapPane getMapPane() {
        return mapPane;
    }

    /**
     * Provides access to the toolbar being used by this frame.
     * If {@link #initComponents} has not been called yet
     * this method will invoke it.
     *
     * @return the toolbar or null if the toolbar was not enabled
     */
    public JToolBar getToolBar() {
        if (!uiSet) initComponents();
        return toolBar;
    }
}

