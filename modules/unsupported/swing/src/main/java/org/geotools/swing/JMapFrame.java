/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;

/**
 * A Swing frame containing a map display pane and (optionally) a toolbar,
 * status bar and map layer table.
 * <p>
 * Simplest use is with the static {@linkplain #showMap(MapContext)} method:
 * <pre>{@code \u0000
 * MapContext context = new DefaultMapContext();
 * context.setTitle("Maps R Us");
 *
 * // add some layers to the MapContext...
 *
 * JMapFrame.showMap(context);
 * }</pre>
 *
 * @see MapLayerTable
 * @see StatusBar
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 */
public class JMapFrame extends JFrame {

    /**
     * Constants for available toolbar buttons used with the
     * {@linkplain #enableTool} method.
     */
    public enum Tool {
        /**
         * Used to request that an empty toolbar be created
         */
        NONE,

        /**
         * Requests the feature info cursor tool
         */
        INFO,

        /**
         * Requests the pan cursor tool
         */
        PAN,

        /**
         * Requests the reset map extent cursor tool
         */
        RESET,

        /**
         * Requests the zoom in and out cursor tools
         */
        ZOOM;
    }

    private Set<Tool> toolSet;

    /*
     * UI elements
     */
    private JMapPane mapPane;
    private MapLayerTable mapLayerTable;
    private JToolBar toolBar;
    private StatusBar statusBar;

    private boolean showStatusBar;
    private boolean showLayerTable;
    private boolean uiSet;

