package org.geotools.image.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;

import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class CompareImageDialog extends JDialog {
    private static final long serialVersionUID = -8640087805737551918L;

    boolean accept = false;

    public CompareImageDialog(RenderedImage expected, RenderedImage actual, boolean showCommands) {
        JPanel content = new JPanel(new BorderLayout());
        this.setContentPane(content);
        this.setTitle("ImageAssert");
        final JLabel topLabel = new JLabel(
                "<html><body>PerceptualDiff thinks the two images are perceptibly different.</html></body>");
        topLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        content.add(topLabel, BorderLayout.NORTH);

        JPanel central = new JPanel(new GridLayout(1, 2));
        central.add(titledImagePanel("Expected", expected));
        central.add(titledImagePanel("Actual", actual));
        content.add(central);

        JPanel commands = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton accept = new JButton("Overwrite reference");
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CompareImageDialog.this.accept = true;
                CompareImageDialog.this.setVisible(false);
            }
        });
        JButton reject = new JButton("Images are different");
        reject.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CompareImageDialog.this.accept = false;
                CompareImageDialog.this.setVisible(false);
            }
        });
        commands.add(accept);
        commands.add(reject);
        commands.setVisible(showCommands);
        content.add(commands, BorderLayout.SOUTH);
        pack();
    }

    private Component titledImagePanel(String string, RenderedImage expected) {
        JPanel panel = new JPanel(new BorderLayout());
        final JLabel title = new JLabel(string);
        title.setAlignmentX(0.5f);
        title.setBorder(new LineBorder(Color.BLACK));
        panel.add(title, BorderLayout.NORTH);
        panel.add(new ScrollingImagePanel(expected, 400, 400), BorderLayout.CENTER);
        return panel;
    }

    public static boolean show(RenderedImage expected, RenderedImage actual, boolean showCommands) {
        CompareImageDialog dialog = new CompareImageDialog(expected, actual, showCommands);
        dialog.setModal(true);
        dialog.setVisible(true);

        return dialog.accept;
    }
    
}
