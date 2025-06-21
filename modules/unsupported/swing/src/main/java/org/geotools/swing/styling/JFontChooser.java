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

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.style.Font;
import org.geotools.api.style.StyleFactory;
import org.geotools.factory.CommonFactoryFinder;

/**
 * A dialog to prompt the user for a font. It has a static method to display the dialog and return a new {@code Font}
 * instance.
 *
 * <p>
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $Id$
 */
public class JFontChooser extends JDialog {

    /** serialVersionUID */
    private static final long serialVersionUID = -1543116265293436599L;

    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private static final String[] families =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    private static final String[] styles = {"Normal", "Italic"};
    private static final String[] weights = {"Normal", "Bold"};
    private static final Integer[] sizes = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 72};

    private static final String sampleText = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
            + "abcdefghijklmnopqrstuvwxyz\n"
            + "0123456789\n"
            + "The quick brown fox jumped over the lazy dog";
    private static final int sampleTextLines = 4;

    private JComboBox<String> familyCBox;
    private JComboBox<String> styleCBox;
    private JComboBox<String> weightCBox;
    private JComboBox<Integer> sizeCBox;
    private JTextArea textArea;

    private int familyIndex;
    private int weightIndex;
    private int styleIndex;
    private int sizeIndex;

    private Font selectedFont = null;
    private boolean completed = false;

    /**
     * Static method to display a JFontChooser dialog and return the selected font as a new Font object.
     *
     * @param owner the parent {@code JFrame} or {@code JDialog}; may be {@code null}
     * @param title dialog title
     * @param labelFont the initial font displayed by the dialog, or {@code null} for the default GeoTools font
     * @return a new Font object or {@code null} if the user cancelled the dialog
     */
    public static Font showDialog(Component owner, String title, Font labelFont) {
        JFontChooser chooser = null;
        Font font = null;

        if (owner == null) {
            chooser = new JFontChooser((JFrame) null, title, labelFont);

        } else {
            Class<? extends Component> ownerClass = owner.getClass();

            if (JDialog.class.isAssignableFrom(ownerClass)) {
                chooser = new JFontChooser((JDialog) owner, title, labelFont);
            } else if (JFrame.class.isAssignableFrom(ownerClass)) {
                chooser = new JFontChooser((JFrame) owner, title, labelFont);

            } else {
                throw new IllegalArgumentException("owner must be a JFrame or JDialog object");
            }
        }

        chooser.setVisible(true);
        if (chooser.completed()) {
            font = chooser.getSelectedFont();
        }

        return font;
    }

    /**
     * Constructor
     *
     * @param owner parent frame or {@code null}
     * @param title dialog title
     * @param initialFont the initial font for the chooser to display, or {@code null} for the GeoTools default font
     */
    public JFontChooser(JFrame owner, String title, Font initialFont) {
        super(owner, title, true);
        setResizable(false);
        selectedFont = initialFont == null ? sf.getDefaultFont() : initialFont;

        initFont();
        initComponents();
    }

    /**
     * Constructor
     *
     * @param owner parent dialog or {@code null}
     * @param title dialog title
     * @param initialFont the initial font for the chooser to display, or {@code null} for the GeoTools default font
     */
    public JFontChooser(JDialog owner, String title, Font initialFont) {
        super(owner, title, true);
        setResizable(false);
        selectedFont = initialFont == null ? sf.getDefaultFont() : initialFont;

        initFont();
        initComponents();
    }

    /**
     * Query if the dialog was completed (user clicked the Apply button)
     *
     * @return true if completed; false otherwise
     */
    public boolean completed() {
        return completed;
    }

    /**
     * Get the selected font
     *
     * @return the selected font or {@code null} if the dialog was cancelled
     */
    public Font getSelectedFont() {
        return selectedFont;
    }

    /** Create and lay out the controls */
    private void initComponents() {
        MigLayout layout = new MigLayout();
        JPanel panel = new JPanel(layout);

        JLabel label = new JLabel("Family");
        panel.add(label);

        familyCBox = new JComboBox<>(families);
        familyCBox.setSelectedIndex(familyIndex);
        familyCBox.addActionListener(e -> {
            familyIndex = familyCBox.getSelectedIndex();
            showSample();
        });
        panel.add(familyCBox);

        label = new JLabel("Style");
        panel.add(label);

        styleCBox = new JComboBox<>(styles);
        styleCBox.setSelectedIndex(styleIndex);
        styleCBox.addActionListener(e -> {
            styleIndex = styleCBox.getSelectedIndex();
            showSample();
        });
        panel.add(styleCBox);

        label = new JLabel("Weight");
        panel.add(label);

        weightCBox = new JComboBox<>(weights);
        weightCBox.setSelectedIndex(weightIndex);
        weightCBox.addActionListener(e -> {
            weightIndex = weightCBox.getSelectedIndex();
            showSample();
        });
        panel.add(weightCBox);

        label = new JLabel("Size");
        panel.add(label);

        sizeCBox = new JComboBox<>(sizes);
        sizeCBox.setSelectedIndex(sizeIndex);
        sizeCBox.addActionListener(e -> {
            sizeIndex = sizeCBox.getSelectedIndex();
            showSample();
        });
        panel.add(sizeCBox, "wrap");

        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setText(sampleText);
        textArea.setRows(sampleTextLines);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, "span, height 200, growx");

        /*
         * Apply and Cancel buttons
         */
        JButton btn = new JButton("Apply");
        btn.addActionListener(e -> {
            completed = true;
            createSelectedFont();
            setVisible(false);
        });
        panel.add(btn, "span, split 2, align right");

        btn = new JButton("Cancel");
        btn.addActionListener(e -> {
            selectedFont = null;
            setVisible(false);
        });
        panel.add(btn);

        getContentPane().add(panel);
        pack();
    }

    /** Set the controls to display the initial font */
    private void initFont() {
        for (Expression family : selectedFont.getFamily()) {
            String familyName = ((Literal) family).getValue().toString();
            for (int index = 0; index < families.length; index++) {
                if (families[index].equalsIgnoreCase(familyName)) {
                    // System.out.println("got family index: " + index);
                    familyIndex = index;
                    break;
                }
            }
        }

        String styleName = ((Literal) selectedFont.getStyle()).getValue().toString();
        for (int index = 0; index < styles.length; index++) {
            if (styles[index].equalsIgnoreCase(styleName)) {
                styleIndex = index;
                break;
            }
        }

        String weightName = ((Literal) selectedFont.getWeight()).getValue().toString();
        for (int index = 0; index < weights.length; index++) {
            if (weights[index].equalsIgnoreCase(weightName)) {
                weightIndex = index;
                break;
            }
        }

        int size = ((Number) ((Literal) selectedFont.getSize()).getValue()).intValue();
        sizeIndex = sizes.length - 1;
        for (int index = 0; index < sizes.length; index++) {
            if (sizes[index] >= size) {
                sizeIndex = index;
                break;
            }
        }
    }

    private void showSample() {
        StringBuilder sb = new StringBuilder(families[familyIndex]);

        if (weightIndex == 0) {
            if (styleIndex == 0) {
                sb.append("-PLAIN-");
            } else {
                sb.append("-ITALIC-");
            }
        } else {
            if (styleIndex == 0) {
                sb.append("-BOLD");
            } else {
                sb.append("-BOLDITALIC-");
            }
        }

        sb.append(sizes[sizeIndex]);

        java.awt.Font sampleFont = java.awt.Font.decode(sb.toString());
        textArea.setFont(sampleFont);
    }

    /** Create a new Font object to return as the selected font */
    private void createSelectedFont() {
        selectedFont = sf.createFont(
                ff.literal(families[familyIndex]),
                ff.literal(styles[styleIndex]),
                ff.literal(weights[weightIndex]),
                ff.literal(String.valueOf(sizes[sizeIndex])));
    }
}
