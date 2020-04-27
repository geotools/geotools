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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CollectionType {
    String identifier;
    String title;
    String description;
    ReferencedEnvelope extent = new ReferencedEnvelope();
    ArrayList<Link> links = new ArrayList<>();
    ArrayList<CoordinateReferenceSystem> crs = new ArrayList<>(0); // not
    // expecting
    // any of these
    // here
    ArrayList<StyleType> styles = new ArrayList<>();

    public void addStyle(StyleType style) {
        try {
            styles.add(style);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReferencedEnvelope getExtent() {
        return extent;
    }

    public void setExtent(ReferencedEnvelope extent) {
        this.extent = extent;
    }

    @Override
    public String toString() {
        return "CollectionType [identifier="
                + identifier
                + ", title="
                + title
                + ", description="
                + description
                + ", styles="
                + styles
                + ", extent="
                + extent
                + "]";
    }
}
