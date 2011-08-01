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

package org.geotools.swing.tool;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.dialog.JTextReporter;
import org.geotools.swing.dialog.TextReporterListener;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.dialog.DialogUtils;
import org.geotools.util.logging.Logging;


/**
 * A cursor tool to retrieve information about features that the user clicks
 * on with the mouse. It works with {@linkplain InfoToolHelper} objects which do
 * the work of querying feature data.
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $URL$
 */
public class InfoTool extends CursorTool implements TextReporterListener {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");

    /** The tool name */
    public static final String TOOL_NAME = stringRes.getString("tool_name_info");
    /** Tool tip text */
    public static final String TOOL_TIP = stringRes.getString("tool_tip_info");
    /** Cursor */
    public static final String CURSOR_IMAGE = "/org/geotools/swing/icons/mActionIdentify.png";
    /** Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT = new Point(0, 0);
    /** Icon for the control */
    public static final String ICON_IMAGE = "/org/geotools/swing/icons/mActionIdentify.png";

    private Cursor cursor;
    private JTextReporter reporter;
    private WeakHashMap<Layer, InfoToolHelper> helperTable;

    /**
     * Constructor
     */
    public InfoTool() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon cursorIcon = new ImageIcon(getClass().getResource(CURSOR_IMAGE));
        cursor = tk.createCustomCursor(cursorIcon.getImage(), CURSOR_HOTSPOT, TOOL_TIP);
        helperTable = new WeakHashMap<Layer, InfoToolHelper>();
    }

    /**
     * Respond to a mouse click by querying each of the {@code Layers}. The
     * details of features lying within the threshold distance of the mouse
     * position are reported on screen using a {@code JTextReporter} dialog.
     * <p>
     * <b>Implementation note:</b> An instance of {@code InfoToolHelper} is created
     * and cached for each of the {@code Layers}. The helpers are created using
     * reflection to avoid direct references to grid coverage classes here that would
     * required JAI (Java Advanced Imaging) to be on the classpath even when only
     * vector layers are being used.
     *
     * @param ev mouse event
     *
     * @see JTextReporter
     * @see InfoToolHelper
     */
    @Override
    public void onMouseClicked(MapMouseEvent ev) {
        DirectPosition2D pos = ev.getWorldPos();
        report(pos);

        MapContent content = getMapPane().getMapContent();
        for (Layer layer : content.layers()) {
            if (layer.isSelected()) {
                InfoToolHelper helper = null;

                String layerName = layer.getTitle();
                if (layerName == null || layerName.length() == 0) {
                    layerName = layer.getFeatureSource().getName().getLocalPart();
                }
                if (layerName == null || layerName.length() == 0) {
                    layerName = layer.getFeatureSource().getSchema().getName().getLocalPart();
                }

                helper = helperTable.get(layer);
                if (helper == null) {
                    helper = InfoToolHelperLookup.getHelper(layer);

                    if (helper == null) {
                        LOGGER.log(Level.WARNING,
                                "InfoTool cannot query {0}", layer.getClass().getName());
                        return;
                    }

                    helper.setMapContent(content);
                    helper.setLayer(layer);
                }

                try {
                    InfoToolResult result = helper.getInfo(pos);
                    reporter.append(layerName + "\n");
                    reporter.append(result.toString());
                    reporter.append("\n");
                    
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Unable to query layer {0}", layerName);
                }
            }
        }
    }

    /**
     * Write the mouse click position to a {@code JTextReporter}
     *
     * @param pos mouse click position (world coords)
     */
    private void report(DirectPosition2D pos) {
        createReporter();

        reporter.append(String.format("Pos x=%.4f y=%.4f\n\n", pos.x, pos.y));
    }

    /**
     * Create and show a {@code JTextReporter} if one is not already active
     * for this tool
     */
    private void createReporter() {
        if (reporter == null) {
            reporter = JTextReporter.create("Feature info", 20, 30);
            reporter.addListener(this);

            DialogUtils.showCentred(reporter);
        }
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * {@inheritDoc}
     * 
     * @return Always returns false
     */
    @Override
    public boolean drawDragBox() {
        return false;
    }

    /**
     * Called when a {@code JTextReporter} frame that was being used by this tool
     * is closed by the user
     *
     * @param ev event published by the {@code JTextReporter}
     */
    @Override
    public void onReporterClosed(WindowEvent ev) {
        reporter = null;
    }

    /**
     * Empty method. Defined to satisfy the {@code TextReporterListener} interface.
     * @param newTextStartLine
     */
    @Override
    public void onReporterUpdated(int newTextStartLine) {
        // no action
    }

}
