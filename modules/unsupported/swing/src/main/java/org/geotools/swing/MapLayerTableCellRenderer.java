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

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.geotools.map.MapLayer;

/**
 * A custom list cell renderer for items in the JList used by {@linkplain MapLayerTable}
 * to show map layer names and states.
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class MapLayerTableCellRenderer extends JPanel implements ListCellRenderer {
    private static final long serialVersionUID = 7907189175227502588L;

    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");

    /**
     * Items used to display layer states and controls. Each item has
     * one or two icons associated with it: one for simple controls,
     * two for toggle controls.
     */
    public static enum LayerControlItem {
        /**
         * Layer visibility - whether the layer will be shown or hidden
         * when the map display is drawn
         */
        VISIBLE(
            new ImageIcon(MapLayerTableCellRenderer.class.getResource(
                "/org/geotools/swing/icons/eye_open.png")),
            new ImageIcon(MapLayerTableCellRenderer.class.getResource(
                "/org/geotools/swing/icons/eye_closed.png"))
        ),

        /**
         * Layer selection - the selected status of layers can be used
         * to include or exclude them in map queries etc.
         */
        SELECTED(
            new ImageIcon(MapLayerTableCellRenderer.class.getResource(
                "/org/geotools/swing/icons/tick.png")),
            new ImageIcon(MapLayerTableCellRenderer.class.getResource(
                "/org/geotools/swing/icons/cross.png"))
        ),

        /**
         * Layer style - to open a style dialog for the layer
         */
        STYLE(
            new ImageIcon(MapLayerTableCellRenderer.class.getResource(
                "/org/geotools/swing/icons/style_layer.png")),
            null // no off state for this label
        ),

        REMOVE(
            new ImageIcon(MapLayerTableCellRenderer.class.getResource(
                "/org/geotools/swing/icons/remove_layer.png")),
            null // no off state for this label
        );

        private ImageIcon onIcon;
        private ImageIcon offIcon;

        /**
         * Private constructor
         * @param onIcon icon for the 'on' state
         * @param offIcon icon for the 'off' state
         */
        private LayerControlItem(ImageIcon onIcon, ImageIcon offIcon) {
            this.onIcon = onIcon;
            this.offIcon = offIcon;
        }

        /**
         * Get the icon used to signify the 'on' state for toggle controls
         * or the single icon for non-toggle controls
         *
         * @return the icon
         */
        public Icon getIcon() {
            return onIcon;
        }

        /**
         * Get the icon used to signify the 'off' state. If called for a non-toggle
         * control this returns the single icon.
         *
         * @return the icon
         */
        public Icon getOffIcon() {
            if (offIcon != null) {
                return offIcon;
            } else {
                return onIcon;
            }
        }
    }

    private final static int CELL_PADDING = 5;
    private final static int CELL_HEIGHT;
    private final static Rectangle SELECT_LABEL_BOUNDS;
    private final static Rectangle VISIBLE_LABEL_BOUNDS;
    private final static Rectangle STYLE_LABEL_BOUNDS;
    private final static Rectangle REMOVE_LABEL_BOUNDS;
    private final static Rectangle NAME_LABEL_BOUNDS;

    static {
        int maxIconHeight = 0;
        for (LayerControlItem state : LayerControlItem.values()) {
            maxIconHeight = Math.max(maxIconHeight, state.getIcon().getIconHeight());
        }
        CELL_HEIGHT = maxIconHeight + 2*CELL_PADDING;

        int x = CELL_PADDING;
        int h = LayerControlItem.VISIBLE.getIcon().getIconHeight();
        int w = LayerControlItem.VISIBLE.getIcon().getIconWidth();
        VISIBLE_LABEL_BOUNDS = new Rectangle(x, CELL_PADDING, w, h);
        x += w + CELL_PADDING;

        h = LayerControlItem.SELECTED.getIcon().getIconHeight();
        w = LayerControlItem.SELECTED.getIcon().getIconWidth();
        SELECT_LABEL_BOUNDS = new Rectangle(x, CELL_PADDING, w, h);
        x += w + CELL_PADDING;

        h = LayerControlItem.STYLE.getIcon().getIconHeight();
        w = LayerControlItem.STYLE.getIcon().getIconWidth();
        STYLE_LABEL_BOUNDS = new Rectangle(x, CELL_PADDING, w, h);
        x += w + CELL_PADDING;

        h = LayerControlItem.REMOVE.getIcon().getIconHeight();
        w = LayerControlItem.REMOVE.getIcon().getIconWidth();
        REMOVE_LABEL_BOUNDS = new Rectangle(x, CELL_PADDING, w, h);
        x += w + CELL_PADDING;

        NAME_LABEL_BOUNDS = new Rectangle(x, CELL_PADDING, 1000, CELL_HEIGHT - 2*CELL_PADDING);
    }

    private JLabel visibleLabel;
    private JLabel selectedLabel;
    private JLabel styleLabel;
    private JLabel removeLayerLabel;
    private JLabel nameLabel;


    /**
     * Get the constant height that will be used for list cells
     * @return cell height in pixels
     */
    public static int getCellHeight() {
        return CELL_HEIGHT;
    }

    /**
     * Check if a point representing a mouse click location lies within
     * the bounds of the layer visibility label
     * @param p coords of the mouse click; relative to this cell's origin
     * @return true if the point is within the label bounds; false otherwise
     */
    public static boolean hitVisibilityLabel(Point p) {
        return VISIBLE_LABEL_BOUNDS.contains(p);
    }

    /**
     * Check if a point representing a mouse click location lies within
     * the bounds of the layer selection label
     * @param p coords of the mouse click; relative to this cell's origin
     * @return true if the point is within the label bounds; false otherwise
     */
    public static boolean hitSelectionLabel(Point p) {
        return SELECT_LABEL_BOUNDS.contains(p);
    }

    /**
     * Check if a point representing a mouse click location lies within
     * the bounds of the layer style label
     * @param p coords of the mouse click; relative to this cell's origin
     * @return true if the point is within the label bounds; false otherwise
     */
    public static boolean hitStyleLabel(Point p) {
        return STYLE_LABEL_BOUNDS.contains(p);
    }

    /**
     * Check if a point representing a mouse click location lies within
     * the bounds of the remove layer label
     * @param p coords of the mouse click; relative to this cell's origin
     * @return true if the point is within the label bounds; false otherwise
     */
    public static boolean hitRemoveLabel(Point p) {
        return REMOVE_LABEL_BOUNDS.contains(p);
    }

    public static boolean hitNameLabel(Point p) {
        return NAME_LABEL_BOUNDS.contains(p);
    }


    /**
     * Constructor
     */
    public MapLayerTableCellRenderer() {
        super(new FlowLayout(FlowLayout.LEFT, CELL_PADDING, CELL_PADDING));

        visibleLabel = new JLabel();
        add(visibleLabel);

        selectedLabel = new JLabel();
        add(selectedLabel);

        styleLabel = new JLabel(LayerControlItem.STYLE.getIcon());
        add(styleLabel);

        removeLayerLabel = new JLabel(LayerControlItem.REMOVE.getIcon());
        add(removeLayerLabel);

        nameLabel = new JLabel();
        add(nameLabel);
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value, // value to display
            int index, // cell index
            boolean isSelected, // is the cell selected
            boolean cellHasFocus) // the list and the cell have the focus
    {
        MapLayer layer = (MapLayer)value;
        String name = layer.getTitle();
        if (name == null || name.trim().length() == 0) {
            name = layer.getFeatureSource().getName().getLocalPart();
        }
        nameLabel.setText(name);

        visibleLabel.setIcon(
                layer.isVisible() ? 
                    LayerControlItem.VISIBLE.getIcon() : LayerControlItem.VISIBLE.getOffIcon());

        selectedLabel.setIcon(
                layer.isSelected() ?
                    LayerControlItem.SELECTED.getIcon() : LayerControlItem.SELECTED.getOffIcon());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
