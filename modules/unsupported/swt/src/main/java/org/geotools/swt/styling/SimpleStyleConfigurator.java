/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2004, Refractions Research Inc.
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
package org.geotools.swt.styling;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.Layer;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;
import org.geotools.swt.styling.simple.AbstractSimpleConfigurator;
import org.geotools.swt.styling.simple.FillViewer;
import org.geotools.swt.styling.simple.GraphicViewer;
import org.geotools.swt.styling.simple.LabelViewer;
import org.geotools.swt.styling.simple.Mode;
import org.geotools.swt.styling.simple.SLDs;
import org.geotools.swt.styling.simple.ScaleViewer;
import org.geotools.swt.styling.simple.StrokeViewer;
import org.geotools.swt.utils.Messages;
import org.geotools.swt.utils.Utils;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDTransformer;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Defines a "simple" StyleConfigurator for working with SLD documents.
 *
 * <p>This style configurator is defined as follows:
 *
 * <pre>
 * &lt;code&gt;
 *         Mode: (*) Point ( ) Line ( ) Polygon
 *               +-+ +-------+ +------+ +------+
 *         Line: |x| | color | |size\/| |100%\/|
 *           	 +-+ +-------+ +------+ +------+
 *           	 +-+ +-------+ +------+
 *         Fill: |x| | color | | 90%\/|
 *           	 +-+ +-------+ +------+
 *           	 +-+ +----------------+ +------+
 *        Label: |x| |         title\/| | Font |
 *           	 +-+ +----------------+ +------+
 *           	 +-+ +-------+ +------+
 *        Point: |x| | star\/| |size\/|
 *           	 +-+ +-------+ +------+
 *               +-+ +-------------+
 * Min scale d.: |x| |      scale\/|
 *               +-+ +-------------+
 *               +-+ +-------------+
 * Max scale d.: |x| |      scale\/|
 *               +-+ +-------------+
 * &lt;/code&gt;
 * </pre>
 *
 * Where:
 *
 * <ul>
 *   <li>Mode is used to switch between Point / Line / Polygon
 *   <li>Line is used for: <br>
 *       LineString: line color, width, opacity <br>
 *       Polygon: border color, width, opacity <br>
 *       Point: border color, width, opacity
 *   <li>Fill is used for Polygon or Point fill color, opacity
 *   <li>Label is used to choose attribute and set font (the only dialog)
 *   <li>Point is used to set the marker type and size
 *   <li>Min/max scale denominator define at which scale the layer is visible
 * </ul>
 *
 * <p>Notes:
 *
 * <ul>
 *   <li>RasterSymbolizer is handled by its own thing, as is WMS etc...
 *   <li>Layout as per the SLDEditorPart examples - so we can take advantage of more or less room.
 *   <li>Presets is a good idea, just not here
 *   <li>Apply/Revert buttons to be green/red
 *   <li>Advanced (edit the SLD) can be in the view menu
 *   <li>If possible replace color button with a drop down list (may not be possible)
 * </ul>
 *
 * <p>We will do our best to make this thing reusable on an Array of Symbolizers.
 *
 * @author Jody Garnett
 * @since 1.0.0
 */
public class SimpleStyleConfigurator extends AbstractSimpleConfigurator {
    private static final String DEFAULT_GEOMETRY = "(default)"; // $NON-NLS-1$

    public static StyleFactory sf = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

    /**
     * Viewer capturing the geometry name; may be "default" or an explicit geometryName provided by
     * the user
     */
    private ComboViewer geometryName;

    /** Radio button used to indicate point geometry type */
    private Button pointMode;

    /** Radio button used to indicate polygon geometry type */
    private Button polyMode;

    /** Radio button used to indicate linestring geometry type */
    private Button lineMode;

    /** Viewer used to allow interaction with Stroke definition */
    private StrokeViewer line = new StrokeViewer();

    /** Viewer used to allow interaction with Fill definition */
    private FillViewer fill = new FillViewer();

