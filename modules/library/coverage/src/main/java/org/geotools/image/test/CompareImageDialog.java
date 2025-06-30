/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.test;

import it.geosolutions.rendered.viewer.ImageViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import org.geotools.util.logging.Logging;

class CompareImageDialog extends JDialog {

    private static final long serialVersionUID = -8640087805737551918L;

    static final Logger LOGGER = Logging.getLogger(CompareImageDialog.class);

    boolean accept = false;

    public CompareImageDialog(RenderedImage expected, RenderedImage actual, boolean showCommands) {
        JPanel content = new JPanel(new BorderLayout());
        this.setContentPane(content);
        this.setTitle("ImageAssert");
        String message;
        if (expected.getWidth() != actual.getWidth() || expected.getHeight() != actual.getHeight()) {
            message = "Image sizes are different, expected "
                    + expected.getWidth()
                    + "x"
                    + expected.getHeight()
                    + " but actual is "
                    + actual.getWidth()
                    + "x"
                    + actual.getHeight();
        } else {
            message = "The two images are perceptibly different.";
        }
        final JLabel topLabel = new JLabel("<html><body>" + message + "</html></body>");
        topLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        content.add(topLabel, BorderLayout.NORTH);

        JPanel central = new JPanel(new GridLayout(1, 2));
        central.add(titledImagePanel("Expected", expected));
        central.add(titledImagePanel("Actual", actual));
        content.add(central);

        JPanel commands = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton accept = new JButton("Overwrite reference");
        accept.addActionListener(e -> {
            this.accept = true;
            setVisible(false);
        });
        JButton reject = new JButton("Images are different");
        reject.addActionListener(e -> {
            this.accept = false;
            setVisible(false);
        });
        JButton save = new JButton("Save comparison");
        save.addActionListener(e -> {
            {
                // File location = getStartupLocation();
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File file) {
                        return file.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "Directories (will save a expected.png and actual.png there)";
                    }
                });

                int result = chooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selected = chooser.getSelectedFile();
                    try {
                        ImageIO.write(expected, "PNG", new File(selected, "expected.png"));
                        ImageIO.write(actual, "PNG", new File(selected, "actual.png"));
                    } catch (IOException e1) {
                        LOGGER.log(Level.WARNING, "Failed to save images", e);
                    }
                }
            }
        });
        commands.add(accept);
        commands.add(reject);
        commands.add(save);
        commands.setVisible(showCommands);
        content.add(commands, BorderLayout.SOUTH);
        pack();
    }

    private Component titledImagePanel(String string, RenderedImage image) {
        JPanel panel = new JPanel(new BorderLayout());
        final JLabel title = new JLabel(string);
        title.setAlignmentX(0.5f);
        title.setBorder(new LineBorder(Color.BLACK));
        panel.add(title, BorderLayout.NORTH);
        ImageViewer viewer = new ImageViewer();
        viewer.setImage(image);
        panel.add(viewer, BorderLayout.CENTER);
        return panel;
    }

    public static boolean show(RenderedImage expected, RenderedImage actual, boolean showCommands) {
        CompareImageDialog dialog = new CompareImageDialog(expected, actual, showCommands);
        dialog.setModal(true);
        dialog.setVisible(true);

        return dialog.accept;
    }
}
