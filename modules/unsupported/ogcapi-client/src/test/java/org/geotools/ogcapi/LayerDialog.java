/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ogcapi;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.geotools.util.logging.Logging;

public class LayerDialog extends JDialog {
    /** serialVersionUID */
    private static final long serialVersionUID = -7331660512351526439L;

    static final Logger LOGGER = Logging.getLogger(LayerDialog.class);
    static LayerDialog thedialog;

    JPanel panel0;

    JLabel label0;

    JComboBox<String> combo0;

    JComboBox<String> combo1;

    JButton ok;

    JButton cancel;
    String[] layers = {"Please wait until layers are loaded"};
    private String layer = "";
    private String style = "";
    private Map<String, CollectionType> collections;

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }

        thedialog = new LayerDialog();
        thedialog.setVisible(true);
    }

    public LayerDialog(QuickTileViewer quickTileViewer, String title) {
        super(quickTileViewer.frame, title, true);

        init();
    }

    /** */
    public LayerDialog() {
        init();
    }

    public void updateLayers(CollectionsType collections1) {
        if (collections1 == null || collections1.collections == null) return;
        this.collections = collections1.collections;
        layers = this.collections.keySet().toArray(new String[] {});
        Arrays.sort(layers);
        combo0.removeAllItems();
        for (String l : layers) {
            combo0.addItem(l);
        }
    }

    private void updateStyles(CollectionType c) {
        combo1.removeAllItems();
        for (StyleType s : c.styles) {
            combo1.addItem(s.getIdentifier());
        }
    }

    /** */
    private void init() {
        setSize(200, 200);
        panel0 = new JPanel();
        GridBagLayout gbpanel0 = new GridBagLayout();
        GridBagConstraints gbcpanel0 = new GridBagConstraints();
        panel0.setLayout(gbpanel0);

        label0 = new JLabel("Choose Layer");
        gbcpanel0.gridx = 1;
        gbcpanel0.gridy = 0;
        gbcpanel0.gridwidth = 18;
        gbcpanel0.gridheight = 1;
        gbcpanel0.fill = GridBagConstraints.BOTH;
        gbcpanel0.weightx = 1;
        gbcpanel0.weighty = 1;
        gbcpanel0.anchor = GridBagConstraints.NORTH;
        gbpanel0.setConstraints(label0, gbcpanel0);
        panel0.add(label0);

        combo0 = new JComboBox<>(layers);
        combo0.addItemListener(new ItemChangeListener());
        gbcpanel0.gridx = 1;
        gbcpanel0.gridy = 2;
        gbcpanel0.gridwidth = 20;
        gbcpanel0.gridheight = 2;
        gbcpanel0.fill = GridBagConstraints.BOTH;
        gbcpanel0.weightx = 1;
        gbcpanel0.weighty = 0;
        gbcpanel0.anchor = GridBagConstraints.NORTH;
        gbpanel0.setConstraints(combo0, gbcpanel0);
        panel0.add(combo0);

        String[] styles = {"Please select a layer first"};
        combo1 = new JComboBox<>(styles);
        gbcpanel0.gridx = 1;
        gbcpanel0.gridy = 9;
        gbcpanel0.gridwidth = 20;
        gbcpanel0.gridheight = 2;
        gbcpanel0.fill = GridBagConstraints.BOTH;
        gbcpanel0.weightx = 1;
        gbcpanel0.weighty = 0;
        gbcpanel0.anchor = GridBagConstraints.NORTH;
        gbpanel0.setConstraints(combo1, gbcpanel0);
        panel0.add(combo1);

        ok = new JButton("OK");
        gbcpanel0.gridx = 2;
        gbcpanel0.gridy = 16;
        gbcpanel0.gridwidth = 5;
        gbcpanel0.gridheight = 3;
        gbcpanel0.fill = GridBagConstraints.BOTH;
        gbcpanel0.weightx = 1;
        gbcpanel0.weighty = 0;
        gbcpanel0.anchor = GridBagConstraints.NORTH;
        gbpanel0.setConstraints(ok, gbcpanel0);
        panel0.add(ok);
        ok.addActionListener(
                e -> {
                    setLayer((String) combo0.getSelectedItem());
                    setStyle((String) combo0.getSelectedItem());
                    setVisible(false);
                });

        cancel = new JButton("Cancel");
        cancel.addActionListener(
                e -> {
                    setLayer(null);
                    setStyle(null);
                    dispose();
                });
        gbcpanel0.gridx = 13;
        gbcpanel0.gridy = 16;
        gbcpanel0.gridwidth = 5;
        gbcpanel0.gridheight = 3;
        gbcpanel0.fill = GridBagConstraints.BOTH;
        gbcpanel0.weightx = 1;
        gbcpanel0.weighty = 0;
        gbcpanel0.anchor = GridBagConstraints.NORTH;
        gbpanel0.setConstraints(cancel, gbcpanel0);
        panel0.add(cancel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setContentPane(panel0);
        pack();
    }
    /** @return the layer */
    public String getLayer() {
        return layer;
    }

    /** @param layer the layer to set */
    public void setLayer(String layer) {
        this.layer = layer;
    }
    /** @return the style */
    public String getStyle() {
        return style;
    }

    /** @param style the style to set */
    public void setStyle(String style) {
        this.style = style;
    }

    class ItemChangeListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) event.getItem();
                updateStyles(collections.get(item));
            }
        }
    }
}