    /** Viewer used to allow interaction with Graphic definition */
    private GraphicViewer point = new GraphicViewer();

    /** Viewer used to allow interaction with TextSymbolizer definition */
    private LabelViewer label = new LabelViewer();

    /** Viewer used to allow interaction with minScale definition */
    private ScaleViewer minScale = new ScaleViewer(ScaleViewer.MIN);

    /** Viewer used to allow interaction with maxScale definition */
    private ScaleViewer maxScale = new ScaleViewer(ScaleViewer.MAX);

    /** The current mode we are working with */
    private Mode mode;

    /**
     * Used to respond to any widget selection event; will call synchronize() method to extract any
     * changes of state from the user interface
     */
    private SelectionListener synchronize =
            new SelectionListener() {
                public void widgetSelected(SelectionEvent e) {
                    synchronize();
                }

                public void widgetDefaultSelected(SelectionEvent e) {
                    synchronize();
                }
            };

    private Button replace;
    /** Construct <code>SimpleStyleConfigurator</code>. */
    public SimpleStyleConfigurator(
            Shell parent, SimpleFeatureCollection featureCollection, Style style) {
        super(parent, featureCollection, style);

        this.line.addListener(this.synchronize);
        this.fill.addListener(this.synchronize);
        this.label.addListener(this.synchronize);
        this.point.addListener(this.synchronize);
        this.minScale.addListener(this.synchronize);
        this.maxScale.addListener(this.synchronize);
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("");
    }

