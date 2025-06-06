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

package org.geotools.swing.data;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import net.miginfocom.swing.MigLayout;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.swing.wizard.JPage;

/** A wizard page that will allow the user to choose a format (ie DataAccess factory). */
public class JDataChoosePage extends JPage {
    /** Factory for which we are collection connection parameters */
    protected DataStoreFactorySpi format;

    private JList<DataStoreFactorySpi> list;

    public JDataChoosePage() {
        this(null);
    }

    public JDataChoosePage(DataStoreFactorySpi format) {
        this.format = format;
    }

    @Override
    public JPanel createPanel() {
        final JPanel page = super.createPanel();
        page.setLayout(new MigLayout());
        JLabel title = new JLabel("Choose DataStore");
        Font titleFont = new Font("Arial", Font.BOLD, 14);
        title.setFont(titleFont);
        page.add(title, "span");
        JLabel description = new JLabel("Available DataStores on your classpath");
        page.add(description, "grow, span");

        java.util.List<DataStoreFactorySpi> factoryList = new ArrayList<>();
        for (Iterator<DataStoreFactorySpi> iter = DataStoreFinder.getAvailableDataStores(); iter.hasNext(); ) {
            factoryList.add(iter.next());
        }
        list = new JList<>(factoryList.toArray(new DataStoreFactorySpi[factoryList.size()]));
        ListCellRenderer<Object> cellRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                DataStoreFactorySpi factory = (DataStoreFactorySpi) value;
                setText(factory.getDisplayName());
                setToolTipText(factory.getDescription());

                return this;
            }
        };
        list.setCellRenderer(cellRenderer);
        list.addListSelectionListener(e -> format = list.getSelectedValue());
        JScrollPane scroll = new JScrollPane(list);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(300, 100));
        page.add(scroll, "growx,growy,span");

        return page;
    }

    @Override
    public void preDisplayPanel() {
        list.addListSelectionListener(getJWizard().getController());
    }

    @Override
    public void preClosePanel() {
        list.addListSelectionListener(getJWizard().getController());

        JDataStoreWizard dataStoreWizard = (JDataStoreWizard) getJWizard();
        dataStoreWizard.setFormat(format);
    }

    @Override
    public boolean isValid() {
        return format != null;
    }
}
