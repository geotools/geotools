/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.Attribute;

/**
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 */
public class PathAttributeList {
   
private Map<String, List<Pair>> elements = new HashMap<String, List<Pair>>();

public void put(String key, String xpath, Attribute attribute) {
    List<Pair> ls = null;
    if(elements.containsKey(key)) {
       ls = elements.get(key); 
    } else {
        ls = new ArrayList<Pair>();
        elements.put(key, ls);
    }
    ls.add(new Pair(xpath, attribute));
}

public List<Pair> get(String key) {
    return elements.get(key);
}

public class Pair {
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
