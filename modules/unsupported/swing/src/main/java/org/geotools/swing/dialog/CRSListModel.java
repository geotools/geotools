/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractListModel;

import org.geotools.referencing.ReferencingFactoryFinder;

import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * List model class for {@code JCRSChooser}. Supports filtering
 * by case-insensitive sub-string matching.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: $
 * @version $Id: $
 */
public class CRSListModel extends AbstractListModel {
    private static class Item {
        String code;
        String desc;
        boolean visible;

        Item(String code, String desc) {
            this.code = code;
            this.desc = desc;
            visible = true;
        }

        @Override
        public String toString() {
            return code + ": " + desc;
        }
    }

    private List<Item> allItems = new ArrayList<Item>();
    private List<Item> filterItems = new ArrayList<Item>();
    
    /**
     * Constructor. Populates the model with available reference systems
     * for the specified authority. If {@code authority} is {@code null}
     * or empty, it defaults to {@link JCRSChooser#DEFAULT_AUTHORITY}.
     * 
     * @param authority the authority name
     * @param showDefaults show GeoTools default reference systems
     */
    public CRSListModel(String authority) {
        try {
            CRSAuthorityFactory fac = 
                    ReferencingFactoryFinder.getCRSAuthorityFactory(authority, null);

            Set<String> codes = fac.getAuthorityCodes(CoordinateReferenceSystem.class);
            
            if (authority == null || authority.trim().length() == 0) {
                authority = JCRSChooser.DEFAULT_AUTHORITY;
            }
            
            for (String code : codes) {
                code = code.trim();
                String desc = fac.getDescriptionText(authority + ":" + code).toString();
                allItems.add(new Item(code, desc));
            }
            filterItems.addAll(allItems);
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * {@inheritDoc}
     * 
     * @return the length of the list with the current filter applied
     */
    @Override
    public int getSize() {
        return filterItems.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @return a {@code String} of the form {@code reference code: description}
     */
    @Override
    public String getElementAt(int i) {
        return filterItems.get(i).toString();
    }
    
    /**
     * Filters the model items by searching for the given sub-string.
     * Case is ignored for matching.
     * 
     * @param subStr sub-string to filter on; or {@code null} or 
     *     empty string for no filtering
     */
    public void setFilter(String subStr) {
        filterItems.clear();
        
        if (subStr == null || subStr.trim().length() == 0) {
            filterItems.addAll(allItems);
            
        } else {
            String lo = subStr.toLowerCase();

            for (Item item : allItems) {
                if (item.code.toLowerCase().contains(lo) || item.desc.toLowerCase().contains(lo)) {
                    filterItems.add(item);
                }
            }
        }

        fireContentsChanged(this, 0, getSize());
    }
    
    /**
     * Gets the code for the given element index.
     * 
     * @param i the index
     * @return the code
     */
    public String getCodeAt(int i) {
        return filterItems.get(i).code;
    }

    /**
     * Searches for the element with the given code. The search
     * is undertaken on the filtered items.
     * 
     * @param code the code to match; may be {@code null} or empty in which
     *     case -1 is returned
     * 
     * @return index of the matching element or -1 if not found.
     */
    public int findCode(String code) {
        String searchCode = code == null ? null : code.trim();
        
        if (searchCode != null && searchCode.length() > 0) {
            for (int i = 0; i < filterItems.size(); i++) {
                if (filterItems.get(i).code.equalsIgnoreCase(code)) {
                    return i;
                }
            }
        }

        return -1;
    }
}
