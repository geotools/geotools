/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ogcapi;

import java.util.ArrayList;

public class TileMatrixSet extends org.geotools.ows.wmts.model.TileMatrixSet {
    private String title;

    private String supportedCRS;
    private ArrayList<Link> links = new ArrayList<>();

    /**
     * Get the links list
     *
     * @return the list of links
     */
    public ArrayList<Link> getLinks() {
        return links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSupportedCRS(String supportedCRS) {
        this.supportedCRS = supportedCRS;
        this.setCoordinateReferenceSystem(OgcApiUtils.parseCRS(supportedCRS));
    }

    @Override
    public String toString() {

        String name;
        if (getCoordinateReferenceSystem() != null) {
            name = getCoordinateReferenceSystem().getName().toString();
        } else {
            name = supportedCRS;
        }

        return getIdentifier() + ", " + name + ", " + title;
    }
}
