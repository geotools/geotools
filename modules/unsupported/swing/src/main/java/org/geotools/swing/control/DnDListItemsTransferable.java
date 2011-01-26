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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements the Transferable interface to carry list item data
 * during drag and drop actions. This class is used by DnDList.
 * Client code will not normally need to refer to it.
 *
 * @see DnDList
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class DnDListItemsTransferable<T> implements Transferable {

    private List<T> items;

    /**
     * Constructor
     * @param stuff a Collection of list item data
     */
    public DnDListItemsTransferable(Collection<T> stuff) {
        super();
        items = new ArrayList<T>();
        items.addAll(stuff);
    }

    /**
     * Description copies from interface:<br>
     * Returns an array of DataFlavor objects indicating the flavors
     * the data can be provided in. The array should be ordered according
     * to preference for providing the data (from most richly descriptive
     * to least descriptive).
     */
    public DataFlavor[] getTransferDataFlavors() {
        // @todo WRITE ME !
        return null;
    }

    /**
     * Description copies from interface:<br>
     * Returns whether or not the specified data flavor is supported for this object.
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // @todo WRITE ME !
        return true;
    }

    /**
     * Returns a List of items to be transferred.
     *
     * @param flavor - required by the interface but ignored here
     *
     * @throws IOException if the data is no longer available in the requested flavor.
     * @throws UnsupportedFlavorException if the requested data flavor is not supported.
     */
    public List<T> getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return items;
    }
}
