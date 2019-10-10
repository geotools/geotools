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
 *
 */

package org.geotools.kml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FolderStack implements Iterable<Folder> {

    private final List<Folder> stack;

    public FolderStack() {
        stack = new ArrayList<Folder>();
    }

    public void push(Folder folder) {
        stack.add(folder);
    }

    private boolean elementsExist() {
        return stack.size() > 0;
    }

    public Folder pop() {
        return elementsExist() ? stack.remove(lastElementIndex()) : null;
    }

    private int lastElementIndex() {
        return stack.size() - 1;
    }

    public Folder peek() {
        return elementsExist() ? stack.get(lastElementIndex()) : null;
    }

    @Override
    public Iterator<Folder> iterator() {
        return stack.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Folder folder : this) {
            String name = folder.getName();
            if (name != null) {
                String trimmedName = name.trim();
                if (trimmedName.length() > 0) {
                    sb.append(sb.length() > 0 ? " -> " + trimmedName : trimmedName);
                }
            }
        }
        return sb.toString();
    }

    public List<Folder> asList() {
        return new ArrayList<Folder>(stack);
    }
}
