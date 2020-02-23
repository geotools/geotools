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
package org.geotools.swt;

import java.util.EnumSet;
import java.util.HashSet;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swt.action.InfoAction;
import org.geotools.swt.action.OpenGeotiffAction;
import org.geotools.swt.action.OpenShapefileAction;
import org.geotools.swt.action.PanAction;
import org.geotools.swt.action.ResetAction;
import org.geotools.swt.action.ZoomInAction;
import org.geotools.swt.action.ZoomOutAction;
import org.geotools.swt.utils.CrsStatusBarButton;
import org.geotools.swt.utils.StatusBarNotifier;

/**
 * An SWT frame containing a map display pane and a toolbar, status bar and map layer table.
 *
 * @author Michael Bedward
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class SwtMapFrame extends ApplicationWindow {

    /** Constants for available toolbar buttons used with the {@linkplain #enableTool} method. */
    public enum Tool {
        /** Used to request that an empty toolbar be created */
        NONE,

        /** Requests the feature info cursor tool */
        INFO,

        /** Requests the pan cursor tool */
        PAN,

        /** Requests the reset map extent cursor tool */
        RESET,

        /** Requests the zoom in and out cursor tools */
        ZOOM;
    }

    /*
     * UI elements
     */
    private SwtMapPane mapPane;

    private HashSet<Tool> toolSet;
    private final boolean showLayerTable;
    private final MapContent content;
    private final GTRenderer renderer;
    private InfoAction infoAction;
    private PanAction panAction;
    private ResetAction resetAction;
    private ZoomInAction zoominAction;
    private ZoomOutAction zoomoutAction;

    private OpenShapefileAction openShapeAction;
    private OpenGeotiffAction openCoverageAction;

    /*
     * to see how overlay of shapes works, uncomment all the lines that
     * contain drawAction
     */
    // private DrawShapeAction drawAction;

    /**
     * Creates a new {@code JMapFrame} object with a toolbar, map pane and status bar; sets the
     * supplied {@code MapContent}; and displays the frame on the AWT event dispatching thread. The
     * context's title is used as the frame's title.
     *
     * @param content the map context containing the layers to display
     */
    public static void showMap(MapContent content) {
        final SwtMapFrame frame = new SwtMapFrame(true, true, true, true, content);
        // frame.getShell().setSize(500, 500);
        frame.setBlockOnOpen(true);
        frame.open();
    }

    /** Default constructor. Creates a {@code JMapFrame} with no context or renderer set */
    public SwtMapFrame(
            boolean showMenu, boolean showToolBar, boolean showStatusBar, boolean showLayerTable) {
        this(showMenu, showToolBar, showStatusBar, showLayerTable, null);
    }

    /**
     * Constructs a new {@code JMapFrame} object with specified context and a default renderer (an
     * instance of {@link StreamingRenderer}).
     *
     * @param content the map context with layers to be displayed
     */
    public SwtMapFrame(
            boolean showMenu,
            boolean showToolBar,
            boolean showStatusBar,
            boolean showLayerTable,
            MapContent content) {
        this(
                showMenu,
                showToolBar,
                showStatusBar,
                showLayerTable,
                content,
                new StreamingRenderer());
    }

    /**
     * Constructs a new {@code JMapFrame} object with specified context and renderer
     *
     * @param context the map context with layers to be displayed
     * @param renderer the renderer to be used
     */
    public SwtMapFrame(
            boolean showMenu,
            boolean showToolBar,
            boolean showStatusBar,
            boolean showLayerTable,
            MapContent content,
            GTRenderer renderer) {
        super(null);
        this.showLayerTable = showLayerTable;
        this.content = content;
        this.renderer = renderer;

        // drawAction = new DrawShapeAction();
        infoAction = new InfoAction();
        panAction = new PanAction();
        resetAction = new ResetAction();
        zoominAction = new ZoomInAction();
        zoomoutAction = new ZoomOutAction();
        openShapeAction = new OpenShapefileAction();
        openCoverageAction = new OpenGeotiffAction();

        toolSet = new HashSet<Tool>();
        toolSet.addAll(EnumSet.allOf(Tool.class));
        if (showToolBar) {
            addToolBar(SWT.FLAT | SWT.WRAP);
        }
        if (showStatusBar) {
            addStatusLine();
        }
        if (showMenu) {
            addMenuBar();
        }
    }

    /**
     * This method is an alternative to {@linkplain #enableToolBar(boolean)}. It requests that a
     * tool bar be created with specific tools, identified by {@linkplain SwtMapFrame.Tool}
     * constants. <code><pre>
     * myMapFrame.enableTool(Tool.PAN, Tool.ZOOM);
     * </pre></code>
     *
     * @param tool one or more {@linkplain SwtMapFrame.Tool} constants
     */
    public void enableTool(Tool... tool) {
        for (Tool t : tool) {
            toolSet.add(t);
        }
    }

    protected Control createContents(Composite parent) {
        String title = content.getTitle();
        content.layers();
        if (title != null) {
            getShell().setText(title);
        }

        Composite mainComposite = null;
        if (showLayerTable) {
            SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);
            mainComposite = sashForm;
            MapLayerComposite mapLayerTable = new MapLayerComposite(mainComposite, SWT.BORDER);
            mapPane = new SwtMapPane(mainComposite, SWT.BORDER | SWT.NO_BACKGROUND);
            mapPane.setMapContent(content);
            mapLayerTable.setMapPane(mapPane);
            sashForm.setWeights(new int[] {1, 3});
        } else {
            mainComposite = parent;
            mapPane = new SwtMapPane(mainComposite, SWT.BORDER | SWT.NO_BACKGROUND);
            mapPane.setMapContent(content);
        }

        // the map pane is the one element that is always displayed
        mapPane.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        mapPane.setRenderer(renderer);

        // drawAction.setMapPane(mapPane);
        infoAction.setMapPane(mapPane);
        panAction.setMapPane(mapPane);
        resetAction.setMapPane(mapPane);
        zoominAction.setMapPane(mapPane);
        zoomoutAction.setMapPane(mapPane);
        openShapeAction.setMapPane(mapPane);
        openCoverageAction.setMapPane(mapPane);

        StatusLineManager statusLineManager = getStatusLineManager();
        if (statusLineManager != null) {
            IContributionItem filler =
                    new ControlContribution("org.geotools.swt.SwtMapFrame.ID") {
                        protected Control createControl(Composite parent) {
                            Label almostParent = new Label(parent, SWT.NONE);
                            StatusLineLayoutData statusLineLayoutData = new StatusLineLayoutData();
                            statusLineLayoutData.widthHint = 1;
                            statusLineLayoutData.heightHint = 45;
                            almostParent.setLayoutData(statusLineLayoutData);
                            return almostParent;
                        }
                    };
            CrsStatusBarButton crsButton = new CrsStatusBarButton(mapPane);
            statusLineManager.add(filler);
            statusLineManager.add(crsButton);
            statusLineManager.update(true);
            new StatusBarNotifier(this, mapPane);
        }

        return mainComposite;
    }

    protected ToolBarManager createToolBarManager(int style) {
        ToolBarManager tool_bar_manager = new ToolBarManager(style);
        // tool_bar_manager.add(drawAction);
        tool_bar_manager.add(infoAction);
        tool_bar_manager.add(panAction);
        tool_bar_manager.add(resetAction);
        tool_bar_manager.add(zoominAction);
        tool_bar_manager.add(zoomoutAction);

        return tool_bar_manager;
    }

    protected MenuManager createMenuManager() {
        MenuManager bar_menu = new MenuManager("");

        MenuManager file_menu = new MenuManager("&File");
        file_menu.add(openShapeAction);
        file_menu.add(openCoverageAction);

        MenuManager navigation_menu = new MenuManager("&Navigation");
        bar_menu.add(file_menu);
        bar_menu.add(navigation_menu);

        navigation_menu.add(infoAction);
        navigation_menu.add(panAction);
        navigation_menu.add(resetAction);
        navigation_menu.add(zoominAction);
        navigation_menu.add(zoomoutAction);
        return bar_menu;
    }

    /**
     * Get the map context associated with this frame. Returns {@code null} if no map context has
     * been set explicitly with the constructor or {@linkplain #setMapContent}.
     *
     * @return the current {@code MapContent} object
     */
    public MapContent getMapContent() {
        return mapPane.getMapContent();
    }

    /**
     * Set the MapContent object used by this frame.
     *
     * @param content a MapContent instance
     * @throws IllegalArgumentException if context is null
     */
    public void setMapContent(MapContent content) {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }

        mapPane.setMapContent(content);
    }

    /**
     * Get the renderer being used by this frame. Returns {@code null} if no renderer was set via
     * the constructor or {@linkplain #setRenderer}.
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
     * Provides access to the instance of {@code JMapPane} being used by this frame.
     *
     * @return the {@code JMapPane} object
     */
    public SwtMapPane getMapPane() {
        return mapPane;
    }
}
