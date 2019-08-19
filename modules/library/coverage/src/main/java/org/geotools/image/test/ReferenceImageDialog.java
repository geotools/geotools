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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ReferenceImageDialog extends JDialog {
    private static final long serialVersionUID = -8640087805737551918L;

    boolean accept = false;

    @SuppressWarnings("deprecation")
    public ReferenceImageDialog(RenderedImage image) {
        JPanel content = new JPanel(new BorderLayout());
        this.setContentPane(content);
        this.setTitle("ImageAssert");
        final JLabel topLabel =
                new JLabel(
                        "<html><body>Reference image file is missing.<br>"
                                + "This is the result, do you want to make it the referecence?</html></body>");
        content.add(topLabel, BorderLayout.NORTH);
        content.add(new javax.media.jai.widget.ScrollingImagePanel(image, 400, 400));
        JPanel commands = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton accept = new JButton("Accept as reference");
        accept.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ReferenceImageDialog.this.accept = true;
                        ReferenceImageDialog.this.setVisible(false);
                    }
                });
        JButton reject = new JButton("Reject output");
        reject.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        ReferenceImageDialog.this.accept = false;
                        ReferenceImageDialog.this.setVisible(false);
                    }
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
