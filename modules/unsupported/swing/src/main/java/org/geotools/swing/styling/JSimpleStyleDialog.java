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

package org.geotools.swing.styling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.geotools.data.AbstractDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Font;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.utils.MapLayerUtils;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.expression.Expression;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A dialog to prompt the user for feature style choices. It has a number of static
 * {@code showDialog} methods to work with different sources ({@code SimpleFeatureType},
 * {@code MapLayer}, {@code DataStore}. Each of these displays a dialog and then creates
 * a new {@code Style} instance.
 * <p>
 * Examples of use:
 * <pre><code>
 * // Use with a shapefile
 * Component parentGUIComponent = null;
 * ShapefileDataStore shapefile = ...
 * Style style = JSimpleStyleDialog.showDialog(parentGUIComponent, shapefile);
 * if (style != null) {
 *    // create a map layer using this style
 * }
 *
 * // Use with an existing MapLayer
 * MapLayer layer = ...
 * Style style = JSimpleStyleDialog.showDialog(parentGUIComponent, layer);
 * if (style != null) {
 *     layer.setStyle( style );
 * }
 * </code></pre>
 *
 * @see SLD SLD style helper class
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class JSimpleStyleDialog extends JDialog {

    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    /**
     * Well known text names for symbol options
     * @todo these must be defined somwhere else ?
     */
    public static final String WELL_KNOWN_SYMBOL_NAMES[] = {"Circle", "Square", "Cross", "X", "Triangle", "Star"};

    public static final Color DEFAULT_LINE_COLOR = Color.BLACK;
    public static final Color DEFAULT_FILL_COLOR = Color.WHITE;
    public static final float DEFAULT_LINE_WIDTH = 1.0f;
    public static final float DEFAULT_OPACITY = 1.0f;
    public static final float DEFAULT_POINT_SIZE = 3.0f;
    public static final String DEFAULT_POINT_SYMBOL_NAME = "Circle";
    
    private static int COLOR_ICON_SIZE = 16;

    /**
     * Constants for the geometry type that the style
     * preferences apply to
     */
    public static enum GeomType {
        UNDEFINED("undefined type", (Class<? extends Geometry>)null),
        POINT("point", Point.class), 
        LINE("line", LineString.class, MultiLineString.class),
        POLYGON("polygon", Polygon.class, MultiPolygon.class);

        private String desc;
        private Set<Class<? extends Geometry>> classes;

        /**
         * Private constructor.
         *
         * @param desc brief description for {@code toString} method
         * @param classes {@code Geometry} classes that this type corresponds to
         */
        private GeomType(String desc, Class<? extends Geometry> ...classes) {
            this.desc = desc;

            this.classes = new HashSet< Class<? extends Geometry> >();
            if (classes != null) {
                for (Class<? extends Geometry> clazz : classes) {
                    this.classes.add(clazz);
                }
            }
        }

        /**
         * Get the {@code Geometry} classes that correspond to this type
         * @return an unmodifiable set of classes
         */
        public Set<Class<? extends Geometry>> getClasses() {
            return Collections.unmodifiableSet(classes);
        }

        @Override
        public String toString() {
            return desc;
        }
    }
    private GeomType geomType;

    private static enum SourceType {
        DATA_STORE,
        MAP_LAYER;
    }

    private Color lineColor;
    private Color fillColor;
    private float lineWidth;
    private float opacity;
    private float pointSize;
    private String pointSymbolName;
    private boolean labelFeatures;
    private String labelField;
    private Font labelFont;

    private JColorIcon lineColorIcon;
    private JLabel lineColorLabel;
    private JColorIcon fillColorIcon;
    private JLabel fillColorLabel;
    private JSlider fillOpacitySlider;
    private JComboBox pointSizeCBox;
    private JComboBox pointSymbolCBox;
    private JLabel typeLabel;
    private JComboBox labelCBox;

    private static enum ControlCategory {
        LINE, FILL, POINT;
    }
    private Map<Component, ControlCategory> controls;

    private final SimpleFeatureType schema;
    private String[] fieldsForLabels;

    private boolean completed;

    /**
     * Static convenience method: displays a {@code JSimpleStyleDialog} to prompt
     * the user for style preferences to use with the given {@code MapLayer}. The
     * layer's existing style, if any, will be used to initialize the dialog.
     *
     * @param parent parent component (may be null)
     * @param layer the map layer
     *
     * @return a new Style instance or null if the user cancels the dialog
     */
    public static Style showDialog(Component parent, MapLayer layer) {
        /*
         * Grid coverages and readers are not supported yet...
         */
        if (MapLayerUtils.isGridLayer(layer)) {
            JOptionPane.showMessageDialog(null,
                    "Sorry, styling for for grid coverages is not working yet",
                    "Style dialog",
                    JOptionPane.WARNING_MESSAGE);

            return null;
        }

        SimpleFeatureType type = (SimpleFeatureType) layer.getFeatureSource().getSchema();
        return showDialog(parent, type, layer.getStyle());
    }

    /**
     * Static convenience method: displays a {@code JSimpleStyleDialog} to prompt
     * the user for style preferences to use with the first feature type in
     * the {@code dataStore}.
     *
     * @param parent parent JFrame (may be null)
     * @param dataStore data store with the features to be rendered
     *
     * @return a new Style instance or null if the user cancels the dialog
     */
    public static Style showDialog(Component parent, AbstractDataStore dataStore) {
        return showDialog(parent, dataStore, (Style) null);
    }
    
    /**
     * Static convenience method: displays a {@code JSimpleStyleDialog} to prompt
     * the user for style preferences to use with the first feature type in
     * the {@code dataStore}.
     *
     * @param parent parent JFrame (may be null)
     * @param dataStore data store with the features to be rendered
     * @param initialStyle an optional Style object to initialize the dialog
     *        (may be {@code null})
     *
     * @return a new Style instance or null if the user cancels the dialog
     */
    public static Style showDialog(Component parent, AbstractDataStore dataStore, Style initialStyle) {
        SimpleFeatureType type = null;
        try {
            String typeName = dataStore.getTypeNames()[0];
            type = dataStore.getSchema(typeName);

        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        return showDialog(parent, type, initialStyle);
    }

    /**
     * Static convenience method: displays a {@code JSimpleStyleDialog} to prompt
     * the user for style preferences to use with the given feature type.
     *
     * @param parent parent component (may be null)
     * @param featureType the feature type that the Style will be used to display
     *
     * @return a new Style instance or null if the user cancels the dialog
     */
    public static Style showDialog(Component parent, SimpleFeatureType featureType) {
        return showDialog(parent, featureType, (Style) null);
    }

    /**
     * Static convenience method: displays a {@code JSimpleStyleDialog} to prompt
     * the user for style preferences to use with the given feature type.
     *
     * @param parent parent component (may be null)
     * @param featureType the feature type that the Style will be used to display
     * @param initialStyle an optional Style object to initialize the dialog
     *        (may be {@code null})
     *
     * @return a new Style instance or null if the user cancels the dialog
     */
    public static Style showDialog(Component parent, 
            SimpleFeatureType featureType, Style initialStyle) {
        
        Style style = null;
        JSimpleStyleDialog dialog = null;
        if (parent != null) {
            if (parent instanceof Frame) {
                dialog = new JSimpleStyleDialog((Frame) parent, featureType, initialStyle);

            } else if (parent instanceof Dialog) {
                dialog = new JSimpleStyleDialog((Dialog) parent, featureType, initialStyle);
            }
        }

        if (dialog == null) {
            dialog = new JSimpleStyleDialog((Frame)null, featureType, initialStyle);
        }

        dialog.setVisible(true);

        if (dialog.completed()) {
            switch (dialog.getGeomType()) {
                case POLYGON:
                    style = SLD.createPolygonStyle(
                            dialog.getLineColor(),
                            dialog.getFillColor(),
                            dialog.getOpacity(),
                            dialog.getLabelField(),
                            dialog.getLabelFont());
                    break;

                case LINE:
                    style = SLD.createLineStyle(
                            dialog.getLineColor(),
                            dialog.getLineWidth(),
                            dialog.getLabelField(),
                            dialog.getLabelFont());
                    break;

                case POINT:
                    style = SLD.createPointStyle(
                            dialog.getPointSymbolName(),
                            dialog.getLineColor(),
                            dialog.getFillColor(),
                            dialog.getOpacity(),
                            dialog.getPointSize(),
                            dialog.getLabelField(),
                            dialog.getLabelFont());
                    break;
            }
        }

        dialog.dispose();
        return style;
    }


    /**
     * Constructor.
     *
     * @param owner the parent Frame (may be null)
     * @param schema the feature type for which the style is being created
     * @param initialStyle an optional Style object to initialize the dialog
     *        (may be {@code null})
     *
     * @throws IllegalStateException if the data store cannot be accessed
     */
    public JSimpleStyleDialog(Frame owner, SimpleFeatureType schema, Style initialStyle) {
        super(owner, "Simple style maker", true);
        setResizable(false);
        this.schema = schema;
        init(initialStyle);
    }

    /**
     * Constructor.
     *
     * @param owner the parent Dialog (may be null)
     * @param schema the feature type for which the style is being created
     * @param initialStyle an optional Style object to initialize the dialog
     *        (may be {@code null})
     *
     * @throws IllegalStateException if the data store cannot be accessed
     */
    public JSimpleStyleDialog(Dialog owner, SimpleFeatureType schema, Style initialStyle) {
        super(owner, "Simple style maker", true);
        setResizable(false);
        this.schema = schema;
        init(initialStyle);
    }

    /**
     * Helper for constructors
     *
     * @param initialStyle an optional Style object to initialize the dialog
     *        (may be {@code null})
     */
    private void init(Style initialStyle) {

        lineColor = DEFAULT_LINE_COLOR;
        fillColor = DEFAULT_FILL_COLOR;
        lineWidth = DEFAULT_LINE_WIDTH;
        opacity = DEFAULT_OPACITY;
        pointSize = DEFAULT_POINT_SIZE;
        pointSymbolName = DEFAULT_POINT_SYMBOL_NAME;
        labelFeatures = false;
        labelField = null;
        labelFont = sf.getDefaultFont();

        geomType = GeomType.UNDEFINED;
        completed = false;

        try {
            initComponents();
            setType();
            setStyle(initialStyle);

        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Query if the dialog was completed (user clicked the Apply button)
     * @return true if completed; false otherwise
     */
    public boolean completed() {
        return completed;
    }

    /**
     * Get the {@linkplain GeomType} constant for the selected
     * feature type. If the user cancelled the dialog this will
     * be {@linkplain GeomType#UNDEFINED}.
     *
     * @return GeomType constant
     */
    public GeomType getGeomType() {
        return geomType;
    }

    /**
     * Get the selected line color
     *
     * @return line color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Get the selected fill color
     *
     * @return fill color
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Get the fill opacity
     *
     * @return fill opacity between 0 and 1
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Get the selected line width
     *
     * @return line width
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Get the selected point size
     *
     * @return point size
     */
    public float getPointSize() {
        return pointSize;
    }

    /**
     * Get the selected point symbol name
     *
     * @return symbol name
     */
    public String getPointSymbolName() {
        return pointSymbolName;
    }

    /**
     * Get the feature field (attribute) to use for
     * labels
     *
     * @return field name
     */
    public String getLabelField() {
        if (labelFeatures) {
            return labelField;
        }

        return null;
    }

    /**
     * Get the font to use for labels
     *
     * @return a GeoTools Font object
     */
    public Font getLabelFont() {
        return labelFont;
    }

    /**
     * Create and layout the controls
     */
    private void initComponents() {
        MigLayout layout = new MigLayout();
        JPanel panel = new JPanel(layout);
        controls = new HashMap<Component, ControlCategory>();

        JLabel label = null;
        JButton btn = null;

        label = new JLabel("Feature type");
        label.setForeground(Color.BLUE);
        panel.add(label, "wrap");

        typeLabel = new JLabel();
        panel.add(typeLabel, "gapbefore indent, span, wrap");

        /*
         * Line style items
         */
        label = new JLabel("Line");
        label.setForeground(Color.BLUE);
        panel.add(label, "wrap");

        btn = new JButton("Color...");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseLineColor();
            }
        });
        panel.add(btn, "gapbefore indent");
        controls.put(btn, ControlCategory.LINE);

        lineColorIcon = new JColorIcon(COLOR_ICON_SIZE, COLOR_ICON_SIZE, DEFAULT_LINE_COLOR);
        lineColorLabel = new JLabel(lineColorIcon);
        panel.add(lineColorLabel, "gapafter 20px");

        label = new JLabel("Width");
        panel.add(label, "split 2");

        Object[] widths = new Object[5];
        for (int i = 1; i <= widths.length; i++) { widths[i-1] = Integer.valueOf(i); }
        final JComboBox lineWidthCBox = new JComboBox(widths);
        lineWidthCBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lineWidth = ((Number)lineWidthCBox.getModel().getSelectedItem()).intValue();
            }
        });
        panel.add(lineWidthCBox, "wrap");
        controls.put(lineWidthCBox, ControlCategory.LINE);


        /*
         * Fill style items
         */
        label = new JLabel("Fill");
        label.setForeground(Color.BLUE);
        panel.add(label, "wrap");

        btn = new JButton("Color...");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseFillColor();
            }
        });
        panel.add(btn, "gapbefore indent");
        controls.put(btn, ControlCategory.FILL);

        fillColorIcon = new JColorIcon(COLOR_ICON_SIZE, COLOR_ICON_SIZE, DEFAULT_FILL_COLOR);
        fillColorLabel = new JLabel(fillColorIcon);
        panel.add(fillColorLabel, "gapafter 20px");

        label = new JLabel("% opacity");
        panel.add(label, "split 2");

        fillOpacitySlider = new JSlider(0, 100, 100);
        fillOpacitySlider.setPaintLabels(true);
        fillOpacitySlider.setMajorTickSpacing(20);
        fillOpacitySlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                opacity = (float)fillOpacitySlider.getValue() / 100;
            }
        });
        panel.add(fillOpacitySlider, "span, wrap");
        controls.put(fillOpacitySlider, ControlCategory.FILL);


        /*
         * Point style items
         */
        label = new JLabel("Point");
        label.setForeground(Color.BLUE);
        panel.add(label, "wrap");

        label = new JLabel("Size");
        panel.add(label, "gapbefore indent, split 2");

        Object[] sizes = new Object[10];
        for (int i = 1; i <= sizes.length; i++) { sizes[i-1] = Integer.valueOf(i*5); }
        pointSizeCBox = new JComboBox(sizes);
        pointSizeCBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pointSize = ((Number)pointSizeCBox.getModel().getSelectedItem()).intValue();
            }
        });
        panel.add(pointSizeCBox);
        controls.put(pointSizeCBox, ControlCategory.POINT);

        label = new JLabel("Symbol");
        panel.add(label, "skip, split 2");

        final Object[] marks = new Object[ WELL_KNOWN_SYMBOL_NAMES.length ];
        for (int i = 0; i < marks.length; i++) { marks[i] = WELL_KNOWN_SYMBOL_NAMES[i]; }
        pointSymbolCBox = new JComboBox(marks);
        pointSymbolCBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pointSymbolName = marks[pointSymbolCBox.getSelectedIndex()].toString();
            }
        });
        panel.add(pointSymbolCBox, "wrap");
        controls.put(pointSymbolCBox, ControlCategory.POINT);


        /*
         * Label items
         */
        label = new JLabel("Labels");
        label.setForeground(Color.BLUE);
        panel.add(label, "wrap");

        final JButton fontBtn = new JButton("Font...");
        fontBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseLabelFont();
            }
        });

        labelCBox = new JComboBox();
        labelCBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                labelField = labelCBox.getModel().getSelectedItem().toString();
            }
        });

        final JCheckBox checkBox = new JCheckBox();
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                labelFeatures = checkBox.isSelected();
                labelCBox.setEnabled(labelFeatures);
                fontBtn.setEnabled(labelFeatures);
            }
        });
        panel.add(checkBox, "gapbefore indent, span, split 3");

        label = new JLabel("Field");
        panel.add(label);
        labelCBox.setEnabled(checkBox.isSelected());
        panel.add(labelCBox, "wrap");
        fontBtn.setEnabled(checkBox.isSelected());
        panel.add(fontBtn, "wrap");

        /*
         * Apply and Cancel buttons
         */
        btn = new JButton("Apply");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                completed = true;
                setVisible(false);
            }
        });
        panel.add(btn, "span, split 2, align right");

        btn = new JButton("Cancel");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                geomType = GeomType.UNDEFINED;
                setVisible(false);
            }
        });
        panel.add(btn);

        getContentPane().add(panel);
        pack();
    }

    /**
     * Set up the dialog to work with a given feature type
     */
    private void setType() {

        GeometryDescriptor desc = schema.getGeometryDescriptor();
        Class<?> clazz = desc.getType().getBinding();

        String labelText = schema.getTypeName();

        if (Polygon.class.isAssignableFrom(clazz) ||
                MultiPolygon.class.isAssignableFrom(clazz)) {
            geomType = GeomType.POLYGON;
            labelText = labelText + " (polygon)";

        } else if (LineString.class.isAssignableFrom(clazz) ||
                MultiLineString.class.isAssignableFrom(clazz)) {
            geomType = GeomType.LINE;
            labelText = labelText + " (line)";

        } else if (Point.class.isAssignableFrom(clazz) ||
                MultiPoint.class.isAssignableFrom(clazz)) {
            geomType = GeomType.POINT;
            labelText = labelText + " (point)";

        } else {
            throw new UnsupportedOperationException("No style method for " + clazz.getName());
        }

        typeLabel.setText(labelText);

        // enable relevant components
        for (Component c : controls.keySet()) {
            switch (controls.get(c)) {
                case LINE:
                    // nothing to do at present
                    break;

                case FILL:
                    c.setEnabled(geomType != GeomType.LINE);
                    break;

                case POINT:
                    c.setEnabled(geomType == GeomType.POINT);
                    break;
            }
        }

        // set the fields available for labels
        fieldsForLabels = new String[schema.getAttributeCount() - 1];

        int k = 0;
        for (AttributeDescriptor attr : schema.getAttributeDescriptors()) {
            if ( Geometry.class.isAssignableFrom( attr.getType().getBinding() ) ) {
                continue;
            }

            fieldsForLabels[k++] = attr.getLocalName();
        }

        labelCBox.setModel(new DefaultComboBoxModel(fieldsForLabels));
    }

    /**
     * Set dialog items to show the contents of the given style
     *
     * @param style style to display
     */
    private void setStyle(Style style) {
        assert(geomType != GeomType.UNDEFINED);

        FeatureTypeStyle featureTypeStyle = null;
        Rule rule = null;
        Symbolizer symbolizer = null;

        if (style != null) {
            featureTypeStyle = SLD.featureTypeStyle(style, schema);

            if (featureTypeStyle != null) {
                /*
                 * At present this dialog just examines the very first rule and symbolizer
                 */
                if (featureTypeStyle.rules() == null || featureTypeStyle.rules().isEmpty()) {
                    return;
                }
                rule = featureTypeStyle.rules().get(0);

                if (rule.symbolizers() == null) {
                    return;
                }
                for (Symbolizer sym : rule.symbolizers()) {
                    if (isValidSymbolizer(sym, geomType)) {
                        symbolizer = sym;
                        break;
                    }
                }
                if (symbolizer == null) {
                    return;
                }

            } else {
                /*
                 * Just grap the first feature type style that contains the
                 * right sort of symbolizer
                 */
                for (int ifts = 0; featureTypeStyle == null && ifts < style.featureTypeStyles().size(); ifts++) {
                    FeatureTypeStyle fts = style.featureTypeStyles().get(ifts);
                    for (int irule = 0; featureTypeStyle == null && irule < fts.rules().size(); irule++) {
                        Rule r = fts.rules().get(irule);
                        for (Symbolizer sym : r.symbolizers()) {
                            if (isValidSymbolizer(sym, geomType)) {
                                featureTypeStyle = fts;
                                rule = r;
                                symbolizer = sym;
                                break;
                            }
                        }
                    }
                }
            }

            if (featureTypeStyle != null && rule != null && symbolizer != null) {
                initControls(featureTypeStyle, rule, symbolizer);
            }
        }
    }

    /**
     * Initialize the control states based on the given style objects
     *
     * @param fts a {@code FeatureTypeStyle}
     * @param rule a {@code Rule}
     * @param sym a {@code Symbolizer}
     */
    private void initControls(FeatureTypeStyle fts, Rule rule, Symbolizer sym) {
        Expression exp = null;

        switch (geomType) {
            case POLYGON:
                PolygonSymbolizer polySym = (PolygonSymbolizer) sym;
                setLineColorItems( SLD.color(polySym.getStroke()) );
                setFillColorItems( SLD.color(polySym.getFill()) );
                setFillOpacityItems( SLD.opacity(polySym.getFill()) );
                break;

            case LINE:
                LineSymbolizer lineSym = (LineSymbolizer) sym;
                setLineColorItems( SLD.color(lineSym) );
                break;

            case POINT:
                PointSymbolizer pointSym = (PointSymbolizer) sym;
                setLineColorItems( SLD.pointColor(pointSym) );
                setFillColorItems( SLD.pointFill(pointSym) );
                setFillOpacityItems( SLD.pointOpacity(pointSym) );
                setPointSizeItems( SLD.pointSize(pointSym) );
                setPointSymbolItems( SLD.pointWellKnownName(pointSym) );
        }
    }

    private boolean isValidSymbolizer(Symbolizer sym, GeomType type) {
        if (sym != null) {
            if (sym instanceof PolygonSymbolizer) {
                return type == GeomType.POLYGON;
            } else if (sym instanceof LineSymbolizer) {
                return type == GeomType.LINE;
            } else if (sym instanceof PointSymbolizer) {
                return type == GeomType.POINT;
            }
        }

        return false;
    }

    /**
     * Display a color chooser dialog to set the line color
     */
    private void chooseLineColor() {
        Color color = JColorChooser.showDialog(this, "Choose line color", lineColor);
        setLineColorItems(color);
    }

    /**
     * Set the line color items to show the given color choice
     * @param color current color
     */
    private void setLineColorItems(Color color) {
        if (color != null) {
            lineColor = color;
            lineColorIcon.setColor(color);
            lineColorLabel.repaint();
        }
    }
    
    /**
     * Display a color chooser dialog to set the fill color
     */
    private void chooseFillColor() {
        Color color = JColorChooser.showDialog(this, "Choose fill color", fillColor);
        setFillColorItems(color);
    }

    /**
     * Set the fill color items to show the given color choice
     * @param color current color
     */
    private void setFillColorItems(Color color) {
        if (color != null) {
            fillColor = color;
            fillColorIcon.setColor(color);
            fillColorLabel.repaint();
        }
    }

    /**
     * Set the fill opacity items to the given value
     * @param value opacity value between 0 and 1
     */
    private void setFillOpacityItems(double value) {
        opacity = (float) Math.min(1.0, Math.max(0.0, value));
        fillOpacitySlider.setValue((int)(opacity * 100));
    }

    /**
     * Set items for the given point size
     *
     * @param value point size
     */
    private void setPointSizeItems(double value) {
        pointSize = (float) Math.max(0.0, value);
        int newValue = (int)pointSize;

        MutableComboBoxModel model = (MutableComboBoxModel) pointSizeCBox.getModel();
        int insert = -1;
        for (int i = 0; i < model.getSize(); i++) {
            int elValue = ((Number)model.getElementAt(i)).intValue();
            if (elValue == newValue) {
                pointSizeCBox.setSelectedIndex(i);
                return;

            } else if (elValue > newValue) {
                insert = i;
                break;
            }
        }

        if (insert < 0) {
            insert = model.getSize();
            model.addElement(Integer.valueOf(newValue));
        } else {
            model.insertElementAt(Integer.valueOf(newValue), insert);
        }
        pointSizeCBox.setSelectedIndex(insert);
    }

    /**
     * Set items for the given point symbol, identified by its 'well known name'
     *
     * @param wellKnownName name of the symbol
     */
    private void setPointSymbolItems(String wellKnownName) {
        if (wellKnownName != null) {
            for (int i = 0; i < WELL_KNOWN_SYMBOL_NAMES.length; i++) {
                if (WELL_KNOWN_SYMBOL_NAMES[i].equalsIgnoreCase(wellKnownName)) {
                    pointSymbolName = WELL_KNOWN_SYMBOL_NAMES[i];
                    pointSymbolCBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void chooseLabelFont() {
        Font font = JFontChooser.showDialog(this, "Choose label font", labelFont);
        if (font != null) {
            labelFont = font;
        }
    }
}