    protected Point getInitialSize() {
        return new Point(450, 370);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite parentPanel = (Composite) super.createDialogArea(parent);
        setLayout(parentPanel);
        // ensure vertical layout
        ((RowLayout) parentPanel.getLayout()).type = SWT.VERTICAL;
        ((RowLayout) parentPanel.getLayout()).spacing = 3;

        KeyAdapter adp =
                new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        /*
                         * I don't like having different ways of checking for keypad enter and the normal
                         * one. Using the keyCode would be better, but I couldn't readily find the value for
                         * CR.
                         */
                        if (e.keyCode == SWT.KEYPAD_CR || e.character == SWT.CR) {
                            refresh(); // makeActionDoStuff();
                        }
                    }
                };
        Composite part =
                AbstractSimpleConfigurator.subpart(
                        parentPanel, Messages.getString("SimpleStyleConfigurator_GeometryLabel"));
        geometryName = new ComboViewer(part);
        geometryName.setContentProvider(
                new IStructuredContentProvider() {
                    FeatureType schema;

                    public Object[] getElements(Object inputElement) {
                        // note use of descriptors; so we can make use of associations if available
                        ArrayList<String> names = new ArrayList<String>();
                        names.add(DEFAULT_GEOMETRY);
                        if (schema != null) {
                            for (PropertyDescriptor descriptor : schema.getDescriptors()) {
                                if (descriptor instanceof GeometryDescriptor) {
                                    names.add(((GeometryDescriptor) descriptor).getLocalName());
                                }
                            }
                        }
                        return names.toArray();
                    }

                    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                        // we don't really care since we are not listening to the change in schema
                        schema = (FeatureType) newInput;
                    }

                    public void dispose() {}
                });
        geometryName.getCombo().setText(DEFAULT_GEOMETRY);
        geometryName.getCombo().addSelectionListener(synchronize);

        part =
                AbstractSimpleConfigurator.subpart(
                        parentPanel, Messages.getString("SimpleStyleConfigurator_ModeLabel"));
        this.pointMode = new Button(part, SWT.RADIO);
        pointMode.setText(Messages.getString("SimpleStyleConfigurator_PointMode"));
        this.lineMode = new Button(part, SWT.RADIO);
        lineMode.setText(Messages.getString("SimpleStyleConfigurator_LineMode"));
        this.polyMode = new Button(part, SWT.RADIO);
        polyMode.setText(Messages.getString("SimpleStyleConfigurator_PolygonMode"));

        this.line.createControl(parentPanel, adp);
        this.fill.createControl(parentPanel, adp);
        this.point.createControl(parentPanel, adp, this.build);
        this.label.createControl(parentPanel, adp);
        this.minScale.createControl(parentPanel, adp);
        this.maxScale.createControl(parentPanel, adp);

        Composite replaceComp =
                AbstractSimpleConfigurator.subpart(
                        parentPanel, Messages.getString("SimpleStyleConfigurator_replaceButton"));
        this.replace = new Button(replaceComp, SWT.CHECK);
        replace.addSelectionListener(synchronize);
        replace.setSelection(true);

        refresh();

        return parentPanel;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == OK) {
            try {
                String styleToString = styleToString(style);
                // System.out.println(styleToString);
            } catch (Exception e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }
        }
        super.buttonPressed(buttonId);
    }

    /**
     * Converts a style to its string representation to be written to file.
     *
     * @param style the style to convert.
     * @return the style string.
     */
    public static String styleToString(Style style) throws Exception {
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        UserLayer layer = sf.createUserLayer();
        layer.setLayerFeatureConstraints(new FeatureTypeConstraint[] {null});
        sld.addStyledLayer(layer);
        layer.addUserStyle(style);

        SLDTransformer aTransformer = new SLDTransformer();
        aTransformer.setIndentation(4);
        String xml = aTransformer.transform(sld);
        return xml;
    }

    public Mode determineMode(SimpleFeatureType schema, boolean askUser) {
        if (schema == null) {
            return Mode.NONE;
        } else if (SLDs.isLine(schema)) {
            return Mode.LINE;
        } else if (SLDs.isPolygon(schema)) {
            return Mode.POLYGON;
        } else if (SLDs.isPoint(schema)) {
            return Mode.POINT;
        } else {
            // we must be Geometry?
            if (askUser) {
                // could not figure it out from the schema
                // try trusting the user?
                if (polyMode.getSelection()) {
                    return Mode.POLYGON;
                } else if (lineMode.getSelection()) {
                    return Mode.LINE;
                } else if (pointMode.getSelection()) {
                    return Mode.POINT;
                }
            }
            return Mode.ALL; // we are a generic geometry
        }
    }

    protected void refresh() {
        Style style = getStyle(); // grab an SLD style or bust

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();
        FeatureTypeStyle fts = null;
        if (ftsList.size() > 0) {
            fts = ftsList.get(0);
        }

        SimpleFeatureType schema = featureCollection.getSchema();
        geometryName.setInput(schema);
        String name = DEFAULT_GEOMETRY;

        Stroke stroke = null;
        Fill fill = null;
        Graphic graphic = null;
        TextSymbolizer text = null;
        LabelPlacement placement = null;

        List<Rule> rules = Collections.emptyList();
        if (fts != null) rules = fts.rules();
        if (rules.size() > 1) {
            // simple mode trimms away all but the first rule
            Rule keepRule = rules.get(0);
            rules.clear();
            rules.add(keepRule);
        }
        this.mode = determineMode(schema, true);

        if (mode == Mode.NONE) {
            pointMode.setSelection(false);
            polyMode.setSelection(false);
            lineMode.setSelection(false);
        } else if (mode == Mode.LINE) {
            lineMode.setSelection(true);
            LineSymbolizer sym = SLDs.lineSymbolizer(fts);
            stroke = SLDs.stroke(sym);
            placement = SLDs.getPlacement(SLDs.ALIGN_LEFT, SLDs.ALIGN_MIDDLE, 0);

            name = sym == null ? null : sym.getGeometryPropertyName();
        } else if (mode == Mode.POLYGON) {
            polyMode.setSelection(true);
            PolygonSymbolizer sym = SLDs.polySymbolizer(fts);
            stroke = SLDs.stroke(sym);
            fill = SLDs.fill(sym);
            placement = SLDs.getPlacement(SLDs.ALIGN_CENTER, SLDs.ALIGN_MIDDLE, 0);

            name = sym == null ? null : sym.getGeometryPropertyName();
        } else if (mode == Mode.POINT || mode == Mode.ALL) { // default to handling as Point
            pointMode.setSelection(true);

            PointSymbolizer sym = SLDs.pointSymbolizer(fts);
            stroke = SLDs.stroke(sym);
            fill = SLDs.fill(sym);
            graphic = SLDs.graphic(sym);
            placement = SLDs.getPlacement(SLDs.ALIGN_LEFT, SLDs.ALIGN_MIDDLE, 0);

            name = sym == null ? null : sym.getGeometryPropertyName();
        }

        text = SLDs.textSymbolizer(fts);
        if (text != null && placement != null) {
            text.setLabelPlacement(placement);
        }

        if (name == null) {
            name = DEFAULT_GEOMETRY;
            geometryName.getCombo().setText(name);
        } else {
            geometryName.getCombo().setText(name);
        }
        Mode raw = determineMode(schema, false);
        pointMode.setEnabled(raw == Mode.ALL);
        polyMode.setEnabled(raw == Mode.ALL);
        lineMode.setEnabled(raw == Mode.ALL);

        double minScaleDen = SLDs.minScale(fts);
        double maxScaleDen = SLDs.maxScale(fts);
        Color defaultColor = Color.red;

        this.line.setStroke(stroke, this.mode, defaultColor);

        this.fill.setFill(fill, this.mode, defaultColor);
        this.point.setGraphic(graphic, this.mode, defaultColor);

        this.label.set(schema, text, this.mode);
        this.minScale.setScale(minScaleDen, 0l);
        this.maxScale.setScale(maxScaleDen, 1111111l);
    }

    /** Synchronize the SLD with the array of symbolizers */
    public void synchronize() {
        List<Symbolizer> acquire = new ArrayList<Symbolizer>();
        TextSymbolizer textSym = this.label.get(this.build);

        SimpleFeatureType schema = featureCollection.getSchema();
        this.mode = determineMode(schema, true);

        String geometryPropertyName = null;
        if (geometryName.getCombo().getSelectionIndex() != 0) {
            geometryPropertyName = geometryName.getCombo().getText();
        }

        switch (this.mode) {
            case LINE:
                {
                    LineSymbolizer lineSymbolizer =
                            this.build.createLineSymbolizer(this.line.getStroke(this.build));
                    acquire.add(lineSymbolizer);
                    lineSymbolizer.setGeometryPropertyName(geometryPropertyName);
                    if (textSym != null) {
                        acquire.add(textSym);
                    }
                }
                break;

            case POLYGON:
                {
                    PolygonSymbolizer polygonSymbolizer =
                            this.build.createPolygonSymbolizer(
                                    this.line.getStroke(this.build), this.fill.getFill(this.build));
                    polygonSymbolizer.setGeometryPropertyName(geometryPropertyName);
                    acquire.add(polygonSymbolizer);
                    if (textSym != null) {
                        acquire.add(textSym);
                    }
                }
                break;

            case POINT:
                {
                    PointSymbolizer pointSymbolizer =
                            this.build.createPointSymbolizer(
                                    this.point.getGraphic(
                                            this.fill.getFill(this.build),
                                            this.line.getStroke(this.build),
                                            this.build));
                    pointSymbolizer.setGeometryPropertyName(geometryPropertyName);
                    acquire.add(pointSymbolizer);
                    if (textSym != null) {
                        acquire.add(textSym);
                    }
                }
                break;
            case ALL:
                {
                    LineSymbolizer lineSymbolizer =
                            this.build.createLineSymbolizer(this.line.getStroke(this.build));
                    acquire.add(lineSymbolizer);
                    acquire.add(lineSymbolizer);
                    PolygonSymbolizer polygonSymbolizer =
                            this.build.createPolygonSymbolizer(
                                    this.line.getStroke(this.build), this.fill.getFill(this.build));
                    polygonSymbolizer.setGeometryPropertyName(geometryPropertyName);
                    acquire.add(polygonSymbolizer);
                    PointSymbolizer pointSymbolizer =
                            this.build.createPointSymbolizer(
                                    this.point.getGraphic(
                                            this.fill.getFill(this.build),
                                            this.line.getStroke(this.build),
                                            this.build));
                    pointSymbolizer.setGeometryPropertyName(geometryPropertyName);
                    acquire.add(pointSymbolizer);
                    if (textSym != null) {
                        acquire.add(textSym);
                    }
                }
                break;
            case NONE:
        }
        double minScaleDen = minScale.getScale();
        double maxScaleDen = maxScale.getScale();

        Symbolizer[] array = acquire.toArray(new Symbolizer[acquire.size()]);
        Rule rule = this.build.createRule(array);
        if (minScale.isEnabled()) rule.setMinScaleDenominator(minScaleDen);
        if (maxScale.isEnabled()) rule.setMaxScaleDenominator(maxScaleDen);
        FeatureTypeStyle featureTypeStyle =
                this.build.createFeatureTypeStyle(SLDs.GENERIC_FEATURE_TYPENAME, rule);
        featureTypeStyle.setName("simple"); // $NON-NLS-1$

        Style style = getStyle();
        style.setDefault(true);
        if (replace.getSelection()) {
            // if repalce was hit we are going to completly redfine the style
            // based on what the user has here
            //
            style.featureTypeStyles().clear();
            style.featureTypeStyles().add(featureTypeStyle);
        } else {
            // if we are just responding to what is going on we will try and update the existing
            // style in place (leaving any other content alone)
            //
            List<FeatureTypeStyle> fts = style.featureTypeStyles();
            boolean match = false;
            for (int i = fts.size() - 1; i > -1; i--) {
                if (SLDs.isSemanticTypeMatch(fts.get(i), "simple")) { // $NON-NLS-1$
                    fts.set(i, featureTypeStyle);
                    match = true;
                    break;
                }
            }
            if (match) {
                style.featureTypeStyles().clear();
                style.featureTypeStyles().addAll(fts);
            } else {
                // add the new entry to the array
                List<FeatureTypeStyle> fts2 = new ArrayList<FeatureTypeStyle>(fts);
                Collections.copy(fts2, fts);
                fts2.add(featureTypeStyle);
                style.featureTypeStyles().clear();
                style.featureTypeStyles().addAll(fts2);
            }
        }

        this.style = style;
    }

    // public static void main( String[] args ) throws Exception {
    // Display display = new Display();
    // Shell shell = new Shell(display);
    //
    // File shapeFile = new File("D:\\TMP\\gt-swt-tests\\junctions.shp");
    // ShapefileDataStore store = new ShapefileDataStore(shapeFile.toURI().toURL());
    // SimpleFeatureSource featureSource = store.getFeatureSource();
    // SimpleFeatureCollection shapefile = featureSource.getFeatures();
    //
    // File styleFile = new File("D:\\TMP\\gt-swt-tests\\junctions.sld");
    // SLDParser parser = new SLDParser(new StyleFactoryImpl(), styleFile);
    // Style[] styles = parser.readXML();
    //
    // SimpleStyleConfigurator tmp = new SimpleStyleConfigurator(shell, shapefile, styles[0]);
    // tmp.setBlockOnOpen(true);
    // tmp.open();
    //
    // }

    public static Style showDialog(Shell parent, Layer layer) throws IOException {
        if (!Utils.isGridLayer(layer)) {
            SimpleFeatureSource featureSource = (SimpleFeatureSource) layer.getFeatureSource();
            Style style = layer.getStyle();
            return showDialog(parent, featureSource, style);
        }
        return null;
    }

    public static Style showDialog(Shell parent, SimpleFeatureSource featureSource, Style style)
            throws IOException {
        SimpleFeatureCollection features = (SimpleFeatureCollection) featureSource.getFeatures();
        SimpleStyleConfigurator tmp = new SimpleStyleConfigurator(parent, features, style);
        tmp.setBlockOnOpen(true);
        tmp.open();
        return tmp.getStyle();
    }
}
