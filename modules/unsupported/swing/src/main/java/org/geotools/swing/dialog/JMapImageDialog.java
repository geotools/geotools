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

package org.geotools.swing.dialog;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.control.JIntegerField;
import org.geotools.swing.control.ValueChangedEvent;
import org.geotools.swing.control.ValueChangedListener;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;

/**
 * A dialog to prompt for the size of an image into which a map
 * will be drawn and the name and format of the file to save this
 * image to. It allows the image size to be set indirectly,
 * by setting the map pixel scale, or directly.
 *
 * @todo Add ability to work with DPI setting
 *
 * @author Michael Bedward
 * @since 2.6.1
 *
 * @source $URL$
 * @version $Id$
 */

public class JMapImageDialog extends JDialog {

    public static final int DEFAULT_IMAGE_WIDTH = 1000;

    private static final int WIDTH_FIELD = 0;
    private static final int HEIGHT_FIELD = 1;

    private JTextField fileField;
    private JComboBox scaleCombo;
    private JIntegerField widthField;
    private JIntegerField heightField;

    private boolean completed;
    private Rectangle imageSize;
    private boolean setByScale;
    private File selectedFile;
    private double heightToWidth;

    private ReferencedEnvelope mapBounds;
    private final int XAXIS;
    private final int YAXIS;

    /**
     * Creates a new dialog
     *
     * @param mapBounds the bounds (world coordinates) of the map area to
     *        draw into the image
     */
    public JMapImageDialog(ReferencedEnvelope mapBounds) {
        if (mapBounds == null) {
            throw new IllegalArgumentException("The mapBounds argument cannot be null");
        }
        
        setTitle("Output image size");
        setModal(true);
        setResizable(false);

        this.mapBounds = new ReferencedEnvelope(mapBounds);
        CoordinateReferenceSystem crs = mapBounds.getCoordinateReferenceSystem();
        boolean swapAxes = false;
        if (crs != null) {
            CoordinateSystem cs = crs.getCoordinateSystem();
            if (cs != null) {
                AxisDirection dir = cs.getAxis(0).getDirection().absolute();
                if (dir.equals(AxisDirection.NORTH) || dir.equals(AxisDirection.DISPLAY_UP)) {
                    swapAxes = true;
                }
            }
        }

        XAXIS = swapAxes ? 1 : 0;
        YAXIS = swapAxes ? 0 : 1;

        initComponents();
        initValues();
    }

    /**
     * Test if the dialog was completed
     *
     * @return true if the dialog was completed; false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Get the requested image bounds
     *
     * @return image bounds
     */
    public Rectangle getImageSize() {
        return imageSize;
    }

    /**
     * Get the file to save the map image to
     *
     * @return the selected file
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * Get the image file format as a file suffix understood
     * by {@code ImageIO}
     *
     * @return file suffix representing the image file format
     */
    public String getImageFormat() {
        String format = null;

        if (selectedFile != null) {
            int dot = selectedFile.getName().lastIndexOf('.');
            if (dot > 0) {
                format = selectedFile.getName().substring(dot + 1);
            }
        }

        return format;
    }

