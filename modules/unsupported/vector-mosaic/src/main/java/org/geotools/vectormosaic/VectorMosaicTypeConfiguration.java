/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.util.ArrayList;
import java.util.List;

public class VectorMosaicTypeConfiguration {
    private String name;
    private List<VectorMosaicGranule> granules;

    public VectorMosaicTypeConfiguration(String typeName, VectorMosaicGranule vectorMosaicGranule) {
        this.name = typeName;
        this.granules = new ArrayList<>();
        this.granules.add(vectorMosaicGranule);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VectorMosaicGranule> getGranules() {
        return granules;
    }

    public void setGranules(List<VectorMosaicGranule> granules) {
        this.granules = granules;
    }

    public void addGranule(VectorMosaicGranule vectorMosaicGranule) {
        if (granules == null) {
            granules = new ArrayList<>();
        }
        granules.add(vectorMosaicGranule);
    }
}
