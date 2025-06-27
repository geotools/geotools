/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.Attribute;

/**
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 */
public class PathAttributeList {

    private Map<String, List<Pair>> elements = new HashMap<>();

    private Map<String, String> labelToXpath = new HashMap<>();

    /**
     * Store information for labelled attributes.
     *
     * @param key AttributeMapping label
     * @param xpath full input xpath from web service including itemXpath + instanceXpath
     * @param attribute Attribute instance that is created for the AttributeMapping
     */
    public void put(String key, String xpath, Attribute attribute) {
        List<Pair> ls = null;
        if (elements.containsKey(key)) {
            ls = elements.get(key);
        } else {
            ls = new ArrayList<>();
            elements.put(key, ls);
            labelToXpath.put(key, xpath);
        }
        ls.add(new Pair(xpath, attribute));
    }

    /**
     * Get full input xpath based on the label.
     *
     * @param label AttributeMapping label
     * @return full input xpath from web service including itemXpath + instanceXpath
     */
    public String getPath(String label) {
        return labelToXpath.get(label);
    }

    /**
     * Return list of matching source input xpath - Attribute pair based on the label.
     *
     * @param key The attribute label
     * @return full input xpath - Attribute pair
     */
    public List<Pair> get(String key) {
        return elements.get(key);
    }

    public static class Pair {
        private String xpath;
        private Attribute attribute;

        public Pair(String xpath, Attribute attribute) {
            this.xpath = xpath;
            this.attribute = attribute;
        }

        public String getXpath() {
            return xpath;
        }

        public Attribute getAttribute() {
            return attribute;
        }
    }
}
