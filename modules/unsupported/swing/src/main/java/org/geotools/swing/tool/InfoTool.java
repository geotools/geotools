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

package org.geotools.swing.tool;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

import javax.swing.ImageIcon;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.swing.JTextReporter;
import org.geotools.swing.TextReporterListener;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.utils.MapLayerUtils;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A cursor tool to retrieve information about features that the user clicks
 * on with the mouse. It works with {@code InfoToolHelper} objects which do
 * the work of querying feature data. The primary reason for this design
 * is to shield this class from the grid coverage classes so that
 * users who are working purely with vector data are not forced to have
 * JAI in the classpath.
 *
 * @see InfoToolHelper
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $Id$
 * @version $URL$
 */
public class InfoTool extends CursorTool implements TextReporterListener {

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

    /**
     * Default distance fraction used with line and point features.
     * When the user clicks on the map, this tool searches for features within
     * a rectangle of width w centred on the mouse location, where w is the
     * average map side length multiplied by the value of this constant.
     */
    public static final double DEFAULT_DISTANCE_FRACTION = 0.01d;

    private Cursor cursor;

    private JTextReporter reporter;

    private WeakHashMap<MapLayer, InfoToolHelper> helperTable;

    /**
     * Constructor
     */
    public InfoTool() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon cursorIcon = new ImageIcon(getClass().getResource(CURSOR_IMAGE));

        int iconWidth = cursorIcon.getIconWidth();
        int iconHeight = cursorIcon.getIconHeight();

        Dimension bestCursorSize = tk.getBestCursorSize(cursorIcon.getIconWidth(), cursorIcon.getIconHeight());

        cursor = tk.createCustomCursor(cursorIcon.getImage(), CURSOR_HOTSPOT, TOOL_TIP);

        helperTable = new WeakHashMap<MapLayer, InfoToolHelper>();
    }

    /**
     * Respond to a mouse click by querying each of the {@code MapLayers}. The
     * details of features lying within the threshold distance of the mouse
     * position are reported on screen using a {@code JTextReporter} dialog.
     * <p>
     * <b>Implementation note:</b> An instance of {@code InfoToolHelper} is created
     * and cached for each of the {@code MapLayers}. The helpers are created using
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
        DirectPosition2D pos = ev.getMapPosition();
        report(pos);

        MapContext context = getMapPane().getMapContext();
        for (MapLayer layer : context.getLayers()) {
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
                    if (MapLayerUtils.isGridLayer(layer)) {
                        try {
                            Class<?> clazz = Class.forName("org.geotools.swing.tool.GridLayerHelper");
                            Constructor<?> ctor = clazz.getConstructor(MapContext.class, MapLayer.class);
                            helper = (InfoToolHelper) ctor.newInstance(context, layer);
                            helperTable.put(layer, helper);

                        } catch (Exception ex) {
                            throw new IllegalStateException("Failed to create InfoToolHelper for grid layer", ex);
                        }

                    } else {
                        try {
                            Class<?> clazz = Class.forName("org.geotools.swing.tool.VectorLayerHelper");
                            Constructor<?> ctor = clazz.getConstructor(MapContext.class, MapLayer.class);
                            helper = (InfoToolHelper) ctor.newInstance(context, layer);
                            helperTable.put(layer, helper);

                        } catch (Exception ex) {
                            throw new IllegalStateException("Failed to create InfoToolHelper for vector layer", ex);
                        }
                    }
                }

                Object info = null;

                if (helper instanceof VectorLayerHelper) {
                    ReferencedEnvelope mapEnv = getMapPane().getDisplayArea();
                    double searchWidth = DEFAULT_DISTANCE_FRACTION * (mapEnv.getWidth() + mapEnv.getHeight()) / 2;
                    try {
                        info = helper.getInfo(pos, Double.valueOf(searchWidth));
                    } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }

                    if (info != null) {
                        FeatureIterator<? extends Feature> iter = null;
                        FeatureCollection selectedFeatures = (FeatureCollection) info;
                        try {
                            iter = selectedFeatures.features();
                            while (iter.hasNext()) {
                                report(layerName, iter.next());
                            }

                        } catch (Exception ex) {
                            throw new IllegalStateException(ex);

                        } finally {
                            if (iter != null) {
                                iter.close();
                            }
                        }
                    }

                } else {
                    try {
                        info = helper.getInfo(pos);
                    } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }

                    if (info != null) {
                        List<Number> bandValues = (List<Number>) info;
                        if (!bandValues.isEmpty()) {
                            report(layerName, bandValues);
                        }
                    }
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
     * Write the feature attribute names and values to a
     * {@code JTextReporter}
     *
     * @param layerName name of the map layer that contains this feature
     * @param feature the feature to report on
     */
    private void report(String layerName, Feature feature) {
        createReporter();

        Collection<Property> props = feature.getProperties();
        String valueStr = null;

        reporter.append(layerName);
        reporter.append("\n");

        for (Property prop : props) {
            String name = prop.getName().getLocalPart();
            Object value = prop.getValue();

            if (value instanceof Geometry) {
                name = "  Geometry";
                valueStr = value.getClass().getSimpleName();
            } else {
                valueStr = value.toString();
            }

            reporter.append(name + ": " + valueStr);
            reporter.append("\n");
        }
        reporter.append("\n");
    }

    /**
     * Write an array of grid coverage band values to a
     * {@code JTextReporter}
     *
     * @param layerName name of the map layer that contains the grid coverage
     * @param bandValues array of values
     */
    private void report(String layerName, List<Number> bandValues) {
        createReporter();

        reporter.append(layerName);
        reporter.append("\n");

        int k = 1;
        for (Number value : bandValues) {
            reporter.append(String.format("  Band %d: %s\n", k++, value.toString()));
        }
        reporter.append("\n");
    }

    /**
     * Create and show a {@code JTextReporter} if one is not already active
     * for this tool
     */
    private void createReporter() {
        if (reporter == null) {
            reporter = new JTextReporter("Feature info", 20, 30);
            reporter.addListener(this);

            reporter.setVisible(true);
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
    public void onReporterClosed(WindowEvent ev) {
        reporter = null;
    }

    /**
     * Empty method. Defined to satisfy the {@code TextReporterListener} interface.
     * @param newTextStartLine
     */
    public void onReporterUpdated(int newTextStartLine) {
        // no action
    }

}
