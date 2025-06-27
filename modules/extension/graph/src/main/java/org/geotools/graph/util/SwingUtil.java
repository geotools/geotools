/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SwingUtil {

    public static ListModel toListModel(final List elements) {
        return new AbstractListModel() {
            @Override
            public int getSize() {
                return elements.size();
            }

            @Override
            public Object getElementAt(int index) {
                return elements.get(index);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static ListModel toListModel(Collection elements) {
        return toListModel(new ArrayList<>(elements));
    }

    @SuppressWarnings("unchecked")
    public static List toList(ListModel model) {
        ArrayList list = new ArrayList<>(model.getSize());
        for (int i = 0; i < model.getSize(); i++) {
            list.add(model.getElementAt(i));
        }

        return list;
    }

    public static void setSelection(JList list, Object element) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            Object value = list.getModel().getElementAt(i);
            if (value == element) {
                list.setSelectedIndex(i);
                list.scrollRectToVisible(list.getCellBounds(i, i));
                return;
            }
        }
    }

    public static void addDoubleClickEvent(JList list) {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList source = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    ListSelectionListener[] listeners = source.getListSelectionListeners();
                    for (ListSelectionListener listener : listeners) {
                        listener.valueChanged(new ListSelectionEvent(
                                source, source.getSelectedIndex(), source.getSelectedIndex(), false));
                    }
                }
            }
        });
    }
}
