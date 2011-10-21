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
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.locale.LocaleUtils;
import org.geotools.swing.dialog.JTextReporter;
import org.geotools.swing.dialog.TextReporterListener;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.util.logging.Logging;


/**
 * A cursor tool to retrieve information about features that the user clicks
 * on with the mouse. It works with {@linkplain InfoToolHelper} objects which do
 * the work of querying feature data.
 * <p>
 * Feature information is displayed on screen using a {@linkplain JTextReporter}
 * dialog. If you want to access the displayed text programmatically you can 
 * override the {@linkplain #onReporterUpdated()} method as shown here:
 * <pre><code>
 * InfoTool tool = new InfoTool() {
 *     &#64;Override
 *     public void onReporterUpdated() {
 *         String text = getTextReporterConnection().getText();
 *         // do something with text
 *     }
 * };
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $URL$
 */
public class InfoTool extends CursorTool implements TextReporterListener {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");

    /** The tool name */
    public static final String TOOL_NAME = LocaleUtils.getValue("CursorTool", "Info");
    
    /** Tool tip text */
    public static final String TOOL_TIP = LocaleUtils.getValue("CursorTool", "InfoTooltip");
    
    /** Cursor */
    public static final String CURSOR_IMAGE = "/org/geotools/swing/icons/mActionIdentify.png";
    
    /** Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT = new Point(0, 0);
    
    /** Icon for the control */
    public static final String ICON_IMAGE = "/org/geotools/swing/icons/mActionIdentify.png";

    private Cursor cursor;
    private WeakHashMap<Layer, InfoToolHelper> helperTable;

    private JTextReporter.Connection textReporterConnection;
    
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
        createReporter();
        report(pos);

        MapContent content = getMapPane().getMapContent();
        final int nlayers = content.layers().size();
        int n = 0;
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
                    textReporterConnection.append(layerName + "\n");
                    textReporterConnection.append(result.toString(), 4);
                    
                    if (++n < nlayers) {
                        textReporterConnection.append("\n");
                    }
                    
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Unable to query layer {0}", layerName);
                }
            }
        }
        
        textReporterConnection.appendSeparatorLine(10, '-');
        textReporterConnection.appendNewline();
    }
    
    /**
     * Gets the connection to the text reporter displayed by this tool. Returns
     * {@code null} if the reporter dialog is not currently displayed. The
     * connection should not be stored because it will expire when the reporter
     * dialog is closed.
     * <p>
     * This method was added for unit test purposes but may be useful for 
     * applications wishing to access the feature data report text, e.g. by
     * overriding {@linkplain #onReporterUpdated()}.
     * 
     * @return the text reporter connection or {@code null} if the reporter dialog
     *     is not currently displayed
     */
    public JTextReporter.Connection getTextReporterConnection() {
        return textReporterConnection;
    }

    /**
     * Writes the mouse click position to a {@code JTextReporter}
     *
     * @param pos mouse click position in world coordinates
     */
    private void report(DirectPosition2D pos) {
        textReporterConnection.append(String.format("Pos x=%.4f y=%.4f\n", pos.x, pos.y));
    }

    /**
     * Creates and shows a {@code JTextReporter}. Does nothing if the 
     * reporter is already active.
     */
    private void createReporter() {
        if (textReporterConnection == null) {
            textReporterConnection = JTextReporter.showDialog(
                    "Feature info", 
                    null, 
                    JTextReporter.DEFAULT_FLAGS,
                    20, 40);
            
            textReporterConnection.addListener(this);
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
     * Called when a {@code JTextReporter} dialog used by this tool is closed.
     */
    @Override
    public void onReporterClosed() {
        textReporterConnection = null;
    }

    /**
     * Called when text is updated in a {@linkplain JTextReporter} dialog being used 
     * by this tool. This is an empty method but may be useful to override.
     * 
     */
    @Override
    public void onReporterUpdated() {
    }

}
