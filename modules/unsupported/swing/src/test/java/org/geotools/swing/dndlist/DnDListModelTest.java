/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dndlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.geotools.swing.control.DnDListModel;
import org.junit.Test;

public class DnDListModelTest {
    class Watcher implements ListDataListener {
        int changed = 0;

        int added = 0;

        int removed = 0;

        public void clear() {
            changed = 0;
            added = 0;
            removed = 0;
        }

        public void contentsChanged(ListDataEvent e) {
            changed++;
        }

        public void intervalAdded(ListDataEvent e) {
            added++;
        }

        public void intervalRemoved(ListDataEvent e) {
            removed++;
        }
    }

    @Test
    public void testDnDListModel() {
        DnDListModel<String> model = new DnDListModel<String>();
        assertNotNull(model);
    }

    @Test
    public void testSetNofifyListeners() {
        DnDListModel<String> model = new DnDListModel<String>();
        Watcher watcher = new Watcher();
        model.addListDataListener(watcher);

        model.setNofifyListeners(true);
        assertTrue(model.getNotifyListeners());
        model.addItem("one");
        assertEquals(1, watcher.added);

        model.setNofifyListeners(false);
        assertFalse(model.getNotifyListeners());
        model.addItem("two");
        assertEquals(1, watcher.added);
    }

    @Test
    public void testGetSize() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        assertEquals(2, model.getSize());
    }

    @Test
    public void testGetElementAt() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        assertEquals("one", model.getElementAt(0));
    }

    @Test
    public void testGetElementsAtIntArray() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        assertEquals(Arrays.asList(new String[] { "one", "two", "one" }), model
                .getElementsAt(new int[] { 0, 1, 0 }));
    }

    @Test
    public void testGetElementsAtCollectionOfInteger() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        List<Integer> indexs = new ArrayList<Integer>();
        indexs.add(0);
        indexs.add(1);
        indexs.add(0);

        assertEquals(Arrays.asList(new String[] { "one", "two", "one" }), model
                .getElementsAt(indexs));
    }

    @Test
    public void testAddItemsTArray() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItems(new String[] { "one", "two" });

        assertEquals("one", model.getElementAt(0));
        assertEquals("two", model.getElementAt(1));
    }

    @Test
    public void testAddItemsCollectionOfT() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItems(Arrays.asList(new String[] { "one", "two" }));

        assertEquals("one", model.getElementAt(0));
        assertEquals("two", model.getElementAt(1));
    }

    @Test
    public void testInsertItem() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        model.insertItem(0, "zero");
        assertEquals("zero", model.getElementAt(0));
        assertEquals("one", model.getElementAt(1));
        assertEquals("two", model.getElementAt(2));
    }

    @Test
    public void testInsertItemsIntTArray() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        model.insertItems(0, Arrays.asList(new String[] { "zero" }));
        assertEquals("zero", model.getElementAt(0));
        assertEquals("one", model.getElementAt(1));
        assertEquals("two", model.getElementAt(2));
    }

    @Test
    public void testInsertItemsIntCollectionOfT() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        model.insertItems(0, new String[] { "zero" });
        assertEquals("zero", model.getElementAt(0));
        assertEquals("one", model.getElementAt(1));
        assertEquals("two", model.getElementAt(2));
    }

    @Test
    public void testMoveItems() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");
        model.addItem("three");
        model.moveItems(1, new int[] { 0 });

        assertEquals("two", model.getElementAt(0));
        assertEquals("one", model.getElementAt(1));
        assertEquals("three", model.getElementAt(2));
    }

    @Test
    public void testRemoveAt() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");
        model.addItem("three");
        model.removeAt(1);

        assertEquals("one", model.getElementAt(0));
        assertEquals("three", model.getElementAt(1));
    }

    @Test
    public void testRemoveItem() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");
        model.addItem("three");
        model.removeItem("two");

        assertEquals("one", model.getElementAt(0));
        assertEquals("three", model.getElementAt(1));
    }

    @Test
    public void testClear() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        model.clear();
        assertEquals(0, model.getSize());
    }

    @Test
    public void testContains() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        assertTrue(model.contains("one"));
        assertFalse(model.contains("three"));
    }

    @Test
    public void testIndexOf() {
        DnDListModel<String> model = new DnDListModel<String>();
        model.addItem("one");
        model.addItem("two");

        assertEquals(0,model.indexOf("one"));
        assertEquals(-1,model.indexOf("three"));
    }

}
