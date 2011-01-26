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
package org.geotools.data.shapefile.indexed.attribute;

/**
 * Record stored in attribute index file
 * 
 * @author Manuele Ventoruzzo
 *
 * @source $URL$
 */
public class IndexRecord implements Comparable {

    private Comparable attribute;
    private long featureID;

    public IndexRecord(Comparable attribute, long featureID) {
        this.attribute = attribute;
        this.featureID = featureID;
    }

    public Object getAttribute() {
        return attribute;
    }

    public long getFeatureID() {
        return featureID;
    }

    public int compareTo(Object o) {
        if (o instanceof IndexRecord) {
            return attribute.compareTo(((IndexRecord) o).getAttribute());
        }
        if (attribute.getClass().isInstance(o)) {
            // compare just attribute with o
            return attribute.compareTo(o);
        }
        throw new ClassCastException("Object " + o.toString() + " is not of Record type");
    }

    public String toString() {
        return "(" + attribute.toString() + "," + featureID + ")";
    }
    }