    /**
     * Create and lay out the dialog components
     */
    private void initComponents() {
        MigLayout layout = new MigLayout(
                "",
                "[][]30px[][]",        // col constraints
                "[][][][]30px[]");  // row constraints

        JPanel panel = new JPanel(layout);

        JLabel fileLabel = new JLabel("File");
        panel.add(fileLabel, "cell 0 0");

        fileField = new JTextField(20);
        panel.add(fileField, "cell 1 0 3 1");

        JButton browseBtn = new JButton("Browse...");
        browseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                brosweFile();
            }
        });
        panel.add(browseBtn, "cell 1 0");

        int[] baseScales = {10, 25, 50};
        int[] multipliers = {1, 10, 100, 1000, 10000, 100000};

        Object[] scales = new Object[baseScales.length * multipliers.length];
        for (int i = 0, k=0; i < multipliers.length; i++) {
            for (int j = 0; j < baseScales.length; j++, k++) {
                scales[k] = new Integer(multipliers[i] * baseScales[j]);
            }
        }

        final JCheckBox scaleCheck = new JCheckBox("Set by pixel scale", false);
        panel.add(scaleCheck, "cell 1 1");

        JLabel scaleLabel = new JLabel("Scale 1:");
        panel.add(scaleLabel, "cell 1 2, gapbefore indent");

        DefaultComboBoxModel scaleModel = new DefaultComboBoxModel(scales);
        scaleCombo = new JComboBox(scaleModel);
        scaleCombo.setEditable(true);
        scaleCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onScaleChange();
            }
        });
        panel.add(scaleCombo, "cell 1 2");

        final JCheckBox sizeCheck = new JCheckBox("Set by image size", true);
        panel.add(sizeCheck, "cell 2 1 2 1");

        JLabel widthLabel = new JLabel("width");
        panel.add(widthLabel, "cell 2 2, gapbefore indent");

        widthField = new JIntegerField();
        widthField.addValueChangedListener( new ValueChangedListener() {
            public void onValueChanged(ValueChangedEvent ev) {
                onImageSizeChange(WIDTH_FIELD);
            }
        } );

        final int fieldWidth = widthField.getFontMetrics(widthField.getFont()).stringWidth("00000000");
        panel.add(widthField, String.format("cell 3 2, w %d!", fieldWidth));

        JLabel heightLabel = new JLabel("height");
        panel.add(heightLabel, "cell 2 3, gapbefore indent");

        heightField = new JIntegerField();
        heightField.addValueChangedListener( new ValueChangedListener() {
            public void onValueChanged(ValueChangedEvent ev) {
                onImageSizeChange(HEIGHT_FIELD);
            }
        } );
        panel.add(heightField, String.format("cell 3 3, w %d!", fieldWidth));

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                completed = true;
                setVisible(false);
            }
        });
        panel.add(okBtn, "cell 1 4 3 1");

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                completed = false;
                setVisible(false);
            }
        });
        panel.add(cancelBtn, "cell 1 4");

        ButtonGroup grp = new ButtonGroup();
        grp.add(scaleCheck);
        grp.add(sizeCheck);


        scaleCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setByScale = scaleCheck.isSelected();
                enableItems();
            }
        });

        sizeCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setByScale = scaleCheck.isSelected();
                enableItems();
            }
        });

        setByScale = scaleCheck.isSelected();
        enableItems();

        getContentPane().add(panel);
        pack();
    }

    /**
     * Enable dialog controls for the current dialog mode:
     * setting map scale or setting image size directly.
     */
    private void enableItems() {
        scaleCombo.setEnabled(setByScale);

        widthField.setEnabled(!setByScale);
        heightField.setEnabled(!setByScale);
    }

    /**
     * Set initial default values for image size
     */
    private void initValues() {
        heightToWidth = mapBounds.getSpan(YAXIS) / mapBounds.getSpan(XAXIS);
        imageSize = new Rectangle();

        // this will cause the height and scale controls to be updated
        widthField.setValue(DEFAULT_IMAGE_WIDTH);
    }

    /**
     * Update controls when one of the image size fields has been edited
     *
     * @param controlModified constant indicating which field was edited
     */
    private void onImageSizeChange(int controlModified) {
        switch (controlModified) {
            case WIDTH_FIELD:
                imageSize.width = widthField.getValue();
                imageSize.height = (int) Math.round(imageSize.width * heightToWidth);
                heightField.setValue(imageSize.height, false);
                break;

            case HEIGHT_FIELD:
                imageSize.height = heightField.getValue();
                imageSize.width = (int) Math.round(imageSize.height / heightToWidth);
                widthField.setValue(imageSize.width, false);
                break;
        }

        int scaleDenominator = 0;
        if (imageSize.width > 0) {
            scaleDenominator = (int) Math.round(mapBounds.getSpan(XAXIS) / imageSize.width);
        }
        scaleCombo.getEditor().setItem(Integer.valueOf(scaleDenominator));
    }

    /**
     * Update the controls when the scale has been changed
     */
    private void onScaleChange() {
        int scaleDenominator = ((Number) scaleCombo.getEditor().getItem()).intValue();
        if (scaleDenominator > 0) {
            imageSize.width = (int) (mapBounds.getSpan(XAXIS) / scaleDenominator);
            imageSize.height = (int) (mapBounds.getSpan(YAXIS) / scaleDenominator);
        } else {
            imageSize.width = imageSize.height = 0;
        }

        widthField.setValue( imageSize.width, false );
        heightField.setValue( imageSize.height, false );
    }

    /**
     * Display a {@code JFileImageChooser} to browse for the path and file
     * to save the image to
     */
    private void brosweFile() {
        File file = JFileImageChooser.showSaveFile(this);
        if (file != null) {
            selectedFile = file;
            fileField.setText(file.getAbsolutePath());
        }
    }

    /**
     * Demonstrate the dialog
     * @param args ignored
     */
    public static void main(String[] args) {
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10000, 0, 20000, null);
        JMapImageDialog dialog = new JMapImageDialog(bounds);
        dialog.setVisible(true);
        dialog.dispose();
    }
}

