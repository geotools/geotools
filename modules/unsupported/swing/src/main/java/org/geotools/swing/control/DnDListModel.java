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
package org.geotools.swing.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractListModel;

/**
 * A generic ListModel class to support {@linkplain DnDList}.
 * <p>
 * The DnDListModel acts as a wrapper around an internal list of
 * items; providing notification as the items are changed.
 * </p>
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class DnDListModel<T> extends AbstractListModel {
    private static final long serialVersionUID = -6110074993686576005L;
    List<T> items;
    private boolean notify;

    /**
     * Default constructor
     */
    public DnDListModel() {
        items = new ArrayList<T>();
        notify = true;
    }

    public void setNofifyListeners(boolean notify) {
        this.notify = notify;
    }

    public boolean getNotifyListeners() {
        return notify;
    }

    public int getSize() {
        return items.size();
    }

    /**
     * Get the list item at the specified index.
     * <p>
     * Note: this method returns a live reference.
     *
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public T getElementAt(int index) {
        return items.get(index);
    }

    /**
     * Returns a list of the items at the specified indices. 
     * <p>
     * Note: The returned List contains live references.
     *
     * @throws IndexOutOfBoundsException if any of the indices are invalid
     */
    public List<T> getElementsAt(int[] indices) {
        List<T> refs = new ArrayList<T>();
        for (int k = 0; k < indices.length; k++) {
            refs.add(items.get(indices[k]));
        }

        return refs;
    }

    /**
     * Returns a list of the items at the indices specified in the Collection.
     * <p>
     * Note: The returned List contains live references.
     *
     * @throws IndexOutOfBoundsException if any of the indices are invalid
     */
    public List<T> getElementsAt(Collection<Integer> indices) {
        List<T> refs = new ArrayList<T>();
        for (Integer index : indices) {
            refs.add(items.get(index));
        }

        return refs;
    }

    /**
     * Append a new item to the end of the list of current items
     */
    public void addItem(T newItem) {
        int index = items.size();
        items.add(newItem);

        if (notify) {
            fireIntervalAdded(this, index, index);
        }
    }

    /**
     * Add new items to the end of the list of current items
     */
    public void addItems(T[] newItems) {
        if (newItems.length > 0) {
            int index0 = items.size();

            for (T item : newItems) {
                items.add(item);
            }

            if (notify) {
                fireIntervalAdded(this, index0, index0 + newItems.length - 1);
            }
        }
    }

    /**
     * Add new items to the end of the list of current items
     */
    public void addItems(Collection<T> newItems) {
        if (!newItems.isEmpty()) {
            int index0 = items.size();

            for (T item : newItems) {
                items.add(item);
            }

            if (notify) {
                fireIntervalAdded(this, index0, index0 + newItems.size() - 1);
            }
        }
    }

    /**
     * Insert an item into the list at the specified position.
     * @param destIndex the position of the new item: if &lt; 0 the item will
     * be inserted at the start of the list; if &gt;= the current list size
     * the item will be appended to the end of the list
     */
    public void insertItem(int destIndex, T newItem) {
        if (destIndex < 0) {
            destIndex = 0;
        } else if (destIndex >= getSize()) {
            addItem(newItem);
            return;
        }

        items.add(destIndex, newItem);

        if (notify) {
            fireIntervalAdded(this, destIndex, destIndex);
        }
    }

    /**
     * Insert new items into the list at the specified position.
     *
     * @param destIndex the position of the new item: if &lt; 0 the items will
     * be inserted at the start of the list; if &gt;= the current list size
     * the items will be appended to the end of the list
     */
    public void insertItems(int destIndex, T[] newItems) {
        insertItems(destIndex, Arrays.asList(newItems));
    }

    /**
     * Insert new items into the list at the specified position.
     *
     * @param destIndex the position of the new item: if &lt; 0 the items will
     * be inserted at the start of the list; if &gt;= the current list size
     * the items will be appended to the end of the list
     */
    public void insertItems(int destIndex, Collection<T> newItems) {
        if (destIndex < 0) {
            destIndex = 0;
        } else if (destIndex >= getSize()) {
            addItems(newItems);
            return;
        }

        items.addAll(destIndex, newItems);

        if (notify) {
            fireIntervalAdded(this, destIndex, destIndex + newItems.size() - 1);
        }
    }

    /**
     * Move the items currently positioned at the indices in the
     * {@code srcIndices} array as block such that they are inserted
     * into the list at {@code destIndex}. It is <b>assumed</b> that
     * srcIndices is sorted in ascending order.
     */
    public void moveItems(int destIndex, int[] srcIndices) {
        if (srcIndices.length > 0) {
            List<T> copies = getElementsAt(srcIndices);

            int minIndex = Math.min(destIndex, srcIndices[0]);
            int maxIndex = Math.max(destIndex - 1, srcIndices[srcIndices.length - 1]);

            for (int k = srcIndices.length - 1; k >= 0; k--) {
                items.remove(srcIndices[k]);
            }

            notify = false;
            insertItems(destIndex, copies);
            this.fireContentsChanged(this, minIndex, maxIndex);
            notify = true;
        }
    }

    /**
     * Remove the item at the specified index
     */
    public void removeAt(int index) {
        items.remove(index);
        if (notify) {
            fireIntervalRemoved(this, index, index);
        }
    }

    /**
     * Removes the first instance of the specified item if it is contained in the list
     */
    public void removeItem(T item) {
        ListIterator<T> iter = items.listIterator();
        int index = 0;
        boolean found = false;

        while (iter.hasNext()) {
            T anItem = iter.next();
            if (anItem.equals(item)) {
                iter.remove();
                found = true;
                break;
            }

            index++;
        }

        if (found && notify) {
            this.fireIntervalRemoved(this, index, index);
        }
    }

    /**
     * Remove all items from the list
     */
    public void clear() {
        int prevSize = items.size();
        items.clear();
        if (notify) {
            this.fireIntervalRemoved(this, 0, prevSize);
        }
    }

    /**
     * Query whether this list contains the specified item
     */
    public boolean contains(T item) {
        return items.contains(item);
    }

    /**
     * Get the (first) index of the given item in the list of items held by this model.
     *
     * @param item the item to search for
     * @return the index or -1 if the item is not present
     */
    public int indexOf(T item) {
        return items.indexOf(item);
    }

    @Override
    public String toString() {
        return "DnD:"+items;
    }
}
