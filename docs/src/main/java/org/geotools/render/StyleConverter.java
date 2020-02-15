/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.render;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.transform.TransformerException;
import net.miginfocom.swing.MigLayout;
import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.xml.styling.SLDParser;
import org.geotools.xml.styling.SLDTransformer;
import org.geotools.xsd.Encoder;
import org.opengis.style.Style;

/**
 * StyleConverter example used to demonstrate reading and writing of SLD and SE files.
 *
 * @author Jody Garnett
 */
@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
public class StyleConverter extends JFrame {
    private static final String SE_1_1 = "se 1.1";

    private static final String SLD_1_1 = "sld 1.1";

    private static final String SLD_1_0 = "sld 1.0";

    private static final long serialVersionUID = -3407373356333558440L;

    Style style;
    private JTextArea text;

    private ActionListener convertListener =
            new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String command = e.getActionCommand();
                    if (SLD_1_0.equals(command)) {
                        convertToSLD_1_0();
                    }
                    if (SLD_1_1.equals(command)) {
                        convertToSLD_1_1();
                    }
                    if (SE_1_1.equals(command)) {
                        convertToSE_1_1();
                    }
                }
            };

    private ButtonGroup group;

    private JButton importSLD;

    private JButton importSE;

    private JButton export;

    private JRadioButton sldButton1_0;

    private JRadioButton seButton_1_1;

    private JRadioButton sldButton_1_1;

    public static void main(String... args) {
        JFrame frame = new StyleConverter();
        frame.setVisible(true);
    }

    public StyleConverter() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text = new JTextArea(40, 80);

        sldButton1_0 = new JRadioButton(SLD_1_0);
        seButton_1_1 = new JRadioButton(SE_1_1);
        sldButton_1_1 = new JRadioButton(SLD_1_0);

        group = new ButtonGroup();
        group.add(sldButton1_0);
        group.add(seButton_1_1);
        group.add(sldButton_1_1);

        sldButton1_0.addActionListener(convertListener);
        seButton_1_1.addActionListener(convertListener);
        sldButton_1_1.addActionListener(convertListener);

        importSLD = new JButton("Import SLD");
        importSLD.setActionCommand("importSLD");
        export.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (style != null) {
                            importSLD();
                        }
                    }
                });

        importSE = new JButton("Import SE");
        importSE.setEnabled(false);

        export = new JButton("Export");
        export.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (style != null) {
                            export();
                        }
                    }
                });

        getContentPane().setLayout(new MigLayout("", "[[]][][][grow][]", "[][][][][grow][]"));

        getContentPane().add(new JLabel("Format"), "cell 0 0,alignx left,aligny top");

        getContentPane().add(sldButton1_0, "cell 1 0");
        getContentPane().add(seButton_1_1, "cell 2 0");
        getContentPane().add(sldButton_1_1, "cell 3 0");

        getContentPane().add(new JLabel("Style"), "cell 0 1,alignx left,aligny top");

        getContentPane().add(new JScrollPane(text), "cell 0 2 5 5,grow");
        getContentPane().add(importSLD, "cell 6 2");
        getContentPane().add(importSE, "cell 6 3");
        getContentPane().add(export, "cell 6 5");
    }

    private File importStyleWizard(String prompt, String ext, String format) {
        List<Parameter<?>> list = new ArrayList<>();
        list.add(new Parameter<>("import", File.class, ext, format, new KVP(Parameter.EXT, "sld")));

        JParameterListWizard wizard = new JParameterListWizard("Import Style", prompt, list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            return null; // no file selected
        }
        File file = (File) wizard.getConnectionParameters().get("import");
        return file;
    }

    protected void importSLD() {
        File file =
                importStyleWizard(
                        "Select style layer descriptor 1.0 document",
                        "sld",
                        "style layer descriptor");
        if (file == null) return; // cancel

        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        SLDParser sldParser = new SLDParser(factory);
    }

    protected void export() {
        String format = group.getSelection().getActionCommand();
    }

    private void convertToSE_1_1() {
        if (style == null) {
            JOptionPane.showMessageDialog(this, "Style not defined");
            return;
        }
        org.geotools.sld.v1_1.SLDConfiguration configuration =
                new org.geotools.sld.v1_1.SLDConfiguration();
        Encoder encoder = new Encoder(configuration);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            encoder.encode(style, org.geotools.sld.bindings.SLD.FEATURETYPESTYLE, outputStream);
            String document = outputStream.toString("UTF-8");

            text.setText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertToSLD_1_0() {
        if (style == null) {
            JOptionPane.showMessageDialog(this, "Style not defined");
            return;
        }
        SLDTransformer aTransformer = new SLDTransformer();
        aTransformer.setIndentation(4);
        try {
            String document = aTransformer.transform(style);

            display(document, SLD_1_0);
        } catch (TransformerException e) {
            e.printStackTrace();
            return;
        }
    }

    private void convertToSLD_1_1() {
        if (style == null) {
            JOptionPane.showMessageDialog(this, "Style not defined");
            return;
        }
        // Wrap style as an SLD
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        StyledLayerDescriptor sld = factory.createStyledLayerDescriptor();

        org.geotools.sld.v1_1.SLDConfiguration configuration =
                new org.geotools.sld.v1_1.SLDConfiguration();
        Encoder encoder = new Encoder(configuration);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            encoder.encode(
                    style, org.geotools.sld.bindings.SLD.STYLEDLAYERDESCRIPTOR, outputStream);
            String document = outputStream.toString("UTF-8");

            display(document, SLD_1_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Update displayed text and format. */
    public void display(String document, String format) {
        if (document == null) {
            text.setText("<!-- copy or import style document here -->");
            seButton_1_1.setSelected(false);
            sldButton1_0.setSelected(true);
            sldButton_1_1.setSelected(false);
            return;
        }
        text.setText(document);
        if (SLD_1_0.equals(format)) {
            sldButton1_0.setSelected(true);
        } else if (SLD_1_1.equals(format)) {
            sldButton_1_1.setSelected(true);
        } else if (SE_1_1.equals(format)) {
            sldButton_1_1.setSelected(true);
        } else {
            seButton_1_1.setSelected(false);
            sldButton1_0.setSelected(false);
            sldButton_1_1.setSelected(false);
        }
    }

    private void readSLD_1_0() {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();

        StringReader reader = new StringReader(text.getText());
        SLDParser sldParser = new SLDParser(factory, reader);

        Style[] parsed = sldParser.readXML();
        if (parsed != null && parsed.length > 0) {
            style = parsed[0];
        }
    }

    public static StyledLayerDescriptor createDefaultSLD(Style style) {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = factory.createStyledLayerDescriptor();
        UserLayer layer = factory.createUserLayer();

        // FeatureTypeConstraint ftc =
        // styleFactory.createFeatureTypeConstraint(null, Filter.INCLUDE, null);
        layer.setLayerFeatureConstraints(new FeatureTypeConstraint[] {null});
        sld.addStyledLayer(layer);
        layer.addUserStyle((org.geotools.styling.Style) style);

        return sld;
    }
}