    /**
     * Creates a new {@code JMapFrame} object with a toolbar, map pane and status
     * bar; sets the supplied {@code MapContext}; and displays the frame on the
     * AWT event dispatching thread. The context's title is used as the frame's
     * title.
     *
     * @param context the map context containing the layers to display
     */
    public static void showMap(MapContext context) {
        final JMapFrame frame = new JMapFrame(context);
        frame.enableStatusBar(true);
        frame.enableToolBar(true);
        frame.initComponents();

        frame.setSize(500, 500);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    /**
     * Default constructor. Creates a {@code JMapFrame} with
     * no context or renderer set
     */
    public JMapFrame() {
        this(null);
    }

    /**
     * Constructs a new {@code JMapFrame} object with specified context
     * and a default renderer (an instance of {@link StreamingRenderer}).
     *
     * @param context the map context with layers to be displayed
     */
    public JMapFrame(MapContext context) {
        this(context, new StreamingRenderer());
    }

    /**
     * Constructs a new {@code JMapFrame} object with specified context and renderer
     *
     * @param context the map context with layers to be displayed
     * @param renderer the renderer to be used
     */
    public JMapFrame(MapContext context, GTRenderer renderer) {
        super(context == null ? "" : context.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showLayerTable = false;
        showStatusBar = false;
        toolSet = new HashSet<Tool>();

        // the map pane is the one element that is always displayed
        mapPane = new JMapPane();
        mapPane.setBackground(Color.WHITE);
        mapPane.setMapContext(context);
        mapPane.setRenderer(renderer);
    }

    /**
     * Set whether a toolbar, with a basic set of map tools, will be displayed
     * (default is false). Calling this with state == true is equivalent to
     * calling {@linkplain #enableTool} with all {@linkplain JMapFrame.Tool}
     * constants.
     * 
     * @param state whether the toolbar is required
     */
    public void enableToolBar(boolean state) {
        if (state) {
            toolSet.addAll(EnumSet.allOf(Tool.class));
        } else {
            toolSet.clear();
        }
    }

    /**
     * This method is an alternative to {@linkplain #enableToolBar(boolean)}.
     * It requests that a tool bar be created with specific tools, identified
     * by {@linkplain JMapFrame.Tool} constants.
     * <code><pre>
     * myMapFrame.enableTool(Tool.PAN, Tool.ZOOM);
     * </pre></code>
     *
     * @param tool one or more {@linkplain JMapFrame.Tool} constants
     */
    public void enableTool(Tool ...tool) {
        for (Tool t : tool) {
            toolSet.add(t);
        }
    }

    /**
     * Set whether a status bar will be displayed to display cursor position
     * and map bounds.
     *
     * @param state whether the status bar is required.
     */
    public void enableStatusBar(boolean state) {
        showStatusBar = state;
    }

    /**
     * Set whether a map layer table will be displayed to show the list
     * of layers in the map context and set their order, visibility and
     * selected status.
     *
     * @param state whether the map layer table is required.
     */
    public void enableLayerTable(boolean state) {
        showLayerTable = state;
    }

    /**
     * Calls {@linkplain #initComponents()} if it has not already been called explicitly
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
     * specified with the enable methods (e.g. {@linkplain #enableToolBar(boolean)} ).
     * If not called explicitly by the client this method will be invoked by
     * {@linkplain #setVisible(boolean) } when the frame is first shown.
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
            sb.append("[30px::]"); // status bar height
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
        if (!toolSet.isEmpty()) {
            toolBar = new JToolBar();
            toolBar.setOrientation(JToolBar.HORIZONTAL);
            toolBar.setFloatable(false);

            ButtonGroup cursorToolGrp = new ButtonGroup();

            if (toolSet.contains(Tool.ZOOM)) {
                JButton zoomInBtn = new JButton(new ZoomInAction(mapPane));
                toolBar.add(zoomInBtn);
                cursorToolGrp.add(zoomInBtn);

                JButton zoomOutBtn = new JButton(new ZoomOutAction(mapPane));
                toolBar.add(zoomOutBtn);
                cursorToolGrp.add(zoomOutBtn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.PAN)) {
                JButton panBtn = new JButton(new PanAction(mapPane));
                toolBar.add(panBtn);
                cursorToolGrp.add(panBtn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.INFO)) {
                JButton infoBtn = new JButton(new InfoAction(mapPane));
                toolBar.add(infoBtn);

                toolBar.addSeparator();
            }

            if (toolSet.contains(Tool.RESET)) {
                JButton resetBtn = new JButton(new ResetAction(mapPane));
                toolBar.add(resetBtn);
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
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, mapLayerTable, mapPane);
            panel.add(splitPane, "grow");

        } else {
            /*
             * No layer table, just the map pane
             */
            panel.add(mapPane, "grow");
        }

        if (showStatusBar) {
            statusBar = new StatusBar(mapPane);
            panel.add(statusBar, "grow");
        }

        this.getContentPane().add(panel);
        uiSet = true;
    }

    /**
     * Get the map context associated with this frame.
     * Returns {@code null} if no map context has been set explicitly with the
     * constructor or {@linkplain #setMapContext}.
     *
     * @return the current {@code MapContext} object
     */
    public MapContext getMapContext() {
        return mapPane.getMapContext();
    }

    /**
     * Set the MapContext object used by this frame.
     *
     * @param context a MapContext instance
     * @throws IllegalArgumentException if context is null
     */
    public void setMapContext(MapContext context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }

        mapPane.setMapContext(context);
    }

    /**
     * Get the renderer being used by this frame.
     * Returns {@code null} if no renderer was set via the constructor
     * or {@linkplain #setRenderer}.
     *
     * @return the current {@code GTRenderer} object
     */
    public GTRenderer getRenderer() {
        return mapPane.getRenderer();
    }

    /**
     * Set the renderer to be used by this frame.
     *
     * @param renderer a GTRenderer instance
     * @throws IllegalArgumentException if renderer is null
     */
    public void setRenderer(GTRenderer renderer) {
        if (renderer == null) {
            throw new IllegalArgumentException("renderer must not be null");
        }

        mapPane.setRenderer(renderer);
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
     * If {@linkplain #initComponents} has not been called yet
     * this method will invoke it.
     *
     * @return the toolbar or null if the toolbar was not enabled
     */
    public JToolBar getToolBar() {
        if (!uiSet) initComponents();
        return toolBar;
    }
}

