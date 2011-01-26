/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.indexed;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

import org.geotools.index.CloseableCollection;
import org.geotools.index.Data;

/**
 * Currently just wraps ArrayList and delegates to that class
 * @author jesse
 *
 * @source $URL$
 */
public class CloseableArrayList extends AbstractList<Data> implements
        CloseableCollection<Data> {

    private final ArrayList<Data> container ;

    public CloseableArrayList(int length) {
        container = new ArrayList<Data>(length);
    }

    public CloseableArrayList() {
        container = new ArrayList<Data>();
    }

    @Override
    public Data get(int index) {
        return container.get(index);
    }

    @Override
    public int size() {
        return container.size();
    }

    
    public void close() throws IOException {
        // do nothing
        
    }

    public boolean add(Data o) {
        return container.add(o);
    }

    public void closeIterator( Iterator<Data> iter ) throws IOException {
        // do nothing
    }
    
}
