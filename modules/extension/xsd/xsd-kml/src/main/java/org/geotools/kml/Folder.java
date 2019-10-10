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

/** Container for folder name hierarchies */
public class Folder {

    private String name;

    public Folder() {
        this(null);
    }

    public Folder(String name) {
        this.name = name;
    }

    /**
     * Return the folder's name.
     *
     * @return folder's name. Can be null.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the folder's name
     *
     * @param name folder's name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Folder (name=" + name + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Folder)) {
            return false;
        }
        Folder that = (Folder) obj;
        if (name == null) {
            return that.name == null;
        }
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
}
