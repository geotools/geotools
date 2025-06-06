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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.RenderedImage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ReferenceImageDialog extends JDialog {
    private static final long serialVersionUID = -8640087805737551918L;

    boolean accept = false;

    public ReferenceImageDialog(RenderedImage image) {
        JPanel content = new JPanel(new BorderLayout());
        this.setContentPane(content);
        this.setTitle("ImageAssert");
        final JLabel topLabel = new JLabel("<html><body>Reference image file is missing.<br>"
                + "This is the result, do you want to make it the referecence?</html></body>");
        content.add(topLabel, BorderLayout.NORTH);
        ImageViewer viewer = new ImageViewer();
        viewer.setImage(image);
        viewer.setMinimumSize(new Dimension(400, 400));
        content.add(viewer);
        JPanel commands = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton accept = new JButton("Accept as reference");
        accept.addActionListener(e -> {
            this.accept = true;
            setVisible(false);
        });
        JButton reject = new JButton("Reject output");
        reject.addActionListener(e -> {
            this.accept = false;
            setVisible(false);
        });
        commands.add(accept);
        commands.add(reject);
        content.add(commands, BorderLayout.SOUTH);
        pack();
    }

    public static boolean show(RenderedImage ri) {
        ReferenceImageDialog dialog = new ReferenceImageDialog(ri);
        dialog.setModal(true);
        dialog.setVisible(true);

        return dialog.accept;
    }
}
